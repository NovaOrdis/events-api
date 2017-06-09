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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/9/17
 */
public class JmxServerAddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_default() throws Exception {

        JmxServerAddress a = new JmxServerAddress();

        assertEquals(JmxServerAddress.DEFAULT_HOST, a.getHost());
        assertEquals(JmxServerAddress.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals(JmxServerAddress.DEFAULT_HOST + ":" + JmxServerAddress.DEFAULT_PORT, a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword() throws Exception {

        JmxServerAddress a = new JmxServerAddress("1.2.3.4:8888");

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4:8888", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_NoPort() throws Exception {

        JmxServerAddress a = new JmxServerAddress("1.2.3.4");

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(JmxServerAddress.DEFAULT_PORT, a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_InvalidPort() throws Exception {

        try {

            new JmxServerAddress("1.2.3.4:blah");
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

            new JmxServerAddress("admin:admin123@1.2.3.4:blah");
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

            new JmxServerAddress("admin@1.2.3.4:2222");
            fail("should have thrown exception");
        }
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing password"));
        }
    }

    @Test
    public void constructor() throws Exception {

        JmxServerAddress a = new JmxServerAddress("admin:admin123@1.2.3.4:8888");

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort());
        assertEquals("admin", a.getUsername());
        assertEquals("admin123", new String(a.getPassword()));
        assertEquals("admin@1.2.3.4:8888", a.getLiteral());
    }

    @Test
    public void constructor_Protocol() throws Exception {

        JmxServerAddress a = new JmxServerAddress("jmx://admin:adminpasswd@1.2.3.4:2345");

        assertEquals("1.2.3.4", a.getHost());
        assertEquals(2345, a.getPort());
        assertEquals("adminpasswd", new String(a.getPassword()));
        assertEquals("admin", a.getUsername());

        assertEquals("admin@1.2.3.4:2345", a.getLiteral());
    }

    @Test
    public void constructor_InvalidProtocol() throws Exception {

        try {

            new JmxServerAddress("http://admin:adminpasswd@1.2.3.4:2345");
        }
        catch(JmxException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid protocol"));
        }
    }

    // equals() --------------------------------------------------------------------------------------------------------

    @Test
    public void testEquals() throws Exception {

        JmxServerAddress a = new JmxServerAddress("admin@somehost:1234");
        JmxServerAddress a2 = new JmxServerAddress("admin@somehost:1234");

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void testEquals2() throws Exception {

        JmxServerAddress a = new JmxServerAddress("somehost");
        JmxServerAddress a2 = new JmxServerAddress("somehost");

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void testEquals3() throws Exception {

        JmxServerAddress a = new JmxServerAddress("somehost");

        //noinspection ObjectEqualsNull
        assertFalse(a.equals(null));
    }

    // hashCode() ------------------------------------------------------------------------------------------------------

    @Test
    public void testHashCode() throws Exception {

        JmxServerAddress a = new JmxServerAddress("admin@somehost:1234");
        JmxServerAddress a2 = new JmxServerAddress("admin@somehost:1235");

        assertTrue(a != a2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
