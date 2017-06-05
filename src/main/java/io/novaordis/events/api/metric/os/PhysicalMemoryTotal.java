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

import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.utilities.os.LinuxOS;
import io.novaordis.utilities.os.MacOS;
import io.novaordis.utilities.os.OS;

/**
 * See https://kb.novaordis.com/index.php/Proc-meminfo#MemTotal
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryTotal extends MetricDefinitionBase implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String MACOS_COMMAND = "top -l 1 -n 0";
    public static final String LINUX_COMMAND = "top -b -n 1 -p 0";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PhysicalMemoryTotal(MetricSource s) {

        super(s);
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
        return "Total Physical Memory";
    }

    @Override
    public String getDescription() {

        return
                "Total amount of usable physical memory, which is the amount of physical memory installed on the " +
                        "system minus a number of reserved bits and the kernel binary code.";
    }

    // OSMetricDefinition implementation -------------------------------------------------------------------------------

    @Override
    public String getOSCommand(OS os) {

        if (os instanceof MacOS) {

            return MACOS_COMMAND;
        }
        else if (os instanceof LinuxOS) {

            return LINUX_COMMAND;
        }
        else {

            throw new RuntimeException(os + " NOT YET SUPPORTED");
        }
    }

    @Override
    public Property commandOutputToProperty(OS os, String commandOutput) {

        if (os instanceof MacOS) {

            // MACOS_COMMAND output
            long value = 1L;
            return new LongProperty(getDefinition(), value);
        }
        else if (os instanceof LinuxOS) {

            // LINUX_COMMAND output
            throw new RuntimeException("NOT YET IMPLEMENTED");
        }
        else {
            throw new RuntimeException(os + " NOT YET SUPPORTED");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
