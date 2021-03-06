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

package io.novaordis.events.api.event;

import java.text.SimpleDateFormat;

/**
 * A dedicated timestamp property that externalizes timestamp outside events.
 *
 * It's type is Long.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class TimestampProperty extends PropertyBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public TimestampProperty(Long time) {

        this(TimedEvent.TIME_PROPERTY_NAME, time, null);
    }

    public TimestampProperty(String name, Long time) {

        this(name, time, null);
    }

    public TimestampProperty(String name, Long time, SimpleDateFormat format) {

        super(name, time, format);
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public Class getType() {
        return Long.class;
    }

    @Override
    public Property fromString(String s) throws IllegalArgumentException {

        try {

            long l = Long.parseLong(s);
            return new TimestampProperty(l);

        }
        catch(NumberFormatException e) {

            throw new IllegalArgumentException(e);
        }
    }

    // PropertyBase overrides ------------------------------------------------------------------------------------------

    /**
     * Exposed setName() publicly, because we may need to "normalize" the property name after creation.
     */
    @Override
    public void setName(String name) {

        super.setName(name);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
