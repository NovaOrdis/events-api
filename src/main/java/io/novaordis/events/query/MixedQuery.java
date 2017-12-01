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
import java.util.List;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.parser.QueryOnce;

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

    private boolean keywordMatchingCaseSensitive;

    //
    // the transient expression to compile; once compiled, this instance becomes null and the query cannot be modified
    //
    private List<ExpressionElement> transientExpression;

    private boolean nullQuery;

    private Query soleQuery;

    private Query[] andQueries;

    private Query[] orQueries;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * A mixed query with no query tokens, equivalent with NullQuery.
     */
    public MixedQuery() throws QueryException {

        this.transientExpression = new ArrayList<>();
        this.keywordMatchingCaseSensitive = false;
    }

    // QueryBase overrides ---------------------------------------------------------------------------------------------

    @Override
    public MixedQuery negate() throws QueryException {

        throw new RuntimeException("negate() NOT YET IMPLEMENTED");
    }

    /**
     * TODO this is a very crude implementation, must be refactored and improved, both in efficiency and in use case
     * coverage.
     */
    @Override
    public void compile() throws QueryException {

        //
        // apply negation
        //

        for(int i = 0; i < transientExpression.size(); i ++) {

            ExpressionElement crt = transientExpression.get(i);

            if (Operator.NOT.equals(crt)) {

                if (i == transientExpression.size() - 1) {

                    throw new QueryException("the query expression contains an incomplete negation");
                }

                //
                // negate the next element and coalesce
                //

                transientExpression.remove(i);

                ExpressionElement negation = transientExpression.get(i).negate();

                if (negation == null) {

                    transientExpression.remove(i);
                }
                else {

                    transientExpression.set(i, negation);
                }
            }
        }

        //
        // recursively compile
        //

        for(ExpressionElement e: transientExpression) {

            e.compile();
        }

        //
        // assign case sensitiveness to individual keyword queries
        //

        if (keywordMatchingCaseSensitive) {

            //noinspection Convert2streamapi
            for(ExpressionElement e: transientExpression) {

                if (e instanceof KeywordQuery) {

                    ((KeywordQuery)e).setCaseSensitive(true);
                }
            }
        }

        //
        // final processing
        //

        int size = transientExpression.size();

        if (size == 0) {

            this.nullQuery = true;

        }
        else if (size == 1) {

            setSoleQuery((Query) transientExpression.get(0));

        }
        else {

            //
            // TODO hack, must be refactored
            //

            List<Query> queries = new ArrayList<>();

            boolean and = false;

            for (ExpressionElement e : transientExpression) {

                if (Operator.AND.equals(e)) {

                    and = true;
                }
                else if (Operator.OR.equals(e)) {

                    if (and) {

                        throw new RuntimeException("NOT YET IMPLEMENTED: cannot combine AND and OR yet");
                    }
                }
                else {

                    queries.add((Query) e);
                }
            }

            if (and) {

                andQueries = new Query[queries.size()];
                queries.toArray(andQueries);
            }
            else {

                orQueries = new Query[queries.size()];
                queries.toArray(orQueries);
            }
        }

        //
        // we can't modify the query anymore
        //

        transientExpression = null;

    }

    @Override
    public boolean isCompiled() {

        return transientExpression == null;
    }

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        if (!isCompiled()) {

            throw new IllegalStateException("query not compiled");
        }

        if (QueryOnce.isQueryOnce(e)) {

            //
            // QueryOnce-marked events are cleared as soon as possible
            //

            return true;
        }

        if (nullQuery) {

            return true;
        }

        if (soleQuery != null) {

            return soleQuery.selects(e);
        }

        if (orQueries != null) {

            //
            // at least one must succeed, in order
            //

            for(Query q: orQueries) {

                boolean selected = q.selects(e);

                if (selected) {

                    return true;
                }
            }

            return false;
        }

        if (andQueries != null) {

            //
            // all must succeed in order
            //

            for(Query q: andQueries) {

                boolean selected = q.selects(e);

                if (!selected) {

                    return false;
                }
            }

            return true;

        }

        throw new IllegalStateException("invalid state: no null query, no sole query, no and queries and no or queries");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void addExpressionElementLiteral(String literal) throws QueryException {

        if (transientExpression == null) {

            throw new QueryException("the query was compiled and cannot be modified anymore");
        }

        if (literal == null) {

            throw new IllegalArgumentException("null literal");
        }

        //
        // offer the lexical token to the last expression element that was added, to give it a chance to consume it
        //

        if (!transientExpression.isEmpty()) {

            ExpressionElement ee = transientExpression.get(transientExpression.size() - 1);

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

            transientExpression.add(o);
        }
        else if (literal.contains(":")) {

            if (literal.startsWith(TimeQuery.FROM_KEYWORD) || literal.startsWith(TimeQuery.TO_KEYWORD)) {

                TimeQuery q = new TimeQuery(literal);
                transientExpression.add(q);
            }
            else {

                FieldQuery q = new FieldQuery(literal);
                transientExpression.add(q);
            }
        }
        else {

            KeywordQuery q = new KeywordQuery(literal);
            transientExpression.add(q);
        }
    }

    public boolean isKeywordMatchingCaseSensitive() {

        return keywordMatchingCaseSensitive;
    }

    /**
     * "Simplifies" - returns the only sub-query, or itself if that is not possible
     */
    public Query simplify() {

        if (transientExpression != null) {

            throw new IllegalStateException("query not compiled yet");
        }

        if (nullQuery) {

            return new NullQuery();
        }

        if (soleQuery != null) {

            return soleQuery;
        }

        return this;
    }

    @Override
    public String toString() {

        if (nullQuery) {

            return "NULL query";
        }
        else if (soleQuery != null) {

            return soleQuery.toString();
        }
        else {

//            String s = "";
//
//            for (int i = 0; i < expression.size(); i++) {
//
//                s += expression.get(i);
//
//                if (i < expression.size() - 1) {
//
//                    s += " ";
//                }
//            }
//
//            return s;

            return "TBD";
        }

    }

    // Package protected -----------------------------------------------------------------------------------------------

    boolean isNullQuery() {

        return nullQuery;
    }

    Query getSoleQuery() {

        return soleQuery;
    }

    /**
     * Testing.
     */
    void setSoleQuery(Query query) {

        this.soleQuery = query;

        if (query != null) {

            this.nullQuery = false;
        }
    }

    /**
     * May return null.
     */
    Query[] getAndQueries() {

        return andQueries;
    }

    /**
     * May return null.
     */
    Query[] getOrQueries() {

        return orQueries;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
