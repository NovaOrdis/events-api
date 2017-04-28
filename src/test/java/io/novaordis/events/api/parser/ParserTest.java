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

import io.novaordis.events.api.event.Event;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
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

        assertTrue(events.isEmpty());
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

        p.close();

        assertEquals(2L, p.getLineNumber());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    protected abstract Parser getParserToTest() throws Exception;

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
