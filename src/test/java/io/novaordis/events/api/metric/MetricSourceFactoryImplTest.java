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

import io.novaordis.events.api.metric.jboss.JBossController;
import io.novaordis.events.api.metric.jmx.JmxBus;
import io.novaordis.events.api.metric.os.LocalOS;
import io.novaordis.events.api.metric.os.RemoteOS;
import io.novaordis.jboss.cli.model.JBossControllerAddress;
import io.novaordis.jmx.JmxAddress;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.address.OSAddressImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/13/17
 */
public class MetricSourceFactoryImplTest extends MetricSourceFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // buildMetricSource() ---------------------------------------------------------------------------------------------

    @Test
    public void buildMetricSource_NullAddress() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        try {

            f.buildMetricSource(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null address"));
        }
    }

    @Test
    public void buildMetricSource_LocalOS() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        MetricSource s = f.buildMetricSource(new LocalOSAddress());

        LocalOS os = (LocalOS)s;
        assertNotNull(os);

        assertEquals(new LocalOSAddress(), os.getAddress());

        MetricSource s2 = f.buildMetricSource(new LocalOSAddress());

        LocalOS os2 = (LocalOS)s2;
        assertNotNull(os2);

        assertTrue(os.equals(os2));
    }

    @Test
    public void buildMetricSource_RemoteSshOS() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        MetricSource s = f.buildMetricSource(new OSAddressImpl("ssh://mock-ssh-host"));

        RemoteOS os = (RemoteOS)s;
        assertNotNull(os);

        assertTrue(new OSAddressImpl("ssh://mock-ssh-host").equals(os.getAddress()));

        MetricSource s2 = f.buildMetricSource(new OSAddressImpl("ssh://mock-ssh-host"));

        RemoteOS os2 = (RemoteOS)s2;
        assertNotNull(os2);

        assertTrue(os.equals(os2));
    }

    @Test
    public void buildMetricSource_JmxBus() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        MetricSource s = f.buildMetricSource(new JmxAddress("localhost:1234"));

        JmxBus jmxBus = (JmxBus)s;
        assertNotNull(jmxBus);

        assertEquals(new JmxAddress("localhost:1234"), jmxBus.getAddress());

        MetricSource s2 = f.buildMetricSource(new JmxAddress("localhost:1234"));

        JmxBus jmxBus2 = (JmxBus)s2;
        assertNotNull(jmxBus2);

        assertTrue(jmxBus.equals(jmxBus2));
    }

    @Test
    public void buildMetricSource_JBossController() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        MetricSource s = f.buildMetricSource(new JBossControllerAddress("jbosscli://localhost:1234/"));

        JBossController jc = (JBossController)s;
        assertNotNull(jc);

        assertTrue(new JBossControllerAddress("jbosscli://localhost:1234/").equals(jc.getAddress()));

        MetricSource s2 = f.buildMetricSource(new JBossControllerAddress("jbosscli://localhost:1234/"));

        JBossController jc2 = (JBossController)s2;
        assertNotNull(jc2);

        assertTrue(jc.equals(jc2));
    }

    @Test
    public void buildMetricSource_JBossController_RegularAddressImplInsteadOfJBossControllerAddress() throws Exception {

        //
        // I am not sure how to handle this yet, we may need to refactor, but currently an AddressImpl("jbosscli://...")
        // is not a valid way to handle JBossControllers and JBossController creation
        //

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        AddressImpl notGood = new AddressImpl("jbosscli://localhost:1234/");

        try {

            f.buildMetricSource(notGood);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("use a JBossControllerAddress instance"));
        }
    }

    // setDelegateFactory() --------------------------------------------------------------------------------------------

    @Test
    public void setDelegateFactory() throws Exception {

        MetricSourceFactoryImpl f = getMetricSourceFactoryToTest();

        f.setDelegateFactory(MockMetricSource::new);

        MockAddress ma = new MockAddress("something");
        MetricSource s = f.buildMetricSource(ma);

        assertEquals(ma, s.getAddress());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MetricSourceFactoryImpl getMetricSourceFactoryToTest() throws Exception {

        return new MetricSourceFactoryImpl();
    }


    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
