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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class EventPropertyTest extends PropertyTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    /**
     * fromString() semantics is not very well defined for EventProperties at the time of the writing, deferring.
     */
    @Test
    public void fromString() throws Exception {

        //
        // noop for the time being
        //
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void value() throws Exception {

        EventProperty p = new EventProperty("test-name", new GenericTimedEvent(1L));

        assertEquals("test-name", p.getName());

        GenericTimedEvent e2 = (GenericTimedEvent)p.getValue();

        assertEquals(1L, e2.getTime().longValue());

        GenericTimedEvent e3 = (GenericTimedEvent)p.getEvent();

        assertEquals(1L, e3.getTime().longValue());

        assertEquals(Event.class, p.getType());
    }

    @Test
    public void externalizeValue_EventProperty() throws Exception {

        EventProperty p = new EventProperty("test-name", new GenericTimedEvent(1L));

        //assertEquals("test-value", p.externalizeValue());

        fail("RETURN HERE");
    }

    @Test
    public void externalizeType_EventProperty() throws Exception {

        EventProperty p = new EventProperty("test-name", new GenericTimedEvent(1L));

        // assertEquals("test-name", sp.externalizeType());

        fail("RETURN HERE");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected EventProperty getPropertyToTest(String name) {

        return new EventProperty(name, new GenericTimedEvent(1L));
    }

    @Override
    protected Event getAppropriateValueForPropertyToTest() {

        return new GenericTimedEvent(2L);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
