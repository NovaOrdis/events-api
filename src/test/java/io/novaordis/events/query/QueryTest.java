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

import io.novaordis.events.api.event.Event;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public abstract class QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

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
    }

    @Test
    public void fromArguments_TwoKeywordQueries() throws Exception {

        MixedQuery q = (MixedQuery)Query.fromArguments(new ArrayList<>(Arrays.asList("blue", "red")), 0);

        assertNotNull(q);

        assertTrue(q.getFieldQueries().isEmpty());
        assertEquals(2, q.getKeywordQueries().size());
        assertEquals("blue", q.getKeywordQueries().get(0).getKeyword());
        assertEquals("red", q.getKeywordQueries().get(1).getKeyword());
    }

    @Test
    public void fromArguments_SingleFieldQuery() throws Exception {

        FieldQuery q = (FieldQuery)Query.fromArguments(
                new ArrayList<>(Collections.singletonList("some-field:some-value")), 0);
        assertNotNull(q);
        assertEquals("some-field", q.getFieldName());
        assertEquals("some-value", q.getValue());
    }

    @Test
    public void fromArguments_TwoFieldQueries() throws Exception {

        MixedQuery q = (MixedQuery)Query.fromArguments(new ArrayList<>(Arrays.asList("something:blue", "color:red")), 0);

        assertNotNull(q);

        assertTrue(q.getKeywordQueries().isEmpty());
        assertEquals(2, q.getFieldQueries().size());
        assertEquals("something", q.getFieldQueries().get(0).getFieldName());
        assertEquals("blue", q.getFieldQueries().get(0).getValue());
        assertEquals("color", q.getFieldQueries().get(1).getFieldName());
        assertEquals("red", q.getFieldQueries().get(1).getValue());
    }

    @Test
    public void fromArguments_MixedQuery() throws Exception {

        MixedQuery q = (MixedQuery)Query.fromArguments(new ArrayList<>(Arrays.asList("blue", "color:red")), 0);

        assertNotNull(q);

        assertEquals(1, q.getKeywordQueries().size());
        assertEquals("blue", q.getKeywordQueries().get(0).getKeyword());

        assertEquals(1, q.getFieldQueries().size());
        assertEquals("color", q.getFieldQueries().get(0).getFieldName());
        assertEquals("red", q.getFieldQueries().get(0).getValue());
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

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
