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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/6/16
 */
public class LineEventTest extends GenericEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    /**
     * We override because this event has a line number.
     * @throws Exception
     */
    @Test
    public void lineNumber() throws Exception {

        Event e = getEventToTest();

        assertEquals(1L, e.getLineNumber().longValue());

        e.setProperty(new LongProperty(Event.LINE_PROPERTY_NAME, 7L));

        assertEquals(7L, e.getLineNumber().longValue());
    }


    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        LineEvent le = new LineEvent(7L, "test");
        assertEquals(7L, le.getLineNumber().longValue());
        assertEquals("test", le.get());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected LineEvent getEventToTest() throws Exception {

        return new LineEvent(1, "test");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
