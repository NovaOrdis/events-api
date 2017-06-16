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
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/15/17
 */
public class MetricSourceDefinitionImplTest extends MetricSourceDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_nullAddress() throws Exception {

        try {

            new MetricSourceDefinitionImpl("some-name", null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null address"));
        }
    }

    @Test
    public void constructor_NullName() throws Exception {

        Address a = new AddressImpl("test");

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl(null, a);

        assertTrue(new AddressImpl("test").equals(d.getAddress()));
        assertNull(d.getName());
        assertNull(d.getType());
    }

    @Test
    public void constructor_InferType_LocalOSAddress() throws Exception {

        LocalOSAddress a = new LocalOSAddress();

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl("some-name", a);

        assertTrue(new LocalOSAddress().equals(d.getAddress()));
        assertEquals("some-name", d.getName());
        assertEquals(MetricSourceType.LOCAL_OS, d.getType());
    }

    @Test
    public void constructor_InferType_JmxBusAddress() throws Exception {

        AddressImpl a = new AddressImpl("jmx://somehost");

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl("some-name", a);

        assertTrue(new AddressImpl("jmx://somehost").equals(d.getAddress()));
        assertEquals("some-name", d.getName());
        assertEquals(MetricSourceType.JMX, d.getType());
    }

    @Test
    public void constructor_InferType_JBossControllerAddress() throws Exception {

        JBossControllerAddress a = new JBossControllerAddress("test");

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl("some-name", a);

        assertTrue(new JBossControllerAddress("test").equals(d.getAddress()));
        assertEquals("some-name", d.getName());
        assertEquals(MetricSourceType.JBOSS_CONTROLLER, d.getType());
    }

    @Test
    public void constructor_InferType_NonDescriptAddress() throws Exception {

        AddressImpl a = new AddressImpl("test");

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl("some-name", a);

        assertTrue(new AddressImpl("test").equals(d.getAddress()));
        assertEquals("some-name", d.getName());
        assertNull(d.getType());
    }

    // YAML constructor ------------------------------------------------------------------------------------------------

    @Test
    public void constructor_yaml_NullName() throws Exception {

        try {
            new MetricSourceDefinitionImpl(null, new HashMap());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null source name"));
        }
    }

    @Test
    public void constructor_yaml_NullMap() throws Exception {

        try {

            new MetricSourceDefinitionImpl("something", (Object)null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null source definition representation"));
        }
    }

    @Test
    public void constructor_yaml_NoType() throws Exception {

        String s =
                "some-source:\n" +
                "  host: something\n" +
                "  port: 80\n";

        Map m = YamlUtil.parse(s);

        try {

            new MetricSourceDefinitionImpl("some-source", m.get("some-source"));
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unspecified type for source"));
        }
    }

    @Test
    public void constructor_yaml_InvalidType() throws Exception {

        String s =
                "some-source:\n" +
                        "  type: we-are-sure-there-is-no-such-type\n" +
                        "  host: something\n" +
                        "  port: 80\n";

        Map m = YamlUtil.parse(s);

        try {

            new MetricSourceDefinitionImpl("some-source", m.get("some-source"));
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid metric source type \"we-are-sure-there-is-no-such-type\""));
        }
    }

    @Test
    public void constructor_yaml_MissingHostName() throws Exception {

        String s =
                "some-source:\n" +
                        "  type: jboss-controller\n" +
                        "  port: 9999\n" +
                        "  classpath:\n" +
                        "    - some.jar\n" +
                        "    - some/other.jar\n";

        Map m = YamlUtil.parse(s);

        try {

            new MetricSourceDefinitionImpl("some-source", m.get("some-source"));
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing host"));
        }
    }

    @Test
    public void constructor_yaml_JBossController() throws Exception {

        String s =
                "some-source:\n" +
                "  type: jboss-controller\n" +
                "  host: something\n" +
                "  port: 9999\n" +
                "  classpath:\n" +
                "    - some.jar\n" +
                "    - some/other.jar\n";

        Map m = YamlUtil.parse(s);

        String name = "some-source";

        MetricSourceDefinitionImpl d = new MetricSourceDefinitionImpl(name, m.get(name));

        assertEquals("some-source", d.getName());
        assertEquals(MetricSourceType.JBOSS_CONTROLLER, d.getType());
        JBossControllerAddress a = (JBossControllerAddress)d.getAddress();
        assertEquals("something", a.getHost());
        assertEquals(9999, a.getPort().intValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MetricSourceDefinitionImpl getMetricSourceDefinitionToTest() throws Exception {

        AddressImpl address = new AddressImpl("mock");
        return new MetricSourceDefinitionImpl(null, address);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
