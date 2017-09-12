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
import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.measure.PercentageArithmetic;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.linux.CPUStats;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.parsing.PreParsedContent;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The percentage of total CPU time spent executing system calls on behalf of processes.
 *
 * https://kb.novaordis.com/index.php/Events_OS_Metrics#CpuKernelTime
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuKernelTime extends OSMetricDefinitionBase {


    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CpuKernelTime(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        this.TYPE = Float.class;

        this.LABEL = "CPU Kernel Time";

        this.BASE_UNIT = Percentage.getInstance();

        this.DESCRIPTION = "Percentage of total CPU time spent running kernel code (system time).";

        this.LINUX_SOURCE_FILE = new File("/proc/stat");

        this.LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // %Cpu(s):  2.8 us,  8.1 sy,  0.0 ni, 88.7 id,  0.4 wa,  0.0 hi,  0.1 si,  0.0 st
        //
        this.LINUX_PATTERN = Pattern.compile(
                "%Cpu.*: +([0-9]+\\.[0-9]) us, +([0-9]+\\.[0-9]) sy, +([0-9]+\\.[0-9]) ni, +([0-9]+\\.[0-9]) id, +([0-9]+\\.[0-9]) wa, +([0-9]+\\.[0-9]) hi, +([0-9]+\\.[0-9]) si, +([0-9]+\\.[0-9]) st");

        this.MAC_COMMAND = "/usr/bin/top -l 1 -n 0";

        //
        // CPU usage: 3.94% user, 11.84% sys, 84.21% idle
        //
        this.MAC_PATTERN = Pattern.compile(
                "CPU usage: +([0-9]+\\.[0-9]+)% user, +([0-9]+\\.[0-9]+)% sys, +([0-9]+\\.[0-9]+)% idle");

    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected InternalMetricReadingContainer parseLinuxSourceFileContent(
            byte[] content, PreParsedContent previousReading) throws ParsingException {

        PreParsedContent[] preParsedContent = distributePreParsedContent(content, previousReading);
        float value = ((CPUStats) preParsedContent[1]).getSystemTimePercentage((CPUStats) preParsedContent[2]);
        return new InternalMetricReadingContainer(value, preParsedContent[0]);
    }

    @Override
    protected InternalMetricReadingContainer parseMacSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException {

        throw new ParsingException("parseMacSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected InternalMetricReadingContainer parseWindowsSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException {

        throw new ParsingException("parseWindowsSourceFileContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + LINUX_PATTERN.pattern());
        }

        String sy = m.group(2);

        try {

            //noinspection UnnecessaryLocalVariable
            Float f = PercentageArithmetic.parse(sy);
            return f;
        }
        catch(Exception e) {

            throw new ParsingException(e);
        }
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = MAC_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + MAC_PATTERN.pattern());
        }

        String sys = m.group(2);

        try {

            //noinspection UnnecessaryLocalVariable
            Float f = PercentageArithmetic.parse(sys);
            return f;
        }
        catch(Exception e) {

            throw new ParsingException(e);
        }
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws ParsingException {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
