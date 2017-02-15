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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class Percentage implements MeasureUnit {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    private static final Percentage INSTANCE = new Percentage();

    public static Percentage getInstance() {
        return INSTANCE;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private Percentage() {
    }

    // MeasureUnit implementation --------------------------------------------------------------------------------------

    @Override
    public String getLabel() {
        return "%";
    }

    @Override
    public Double getConversionFactor(MeasureUnit that) {

        if (that == null) {
            throw new IllegalArgumentException("null measure unit");
        }

        if (!(that instanceof Percentage)) {
            throw new IllegalArgumentException(that + " cannot be converted to " + this);
        }

        //
        // no conversion
        //

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
