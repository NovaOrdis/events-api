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

import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceException;
import io.novaordis.events.api.metric.MetricSourceTest;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.jmx.JmxException;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import org.junit.After;
import org.junit.Test;

import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @After
    public void cleanup() throws Exception {

        MockMBeanServerConnection.clear();
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_default() throws Exception {

        JmxBus b = new JmxBus();

        JmxAddress a = b.getAddress();

        assertNull(a);
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

        try {

            new JmxBus("1.2.3.4");
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            AddressException cause = (AddressException)e.getCause();
            String msg = cause.getMessage();
            assertTrue(msg.contains("missing port"));
        }
    }

    @Test
    public void constructor_NoUsernamePassword_InvalidPort() throws Exception {

        try {

            new JmxBus("1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            AddressException cause = (AddressException)e.getCause();
            String msg = cause.getMessage();
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

            AddressException cause = (AddressException)e.getCause();
            String msg = cause.getMessage();
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

        JmxAddress a = b.getAddress();

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

        JmxBus b = new JmxBus("example:77");
        JmxBus b2 = new JmxBus("example:77");

        assertEquals(b, b2);
        assertEquals(b2, b);
    }

    @Override
    @Test
    public void hashCodeTest() throws Exception {

        JmxBus b = getMetricSourceToTest("jmx://example:1000");
        assertEquals(b.hashCode(), b.getAddress().hashCode());
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // collect() -------------------------------------------------------------------------------------------------------

    @Test
    public void collect_Long_MetricDefinitionDoesNotSpecifyType() throws Exception {

        MockMBeanServerConnection.addAttribute(new ObjectName("test.domain:service=Mock"), "TestAttribute", 7L);

        JmxBus b = getMetricSourceToTest("jmx://example:1000");

        JmxMetricDefinition md =
                new JmxMetricDefinitionImpl(b.getAddress(), "test.domain", "service=Mock", "TestAttribute");

        List<MetricDefinition> mds = Collections.singletonList(md);

        b.start();

        List<Property> properties = b.collect(mds);

        assertEquals(1, properties.size());
        Property p = properties.get(0);

        assertTrue(p instanceof LongProperty);
        LongProperty lp = (LongProperty)p;
        assertEquals("test.domain:service=Mock/TestAttribute", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(7L, lp.getLong().longValue());

        assertTrue(b.isStarted());
    }

    @Test
    public void collect_IndividualMetricFails_MBeanException() throws Exception {

        //
        // first metric fails, the second succeeds
        //

        MockMBeanServerConnection.configureFailure(
                new ObjectName("test:service=A"), new MBeanException(new RuntimeException(), "SYNTHETIC"));

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=B"), "Attr", 8L);

        JmxBus b = getMetricSourceToTest("jmx://test:1000");

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=A", "DoesNotMatter");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "Attr");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        List<Property> properties = b.collect(mds);

        assertEquals(2, properties.size());

        Property p = properties.get(0);

        //
        // not collected
        //
        assertEquals("test:service=A/DoesNotMatter", p.getName());
        assertNull(p.getValue());

        Property p2 = properties.get(1);
        LongProperty lp = (LongProperty) p2;
        assertEquals("test:service=B/Attr", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(8L, lp.getLong().longValue());

        assertTrue(b.isStarted());
    }

    @Test
    public void collect_IndividualMetricFails_AttributeNotFoundException() throws Exception {

        //
        // first metric fails, the second succeeds
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=A"), "AttrA", 1L);
        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=B"), "AttrB", 2L);

        JmxBus b = getMetricSourceToTest("jmx://test:1000");

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(
                b.getAddress(), "test", "service=A", "IAmSureThereIsNoSuchAttribute");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "AttrB");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        List<Property> properties = b.collect(mds);

        assertEquals(2, properties.size());

        Property p = properties.get(0);

        //
        // not collected
        //
        assertEquals("test:service=A/IAmSureThereIsNoSuchAttribute", p.getName());
        assertNull(p.getValue());

        Property p2 = properties.get(1);
        LongProperty lp = (LongProperty) p2;
        assertEquals("test:service=B/AttrB", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(2L, lp.getLong().longValue());

        assertTrue(b.isStarted());
    }

    @Test
    public void collect_IndividualMetricFails_InstanceNotFoundException() throws Exception {

        //
        // first metric fails, the second succeeds
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=A"), "AttrA", 1L);
        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=B"), "AttrB", 2L);

        JmxBus b = getMetricSourceToTest("jmx://test:1000");

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(
                b.getAddress(), "test", "service=IAmSureThereIsNoSuchService", "DoesNotMatter");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "AttrB");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        List<Property> properties = b.collect(mds);

        assertEquals(2, properties.size());

        Property p = properties.get(0);

        //
        // not collected
        //
        assertEquals("test:service=IAmSureThereIsNoSuchService/DoesNotMatter", p.getName());
        assertNull(p.getValue());

        Property p2 = properties.get(1);
        LongProperty lp = (LongProperty) p2;
        assertEquals("test:service=B/AttrB", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(2L, lp.getLong().longValue());

        assertTrue(b.isStarted());
    }

    @Test
    public void collect_IndividualMetricFails_ReflectionException() throws Exception {

        //
        // first metric fails, the second succeeds
        //

        MockMBeanServerConnection.configureFailure(
                new ObjectName("test:service=A"), new ReflectionException(new RuntimeException(), "SYNTHETIC"));

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=B"), "Attr", 8L);

        JmxBus b = getMetricSourceToTest("jmx://test:1000");

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=A", "DoesNotMatter");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "Attr");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        List<Property> properties = b.collect(mds);

        assertEquals(2, properties.size());

        Property p = properties.get(0);

        //
        // not collected
        //
        assertEquals("test:service=A/DoesNotMatter", p.getName());
        assertNull(p.getValue());

        Property p2 = properties.get(1);
        LongProperty lp = (LongProperty) p2;
        assertEquals("test:service=B/Attr", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(8L, lp.getLong().longValue());

        assertTrue(b.isStarted());
    }

    @Test
    public void collect_JmxSourceFails_IOException() throws Exception {

        //
        // first metric succeeds, the second fails, but it does not matter, as IOException is a source-wide exception
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=A"), "AttrA", 1L);
        MockMBeanServerConnection.configureFailure(new ObjectName("test:service=B"), new IOException("SYNTHETIC"));

        JmxBus b = getMetricSourceToTest("jmx://test:1000");

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=A", "AttrA");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "AttrB");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        try {

            b.collect(mds);
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            Throwable t = e.getCause();

            assertTrue(t instanceof IOException);
            String msg = t.getMessage();
            assertEquals("SYNTHETIC", msg);
        }

        //
        // we automatically stop the source on source-wide failure, so if it is a transient failure, we'll retry
        //
        assertFalse(b.isStarted());
    }

    @Test
    public void collect_JmxSourceFails_JmxException() throws Exception {

        //
        // first metric succeeds, the second fails, but it does not matter, as JmxException is a source-wide exception
        //

        MockMBeanServerConnection.addAttribute(new ObjectName("test:service=A"), "AttrA", 1L);
        MockJmxClientFactory mf = new MockJmxClientFactory();
        mf.configureClientToFailOnGetMBeanServerConnection();

        JmxBus b = new JmxBus("jmx://test:1000");
        b.setJmxClientFactory(mf);

        JmxMetricDefinition md = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=A", "AttrA");
        JmxMetricDefinition md2 = new JmxMetricDefinitionImpl(b.getAddress(), "test", "service=B", "AttrB");

        List<MetricDefinition> mds = Arrays.asList(md, md2);

        b.start();

        try {

            b.collect(mds);
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            Throwable t = e.getCause();

            assertTrue(t instanceof JmxException);
            String msg = t.getMessage();
            assertEquals("SYNTHETIC", msg);
        }

        //
        // we automatically stop the source on source-wide failure, so if it is a transient failure, we'll retry
        //
        assertFalse(b.isStarted());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MockJmxMetricDefinition getCorrespondingMockMetricDefinition(Address address)
            throws Exception {

        if (!(address instanceof JmxAddress)) {

            fail("we expect a JmxAddress but we got " + address);
        }

        return new MockJmxMetricDefinition((JmxAddress)address, "test.domain", "service=MockService", "testAttribute");
    }

    @Override
    protected JmxBus getMetricSourceToTest(String... addresses) throws Exception {

        JmxBus testInstance;

        if (addresses.length == 0) {

            testInstance =  new JmxBus("jmx://mock-jmx-server:1000");
        }
        else if (addresses.length == 1) {

            String address = addresses[0];

            address = address.startsWith(JmxAddress.PROTOCOL) ?
                    address : JmxAddress.PROTOCOL + JmxAddress.PROTOCOL_SEPARATOR + address;

            testInstance = new JmxBus(address);
        }
        else {

            // at most one argument is expected
            throw new IllegalArgumentException(addresses.length + " arguments");
        }

        //
        // install a factory that produces an automatically testable client
        //

        testInstance.setJmxClientFactory(new MockJmxClientFactory());

        return testInstance;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
