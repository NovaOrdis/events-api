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

import io.novaordis.utilities.time.Timestamp;
import io.novaordis.utilities.time.TimestampImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/6/16
 */
public class GenericTimedEventTest extends TimedEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_PropertyList_MissingTimestamp() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        try {

            new GenericTimedEvent(input);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no timestamp property found"));
        }
    }

    @Test
    public void constructor_TimestampInPropertyList_NullDirectTimestampArgument() throws Exception {

        List<Property> input = Collections.singletonList(new TimestampProperty(7L));

        GenericTimedEvent gte = new GenericTimedEvent(null, input);

        assertEquals(7L, gte.getTime().longValue());
    }

    @Test
    public void constructor_TimestampInPropertyList_DifferentDirectTimestampArgument() throws Exception {

        List<Property> input = Collections.singletonList(new TimestampProperty(8L));

        try {
            new GenericTimedEvent(new TimestampImpl(7L), input);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("conflicting timestamp values"));
        }
    }

    @Test
    public void constructor_TimestampInPropertyList_SameValueDirectTimestampArgument() throws Exception {

        List<Property> input = Collections.singletonList(new TimestampProperty(7L));

        GenericTimedEvent gte = new GenericTimedEvent(new TimestampImpl(7L), input);

        assertEquals(7L, gte.getTime().longValue());
    }

    @Test
    public void constructor_PropertyList() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new TimestampProperty(7L));
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        GenericTimedEvent gte = new GenericTimedEvent(input);

        assertEquals(7L, gte.getTime().longValue());

        Timestamp ts = gte.getTimestamp();
        assertEquals(7L, ts.getTime());

        List<Property> result = gte.getProperties();

        assertEquals(input.size(), result.size());

        for(int i = 0; i < input.size(); i ++) {

            Property ip = input.get(i);
            Property op = result.get(i);

            assertEquals(ip.getName(), op.getName());
            assertEquals(ip.getValue(), op.getValue());
        }
    }

    @Test
    public void constructor_PropertyList_NullTimestamp() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        GenericTimedEvent gte = new GenericTimedEvent(null, input);

        assertNull(gte.getTime());
        assertNull(gte.getTimestamp());

        List<Property> result = gte.getProperties();

        assertEquals(input.size() + 1, result.size());

        TimestampProperty tp = (TimestampProperty)result.get(0);
        assertNull(tp.getValue());

        for(int i = 0; i < input.size(); i ++) {

            Property ip = input.get(i);
            Property op = result.get(i + 1);

            assertEquals(ip.getName(), op.getName());
            assertEquals(ip.getValue(), op.getValue());
        }
    }

    @Test
    public void constructor_PropertyList3() throws Exception {

        List<Property> input = new ArrayList<>();
        input.add(new IntegerProperty("test1", 1));
        input.add(new StringProperty("test2", "2"));

        long t = 7L;

        GenericTimedEvent gte = new GenericTimedEvent(t, input);

        assertEquals(t, gte.getTime().longValue());

        Timestamp ts = gte.getTimestamp();
        assertEquals(t, ts.getTime());

        List<Property> result = gte.getProperties();

        assertEquals(input.size() + 1, result.size());

        TimestampProperty tp = (TimestampProperty)result.get(0);
        assertEquals(7L, tp.getValue());

        for(int i = 0; i < input.size(); i ++) {

            Property ip = input.get(i);
            Property op = result.get(i + 1);

            assertEquals(ip.getName(), op.getName());
            assertEquals(ip.getValue(), op.getValue());
        }
    }

    // setProperty() ---------------------------------------------------------------------------------------------------

    @Test
    public void setProperty_TimestampProperty() throws Exception {

        GenericTimedEvent e = getEventToTest();

        assertNull(e.getTime());

        Property old = e.setProperty(new TimestampProperty(7L));

        assertNull(old);

        assertEquals(7L, e.getTime().longValue());

        Property old2 = e.setProperty(new TimestampProperty(8L));

        assertEquals(7L, old2.getValue());

        assertEquals(8L, e.getTime().longValue());
    }

    // getProperties() -------------------------------------------------------------------------------------------------

    @Test
    public void getProperties_NoOtherProperties() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(7L);

        List<Property> properties = e.getProperties();
        assertEquals(1, properties.size());

        TimestampProperty p = (TimestampProperty)properties.get(0);
        assertEquals(7L, p.getValue());
    }

    @Test
    public void getProperties_OtherPropertiesExist() throws Exception {

        GenericTimedEvent e = new GenericTimedEvent(7L);

        e.setStringProperty("something", "something else");
        e.setIntegerProperty("something2", 1);

        List<Property> properties = e.getProperties();
        assertEquals(3, properties.size());

        TimestampProperty p = (TimestampProperty)properties.get(0);
        assertEquals(7L, p.getValue());

        StringProperty p2 = (StringProperty)properties.get(1);
        assertEquals("something", p2.getName());
        assertEquals("something else", p2.getValue());

        IntegerProperty p3 = (IntegerProperty)properties.get(2);
        assertEquals("something2", p3.getName());
        assertEquals(1, p3.getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected GenericTimedEvent getEventToTest() throws Exception {
        return new GenericTimedEvent();
    }

    @Override
    protected GenericTimedEvent getEventToTest(Long timestamp) throws Exception {
        return new GenericTimedEvent(timestamp);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
