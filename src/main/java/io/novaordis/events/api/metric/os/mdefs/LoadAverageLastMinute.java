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

import io.novaordis.events.api.measure.PercentageArithmetic;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.events.api.metric.os.OSSourceBase;
import io.novaordis.events.api.parser.ParsingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class LoadAverageLastMinute extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public LoadAverageLastMinute(OSSourceBase s) {

        super(s);

        this.TYPE = Float.class;

        this.LABEL = "Last Minute Load Average";

        this.BASE_UNIT = null;

        this.DESCRIPTION = "CPU and IO utilization during the last minute.";

        this.LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // "... load average: 0.15, 0.04, 0.02\n" +
        //
        this.LINUX_PATTERN = Pattern.compile(
                "load average: +([0-9]+\\.[0-9]+), +([0-9]+\\.[0-9]+), +([0-9]+\\.[0-9]+)");

        this.MAC_COMMAND = "/usr/bin/top -l 1 -n 0";

        //
        // "Load Avg: 2.29, 2.02, 1.90"
        //
        this.MAC_PATTERN = Pattern.compile(
                "Load Avg: +([0-9]+\\.[0-9]+), +([0-9]+\\.[0-9]+), +([0-9]+\\.[0-9]+)");

        this.WINDOWS_COMMAND =  null;
        this.WINDOWS_PATTERN = null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws Exception {

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + LINUX_PATTERN.pattern());
        }

        String s = m.group(1);

        //noinspection UnnecessaryLocalVariable
        Float f = PercentageArithmetic.parse(s);
        return f;
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws Exception {

        Matcher m = MAC_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + MAC_PATTERN.pattern());
        }

        String s = m.group(1);

        //noinspection UnnecessaryLocalVariable
        Float f = PercentageArithmetic.parse(s);
        return f;
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws Exception {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
