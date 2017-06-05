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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.MetricSourceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a JMX bus of a local or remote JVM.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBus extends MetricSourceBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9999;

    private static final Logger log = LoggerFactory.getLogger(JmxBus.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String username;
    private char[] password;
    private String host;
    private int port;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxBus() throws JmxException {

        this(DEFAULT_HOST + ":" + DEFAULT_PORT);
    }

    public JmxBus(String address) throws JmxException  {

        parseAddress(address);

        log.debug(this + " constructed");
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        throw new RuntimeException("not yet implemented");
    }

    @Override
    public String getAddress() {

        String s = "";

        if (username != null) {

            s += username + "@";
        }

        s += host + ":" + port;

        return s;
    }

    @Override
    public boolean hasAddress(String address) {

        return getAddress().equals(address);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getHost() {

        return host;
    }

    public int getPort() {

        return port;
    }

    public String getUsername() {

        return username;
    }

    @Override
    public int hashCode() {

        return getAddress().hashCode();
    }

    @Override
    public String toString() {

        return getAddress();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    char[] getPassword() {

        return password;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected List<Property> collect(List<String> metricDefinitions) throws MetricException {
        throw new RuntimeException("collect() NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void parseAddress(String address) throws JmxException {

        int i = address.indexOf('@');
        String hostAndPort;

        if (i != -1) {

            //
            // username and password
            //

            String usernameAndPassword = address.substring(0, i);
            hostAndPort = address.substring(i + 1);

            i = usernameAndPassword.indexOf(':');

            if (i == -1) {

                throw new JmxException("missing password");
            }

            this.username = usernameAndPassword.substring(0, i);
            this.password = usernameAndPassword.substring(i + 1).toCharArray();
        }
        else {

            hostAndPort = address;
        }

        i = hostAndPort.indexOf(':');

        if (i == -1) {

            //
            // no port
            //

            host = hostAndPort;
            port = DEFAULT_PORT;
        }
        else {

            host = hostAndPort.substring(0, i);

            String s = hostAndPort.substring(i + 1);

            try {

                port = Integer.parseInt(s);
            }
            catch(Exception e) {

                throw new JmxException("invalid port \"" + s + "\"");
            }
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
