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

import io.novaordis.events.api.metric.jboss.JBossCliMetricDefinition;
import io.novaordis.events.api.metric.jboss.JBossController;
import io.novaordis.events.api.metric.jmx.JmxBus;
import io.novaordis.events.api.metric.jmx.JmxMetricDefinition;
import io.novaordis.events.api.metric.os.LocalOS;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryFree;
import io.novaordis.events.api.metric.os.RemoteOS;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class MetricDefinitionParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_LocalOSMetricDefinition_NullRepository() throws Exception {

        MetricDefinition d = MetricDefinitionParser.parse(null, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los = (LocalOSAddress)s;
        assertNotNull(los);
    }

    @Test
    public void parse_NewLocalOSMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los = (LocalOSAddress)s;
        assertNotNull(los);

        mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());
    }

    @Test
    public void parse_ExistingLocalOSMetricDefinition() throws Exception {

        LocalOS los = new LocalOS();
        MetricSourceRepository mr = new MetricSourceRepositoryImpl();
        mr.add(los);

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertEquals(1, localOses.size());
        assertEquals(los, localOses.iterator().next());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los2 = (LocalOSAddress)s;
        assertEquals(los.getAddress(), los2);

        localOses = mr.getSources(LocalOS.class);
        assertEquals(1, localOses.size());
        assertEquals(los, localOses.iterator().next());
    }

    @Test
    public void parse_NewRemoteOSMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<RemoteOS> remoteOSes = mr.getSources(RemoteOS.class);
        assertTrue(remoteOSes.isEmpty());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "ssh://test-remote-host:22/PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        assertNotNull(s);

        remoteOSes = mr.getSources(RemoteOS.class);
        assertTrue(remoteOSes.isEmpty());
    }

    @Test
    public void parse_ExistingRemoteOSMetricDefinition() throws Exception {

        RemoteOS ros = new RemoteOS("ssh://test-remote-host:22");

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();
        mr.add(ros);

        Set<RemoteOS> remoteOSes = mr.getSources(RemoteOS.class);
        assertEquals(1, remoteOSes.size());
        assertEquals(ros, remoteOSes.iterator().next());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "ssh://test-remote-host:22/PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        assertEquals(ros.getAddress(), s);

        remoteOSes = mr.getSources(RemoteOS.class);
        assertEquals(1, remoteOSes.size());
        assertEquals(ros, remoteOSes.iterator().next());
    }

    @Test
    public void parse_NewDefaultJBossControllerMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());

        String mds = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition jmd = (JBossCliMetricDefinition)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertNotNull(s);

        String definition = jmd.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);

        controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());
    }

    @Test
    public void parse_ExistingDefaultJBossControllerMetricDefinition() throws Exception {

        JBossController c = new JBossController();

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        mr.add(c);

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());

        String mds = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition jmd = (JBossCliMetricDefinition)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertEquals(c.getAddress(), s);

        String definition = jmd.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);

        controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());
        JBossController c3 = controllers.iterator().next();

        assertEquals(c, c3);
    }

    @Test
    public void parse_NewRemoteJBossControllerMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());

        String mds =
                "jbosscli://admin:passwd@1.2.3.4:9999/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition jmd = (JBossCliMetricDefinition)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertNotNull(s);

        String definition = d.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);

        controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());
    }

    @Test
    public void parse_ExistingRemoteJBossControllerMetricDefinition() throws Exception {

        JBossController c = new JBossController(new JBossControllerAddress("jbosscli://admin:passwd@1.2.3.4:9999"));

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        mr.add(c);

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());

        String mds =
                "jbosscli://admin:passwd@1.2.3.4:9999/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition jmd = (JBossCliMetricDefinition)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertEquals(c.getAddress(), s);

        String definition = d.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);

        controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());
        JBossController c3 = controllers.iterator().next();

        assertEquals(c, c3);
    }

    @Test
    public void parse_NewJmxBusMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JmxBus> buses = mr.getSources(JmxBus.class);
        assertTrue(buses.isEmpty());

        String s =
                "jmx://admin:adminpasswd@1.2.3.4:2345/jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount";

        MetricDefinition d = MetricDefinitionParser.parse(mr, s);

        JmxMetricDefinition jmxm = (JmxMetricDefinition)d;

        Address a = jmxm.getMetricSourceAddress();
        assertNotNull(a);

        String definition = jmxm.getId();
        assertEquals("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount", definition);

        buses = mr.getSources(JmxBus.class);
        assertTrue(buses.isEmpty());
    }

    @Test
    public void parse_ExistingJmxBusMetricDefinition() throws Exception {

        JmxBus b = new JmxBus("jmx://admin:adminpasswd@1.2.3.4:2345");

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        mr.add(b);

        Set<JmxBus> buses = mr.getSources(JmxBus.class);
        assertEquals(1, buses.size());

        String s =
                "jmx://admin:adminpasswd@1.2.3.4:2345/jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount";

        MetricDefinition d = MetricDefinitionParser.parse(mr, s);

        JmxMetricDefinition jmxm = (JmxMetricDefinition)d;

        Address b2 = jmxm.getMetricSourceAddress();
        assertEquals(b.getAddress(), b2);

        String definition = jmxm.getId();
        assertEquals("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount", definition);

        buses = mr.getSources(JmxBus.class);
        assertEquals(1, buses.size());
        JmxBus b3 = buses.iterator().next();
        assertEquals(b, b3);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
