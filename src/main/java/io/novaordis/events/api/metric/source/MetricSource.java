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

import java.util.List;

/**
 * A source of metrics. Implies an underlying mechanism that can be exercised to obtain a set of values for a set of
 * metric definitions, ideally in a single operation, for performance reasons. However, the implementations are free
 * to define what "efficient reading" means. Example of metric sources:
 *
 * 1. Local O/S instance can be queried for metrics such as free memory, CPU usage or disk space.
 *
 * 2. Remote O/S instance can be queried for metrics such as free memory, CPU usage or disk space over a SSH
 *    connections (assuming credentials are available and the system is reachable).
 *˙
 * 3. The JMX bus of a local or remote Java virtual machine.
 *
 * 4. The management controller (standalone or domain) of a WildFly instance.
 *
 * Can represent a native O/S command, a file, etc.
 *
 * The implementations must correctly implement equals() and hashCode(), as metric sources will be used as map keys.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/4/16
 */
public interface MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Collect the metrics for the given definitions, in one invocation.
     *
     * @return the list of properties. If no properties are collected, returns an empty list, but never null. However,
     * if a property for a specific metric definition cannot be collected, or it does not apply to this specific source,
     * the list will contain a null on the respective position.
     *
     * @exception MetricCollectionException if metric definitions do not list this source among their sources.
     */
    List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricCollectionException;

}
