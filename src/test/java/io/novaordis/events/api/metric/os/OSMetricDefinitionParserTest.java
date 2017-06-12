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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSourceRepository;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.events.api.metric.os.mdefs.MockOSMetricDefinition;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryFree;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public class OSMetricDefinitionParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void parse_NoSuchOSMetric() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = OSMetricDefinitionParser.parse(mr, "IAmPrettySureThereIsNoSuchOsMetric");

        assertNull(d);

        localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());
    }

    @Test
    public void parse_NullRepository_NoSuchOSMetric() throws Exception {

        MetricDefinition d = OSMetricDefinitionParser.parse(null, "IAmPrettySureThereIsNoSuchOsMetric");
        assertNull(d);
    }

    @Test
    public void parse_ReflectionFailure() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        try {

            MockOSMetricDefinition.setFailInConstructor(true);

            OSMetricDefinitionParser.parse(mr, "MockOSMetricDefinition");
            fail("should have thrown exception");
        }
        catch(MetricException e) {

            String msg = e.getMessage();
            assertEquals("failed to instantiate metric \"MockOSMetricDefinition\"", msg);

            InvocationTargetException cause = (InvocationTargetException)e.getCause();

            RuntimeException re = (RuntimeException)cause.getTargetException();
            assertEquals("SYNTHETIC", re.getMessage());
        }
        finally {

            MockOSMetricDefinition.setFailInConstructor(false);
        }

        localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());
    }

    @Test
    public void parse_NullRepository_LocalOS() throws Exception {

        MetricDefinition d = OSMetricDefinitionParser.parse(null, "PhysicalMemoryFree");
        PhysicalMemoryFree m = (PhysicalMemoryFree)d;
        assertNotNull(m);

        LocalOS los = (LocalOS)d.getMetricSourceAddress();
        assertNotNull(los);
    }

    @Test
    public void parse_LocalOS() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = OSMetricDefinitionParser.parse(mr, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;
        assertNotNull(m);

        Address s = m.getMetricSourceAddress();
        LocalOSAddress los = (LocalOSAddress)s;
        assertNotNull(los);

        assertTrue(mr.getSources(LocalOS.class).isEmpty());
    }

    @Test
    public void parse_NullRepository_RemoteOS() throws Exception {

        MetricDefinition d = OSMetricDefinitionParser.parse(null, "ssh://1.2.3.4/PhysicalMemoryFree");
        PhysicalMemoryFree m = (PhysicalMemoryFree)d;
        assertNotNull(m);

        Address ros = d.getMetricSourceAddress();
        assertNotNull(ros);
        assertEquals(new AddressImpl("ssh://1.2.3.4"), ros);
    }

    @Test
    public void parse_RemoteOS() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = OSMetricDefinitionParser.parse(mr, "ssh://1.2.3.4/PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        assertNotNull(m);

        Address s = m.getMetricSourceAddress();
        assertEquals(new AddressImpl("ssh://1.2.3.4"), s);
        assertTrue(mr.getSources(LocalOS.class).isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
