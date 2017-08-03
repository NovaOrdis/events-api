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

package io.novaordis.events.api.event;

import io.novaordis.events.api.measure.MeasureUnit;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class StringProperty extends PropertyBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public StringProperty(String name) {

        this(name, null);
    }

    public StringProperty(String name, String value) {

        this(name, value, null);
    }

    public StringProperty(String name, String value, MeasureUnit mu) {

        super(name, value, mu);
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public Class getType() {

        return String.class;
    }

    @Override
    public StringProperty fromString(String s) {
        return new StringProperty(getName(), s);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getString() {

        return (String)getValue();
    }

    @Override
    public String toString() {

        String value = getString();

        if (value == null) {
            return getName();
        }

        return getName() + "=" + "\"" + value + "\"";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
