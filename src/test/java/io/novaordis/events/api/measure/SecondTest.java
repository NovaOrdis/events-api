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
public class SecondTest extends TimeMeasureUnitTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void getLabel() throws Exception {

        String label = TimeMeasureUnit.SECOND.getLabel();
        assertEquals("sec", label);
    }

    // getConversionFactor() -------------------------------------------------------------------------------------------

    @Test
    public void getConversionFactor_millisecondsToSeconds() throws Exception {

        double d = TimeMeasureUnit.SECOND.getConversionFactor(TimeMeasureUnit.MILLISECOND);
        assertEquals(1d/1000, d, 0.00001);
    }

    @Test
    public void getConversionFactor_secondsToSeconds() throws Exception {

        double d = TimeMeasureUnit.SECOND.getConversionFactor(TimeMeasureUnit.SECOND);
        assertEquals(1, (int)d);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected TimeMeasureUnit getMeasureUnitToTest() throws Exception {
        return TimeMeasureUnit.SECOND;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
