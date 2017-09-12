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
 * The total amount of free swap.
 *
 * https://kb.novaordis.com/index.php/Events_OS_Metrics#SwapFree
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class SwapFree extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public SwapFree(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        this.TYPE = Long.class;

        this.LABEL = "Free Swap";

        this.BASE_UNIT = MemoryMeasureUnit.BYTE;

        this.DESCRIPTION = "The total amount of free swap.";

        this.LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // KiB Swap:        0 total,      321 free,        0 used.   715840 avail Mem
        //
        this.LINUX_PATTERN = Pattern.compile(
                "([KMGiB]+) *Swap *: *([0-9]+) total, *([0-9]+) free, *([0-9]+) used\\.");

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
        String s = m.group(3);

        try {
            //noinspection UnnecessaryLocalVariable
            Long value = MemoryArithmetic.parse(s, memoryUnit, (MemoryMeasureUnit)BASE_UNIT);
            return value;
        }
        catch(Exception e) {

            throw new ParsingException(e);
        }
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws ParsingException {

        throw new IllegalStateException(this + " not available on Mac");
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws ParsingException {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
