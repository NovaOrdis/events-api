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
import io.novaordis.events.api.measure.PercentageArithmetic;
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.address.OSAddress;
import io.novaordis.utilities.parsing.PreParsedContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://kb.novaordis.com/index.php/Events_OS_Metrics#LoadAverageLastFiveMinutes
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class LoadAverageLastFiveMinutes extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public LoadAverageLastFiveMinutes(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        this.TYPE = Float.class;

        this.LABEL = "Last Five Minutes Load Average";

        this.BASE_UNIT = null;

        this.DESCRIPTION = "CPU and IO utilization during the last five minutes.";

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

    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected InternalMetricReadingContainer parseSourceFileContent
            (OSType osType, byte[] content, PreParsedContent previousReading) throws ParsingException {

        throw new IllegalStateException(this + " cannot be extracted from a file on " + osType);
    }

    @Override
    protected Object parseLinuxCommandOutput(String commandOutput) throws ParsingException {

        Matcher m = LINUX_PATTERN.matcher(commandOutput);

        if (!m.find()) {

            throw new ParsingException("failed to match pattern " + LINUX_PATTERN.pattern());
        }

        String s = m.group(2);

        try {

            //noinspection UnnecessaryLocalVariable
            Float f = PercentageArithmetic.parse(s);
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

        String s = m.group(2);

        try {

            //noinspection UnnecessaryLocalVariable
            Float f = PercentageArithmetic.parse(s);
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
