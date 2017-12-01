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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public class QueryStaticTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final SimpleDateFormat TEST_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Query.fromArguments() -------------------------------------------------------------------------------------------

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

        assertTrue(q.isCompiled());

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

        assertTrue(q.isCompiled());

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

        assertTrue(q.isCompiled());

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

        assertTrue(q.isCompiled());

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

    // Query.fromArguments() and selects(time) -------------------------------------------------------------------------

    @Test
    public void fromArgument_From_To() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList(
                "from:11/01/17 10:00:00,000",
                "AND",
                "to:11/01/17 11:00:00,000",
                "AND",
                "color:blue"));

        Query q = Query.fromArguments(args, 0);

        assertNotNull(q);

        assertTrue(args.isEmpty());

        long t = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 09:59:59,999").getTime();
        Event b = new GenericTimedEvent(t, new StringProperty("color", "blue"));
        Event r = new GenericTimedEvent(t, new StringProperty("color", "red"));

        assertFalse(q.selects(t));
        assertFalse(q.selects(b));
        assertFalse(q.selects(r));

        long t2 = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 10:00:00,000").getTime();
        Event b2 = new GenericTimedEvent(t2, new StringProperty("color", "blue"));
        Event r2 = new GenericTimedEvent(t2, new StringProperty("color", "red"));

        assertTrue(q.selects(t2));
        assertTrue(q.selects(b2));
        assertFalse(q.selects(r2));

        long t3 = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 10:00:00,001").getTime();
        Event b3 = new GenericTimedEvent(t3, new StringProperty("color", "blue"));
        Event r3 = new GenericTimedEvent(t3, new StringProperty("color", "red"));

        assertTrue(q.selects(t3));
        assertTrue(q.selects(b3));
        assertFalse(q.selects(r3));

        long t4 = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 10:59:59,999").getTime();
        Event b4 = new GenericTimedEvent(t4, new StringProperty("color", "blue"));
        Event r4 = new GenericTimedEvent(t4, new StringProperty("color", "red"));

        assertTrue(q.selects(t4));
        assertTrue(q.selects(b4));
        assertFalse(q.selects(r4));

        long t5 = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 11:00:00,000").getTime();
        Event b5 = new GenericTimedEvent(t5, new StringProperty("color", "blue"));
        Event r5 = new GenericTimedEvent(t5, new StringProperty("color", "red"));

        assertTrue(q.selects(t5));
        assertTrue(q.selects(b5));
        assertFalse(q.selects(r5));

        long t6 = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS").parse("11/01/17 11:00:00,001").getTime();
        Event b6 = new GenericTimedEvent(t6, new StringProperty("color", "blue"));
        Event r6 = new GenericTimedEvent(t6, new StringProperty("color", "red"));

        assertFalse(q.selects(t6));
        assertFalse(q.selects(b6));
        assertFalse(q.selects(r6));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
