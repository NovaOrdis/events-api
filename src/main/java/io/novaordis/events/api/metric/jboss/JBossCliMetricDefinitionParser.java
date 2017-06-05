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

package io.novaordis.events.api.metric.jboss;

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.MetricSourceRepository;
import io.novaordis.jboss.cli.JBossCliException;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JBoss controller metrics definition parser.
 *
 * It parses the string representation of a metric definition including optionally the metric source:
 *
 * [metric-source-representation:]<metric-definition>
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class JBossCliMetricDefinitionParser {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossCliMetricDefinitionParser.class);

    public static final String PROTOCOL = "jbosscli";
    public static final String PROTOCOL_SEPARATOR = "://";

    public static final Pattern JBOSS_CLI_PATH_SEGMENT = Pattern.compile(
            "/[a-zA-Z][a-zA-Z0-9_\\-]*=[a-zA-Z][a-zA-Z0-9_\\-\\.]*");

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @param metricSourceAndMetricDefinitionRepresentation a metric definition representation, optionally including
     *                                                      the OS metric source representation.
     */
    public static JBossCliMetricDefinition parse(MetricSourceRepository repository,
                                                 String metricSourceAndMetricDefinitionRepresentation)
            throws MetricDefinitionException {

        String mds;

        MetricSource metricSource;

        boolean thisIsAJBossCliMetric = false;

        if (metricSourceAndMetricDefinitionRepresentation.startsWith("/")) {

            //
            // use the default controller
            //

            metricSource = new JBossController();
            mds = metricSourceAndMetricDefinitionRepresentation;
        }
        else {

            //
            // metric source definition, initialize a non-default controller
            //

            int i = metricSourceAndMetricDefinitionRepresentation.indexOf(PROTOCOL_SEPARATOR);

            if (i != -1) {

                String protocol = metricSourceAndMetricDefinitionRepresentation.substring(0, i);

                if (!PROTOCOL.equals(protocol)) {

                    log.debug
                            (JBossCliMetricDefinitionParser.class.getSimpleName() +
                                    " won't parse " + protocol + PROTOCOL_SEPARATOR + " metric definitions");

                    return null;
                }

                metricSourceAndMetricDefinitionRepresentation =
                        metricSourceAndMetricDefinitionRepresentation.
                        substring(i + PROTOCOL_SEPARATOR.length());

                thisIsAJBossCliMetric = true;
            }

            i = metricSourceAndMetricDefinitionRepresentation.indexOf('/');

            if (i == -1) {

                String msg = "no / found in metric definition";

                if (thisIsAJBossCliMetric) {

                    throw new MetricDefinitionException(msg);
                }
                else {

                    log.debug(msg + ", bailing out");
                    return null;
                }
            }

            mds = metricSourceAndMetricDefinitionRepresentation.substring(i);
            String controllerAddress = metricSourceAndMetricDefinitionRepresentation.substring(0, i);

            JBossControllerAddress ca;

            try {

                ca = JBossControllerAddress.parseAddress(controllerAddress);
            }
            catch(JBossCliException e) {

                String msg = "cannot get a JBoss controller address from \"" + controllerAddress + "\"";

                if (thisIsAJBossCliMetric) {

                    throw new MetricDefinitionException(msg, e);
                }
                else {

                    log.debug(msg + ", bailing out", e);
                    return null;
                }
            }

            metricSource = repository.getSource(JBossController.class, ca.getLiteral());

            if (metricSource == null) {

                metricSource = new JBossController(ca);
            }
        }

        //
        // qualify the metric definition as valid
        //

        Matcher m = JBOSS_CLI_PATH_SEGMENT.matcher(mds);

        CliPath path = null;
        int end = -1;

        while(m.find()) {

            if (path == null) {

                path = new CliPath();
            }

            end = m.end();
            String pe = mds.substring(m.start() + 1, end);
            path.addPathElement(pe);
        }

        if (path == null) {

            String msg = "invalid jboss CLI metric \"" + mds + "\"";

            if (thisIsAJBossCliMetric) {

                throw new MetricDefinitionException(msg);
            }
            else {

                log.debug(msg + ", bailing out");
                return null;
            }
        }

        //
        // CLI path matched, if there is remaining content it represents the attribute name
        //

        String attributeName = mds.substring(end + 1);
        CliAttribute attribute = new CliAttribute(attributeName);

        //
        // add the source, if it already exists, it will be a noop, if it was just created, it will be added
        //

        repository.add(metricSource);

        return new JBossCliMetricDefinition(metricSource, path, attribute);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private JBossCliMetricDefinitionParser() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
