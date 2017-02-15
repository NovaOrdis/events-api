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

package io.novaordis.events.api.metric.source;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricCollectionException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.utilities.os.OS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/5/16
 */
public class MockMetricSource implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<OS, List<Property>> results;

    private boolean breakOnCollect;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockMetricSource() {

        results = new HashMap<>();
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectAllMetrics(OS os) throws MetricCollectionException {

        if (breakOnCollect) {
            throw new MetricCollectionException("SYNTHETIC");
        }

        List<Property> props = results.get(os);

        if (props == null) {
            return Collections.emptyList();
        }

        return props;
    }

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricCollectionException {
        throw new RuntimeException("collectMetrics() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void mockMetricGeneration(OS os, Property p) {

        List<Property> ps = results.get(os);

        if (ps == null) {

            ps = new ArrayList<>();
            results.put(os, ps);
        }

        ps.add(p);
    }

    public void breakOnCollectMetrics() {

        breakOnCollect = true;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
