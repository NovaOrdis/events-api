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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public class OperatorTest extends ExpressionElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void fromLiteral_NoSuchOperator() throws Exception {

        assertNull(Operator.fromLiteral("thereisnosuchoperator"));
    }

    @Test
    public void fromLiteral_Null() throws Exception {

        assertNull(Operator.fromLiteral(null));
    }

    @Test
    public void fromLiteral_AND() throws Exception {

        assertEquals(Operator.AND, Operator.fromLiteral("AND"));
        assertEquals(Operator.AND, Operator.fromLiteral("And"));
        assertEquals(Operator.AND, Operator.fromLiteral("and"));
        assertEquals(Operator.AND, Operator.fromLiteral("-a"));
    }

    @Test
    public void fromLiteral_OR() throws Exception {

        assertEquals(Operator.OR, Operator.fromLiteral("OR"));
        assertEquals(Operator.OR, Operator.fromLiteral("Or"));
        assertEquals(Operator.OR, Operator.fromLiteral("or"));
        assertEquals(Operator.OR, Operator.fromLiteral("-o"));
    }

    @Test
    public void fromLiteral_NOT() throws Exception {

        assertEquals(Operator.NOT, Operator.fromLiteral("NOT"));
        assertEquals(Operator.NOT, Operator.fromLiteral("Not"));
        assertEquals(Operator.NOT, Operator.fromLiteral("not"));
        assertEquals(Operator.NOT, Operator.fromLiteral("-n"));
    }

    @Override
    protected Operator getExpressionElementToTest() throws Exception {

        return Operator.AND;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
