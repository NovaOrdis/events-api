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

package io.novaordis.events.api.metric.jmx;

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.AddressImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class JmxMetricDefinitionParser {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxMetricDefinitionParser.class);

    public static final String PROTOCOL = "jmx";
    public static final String PROTOCOL_SEPARATOR = "://";

    // "/something:"
    public static final Pattern OBJECT_NAME_DOMAIN_PATTERN = Pattern.compile("/[a-zA-Z][a-zA-Z_0-9\\.]*:");


    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @param metricSourceAndMetricDefinitionRepresentation a metric definition representation, optionally including
     *                                                      the OS metric source representation.
     */
    public static JmxMetricDefinitionImpl parse(String metricSourceAndMetricDefinitionRepresentation)
            throws MetricDefinitionException, AddressException {

        boolean thisIsAJmxMetric = false;

        int i = metricSourceAndMetricDefinitionRepresentation.indexOf(PROTOCOL_SEPARATOR);

        if (i != -1) {

            String protocol = metricSourceAndMetricDefinitionRepresentation.substring(0, i);

            if (!PROTOCOL.equals(protocol)) {

                log.debug("not a JMX metric definition");
                return null;
            }

            thisIsAJmxMetric = true;
            metricSourceAndMetricDefinitionRepresentation = metricSourceAndMetricDefinitionRepresentation.
                    substring((PROTOCOL + PROTOCOL_SEPARATOR).length());

        }

        //
        // identify the domain name
        //

        Matcher m = OBJECT_NAME_DOMAIN_PATTERN.matcher(metricSourceAndMetricDefinitionRepresentation);

        if (!m.find()) {

            String msg =
                    "no ObjectName domain name identified in the metric definition \"" +
                            metricSourceAndMetricDefinitionRepresentation + "\"";

            if (thisIsAJmxMetric) {

                throw new MetricDefinitionException(msg);

            }
            else {

                log.debug(msg + ", bailing out ...");
                return null;
            }
        }

        //
        // domain name found
        //

        int start = m.start();
        int end = m.end();

        String as = metricSourceAndMetricDefinitionRepresentation.substring(0, start);

        Address jmxBusAddress;

        try {

            jmxBusAddress = new AddressImpl(as);
            jmxBusAddress.setProtocol(JmxBus.PROTOCOL);
        }
        catch(Exception e) {

            String msg = "invalid JMX bus address \"" + as + "\"";

            if (thisIsAJmxMetric) {

                throw new MetricDefinitionException(msg, e);
            }

            log.debug(msg + ", bailing out ...");
            return null;
        }

        String domainName = metricSourceAndMetricDefinitionRepresentation.substring(start + 1, end - 1);
        String restOfObjectNameAndAttribute = metricSourceAndMetricDefinitionRepresentation.substring(end);

        i = restOfObjectNameAndAttribute.lastIndexOf('/');

        if (i == -1) {

            //
            // no attribute name
            //

            String msg = "missing attribute name from \"" + domainName + ":" + restOfObjectNameAndAttribute + "\"";

            if (thisIsAJmxMetric) {

                throw new MetricDefinitionException(msg);
            }

            log.debug(msg + ", bailing out ...");
            return null;
        }

        String attributeName = restOfObjectNameAndAttribute.substring(i + 1);
        String keyValuePairs = restOfObjectNameAndAttribute.substring(0, i);

        JmxMetricDefinitionImpl d;

        try {

            d = new JmxMetricDefinitionImpl(jmxBusAddress, domainName, keyValuePairs, attributeName);
        }
        catch(MetricDefinitionException e) {

            if (thisIsAJmxMetric) {

                throw e;
            }

            log.debug(e.getMessage() + ", bailing out ...");
            return null;
        }

        //
        // DO NOT add the source to the repository, let the upper layer to do it if they want to
        //

        return d;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private JmxMetricDefinitionParser() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
