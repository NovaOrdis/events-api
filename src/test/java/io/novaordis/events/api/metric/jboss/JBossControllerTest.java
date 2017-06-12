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

package io.novaordis.events.api.metric.jboss;

import io.novaordis.events.api.event.IntegerProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceTest;
import io.novaordis.events.api.metric.MockMetricDefinition;
import io.novaordis.jboss.cli.JBossControllerClient;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
public class JBossControllerTest extends MetricSourceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {

        //
        // install the mock JBossControllerClient
        //

        System.setProperty(
                JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME,
                MockJBossControllerClient.class.getName());
    }

    @After
    public void cleanUp() throws Exception {

        //
        // clean the mock JBossControllerClient
        //

        System.clearProperty(JBossControllerClient.JBOSS_CONTROLLER_CLIENT_IMPLEMENTATION_SYSTEM_PROPERTY_NAME);
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullAddress() throws Exception {

        try {

            new JBossController((JBossControllerAddress)null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null controller address", msg);
        }
    }

    // collectMetrics() ------------------------------------------------------------------------------------------------

    @Test
    public void collectMetrics_SomeOfTheDefinitionsAreNotJBossCliMetricDefinitions() throws Exception {

        fail("RETURN HERE");

        //
        // configure the internal client as a mock client and install state
        //

        JBossControllerAddress mockAddress = new JBossControllerAddress("jbosscli://MOCK-USER:mock@MOCK-HOST:7777");
        JBossControllerClient client = JBossControllerClient.getInstance(mockAddress);
        ((MockJBossControllerClient)client).setAttributeValue("/test-path", "test-attribute", 7);

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setControllerClient(client);

        MockMetricDefinition mmd = new MockMetricDefinition(jbossSource.getAddress());

        JBossCliMetricDefinition jbmd = new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute"));

        MockMetricDefinition mmd2 = new MockMetricDefinition(jbossSource.getAddress());

        List<MetricDefinition> definitions = Arrays.asList(mmd, jbmd, mmd2);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(3, properties.size());

        assertNull(properties.get(0));

        IntegerProperty p = (IntegerProperty)properties.get(1);
        String name = p.getName();
        String expected = client.getHost() + ":" + client.getPort() + "/test-path/test-attribute";
        assertEquals(expected, name);
        assertEquals(7, p.getValue());

        assertNull(properties.get(2));
    }

    @Test
    public void collectMetrics_SomeOfTheDefinitionsDoNotExistOnController() throws Exception {

        fail("RETURN HERE");

        //
        // configure the internal client as a mock client and install state
        //

        JBossControllerAddress mockAddress = new JBossControllerAddress("jbosscli://MOCK-USER:mock@MOCK-HOST:7777");
        JBossControllerClient client = JBossControllerClient.getInstance(mockAddress);
        ((MockJBossControllerClient)client).setAttributeValue("/test-path", "test-attribute-1", 10);

        //
        // test-attribute-2 does not exist on the controller
        //

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setControllerClient(client);

        JBossCliMetricDefinition jbmd = new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute-1"));

        JBossCliMetricDefinition jbmd2 = new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute-2"));

        List<MetricDefinition> definitions = Arrays.asList(jbmd, jbmd2);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(2, properties.size());

        IntegerProperty p = (IntegerProperty)properties.get(0);
        assertEquals(10, p.getValue());

        Property p2 = properties.get(1);
        assertNull(p2);
    }

    @Test
    public void collectMetrics_DefinitionDoesNotHaveCorrespondingValue() throws Exception {

        fail("RETURN HERE");

        //
        // configure the internal client as a mock client and install state
        //

        JBossControllerAddress mockAddress = new JBossControllerAddress("jbosscli://MOCK-USER:mock@MOCK-HOST:7777");
        JBossControllerClient client = JBossControllerClient.getInstance(mockAddress);

        //
        // this simulates an "undefined" CLI attribute
        //
        ((MockJBossControllerClient)client).setAttributeValue("/test-path", "test-attribute", null);

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setControllerClient(client);

        JBossCliMetricDefinition jbmd = new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute"));

        List<MetricDefinition> definitions = Collections.singletonList(jbmd);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(1, properties.size());
        Property p = properties.get(0);
        assertNull(p);
    }

    @Test
    public void collectMetrics_LazyClientInitialization() throws Exception {

        fail("RETURN HERE");

        //
        // make sure the first collectMetrics() correctly initializes the internal client
        //

        JBossControllerAddress mockAddress = new JBossControllerAddress("jbosscli://MOCK-USER:mock@MOCK-HOST:7777");
        JBossControllerClient client = JBossControllerClient.getInstance(mockAddress);
        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setControllerClient(client);

        JBossControllerClient client2 = jbossSource.getControllerClient();
        assertNotNull(client2);
        assertFalse(client2.isConnected());

        JBossCliMetricDefinition jbmd = new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute"));

        // this should trigger initialization, even if no properties are read
        List<Property> properties = jbossSource.collectMetrics(Collections.singletonList(jbmd));

        assertTrue(jbossSource.getControllerClient().isConnected());

        assertFalse(properties.isEmpty());
    }

    @Test
    public void collectMetrics_Long() throws Exception {

        fail("RETURN HERE");

        //
        // make sure the first collectMetrics() correctly initializes the internal client
        //

        JBossControllerAddress mockAddress = new JBossControllerAddress("jbosscli://MOCK-USER:mock@MOCK-HOST:7777");

        MockJBossControllerClient mc = (MockJBossControllerClient)JBossControllerClient.getInstance(mockAddress);

        //
        // "install" a Long
        //

        mc.setAttributeValue("test-path", "test-attribute", 7L);

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setControllerClient(mc);

        List<MetricDefinition> md = Collections.singletonList(new JBossCliMetricDefinition(
                jbossSource.getAddress(), new CliPath("test-path"), new CliAttribute("test-attribute")));

        // this should trigger initialization, even if no properties are read
        List<Property> properties = jbossSource.collectMetrics(md);

        assertEquals(1, properties.size());

        LongProperty p = (LongProperty)properties.get(0);

        assertEquals(7L, p.getLong().longValue());

        assertEquals("MOCK-HOST:7777/test-path/test-attribute", p.getName());
    }

    // host, port and username support ---------------------------------------------------------------------------------

    @Test
    public void defaultValues() throws Exception {

        JBossController s = new JBossController();

        JBossControllerAddress address = s.getAddress();

        assertEquals(JBossControllerClient.DEFAULT_HOST, address.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, address.getPort().intValue());
        assertNull(address.getUsername());
        assertNull(address.getPassword());
    }

    // getAddress()/hasAddress() ---------------------------------------------------------------------------------------

    @Test
    public void getAddress_hasAddress() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("admin:adminpassword@1.2.3.4:9999");
        JBossController c = new JBossController(a);

        Address address = c.getAddress();
        // password is not represented
        assertEquals(new AddressImpl("admin@1.2.3.4:9999"), address);
        assertTrue(c.hasAddress(address));
    }

    @Test
    public void getAddress_hasAddress2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("1.2.3.4:9999");
        JBossController c = new JBossController(a);

        Address address = c.getAddress();
        assertEquals(new AddressImpl("1.2.3.4:9999"), address);
        assertTrue(c.hasAddress(address));
    }

    @Test
    public void getAddress_hasAddress3() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("someHost");
        JBossController c = new JBossController(a);

        Address address = c.getAddress();
        assertEquals(new AddressImpl("someHost"), address);
        assertTrue(c.hasAddress(address));
    }

    @Test
    public void getAddress_hasAddress4() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("localhost"));

        assertEquals(new AddressImpl("localhost"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("localhost")));
    }

    @Test
    public void getAddress_hasAddress5() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("localhost:9999"));

        assertEquals(new AddressImpl("localhost:9999"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("localhost:9999")));
    }

    @Test
    public void getAddress_hasAddress6() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("somehost"));

        assertEquals(new AddressImpl("somehost"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("somehost")));
    }

    @Test
    public void getAddress_hasAddress7() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("somehost:1111"));

        assertEquals(new AddressImpl("somehost:1111"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("somehost:1111")));
    }

    @Test
    public void getAddress_hasAddress8() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("testuser:blah@localhost"));

        assertEquals(new AddressImpl("testuser@localhost"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("testuser@localhost")));
    }

    @Test
    public void getAddress_hasAddress9() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("testuser:blah@localhost"));

        assertEquals(new AddressImpl("testuser@localhost"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("testuser@localhost")));
    }

    @Test
    public void getAddress_hasAddress10() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("test:test123!@localhost"));

        assertEquals(new AddressImpl("test@localhost"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("test@localhost")));
    }

    @Test
    public void getAddress_hasAddress11() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("test:test123!@localhost:9999"));

        assertEquals(new AddressImpl("test@localhost:9999"), c.getAddress());
        assertTrue(c.hasAddress(new AddressImpl("test@localhost:9999")));
    }

    // equals() and hashCode() -----------------------------------------------------------------------------------------

    @Test
    @Override
    public void equalsTest() throws Exception {

        JBossController s = new JBossController(new JBossControllerAddress("jbosscli://someuser:1@somehost:1234"));
        JBossController s2 = new JBossController(new JBossControllerAddress("jbosscli://someuser:2@somehost:1234"));

        assertEquals(s, s2);
        assertEquals(s2, s);
    }

    @Test
    public void equals_DefaultController() throws Exception {

        JBossController s = new JBossController();
        JBossController s2 = new JBossController();

        assertEquals(s, s2);
        assertEquals(s2, s);
    }

    @Test
    public void equals_DefaultControllerPort() throws Exception {

        JBossController s = new JBossController(
                new JBossControllerAddress("jbosscli://somehost:" + JBossControllerClient.DEFAULT_PORT));
        JBossController s2 = new JBossController(
                new JBossControllerAddress("jbosscli://somehost:" + JBossControllerClient.DEFAULT_PORT));

        assertEquals(s, s2);
        assertEquals(s2, s);
    }

    @Test
    public void equals_SameControllerAddress() throws Exception {

        JBossController s = new JBossController(new JBossControllerAddress("jbosscli://somehost:1234"));
        JBossController s2 = new JBossController(new JBossControllerAddress("jbosscli://somehost:1234"));

        assertEquals(s, s2);
        assertEquals(s2, s);
    }

    @Test
    public void notEquals_DifferentUser() throws Exception {

        JBossController s = new JBossController(new JBossControllerAddress("jbosscli://someuser:a@somehost:1234"));
        JBossController s2 = new JBossController(new JBossControllerAddress("jbosscli://someuser2:a@somehost:1234"));

        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test
    public void notEquals_DifferentPort() throws Exception {

        JBossController s = new JBossController(new JBossControllerAddress("jbosscli://localhost:1234"));
        JBossController s2 = new JBossController(new JBossControllerAddress("jbosscli://localhost:1235"));

        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    // hashCode() ------------------------------------------------------------------------------------------------------

    @Override
    public void hashCodeTest() throws Exception {

        JBossController s = new JBossController(new JBossControllerAddress("jbosscli://someuser:1@somehost:1234"));

        JBossControllerAddress a = s.getAddress();
        assertEquals(a.hashCode(), s.hashCode());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JBossController getMetricSourceToTest(String... addresses) throws Exception {

        if (addresses.length == 0) {

            return new JBossController();
        }
        else if (addresses.length == 1) {

            String address = addresses[0];
            address = "jbosscli://" + address;
            return new JBossController(address);
        }
        else {

            // at most one argument is expected
            throw new IllegalArgumentException(addresses.length + " arguments");
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
