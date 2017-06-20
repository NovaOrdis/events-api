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
 * Encapsulates the information that is known about a certain data source, most importantly the Address, and optionally
 * the name, etc.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public interface MetricSourceDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // YAML configuration elements
    //

    String TYPE_YAML_KEY = "type";
    String HOST_YAML_KEY = "host";
    String PORT_YAML_KEY = "port";
    String USERNAME_YAML_KEY = "username";
    String PASSWORD_YAML_KEY = "password";
    String CLASSPATH_YAML_KEY = "classpath";

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    Address getAddress();

    /**
     * The type is optional, may return null.
     */
    MetricSourceType getType();

    /**
     * The name is optional, may return null. The Address instance is fully capable of uniquely identifying a
     * metric source.
     */
    String getName();

}
