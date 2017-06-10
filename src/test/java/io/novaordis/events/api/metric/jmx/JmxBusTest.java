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

import io.novaordis.events.api.metric.MetricSourceTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

        JmxServerAddress a = b.getJmxServerAddress();

        assertEquals(JmxServerAddress.DEFAULT_HOST, a.getHost());
        assertEquals(JmxServerAddress.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());

        assertEquals(JmxServerAddress.DEFAULT_HOST + ":" + JmxServerAddress.DEFAULT_PORT, b.getAddress());
    }

    @Test
    public void constructor_NoUsernamePassword() throws Exception {

        JmxBus b = new JmxBus("1.2.3.4:8888");

        JmxServerAddress a = b.getJmxServerAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4:8888", b.getAddress());
    }

    @Test
    public void constructor_NoUsernamePassword_NoPort() throws Exception {

        JmxBus b = new JmxBus("1.2.3.4");

        JmxServerAddress a = b.getJmxServerAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(JmxServerAddress.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", b.getAddress());
    }

    @Test
    public void constructor_NoUsernamePassword_InvalidPort() throws Exception {

        try {

            new JmxBus("1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(JmxException e) {

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
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_InvalidAddress_MissingPassword() throws Exception {

        try {

            new JmxBus("admin@1.2.3.4:2222");
            fail("should have thrown exception");
        }
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing password"));
        }
    }

    @Test
    public void constructor() throws Exception {

        JmxBus b = new JmxBus("admin:admin123@1.2.3.4:8888");

        JmxServerAddress a = b.getJmxServerAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort());
        assertEquals("admin", a.getUsername());
        assertEquals("admin123", new String(a.getPassword()));
        assertEquals("admin@1.2.3.4:8888", b.getAddress());
    }

    @Test
    public void constructor_Protocol() throws Exception {

        JmxBus b = new JmxBus("jmx://admin:adminpasswd@1.2.3.4:2345");

        JmxServerAddress a = b.getJmxServerAddress();

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(2345, a.getPort());
        assertEquals("adminpasswd", new String(a.getPassword()));
        assertEquals("admin", a.getUsername());

        assertEquals("admin@1.2.3.4:2345", b.getAddress());
    }

    @Test
    public void constructor_InvalidProtocol() throws Exception {

        try {

            new JmxBus("http://admin:adminpasswd@1.2.3.4:2345");
        }
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }

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
        assertEquals(b.hashCode(), b.getJmxServerAddress().hashCode());
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // collectMetrics() ------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxBus getMetricSourceToTest() throws Exception {

        return new JmxBus();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
