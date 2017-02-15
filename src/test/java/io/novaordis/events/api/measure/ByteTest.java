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

package io.novaordis.events.api.measure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class ByteTest extends MemoryMeasureUnitTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void getLabel() throws Exception {

        String label = MemoryMeasureUnit.BYTE.getLabel();
        assertEquals("bytes", label);
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_b() throws Exception {

        MemoryMeasureUnit mmu = MemoryMeasureUnit.parse("b");
        assertEquals(MemoryMeasureUnit.BYTE, mmu);
    }

    @Test
    public void parse_b_CaseNotImportant() throws Exception {

        MemoryMeasureUnit mmu = MemoryMeasureUnit.parse("B");
        assertEquals(MemoryMeasureUnit.BYTE, mmu);
    }

    @Test
    public void parse_bytes() throws Exception {

        MemoryMeasureUnit mmu = MemoryMeasureUnit.parse("bytes");
        assertEquals(MemoryMeasureUnit.BYTE, mmu);
    }

    @Test
    public void parse_DeclaredUnit() throws Exception {

        MemoryMeasureUnit mmu = MemoryMeasureUnit.parse("BYTE");
        assertEquals(MemoryMeasureUnit.BYTE, mmu);
    }

    // getConversionFactor() -------------------------------------------------------------------------------------------

    @Test
    public void getConversionFactor_bytesToBytes() throws Exception {

        double d = MemoryMeasureUnit.BYTE.getConversionFactor(MemoryMeasureUnit.BYTE);
        assertEquals(1, (int)d);
    }

    @Test
    public void getConversionFactor_KilobytesToBytes() throws Exception {

        double d = MemoryMeasureUnit.BYTE.getConversionFactor(MemoryMeasureUnit.KILOBYTE);
        assertEquals(1024, (int)d);
    }

    @Test
    public void getConversionFactor_MegabytesToBytes() throws Exception {

        double d = MemoryMeasureUnit.BYTE.getConversionFactor(MemoryMeasureUnit.MEGABYTE);
        assertEquals(1024 * 1024, (int)d);
    }

    @Test
    public void getConversionFactor_GigabytesToBytes() throws Exception {

        double d = MemoryMeasureUnit.BYTE.getConversionFactor(MemoryMeasureUnit.GIGABYTE);
        assertEquals(1024 * 1024 * 1024, (int)d);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MemoryMeasureUnit getMeasureUnitToTest() throws Exception {
        return MemoryMeasureUnit.BYTE;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
