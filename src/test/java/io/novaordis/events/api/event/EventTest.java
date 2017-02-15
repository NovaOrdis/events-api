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

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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

        assertTrue(event.getProperties().isEmpty());

        Property old = event.setProperty(new StringProperty("test", "test-value"));
        assertNull(old);

        Set<Property> properties = event.getProperties();
        assertEquals(1, properties.size());
        StringProperty p = (StringProperty)properties.iterator().next();
        assertEquals("test", p.getName());
        assertEquals("test-value", p.getString());

        old = event.setProperty(new StringProperty("test", "test-value-2"));
        assertNotNull(old);
        assertEquals("test", old.getName());
        assertEquals("test-value", old.getValue());

        properties = event.getProperties();
        assertEquals(1, properties.size());
        StringProperty p2 = (StringProperty)properties.iterator().next();
        assertEquals("test", p2.getName());
        assertEquals("test-value-2", p2.getString());

        StringProperty sp = (StringProperty)event.getProperty("test");
        assertEquals("test", sp.getName());
        assertEquals("test-value-2", sp.getString());
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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Event getEventToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
