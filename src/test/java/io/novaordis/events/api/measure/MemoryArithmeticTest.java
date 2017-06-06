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

package io.novaordis.events.api.measure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public class MemoryArithmeticTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // add() -----------------------------------------------------------------------------------------------------------

    @Test
    public void add_InvalidMemoryValue() throws Exception {

        try {

            MemoryArithmetic.add("blah1", "G", "15", "M", MemoryMeasureUnit.BYTE);
            fail("should have thrown exception");
        }
        catch(MemoryArithmeticException e) {

            String msg = e.getMessage();
            assertEquals("invalid memory value \"blah1\"", msg );
        }
    }

    @Test
    public void add_InvalidMemoryValue2() throws Exception {

        try {

            MemoryArithmetic.add("15", "G", "blah2", "M", MemoryMeasureUnit.BYTE);
            fail("should have thrown exception");
        }
        catch(MemoryArithmeticException e) {

            String msg = e.getMessage();
            assertEquals("invalid memory value \"blah2\"", msg );
        }
    }

    @Test
    public void add() throws Exception {

        long r = MemoryArithmetic.add("1", "G", "1024", "M", MemoryMeasureUnit.BYTE);

        assertEquals(2L * 1024 * 1024 * 1024, r);
    }

    // convert() -------------------------------------------------------------------------------------------------------

    @Test
    public void convert_SameMeasureUnit() throws Exception {

        double r = MemoryArithmetic.convert(10, MemoryMeasureUnit.BYTE, MemoryMeasureUnit.BYTE);
        assertEquals(10d, r, 0.000001);
    }

    @Test
    public void convert_UnitSmallToLarge_ExactConversion() throws Exception {

        double r = MemoryArithmetic.convert(2048, MemoryMeasureUnit.BYTE, MemoryMeasureUnit.KILOBYTE);
        assertEquals(2d, r, 0.0000001);
    }

    @Test
    public void convert_UnitLargeToSmall() throws Exception {

        double r = MemoryArithmetic.convert(1, MemoryMeasureUnit.KILOBYTE, MemoryMeasureUnit.BYTE);
        assertEquals(1024d, r, 0.0000001);
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        long result = MemoryArithmetic.parse("999936", "KiB", MemoryMeasureUnit.BYTE);
        assertEquals(999936L * 1024, result);
    }

    @Test
    public void parse_TargetSameAsSource() throws Exception {

        long result = MemoryArithmetic.parse("10", "MB");
        assertEquals(10L, result);
    }

    @Test
    public void parse_MultipleTargetMeasureUnits() throws Exception {

        try {

            MemoryArithmetic.parse("1", "MB", MemoryMeasureUnit.BYTE, MemoryMeasureUnit.MEGABYTE);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("multiple target units", msg);
        }
    }

    @Test
    public void parse_InvalidSourceMeasureUnit() throws Exception {

        try {

            MemoryArithmetic.parse("1", "blah");
            fail("should have thrown exception");
        }
        catch(MemoryArithmeticException e) {

            String msg = e.getMessage();
            assertEquals("invalid source memory unit \"blah\"", msg);
        }
    }

    @Test
    public void parse_InvalidMemoryValue() throws Exception {

        try {

            MemoryArithmetic.parse("blah", "GB");
            fail("should have thrown exception");
        }
        catch(MemoryArithmeticException e) {

            String msg = e.getMessage();
            assertEquals("invalid memory value \"blah\"", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
