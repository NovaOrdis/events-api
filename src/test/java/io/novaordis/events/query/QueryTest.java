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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.GenericEvent;
import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.api.parser.QueryOnce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    private static final SimpleDateFormat TEST_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // fromArguments() -------------------------------------------------------------------------------------------------

    @Test
    public void fromArguments_NoArguments() throws Exception {

        Query q = Query.fromArguments(Collections.emptyList(), 0);
        assertNull(q);
    }

    @Test
    public void fromArguments_SingleKeywordQuery() throws Exception {

        KeywordQuery q = (KeywordQuery)Query.fromArguments(new ArrayList<>(Collections.singletonList("blue")), 0);
        assertNotNull(q);
        assertEquals("blue", q.getKeyword());
        assertFalse(q.isCaseSensitive());
        assertTrue(q.isCompiled());
    }

    @Test
    public void fromArguments_TwoKeywordQueries() throws Exception {

        Query q = Query.fromArguments(new ArrayList<>(Arrays.asList("blue", "red")), 0);

        assertNotNull(q);

        Event blue = new GenericEvent(new StringProperty("color", "this is a blue car"));
        Event red = new GenericEvent(new StringProperty("color", "this is a red car"));
        Event green = new GenericEvent(new StringProperty("color", "this is a green car"));

        assertTrue(q.selects(blue));
        assertTrue(q.selects(red));
        assertFalse(q.selects(green));
    }

    @Test
    public void fromArguments_SingleFieldQuery() throws Exception {

        FieldQuery q = (FieldQuery)Query.fromArguments(
                new ArrayList<>(Collections.singletonList("some-field:some-value")), 0);

        assertNotNull(q);
        assertEquals("some-field", q.getFieldName());
        assertEquals("some-value", q.getValue());
        assertTrue(q.isCompiled());
    }

    @Test
    public void fromArguments_TwoFieldQueries() throws Exception {

        Query q = Query.fromArguments(new ArrayList<>(Arrays.asList("size:small", "color:red")), 0);

        assertNotNull(q);

        Event smallRed = new GenericEvent(new StringProperty("size", "small"), new StringProperty("color", "red"));
        Event smallBlue = new GenericEvent(new StringProperty("size", "small"), new StringProperty("color", "blue"));
        Event bigRed = new GenericEvent(new StringProperty("size", "big"), new StringProperty("color", "red"));
        Event bigBlue = new GenericEvent(new StringProperty("size", "big"), new StringProperty("color", "blue"));

        assertTrue(q.selects(smallRed));
        assertTrue(q.selects(smallBlue));
        assertTrue(q.selects(bigRed));
        assertFalse(q.selects(bigBlue));
    }

    @Test
    public void fromArguments_MixedQuery() throws Exception {

        Query q = Query.fromArguments(new ArrayList<>(Arrays.asList("car", "color:red")), 0);

        assertNotNull(q);

        Event redCar = new GenericEvent(
                new StringProperty("color", "red"), new StringProperty("description", "this is a car"));
        Event blueCar = new GenericEvent(
                new StringProperty("color", "blue"), new StringProperty("description", "this is a car"));
        Event redPlane = new GenericEvent(
                new StringProperty("color", "red"), new StringProperty("description", "this is a plane"));
        Event bluePlane = new GenericEvent(
                new StringProperty("color", "blue"), new StringProperty("description", "this is a plane"));

        assertTrue(q.selects(redCar));
        assertTrue(q.selects(blueCar));
        assertTrue(q.selects(redPlane));
        assertFalse(q.selects(bluePlane));
    }

    @Test
    public void fromArguments_CaseSensitive() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("--case-sensitive", "red"));

        KeywordQuery q = (KeywordQuery)Query.fromArguments(args, 0);

        assertNotNull(q);
        assertTrue(q.isCompiled());

        assertTrue(q.isCaseSensitive());
    }

    @Test
    public void fromArguments_CaseSensitive2() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("red", "--case-sensitive"));

        KeywordQuery q = (KeywordQuery)Query.fromArguments(args, 0);

        assertNotNull(q);

        assertTrue(q.isCaseSensitive());

        assertTrue(args.isEmpty());
    }

    @Test
    public void fromArguments_TimeQuery_From_SeparatedFromTimestamp() throws Exception {

        List<String> args = new ArrayList<>(Collections.singletonList("from:01/01/16 12:00:00"));

        Query q = Query.fromArguments(args, 0);

        assertTrue(args.isEmpty());

        assertNotNull(q);

        assertTrue(((QueryBase)q).isCompiled());

        TimedEvent e = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 11:59:59").getTime());
        TimedEvent e2 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:00").getTime());
        TimedEvent e3 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:01").getTime());

        assertFalse(q.selects(e));
        assertTrue(q.selects(e2));
        assertTrue(q.selects(e3));
    }

    @Test
    public void fromArguments_TimeQuery_From_AdjacentToTimestamp() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("from:", "01/01/16 12:00:00"));

        Query q = Query.fromArguments(args, 0);

        assertTrue(args.isEmpty());

        assertNotNull(q);

        assertTrue(((QueryBase) q).isCompiled());

        TimedEvent e = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 11:59:59").getTime());
        TimedEvent e2 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:00").getTime());
        TimedEvent e3 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:01").getTime());

        assertFalse(q.selects(e));
        assertTrue(q.selects(e2));
        assertTrue(q.selects(e3));
    }

    @Test
    public void fromArguments_TimeQuery_To_SeparatedFromTimestamp() throws Exception {

        List<String> args = new ArrayList<>(Collections.singletonList("to:01/01/16 12:00:00"));

        Query q = Query.fromArguments(args, 0);

        assertTrue(args.isEmpty());

        assertNotNull(q);

        assertTrue(((QueryBase) q).isCompiled());

        TimedEvent e = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 11:59:59").getTime());
        TimedEvent e2 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:00").getTime());
        TimedEvent e3 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:01").getTime());

        assertTrue(q.selects(e));
        assertTrue(q.selects(e2));
        assertFalse(q.selects(e3));
    }

    @Test
    public void fromArguments_TimeQuery_To_AdjacentToTimestamp() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("to:", "01/01/16 12:00:00"));

        Query q = Query.fromArguments(args, 0);

        assertTrue(args.isEmpty());

        assertNotNull(q);

        assertTrue(((QueryBase) q).isCompiled());

        TimedEvent e = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 11:59:59").getTime());
        TimedEvent e2 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:00").getTime());
        TimedEvent e3 = new GenericTimedEvent(TEST_FORMAT.parse("01/01/16 12:00:01").getTime());

        assertTrue(q.selects(e));
        assertTrue(q.selects(e2));
        assertFalse(q.selects(e3));
    }

    @Test
    public void fromArgument_Expression() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList(
                "stack:mssql",
                "and",
                "not",
                "stack:Object.wait("));

        Query q = Query.fromArguments(args, 0);

        assertTrue(args.isEmpty());

        assertNotNull(q);
    }

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
