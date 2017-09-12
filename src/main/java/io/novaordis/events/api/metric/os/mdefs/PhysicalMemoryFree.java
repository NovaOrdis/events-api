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

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MemoryArithmetic;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.parsing.PreParsedContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The amount of physical RAM, left unused by the system.
 *
 * https://kb.novaordis.com/index.php/Events_OS_Metrics#PhysicalMemoryFree
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class PhysicalMemoryFree extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public PhysicalMemoryFree(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        this.TYPE = Long.class;

        this.LABEL = "Free Physical Memory";

        this.BASE_UNIT = MemoryMeasureUnit.BYTE;

        this.DESCRIPTION = "The amount of physical memory left unused by the system.";

        this.LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // KiB Mem :   999936 total,   735636 free,   117680 used,   146620 buff/cache
        //
        this.LINUX_PATTERN = Pattern.compile(
                "([KMGiB]+) *Mem *: *([0-9]+) total, *([0-9]+) free, *([0-9]+) used, *([0-9]+) buff/cache");

        this.MAC_COMMAND = "/usr/bin/top -l 1 -n 0";

        //
        // PhysMem: 12G used (2149M wired), 4305M unused.
        //
        this.MAC_PATTERN = Pattern.compile(
                "PhysMem: ([0-9]+)([MG]+) used .* ([0-9]+)([MG]+) unused");

        this.WINDOWS_COMMAND =  "typeperf -sc 1 \"\\Memory\\*\"";

        this.WINDOWS_PATTERN = Pattern.compile("TBD");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected InternalMetricReadingContainer parseLinuxSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new ParsingException("parseLinuxSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected InternalMetricReadingContainer parseMacSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new ParsingException("parseMacSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected InternalMetricReadingContainer parseWindowsSourceFileContent
            (byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new ParsingException("parseWindowsSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + LINUX_PATTERN.pattern());
        }

        String memoryUnit = m.group(1);
        String freeMemory = m.group(3);

        try {

            //noinspection UnnecessaryLocalVariable
            Long value = MemoryArithmetic.parse(freeMemory, memoryUnit, (MemoryMeasureUnit)BASE_UNIT);
            return value;
        }
        catch(Exception e) {

            throw new ParsingException(e);
        }
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = MAC_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern \"" + MAC_PATTERN.pattern() + "\"");
        }

        String unusedMemory = m.group(3);
        String unusedMemoryUnit = m.group(4);

        try {
            //noinspection UnnecessaryLocalVariable
            Long value = MemoryArithmetic.parse(unusedMemory, unusedMemoryUnit, (MemoryMeasureUnit)BASE_UNIT);
            return value;
        }
        catch(Exception e) {

            throw new ParsingException(e);
        }
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = WINDOWS_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + WINDOWS_PATTERN.pattern());
        }

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
