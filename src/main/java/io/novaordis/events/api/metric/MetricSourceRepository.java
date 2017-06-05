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

package io.novaordis.events.api.metric;

import java.util.Set;

/**
 * The runtime repository of metric sources. The intention behind depositing metric sources in a central location is
 * to maintain state per metric source and thus optimize collection.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/4/17
 */
public interface MetricSourceRepository {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The set of metric sources ofa specific type. May be empty but never null.
     */
    <T extends MetricSource> Set<T> getSources(Class<T> t);

    /**
     * @param t the type of the metric source.
     *
     * @param address the address of the metric source. If not specified, it is implied that there could be just on
     *                metric source of that type (example: the LocalOS instance). In case of JBossControllers, the
     *                lookup address is jbossController.getControllerAddress().getLiteral().
     *
     * @return the metric source repository of the specified type with the specified address, or null if no source
     * exists.
     *
     * @exception IllegalArgumentException if more than one address is provided.
     * @exception IllegalStateException if more than one metric sources for the given type exist, and no address is
     *  provided.
     */
    <T extends MetricSource> T getSource(Class<T> t, String... address);

    <T extends MetricSource> void add(T source);

}
