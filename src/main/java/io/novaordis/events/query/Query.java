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

import java.util.List;

/**
 * A query is a combination of free format and structured text that can be used to filter a stream of events.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public interface Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Extracts a query from the argument list, starting with "from", removing the processed arguments from the list.
     *
     * It removes from the list given as argument all elements until a non-query argument is encountered or the end of
     * the list is reached. The remaining unprocessed arguments are left in the list.
     *
     * If no query is identified, the method returns null.
     *
     * @param args a mutable list.
     */
    static Query fromArguments(List<String> args, int from) throws QueryException {

        if (args.isEmpty()) {

            return null;
        }

        //
        // TODO More efficient implementation needed
        //

        MixedQuery mixedQuery = new MixedQuery();

        for(int i = from; i < args.size(); i ++) {

            String token = args.remove(i--);

            mixedQuery.addLiteral(token);
        }

        List<FieldQuery> fieldQueries = mixedQuery.getFieldQueries();
        List<KeywordQuery> keywordQueries = mixedQuery.getKeywordQueries();

        if (fieldQueries.size() == 0 && keywordQueries.size() == 1) {

            return keywordQueries.get(0);
        }

        if (fieldQueries.size() == 1 && keywordQueries.size() == 0) {

            return fieldQueries.get(0);
        }

        return mixedQuery;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true if the event is selected the query, false otherwise.
     *
     * @exception IllegalArgumentException on null events.
     */
    boolean selects(Event e);

}
