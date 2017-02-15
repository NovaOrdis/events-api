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

import java.util.List;

/**
 * The source for metrics. Can represent a native O/S command, a file, etc.
 *
 * The implementations must correctly implement equals() and hashCode().
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/4/16
 */
public interface MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Collect all the possible metrics that can be collected in one invocation. Use this to optimize expensive
     * invocations to sources.
     *
     * @return the complete list of properties. If no properties are collected, returns an empty list, but never null.
     */
    List<Property> collectAllMetrics(OS os) throws MetricCollectionException;

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
