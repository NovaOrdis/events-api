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

package io.novaordis.events.api.metric.os;

import io.novaordis.utilities.parsing.PreParsedContent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/12/17
 */
public class PreParsedContentPair {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private PreParsedContent current;
    private PreParsedContent previous;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param current must not be null
     * @param previous may be null if not available
     */
    public PreParsedContentPair(PreParsedContent current, PreParsedContent previous) {

        if (current == null) {

            throw new IllegalArgumentException("null current pre-parsed content");
        }

        this.current = current;
        this.previous = previous;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public PreParsedContent getCurrent() {

        return current;
    }

    public PreParsedContent getPrevious() {

        return previous;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
