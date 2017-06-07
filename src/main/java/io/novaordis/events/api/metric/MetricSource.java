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
 * 5. etc.
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
     * @return the address of this metric source, as string, or null if it does not apply to this metric source (for
     * example, LocalOS). If a non-null string is returned hasAddress() invoked on this string will always return
     * true.
     */
    String getAddress();

    /**
     * @return true if this metric source has the specified address, false otherwise.
     */
    boolean hasAddress(String address);

    /**
     * Collect the metrics for the given definitions, in one invocation.
     *
     * If a value corresponding to a specific MetricDefinition from the list cannot be successfully collected,
     * a Property with the correct id, type and base unit must, but with a null value, be returned returned.
     * Implementations should also log as WARN more details on why the collection failed.
     *
     * @exception MetricException if metric definitions do not list this source among their sources. This indicates
     *      a programming error, not a runtime collection failure.
     */
    List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException;

}
