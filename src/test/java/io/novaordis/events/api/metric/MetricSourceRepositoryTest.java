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
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public abstract class MetricSourceRepositoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void emptyRepository() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        assertTrue(r.isEmpty());

        Set<LocalOS> sources = r.getSources(LocalOS.class);
        assertTrue(sources.isEmpty());

        Set<RemoteOS> sources2 = r.getSources(RemoteOS.class);
        assertTrue(sources2.isEmpty());

        Set<JBossController> sources3 = r.getSources(JBossController.class);
        assertTrue(sources3.isEmpty());

        Set<JmxBus> sources4 = r.getSources(JmxBus.class);
        assertTrue(sources4.isEmpty());
    }

    @Test
    public void addNull() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        try {
            r.add(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null source", msg);
        }
    }

    @Test
    public void add() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        LocalOS los = new LocalOS();
        r.add(los);

        RemoteOS ros = new RemoteOS("ssh://1.2.3.4");
        r.add(ros);

        JBossController jbc = new JBossController();
        r.add(jbc);

        Set<LocalOS> sources = r.getSources(LocalOS.class);
        assertEquals(1, sources.size());
        assertTrue(sources.contains(los));

        Set<RemoteOS> sources2 = r.getSources(RemoteOS.class);
        assertEquals(1, sources2.size());
        assertTrue(sources2.contains(ros));

        Set<JBossController> sources3 = r.getSources(JBossController.class);
        assertEquals(1, sources3.size());
        assertTrue(sources3.contains(jbc));
    }

    // getSource() -----------------------------------------------------------------------------------------------------

    @Test
    public void getSource_MoreThanOneAddress() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        try {

            r.getSource(MockMetricSource.class, "address1", "address2");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("illegal to provide more than one address"));
        }
    }

    @Test
    public void getSource_MoreThanOneSourceKnownAndNoAddressIsSpecified() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        r.add(new RemoteOS("ssh://1.2.3.4"));
        r.add(new RemoteOS("ssh://1.2.3.5"));

        try {

            r.getSource(RemoteOS.class);
            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(
                    "no address was specified, but more than one " + RemoteOS.class.getSimpleName() +
                            " sources are known"));
        }
    }

    @Test
    public void getSource() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        //
        // add all
        //

        LocalOS los = new LocalOS();
        r.add(los);

        RemoteOS ros = new RemoteOS("ssh://1.2.3.4");
        r.add(ros);

        //
        // verify
        //

        LocalOS los2 = r.getSource(LocalOS.class);
        assertEquals(los, los2);

        LocalOS los3 = r.getSource(LocalOS.class, "does not matter");
        assertNull(los3);

        RemoteOS ros2 = r.getSource(RemoteOS.class);
        assertEquals(ros, ros2);

        RemoteOS ros3 = r.getSource(RemoteOS.class, "ssh://1.2.3.4");
        assertEquals(ros, ros3);

        RemoteOS ros4 = r.getSource(RemoteOS.class, "ssh://5.6.7.8");
        assertNull(ros4);
    }

    @Test
    public void getSource_JBossController() throws Exception {

        MetricSourceRepository r = getMetricSourceRepositoryToTest();

        //
        // add
        //

        JBossControllerAddress address = JBossControllerAddress.parseAddress("admin:admin123@1.2.3.4:8888");
        JBossController c = new JBossController(address);
        r.add(c);

        //
        // verify
        //

        String lookupAddress = c.getControllerAddress().getLiteral();

        JBossController c2 = r.getSource(JBossController.class, lookupAddress);

        assertEquals(c, c2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract MetricSourceRepository getMetricSourceRepositoryToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
