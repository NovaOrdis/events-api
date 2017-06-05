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

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.jboss.JBossCliMetricDefinition;
import io.novaordis.utilities.UserErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public interface MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    Logger log = LoggerFactory.getLogger(MetricDefinition.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null if the metric is non-dimensional (for example load average).
     *
     * @see MeasureUnit
     */
    MeasureUnit getMeasureUnit();

    /**
     * The human readable text that explains what this metric represents.
     */
    String getDescription();

    /**
     * The types for values corresponding to this metric definition. Typical: Integer, Long, Double.
     */
    Class getType();

    /**
     * The metric definition, as string.
     *
     * Information return by the method should be sufficient for a metric source to look up the corresponding metric
     * and provide a value for it.
     *
     * The definition does not include metric source information.
     *
     * Examples:
     *
     * "PhysicalMemoryTotal" - makes sense in the context of a local or remote OS.
     * "java.lang:type=Threading.ThreadCount" - makes sense in the context of a JMX bus.
     * "/subsystem=messaging/hornetq-server=default/jms-queue=DLQ:message-count" - makes sense in the context of a
     * JBoss management controller.
     */
    String getDefinition();

    /**
     * A human readable string, possibly space separated, that is used to represent the metric in user-facing
     * representations, such as a CSV file headers. The label includes, by default, the parantheses-enclosed measure
     * unit, if the metric has a measure unit. For example, PhysicalMemoryTotal's label is
     * "Total Physical Memory (bytes)"
     *
     * @param attributes different attributes to include within the label, such as measure unit, etc. If no label
     *                   attributes are specified, a simple label is generated.
     *
     * @see MeasureUnit#getLabel()
     */
    String getLabel(LabelAttribute ... attributes);

    /**
     * @return the source for this metric. Never returns null.
     */
    MetricSource getSource();

}
