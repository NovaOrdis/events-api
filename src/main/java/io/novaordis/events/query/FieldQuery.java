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

package io.novaordis.events.query;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.Property;

/**
 * "Field query" and "property query" terms can be used interchangeably, a field query matches when a property with
 * the given name has the value specified by the query.
 *
 * The current implementation requires exact matches, including capitalization. This may be extended in the future.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class FieldQuery implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String propertyName;
    private String propertyValue;

    // Constructors ----------------------------------------------------------------------------------------------------

    public FieldQuery(String propertyName, String propertyValue) {

        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public FieldQuery(String literal) throws QueryException {

        if (literal == null) {

            throw new IllegalArgumentException("null literal");
        }

        int i = literal.indexOf(":");

        if (i == -1) {

            throw new QueryException("not a valid FieldQuery literal, missing colon: \"" + literal + "\"");
        }

        this.propertyName = literal.substring(0, i).trim();

        if (propertyName.isEmpty()) {

            throw new QueryException("not a valid FieldQuery literal, empty field name: \"" + literal + "\"");
        }

        this.propertyValue = literal.substring(i + 1).trim();

        if (propertyValue.isEmpty()) {

            throw new QueryException("not a valid FieldQuery literal, empty field value: \"" + literal + "\"");
        }
    }

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        if (propertyValue == null) {

            //
            // we don't match against null values
            //

            return false;
        }

        Property p = e.getProperty(propertyName);

        return p != null && propertyValue.equals(p.getValue());
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getPropertyName() {

        return propertyName;
    }

    public String getFieldName() {

        return propertyName;
    }

    public Object getValue() {

        return propertyValue;
    }

    @Override
    public String toString() {

        return propertyName + ":" + propertyValue;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
