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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.parser.QueryOnce;

/**
 * A field query attempts to match the value of a certain field (property) against a regular expression. Only String
 * properties are matched currently, anything else will not match. "Field query" and "property query" are terms can be
 * used interchangeably.
 *
 * https://kb.novaordis.com/index.php/Events-api_Concepts#Field_Query
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class FieldQuery extends QueryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String propertyName;
    private String regularExpression;
    private Pattern pattern;

    // Constructors ----------------------------------------------------------------------------------------------------

    public FieldQuery(String propertyName, String regularExpression) {

        this.propertyName = propertyName;

        setRegularExpression(regularExpression);

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

        String re = literal.substring(i + 1).trim();

        if (re.isEmpty()) {

            throw new QueryException("not a valid FieldQuery literal, empty regular expression");
        }

        setRegularExpression(re);
    }

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        if (QueryOnce.isQueryOnce(e)) {

            return true;
        }

        if (regularExpression == null) {

            //
            // we don't match against null values
            //

            return false;
        }

        Property p = e.getProperty(propertyName);

        if (p == null) {

            return false;
        }

        Object value = p.getValue();

        if (value == null) {

            return false;
        }

        //
        // for the time being we don't apply regular expression to values other than strings. This is not because of
        // a very thought out reason, just because we want to keep the code simple and we did not have a good use case.
        // If such a case arises, we'll refactor
        //

        if (!(value instanceof String)) {

            return false;
        }

        Matcher m = pattern.matcher((String)value);

        return m.find();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getPropertyName() {

        return propertyName;
    }

    public String getFieldName() {

        return propertyName;
    }

    public String getRegularExpression() {

        return regularExpression;
    }

    public void setRegularExpression(String regex) {

        if (regex == null) {

            throw new IllegalArgumentException("null regular expression");
        }

        if (regex.isEmpty()) {

            throw new IllegalArgumentException("empty regular expression");
        }

        this.regularExpression = regex;

        this.pattern = Pattern.compile(regularExpression);
    }

    public Object getValue() {

        return regularExpression;
    }

    @Override
    public String toString() {

        return propertyName + ":" + regularExpression;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    boolean offerArgument(String literal) {

        //
        // we don't look at individual arguments at this level, yet
        //

        return false;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected void validate(boolean validated) throws QueryException {

        //
        // noop for the time being
        //
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
