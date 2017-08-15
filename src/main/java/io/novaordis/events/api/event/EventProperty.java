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

/**
 * Support for hierarchical events.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class EventProperty extends PropertyBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public EventProperty(String name) {

        this(name, null);
    }

    public EventProperty(String name, Event value) {

        super(name, value);
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public Class getType() {

        Event e = getEvent();

        if (e != null) {

            return e.getClass();
        }

        return Event.class;
    }

    @Override
    public EventProperty fromString(String s) {

        throw new RuntimeException("NYE");
    }

    @Override
    public String toString() {

        if (value == null) {

            return getName();
        }

        return getName() + "=" + "\"" + value + "\"";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Event getEvent() {

        return (Event)getValue();
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
