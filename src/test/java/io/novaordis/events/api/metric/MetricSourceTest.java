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
import io.novaordis.events.api.metric.os.MockOSSource;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.address.AddressImpl;
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

    // hasAddress() ----------------------------------------------------------------------------------------------------

    @Test
    public void hasAddress_DoesNotHaveTheAddress() throws Exception {

        MetricSource s = getMetricSourceToTest();

        Address a = new AddressImpl("test://i-am-sure-the-metric-does-not-have-this-address");
        assertFalse(s.hasAddress(a));
    }

    @Test
    public void hasAddress_Identity() throws Exception {

        MetricSource s = getMetricSourceToTest();

        Address address = s.getAddress();

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

        MetricDefinition md = getCorrespondingMockMetricDefinition(source.getAddress());

        MetricSource source2 = getMetricSourceToTest("other-host");

        MetricDefinition md2 = getCorrespondingMockMetricDefinition(source2.getAddress());

        try {

            source.collectMetrics(Arrays.asList(md, md2));
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("has a different source than"));
        }
    }

    @Test
    public final void collectMetrics_SourceDoesNotProduceMetricForADefinition() throws Exception {

        MetricSource s = getMetricSourceToTest();

        MetricDefinition mmd = getCorrespondingMockMetricDefinition(s.getAddress());

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

    // collect() -------------------------------------------------------------------------------------------------------

    @Test
    public void collect_FailIfSourceNotStarted() throws Exception {

        MetricSourceBase s = (MetricSourceBase)getMetricSourceToTest();

        if (s.isStarted()) {

            //
            // noop, this test does not apply
            //
            return;
        }

        MetricDefinition md = getCorrespondingMockMetricDefinition(s.getAddress());

        try {

            s.collect(Collections.singletonList(md));
            fail("should have thrown exceptions");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not started"));
        }
    }

    @Test
    public void collect_SourceAddressNotRelevant() throws Exception {

        MetricSourceBase source = (MetricSourceBase)getMetricSourceToTest();
        MetricDefinition md = getCorrespondingMockMetricDefinition(source.getAddress());

        MetricSource source2 = getMetricSourceToTest("other-host");
        MetricDefinition md2 = getCorrespondingMockMetricDefinition(source2.getAddress());

        source.start();

        List<Property> result = source.collect(Arrays.asList(md, md2));

        //
        // we don't care about result at this point, it is important that the method does not fail
        //
        assertNotNull(result);
    }

    // lifecycle -------------------------------------------------------------------------------------------------------

    @Test
    public void start() throws Exception {

        fail("RETURN HERE");
    }

    @Test
    public void stop() throws Exception {

        fail("RETURN HERE");
    }

    // equals() and related --------------------------------------------------------------------------------------------

    @Test
    public abstract void equalsTest() throws Exception;

    @Test
    public void equalsTest_Null() throws Exception {

        MetricSource s = getMetricSourceToTest();
        final Object nullReference = null;
        assertFalse(s.equals(nullReference));
    }

    @Test
    public void equalsTest_NotSameClass() throws Exception {

        MetricSource s = getMetricSourceToTest();
        MockOSSource mos = new MockOSSource(new AddressImpl("test://test"));

        assertFalse(s.equals(mos));
        assertFalse(mos.equals(s));
    }

    @Test
    public abstract void hashCodeTest() throws Exception;

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Gives the sub-classes a chance to provide more specialized mocks.
     */
    protected MetricDefinition getCorrespondingMockMetricDefinition(Address metricSourceAddress) {

        return new MockMetricDefinition(metricSourceAddress);
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
