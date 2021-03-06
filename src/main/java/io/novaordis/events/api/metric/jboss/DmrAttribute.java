/*
 * Copyright (c) 2016 Nova Ordis LLC
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

/**
 * A JBoss DMR (Dynamic Model Representation) attribute.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
class DmrAttribute {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;

    // Constructors ----------------------------------------------------------------------------------------------------

    public DmrAttribute(String literal) throws MetricDefinitionException {

        this.name = literal;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name == null ? "null" : name;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
