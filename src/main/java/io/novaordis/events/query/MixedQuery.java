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
 * TODO: temporary implementation, waiting to be refactored
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQuery extends QueryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    //
    // only add via addQuery()
    //
    private List<KeywordQuery> keywordQueries;

    private boolean keywordMatchingCaseSensitive;

    //
    // only add via addQuery()
    //
    private List<FieldQuery> fieldQueries;

    //
    // only add via addQuery()
    //
    private List<TimeQuery> timeQueries;

    private List<ExpressionElement> expression;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * A mixed query with no query tokens, equivalent with NullQuery.
     */
    public MixedQuery() throws QueryException {

        this.expression = new ArrayList<>();
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

    public void addExpressionElementLiteral(String literal) throws QueryException {

        if (literal == null) {

            throw new IllegalArgumentException("null literal");
        }

        //
        // offer the lexical token to the last expression element that was added, to give it a chance to consume it
        //

        if (!expression.isEmpty()) {

            ExpressionElement ee = expression.get(expression.size() - 1);

            if (ee.offerLexicalToken(literal)) {

                //
                // we're done with this lexical element, get out
                //

                return;
            }
        }

        if (CASE_SENSITIVE_MODIFIER_LITERAL.equals(literal)) {

            keywordMatchingCaseSensitive = true;
            return;
        }

        Operator o;

        if ((o = Operator.fromLiteral(literal)) != null) {

            addOperator(o);
        }
        else if (literal.contains(":")) {

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

    public void addQuery(Query q) {

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

        expression.add(q);
    }

    public void addOperator(Operator o) {

        expression.add(o);
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

    /**
     * The query expression in the format (order) in which was parsed from arguments.
     *
     * @return the internal storage, handle with care.
     */
    List<ExpressionElement> getExpression() {

        return expression;
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
