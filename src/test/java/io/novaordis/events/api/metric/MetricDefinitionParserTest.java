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

import io.novaordis.events.api.metric.jboss.JBossDmrMetricDefinitionImpl;
import io.novaordis.events.api.metric.jmx.JmxMetricDefinitionImpl;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryFree;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        MetricDefinition d = MetricDefinitionParser.parse("PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los = (LocalOSAddress)s;
        assertNotNull(los);
    }

    @Test
    public void parse_NewLocalOSMetricDefinition() throws Exception {

        MetricDefinition d = MetricDefinitionParser.parse("PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los = (LocalOSAddress)s;
        assertNotNull(los);
    }

    @Test
    public void parse_NewRemoteOSMetricDefinition() throws Exception {

        MetricDefinition d = MetricDefinitionParser.parse("ssh://test-remote-host:22/PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        Address s = m.getMetricSourceAddress();
        assertNotNull(s);
    }

    @Test
    public void parse_NewDefaultJBossControllerMetricDefinition() throws Exception {

        String mds = "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mds);

        JBossDmrMetricDefinitionImpl jmd = (JBossDmrMetricDefinitionImpl)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertNotNull(s);

        String definition = jmd.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);
    }

    @Test
    public void parse_NewRemoteJBossControllerMetricDefinition() throws Exception {

        String mds =
                "jbosscli://admin:passwd@1.2.3.4:9999/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count";

        MetricDefinition d = MetricDefinitionParser.parse(mds);

        JBossDmrMetricDefinitionImpl jmd = (JBossDmrMetricDefinitionImpl)d;

        JBossControllerAddress s = jmd.getMetricSourceAddress();
        assertNotNull(s);

        String definition = d.getId();
        assertEquals("/subsystem=messaging/hornetq-server=default/jms-queue=DLQ/message-count", definition);
    }

    @Test
    public void parse_NewJmxBusMetricDefinition() throws Exception {

        String s =
                "jmx://admin:adminpasswd@1.2.3.4:2345/jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount";

        MetricDefinition d = MetricDefinitionParser.parse(s);

        JmxMetricDefinitionImpl jmxm = (JmxMetricDefinitionImpl)d;

        Address a = jmxm.getMetricSourceAddress();
        assertNotNull(a);

        String definition = jmxm.getId();
        assertEquals("jboss.as:subsystem=messaging,hornetq-server=default,jms-queue=DLQ/messageCount", definition);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
