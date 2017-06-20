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

import io.novaordis.jmx.JmxAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private JmxMetricSourceDefinitionUtil() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
