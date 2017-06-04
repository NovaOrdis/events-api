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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.source.Top;
import io.novaordis.utilities.os.OS;

/**
 * See https://kb.novaordis.com/index.php/Proc-meminfo#SwapTotal
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class SwapTotal extends MetricDefinitionBase implements MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public SwapTotal() {

        addSource(OS.Linux, new Top("-b -n 1 -p 0"));
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getDefinition() {

        return getClass().getSimpleName();
    }

    /**
     * All memory metrics are by default expressed in bytes.
     */
    @Override
    public MeasureUnit getMeasureUnit() {
        return MemoryMeasureUnit.BYTE;
    }

    @Override
    public Class getType() {
        return Long.class;
    }

    @Override
    public String getSimpleLabel() {
        return "Total Swap";
    }

    @Override
    public String getDescription() {

        return "The total amount of swap available.";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
