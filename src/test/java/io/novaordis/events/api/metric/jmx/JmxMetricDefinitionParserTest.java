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

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import org.junit.Test;

import javax.management.MalformedObjectNameException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class JmxMetricDefinitionParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void parse_NotAValidJBossCLIMetricDefinition() throws Exception {

        String s = "I am pretty sure this is not a valid JMX metric definition";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);
        assertNull(d);

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NotAValidJBossCLIMetricDefinition_NullRepository() throws Exception {

        String s = "I am pretty sure this is not a valid JMX metric definition";

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(null, s);
        assertNull(d);
    }

    @Test
    public void parse_UnknownProtocol() throws Exception {

        String s = "something://admin:passwd@1.2.3.4:8888/test:service=Test/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNull(d);
        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NoDomainNameInObjectName_ProtocolPrefix() throws Exception {

        String s = "jmx://admin:passwd@1.2.3.4:8888/something";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        try {

            JmxMetricDefinitionParser.parse(r, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no ObjectName domain name identified in the metric definition"));
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NoDomainNameInObjectName_NoProtocolPrefix() throws Exception {

        String s = "admin:passwd@1.2.3.4:8888/something";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNull(d);
        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_ProtocolPrefixed_InvalidJmxBusAddress() throws Exception {

        String s = "jmx://admin:apsswd@1.2.3.4:blah/test.domain:service=Test,subService=Test/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        try {

            JmxMetricDefinitionParser.parse(r, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid JMX bus address"));

            JmxException e2 = (JmxException)e.getCause();
            String msg2 = e2.getMessage();
            assertTrue(msg2.contains("invalid port"));
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NotProtocolPrefixed_InvalidJmxBusAddress() throws Exception {

        String s = "admin:apsswd@1.2.3.4:blah/test.domain:service=Test,subService=Test/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNull(d);

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_ProtocolPrefixed_NoAttributeName() throws Exception {

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        try {

            JmxMetricDefinitionParser.parse(r, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing attribute name"));
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NotProtocolPrefixed_NoAttributeName() throws Exception {

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNull(d);

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_ProtocolPrefixed_InvalidObjectName() throws Exception {

        String s = "jmx://1.2.3.4:8888/test.domain:999999/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        try {

            JmxMetricDefinitionParser.parse(r, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid ObjectName"));

            MalformedObjectNameException e2 = (MalformedObjectNameException)e.getCause();
            assertNotNull(e2);
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NotProtocolPrefixed_InvalidObjectName() throws Exception {

        String s = "1.2.3.4:8888/test.domain:999999/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNull(d);

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_ProtocolPrefixed_NullRepository() throws Exception {

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(null, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        assertEquals(new AddressImpl("jmx://admin@1.2.3.4:8888"), a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_ProtocolPrefixed() throws Exception {

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        assertEquals(new AddressImpl("admin@1.2.3.4:8888"), a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NotProtocolPrefixed_NullRepository() throws Exception {

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(null, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        assertEquals(new AddressImpl("admin@1.2.3.4:8888"), a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_NotProtocolPrefixed() throws Exception {

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JmxMetricDefinition d = JmxMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        assertEquals(new AddressImpl("admin@1.2.3.4:8888"), a);

        Set<JmxBus> buses = r.getSources(JmxBus.class);
        assertTrue(buses.isEmpty());

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
