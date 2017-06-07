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

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricDefinitionTest;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.jboss.cli.JBossControllerClient;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class JBossCliMetricDefinitionTest extends MetricDefinitionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    @Test
    public void getDefinition() throws Exception {

        JBossCliMetricDefinition d = getMetricDefinitionToTest();
        assertEquals("/test=test/test", d.getId());
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullArgument() throws Exception {

        try {

            new JBossCliMetricDefinition(null, null, null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null"));
        }
    }

    // getId() -------------------------------------------------------------------------------------------------

    @Test
    public void getDefinition_DefaultController() throws Exception {

        JBossCliMetricDefinition d = new JBossCliMetricDefinition(
                new JBossController(), new CliPath("test-path"), new CliAttribute("test-attribute"));

        String definition = d.getId();
        assertEquals("/test-path/test-attribute", definition);
    }

    @Test
    public void getDefinition_NonDefaultController() throws Exception {

        JBossCliMetricDefinition d = new JBossCliMetricDefinition(
                new JBossController(JBossControllerAddress.parseAddress("admin:adminp@1.2.3.4:8888")),
                new CliPath("test-path"), new CliAttribute("test-attribute"));

        String definition = d.getId();
        assertEquals("/test-path/test-attribute", definition);
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_JBoss_NoHostNoPort() throws Exception {

        String s = "jbosscli:///a=b/c=d/f";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(new MetricSourceRepositoryImpl(), s);

        assertNotNull(d);
        
        JBossController source = d.getSource();

        JBossControllerAddress controllerAddress = source.getControllerAddress();

        assertEquals(JBossControllerClient.DEFAULT_HOST, controllerAddress.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, controllerAddress.getPort());

        CliPath path = d.getPathInstance();

        assertEquals("/a=b/c=d", path.getPath());
        assertEquals("/a=b/c=d", d.getPath());

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());

        assertEquals("/a=b/c=d/f", d.getSimpleLabel());
        assertEquals("/a=b/c=d/f", d.getDescription());
    }

    @Test
    public void parse_JBoss_LocalhostNoPort() throws Exception {

        String s = "jbosscli://localhost/a=b/c=d/f";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(new MetricSourceRepositoryImpl(), s);

        assertNotNull(d);

        JBossController source = d.getSource();

        JBossControllerAddress controllerAddress = source.getControllerAddress();

        assertEquals("localhost", controllerAddress.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, controllerAddress.getPort());

        CliPath path = d.getPathInstance();

        assertEquals("/a=b/c=d", path.getPath());
        assertEquals("/a=b/c=d", d.getPath());

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());

        assertEquals("/a=b/c=d/f", d.getSimpleLabel());
        assertEquals("/a=b/c=d/f", d.getDescription());
    }

    @Test
    public void parse_JBoss_HostNoPort() throws Exception {

        String s = "jbosscli://blue/a=b/c=d/f";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(new MetricSourceRepositoryImpl(), s);

        assertNotNull(d);

        JBossController source = d.getSource();

        JBossControllerAddress controllerAddress = source.getControllerAddress();

        assertEquals("blue", controllerAddress.getHost());
        assertEquals(JBossControllerClient.DEFAULT_PORT, controllerAddress.getPort());

        CliPath path = d.getPathInstance();

        assertEquals("/a=b/c=d", path.getPath());
        assertEquals("/a=b/c=d", d.getPath());

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());

        assertEquals("/a=b/c=d/f", d.getSimpleLabel());
        assertEquals("/a=b/c=d/f", d.getDescription());
    }

    @Test
    public void parse_JBoss_LocalhostAndPort() throws Exception {

        String s = "jbosscli://localhost:9999/a=b/c=d/f";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(new MetricSourceRepositoryImpl(), s);

        assertNotNull(d);

        JBossController source = d.getSource();

        JBossControllerAddress controllerAddress = source.getControllerAddress();

        assertEquals("localhost", controllerAddress.getHost());
        assertEquals(9999, controllerAddress.getPort());

        CliPath path = d.getPathInstance();

        assertEquals("/a=b/c=d", path.getPath());
        assertEquals("/a=b/c=d", d.getPath());

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());

        assertEquals("/a=b/c=d/f", d.getSimpleLabel());
        assertEquals("/a=b/c=d/f", d.getDescription());
    }

    @Test
    public void parse_JBoss_HostAndPort() throws Exception {

        String s = "jbosscli://blue:9999/a=b/c=d/f";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(new MetricSourceRepositoryImpl(), s);

        assertNotNull(d);

        JBossController source = d.getSource();

        JBossControllerAddress controllerAddress = source.getControllerAddress();

        assertEquals("blue", controllerAddress.getHost());
        assertEquals(9999, controllerAddress.getPort());

        CliPath path = d.getPathInstance();

        assertEquals("/a=b/c=d", path.getPath());
        assertEquals("/a=b/c=d", d.getPath());

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());

        assertEquals("/a=b/c=d/f", d.getSimpleLabel());
        assertEquals("/a=b/c=d/f", d.getDescription());
    }

    @Test
    public void parse_InvalidMetricDefinition() throws Exception {

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();

        try {

            JBossCliMetricDefinitionParser.parse(r, "jbosscli:///this-should-fail");
            fail("should have thrown exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid jboss CLI metric"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JBossCliMetricDefinition getMetricDefinitionToTest() throws Exception {

        return new JBossCliMetricDefinition(new JBossController(), new CliPath("test=test"), new CliAttribute("test") );
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
