/*
 * Copyright (c) 2017 Nova Ordis LLC
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
import io.novaordis.events.api.metric.os.InternalMetricReadingContainer;
import io.novaordis.linux.CPUStats;
import io.novaordis.utilities.address.LocalOSAddress;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.PreParsedContent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class CpuKernelTimeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // parseSourceFileContent() ----------------------------------------------------------------------------------------

    @Test
    public void parseSourceFileContent() throws Exception {

        CpuKernelTime m = new CpuKernelTime(new PropertyFactory(), new LocalOSAddress());

        assertNotNull(m.getSourceFile(OSType.LINUX));

        // simulate the first reading, valid content

        String firstFileContentReading =
                "cpu  1 2 3 4 5 6 7 8 9 10\n" +
                        "cpu0 1 2 3 4 5 6 7 8 9 10\n" +
                        "intr 5784556411 57 10 0 0 1010\n" +
                        "ctxt 9216607429\n" +
                        "btime 1499371654\n" +
                        "processes 8116472\n" +
                        "procs_running 1\n" +
                        "procs_blocked 0\n" +
                        "softirq 3995572696 7 1762568953 35455 456569428 0 0 12771 767863139 0 1008522943\n";


        PreParsedContent previousReading = null;

        //noinspection ConstantConditions
        InternalMetricReadingContainer r =
                m.parseSourceFileContent(OSType.LINUX, firstFileContentReading.getBytes(), previousReading);

        Float f = (Float)r.getPropertyValue();

        //
        // current reading becomes the previous reading
        //

        previousReading = r.getPreParsedContent();
        assertNotNull(previousReading);
        assertEquals(55L, ((CPUStats)previousReading).getTotalTime());

        //
        // the statistics are calculated since the beginning
        //

        assertEquals(3f / 55, f.floatValue(), 0.00001);

        // simulate the second reading, valid content

        String secondFileContentReading =
                "cpu  11 22 33 44 55 66 77 88 99 110\n" +
                        "cpu0 11 22 33 44 55 66 77 88 99 110\n" +
                        "intr 5784556411 57 10 0 0 1010\n" +
                        "ctxt 9216607429\n" +
                        "btime 1499371654\n" +
                        "processes 8116472\n" +
                        "procs_running 1\n" +
                        "procs_blocked 0\n" +
                        "softirq 3995572696 7 1762568953 35455 456569428 0 0 12771 767863139 0 1008522943\n";

        InternalMetricReadingContainer r2 =
                m.parseSourceFileContent(OSType.LINUX, secondFileContentReading.getBytes(), previousReading);

        Float f2 = (Float)r2.getPropertyValue();
        previousReading = r2.getPreParsedContent();

        //
        // the statistics are calculated for the last interval
        //

        assertEquals(30f / 550, f2.floatValue(), 0.00001);

        assertEquals(605L, ((CPUStats)previousReading).getTotalTime());
    }
    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
