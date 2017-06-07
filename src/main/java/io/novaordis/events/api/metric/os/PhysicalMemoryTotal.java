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

import io.novaordis.events.api.measure.MemoryArithmetic;
import io.novaordis.events.api.measure.MemoryMeasureUnit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * See https://kb.novaordis.com/index.php/Proc-meminfo#MemTotal
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryTotal extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    static {

        TYPE = Long.class;

        BASE_UNIT = MemoryMeasureUnit.BYTE;

        LABEL = "Total Physical Memory";

        DESCRIPTION =
                "Total amount of usable physical memory, which is the amount of physical memory installed on the " +
                        "system minus a number of reserved bits and the kernel binary code.";

        LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache
        //
        LINUX_PATTERN = Pattern.compile(
                "([KMGiB]+) *Mem *: *([0-9]+) total, *([0-9]+) free, *([0-9]+) used, *([0-9]+) buff/cache");

        MAC_COMMAND = "/usr/bin/top -l 1 -n 0";

        //
        // PhysMem: 12G used (2149M wired), 4305M unused.
        //
        MAC_PATTERN = Pattern.compile(
                "PhysMem: ([0-9]+)([MG]+) used .* ([0-9]+)([MG]+) unused");


        WINDOWS_COMMAND = "TBD";
        WINDOWS_PATTERN = null;
    }

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PhysicalMemoryTotal(OSSource s) {

        super(s);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws Exception {

        Matcher m = MAC_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            return null;
        }

        String usedMemory = m.group(1);
        String usedMemoryUnit = m.group(2);
        String unusedMemory = m.group(3);
        String unusedMemoryUnit = m.group(4);

        //noinspection UnnecessaryLocalVariable
        Long value = MemoryArithmetic.add(
                usedMemory, usedMemoryUnit,
                unusedMemory, unusedMemoryUnit,
                (MemoryMeasureUnit)BASE_UNIT);

        return value;
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws Exception {

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            return null;
        }

        String memoryUnit = m.group(1);
        String totalMemory = m.group(2);

        //noinspection UnnecessaryLocalVariable
        Long value = MemoryArithmetic.parse(totalMemory, memoryUnit, (MemoryMeasureUnit)BASE_UNIT);
        return value;
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws Exception {

        throw new RuntimeException("NOT YET IMPLEMENTED: windows parsing for " + commandOutput);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
