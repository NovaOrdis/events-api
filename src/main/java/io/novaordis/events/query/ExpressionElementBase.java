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
public abstract class ExpressionElementBase implements ExpressionElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // ExpressionElement implementation --------------------------------------------------------------------------------

    /**
     * Base implementation, valid in most cases.
     *
     * Override to refine.
     */
    @Override
    public boolean offerLexicalToken(String literal) throws QueryException {

        return false;
    }

    /**
     * Base implementation, valid in most cases.
     *
     * Override to refine.
     */
    @Override
    public void compile() throws QueryException {
    }

    /**
     * Base implementation, valid in most cases.
     *
     * Override to refine.
     */
    @Override
    public boolean isCompiled() {

        return true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
