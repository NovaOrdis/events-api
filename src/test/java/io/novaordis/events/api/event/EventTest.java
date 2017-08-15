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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/24/16
 */
public abstract class EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(EventTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void properties() throws Exception {

        Event event = getEventToTest();

        Property old = event.setProperty(new StringProperty("test", "test-value"));
        assertNull(old);

        StringProperty p = (StringProperty)event.getProperty("test");
        assertEquals("test", p.getName());
        assertEquals("test-value", p.getString());

        old = event.setProperty(new StringProperty("test", "test-value-2"));
        assertNotNull(old);
        assertEquals("test", old.getName());
        assertEquals("test-value", old.getValue());

        StringProperty p2 = (StringProperty)event.getProperty("test");
        assertEquals("test", p2.getName());
        assertEquals("test-value-2", p2.getString());
    }

    @Test
    public void nullProperty() throws Exception {

        Event event = getEventToTest();

        try {
            event.setProperty(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void mapProperty_MergeContents() throws Exception {

        Event event = getEventToTest();

        assertNull(event.getProperty("test-map-property"));

        MapProperty mp = new MapProperty("test-map-property");
        mp.getMap().put("key1", "value1");

        assertNull(event.setProperty(mp));

        MapProperty mp2 = (MapProperty)event.getProperty("test-map-property");

        assertEquals(mp, mp2);

        Map m = mp2.getMap();
        assertEquals(1, m.size());
        assertEquals("value1", m.get("key1"));

        //
        // verify values are merged
        //

        MapProperty mp3 = new MapProperty("test-map-property");
        mp3.getMap().put("key2", "value2");


        MapProperty mp4 = (MapProperty)event.setProperty(mp3);

        assertEquals(mp2, mp4);

        assertEquals("test-map-property", mp4.getName());
        Map m2 = mp4.getMap();
        assertEquals(2, m2.size());
        assertEquals("value1", m2.get("key1"));
        assertEquals("value2", m2.get("key2"));
    }

    @Test
    public void getStringProperty_NoSuchProperty() throws Exception {

        Event event = getEventToTest();
        assertNull(event.getStringProperty("no-such-string-property"));
    }

    @Test
    public void getStringProperty_PropertyExistsButNotALong() throws Exception {

        Event event = getEventToTest();

        IntegerProperty ip = new IntegerProperty("test-property", 1);
        event.setProperty(ip);

        assertNull(event.getStringProperty("test-property"));
        assertEquals(ip, event.getProperty("test-property"));
    }

    @Test
    public void getStringProperty() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test");

        event.setProperty(sp);
        assertEquals(sp, event.getStringProperty("test-property"));
        assertEquals(sp, event.getProperty("test-property"));
    }

    @Test
    public void getLongProperty_NoSuchProperty() throws Exception {

        Event event = getEventToTest();
        assertNull(event.getLongProperty("no-such-map-property"));
    }

    @Test
    public void getLongProperty_PropertyExistsButNotALong() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test-value");
        event.setProperty(sp);

        assertNull(event.getLongProperty("test-property"));
        assertEquals(sp, event.getProperty("test-property"));
    }

    @Test
    public void getLongProperty() throws Exception {

        Event event = getEventToTest();

        LongProperty lp = new LongProperty("test-property", 1L);

        event.setProperty(lp);
        assertEquals(lp, event.getLongProperty("test-property"));
        assertEquals(lp, event.getProperty("test-property"));
    }

    @Test
    public void getIntegerProperty_NoSuchProperty() throws Exception {

        Event event = getEventToTest();
        assertNull(event.getIntegerProperty("no-such-map-property"));
    }

    @Test
    public void getIntegerProperty_PropertyExistsButNotAnInteger() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test-value");
        event.setProperty(sp);

        assertNull(event.getIntegerProperty("test-property"));
        assertEquals(sp, event.getProperty("test-property"));
    }

    @Test
    public void getIntegerProperty() throws Exception {

        Event event = getEventToTest();

        IntegerProperty ip = new IntegerProperty("test-property", 1);

        event.setProperty(ip);
        assertEquals(ip, event.getIntegerProperty("test-property"));
        assertEquals(ip, event.getProperty("test-property"));
    }

    @Test
    public void getMapProperty_NoSuchProperty() throws Exception {

        Event event = getEventToTest();
        assertNull(event.getMapProperty("no-such-map-property"));
    }

    @Test
    public void getMapProperty_PropertyExistsButNotAMap() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test-value");
        event.setProperty(sp);

        assertNull(event.getMapProperty("test-property"));
        assertEquals(sp, event.getProperty("test-property"));
    }

    @Test
    public void getMapProperty() throws Exception {

        Event event = getEventToTest();

        MapProperty mp = new MapProperty("test-property");

        event.setProperty(mp);
        assertEquals(mp, event.getMapProperty("test-property"));
        assertEquals(mp, event.getProperty("test-property"));
    }

    @Test
    public void getPropertyByName_NullName() throws Exception {

        Event event = getEventToTest();

        try {

            event.getProperty(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null property name", msg);
        }
    }

    // setProperty() ---------------------------------------------------------------------------------------------------

    @Test
    public void setProperty_PreservesOrder() throws Exception {

        Event e = getEventToTest();

        List<Property> originalProperties = e.getProperties();
        int originalPropertyCount = originalProperties.size();

        e.setStringProperty("Z", "something");

        List<Property> properties2 = e.getProperties();

        assertEquals(originalPropertyCount + 1, properties2.size());

        StringProperty p = (StringProperty)properties2.get(originalPropertyCount);

        assertEquals("Z", p.getName());

        e.setStringProperty("A", "something");

        List<Property> properties3 = e.getProperties();

        assertEquals(originalPropertyCount + 2, properties3.size());

        StringProperty p2 = (StringProperty)properties3.get(originalPropertyCount);

        assertEquals("Z", p2.getName());

        StringProperty p3 = (StringProperty)properties3.get(originalPropertyCount + 1);

        assertEquals("A", p3.getName());
    }

    @Test
    public void setProperty_PreservesOrderEvenIfReplacesAnExistingProperty() throws Exception {

        Event e = getEventToTest();

        List<Property> originalProperties = e.getProperties();
        int originalPropertyCount = originalProperties.size();

        e.setStringProperty("Z", "Z value");
        e.setStringProperty("A", "A value");

        List<Property> properties = e.getProperties();

        assertEquals(originalPropertyCount + 2, properties.size());

        StringProperty p = (StringProperty) properties.get(originalPropertyCount);

        assertEquals("Z", p.getName());
        assertEquals("Z value", p.getString());

        StringProperty p2 = (StringProperty) properties.get(originalPropertyCount + 1);

        assertEquals("A", p2.getName());
        assertEquals("A value", p2.getString());

        //
        // replace
        //

        StringProperty sp = e.setStringProperty("Z", "another Z value");
        assertNotNull(sp);
        assertEquals("Z value", sp.getValue());

        List<Property> properties2 = e.getProperties();

        assertEquals(originalPropertyCount + 2, properties2.size());

        StringProperty p3 = (StringProperty) properties2.get(originalPropertyCount);

        assertEquals("Z", p3.getName());
        assertEquals("another Z value", p3.getString());

        StringProperty p4 = (StringProperty) properties2.get(originalPropertyCount + 1);

        assertEquals("A", p4.getName());
        assertEquals("A value", p4.getString());
    }

    @Test
    public void setProperty_ReplacesAnExistingProperties() throws Exception {

        Event e = getEventToTest();

        Property p = e.setStringProperty("Z", "Z value");
        assertNull(p);

        StringProperty p2 = e.setStringProperty("Z", "another Z value");

        assertNotNull(p2);
        assertEquals("Z", p2.getName());
        assertEquals("Z value", p2.getString());

        StringProperty p3 = (StringProperty)e.getProperty("Z");

        assertEquals("Z", p3.getName());
        assertEquals("another Z value", p3.getString());
    }

    // line number -----------------------------------------------------------------------------------------------------

    @Test
    public void lineNumber() throws Exception {

        Event e = getEventToTest();

        assertNull(e.getLineNumber());

        e.setProperty(new LongProperty(Event.LINE_NUMBER_PROPERTY_NAME, 7L));

        assertEquals(7L, e.getLineNumber().longValue());
    }

    @Test
    public void lineNumber_InvalidInternalValue() throws Exception {

        Event e = getEventToTest();

        e.setProperty(new StringProperty(Event.LINE_NUMBER_PROPERTY_NAME, "not a long value"));

        assertNull(e.getLineNumber());
    }

    @Test
    public void lineNumber_NullRemovesIt() throws Exception {

        Event e = getEventToTest();

        e.setLineNumber(78L);
        assertEquals(78L, e.getLineNumber().longValue());

        e.setLineNumber(null);
        assertNull(e.getLineNumber());
    }

    // getProperty() ---------------------------------------------------------------------------------------------------

    @Test
    public  void getProperty_NullName() throws Exception {

        Event event = getEventToTest();

        try {

            event.getProperty(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null property name", msg);
        }
    }

    @Test
    public void getProperty_StringArgumentsImplyGetPropertyByNameSemantics_NoSuchName() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test-value");

        event.setProperty(sp);

        assertNull(event.getProperty("I-am-sure-there-is-no-property-with-this-name"));
    }

    @Test
    public void getProperty_StringArgumentsImplyGetPropertyByNameSemantics() throws Exception {

        Event event = getEventToTest();

        StringProperty sp = new StringProperty("test-property", "test-value");

        event.setProperty(sp);

        Property p = event.getProperty("test-property");

        assertNotNull(p);

        StringProperty sp2 = (StringProperty)p;

        assertEquals("test-property", sp2.getName());
        assertEquals("test-value", sp2.getValue());
    }

    // getProperties() -------------------------------------------------------------------------------------------------

    @Test
    public void getProperties_ReturnsACopyOfTheInternalStorage() throws Exception {

        Event e = getEventToTest();

        List<Property> originalProperties = e.getProperties();

        e.setStringProperty("A", "B");

        List<Property> properties = e.getProperties();

        int lastPropertyIndex = properties.size() - 1;

        assertEquals(properties.size(), originalProperties.size() + 1);
        assertEquals("A", properties.get(lastPropertyIndex).getName());

        properties.clear();

        assertTrue(properties.isEmpty());

        List<Property> properties2 = e.getProperties();

        //
        // makes sure the internal storage was not modified
        //

        assertEquals(properties2.size(), originalProperties.size() + 1);
        assertEquals("A", properties2.get(lastPropertyIndex).getName());

        for(int i = 0; i < lastPropertyIndex; i ++) {

            assertEquals(originalProperties.get(0), properties2.get(0));
        }
    }

    // getProperties(type) ---------------------------------------------------------------------------------------------

    @Test
    public void getProperties_NullType() throws Exception {

        Event e = getEventToTest();


        try {

            e.getProperties(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException iae) {

            String msg = iae.getMessage();
            assertTrue(msg.contains("null type"));
        }
    }

    @Test
    public void getProperties_Type() throws Exception {

        Event e = getEventToTest();

        List<Property> originalProperties = e.getProperties(String.class);

        e.setStringProperty("A", "B");

        List<Property> properties = e.getProperties(String.class);

        int lastPropertyIndex = properties.size() - 1;

        assertEquals(properties.size(), originalProperties.size() + 1);
        assertEquals("A", properties.get(lastPropertyIndex).getName());

        //
        // we make sure a copy of the list is returned, and not the storage itself
        //

        properties.clear();

        assertTrue(properties.isEmpty());

        List<Property> properties2 = e.getProperties(String.class);

        //
        // makes sure the internal storage was not modified
        //

        assertEquals(properties2.size(), originalProperties.size() + 1);
        assertEquals("A", properties2.get(lastPropertyIndex).getName());

        for(int i = 0; i < lastPropertyIndex; i ++) {

            assertEquals(originalProperties.get(0), properties2.get(0));
        }
    }

    @Test
    public void getProperties_Type2() throws Exception {

        Event e = getEventToTest();

        e.clearProperties();

        assertTrue(e.getProperties().isEmpty());

        assertTrue(e.getProperties(String.class).isEmpty());
        assertTrue(e.getProperties(Long.class).isEmpty());
        assertTrue(e.getProperties(Event.class).isEmpty());

        e.setStringProperty("Z", "something");

        List<Property> properties = e.getProperties(String.class);
        assertTrue(e.getProperties(Long.class).isEmpty());
        assertTrue(e.getProperties(Event.class).isEmpty());

        assertEquals(1, properties.size());
        assertEquals("Z", properties.get(0).getName());
        assertEquals("something", properties.get(0).getValue());

        e.setStringProperty("A", "something else");

        List<Property> properties2 = e.getProperties(String.class);
        assertTrue(e.getProperties(Long.class).isEmpty());
        assertTrue(e.getProperties(Event.class).isEmpty());

        assertEquals(2, properties2.size());
        assertEquals("Z", properties2.get(0).getName());
        assertEquals("something", properties2.get(0).getValue());
        assertEquals("A", properties2.get(1).getName());
        assertEquals("something else", properties2.get(1).getValue());
    }

    @Test
    public void getProperties_TypeInheritance() throws Exception {

        Event e = getEventToTest();

        MockEvent me = new MockEvent();
        GenericEvent ge = new GenericEvent();

        e.setEventProperty("mock-property", me);
        e.setEventProperty("generic-property", ge);

        List<Property> properties = e.getProperties(Event.class);

        assertEquals(2, properties.size());
        assertEquals(me, properties.get(0).getValue());
        assertEquals(ge, properties.get(1).getValue());

        List<Property> properties2 = e.getProperties(MockEvent.class);

        assertEquals(1, properties2.size());
        assertEquals(me, properties2.get(0).getValue());

        List<Property> properties3 = e.getProperties(GenericEvent.class);

        // MockEvent is a GenericEvent
        assertEquals(2, properties3.size());
        assertEquals(me, properties3.get(0).getValue());
        assertEquals(ge, properties3.get(1).getValue());
    }

    // getRawRepresentation() ------------------------------------------------------------------------------------------

    @Test
    public void getRawRepresentation() throws Exception {

        Event event = getEventToTest();

        assertNull(event.getRawRepresentation());

        event.setProperty(new StringProperty(Event.RAW_PROPERTY_NAME, "something"));

        String rawRepresentation = event.getRawRepresentation();
        assertEquals("something", rawRepresentation);
    }

    // getRawRepresentation() ------------------------------------------------------------------------------------------

    @Test
    public void getPreferredRepresentation() throws Exception {

        Event event = getEventToTest();

        // we just make sure we don't throw any exception - subclasses may or may not return null
        event.getPreferredRepresentation("does not matter");
    }

    // EventProperty convenience accessors/mutators --------------------------------------------------------------------

    @Test
    public void stringPropertyAccessorsMutators() throws Exception {

        Event e = getEventToTest();

        assertNull(e.getStringProperty("no-such-string-property"));

        assertNull(e.setStringProperty("test-name", "test-value"));

        StringProperty sp = e.getStringProperty("test-name");

        assertEquals("test-name", sp.getName());
        assertEquals(String.class, sp.getType());
        assertEquals("test-value", sp.getString());
        assertNull(sp.getMeasureUnit());
        assertNull(sp.getFormat());

        StringProperty sp2 = e.setStringProperty("test-name", "test-value-2");

        assertEquals("test-name", sp2.getName());
        assertEquals(String.class, sp2.getType());
        assertEquals("test-value", sp2.getString());
        assertNull(sp2.getMeasureUnit());
        assertNull(sp2.getFormat());

        StringProperty sp3 = e.getStringProperty("test-name");

        assertEquals("test-name", sp3.getName());
        assertEquals(String.class, sp3.getType());
        assertEquals("test-value-2", sp3.getString());
        assertNull(sp3.getMeasureUnit());
        assertNull(sp3.getFormat());

        assertNull(e.removeEventProperty("test-name"));

        assertNotNull(e.getStringProperty("test-name"));

        StringProperty sp4 = e.removeStringProperty("test-name");

        assertEquals("test-name", sp4.getName());
        assertEquals(String.class, sp4.getType());
        assertEquals("test-value-2", sp4.getString());
        assertNull(sp4.getMeasureUnit());
        assertNull(sp4.getFormat());

        assertNull(e.getStringProperty("test-name"));

        assertNull(e.removeStringProperty("no-such-string-property"));
    }

    // EventProperty convenience accessors/mutators --------------------------------------------------------------------

    @Test
    public void eventPropertyAccessorsMutators() throws Exception {

        Event e = getEventToTest();

        assertNull(e.getEventProperty("no-such-event-property"));

        assertNull(e.setEventProperty("test-name", new GenericTimedEvent(1L)));

        EventProperty ep = e.getEventProperty("test-name");

        assertEquals("test-name", ep.getName());
        assertEquals(GenericTimedEvent.class, ep.getType());
        assertEquals(1L, ((TimedEvent) ep.getEvent()).getTime().longValue());
        assertNull(ep.getMeasureUnit());
        assertNull(ep.getFormat());

        EventProperty ep2 = e.setEventProperty("test-name", new GenericTimedEvent(2L));

        assertEquals("test-name", ep2.getName());
        assertEquals(GenericTimedEvent.class, ep2.getType());
        assertEquals(1L, ((TimedEvent) ep2.getEvent()).getTime().longValue());
        assertNull(ep2.getMeasureUnit());
        assertNull(ep2.getFormat());

        EventProperty ep3 = e.getEventProperty("test-name");

        assertEquals("test-name", ep3.getName());
        assertEquals(GenericTimedEvent.class, ep3.getType());
        assertEquals(2L, ((TimedEvent) ep3.getEvent()).getTime().longValue());
        assertNull(ep3.getMeasureUnit());
        assertNull(ep3.getFormat());

        assertNull(e.removeStringProperty("test-name"));

        assertNotNull(e.getEventProperty("test-name"));

        EventProperty ep4 = e.removeEventProperty("test-name");

        assertEquals("test-name", ep4.getName());
        assertEquals(GenericTimedEvent.class, ep4.getType());
        assertEquals(2L, ((TimedEvent) ep4.getEvent()).getTime().longValue());
        assertNull(ep4.getMeasureUnit());
        assertNull(ep4.getFormat());

        assertNull(e.getEventProperty("test-name"));

        assertNull(e.removeEventProperty("no-such-event-property"));
    }

    // clearProperties() -----------------------------------------------------------------------------------------------

    @Test
    public void clearProperties() throws Exception {

        Event e = getEventToTest();

        e.setStringProperty("something-that-surely-does-not-exist-yet", "something");

        assertFalse(e.getProperties().isEmpty());

        e.clearProperties();

        assertTrue(e.getProperties().isEmpty());
        assertTrue(e.getProperties(String.class).isEmpty());
        assertTrue(e.getProperties(Integer.class).isEmpty());
        assertTrue(e.getProperties(Long.class).isEmpty());
        assertTrue(e.getProperties(Event.class).isEmpty());
    }

    // removeProperty() ------------------------------------------------------------------------------------------------

    @Test
    public void removeProperty_NullName() throws Exception {

        Event e = getEventToTest();

        try {

            e.removeProperty(null, String.class);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException iae) {

            String msg = iae.getMessage();
            assertTrue(msg.contains("null name"));
        }
    }

    @Test
    public void removeProperty_NullType() throws Exception {

        Event e = getEventToTest();

        try {

            e.removeProperty("something", null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException iae) {

            String msg = iae.getMessage();
            assertTrue(msg.contains("null type"));
        }
    }

    @Test
    public void removeProperty_NoSuchProperty() throws Exception {

        Event e = getEventToTest();
        assertNull(e.removeProperty("no-such-property", String.class));
    }

    @Test
    public void removeProperty_NameMatchesTypeDoesNot() throws Exception {

        Event e = getEventToTest();
        e.setStringProperty("test-property", "something");

        assertNull(e.removeProperty("test-property", Long.class));

        assertEquals("something", e.getStringProperty("test-property").getString());
    }

    @Test
    public void removeProperty_ExistingPropertyHasNoType() throws Exception {

        Event e = getEventToTest();

        List<Property> original = e.getProperties();

        e.setProperty(new UndefinedTypeProperty("something"));

        assertNull(e.removeProperty("something", String.class));

        List<Property> properties = e.getProperties();
        assertEquals(original.size() + 1, properties.size());
        Property p = properties.get(original.size());
        assertEquals("something", p.getName());
        assertNull(p.getType());
    }

    @Test
    public void removeProperty() throws Exception {

        Event e = getEventToTest();

        e.setStringProperty("something", "something else");

        Property p = e.removeProperty("something", String.class);

        assertTrue(e.getProperties().isEmpty());

        assertEquals("something", p.getName());
        assertEquals("something else", p.getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Event getEventToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
