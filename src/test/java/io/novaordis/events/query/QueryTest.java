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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.parser.QueryOnce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public abstract class QueryTest extends ExpressionElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // selects() and filter() ------------------------------------------------------------------------------------------

    @Test
    public void selects_NullEvent() throws Exception {

        Query q = getQueryToTest();

        try {

            q.selects(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null event", msg);
        }
    }

    @Test
    public void filter_NullEventList() throws Exception {

        Query q = getQueryToTest();

        try {

            q.filter(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null event list", msg);
        }
    }

    @Test
    public void selects_and_filter() throws Exception {

        Query q = getQueryToTest();

        Event e = getEventThatMatchesQuery();
        Event e2 = getEventThatDoesNotMatchQuery();

        if (e != null) {

            assertTrue(q.selects(e));
        }

        if (e2 != null) {

            assertFalse(q.selects(e2));
        }

        List<Event> input = new ArrayList<>();

        int expected = 0;

        if (e != null) {

            input.add(e);
            expected ++;
        }

        if (e2 != null) {

            input.add(e2);
        }

        List<Event> filtered = q.filter(input);

        assertEquals(expected, filtered.size());

        if (expected > 0) {
            assertEquals(e, filtered.get(0));
        }
    }

    // Query Once ------------------------------------------------------------------------------------------------------

    @Test
    public void selects_QueryOnce() throws Exception {

        Query q = getQueryToTest();

        Event doesNotMatch = getEventThatDoesNotMatchQuery();

        QueryOnce.set(doesNotMatch, true);

        //
        // this is a side effect of the QueryOnce, once an event was declared as "matching", it'll match even if it doesn't
        //
        assertTrue(q.selects(doesNotMatch));

        Event doesMatch = getEventThatMatchesQuery();

        QueryOnce.set(doesMatch, true);

        assertTrue(q.selects(doesMatch));
    }

    @Test
    public void filter_QueryOnce() throws Exception {

        Query q = getQueryToTest();

        Event e = getEventThatDoesNotMatchQuery();

        QueryOnce.set(e, true);

        Event e2 = getEventThatMatchesQuery();

        QueryOnce.set(e2, true);

        //
        // this is a side effect of the QueryOnce, once an event was declared as "matching", it'll match even if it doesn't
        //

        List<Event> events = Arrays.asList(e, e2);

        List<Event> events2 = q.filter(events);

        assertEquals(2, events2.size());
        assertEquals(e, events2.get(0));
        assertEquals(e2, events2.get(1));
    }

    // special cases ---------------------------------------------------------------------------------------------------

    /**
     * We simulate the case when the time query keyword "from" conflicts with a field named "from".
     *
     * See Events API TODO Ce3RTy
     */
    // @Test
    public void timedEventContainsAFieldNamedFrom() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(10L);
        e.setStringProperty("from", "San Francisco");

        MixedQuery q = new MixedQuery();
//        q.addExpressionElement(new TimeQuery("from:", 9L));
//        q.addExpressionElement(new FieldQuery("from", "Los Angeles"));

        fail("return here");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected ExpressionElement getExpressionElementToTest() throws Exception {

        return getQueryToTest();
    }

    protected abstract Query getQueryToTest() throws Exception;

    /**
     * May return null if no such event exists.
     */
    protected abstract Event getEventThatMatchesQuery();

    /**
     * May return null if no such event exists.
     */
    protected abstract Event getEventThatDoesNotMatchQuery();

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
