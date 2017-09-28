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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQueryTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void fromArguments() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("red", "blue"));
        MixedQuery q = (MixedQuery)Query.fromArguments(args, 0);

        assertNotNull(q);

        List<KeywordQuery> keywords = q.getKeywordQueries();

        assertEquals(2, keywords.size());
        assertEquals("red", keywords.get(0).getKeyword());
        assertEquals("blue", keywords.get(1).getKeyword());

        assertTrue(args.isEmpty());
    }

    @Test
    public void fromArguments_FieldQuery() throws Exception {

        List<String> args = new ArrayList<>(Collections.singletonList("something:SomethingElse"));
        FieldQuery q = (FieldQuery)Query.fromArguments(args, 0);

        assertNotNull(q);

        assertEquals("something", q.getFieldName());
        assertEquals("SomethingElse", q.getValue());

        assertTrue(args.isEmpty());
    }

    @Test
    public void fromArguments_FieldQueryAndKeywordQuery() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("red", "something:SomethingElse", "blue"));
        MixedQuery q = (MixedQuery)Query.fromArguments(args, 0);

        assertNotNull(q);

        List<KeywordQuery> keywords = q.getKeywordQueries();

        assertEquals(2, keywords.size());
        assertEquals("red", keywords.get(0).getKeyword());
        assertEquals("blue", keywords.get(1).getKeyword());

        List<FieldQuery> fields = q.getFieldQueries();

        assertEquals(1, fields.size());
        assertEquals("something", fields.get(0).getFieldName());
        assertEquals("SomethingElse", fields.get(0).getValue());

        assertTrue(args.isEmpty());
    }

    // addLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void addLiteral_KeywordQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral("something");

        List<KeywordQuery> keywords = q.getKeywordQueries();

        assertEquals(1, keywords.size());
        assertEquals("something", keywords.get(0).getKeyword());
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(keywords.get(0), q.getQueryInitializationOrder().get(0));

        GenericTimedEvent e = new GenericTimedEvent();

        e.setStringProperty("test-key", "blah blah something blah");

        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent();

        e2.setStringProperty("test-key", "blah blah blah");

        assertFalse(q.selects(e2));

        assertTrue(q.getFieldQueries().isEmpty());
    }

    @Test
    public void addLiteral_FieldQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral("something:somethingelse");

        List<FieldQuery> fields = q.getFieldQueries();

        assertEquals(1, fields.size());
        assertEquals("something", fields.get(0).getFieldName());
        assertEquals("somethingelse", fields.get(0).getValue());
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(fields.get(0), q.getQueryInitializationOrder().get(0));

        assertTrue(q.getKeywordQueries().isEmpty());
    }

    @Test
    public void addLiteral_TimeQuery_From() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral(TimeQuery.FROM_KEYWORD);

        List<TimeQuery> timeQueries = q.getTimeQueries();
        assertEquals(1, timeQueries.size());
        TimeQuery tq = timeQueries.get(0);
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(tq, q.getQueryInitializationOrder().get(0));

        try {

            q.validate();

            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }

        q.addLiteral("12/01/16 12:00:00");

        List<TimeQuery> timeQueries2 = q.getTimeQueries();
        assertEquals(1, timeQueries2.size());
        TimeQuery tq2 = timeQueries2.get(0);
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(tq2, q.getQueryInitializationOrder().get(0));
        assertTrue(tq.isFrom());
        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 12:00:00").getTime(),
                tq.getTimestamp().longValue());

        q.validate();
    }

    @Test
    public void addLiteral_TimeQuery_To() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral(TimeQuery.TO_KEYWORD);

        List<TimeQuery> timeQueries = q.getTimeQueries();
        assertEquals(1, timeQueries.size());
        TimeQuery tq = timeQueries.get(0);
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(tq, q.getQueryInitializationOrder().get(0));

        try {

            q.validate();

            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }

        q.addLiteral("12/01/16 12:00:00");

        List<TimeQuery> timeQueries2 = q.getTimeQueries();
        assertEquals(1, timeQueries2.size());
        TimeQuery tq2 = timeQueries2.get(0);
        assertEquals(1, q.getQueryInitializationOrder().size());
        assertEquals(tq2, q.getQueryInitializationOrder().get(0));
        assertTrue(tq.isTo());
        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 12:00:00").getTime(),
                tq.getTimestamp().longValue());

        q.validate();
    }

    // selects() -------------------------------------------------------------------------------------------------------

    @Test
    public void selects_noArgumentsIsNullQuery() throws Exception {

        MixedQuery q = new MixedQuery();
        assertTrue(q.selects(new GenericTimedEvent()));
    }

    @Test
    public void selects_keywordQueryIsCaseInsensitiveByDefault() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral("blah");

        assertFalse(q.isKeywordMatchingCaseSensitive());

        GenericTimedEvent e = new GenericTimedEvent(3L, Collections.singletonList(new StringProperty("key", "blah")));
        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent(4L, Collections.singletonList(new StringProperty("key", "Blah")));
        assertTrue(q.selects(e2));

        GenericTimedEvent e3 = new GenericTimedEvent(5L, Collections.singletonList(new StringProperty("key", "BlaH")));
        assertTrue(q.selects(e3));

        GenericTimedEvent e4 = new GenericTimedEvent(6L, Collections.singletonList(new StringProperty("key", "BLAH")));
        assertTrue(q.selects(e4));

        GenericTimedEvent e5 =
                new GenericTimedEvent(7L, Collections.singletonList(new StringProperty("key", "something")));

        assertFalse(q.selects(e5));
    }

    @Test
    public void selects_OnlyFieldQueries() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral("color:blue");

        GenericTimedEvent e = new GenericTimedEvent(7L, Collections.singletonList(new StringProperty("color", "blue")));
        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent(8L, Collections.singletonList(new StringProperty("color", "red")));
        assertFalse(q.selects(e2));
    }

    @Test
    public void selects_KeywordsAndFieldQueries() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addLiteral("blah");
        q.addLiteral("color:blue");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("something", "something else");
        e.setStringProperty("color", "red");

        assertFalse(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent();
        e2.setStringProperty("something", "something blah else");
        e2.setStringProperty("color", "red");

        assertTrue(q.selects(e2));

        GenericTimedEvent e3 = new GenericTimedEvent();
        e3.setStringProperty("something", "something else");
        e3.setStringProperty("color", "blue");

        assertTrue(q.selects(e3));

        GenericTimedEvent e4 = new GenericTimedEvent();
        e4.setStringProperty("something", "something blah else");
        e4.setStringProperty("color", "blue");

        assertTrue(q.selects(e4));
    }

    // offerArgument ---------------------------------------------------------------------------------------------------

    @Test
    public void offerArgument() throws Exception {

        MixedQuery q = new MixedQuery();

        assertFalse(q.offerArgument("something"));
    }

    // validate() ------------------------------------------------------------------------------------------------------

    @Test
    public void validate() throws Exception {

        FieldQuery f = new FieldQuery("something:somethingelse");
        assertFalse(f.wasValidated());

        KeywordQuery k = new KeywordQuery("something");
        assertFalse(k.wasValidated());

        TimeQuery t = new TimeQuery("from:12/01/16 12:00:00");
        assertFalse(t.wasValidated());

        MixedQuery q = new MixedQuery();

        q.addQuery(t);
        q.addQuery(k);
        q.addQuery(f);

        q.validate();

        assertTrue(f.wasValidated());
        assertTrue(t.wasValidated());
        assertTrue(k.wasValidated());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Query getQueryToTest() throws Exception {

        MixedQuery q = new MixedQuery();
        q.addLiteral("test-field-name:test-value");
        return q;
    }

    @Override
    protected Event getEventThatMatchesQuery() {

        return new GenericEvent(Collections.singletonList(new StringProperty("test-field-name", "test-value")));
    }

    @Override
    protected Event getEventThatDoesNotMatchQuery() {

        return new GenericEvent();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
