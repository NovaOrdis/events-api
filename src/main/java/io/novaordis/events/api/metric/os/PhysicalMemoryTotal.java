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
import io.novaordis.events.api.measure.MemoryArithmetic;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.utilities.os.LinuxOS;
import io.novaordis.utilities.os.MacOS;
import io.novaordis.utilities.os.OS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * See https://kb.novaordis.com/index.php/Proc-meminfo#MemTotal
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryTotal extends MetricDefinitionBase implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String MACOS_COMMAND = "/usr/bin/top -l 1 -n 0";

    public static final Pattern MACOS_PATTERN = Pattern.compile(
            "PhysMem: ([0-9]+)([MG]+) used .* ([0-9]+)([MG]+) unused");

    public static final String LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

    public static final String DEFINITION = PhysicalMemoryTotal.class.getSimpleName();

    // Static ----------------------------------------------------------------------------------------------------------

    public static Property mac(String commandOutput) {

        LongProperty p = new LongProperty(DEFINITION);

        Long value = null;

        try {

            Matcher m = MACOS_PATTERN.matcher(commandOutput);

            if (m.find()) {

                String usedMemory = m.group(1);
                String usedMemoryUnit = m.group(2);
                String unusedMemory = m.group(3);
                String unusedMemoryUnit = m.group(4);

                value = MemoryArithmetic.add(
                        usedMemory, usedMemoryUnit,
                        unusedMemory, unusedMemoryUnit,
                        MemoryMeasureUnit.BYTE);
            }
        }
        catch(Exception e) {

            log.warn("failed to compute total memory from " + MACOS_COMMAND + " output: \n" + commandOutput, e);
        }

        p.setMeasureUnit(MemoryMeasureUnit.BYTE);
        p.setValue(value);

        return p;
    }

    public static Property linux(String commandOutput) {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    public static Property windows(String commandOutput) {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PhysicalMemoryTotal(MetricSource s) {

        super(s);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getDefinition() {

        return DEFINITION;
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
