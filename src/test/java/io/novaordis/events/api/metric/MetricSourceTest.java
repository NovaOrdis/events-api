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
import io.novaordis.events.api.event.PropertyFactory;
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
    public void collectMetrics_Null() throws Exception {

        MetricSource source = getMetricSourceToTest();

        try {

            source.collectMetrics(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null"));
        }
    }

    @Test
    public void collectMetrics_NoDefinitions() throws Exception {

        MetricSource source = getMetricSourceToTest();

        List<Property> result = source.collectMetrics(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    public void collectMetrics_DefinitionBoundToADifferentSource() throws Exception {

        MetricSource source = getMetricSourceToTest();

        MetricDefinition md = getCorrespondingMockMetricDefinition(source.getAddress());

        MetricSource source2 = getMetricSourceToTest("other-host:1000");

        MetricDefinition md2 = getCorrespondingMockMetricDefinition(source2.getAddress());

        try {

            source.collectMetrics(Arrays.asList(md, md2));
            fail("should have thrown exception");
        }
        catch(MetricSourceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("is bound to a different source than"));
            assertTrue(msg.contains("" + source));
            assertTrue(msg.contains("" + source2));
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

    @Test
    public final void collectMetrics_PropertyNameIsMetricDefinitionID() throws Exception {

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

        String metricDefinitionId = mmd.getId();
        String propertyName = p.getName();

        assertEquals(propertyName, metricDefinitionId);

        //
        // the value may or may be non-null, it depends on the implementation
        //
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

    /**
     * We test the fact that at this level (collect()), we don't care anymore about the metric definition source,
     * we trust it was checked by the upper layer (collectMetrics()), and we simply ignore it.
     * @throws Exception
     */
    @Test
    public void collect_SourceAddressNotRelevant() throws Exception {

        MetricSourceBase source = (MetricSourceBase)getMetricSourceToTest();
        MetricDefinition md = getCorrespondingMockMetricDefinition(source.getAddress());

        MetricSource source2 = getMetricSourceToTest("other-host:1000");
        MetricDefinition md2 = getCorrespondingMockMetricDefinition(source2.getAddress());

        source.start();

        List<Property> result = source.collect(Arrays.asList(md, md2));

        //
        // we don't care about result at this point, it is important that the method does not fail
        //
        assertNotNull(result);
    }

    @Test
    public void collect_InvalidMetricType() throws Exception {

        MetricSourceBase source = (MetricSourceBase)getMetricSourceToTest();

        PropertyFactory f = new PropertyFactory();

        //
        // this metric should be rejected as it is not known by any of the actual implementations
        //
        MetricDefinition md = new MockMetricDefinition(f, new AddressImpl("test"));

        source.start();

        try {

            source.collect(Collections.singletonList(md));
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("does not handle"));
        }
    }

    // lifecycle -------------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle() throws Exception {

        MetricSource ms = getMetricSourceToTest();

        assertFalse(ms.isStarted());

        ms.start();

        assertTrue(ms.isStarted());

        //
        // idempotence
        //

        ms.start();

        assertTrue(ms.isStarted());

        //
        // collect a random metric that surely does not exist - the corresponding property with an empty value
        // must return, though
        //

        MetricDefinition md = getCorrespondingMockMetricDefinition(ms.getAddress());

        List<Property> properties = ms.collectMetrics(Collections.singletonList(md));

        assertEquals(1, properties.size());

        Property p = properties.get(0);

        assertEquals(md.getId(), p.getName());
        assertNull(p.getValue());

        //
        // stop
        //

        ms.stop();

        assertFalse(ms.isStarted());

        //
        // idempotence
        //

        ms.stop();

        assertFalse(ms.isStarted());
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
     * @param addresses needed to simulate "different" metric source. If not specified, the default metric source
     *                is returned. Only the first argument is used, if more than one is sent, we will throw
     *                IllegalArgumentException.
     */
    protected abstract MetricSource getMetricSourceToTest(String... addresses) throws Exception;

    /**
     * Gives the sub-classes a chance to provide more specialized mocks.
     */
    protected MetricDefinition getCorrespondingMockMetricDefinition(Address metricSourceAddress) throws Exception {

        PropertyFactory f = new PropertyFactory();

        return new MockMetricDefinition(f, metricSourceAddress);
    }


    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
