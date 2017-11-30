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

import java.util.List;

import io.novaordis.events.api.event.Event;

/**
 * A query is a combination of free format and structured text that is interpreted by the events runtime to filter a
 * stream of events. An individual event must be selected by the query (match) in order to be considered for further
 * processing.
 *
 * https://kb.novaordis.com/index.php/Events-api_Concepts#Query
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public interface Query extends ExpressionElement {

    // Constants -------------------------------------------------------------------------------------------------------

    String CASE_SENSITIVE_MODIFIER_LITERAL = "--case-sensitive";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Extracts a query from the argument list, starting with "from", removing the processed arguments from the list.
     *
     * It removes from the list given as argument all elements until a non-query argument is encountered or the end of
     * the list is reached. The remaining unprocessed arguments are left in the list.
     *
     * If no query is identified, the method returns null.
     *
     * @param mutableArgumentList a mutable list.
     */
    static Query fromArguments(List<String> mutableArgumentList, int from) throws QueryException {

        if (mutableArgumentList.isEmpty()) {

            return null;
        }

        //
        // TODO More efficient implementation needed, probably need to get rid of MixedQuery
        //

        MixedQuery mixedQuery = new MixedQuery();

        for(int i = from; i < mutableArgumentList.size(); i ++) {

            String lexicalToken = mutableArgumentList.remove(i--);

            mixedQuery.addExpressionElementLiteral(lexicalToken);
        }

        mixedQuery.compile();

        return mixedQuery.simplify();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true if the event is selected by (matches) the query, false otherwise.
     *
     * @exception IllegalArgumentException on null events.
     */
    boolean selects(Event e);

    /**
     * Throw away events that do not match the query and only allow those that match in the final result. Convenience
     * complementary method for selects(): if selects(e) returns true, then filtering a list that contains e will leave
     * e in the result.
     *
     * @exception IllegalArgumentException on null list.
     */
    List<Event> filter(List<Event> events);


}
