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
 * A container for the extracted property value, and also for the entire pre-parsed reading. This is an optimization,
 * to avoid parsing the same raw content twice.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/11/17
 */
public class MetricReading {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Object propertyValue;
    private PreParsedContent preParsedContent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MetricReading(Object propertyValue, PreParsedContent preParsedContent) {

        this.propertyValue = propertyValue;
        this.preParsedContent = preParsedContent;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Object getPropertyValue() {

        return propertyValue;
    }

    public PreParsedContent getPreParsedContent() {

        return preParsedContent;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
