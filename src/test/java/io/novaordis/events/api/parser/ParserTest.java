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

package io.novaordis.events.api.parser;

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public abstract class ParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void closeAnUnusedParser() throws Exception {

        //
        // a parser on which no parse() invocation was called has no accumulated events
        //

        Parser p = getParserToTest();

        List<Event> events = p.close();

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof EndOfStreamEvent);

        //
        // redundantly close the parser
        //

        List<Event> events2 = p.close();

        assertTrue(events2.isEmpty());
    }

    @Test
    public void closedParserCannotBeUsed() throws Exception {

        //
        // a parser on which no parse() invocation was called has no accumulated events
        //

        Parser p = getParserToTest();

        p.close();

        try {

            p.parse("something");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("closed"));
        }
    }

    @Test
    public void lineNumber() throws Exception {

        Parser p = getParserToTest();

        assertEquals(0L, p.getLineNumber());

        p.parse("something");

        assertEquals(1L, p.getLineNumber());

        p.parse("something else");

        assertEquals(2L, p.getLineNumber());

        List<Event> events = p.close();

        assertEquals(2L, p.getLineNumber());

        //
        // the last event in the list is EndOfStream
        //

        assertTrue(events.size() > 0);

        for(int i = 0; i < events.size(); i ++) {

            if (i < events.size() - 1) {

                assertFalse(events.get(i) instanceof EndOfStreamEvent);
            }
            else {

                //
                // last one is EndOfStream
                //
                assertTrue(events.get(i) instanceof EndOfStreamEvent);
            }
        }

        assertTrue(p.close().isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    protected abstract Parser getParserToTest() throws Exception;

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
