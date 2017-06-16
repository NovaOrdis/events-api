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

import io.novaordis.utilities.address.Address;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public class MetricSourceDefinitionImpl implements MetricSourceDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Address address;
    private String name;
    private MetricSourceType type;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Makes an internal copy of the address passed as argument.
     *
     * @see Address#copy()
     */
    public MetricSourceDefinitionImpl(Address a) {

        if (a == null) {

            throw new IllegalArgumentException("null address");
        }

        this.address = a.copy();
        this.name = null;
        this.type = null;
    }

    /**
     * This constructor was designed to be used with YAML representations of source metric definitions.
     *
     * @param sourceName the metric source definition name.
     *
     * @param yamlProducedStructure the structure following the source name, as extracted by a Yaml parser.
     *
     * @exception MetricSourceException should contain human readable error messages, as it most likely will be
     * displayed after a configuration file parsing failure.
     */
    public MetricSourceDefinitionImpl(String sourceName, Object yamlProducedStructure) throws MetricSourceException {

        if (sourceName == null) {

            throw new IllegalArgumentException("null source name");
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
