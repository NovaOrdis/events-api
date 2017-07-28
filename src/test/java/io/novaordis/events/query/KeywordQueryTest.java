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

package io.novaordis.events.query;

import io.novaordis.events.api.event.GenericTimedEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class KeywordQueryTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Query.fromArguments() -------------------------------------------------------------------------------------------

    @Test
    public void Query_fromArguments() throws Exception {

        List<String> arguments = new ArrayList<>(Collections.singletonList("something"));

        KeywordQuery q = (KeywordQuery)Query.fromArguments(arguments, 0);

        assertTrue(arguments.isEmpty());

        assertNotNull(q);

        assertEquals("something", q.getKeyword());
    }

    // identity --------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        KeywordQuery q = new KeywordQuery("test1");
        assertEquals("test1", q.getKeyword());

        //
        // by default matching is not case sensitive
        //
        assertFalse(q.isMatchingCaseSensitive());
    }

    // selects() -------------------------------------------------------------------------------------------------------

    @Test
    public void selects_NoProperties() throws Exception {

        KeywordQuery q = new KeywordQuery("test1");

        GenericTimedEvent e = new GenericTimedEvent();

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_PropertiesExists_NoMatch() throws Exception {

        KeywordQuery q = new KeywordQuery("blue");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("something", "something else");

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_PropertiesExists_CaseMismatch_DefaultBehaviorIsToMatch() throws Exception {

        KeywordQuery q = new KeywordQuery("blue");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", "Something is Blue here");

        assertTrue(q.selects(e));
    }

    @Test
    public void selects_PropertiesExists_CaseMismatch_ConfiguredForStrictCaseMatching() throws Exception {

        KeywordQuery q = new KeywordQuery("blue");

        q.setMatchingCaseSensitive(true);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", "Something is Blue here");

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_NullValue() throws Exception {

        try {

            new KeywordQuery(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null keyword", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected KeywordQuery getQueryToTest() throws Exception {

        return new KeywordQuery("mock-keyword");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
