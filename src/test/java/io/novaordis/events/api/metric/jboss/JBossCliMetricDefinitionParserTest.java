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

package io.novaordis.events.api.metric.jboss;

import io.novaordis.events.api.metric.MetricDefinitionException;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class JBossCliMetricDefinitionParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void parse_NotAValidJBossCLIMetricDefinition() throws Exception {

        String s = "I am pretty sure this is not a valid JBoss CLI metric definition";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);
        assertNull(d);

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NullRepository_NotAValidJBossCLIMetricDefinition() throws Exception {

        String s = "I am pretty sure this is not a valid JBoss CLI metric definition";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(null, s);
        assertNull(d);
    }

    @Test
    public void parse_NullRepository_DefaultController() throws Exception {

        String s = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(null, s);

        assertNotNull(d);

        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getId());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ", d.getPath());
        assertEquals("message-count", d.getAttributeName());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());

        JBossController c = d.getSource();
        assertNotNull(c);
    }

    @Test
    public void parse_DefaultController() throws Exception {

        String s = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        JBossController c = d.getSource();
        assertNotNull(c);

        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getId());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ", d.getPath());
        assertEquals("message-count", d.getAttributeName());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NullRepository_ExplicitController() throws Exception {

        String s =
                "jbosscli://admin:apsswd@1.2.3.4:8888/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(null, s);

        assertNotNull(d);

        JBossController c = d.getSource();
        assertNotNull(c);

        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getId());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ", d.getPath());
        assertEquals("message-count", d.getAttributeName());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());
    }

    @Test
    public void parse_ExplicitController() throws Exception {

        String s =
                "jbosscli://admin:apsswd@1.2.3.4:8888/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        JBossController c = d.getSource();
        assertNotNull(c);

        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getId());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ", d.getPath());
        assertEquals("message-count", d.getAttributeName());
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", d.getDescription());
        assertNull(d.getBaseUnit());
        assertNull(d.getType());

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_UnknownProtocol() throws Exception {

        String s = "something://admin@1.2.3.4:8888/test=test/test";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNull(d);
        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NoSlash() throws Exception {

        String s = "admin@1.2.3.4:8888";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNull(d);
        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_NoSlash_WeKnowItIsAJBossCliMetric() throws Exception {

        String s = "jbosscli://something";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();

        try {

            JBossCliMetricDefinitionParser.parse(r, s);
            fail("should have thrown exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no / found in metric definition"));
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_InvalidJBossControllerAddressPort() throws Exception {

        String s = "admin:adminpassword@1.2.3.4:blah/test=test/test";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNull(d);
        assertTrue(r.isEmpty());
    }

    @Test
    public void parse_InvalidJBossControllerAddressPort_WeKnowItIsAJBossCliMetric() throws Exception {

        String s = "jbosscli://admin:adminpassword@1.2.3.4:blah/test=test/test";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();

        try {

            JBossCliMetricDefinitionParser.parse(r, s);
            fail("should have thrown exception");
        }
        catch(MetricDefinitionException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot get a jboss controller address from"));

            Throwable cause = e.getCause();
            String msg2 = cause.getMessage();
            assertTrue(msg2.contains("invalid"));
            assertTrue(msg2.contains("port"));
        }

        assertTrue(r.isEmpty());
    }

    @Test
    public void parse() throws Exception {

        String s = "jbosscli://some-host:1000/a=b/c=d/f";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());
        assertEquals("f", d.getAttributeName());

        CliPath pathInstance = d.getPathInstance();
        assertEquals("/a=b/c=d", pathInstance.getPath());

        String path = d.getPath();
        assertEquals("/a=b/c=d", path);
    }

    @Test
    public void constructor_UsernameAndPassword() throws Exception {

        String s = "jbosscli://some-user:some-password@some-host:1000/a=b/c=d/f";

        MetricSourceRepositoryImpl r = new MetricSourceRepositoryImpl();
        assertTrue(r.isEmpty());

        JBossCliMetricDefinition d = JBossCliMetricDefinitionParser.parse(r, s);

        assertNotNull(d);

        CliAttribute attribute = d.getAttribute();
        assertEquals("f", attribute.getName());
        assertEquals("f", d.getAttributeName());

        CliPath pathInstance = d.getPathInstance();
        assertEquals("/a=b/c=d", pathInstance.getPath());

        String path = d.getPath();
        assertEquals("/a=b/c=d", path);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
