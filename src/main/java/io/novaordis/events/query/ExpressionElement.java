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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public interface ExpressionElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * This method exists to allow expression elements (and specifically query elements) a more flexible syntax, where
     * multiple query element tokens can be separated by space, as in time query elements). These query elements are
     * known as multi-word query elements. The method must return true if the token is accepted. If a token is accepted,
     * it is considered "consumed" and must not be offered to other queries. Returns false if the lexical token does
     * not have any significance to this expression element.
     *
     * @exception QueryException when the argument being handled is expected, mandatory, but it is invalid.
     */
    boolean offerLexicalToken(String literal) throws QueryException;


}
