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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricCollectionException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.source.MetricSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a JMX bus of a local or remote JVM.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JmxBus implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JmxBus.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // MetricSource implementation -------------------------------------------------------------------------------------

    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricCollectionException {

        throw new RuntimeException("not yet implemented");

    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {

        throw new RuntimeException("not yet implemented");
    }

    @Override
    public String toString() {

        throw new RuntimeException("not yet implemented");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
