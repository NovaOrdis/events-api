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

package io.novaordis.events.api.metric.os.mdefs;

import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.os.OSSource;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class LoadAverageLastTenMinutes extends MetricDefinitionBase  {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public LoadAverageLastTenMinutes(OSSource s) {

        super(s);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public MeasureUnit getBaseUnit() {
        return null;
    }

    @Override
    public String getId() {
        throw new RuntimeException("getId() NOT YET IMPLEMENTED");
    }

    @Override
    public Class getType() {
        return Float.class;
    }

    @Override
    public String getSimpleLabel() {
        return "Last Ten Minutes Load Average";
    }

    @Override
    public String getDescription() {

        return "CPU and IO utilization during the last ten minutes.";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
