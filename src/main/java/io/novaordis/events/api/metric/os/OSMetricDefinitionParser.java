/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.MetricSourceRepository;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.OSAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;

/**
 * The logic that parses the string representation of a metric definition, which optionally may include the metric
 * source definition, and creates the corresponding MetricDefinition instance:
 *
 *          [metric-source-representation:]<metric-definition>
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class OSMetricDefinitionParser {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSMetricDefinitionParser.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @param metricSourceAndMetricDefinitionRepresentation a metric definition representation, optionally including
     *                                                      the OS metric source representation.
     */
    public static MetricDefinition parse(MetricSourceRepository repository,
                                         String metricSourceAndMetricDefinitionRepresentation)
            throws MetricDefinitionException {

        int sourceSeparatorIndex = metricSourceAndMetricDefinitionRepresentation.lastIndexOf('/');

        String metricDefinition;
        String metricSourceAddress = null;
        boolean local = true;

        if (sourceSeparatorIndex != -1) {

            //
            // OS metric source candidate
            //
            local = false;
            metricSourceAddress = metricSourceAndMetricDefinitionRepresentation.substring(0, sourceSeparatorIndex);
            metricDefinition = metricSourceAndMetricDefinitionRepresentation.substring(sourceSeparatorIndex + 1);
        }
        else {

            //
            // no metric source, defaults to LocalOS, but we don't create the instance just yet, we may not need
            // it if the metric parsing fails.
            //

            metricDefinition = metricSourceAndMetricDefinitionRepresentation;
        }

        //
        // TODO naive implementation, come up with something better
        //

        String[] packages = {

                "io.novaordis.events.api.metric.os.mdefs",
                "io.novaordis.events.api.metric",
        };

        String fqcn;
        Class<MetricSource> c = null;

        for(String p : packages) {

            fqcn = p + "." + metricDefinition;

            try {

                //noinspection unchecked
                c = (Class<MetricSource>) Class.forName(fqcn);

                if (c != null) {
                    break;
                }
            }
            catch (Exception e) {

                log.debug("no such metric implementation: " + fqcn);
            }
        }

        if (c == null) {

            //
            // no implementation found
            //

            log.debug("no known OS metric \"" + metricDefinition + "\"");
            return null;
        }

        //
        // metric definition resolved, figure out the source, by looking into repository first and creating the source
        // if necessary
        //

        MetricSource metricSource;

        try {

            if (local) {

                Set<LocalOS> sources =
                        repository == null ? Collections.emptySet() : repository.getSources(LocalOS.class);

                if (sources.isEmpty()) {

                    metricSource = new LocalOS();
                }
                else {

                    metricSource = sources.iterator().next();
                }
            }
            else {

                Address address = new AddressImpl(metricSourceAddress);

                metricSource =  repository == null ? null : repository.getSource(RemoteOS.class, address);

                if (metricSource == null) {

                    metricSource = new RemoteOS(metricSourceAddress);
                }
            }
        }
        catch(Exception e) {

            throw new MetricDefinitionException("failed to instantiate metric source", e);
        }

        MetricDefinition md;

        try {

            Constructor<MetricSource> constructor = c.getConstructor(OSAddress.class);
            md = (MetricDefinition)constructor.newInstance(metricSource.getAddress());

        }
        catch(Exception e) {

            throw new MetricDefinitionException("failed to instantiate metric \"" + metricDefinition + "\"", e);
        }

        //
        // DO NOT add the source to the repository, let the upper layer to do it if they want to
        //

        return md;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private OSMetricDefinitionParser() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
