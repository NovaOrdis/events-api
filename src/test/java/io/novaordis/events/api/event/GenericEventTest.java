/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.events.api.event;

import io.novaordis.events.api.measure.MockMeasureUnit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/6/16
 */
public class GenericEventTest extends EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NoArgs() throws Exception {

        GenericEvent e = new GenericEvent();
        assertNull(e.getLineNumber());
        assertTrue(e.getProperties().isEmpty());
    }

    @Test
    public void constructor_LineNumber() throws Exception {

        GenericEvent e = new GenericEvent(7L);
        assertEquals(7L, e.getLineNumber().longValue());

        assertEquals(1, e.getProperties().size());
        LongProperty p = (LongProperty)e.getProperties().get(0);
        assertEquals(7L, p.getLong().longValue());
    }

    @Test
    public void constructor_PropertyList() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        GenericEvent e = new GenericEvent(input);

        assertNull(e.getLineNumber());

        List<Property> result = e.getProperties();

        assertEquals(input.size(), result.size());

        for(int i = 0; i < input.size(); i ++) {

            Property ip = input.get(i);
            Property op = result.get(i);

            assertEquals(ip.getName(), op.getName());
            assertEquals(ip.getValue(), op.getValue());
        }
    }

    @Test
    public void constructor_PropertyList_TimestampAmongThose() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new TimestampProperty(7L));

        //
        // a timestamp property is not allowed among the property passed to a non-timed event constructor, to eliminate
        // confusion
        //

        try {

            new GenericEvent(input);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("timestamp property not allowed on a non-timed event"));
        }
    }

    @Test
    public void constructor_LineNumberAndPropertyList() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        GenericEvent e = new GenericEvent(7L, input);

        assertEquals(7L, e.getLineNumber().longValue());

        List<Property> result = e.getProperties();

        assertEquals(input.size() + 1, result.size());

        for(int i = 1; i < input.size(); i ++) {

            Property ip = input.get(i - 1);
            Property op = result.get(i);

            assertEquals(ip.getName(), op.getName());
            assertEquals(ip.getValue(), op.getValue());
        }
    }

    // setProperty() ---------------------------------------------------------------------------------------------------

    @Test
    public void verifySetPropertyRemembersOrder() throws Exception {

        GenericEvent ge = getEventToTest();

        List<Property> props = ge.getProperties();

        int initialSize = props.size();

        assertNull(ge.setProperty(new StringProperty("X", "val1")));
        assertNull(ge.setProperty(new StringProperty("I", "val2")));
        assertNull(ge.setProperty(new StringProperty("A", "val3")));

        props = ge.getProperties();

        assertEquals(initialSize + 3, props.size());

        assertEquals("X", props.get(initialSize).getName());
        assertEquals("val1", props.get(initialSize).getValue());

        assertEquals("I", props.get(initialSize + 1).getName());
        assertEquals("val2", props.get(initialSize + 1).getValue());

        assertEquals("A", props.get(initialSize + 2).getName());
        assertEquals("val3", props.get(initialSize + 2).getValue());
    }

    @Test
    public void setStringProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        assertNull(ge.getStringProperty("test-property"));

        ge.setStringProperty("test-property", "value1");
        assertEquals("value1", ge.getStringProperty("test-property").getValue());

        StringProperty sp = (StringProperty)ge.getProperty("test-property");
        assertEquals("test-property", sp.getName());
        assertEquals("value1", sp.getValue());
        assertNull(sp.getMeasureUnit());

        ge.setStringProperty("test-property", "value2");
        assertEquals("value2", ge.getStringProperty("test-property").getValue());

        StringProperty sp2 = (StringProperty)ge.getProperty("test-property");
        assertEquals("test-property", sp2.getName());
        assertEquals("value2", sp2.getValue());

        ge.setStringProperty("test-property", null);
        assertNull(ge.getProperty("test-property"));
    }

    @Test
    public void removeStringProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        ge.setStringProperty("test-name", "test-value");
        assertEquals("test-value", ge.getStringProperty("test-name").getString());

        ge.removeStringProperty("test-name");
        assertNull(ge.getStringProperty("test-name"));

        ge.removeStringProperty("test-name");
    }

    @Test
    public void setStringProperty_Null() throws Exception {

        GenericEvent ge = getEventToTest();

        ge.setStringProperty("test-name", "test-value");
        assertEquals("test-value", ge.getStringProperty("test-name").getString());

        ge.setStringProperty("test-name", null);
        assertNull(ge.getStringProperty("test-name"));

        ge.setStringProperty("test-name", null);
    }

    @Test
    public void setLongProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        assertNull(ge.getLongProperty("test-property"));

        ge.setLongProperty("test-property", 7L);
        assertEquals(7L, ge.getLongProperty("test-property").getValue());

        LongProperty p = (LongProperty)ge.getProperty("test-property");
        assertEquals("test-property", p.getName());
        assertEquals(7L, p.getValue());
        assertNull(p.getMeasureUnit());

        ge.setLongProperty("test-property", 8L);
        assertEquals(8L, ge.getLongProperty("test-property").getValue());

        LongProperty p2 = (LongProperty)ge.getProperty("test-property");
        assertEquals("test-property", p2.getName());
        assertEquals(8L, p2.getValue());

        MockMeasureUnit mmu = new MockMeasureUnit();
        ge.setLongProperty("test-property", 9L, mmu);
        LongProperty p3 = (LongProperty)ge.getProperty("test-property");
        assertEquals("test-property", p3.getName());
        assertEquals(9L, p3.getValue());
        assertEquals(mmu, p3.getMeasureUnit());
    }

    @Test
    public void removeLongProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        ge.setLongProperty("test-name", 1L);
        assertEquals(1L, ge.getLongProperty("test-name").getLong().longValue());

        ge.removeLongProperty("test-name");
        assertNull(ge.getLongProperty("test-name"));

        ge.removeLongProperty("test-name");
    }

    @Test
    public void setIntegerProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        assertNull(ge.getIntegerProperty("test-property"));

        ge.setIntegerProperty("test-property", 7);
        assertEquals(7, ge.getIntegerProperty("test-property").getValue());

        IntegerProperty p = (IntegerProperty)ge.getProperty("test-property");
        assertEquals("test-property", p.getName());
        assertEquals(7, p.getValue());
        assertNull(p.getMeasureUnit());

        ge.setIntegerProperty("test-property", 8);
        assertEquals(8, ge.getIntegerProperty("test-property").getValue());

        IntegerProperty p2 = (IntegerProperty)ge.getProperty("test-property");
        assertEquals("test-property", p2.getName());
        assertEquals(8, p2.getValue());

        MockMeasureUnit mmu = new MockMeasureUnit();
        ge.setIntegerProperty("test-property", 9, mmu);
        IntegerProperty p3 = (IntegerProperty)ge.getProperty("test-property");
        assertEquals("test-property", p3.getName());
        assertEquals(9, p3.getValue());
        assertEquals(mmu, p3.getMeasureUnit());
    }

    @Test
    public void removeIntegerProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        ge.setIntegerProperty("test-name", 1);
        assertEquals(1, ge.getIntegerProperty("test-name").getInteger().intValue());

        ge.removeIntegerProperty("test-name");
        assertNull(ge.getIntegerProperty("test-name"));

        ge.removeIntegerProperty("test-name");
    }

    @Test
    public void setBooleanProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        assertNull(ge.getBooleanProperty("test-property"));

        ge.setBooleanProperty("test-property", true);
        assertEquals(true, ge.getBooleanProperty("test-property").getValue());

        BooleanProperty p = (BooleanProperty)ge.getProperty("test-property");
        assertEquals("test-property", p.getName());
        assertEquals(true, p.getValue());
        assertNull(p.getMeasureUnit());

        ge.setBooleanProperty("test-property", false);
        assertEquals(false, ge.getBooleanProperty("test-property").getValue());

        BooleanProperty p2 = (BooleanProperty)ge.getProperty("test-property");
        assertEquals("test-property", p2.getName());
        assertEquals(false, p2.getValue());
    }

    @Test
    public void removeBooleanProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        assertNull(ge.setBooleanProperty("test-name", true));
        assertTrue(ge.getBooleanProperty("test-name").getBoolean());

        BooleanProperty p = ge.removeBooleanProperty("test-name");
        assertNotNull(p);
        assertEquals("test-name", p.getName());
        assertTrue(p.getBoolean());

        assertNull(ge.getBooleanProperty("test-name"));

        BooleanProperty p2 = ge.removeBooleanProperty("test-name");
        assertNull(p2);
    }

    @Test
    public void setProperty_TimestampProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        try {

            ge.setProperty(new TimestampProperty(7L));
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("timestamp property not allowed on a non-timed event"));
        }
    }

    // setListProperty() -----------------------------------------------------------------------------------------------

    @Test
    public void setListProperty() throws Exception {

        GenericEvent ge = getEventToTest();

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");

        ge.setListProperty("test", list);

        ListProperty p = ge.getListProperty("test");

        assertNotNull(p);

        assertEquals("test", p.getName());

        List list2 = p.getList();

        assertEquals(2, list2.size());

        assertEquals("A", list2.get(0));
        assertEquals("B", list2.get(1));
    }

    // miscellaneous ---------------------------------------------------------------------------------------------------

    @Test
    public void getPreferredRepresentation_ReturnsNullAtThisLevel() throws Exception {

        GenericEvent e = getEventToTest();

        assertNull(e.getPreferredRepresentation("does not matter"));
    }

    // removeProperty() ------------------------------------------------------------------------------------------------

    @Test
    public void removeProperty() throws Exception {

        GenericEvent e = getEventToTest();

        List<Property> originalProperties = e.getProperties();

        Property p = e.removeProperty("something", String.class);
        assertNull(p);

        assertNull(e.setStringProperty("A", "something"));

        Property p2 = e.removeProperty("A", Long.class);

        //
        // won't remove anything, type does not match
        //

        assertNull(p2);

        List<Property> properties = e.getProperties();
        assertEquals(1, properties.size() - originalProperties.size());

        Property p3 = null;

        upper: for(Property pi: properties) {

            for(Property pj: originalProperties) {

                if (pi.equals(pj)) {

                    continue upper;
                }
            }

            p3 = pi;
        }

        assertTrue(p3 instanceof StringProperty);
        assertEquals(p3.getName(), "A");
        assertEquals(p3.getValue(), "something");

        //
        // do remove
        //

        Property p4 = e.removeProperty("A", String.class);
        assertNotNull(p4);
        assertTrue(p4 instanceof StringProperty);
        assertEquals(p4.getName(), "A");
        assertEquals(p4.getValue(), "something");

        List<Property> properties2 = e.getProperties();


        assertEquals(originalProperties.size(), properties2.size());

        for(int i = 0; i < originalProperties.size(); i ++) {

            assertTrue(originalProperties.get(0).equals(properties.get(0)));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected GenericEvent getEventToTest() throws Exception {
        return new GenericEvent();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
