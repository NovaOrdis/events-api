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

import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricSource;

/**
 * See https://kb.novaordis.com/index.php/Vmstat#si_2
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuSoftwareInterruptTime extends OSMetricDefinitionBase {


    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CpuSoftwareInterruptTime(OSSource s) {

        super(s);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public Percentage getBaseUnit() {

        return Percentage.getInstance();
    }

    @Override
    public Class getType() {
        return Float.class;
    }

    @Override
    public String getSimpleLabel() {
        return "CPU Software Interrupt Time";
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws Exception {
        throw new RuntimeException("parseMacCommandOutput() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws Exception {
        throw new RuntimeException("parseLinuxCommandOutput() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws Exception {
        throw new RuntimeException("parseWindowsCommandOutput() NOT YET IMPLEMENTED");
    }

    @Override
    public String getDescription() {
        return "Percentage of total CPU time spent time spent spent servicing software interrupts.";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
