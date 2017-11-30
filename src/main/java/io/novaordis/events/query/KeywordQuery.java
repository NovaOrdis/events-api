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
import io.novaordis.events.api.parser.QueryOnce;

/**
 * A keyword query matches the keyword against the content of all properties of an event. If there is at least one
 * match, the query matches.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/19/17
 */
public class KeywordQuery extends QueryBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String keyword;

    private boolean caseSensitive;

    // Constructors ----------------------------------------------------------------------------------------------------

    public KeywordQuery(String keyword) {

        if (keyword == null) {

            throw new IllegalArgumentException("null keyword");
        }

        this.keyword = keyword;

        //
        // by default matching is not case sensitive
        //

        this.caseSensitive = false;
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

        for(Property p: e.getProperties()) {

            Object o = p.getValue();

            if (o instanceof String) {

                String target = caseSensitive ? (String)o : ((String)o).toLowerCase();
                String searchKey = caseSensitive ? keyword : keyword.toLowerCase();

                if (target.contains(searchKey)) {

                    return true;
                }
            }
            else {

                //
                // TODO currently we don't attempt to match non-string properties, return here
                //
            }
        }

        return false;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getKeyword() {

        return keyword;
    }

    public boolean isCaseSensitive() {

        return caseSensitive;
    }

    public void setCaseSensitive(boolean b) {

        this.caseSensitive = b;
    }

    @Override
    public String toString() {

        return keyword;
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
