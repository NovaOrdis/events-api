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
import io.novaordis.events.api.metric.os.OSMetricDefinitionBase;
import io.novaordis.utilities.ParsingException;
import io.novaordis.utilities.address.OSAddress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * See https://kb.novaordis.com/index.php/Vmstat#hi
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuHardwareInterruptTime extends OSMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CpuHardwareInterruptTime(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        this.TYPE = Float.class;

        this.LABEL = "CPU Hardware Interrupt Time";

        this.BASE_UNIT = Percentage.getInstance();

        this.DESCRIPTION = "Percentage of total CPU time spent time spent spent servicing hardware interrupts.";

        this.LINUX_COMMAND = "/usr/bin/top -b -n 1 -p 0";

        //
        // %Cpu(s):  2.8 us,  8.1 sy,  0.0 ni, 88.7 id,  0.4 wa,  0.0 hi,  0.1 si,  0.0 st
        //
        this.LINUX_PATTERN = Pattern.compile(
                "%Cpu.*: +([0-9]+\\.[0-9]) us, +([0-9]+\\.[0-9]) sy, +([0-9]+\\.[0-9]) ni, +([0-9]+\\.[0-9]) id, +([0-9]+\\.[0-9]) wa, +([0-9]+\\.[0-9]) hi, +([0-9]+\\.[0-9]) si, +([0-9]+\\.[0-9]) st");

        this.MAC_COMMAND = null;
        this.MAC_PATTERN = null;

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

        String hi = m.group(6);

        //noinspection UnnecessaryLocalVariable
        Float f = PercentageArithmetic.parse(hi);
        return f;
    }

    @Override
    protected Object parseMacCommandOutput(String commandOutput) throws Exception {

        throw new IllegalStateException(this + " not available on Mac");
    }

    @Override
    protected Object parseWindowsCommandOutput(String commandOutput) throws Exception {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
