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

package io.novaordis.events.api.parser;

/**
 * "Query Once" is a pattern (essentially a hack) that is used to work around the fact that event runtimes that use
 * the query while parsing DO NOT need to apply the query again when processing the events, because the events resulted
 * from parsing *already* matched the query.
 *
 * In order to avoid applying the query multiple times, we proceed as such:
 *
 * 1. If the parser used the query when parsing, it "marks" the event with a query-once=true Boolean property.
 *
 * 2. Each layer that wants to apply the query it first looks for "query-once". If found true, the query is assumed
 * satisfied.
 *
 * All API interaction must be conducted via this class, so the hack can be refactored easier, later.
 *
 * Also see https://kb.novaordis.com/index.php/Events-api_Concepts#Query_Once
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/29/17
 */
public class QueryOnce {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
