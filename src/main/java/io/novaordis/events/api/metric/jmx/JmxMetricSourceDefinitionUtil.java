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

import io.novaordis.events.api.metric.MetricSourceDefinition;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Static logic to extract additional information from a JMX MetricSourceDefinition.
 *
 * We chose this solution instead of subclassing the io.novaordis.events.api.metric.MetricSourceDefinition/
 * io.novaordis.events.api.metric.MetricSourceDefinitionImpl hierarchy for simplicity. If we noticed that we overuse
 * this pattern, we will refactor.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/20/17
 */
public class JmxMetricSourceDefinitionUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxMetricSourceDefinitionUtil.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Looks for classpath and attempts to infer the type of the JMX bus and the protocol defintion.
     *
     * @param address a JmxAddress
     *
     * @param yamlBasedSourceDefinitionConfigurationMap the map corresponding to the metric source definition, as
     *                                                  produced by the YAML parsers.
     *
     * @exception IllegalArgumentException on null address.
     */
    public static void extractAdditionalConfigurationElements(
            JmxAddress address, Map yamlBasedSourceDefinitionConfigurationMap) {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        if (yamlBasedSourceDefinitionConfigurationMap != null) {

            Object o = yamlBasedSourceDefinitionConfigurationMap.get(MetricSourceDefinition.CLASSPATH_YAML_KEY);

            if (o != null && o instanceof List) {

                List classpathElements = (List)o;

                //noinspection Convert2streamapi
                for(Object e: classpathElements) {

                    if (e instanceof String) {

                        String classpathElement = (String)e;
                        processClasspathElementHeuristics(address, classpathElement);
                    }
                }
            }
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private JmxMetricSourceDefinitionUtil() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Package protected static ----------------------------------------------------------------------------------------

    static void processClasspathElementHeuristics(JmxAddress address, String classpathElement) {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        if (classpathElement == null) {

            return;
        }

        log.debug("processing " + classpathElement);

        String jbossCliClientJarName = "jboss-cli-client.jar";

        int i = classpathElement.indexOf(jbossCliClientJarName);

        if (i != -1) {

            //
            // a JBoss EAP/WildFly JMX bus
            //

            File d = new File(classpathElement.substring(0, i));

            if (!d.isDirectory()) {

                log.warn("the directory in which " + jbossCliClientJarName + " was declared, does not exist: " + d);
                return;
            }

            d = d.getParentFile();

            if (!d.isDirectory()) {

                log.warn("one of the parent directories of " + jbossCliClientJarName + ", does not exist: " + d);
                return;
            }

            File jbossHome = d.getParentFile();

            if (!jbossHome.isDirectory()) {

                log.warn(jbossHome + " is not a valid JBoss home directory");
                return;
            }

            File versionFile = new File(jbossHome, "version.txt");

            if (!versionFile.isFile()) {

                log.warn(versionFile + " does not exist");
            }

            String versionString;

            try {

                versionString = Files.read(versionFile);
            }
            catch(Exception e) {

                log.warn("failed to read JBoss version file " + versionFile, e);
                return;
            }


            if (versionString.contains("Version 6")) {

                //
                // EAP 6
                //

                log.debug("setting JMX service URL protocol as \"" + JmxAddress.EAP6_JMX_SERVICE_URL_PROTOCOL + "\" on JMX address " + address);

                address.setJmxServiceUrlProtocol(JmxAddress.EAP6_JMX_SERVICE_URL_PROTOCOL);
            }
            else {

                log.warn("failed to figure out JBoss version");
            }
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
