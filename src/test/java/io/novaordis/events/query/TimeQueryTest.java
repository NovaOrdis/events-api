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

import org.junit.Test;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.GenericEvent;
import io.novaordis.events.api.event.GenericTimedEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/23/17
 */
public class TimeQueryTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_KeywordAndPossibleValueString_Null() throws Exception {

        try {

            new TimeQuery(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null keyword"));
        }
    }

    @Test
    public void constructor_KeywordAndPossibleValueString_UnknownTimeQueryKeyword() throws Exception {

        try {

            new TimeQuery("i-am-sure-there-is-no-such-time-query-keyword");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown time query keyword"));
            assertTrue(msg.contains("i-am-sure-there-is-no-such-time-query-keyword"));
        }
    }

    @Test
    public void constructor_Keyword_From_SubsequentArgumentArrives_ValidTimestamp()
            throws Exception {

        TimeQuery q = new TimeQuery("from:");

        assertNull(q.getTimestamp());

        assertTrue(q.offerArgument("12/01/16 01:01:01"));

        //
        // should be a noop
        //
        q.validate();

        assertTrue(q.isFrom());
        assertFalse(q.isTo());

        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:01").getTime(),
                q.getTimestamp().longValue());
    }

    @Test
    public void constructor_Keyword_From_SubsequentArgumentArrives_InvalidTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("from:");

        assertNull(q.getTimestamp());

        try {

            q.offerArgument("blah");

            fail("should throw exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown timestamp format or invalid timestamp"));
            assertTrue(msg.contains("'blah'"));
        }
    }

    @Test
    public void constructor_Keyword_From_SubsequentArgumentDoesNotArrive() throws Exception {

        TimeQuery q = new TimeQuery("from:");

        try {

            q.validate();

            fail("should have thrown exception");

        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }
    }

    @Test
    public void constructor_Keyword_From_InvalidTimestamp() throws Exception {

        try {

            new TimeQuery("from:something that does not make sense");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown timestamp format or invalid timestamp"));
            assertTrue(msg.contains("something that does not make sense"));
        }
    }

    @Test
    public void constructor_Keyword_To_SubsequentArgumentArrives_ValidTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("to:");

        assertNull(q.getTimestamp());

        assertTrue(q.offerArgument("12/01/16 01:01:01"));

        //
        // should be a noop
        //
        q.validate();

        assertFalse(q.isFrom());
        assertTrue(q.isTo());

        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:01").getTime(),
                q.getTimestamp().longValue());
    }

    @Test
    public void constructor_Keyword_To_SubsequentArgumentArrives_InvalidTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("to:");

        assertNull(q.getTimestamp());

        try {

            q.offerArgument("blah");

            fail("should throw exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown timestamp format or invalid timestamp"));
            assertTrue(msg.contains("'blah'"));
        }
    }

    @Test
    public void constructor_Keyword_To_SubsequentArgumentDoesNotArrive() throws Exception {

        TimeQuery q = new TimeQuery("to:");

        try {

            q.validate();

            fail("should have thrown exception");

        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }
    }

    @Test
    public void constructor_Keyword_To_InvalidTimestamp() throws Exception {

        try {

            new TimeQuery("to:something that does not make sense");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown timestamp format or invalid timestamp"));
            assertTrue(msg.contains("something that does not make sense"));
        }
    }

    @Test
    public void constructor_Components() throws Exception {

        TimeQuery q = new TimeQuery("to:", 1L);

        assertTrue(q.isTo());
        assertEquals(1L, q.getTimestamp().longValue());
    }

    @Test
    public void constructor_Components_InvalidKeyword() throws Exception {

        try {

            new TimeQuery("to", 1L);
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown time query keyword"));
        }
    }

    // setTimestamp_String() -------------------------------------------------------------------------------------------

    @Test
    public void setTimestamp_Null() throws Exception {

        TimeQuery q = getQueryToTest();

        try {

            q.setTimestamp(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null timestamp"));
        }
    }

    @Test
    public void setTimestamp_UnsupportedFormat() throws Exception {

        TimeQuery q = getQueryToTest();

        try {

            q.setTimestamp("I am sure there's no such supported timestamp format");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unknown timestamp format"));
            assertTrue(msg.contains("I am sure there's no such supported timestamp format"));
        }
    }

    @Test
    public void setTimestamp() throws Exception {

        TimeQuery q = getQueryToTest();

        String timestamp = "12/01/16 14:00:01";

        q.setTimestamp(timestamp);

        assertEquals(timestamp, TimeQuery.SUPPORTED_FORMATS[0].format(q.getTimestamp()));

        assertEquals(TimeQuery.SUPPORTED_FORMATS[0], q.getFormat());
    }

    // setTimestamp_Long() ---------------------------------------------------------------------------------------------

    @Test
    public void setTimestamp_Long() throws Exception {

        TimeQuery q = getQueryToTest();

        q.setTimestamp(777L);

        assertEquals(777L, q.getTimestamp().longValue());
    }

    // business tests --------------------------------------------------------------------------------------------------

    @Test
    public void selects_from_NonTimedEvents() throws Exception {

        TimeQuery q = new TimeQuery("from:12/01/16 01:01:01");

        //
        // we don't select non-timed events, as we don't have the information necessary to make a decision
        //
        assertFalse(q.selects(new GenericEvent()));
    }

    @Test
    public void selects_from_TimedEventsCarryingNullTime() throws Exception {

        TimeQuery q = new TimeQuery("from:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent((Long)null);

        assertFalse(q.selects(te));
    }

    @Test
    public void selects_from_EventBeforeTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("from:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:00").getTime());

        assertFalse(q.selects(te));
    }

    @Test
    public void selects_from_EventRightOnTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("from:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:01").getTime());

        assertTrue(q.selects(te));
    }

    @Test
    public void selects_from_EventAfterTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("from:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:02").getTime());

        assertTrue(q.selects(te));
    }

    @Test
    public void selects_to_NonTimedEvents() throws Exception {

        TimeQuery q = new TimeQuery("to:12/01/16 01:01:01");

        //
        // we don't select non-timed events, as we don't have the information necessary to make a decision
        //
        assertFalse(q.selects(new GenericEvent()));
    }

    @Test
    public void selects_to_TimedEventsCarryingNullTime() throws Exception {

        TimeQuery q = new TimeQuery("to:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent((Long)null);

        assertFalse(q.selects(te));
    }

    @Test
    public void selects_to_EventBeforeTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("to:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:00").getTime());

        assertTrue(q.selects(te));
    }

    @Test
    public void selects_to_EventRightOnTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("to:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:01").getTime());

        assertTrue(q.selects(te));
    }

    @Test
    public void selects_to_EventAfterTimestamp() throws Exception {

        TimeQuery q = new TimeQuery("to:12/01/16 01:01:01");

        GenericTimedEvent te = new GenericTimedEvent(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 01:01:02").getTime());

        assertFalse(q.selects(te));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    private long testTimestamp = 1000L;

    @Override
    protected TimeQuery getQueryToTest() throws Exception {

        TimeQuery q = new TimeQuery(TimeQuery.FROM_KEYWORD);
        q.setTimestamp(testTimestamp);
        return q;
    }

    @Override
    protected Event getEventThatMatchesQuery() {

        return new GenericTimedEvent(testTimestamp);
    }

    @Override
    protected Event getEventThatDoesNotMatchQuery() {

        return new GenericTimedEvent(testTimestamp - 1);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
