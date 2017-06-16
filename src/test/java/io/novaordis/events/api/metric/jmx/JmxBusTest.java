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

package io.novaordis.events.api.metric.jmx;

import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.events.api.metric.MetricSourceTest;
import io.novaordis.utilities.address.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBusTest extends MetricSourceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_default() throws Exception {

        JmxBus b = new JmxBus();

        Address a = b.getAddress();

        assertEquals(JmxBus.DEFAULT_HOST, a.getHost());
        assertEquals(JmxBus.DEFAULT_PORT, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());

        assertEquals(JmxBus.DEFAULT_HOST + ":" + JmxBus.DEFAULT_PORT, b.getAddress().getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword() throws Exception {

        JmxBus b = new JmxBus("1.2.3.4:8888");

        Address a = b.getAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4:8888", b.getAddress().getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_NoPort() throws Exception {

        JmxBus b = new JmxBus("1.2.3.4");

        Address a = b.getAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(JmxBus.DEFAULT_PORT, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_InvalidPort() throws Exception {

        try {

            new JmxBus("1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_UsernameAndPassword_InvalidPort() throws Exception {

        try {

            new JmxBus("admin:admin123@1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_MissingPassword() throws Exception {

        JmxBus b = new JmxBus("admin@1.2.3.4:2222");

        Address a = b.getAddress();
        assertNotNull(a);

        assertEquals("admin", a.getUsername());
        assertNull(a.getPassword());
    }

    @Test
    public void constructor() throws Exception {

        JmxBus b = new JmxBus("admin:admin123@1.2.3.4:8888");

        Address a = b.getAddress();

        assertEquals(JmxBus.PROTOCOL, a.getProtocol());
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort().intValue());
        assertEquals("admin", a.getUsername());
        assertEquals("admin123", new String(a.getPassword()));
        assertEquals("admin@1.2.3.4:8888", b.getAddress().getLiteral());
    }

    @Test
    public void constructor_Protocol() throws Exception {

        JmxBus b = new JmxBus("jmx://admin:adminpasswd@1.2.3.4:2345");

        Address a = b.getAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(2345, a.getPort().intValue());
        assertEquals("adminpasswd", new String(a.getPassword()));
        assertEquals("admin", a.getUsername());

        assertEquals("admin@1.2.3.4:2345", b.getAddress().getLiteral());
    }

    @Test
    public void constructor_InvalidProtocol() throws Exception {

        try {

            new JmxBus("http://admin:adminpasswd@1.2.3.4:2345");
        }
        catch(Exception e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }

    // equals() --------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void equalsTest() throws Exception {

        JmxBus b = new JmxBus("admin:sompass@1.2.3.4:567");
        JmxBus b2 = new JmxBus("admin:someotherpass@1.2.3.4:567");

        assertEquals(b, b2);
        assertEquals(b2, b);
    }

    @Test
    public void equalsTest2() throws Exception {

        JmxBus b = new JmxBus("admin:somepass@1.2.3.4:567");
        //noinspection ObjectEqualsNull
        assertFalse(b.equals(null));
    }

    @Test
    public void equalsTest3() throws Exception {

        JmxBus b = new JmxBus();
        JmxBus b2 = new JmxBus();

        assertEquals(b, b2);
        assertEquals(b2, b);
    }

    @Override
    public void hashCodeTest() throws Exception {

        JmxBus b = getMetricSourceToTest();
        assertEquals(b.hashCode(), b.getAddress().hashCode());
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // collectMetrics() ------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxBus getMetricSourceToTest(String... addresses) throws Exception {

        if (addresses.length == 0) {

            return new JmxBus();
        }
        else if (addresses.length == 1) {

            String address = addresses[0];
            address = "jmx://" + address;
            return new JmxBus(address);
        }
        else {

            // at most one argument is expected
            throw new IllegalArgumentException(addresses.length + " arguments");
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
