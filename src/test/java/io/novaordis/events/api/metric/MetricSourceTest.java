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

package io.novaordis.events.api.metric;

import io.novaordis.events.api.event.Property;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/4/16
 */
public abstract class MetricSourceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public abstract void equalsTest() throws Exception;

    @Test
    public abstract void hashCodeTest() throws Exception;

    // hasAddress() ----------------------------------------------------------------------------------------------------

    @Test
    public void hasAddress_DoesNotHaveTheAddress() throws Exception {

        MetricSource s = getMetricSourceToTest();
        assertFalse(s.hasAddress("I am sure the metric does not have this address"));
    }

    @Test
    public void hasAddress_Identity() throws Exception {

        MetricSource s = getMetricSourceToTest();

        String address = s.getAddress();

        if (address != null) {

            assertTrue(s.hasAddress(address));
        }
    }

    @Test
    public void hasAddress_Null() throws Exception {

        MetricSource s = getMetricSourceToTest();
        assertFalse(s.hasAddress(null));
    }

    // collectMetrics() ------------------------------------------------------------------------------------------------

    @Test
    public void collectMetrics_DefinitionsHaveADifferentSource() throws Exception {

        MetricSource s = getMetricSourceToTest();

        MockMetricDefinition md = new MockMetricDefinition(new MockMetricSource());

        try {

            s.collectMetrics(Collections.singletonList(md));
            fail("should have thrown exception");
        }
        catch(MetricException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("has a different source than"));
        }
    }

    @Test
    public void collectMetrics_SourceDoesNotProduceMetricForADefinition() throws Exception {

        MetricSource s = getMetricSourceToTest();

        MockMetricDefinition mmd = new MockMetricDefinition(s);

        //noinspection ArraysAsListWithZeroOrOneArgument
        List<MetricDefinition> definitions = Arrays.asList(mmd);

        List<Property> properties = s.collectMetrics(definitions);

        assertNotNull(properties);
        assertEquals(1, properties.size());

        Property p = properties.get(0);
        assertNull(p);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract MetricSource getMetricSourceToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
