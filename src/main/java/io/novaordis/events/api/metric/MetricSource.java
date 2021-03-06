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
import io.novaordis.utilities.address.Address;

import java.util.List;

/**
 * A source of metrics. Implies an underlying mechanism that can be used to obtain a set of values for a set of
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
 * A metric source instance must be started before metrics can be collected from it. The start operation usually
 * implies expensive remote connection creation, initial state verification, etc. so metric source implementations
 * should be designed to be started once and then stay in that state indefinitely. However, the underlying connection
 * may break for various reasons during the metric source life, so it may become necessary to re-start a metric source.
 *
 * Implementations are NOT expected to maintain their own internal threads. The recommended way to manage sources
 * and collect metrics is to do it with external threads/executors.
 *
 * The implementations must correctly implement equals() and hashCode(), as metric sources will be used as map keys.
 *
 * See https://kb.novaordis.com/index.php/Events-api_Concepts#Metric_Source
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/4/16
 */
public interface MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the address of this metric source. Must never return null. This instance's hasAddress() invoked with the
     * value returned by getAddress() must return true.
     * true.
     */
    Address getAddress();

    /**
     * @return true if this metric source has the specified address, false otherwise.
     */
    boolean hasAddress(Address address);

    /**
     * Collect all metrics whose definitions are given as argument. If a value corresponding to a specific metric
     * definition from the list cannot be successfully collected, a Property instance with the correct id, type and base
     * unit is still returned, but its value is null. Implementations may log as WARN more details on why the collection
     * failed. Implementations may choose - and they are encouraged - to attempt to start the source in-line, if the
     * source is not already started. They, however, may chose to signal the fact that the source is not started and
     * request an external start. The metric source will throw an exception if if finds at least a metric definition
     * that has a null or different source. This indicates a programming error, not a runtime collection failure. A
     * Property instance returned for a specific metric definition has its name equals with the metric definition ID.
     *
     * See https://kb.novaordis.com/index.php/Events-api_Concepts#Relationship_between_a_Metric_Definition_and_a_Metric_Source
     *
     * @param metricDefinitions a non-null, possibly empty, list of metrics to collect.
     *
     * @exception MetricSourceException if a metric definition present in the list is not bound to this source.
     *      This indicates a programming error, not a runtime collection failure.
     *
     * @exception IllegalArgumentException if the list is null (may be empty though).
     */
    List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricException;

    // Life cycle ------------------------------------------------------------------------------------------------------

    /**
     * A metric source instance must be started before metrics can be collected from it. The start operation usually
     * implies expensive remote connection creation, initial state verification, etc. so metric source implementations
     * should be designed to be started once and then stay in that state indefinitely. However, the underlying
     * connection may break for various reasons during the metric source life, so it may become necessary to re-start a
     * metric source. Implementations may chose to attempt to start a source that is not started during the
     * collectMetrics() call.
     *
     * @see MetricSource#collectMetrics(List)
     */
    void start() throws MetricSourceException;

    boolean isStarted();

    void stop();

}
