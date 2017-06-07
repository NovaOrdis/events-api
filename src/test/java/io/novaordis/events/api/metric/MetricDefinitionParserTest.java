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
import io.novaordis.events.api.metric.os.mdefs.LocalOS;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryFree;
import io.novaordis.events.api.metric.os.RemoteOS;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    public void parse_LocalOSMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        MetricSource s = m.getSource();
        LocalOS los = (LocalOS)s;

        localOses = mr.getSources(LocalOS.class);
        assertEquals(1, localOses.size());
        LocalOS los2 = localOses.iterator().next();

        assertEquals(los, los2);
    }

    @Test
    public void parse_RemoteOSMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<RemoteOS> remoteOSes = mr.getSources(RemoteOS.class);
        assertTrue(remoteOSes.isEmpty());

        MetricDefinition d = MetricDefinitionParser.parse(mr, "ssh://test-remote-host:22/PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        MetricSource s = m.getSource();
        RemoteOS ros = (RemoteOS)s;

        remoteOSes = mr.getSources(RemoteOS.class);
        assertEquals(1, remoteOSes.size());
        RemoteOS ros2 = remoteOSes.iterator().next();

        assertEquals(ros, ros2);
    }

    @Test
    public void parse_DefaultJBossControllerMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());

        String mds = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition cli = (JBossCliMetricDefinition)d;

        MetricSource s = cli.getSource();
        JBossController c = (JBossController)s;

        controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());
        JBossController c2 = controllers.iterator().next();

        assertEquals(c, c2);

        String definition = d.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);
    }

    @Test
    public void parse_RemoteJBossControllerMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JBossController> controllers = mr.getSources(JBossController.class);
        assertTrue(controllers.isEmpty());

        String mds =
                "jbosscli://admin:passwd@1.2.3.4:9999/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mr, mds);

        JBossCliMetricDefinition cli = (JBossCliMetricDefinition)d;

        MetricSource s = cli.getSource();
        JBossController c = (JBossController)s;

        controllers = mr.getSources(JBossController.class);
        assertEquals(1, controllers.size());
        JBossController c2 = controllers.iterator().next();

        assertEquals(c, c2);

        String definition = d.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);
    }

    @Test
    public void parse_JmxBusMetricDefinition() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<JmxBus> buses = mr.getSources(JmxBus.class);
        assertTrue(buses.isEmpty());

        String s =
                "jmx://admin:adminpasswd@1.2.3.4:2345/jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount";

        MetricDefinition d = MetricDefinitionParser.parse(mr, s);

        JmxMetricDefinition jmxm = (JmxMetricDefinition)d;

        JmxBus b = jmxm.getSource();

        buses = mr.getSources(JmxBus.class);
        assertEquals(1, buses.size());
        JmxBus b2 = buses.iterator().next();

        assertEquals(b, b2);

        String definition = jmxm.getId();
        assertEquals("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount", definition);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
