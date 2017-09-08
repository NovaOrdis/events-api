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

import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public abstract class TimedEventTest extends EventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // clearProperties() -----------------------------------------------------------------------------------------------

    /**
     * Overridden in TimedEventTest, because the behavior is different for timed events.
     */
    @Test
    @Override
    public void clearProperties() throws Exception {

        Event e = getEventToTest();

        e.setStringProperty("something-that-surely-does-not-exist-yet", "something");

        assertFalse(e.getProperties().isEmpty());

        e.clearProperties();

        assertFalse(e.getProperties().isEmpty());

        TimestampProperty tp = (TimestampProperty)e.getProperties().get(0);
        assertNotNull(tp);

        assertTrue(e.getProperties(String.class).isEmpty());
        assertTrue(e.getProperties(Integer.class).isEmpty());
        assertTrue(e.getProperties(Long.class).isEmpty());
        assertTrue(e.getProperties(Event.class).isEmpty());
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // isTimed() -------------------------------------------------------------------------------------------------------

    @Test
    public void isTimed() throws Exception {

        TimedEvent e = getEventToTest();
        assertTrue(e.isTimed());
    }

    // getTime() -------------------------------------------------------------------------------------------------------

    @Test
    public void timestamp_NullTimestamp() throws Exception {

        TimedEvent te = getEventToTest(null);
        assertNull(te.getTime());
    }

    @Test
    public void timestamp() throws Exception {

        TimedEvent te = getEventToTest(1L);
        assertEquals(1L, te.getTime().longValue());
    }

    // getProperty() by name -------------------------------------------------------------------------------------------

    /**
     * The timestamp should be accessible by its conventional property name.
     */
    @Test
    public void getProperty_ByName_timestamp_Null() throws Exception {

        TimedEvent te = getEventToTest(null);

        assertNull(te.getTimestamp());
        assertNull(te.getTime());

        TimestampProperty p = (TimestampProperty)te.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);

        assertNotNull(p);
        assertNull(p.getValue());
    }

    /**
     * The timestamp should be accessible by its conventional property name.
     */
    @Test
    public void getProperty_ByName_timestamp_NotNull() throws Exception {

        TimedEvent te = getEventToTest(125L);

        TimestampProperty p = (TimestampProperty)te.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);

        assertNotNull(p);

        long time = (Long)p.getValue();
        assertEquals(125L, time);
    }

    // getProperty() by index ------------------------------------------------------------------------------------------

    @Test
    public void getProperty_ByIndex_NullTimestamp() throws Exception {

        TimedEvent e = getEventToTest(null);

        //
        // timestamp
        //

        TimestampProperty p = (TimestampProperty)e.getProperty(0);
        assertNull(p.getValue());

        TimestampProperty p2 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertNull(p2.getValue());

        //
        // other properties
        //

        assertNull(e.getProperty(1));

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties = e.getProperties();

        assertEquals(1, properties.size());

        TimestampProperty p3 = (TimestampProperty)e.getProperty(0);
        assertNull(p3.getValue());

        //
        // add properties
        //

        e.setStringProperty("A", "A value");

        //
        // timestamp
        //

        TimestampProperty p4 = (TimestampProperty)e.getProperty(0);
        assertNull(p4.getValue());

        TimestampProperty p5 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertNull(p5.getValue());

        //
        // other properties
        //

        assertEquals("A value", e.getProperty(1).getValue());

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties2 = e.getProperties();

        assertEquals(2, properties2.size());

        TimestampProperty p6 = (TimestampProperty)properties2.get(0);
        assertNull(p6.getValue());

        assertEquals("A value", properties2.get(1).getValue());

        //
        // set timestamp
        //

        e.setTimestamp(new TimestampImpl(7L));

        //
        // timestamp
        //

        TimestampProperty p7 = (TimestampProperty)e.getProperty(0);
        assertEquals(7L, p7.getValue());

        TimestampProperty p8 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertEquals(7L, p8.getValue());

        //
        // other properties
        //

        assertEquals("A value", e.getProperty(1).getValue());

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties3 = e.getProperties();

        assertEquals(2, properties3.size());

        TimestampProperty p9 = (TimestampProperty)properties3.get(0);
        assertEquals(7L, p9.getValue());

        assertEquals("A value", properties3.get(1).getValue());
    }

    @Test
    public void getProperty_ByIndex_NonNullTimestamp() throws Exception {

        TimedEvent e = getEventToTest(8L);

        //
        // timestamp
        //

        TimestampProperty p = (TimestampProperty)e.getProperty(0);
        assertEquals(8L, p.getValue());

        TimestampProperty p2 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertEquals(8L, p2.getValue());

        //
        // other properties
        //

        assertNull(e.getProperty(1));

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties = e.getProperties();

        assertEquals(1, properties.size());

        TimestampProperty p3 = (TimestampProperty)e.getProperty(0);
        assertEquals(8L, p3.getValue());

        //
        // add properties
        //

        e.setStringProperty("A", "A value");

        //
        // timestamp
        //

        TimestampProperty p4 = (TimestampProperty)e.getProperty(0);
        assertEquals(8L, p4.getValue());

        TimestampProperty p5 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertEquals(8L, p5.getValue());

        //
        // other properties
        //

        assertEquals("A value", e.getProperty(1).getValue());

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties2 = e.getProperties();

        assertEquals(2, properties2.size());

        TimestampProperty p6 = (TimestampProperty)properties2.get(0);
        assertEquals(8L, p6.getValue());

        assertEquals("A value", properties2.get(1).getValue());

        //
        // set timestamp
        //

        e.setTimestamp(new TimestampImpl(80L));

        //
        // timestamp
        //

        TimestampProperty p7 = (TimestampProperty)e.getProperty(0);
        assertEquals(80L, p7.getValue());

        TimestampProperty p8 = (TimestampProperty)e.getProperty(TimedEvent.TIMESTAMP_PROPERTY_NAME);
        assertEquals(80L, p8.getValue());

        //
        // other properties
        //

        assertEquals("A value", e.getProperty(1).getValue());

        assertNull(e.getProperty(2));

        //
        // getProperties()
        //

        List<Property> properties3 = e.getProperties();

        assertEquals(2, properties3.size());

        TimestampProperty p9 = (TimestampProperty)properties3.get(0);
        assertEquals(80L, p9.getValue());

        assertEquals("A value", properties3.get(1).getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected TimedEvent getEventToTest() throws Exception {
        return getEventToTest(0L);
    }

    protected abstract TimedEvent getEventToTest(Long timestamp) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
