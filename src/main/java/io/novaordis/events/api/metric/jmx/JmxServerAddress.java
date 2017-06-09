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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/9/17
 */
public class JmxServerAddress {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL = "jmx";
    public static final String PROTOCOL_SEPARATOR = "://";

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9999;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String username;
    private char[] password;
    private String host;
    private Integer port;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JmxServerAddress() throws JmxException {

        this(DEFAULT_HOST + ":" + DEFAULT_PORT);
    }

    public JmxServerAddress(String s) throws JmxException {

        if (s == null) {

            throw new IllegalArgumentException("null address");
        }

        parse(s);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getHost() {

        return host;
    }

    public int getPort() {

        if (port == null) {

            return DEFAULT_PORT;
        }

        return port;
    }

    public String getUsername() {

        return username;
    }

    public String getLiteral() {

        String s = "";

        if (username != null) {

            s += username + "@";
        }

        s += host;

        if (port != null) {

            s += ":";
            s += port;
        }

        return s;
    }

    @Override
    public boolean equals(Object o)  {

        if (this == o) {

            return true;
        }

        if (!(o instanceof JmxServerAddress)) {

            return false;

        }

        JmxServerAddress that = (JmxServerAddress)o;

        return getLiteral().equals(that.getLiteral());
    }

    @Override
    public int hashCode() {

        return getLiteral().hashCode();
    }

    @Override
    public String toString() {

        return getLiteral();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    char[] getPassword() {

        return password;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse(String address) throws JmxException {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        int i = address.indexOf(PROTOCOL_SEPARATOR);

        if (i != -1) {

            String protocol = address.substring(0, i);
            if (!PROTOCOL.equals(protocol)) {

                throw new JmxException("invalid protocol " + protocol + PROTOCOL_SEPARATOR);
            }

            address = address.substring(i + PROTOCOL_SEPARATOR.length());
        }

        i = address.indexOf('@');
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
