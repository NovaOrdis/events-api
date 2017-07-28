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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A query comprising structured (event property definitions) and unstructured (keywords, regular expressions) text.
 *
 * Example: "blah blah <property-name>:'something'"
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQuery implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<KeywordQuery> keywordQueries;
    private boolean keywordMatchingCaseSensitive;
    private List<FieldQuery> fieldQueries;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * A mixed query with no query tokens, equivalent with NullQuery.
     */
    public MixedQuery() throws QueryException {

        this.keywordQueries = new ArrayList<>();
        this.fieldQueries = new ArrayList<>();
        this.keywordMatchingCaseSensitive = false;
    }

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        for(KeywordQuery kq: keywordQueries) {

            if (kq.selects(e)) {

                return true;
            }
        }

        for(FieldQuery fq : fieldQueries) {

            if (fq.selects(e)) {

                return true;
            }
        }

        return keywordQueries.isEmpty() && fieldQueries.isEmpty();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void addLiteral(String literal) throws QueryException {

        if (literal == null) {

            throw new IllegalArgumentException("null literal");
        }

        if (literal.contains(":")) {

            FieldQuery q = new FieldQuery(literal);
            fieldQueries.add(q);
        }
        else {

            KeywordQuery q = new KeywordQuery(literal);
            keywordQueries.add(q);
        }
    }

    /**
     * @return the internal storage so handle with care
     */
    public List<KeywordQuery> getKeywordQueries() {

        return keywordQueries;
    }

    public boolean isKeywordMatchingCaseSensitive() {

        return keywordMatchingCaseSensitive;
    }

    /**
     * @return the internal storage so handle with care
     */
    public List<FieldQuery> getFieldQueries() {

        return fieldQueries;
    }

    @Override
    public String toString() {

        String s = "";

        for(Iterator<KeywordQuery> i = keywordQueries.iterator(); i.hasNext(); ) {

            s += "\"" + i.next().toString()  + "\"";

            if (i.hasNext()) {

                s += ", ";
            }
        }

        if (!fieldQueries.isEmpty() && !s.isEmpty()) {

            s += ", ";
        }

        for(Iterator<FieldQuery> i = fieldQueries.iterator(); i.hasNext(); ) {

            s += "\"" + i.next().toString()  + "\"";

            if (i.hasNext()) {

                s += ", ";
            }
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
