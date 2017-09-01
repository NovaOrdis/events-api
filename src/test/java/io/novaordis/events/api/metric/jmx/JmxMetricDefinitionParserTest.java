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

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressException;
import io.novaordis.utilities.address.AddressImpl;
import org.junit.Test;

import javax.management.MalformedObjectNameException;

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

        PropertyFactory f = new PropertyFactory();

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);
        assertNull(d);
    }

    @Test
    public void parse_NotAValidJBossCLIMetricDefinition_NullRepository() throws Exception {

        String s = "I am pretty sure this is not a valid JMX metric definition";

        PropertyFactory f = new PropertyFactory();

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);
        assertNull(d);
    }

    @Test
    public void parse_UnknownProtocol() throws Exception {

        String s = "something://admin:passwd@1.2.3.4:8888/test:service=Test/testAttribute";

        PropertyFactory f = new PropertyFactory();

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNull(d);
    }

    @Test
    public void parse_NoDomainNameInObjectName_ProtocolPrefix() throws Exception {

        String s = "jmx://admin:passwd@1.2.3.4:8888/something";

        PropertyFactory f = new PropertyFactory();

        try {

            JmxMetricDefinitionParser.parse(f, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no ObjectName domain name identified in the metric definition"));
        }
    }

    @Test
    public void parse_NoDomainNameInObjectName_NoProtocolPrefix() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "admin:passwd@1.2.3.4:8888/something";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNull(d);
    }

    @Test
    public void parse_ProtocolPrefixed_InvalidJmxBusAddress() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "jmx://admin:apsswd@1.2.3.4:blah/test.domain:service=Test,subService=Test/testAttribute";

        try {

            JmxMetricDefinitionParser.parse(f, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid JMX bus address"));

            AddressException e2 = (AddressException)e.getCause();
            String msg2 = e2.getMessage();
            assertTrue(msg2.contains("invalid port"));
        }
    }

    @Test
    public void parse_NotProtocolPrefixed_InvalidJmxBusAddress() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "admin:apsswd@1.2.3.4:blah/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNull(d);
    }

    @Test
    public void parse_ProtocolPrefixed_NoAttributeName() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test";

        try {

            JmxMetricDefinitionParser.parse(f, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing attribute name"));
        }
    }

    @Test
    public void parse_NotProtocolPrefixed_NoAttributeName() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNull(d);
    }

    @Test
    public void parse_ProtocolPrefixed_InvalidObjectName() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "jmx://1.2.3.4:8888/test.domain:999999/testAttribute";

        try {

            JmxMetricDefinitionParser.parse(f, s);
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid ObjectName"));

            MalformedObjectNameException e2 = (MalformedObjectNameException)e.getCause();
            assertNotNull(e2);
        }
    }

    @Test
    public void parse_NotProtocolPrefixed_InvalidObjectName() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "1.2.3.4:8888/test.domain:999999/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNull(d);
    }

    @Test
    public void parse_ProtocolPrefixed_NullRepository() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        Address expected = new AddressImpl("jmx", "admin", null, "1.2.3.4", 8888);

        assertEquals(expected, a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_ProtocolPrefixed() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "jmx://admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        Address expected = new AddressImpl("jmx", "admin", null, "1.2.3.4", 8888);

        assertEquals(expected, a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_NotProtocolPrefixed_NullRepository() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        Address expected = new AddressImpl("jmx", "admin", null, "1.2.3.4", 8888);

        assertEquals(expected, a);

        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getId());
        assertEquals("testAttribute", d.getAttributeName());
        assertEquals("test.domain:service=Test,subService=Test/testAttribute", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_NotProtocolPrefixed() throws Exception {

        PropertyFactory f = new PropertyFactory();

        String s = "admin:apsswd@1.2.3.4:8888/test.domain:service=Test,subService=Test/testAttribute";

        JmxMetricDefinitionImpl d = JmxMetricDefinitionParser.parse(f, s);

        assertNotNull(d);

        Address a = d.getMetricSourceAddress();
        Address expected = new AddressImpl("jmx", "admin", null, "1.2.3.4", 8888);
        assertTrue(expected.equals(a));

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
