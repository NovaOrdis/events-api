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

/**
 * Memory arithmetic.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public class MemoryArithmetic {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static long add(
            String memoryQuantity, String memoryMeasureUnit,
            String memoryQuantity2, String memoryMeasureUnit2,
            MemoryMeasureUnit resultMeasureUnit) throws MemoryArithmeticException {

        long m;

        try {

            m = Long.parseLong(memoryQuantity);
        }
        catch(Exception e) {

            throw new MemoryArithmeticException("invalid memory value \"" + memoryQuantity + "\"");
        }

        long m2;

        try {

            m2 = Long.parseLong(memoryQuantity2);
        }
        catch(Exception e) {

            throw new MemoryArithmeticException("invalid memory value \"" + memoryQuantity2 + "\"");
        }

        MemoryMeasureUnit mu = MemoryMeasureUnit.parse(memoryMeasureUnit);
        MemoryMeasureUnit mu2 = MemoryMeasureUnit.parse(memoryMeasureUnit2);

        long result =
                (long)(MemoryArithmetic.convert(m, mu, MemoryMeasureUnit.BYTE) +
                        MemoryArithmetic.convert(m2, mu2, MemoryMeasureUnit.BYTE));

        return (long)MemoryArithmetic.convert(result, MemoryMeasureUnit.BYTE, resultMeasureUnit);
    }

    public static double convert(long value, MemoryMeasureUnit from, MemoryMeasureUnit to) {

        if (from.equals(to)) {

            return value;
        }

        double c = to.getConversionFactor(from);
        return c * value;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
