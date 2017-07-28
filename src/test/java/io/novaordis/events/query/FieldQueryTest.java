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
        assertEquals("test2", q.getValue());
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
            assertTrue(msg.contains("not a valid FieldQuery literal, empty field value"));
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

        FieldQuery q = new FieldQuery("test1", null);

        GenericTimedEvent e = new GenericTimedEvent();
        e.setStringProperty("test1", null);

        assertFalse(q.selects(e));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected FieldQuery getQueryToTest() throws Exception {

        return new FieldQuery("mock-property", "mock-value");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
