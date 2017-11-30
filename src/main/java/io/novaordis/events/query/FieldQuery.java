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

    //
    // this is the original regular expression literal as it provided on the command line. The metacharacters are
    // represented using the application's conventions, not Java regular expression conventions. For example, the
    // parantheses are represented as (, not as \(.
    //
    private String regularExpressionLiteral;

    private String javaRegex;

    private Pattern pattern;

    // Constructors ----------------------------------------------------------------------------------------------------

    public FieldQuery(String propertyName, String regularExpressionLiteral) {

        this.propertyName = propertyName;

        setRegularExpressionLiteral(regularExpressionLiteral);

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

        setRegularExpressionLiteral(re);
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

        if (regularExpressionLiteral == null) {

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

        //noinspection UnnecessaryLocalVariable
        boolean found = m.find();

        return found;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getPropertyName() {

        return propertyName;
    }

    public String getFieldName() {

        return propertyName;
    }

    /**
     * @return the original regular expression literal as it provided on the command line. The metacharacters are
     * represented using the application's conventions, not Java regular expression conventions. For example, the
     * parantheses are represented as (, not as \(.
     */
    public String getRegularExpressionLiteral() {

        return regularExpressionLiteral;
    }

    /**
     * @param regex the original regular expression literal as it provided on the command line. The metacharacters are
     * represented using the application's conventions, not Java regular expression conventions. For example, the
     * parantheses are represented as (, not as \(.
     */
    public void setRegularExpressionLiteral(String regex) {

        if (regex == null) {

            throw new IllegalArgumentException("null regular expression");
        }

        if (regex.isEmpty()) {

            throw new IllegalArgumentException("empty regular expression");
        }

        this.regularExpressionLiteral = regex;

        //
        // convert our metacharacters to Java metacharacters
        //

        this.javaRegex = Regexp.convertMetaCharacters(regularExpressionLiteral);

        this.pattern = Pattern.compile(javaRegex);
    }

    public String getJavaRegex() {

        return javaRegex;
    }

    public Object getValue() {

        return regularExpressionLiteral;
    }

    @Override
    public String toString() {

        return propertyName + ":" + regularExpressionLiteral;
    }

    // Package protected -----------------------------------------------------------------------------------------------

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
