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
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.MetricSourceRepository;
import io.novaordis.events.api.metric.MetricSourceRepositoryImpl;
import io.novaordis.events.api.metric.MockMetricDefinition;
import io.novaordis.events.api.metric.os.mdefs.MockOSMetricDefinition;
import io.novaordis.events.api.metric.os.mdefs.PhysicalMemoryFree;
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
    public void parse_ReflectionFailure() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        try {

            MockOSMetricDefinition.FAIL_IN_CONSTRUCTOR = true;

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

            MockMetricDefinition.FAIL_IN_CONSTRUCTOR = false;
        }

        localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());
    }

    @Test
    public void parse_LocalOS() throws Exception {

        MetricSourceRepository mr = new MetricSourceRepositoryImpl();

        Set<LocalOS> localOses = mr.getSources(LocalOS.class);
        assertTrue(localOses.isEmpty());

        MetricDefinition d = OSMetricDefinitionParser.parse(mr, "PhysicalMemoryFree");

        PhysicalMemoryFree m = (PhysicalMemoryFree)d;

        assertNotNull(m);

        MetricSource s = m.getSource();
        LocalOS los = (LocalOS)s;

        localOses = mr.getSources(LocalOS.class);
        assertEquals(1, localOses.size());
        LocalOS los2 = localOses.iterator().next();

        assertEquals(los, los2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
