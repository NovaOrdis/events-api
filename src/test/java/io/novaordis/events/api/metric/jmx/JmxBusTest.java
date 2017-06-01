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

package io.novaordis.events.api.metric.jmx;

import io.novaordis.events.api.metric.source.MetricSourceTest;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBusTest extends MetricSourceTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Override
    public void equalsTest() throws Exception {
        throw new RuntimeException("equalsTest() NOT YET IMPLEMENTED");
    }

    @Override
    public void hashCodeTest() throws Exception {
        throw new RuntimeException("hashCodeTest() NOT YET IMPLEMENTED");
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // collectMetrics() ------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected JmxBus getMetricSourceToTest() throws Exception {

        return new JmxBus();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
