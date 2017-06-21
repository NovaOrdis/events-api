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

package io.novaordis.events.api.metric;

import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public class MetricSourceTypeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // fromString() ----------------------------------------------------------------------------------------------------

    @Test
    public void fromString_NoSuchMetricSourceType() throws Exception {

        MetricSourceType t = MetricSourceType.fromString("I-am-sure-theres-no-such-type");
        assertNull(t);
    }

    @Test
    public void fromString_LOCAL_OS() throws Exception {

        MetricSourceType t = MetricSourceType.fromString("local-os");
        assertEquals(MetricSourceType.LOCAL_OS, t);
    }

    @Test
    public void fromString_JBOSS_CONTROLLER() throws Exception {

        MetricSourceType t = MetricSourceType.fromString("jboss-controller");
        assertEquals(MetricSourceType.JBOSS_CONTROLLER, t);
    }

    @Test
    public void fromString_JMX() throws Exception {

        MetricSourceType t = MetricSourceType.fromString("jmx");
        assertEquals(MetricSourceType.JMX, t);
    }

    // fromAddress() ---------------------------------------------------------------------------------------------------

    @Test
    public void fromAddress_null() throws Exception {

        MetricSourceType t = MetricSourceType.fromAddress(null);
        //noinspection ConstantConditions
        assertNull(t);
    }

    @Test
    public void fromAddress_LocalOSAddress() throws Exception {

        MetricSourceType t = MetricSourceType.fromAddress(new LocalOSAddress());
        assertEquals(MetricSourceType.LOCAL_OS, t);
    }

    @Test
    public void fromAddress_RemoteOSAddress() throws Exception {

        Address a = new AddressImpl("ssh", "test-user", "test-passwd", "test-host", 1000);
        MetricSourceType t = MetricSourceType.fromAddress(a);
        assertEquals(MetricSourceType.REMOTE_OS, t);
    }

    @Test
    public void fromAddress_JBossControllerAddress() throws Exception {

        MetricSourceType t = MetricSourceType.fromAddress(new JBossControllerAddress());
        assertEquals(MetricSourceType.JBOSS_CONTROLLER, t);
    }

    @Test
    public void fromAddress_JmxAddress() throws Exception {

        MetricSourceType t = MetricSourceType.fromAddress(new JmxAddress("jmx://something:99/"));
        assertEquals(MetricSourceType.JMX, t);
    }

    // toAddress() -----------------------------------------------------------------------------------------------------

    @Test
    public void toAddress_LOCAL_OS() throws Exception {

        Address a = MetricSourceType.LOCAL_OS.toAddress("test-user", "test-password", "test-host", 1000);

        LocalOSAddress loa = (LocalOSAddress)a;

        assertNotNull(loa);

        assertNull(loa.getPort());
        assertNull(loa.getUsername());
        assertNull(loa.getPassword());
    }

    @Test
    public void toAddress_LOCAL_OS_AllNull() throws Exception {

        Address a = MetricSourceType.LOCAL_OS.toAddress(null, null, null, null);

        assertNotNull(a);

        LocalOSAddress loa = (LocalOSAddress)a;

        assertNotNull(loa);

        assertNull(loa.getPort());
        assertNull(loa.getUsername());
        assertNull(loa.getPassword());
    }

    @Test
    public void toAddress_REMOTE_OS() throws Exception {

        Address a = MetricSourceType.REMOTE_OS.toAddress("test-user", "test-password", "test-host", 1000);

        assertNotNull(a);

        assertEquals("ssh", a.getProtocol());

        assertEquals("test-host", a.getHost());
        assertEquals(1000, a.getPort().longValue());
        assertEquals("test-user", a.getUsername());
        assertEquals("test-password", new String(a.getPassword()));
    }

    @Test
    public void toAddress_JBOSS_CONTROLLER() throws Exception {

        Address a = MetricSourceType.JBOSS_CONTROLLER.toAddress("test-user", "test-password", "test-host", 1000);

        assertNotNull(a);

        JBossControllerAddress jca = (JBossControllerAddress)a;

        assertEquals("test-user", jca.getUsername());
        assertEquals("test-password", new String(jca.getPassword()));
        assertEquals("test-host", jca.getHost());
        assertEquals(1000, jca.getPort().intValue());
    }

    @Test
    public void toAddress_JBOSS_CONTROLLER_NullUsernameAndPassword() throws Exception {

        Address a = MetricSourceType.JBOSS_CONTROLLER.toAddress(null, null, "test-host", 1000);

        assertNotNull(a);

        JBossControllerAddress jca = (JBossControllerAddress)a;

        assertNull(jca.getUsername());
        assertNull(jca.getPassword());
        assertEquals("test-host", jca.getHost());
        assertEquals(1000, jca.getPort().intValue());
    }

    @Test
    public void toAddress_JMX() throws Exception {

        Address a = MetricSourceType.JMX.toAddress("test-user", "test-password", "test-host", 1000);

        assertNotNull(a);

        JmxAddress jmxa = (JmxAddress)a;

        assertEquals("test-user", jmxa.getUsername());
        assertEquals("test-password", new String(jmxa.getPassword()));
        assertEquals("test-host", jmxa.getHost());
        assertEquals(1000, jmxa.getPort().intValue());
    }

    @Test
    public void toAddress_JMX_NullUsernameAndPassword() throws Exception {

        Address a = MetricSourceType.JMX.toAddress(null, null, "test-host", 1000);

        assertNotNull(a);

        JmxAddress jmxa = (JmxAddress)a;

        assertNull(jmxa.getUsername());
        assertNull(jmxa.getPassword());
        assertEquals("test-host", jmxa.getHost());
        assertEquals(1000, jmxa.getPort().intValue());
    }

    // getLiteral()  ---------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral() throws Exception {

        for(MetricSourceType t: MetricSourceType.values()) {

            String literal = t.getLiteral();
            assertEquals(t, MetricSourceType.fromString(literal));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
