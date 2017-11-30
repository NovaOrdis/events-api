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
 * A query operator (AND, OR, NOT)
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/30/17
 */
public enum Operator implements ExpressionElement {

    // Constants -------------------------------------------------------------------------------------------------------

    AND,
    OR,
    NOT {

        @Override
        public Operator negate() throws QueryException {

            return null;
        }
    };

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return null if no operator is recognized.
     */
    public static Operator fromLiteral(String literal) {

        if (literal == null) {

            return null;
        }

        String lup = literal.toUpperCase();

        if (lup.equals(AND.name()) || literal.equals("-a")) {

            return AND;
        }
        else if (lup.equals(OR.name()) || literal.equals("-o")) {

            return OR;
        }
        else if (lup.equals(NOT.name()) || literal.equals("-n")) {

            return NOT;
        }

        return null;
    }

    // ExpressionElement implementation --------------------------------------------------------------------------------

    @Override
    public boolean offerLexicalToken(String literal) throws QueryException {

        return false;
    }

    @Override
    public Operator negate() throws QueryException {

        throw new QueryException("invalid query expression syntax: negation followed by " + this);
    }

    @Override
    public void compile() throws QueryException {

        //
        // noop;
        //
    }

    @Override
    public boolean isCompiled() {

        return true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

}
