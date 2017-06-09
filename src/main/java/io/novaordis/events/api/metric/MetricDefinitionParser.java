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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.metric.jboss.JBossCliMetricDefinitionParser;
import io.novaordis.events.api.metric.jmx.JmxMetricDefinitionParser;
import io.novaordis.events.api.metric.os.OSMetricDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Top-level metric definition parser.
 *
 * It parses the string representation of a metric definition including optionally the metric source:
 *
 * [metric-source-representation:]<metric-definition>
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class MetricDefinitionParser {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MetricDefinitionParser.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Parse the metric definition representation, which may optionally include the metric source representation, and
     * return the associated MetricDefinition instance. If the metric definition contains the metric source, and
     * the repository is not null, it will be queried in an attempt to retrieve the metric source from it, if it exists
     * in the repository. If the metric is found, it will be used and no new metric source instance will be created.
     * If it does not exist, a new metric source instance will be created, but it will NOT be placed in the repository.
     * It is up to the caller whether to place the source in the repository or not.
     *
     * @param repository the source repository that will be queried before a specific metric source is created. May
     *                   be null.
     *
     * @param metricSourceAndMetricDefinitionRepresentation a metric definition representation, optionally including
     *                                                      a metric source representation.
     */
    public static MetricDefinition parse(MetricSourceRepository repository,
                                  String metricSourceAndMetricDefinitionRepresentation)
            throws MetricDefinitionException {


        log.debug("parsing metric definition \"" + metricSourceAndMetricDefinitionRepresentation + "\"");

        //
        // try, in order, all known metric parsers ...
        //

        //
        // assume we're an OS metric ...
        //

        MetricDefinition d = OSMetricDefinitionParser.parse(repository, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            return d;
        }

        //
        // ... then assume we're a JBoss CLI metric ...
        //

        d = JBossCliMetricDefinitionParser.parse(repository, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            return d;
        }

        //
        // ... then assume we're a JMX metric ...
        //

        d = JmxMetricDefinitionParser.parse(repository, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            return d;
        }

        //
        // ... then give up
        //


        throw new MetricDefinitionException(
                "no known parser can understand \"" + metricSourceAndMetricDefinitionRepresentation + "\"");
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private MetricDefinitionParser() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
