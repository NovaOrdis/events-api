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

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.metric.jboss.JBossDmrMetricDefinitionParser;
import io.novaordis.events.api.metric.jmx.JmxMetricDefinitionParser;
import io.novaordis.events.api.metric.os.OSMetricDefinitionParser;
import io.novaordis.utilities.address.AddressException;
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
     * return the associated MetricDefinition instance.
     *
     * @param metricSourceAndMetricDefinitionRepresentation a metric definition representation, optionally including
     *                                                      a metric source representation.
     */
    public static MetricDefinition parse(
            PropertyFactory propertyFactory, String metricSourceAndMetricDefinitionRepresentation)
            throws MetricDefinitionException, AddressException {


        log.debug("parsing metric definition \"" + metricSourceAndMetricDefinitionRepresentation + "\"");

        //
        // try, in order, all known metric parsers ...
        //

        //
        // assume we're an OS metric ...
        //

        MetricDefinition d =
                OSMetricDefinitionParser.parse(propertyFactory, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            log.debug("OSMetricDefinitionParser successfully parsed " + d);
            return d;
        }

        //
        // ... then assume we're a JBoss CLI metric ...
        //

        d = JBossDmrMetricDefinitionParser.parse(propertyFactory, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            log.debug("JBossDmrMetricDefinitionParser successfully parsed " + d);
            return d;
        }

        //
        // ... then assume we're a JMX metric ...
        //

        d = JmxMetricDefinitionParser.parse(propertyFactory, metricSourceAndMetricDefinitionRepresentation);

        if (d != null) {

            log.debug("JmxMetricDefinitionParser successfully parsed " + d);
            return d;
        }

        //
        // ... then give up
        //

        throw new MetricDefinitionException(
                "no metric definition parser can understand \"" + metricSourceAndMetricDefinitionRepresentation + "\"");
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
