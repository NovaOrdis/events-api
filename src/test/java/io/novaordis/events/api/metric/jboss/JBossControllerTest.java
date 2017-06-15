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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void collectMetrics_SomeOfTheDefinitionsAreNotJBossDmrMetricDefinitions() throws Exception {

        //
        // install the client factory that produces a mock client
        //

        MockJBossControllerClientFactory mcf = new MockJBossControllerClientFactory();

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setJBossControllerClientFactory(mcf);

        //
        // install test state into the mock client
        //

        jbossSource.start();

        MockJBossControllerClient mc = (MockJBossControllerClient)jbossSource.getControllerClient();

        mc.setAttributeValue("/test-path", "test-attribute-1", 7);

        JBossDmrMetricDefinitionImpl mmd = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute"));

        //
        // this metric is not a JBoss DMR metric
        //

        MockMetricDefinition mmd2 = new MockMetricDefinition(jbossSource.getAddress());

        List<MetricDefinition> definitions = Arrays.asList(mmd, mmd2);

        try {

            jbossSource.collectMetrics(definitions);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("non-JBoss DMR metric"));
        }
    }

    @Test
    public void collectMetrics_SomeOfTheDefinitionsDoNotExistOnController() throws Exception {

        //
        // install the client factory that produces a mock client
        //

        MockJBossControllerClientFactory mcf = new MockJBossControllerClientFactory();

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setJBossControllerClientFactory(mcf);

        //
        // install test state into the mock client
        //

        jbossSource.start();

        MockJBossControllerClient mc = (MockJBossControllerClient)jbossSource.getControllerClient();

        mc.setAttributeValue("/test-path", "test-attribute-1", 10);

        //
        // test-attribute-2 does not exist on the controller
        //

        JBossDmrMetricDefinitionImpl jbmd = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute-1"));

        JBossDmrMetricDefinitionImpl jbmd2 = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute-2"));

        List<MetricDefinition> definitions = Arrays.asList(jbmd, jbmd2);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(2, properties.size());

        IntegerProperty p = (IntegerProperty)properties.get(0);
        assertEquals("/test-path/test-attribute-1", p.getName());
        assertEquals(10, p.getValue());
        assertEquals(Integer.class, p.getType());

        Property p2 = properties.get(1);
        assertEquals("/test-path/test-attribute-2", p2.getName());
        assertNull(p2.getValue());
        assertNull(p2.getType());
    }

    @Test
    public void collectMetrics_DefinitionDoesNotHaveCorrespondingValue() throws Exception {

        //
        // install the client factory that produces a mock client
        //

        MockJBossControllerClientFactory mcf = new MockJBossControllerClientFactory();

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setJBossControllerClientFactory(mcf);

        //
        // install test state into the mock client
        //

        jbossSource.start();

        MockJBossControllerClient mc = (MockJBossControllerClient)jbossSource.getControllerClient();

        mc.setAttributeValue("/test-path", "test-attribute", null);

        //
        // test-attribute-2 does not exist on the controller
        //

        JBossDmrMetricDefinitionImpl jbmd = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute"));

        List<MetricDefinition> definitions = Collections.singletonList(jbmd);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(1, properties.size());

        Property p2 = properties.get(0);
        assertEquals("/test-path/test-attribute", p2.getName());
        assertNull(p2.getValue());
        assertNull(p2.getType());
    }

    @Test
    public void collectMetrics_LazyClientInitialization() throws Exception {

        //
        // make sure the first collectMetrics() correctly initializes the internal client
        //

        MockJBossControllerClientFactory mcf = new MockJBossControllerClientFactory();
        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setJBossControllerClientFactory(mcf);

        //
        // client not initialized at this type
        //

        assertNull(jbossSource.getControllerClient());


        JBossDmrMetricDefinitionImpl jbmd = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute"));

        //
        // this should trigger initialization, even if no properties are read
        //
        List<Property> properties = jbossSource.collectMetrics(Collections.singletonList(jbmd));

        assertTrue(jbossSource.getControllerClient().isConnected());
        assertEquals(1, properties.size());
    }

    @Test
    public void collectMetrics_Long() throws Exception {

        //
        // install the client factory that produces a mock client
        //

        MockJBossControllerClientFactory mcf = new MockJBossControllerClientFactory();

        JBossController jbossSource = getMetricSourceToTest();
        jbossSource.setJBossControllerClientFactory(mcf);

        //
        // install test state into the mock client
        //

        jbossSource.start();

        MockJBossControllerClient mc = (MockJBossControllerClient)jbossSource.getControllerClient();

        mc.setAttributeValue("/test-path", "test-attribute", 10L);

        //
        // test-attribute-2 does not exist on the controller
        //

        JBossDmrMetricDefinitionImpl jbmd = new JBossDmrMetricDefinitionImpl(
                jbossSource.getAddress(), new DmrPath("test-path"), new DmrAttribute("test-attribute"));

        List<MetricDefinition> definitions = Collections.singletonList(jbmd);

        List<Property> properties = jbossSource.collectMetrics(definitions);

        assertEquals(1, properties.size());

        LongProperty p = (LongProperty)properties.get(0);
        assertEquals("/test-path/test-attribute", p.getName());
        assertEquals(10L, p.getValue());
        assertEquals(Long.class, p.getType());
    }

    // host, port and username support ---------------------------------------------------------------------------------

    @Test
    public void defaultValues() throws Exception {

        JBossController s = new JBossController();

        JBossControllerAddress address = s.getAddress();

        assertEquals(JBossControllerAddress.DEFAULT_HOST, address.getHost());
        assertEquals(JBossControllerAddress.DEFAULT_PORT, address.getPort().intValue());
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
        JBossControllerAddress expected = new JBossControllerAddress("admin", null, "1.2.3.4", 9999);
        assertEquals(expected, address);
        assertTrue(c.hasAddress(expected));
    }

    @Test
    public void getAddress_hasAddress2() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("1.2.3.4:9999");
        JBossController c = new JBossController(a);

        Address address = c.getAddress();
        assertEquals(new JBossControllerAddress("1.2.3.4:9999"), address);
        assertTrue(c.hasAddress(address));
    }

    @Test
    public void getAddress_hasAddress3() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("someHost");
        JBossController c = new JBossController(a);

        Address address = c.getAddress();
        assertEquals(new JBossControllerAddress("someHost"), address);
        assertTrue(c.hasAddress(address));
    }

    @Test
    public void getAddress_hasAddress4() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("localhost"));

        assertEquals(new JBossControllerAddress("localhost"), c.getAddress());
        assertTrue(c.hasAddress(new JBossControllerAddress("localhost")));
    }

    @Test
    public void getAddress_hasAddress5() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("localhost:9999"));

        assertEquals(new JBossControllerAddress("localhost:9999"), c.getAddress());
        assertTrue(c.hasAddress(new JBossControllerAddress("localhost:9999")));
    }

    @Test
    public void getAddress_hasAddress6() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("somehost"));

        assertEquals(new JBossControllerAddress("somehost"), c.getAddress());
        assertTrue(c.hasAddress(new JBossControllerAddress("somehost")));
    }

    @Test
    public void getAddress_hasAddress7() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("somehost:1111"));

        assertEquals(new JBossControllerAddress("somehost:1111"), c.getAddress());
        assertTrue(c.hasAddress(new JBossControllerAddress("somehost:1111")));
    }

    @Test
    public void getAddress_hasAddress8() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("testuser:blah@localhost"));
        JBossControllerAddress expected =
                new JBossControllerAddress("testuser", null, "localhost", JBossControllerAddress.DEFAULT_PORT);
        assertEquals(expected, c.getAddress());
        assertTrue(c.hasAddress(expected));
    }

    @Test
    public void getAddress_hasAddress9() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("testuser:blah@localhost"));
        JBossControllerAddress expected =
                new JBossControllerAddress("testuser", null, "localhost", JBossControllerAddress.DEFAULT_PORT);
        assertEquals(expected, c.getAddress());
        assertTrue(c.hasAddress(expected));
    }

    @Test
    public void getAddress_hasAddress10() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("test:test123!@localhost"));
        JBossControllerAddress expected =
                new JBossControllerAddress("test", null, "localhost", JBossControllerAddress.DEFAULT_PORT);
        assertEquals(expected, c.getAddress());
        assertTrue(c.hasAddress(expected));
    }

    @Test
    public void getAddress_hasAddress11() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("test:test123!@localhost:9999"));
        JBossControllerAddress expected = new JBossControllerAddress("test", null, "localhost", 9999);
        assertEquals(expected, c.getAddress());
        assertTrue(c.hasAddress(expected));
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

        JBossControllerAddress a =
                new JBossControllerAddress("jbosscli://somehost:" + JBossControllerAddress.DEFAULT_PORT);

        JBossControllerAddress a2 =
                new JBossControllerAddress("jbosscli://somehost:" + JBossControllerAddress.DEFAULT_PORT);


        JBossController s = new JBossController(a);
        JBossController s2 = new JBossController(a2);

        assertTrue(s.equals(s2));
        assertTrue(s2.equals(s));
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
    protected JBossDmrMetricDefinition getCorrespondingMockMetricDefinition(Address metricSourceAddress)
            throws Exception {

        return new MockJBossDmrMetricDefinition(metricSourceAddress, new DmrPath("test=test"), new DmrAttribute("test"));
    }

    @Override
    protected JBossController getMetricSourceToTest(String... addresses) throws Exception {

        JBossController result;

        if (addresses.length == 0) {

            result = new JBossController();
        }
        else if (addresses.length == 1) {

            String address = addresses[0];
            address = "jbosscli://" + address;
            result = new JBossController(address);
        }
        else {

            // at most one argument is expected
            throw new IllegalArgumentException(addresses.length + " arguments");
        }

        //
        // since we don't have a JBoss controller lying around, we simulate one with a mock JBossControllerClient
        // built by this factory
        //

        result.setJBossControllerClientFactory(new MockJBossControllerClientFactory());

        return result;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
