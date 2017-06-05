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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.utilities.os.OS;

/**
 * See https://kb.novaordis.com/index.php/Vmstat#hi
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuHardwareInterruptTime extends MetricDefinitionBase implements OSMetricDefinition {


    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CpuHardwareInterruptTime(MetricSource s) {

        super(s);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getDefinition() {

        return getClass().getSimpleName();
    }

    @Override
    public Percentage getMeasureUnit() {

        return Percentage.getInstance();
    }

    @Override
    public Class getType() {
        return Float.class;
    }

    @Override
    public String getSimpleLabel() {
        return "CPU Hardware Interrupt Time";
    }

    @Override
    public String getDescription() {
        return "Percentage of total CPU time spent time spent spent servicing hardware interrupts.";
    }

    // OSMetricDefinition implementation -------------------------------------------------------------------------------

    @Override
    public String getOSCommand(OS os) {
        throw new RuntimeException("getOSCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public Property commandOutputToProperty(OS os, String commandOutput) {
        throw new RuntimeException("commandOutputToProperty() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
