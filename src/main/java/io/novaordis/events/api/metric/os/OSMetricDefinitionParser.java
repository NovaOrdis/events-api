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
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.MetricSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * OS metrics definition parser.
 *
 * It parses the string representation of a metric definition including optionally the metric source:
 *
 * [metric-source-representation:]<metric-definition>
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

                "io.novaordis.events.api.metric.os",
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

        MetricSource metricSource = null;

        if (local) {

            Set<LocalOS> sources = repository.getSources(LocalOS.class);

            if (sources.isEmpty()) {

                metricSource = new LocalOS();
            }
            else {

                metricSource = sources.iterator().next();
            }
        }
        else {

            metricSource = repository.getSource(RemoteOS.class, metricSourceAddress);

            if (metricSource == null) {

                metricSource = new RemoteOS(metricSourceAddress);
            }
        }

        MetricDefinition md;

        try {

            Constructor<MetricSource> constructor = c.getConstructor(MetricSource.class);
            md = (MetricDefinition)constructor.newInstance(metricSource);

        }
        catch(Exception e) {

            throw new MetricDefinitionException("failed to instantiate metric \"" + metricDefinition + "\"", e);
        }

        //
        // add the source, if it already exists, it will be a noop, if it was just created, it will be added
        //

        repository.add(metricSource);
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
