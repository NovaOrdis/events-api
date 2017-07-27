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
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.utilities.address.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://kb.novaordis.com/index.php/Events-api_Concepts#Metric_Definitions
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public interface MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    Logger log = LoggerFactory.getLogger(MetricDefinition.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The metric ID. It uniquely identifies a metric to a MetricSource so the MetricSource can provide a value
     * (reading) for that metric. The ID does NOT include metric source information. The same ID can be provided to
     * different MetricSources, and each MetricSource will return a different value of the metric.
     *
     * Examples:
     *
     * "PhysicalMemoryTotal" - uniquely identifies the total physical memory metric in the context of a local or remote
     *      operating system instance.
     *
     * "java.lang:type=Threading.ThreadCount" - uniquely identifies the JVM thread count metric in the context of a
     *      JVM's platform MBean server.
     *
     * "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:message-count" - uniquely identifies the DLQ queue
     *      depth metric for a specific broker node, managed by a JBoss management controller.
     *
     * See https://kb.novaordis.com/index.php/Events-api_Concepts#Metric_Definition_ID.
     */
    String getId();

    /**
     * @return the address of this metric definition MetricSource instance. Never returns null.
     */
    Address getMetricSourceAddress();

    /**
     * The types for values corresponding to this metric definition. Typically Integer, Long, Double.
     */
    Class getType();

    /**
     * The base measure unit for this metric definition. For example, the base measure unit for memory metrics is
     * MemoryMeasureUnit.BYTE. Metric values corresponding to this metric definition may be expressed in multiples of
     * the base unit. For example, in case of a memory metric definition, values may be expressed in
     * MemoryMeasureUnit.BYTE, MemoryMeasureUnit.KILOBYTE, etc. May return null if the metric is non-dimensional (for
     * example load average).
     *
     * @see MeasureUnit
     * @see MetricDefinition#getBaseUnit()
     */
    MeasureUnit getBaseUnit();

    /**
     * A human readable string, possibly space separated, that is used to represent the metric in user-facing
     * representations, such as a CSV file headers. The label includes, by default, the source address (unless the
     * source address is the local OS) and the parantheses-enclosed measure unit, if the metric has a measure unit.
     * For example, PhysicalMemoryTotal's label is "Total Physical Memory (bytes)".
     *
     * @param attributes different attributes to include within the label, such as measure unit, etc. If no label
     *                   attributes are specified, a simple label is generated.
     *
     * @see MeasureUnit#getLabel()
     */
    String getLabel(LabelAttribute ... attributes);

    /**
     * The human readable text that explains what this metric represents.
     */
    String getDescription();

    /**
     * Builds a property instance corresponding to the given value of the metric - it has the correct name, type, etc.
     * If the value is null, build an empty property, but also with the correct name, type, etc.
     *
     * @throws MetricException
     *
     * @see MetricDefinition#buildProperty()
     */
    Property buildProperty(Object value) throws MetricException;

    /**
     * Builds an empty (null valued) property instance corresponding this metric definition - it has the correct name,
     * type, etc. This is a convenience method, it produces the same result as buildProperty(null).
    *
     * @throws MetricException
     *
     * @see MetricDefinition#buildProperty(Object)
     */
    Property buildProperty() throws MetricException;

}
