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
    public void collectMetrics_DefinitionsHaveDifferentSources() throws Exception {

        MetricSource source = getMetricSourceToTest();

        MockMetricDefinition md = getCorrespondingMockMetricDefinition(source);

        MetricSource source2 = getMetricSourceToTest("other-host");

        MockMetricDefinition md2 = getCorrespondingMockMetricDefinition(source2);

        try {

            source.collectMetrics(Arrays.asList(md, md2));
            fail("should have thrown exception");
        }
        catch(MetricException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("has a different source than"));
        }
    }

    @Test
    public final void collectMetrics_SourceDoesNotProduceMetricForADefinition() throws Exception {

        MetricSource s = getMetricSourceToTest();

        MockMetricDefinition mmd = getCorrespondingMockMetricDefinition(s);

        List<MetricDefinition> definitions = Collections.singletonList(mmd);

        List<Property> properties = s.collectMetrics(definitions);

        assertNotNull(properties);
        assertEquals(1, properties.size());

        Property p = properties.get(0);

        //
        // non-null property but with a null value
        //
        assertNotNull(p);
        assertNull(p.getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Gives the sub-classes a chance to provide more specialized mocks.
     */
    protected MockMetricDefinition getCorrespondingMockMetricDefinition(MetricSource source) {

        return new MockMetricDefinition(source);
    }

    /**
     * @param addresses needed to simulate "different" metric source. If not specified, the default metric source
     *                is returned. Only the first argument is used, if more than one is sent, we will throw
     *                IllegalArgumentException.
     */
    protected abstract MetricSource getMetricSourceToTest(String... addresses) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
