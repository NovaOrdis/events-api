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
import io.novaordis.utilities.os.WindowsOS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * See https://kb.novaordis.com/index.php/Proc-meminfo#Physical_Memory_Used_by_Processes
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryUsed extends MetricDefinitionBase implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String DEFINITION = PhysicalMemoryUsed.class.getSimpleName();

    public static final String LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

    // matches
    // KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache
    public static final Pattern LINUX_PATTERN = Pattern.compile(
            "([KMGiB]+) *Mem *: *([0-9]+) total, *([0-9]+) free, *([0-9]+) used, *([0-9]+) buff/cache");

    public static final String MACOS_COMMAND = "/usr/bin/top -l 1 -n 0";

    // matches:
    // PhysMem: 12G used (2149M wired), 4305M unused.
    public static final Pattern MACOS_PATTERN = Pattern.compile(
            "PhysMem: ([0-9]+)([MG]+) used .* ([0-9]+)([MG]+) unused");

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * The parsing logic that extracts the value from the output of the default reporting command on Mac.
     */
    public static Property mac(String commandOutput) {

        LongProperty p = new LongProperty(DEFINITION);
        p.setMeasureUnit(MemoryMeasureUnit.BYTE);

        Matcher m = MACOS_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            log.warn("failed to extract " + DEFINITION + " from \"" + MACOS_COMMAND + "\" output: \n\n" +
                    commandOutput + "\n");
            return p;
        }

        try {

            String usedMemory = m.group(1);
            String usedMemoryUnit = m.group(2);

            Long value = MemoryArithmetic.parse(usedMemory, usedMemoryUnit, (MemoryMeasureUnit) p.getMeasureUnit());
            p.setValue(value);
        }
        catch(Exception e) {

            log.warn("failed to compute " + DEFINITION + " from \"" + MACOS_COMMAND + "\" output: \n\n" +
                    commandOutput + "\n", e);
        }

        return p;
    }

    public static Property linux(String commandOutput) {

        LongProperty p = new LongProperty(DEFINITION);
        p.setMeasureUnit(MemoryMeasureUnit.BYTE);

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            log.warn("failed to extract " + DEFINITION + " from \"" + LINUX_PATTERN + "\" output: \n\n" +
                    commandOutput + "\n");
            return p;
        }

        try {

            String usedMemoryUnit = m.group(1);
            String usedMemory = m.group(4);

            Long value = MemoryArithmetic.parse(usedMemory, usedMemoryUnit, (MemoryMeasureUnit)p.getMeasureUnit());
            p.setValue(value);
        }
        catch(Exception e) {

            log.warn("failed to compute " + DEFINITION + " from \"" + LINUX_PATTERN + "\" output: \n\n" +
                    commandOutput + "\n", e);
        }

        return p;
    }

    public static Property windows(String commandOutput) {

        throw new RuntimeException("NOT YET IMPLEMENTED: windows parsing for " + commandOutput);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PhysicalMemoryUsed(MetricSource s) {

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
        return "Used Physical Memory";
    }

    @Override
    public String getDescription() {

        return "The amount of physical memory used by the processes running on the system.";
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
            return mac(commandOutput);
        }
        else if (os instanceof LinuxOS) {

            // LINUX_COMMAND output
            return linux(commandOutput);
        }
        else if (os instanceof WindowsOS) {

            return windows(commandOutput);
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
