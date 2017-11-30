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
import static org.junit.Assert.assertNull;
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

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void negate_ProducesADifferentInstance() throws Exception {

        try {

            new MixedQuery().negate();

            fail("If you see this, remove MixedQueryTest.negate_ProducesADifferentInstance() and activate the superclass override");
        }
        catch(RuntimeException e) {

            assertEquals("negate() NOT YET IMPLEMENTED", e.getMessage());
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void fromArguments() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("red", "blue"));

        MixedQuery q = (MixedQuery)Query.fromArguments(args, 0);

        assertNotNull(q);

        assertTrue(args.isEmpty());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());

        Query[] orQueries = q.getOrQueries();
        assertEquals(2, orQueries.length);
        assertEquals("red", ((KeywordQuery)orQueries[0]).getKeyword());
        assertEquals("blue", ((KeywordQuery)orQueries[1]).getKeyword());

        Event red = new GenericEvent(new StringProperty("label", "this is something red in color"));
        Event blue = new GenericEvent(new StringProperty("label", "this is something blue in color"));
        Event green = new GenericEvent(new StringProperty("label", "this is something green in color"));

        assertTrue(q.selects(red));
        assertTrue(q.selects(blue));
        assertFalse(q.selects(green));
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

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());

        Query[] orQueries = q.getOrQueries();
        assertEquals(3, orQueries.length);

        assertEquals("red", ((KeywordQuery) orQueries[0]).getKeyword());

        assertEquals("something", ((FieldQuery) orQueries[1]).getFieldName());
        assertEquals("SomethingElse", ((FieldQuery) orQueries[1]).getValue());

        assertEquals("blue", ((KeywordQuery) orQueries[2]).getKeyword());

        assertTrue(args.isEmpty());
    }

    // addExpressionElementLiteral() -----------------------------------------------------------------------------------

    @Test
    public void addExpressionElementLiteral_KeywordQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("something");

        assertFalse(q.isCompiled());

        q.compile();

        assertFalse(q.isNullQuery());
        KeywordQuery k = (KeywordQuery)q.getSoleQuery();
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        assertEquals("something", k.getKeyword());

        GenericTimedEvent e = new GenericTimedEvent();

        e.setStringProperty("test-key", "blah blah something blah");

        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent();

        e2.setStringProperty("test-key", "blah blah blah");

        assertFalse(q.selects(e2));
    }

    @Test
    public void addExpressionElementLiteral_FieldQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("something:somethingelse");

        assertFalse(q.isCompiled());

        q.compile();

        assertFalse(q.isNullQuery());
        FieldQuery f = (FieldQuery)q.getSoleQuery();
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        assertEquals("something", f.getFieldName());
        assertEquals("somethingelse", f.getValue());
    }

    @Test
    public void addExpressionElementLiteral_TimeQuery_From() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(TimeQuery.FROM_KEYWORD);

        assertFalse(q.isCompiled());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        try {

            q.compile();

            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }

        q.addExpressionElementLiteral("12/01/16 12:00:00");

        assertFalse(q.isCompiled());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        q.compile();

        assertFalse(q.isNullQuery());
        TimeQuery tq = (TimeQuery)q.getSoleQuery();
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        assertTrue(tq.isFrom());
        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 12:00:00").getTime(),
                tq.getTimestamp().longValue());
    }

    @Test
    public void addExpressionElementLiteral_TimeQuery_To() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(TimeQuery.TO_KEYWORD);

        assertFalse(q.isCompiled());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        try {

            q.compile();

            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing timestamp"));
        }

        q.addExpressionElementLiteral("12/01/16 12:00:00");

        assertFalse(q.isCompiled());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        q.compile();

        assertFalse(q.isNullQuery());
        TimeQuery tq = (TimeQuery)q.getSoleQuery();
        assertNull(q.getAndQueries());
        assertNull(q.getOrQueries());

        assertTrue(tq.isTo());
        assertEquals(
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("12/01/16 12:00:00").getTime(),
                tq.getTimestamp().longValue());
    }

    @Test
    public void addExpressionElementLiteral_QueryWasCompiled() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("something:somethingelse");

        q.compile();

        try {

            q.addExpressionElementLiteral("other:value");

            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("the query was compiled and cannot be modified anymore"));
        }
    }

    // selects() -------------------------------------------------------------------------------------------------------

    @Test
    public void selects_QueryNotCompiled() throws Exception {

        MixedQuery q = new MixedQuery();

        assertFalse(q.isCompiled());

        try {

            q.selects(new GenericTimedEvent());

            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("query not compiled"));
        }
    }

    @Test
    public void selects_noArgumentsIsNullQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.compile();

        assertTrue(q.selects(new GenericTimedEvent()));
    }

    @Test
    public void selects_keywordQueryIsCaseInsensitiveByDefault() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("blah");

        q.compile();

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

        q.addExpressionElementLiteral("color:blue");

        q.compile();

        GenericTimedEvent e = new GenericTimedEvent(7L, Collections.singletonList(new StringProperty("color", "blue")));
        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent(8L, Collections.singletonList(new StringProperty("color", "red")));
        assertFalse(q.selects(e2));
    }

    @Test
    public void selects_KeywordsAndFieldQueries() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("blah");
        q.addExpressionElementLiteral("color:blue");

        q.compile();

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

    /**
     * Events API TODO B8Pny4
     */
    // @Test
    public void selects_KeywordsAndTimeQueries() throws Exception {

        fail("return here");
    }

    /**
     * Events API TODO B8Pny4
     */
    // @Test
    public void selects_FieldAndTimeQueries() throws Exception {

        fail("return here");
    }

    @Test
    public void selects_ExpressionQuery() throws Exception {

        List<String> args = new ArrayList<>(Arrays.asList("test:blue", "and", "not", "test:green"));

        Query q = Query.fromArguments(args, 0);

        assertNotNull(q);

        Event e = new GenericEvent(new StringProperty("test", "blue"));

        assertTrue(q.selects(e));

        Event e2 = new GenericEvent(new StringProperty("test", "green, blue"));

        assertFalse(q.selects(e2));
    }

    // offerLexicalToken -----------------------------------------------------------------------------------------------

    @Test
    public void offerLexicalToken() throws Exception {

        MixedQuery q = new MixedQuery();

        assertFalse(q.offerLexicalToken("something"));
    }

    // compile() -------------------------------------------------------------------------------------------------------

    @Test
    public void compile() throws Exception {

        MixedQuery q = new MixedQuery();

        assertFalse(q.isCompiled());

        q.addExpressionElementLiteral("something:somethingelse");
        assertFalse(q.isCompiled());

        q.addExpressionElementLiteral("something");
        assertFalse(q.isCompiled());

        q.addExpressionElementLiteral("from:12/01/16 12:00:00");
        assertFalse(q.isCompiled());

        q.compile();

        assertTrue(q.isCompiled());
    }

    @Test
    public void compile_NothingToNegate() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(Operator.NOT.name());

        assertFalse(q.isCompiled());

        try {

            q.compile();

            fail("should have thrown exception");

        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("the query expression contains an incomplete negation"));
        }
    }

    @Test
    public void compile_Negation() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(Operator.NOT.name());

        q.addExpressionElementLiteral("test:blue");

        assertFalse(q.isCompiled());

        q.compile();

        assertTrue(q.isCompiled());

        Event blue = new GenericEvent(new StringProperty("test", "blue"));

        assertFalse(q.selects(blue));

        Event red = new GenericEvent(new StringProperty("test", "red"));

        assertTrue(q.selects(red));
    }

    @Test
    public void compile_Negation_TwoElements() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral("test:blue");

        q.addExpressionElementLiteral(Operator.NOT.name());

        q.addExpressionElementLiteral("test:red");

        assertFalse(q.isCompiled());

        q.compile();

        assertTrue(q.isCompiled());

        assertFalse(q.isNullQuery());
        assertNull(q.getSoleQuery());
        assertNull(q.getAndQueries());

        Query[] orQueries = q.getOrQueries();

        assertEquals(2, orQueries.length);

        FieldQuery q2 = (FieldQuery)orQueries[1];

        Event blue = new GenericEvent(new StringProperty("test", "blue"));

        assertTrue(q2.selects(blue));

        Event red = new GenericEvent(new StringProperty("test", "red"));

        assertFalse(q2.selects(red));
    }

    @Test
    public void compile_Double_Negation() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(Operator.NOT.name());

        q.addExpressionElementLiteral(Operator.NOT.name());

        q.addExpressionElementLiteral("test:blue");

        assertFalse(q.isCompiled());

        q.compile();

        assertTrue(q.isCompiled());

        Event blue = new GenericEvent(new StringProperty("test", "blue"));

        assertTrue(q.selects(blue));

        Event red = new GenericEvent(new StringProperty("test", "red"));

        assertFalse(q.selects(red));
    }

    @Test
    public void compile_NegateANonNegatableElement() throws Exception {

        MixedQuery q = new MixedQuery();

        q.addExpressionElementLiteral(Operator.NOT.name());

        q.addExpressionElementLiteral(Operator.AND.name());

        assertFalse(q.isCompiled());

        try {

            q.compile();

            fail("should have thrown exception");

        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid query expression syntax:"));
            assertTrue(msg.contains("negation followed by AND"));
        }
    }

    // simplify() ------------------------------------------------------------------------------------------------------

    @Test
    public void simplify_NotCompiled() throws Exception {

        MixedQuery q = new MixedQuery();

        try {

            q.simplify();

            fail("should have thrown exception");
        }
        catch (IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("query not compiled yet"));
        }
    }

    @Test
    public void simplify_NullQuery() throws Exception {

        MixedQuery q = new MixedQuery();

        q.compile();

        Query q2 = q.simplify();

        assertTrue(q2 instanceof NullQuery);
    }

    @Test
    public void simplify_OneSubQuery() throws Exception {

        MixedQuery q = new MixedQuery();
        q.addExpressionElementLiteral("test:blue");
        q.compile();

        FieldQuery q2 = (FieldQuery)q.simplify();

        assertEquals("test", q2.getFieldName());
        assertEquals("blue", q2.getValue());
    }

    @Test
    public void simplify_MultipleSubQueries() throws Exception {

        MixedQuery q = new MixedQuery();
        q.addExpressionElementLiteral("test:blue");
        q.addExpressionElementLiteral("test:red");

        q.compile();

        Query sq = q.simplify();

        //
        // can't simplify
        //
        assertEquals(sq, q);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Query getQueryToTest() throws Exception {

        MixedQuery q = new MixedQuery();
        q.addExpressionElementLiteral("test-field-name:test-value");
        q.compile();
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
