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
public class PercentageArithmeticTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_InvalidValue() throws Exception {

        try {

            PercentageArithmetic.parse("blah");
            fail("should have thrown exception");
        }
        catch(PercentageArithmeticException e) {

            String msg = e.getMessage();
            assertEquals("invalid percentage value \"blah\"", msg);
        }
    }

    @Test
    public void parse_NegativeHigh() throws Exception {

        float result = PercentageArithmetic.parse("-1234.56");
        float expected = -1234.56f;
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void parse_NegativeUnder100() throws Exception {

        float result = PercentageArithmetic.parse("-1.56");
        float expected = -1.56f;
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void parse_NegativeUnder1() throws Exception {

        float result = PercentageArithmetic.parse("-0.56");
        float expected = -0.56f;
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void parse_Zero() throws Exception {

        float result = PercentageArithmetic.parse("0.0");
        float expected = 0f;
        assertEquals(expected, result, 0.00001);
    }
    @Test
    public void parse_PositiveUnder1() throws Exception {

        float result = PercentageArithmetic.parse("0.56");
        float expected = 0.56f;
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void parse_PositiveUnder100() throws Exception {

        float result = PercentageArithmetic.parse("12.34");
        float expected = 12.34f;
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void parse_PositiveHigh() throws Exception {

        float result = PercentageArithmetic.parse("1234.56");
        float expected = 1234.56f;
        assertEquals(expected, result, 0.00001);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
