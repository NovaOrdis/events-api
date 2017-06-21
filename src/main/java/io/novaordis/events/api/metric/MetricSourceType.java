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

import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.LocalOSAddress;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public enum MetricSourceType {

    // Constants -------------------------------------------------------------------------------------------------------

    LOCAL_OS("local-os"),
    REMOTE_OS("ssh"),
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
     *
     * @see MetricSourceType#toAddress(String, String, String, Integer)
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

        if (a instanceof JmxAddress) {

            return JMX;
        }

        String protocol = a.getProtocol();

        if ("ssh".equals(protocol)) {

            return REMOTE_OS;
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

    /**
     * Creates an address instance corresponding to this source type.
     *
     * @param username may be null
     * @param password may be null
     * @param host may be null for some types of sources
     * @param port may be null for some types of sources
     *
     * @return a valid address corresponding to this source type.
     *
     * @see MetricSourceType#fromAddress(Address)
     *
     * @exception AddressException in case we run into troubles building the address instance.
     */
    public Address toAddress(String username, String password, String host, Integer port) throws AddressException {

        if (LOCAL_OS.equals(this)) {

            return new LocalOSAddress();
        }
        else if (REMOTE_OS.equals(this)) {

            return new AddressImpl("ssh", username, password, host, port);
        }
        else if (JBOSS_CONTROLLER.equals(this)) {

            return new JBossControllerAddress(username, password, host, port);
        }
        else if (JMX.equals(this)) {

            return new JmxAddress(username, password, host, port);
        }

        throw new RuntimeException("NOT YET IMPLEMENTED for " + this);
    }

}
