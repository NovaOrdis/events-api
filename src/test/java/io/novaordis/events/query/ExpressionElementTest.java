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

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public abstract class ExpressionElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // offerLexicalToken() ---------------------------------------------------------------------------------------------

    @Test
    public void offerLexicalToken_NotAccepted() throws Exception {

        //
        // Offer a lexical token that will surely be rejected in most cases.
        //
        // Override if you need more refined behavior.
        //

        ExpressionElement e = getExpressionElementToTest();

        assertFalse(e.offerLexicalToken("this-surely-will-be-rejected-as-it-does-not-make-sense-to-any-query"));
    }

    // negate() --------------------------------------------------------------------------------------------------------

    @Test
    public void negate_ProducesADifferentInstance() throws Exception {

        ExpressionElement e = getExpressionElementToTest();

        ExpressionElement negated = e.negate();

        //
        // make sure the instances are different - some specific elements may need to override this test
        //

        assertFalse(e.equals(negated));
    }

    // compile() -------------------------------------------------------------------------------------------------------

    @Test
    public void compile() throws Exception {

        ExpressionElement e = getExpressionElementToTest();

        e.compile();

        // no exceptions
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * @return an elements that compiles correctly.
     */
    protected abstract ExpressionElement getExpressionElementToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
