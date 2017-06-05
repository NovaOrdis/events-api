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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.utilities.os.OS;

import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/1/17
 */
public class LocalOS implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException {

        throw new RuntimeException("collectMetrics() NOT YET IMPLEMENTED");
    }

    @Override
    public String getAddress() {

        return null;
    }

    @Override
    public boolean hasAddress(String address) {

        return false;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public OS getOS() {

        throw new RuntimeException("NYE");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
