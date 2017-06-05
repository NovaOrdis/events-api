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
