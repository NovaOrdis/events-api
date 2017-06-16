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

import io.novaordis.events.api.metric.jmx.JmxBus;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.LocalOSAddress;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public enum MetricSourceType {

    // Constants -------------------------------------------------------------------------------------------------------

    LOCAL_OS("local-os"),
    JBOSS_CONTROLLER("jboss-controller"),
    JMX("jmx"),
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return null if no know MetricSourceType is identified
     */
    public static MetricSourceType fromString(String s) {

        for(MetricSourceType t: values()) {

            if (t.literal.equals(s)) {
                return t;
            }
        }

        return null;
    }

    /**
     * @return null if no know MetricSourceType is identified
     */
    public static MetricSourceType fromAddress(Address a) {

        if (a == null) {

            return null;
        }

        if (a instanceof LocalOSAddress) {

            return LOCAL_OS;
        }

        if (a instanceof JBossControllerAddress) {

            return JBOSS_CONTROLLER;
        }

        String protocol = a.getProtocol();

        if (JmxBus.PROTOCOL.equals(protocol)) {

            return JMX;
        }

        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;

    // Constructors ----------------------------------------------------------------------------------------------------

    MetricSourceType(String literal) {

        this.literal = literal;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The value used to declare this type in Yaml (and other text) configuration.
     */
    public String getLiteral() {

        return literal;
    }

}
