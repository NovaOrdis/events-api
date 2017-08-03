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
public class LongProperty extends PropertyBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public LongProperty(String name) {

        this(name, null);
    }

    public LongProperty(String name, Long value) {

        this(name, value, null);
    }

    public LongProperty(String name, Long value, MeasureUnit mu) {

        super(name, value, mu);
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public Class getType() {
        return Long.class;
    }

    @Override
    public Property fromString(String s) throws IllegalArgumentException {

        try {
            long l = Long.valueOf(s);
            return new LongProperty(getName(), l);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("\"" + s + "\" cannot be converted to an LongProperty value");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Long getLong() {

        return (Long)getValue();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
