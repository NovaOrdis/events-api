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
import io.novaordis.utilities.address.Address;

import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public class MetricSourceDefinitionImpl implements MetricSourceDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String TYPE_YAML_KEY = "type";
    public static final String HOST_YAML_KEY = "host";
    public static final String PORT_YAML_KEY = "port";
    public static final String USERNAME_YAML_KEY = "username";
    public static final String PASSWORD_YAML_KEY = "password";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Address address;
    private String name;
    private MetricSourceType type;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param name may be null
     * @param address Makes an internal copy of the address passed as argument. May not be null. The source type
     *                will be inferred from address, if possible.
     *
     * @see Address#copy()
     */
    public MetricSourceDefinitionImpl(String name, Address address) {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        this.name = name;
        this.address = address.copy();
        this.type = MetricSourceType.fromAddress(address);
    }

    /**
     * This constructor was designed to be used with YAML representations of source metric definitions.
     *
     * @param name the metric source definition name.
     *
     * @param yamlProducedStructure the structure following the source name, as extracted by a Yaml parser.
     *
     * @exception MetricSourceException should contain human readable error messages, as it most likely will be
     * displayed after a configuration file parsing failure.
     */
    public MetricSourceDefinitionImpl(String name, Object yamlProducedStructure) throws MetricSourceException {

        if (name == null) {

            throw new IllegalArgumentException("null source name");
        }

        if (yamlProducedStructure == null) {

            throw new IllegalArgumentException("null source definition representation");
        }

        if (!(yamlProducedStructure instanceof Map)) {

            throw new MetricSourceException("source \"" + name + "\" definition representation is not a map");
        }

        Map m = (Map)yamlProducedStructure;

        this.name = name;

        Object o;

        o = m.get(TYPE_YAML_KEY);

        if (o == null) {

            throw new MetricSourceException("unspecified type for source \"" + name + "\"");
        }
        else {

            if (!(o instanceof String)) {

                throw new MetricSourceException("type not a string");
            }

            this.type = MetricSourceType.fromString((String)o);

            if (this.type == null) {

                throw new MetricSourceException("invalid metric source type \"" + o + "\"");
            }
        }

        if (MetricSourceType.LOCAL_OS.equals(type)) {

            throw new RuntimeException("NOT YET IMPLEMENTED: " + type);
        }

        o = m.get(HOST_YAML_KEY);

        if (o == null) {

            throw new MetricSourceException("missing host name");
        }

        if (!(o instanceof String)) {

            throw new MetricSourceException("host not a string");
        }

        String host = (String)o;

        Integer port = null;

        o = m.get(PORT_YAML_KEY);

        if (o != null) {

            if (!(o instanceof Integer)) {

                throw new MetricSourceException("port not an integer");
            }

            port = (Integer)o;
        }

        String username = null;

        o = m.get(USERNAME_YAML_KEY);

        if (o != null) {

            if (!(o instanceof String)) {

                throw new MetricSourceException("username not a string");
            }

            username = (String)o;
        }

        String password = null;

        o = m.get(PASSWORD_YAML_KEY);

        if (o != null) {

            if (!(o instanceof String)) {

                throw new MetricSourceException("password not a string");
            }

            password = (String)o;
        }

        if (MetricSourceType.JBOSS_CONTROLLER.equals(type)) {

            this.address = new JBossControllerAddress(username, password, host, port);
        }
        else {

            throw new RuntimeException("NOT YET IMPLEMENTED: support for type " + type);
        }
    }

    // MetricSourceDefinition ------------------------------------------------------------------------------------------

    @Override
    public Address getAddress() {

        return address;
    }

    @Override
    public MetricSourceType getType() {

        return type;
    }

    @Override
    public String getName() {

        return name;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String s = "";

        if (name != null) {

            s += name + ": ";
        }

        s += address;

        if (type != null) {

            s += " (" + type + ")";
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
