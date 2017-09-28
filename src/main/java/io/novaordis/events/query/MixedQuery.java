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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.novaordis.events.api.event.Event;

/**
 * A query comprising structured (event property definitions) and unstructured (keywords, regular expressions) text.
 *
 * Example: "blah blah <property-name>:'something'"
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQuery extends QueryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // only add via addQuery()
    private List<KeywordQuery> keywordQueries;

    private boolean keywordMatchingCaseSensitive;

    // only add via addQuery()
    private List<FieldQuery> fieldQueries;

    // only add via addQuery()
    private List<TimeQuery> timeQueries;

    private List<Query> queryInitializationOrder;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * A mixed query with no query tokens, equivalent with NullQuery.
     */
    public MixedQuery() throws QueryException {

        this.queryInitializationOrder = new ArrayList<>();
        this.keywordQueries = new ArrayList<>();
        this.fieldQueries = new ArrayList<>();
        this.timeQueries = new ArrayList<>();
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

        //
        // offer it in the reverse order in which the queries were added, the latest query has the highest changes
        // of recognizing the argument
        //

        for(int i = queryInitializationOrder.size() - 1; i >= 0; i --) {

            QueryBase q = (QueryBase)queryInitializationOrder.get(i);

            if (q.offerArgument(literal)) {

                //
                // the argument must not be offered to other queries, get out of the loop
                //

                return;
            }
        }

        if (CASE_SENSITIVE_MODIFIER_LITERAL.equals(literal)) {

            keywordMatchingCaseSensitive = true;
            return;
        }

        if (literal.contains(":")) {

            if (literal.startsWith(TimeQuery.FROM_KEYWORD) || literal.startsWith(TimeQuery.TO_KEYWORD)) {

                TimeQuery q = new TimeQuery(literal);
                addQuery(q);
            }
            else {

                FieldQuery q = new FieldQuery(literal);
                addQuery(q);
            }
        }
        else {

            KeywordQuery q = new KeywordQuery(literal);
            addQuery(q);
        }
    }

    /**
     * @return a copy of the internal storage
     */
    public List<KeywordQuery> getKeywordQueries() {

        if (keywordQueries.isEmpty()) {

            return Collections.emptyList();
        }

        ArrayList<KeywordQuery> result = new ArrayList<>();

        for(KeywordQuery k: keywordQueries) {

            k.setCaseSensitive(isKeywordMatchingCaseSensitive());
            result.add(k);
        }

        return result;
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

    /**
     * @return the internal storage so handle with care
     */
    public List<TimeQuery> getTimeQueries() {

        return timeQueries;
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

    void addQuery(Query q) {

        if (q instanceof FieldQuery) {

            fieldQueries.add((FieldQuery)q);
        }
        else if (q instanceof KeywordQuery) {

            keywordQueries.add((KeywordQuery)q);
        }
        else if (q instanceof TimeQuery) {

            timeQueries.add((TimeQuery)q);
        }
        else {

            throw new IllegalArgumentException("unknown query type " + q);
        }

        queryInitializationOrder.add(q);
    }

    /**
     * @return the internal storage, handle with care.
     */
    List<Query> getQueryInitializationOrder() {

        return queryInitializationOrder;
    }

    @Override
    boolean offerArgument(String literal) {

        //
        // we don't look at individual arguments at this level, they must be offered to the lower level queries
        //

        return false;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Validates all delegate queries.
     */
    @Override
    protected void validate(boolean validated) throws QueryException {

        for(KeywordQuery k: keywordQueries) {

            k.validate();
        }

        for(FieldQuery f: fieldQueries) {

            f.validate();
        }

        for(TimeQuery t : timeQueries) {

            t.validate();
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
