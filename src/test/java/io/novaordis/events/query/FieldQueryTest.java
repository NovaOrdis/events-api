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
 * @since 7/19/17
 */
public class FieldQueryTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Query.fromArguments() -------------------------------------------------------------------------------------------

    @Test
    public void Query_fromArguments() throws Exception {

        List<String> arguments = new ArrayList<>(Collections.singletonList("something:blue"));

        FieldQuery q = (FieldQuery)Query.fromArguments(arguments, 0);

        assertTrue(arguments.isEmpty());

        assertNotNull(q);

        assertEquals("something", q.getFieldName());
        assertEquals("blue", q.getValue());
    }

    // identity --------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test2");

        assertEquals("test1", q.getPropertyName());
        assertEquals("test1", q.getFieldName());
        assertEquals("test2", q.getValue());
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullLiteral() throws Exception {

        try {

            new FieldQuery(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null literal"));
        }
    }

    @Test
    public void constructor_Literal() throws Exception {

        FieldQuery q = new FieldQuery("test1:test2");

        assertEquals("test1", q.getPropertyName());
        assertEquals("test1", q.getFieldName());
        assertEquals("test2", q.getRegularExpressionLiteral());
    }

    @Test
    public void constructor_Literal_InvalidFormat() throws Exception {

        try {

            new FieldQuery("test1");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not a valid FieldQuery literal, missing colon"));
        }
    }

    @Test
    public void constructor_Literal_EmptyFieldName() throws Exception {

        try {

            new FieldQuery(":test1");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not a valid FieldQuery literal, empty field name"));
        }
    }

    @Test
    public void constructor_Literal_EmptyValue() throws Exception {

        try {

            new FieldQuery("test1:");
            fail("should have thrown exception");
        }
        catch(QueryException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not a valid FieldQuery literal, empty regular expression"));
        }
    }

    // selects() -------------------------------------------------------------------------------------------------------

    @Test
    public void selects_NoProperty() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test2");

        GenericTimedEvent e = new GenericTimedEvent();

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_PropertyExists_ValueMismatch() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test2");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", "test3");

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_PropertyExists_CaseMismatch() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", "Test");

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_Match() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test2");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", "test2");

        assertTrue(q.selects(e));
    }

    @Test
    public void selects_NullValue() throws Exception {

        FieldQuery q = new FieldQuery("test1", "test");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", null);

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_NotAStringProperty() throws Exception {

        FieldQuery q = new FieldQuery("test", "1");

        GenericTimedEvent e = new GenericTimedEvent();
        e.setIntegerProperty("test", 1);

        // we don't select, even if the string representation matches
        assertFalse(q.selects(e));
    }

    @Test
    public void selects_RegularExpression_SingleLine() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("name", "blue 1");
        FieldQuery q = new FieldQuery("name", "blue");
        assertTrue(q.selects(e));
    }

    @Test
    public void selects_RegularExpression_SingleLine_NoMatch() throws Exception {

        GenericEvent e = new GenericEvent();
        e.setStringProperty("name", "blue 1");
        FieldQuery q = new FieldQuery("name", "green");

        assertFalse(q.selects(e));
    }

    @Test
    public void selects_RegularExpression_MultiLineTarget() throws Exception {

        String content =
                "\tat sun.misc.Unsafe.park(Native Method)\n" +
                "\t- parking to wait for  <0x00000000c19a4178> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)\n" +
                "\tat java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)\n" +
                "\tat java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)\n" +
                "\tat java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)\n" +
                "\tat java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)\n" +
                "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)\n" +
                "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n" +
                "\tat java.lang.Thread.run(Thread.java:745)";

        GenericEvent e = new GenericEvent();
        e.setStringProperty("stack", content);

        FieldQuery q = new FieldQuery("stack", "take(.*)");

        assertTrue(q.selects(e));
    }

    @Test
    public void selects_RegularExpression_MultiLineTarget_NoMatch() throws Exception {

        String content =
                "\tat sun.misc.Unsafe.park(Native Method)\n" +
                        "\t- parking to wait for  <0x00000000c19a4178> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)\n" +
                        "\tat java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)\n" +
                        "\tat java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)\n" +
                        "\tat java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)\n" +
                        "\tat java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)\n" +
                        "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)\n" +
                        "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n" +
                        "\tat java.lang.Thread.run(Thread.java:745)";

        GenericEvent e = new GenericEvent();
        e.setStringProperty("stack", content);

        FieldQuery q = new FieldQuery("stack", "noSuchMethod(.*)");

        assertFalse(q.selects(e));
    }

    // offerLexicalToken -----------------------------------------------------------------------------------------------

    @Test
    public void offerLexicalToken() throws Exception {

        FieldQuery q = new FieldQuery("something:somethingelse");

        assertFalse(q.offerLexicalToken("blah"));
    }

    // setRegularExpressionLiteral() -----------------------------------------------------------------------------------

    @Test
    public void setRegularExpressionLiteral_Null() throws Exception {

        FieldQuery q = new FieldQuery("test", "test");

        try {

            q.setRegularExpressionLiteral(null);

            fail("should throw exception");
        }
        catch (IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null regular expression"));
        }
    }

    @Test
    public void setRegularExpressionLiteral_Empty() throws Exception {

        FieldQuery q = new FieldQuery("test", "test");

        try {

            q.setRegularExpressionLiteral("");

            fail("should throw exception");
        }
        catch (IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("empty regular expression"));
        }
    }

    @Test
    public void setRegularExpressionLiteral() throws Exception {

        FieldQuery q = new FieldQuery("test", "test");

        q.setRegularExpressionLiteral("^something.*else$");

        assertEquals("^something.*else$", q.getRegularExpressionLiteral());

        Event e = new GenericEvent(Collections.singletonList(new StringProperty("test", "something blah blah else")));

        assertTrue(q.selects(e));
    }

    @Test
    public void setRegularExpressionLiteral_Metacharacter_Parantheses_Match() throws Exception {

        FieldQuery q = new FieldQuery("test", "initial");

        q.setRegularExpressionLiteral("some-function(.*)");

        assertEquals("some-function(.*)", q.getRegularExpressionLiteral());
        assertEquals("some-function\\(.*\\)", q.getJavaRegex());

        Event e = new GenericEvent(Collections.singletonList(
                new StringProperty("test", "MyClass.some-function(blah);")));

        assertTrue(q.selects(e));
    }

    @Test
    public void setRegularExpressionLiteral_Metacharacter_Parantheses_NoMatch() throws Exception {

        FieldQuery q = new FieldQuery("test", "initial");

        q.setRegularExpressionLiteral("some-function(.*)");

        assertEquals("some-function(.*)", q.getRegularExpressionLiteral());
        assertEquals("some-function\\(.*\\)", q.getJavaRegex());

        Event e = new GenericEvent(Collections.singletonList(
                new StringProperty("test", "MyClass.some-function(blah")));

        assertFalse(q.selects(e));
    }

    // negate() --------------------------------------------------------------------------------------------------------

    @Test
    public void negate() throws Exception {

        FieldQuery q = new FieldQuery("test", "blue");

        FieldQuery q2 = q.negate();

        assertFalse(q.equals(q2));

        Event blue = new GenericEvent(new StringProperty("test", "blue"));

        assertTrue(q.selects(blue));
        assertFalse(q2.selects(blue));

        Event red = new GenericEvent(new StringProperty("test", "red"));

        assertFalse(q.selects(red));
        assertTrue(q2.selects(red));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected FieldQuery getQueryToTest() throws Exception {

        return new FieldQuery("mock-property", "mock-value");
    }

    @Override
    protected Event getEventThatMatchesQuery() {

        return new GenericEvent(Collections.singletonList(new StringProperty("mock-property", "mock-value")));
    }

    @Override
    protected Event getEventThatDoesNotMatchQuery() {

        return new GenericEvent();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
