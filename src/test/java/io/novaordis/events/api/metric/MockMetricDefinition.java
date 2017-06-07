/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.events.api.metric;


/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class MockMetricDefinition extends MockMetricDefinitionBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static boolean FAIL_IN_CONSTRUCTOR = false;

    // Attributes ------------------------------------------------------------------------------------------------------

    private String definition;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockMetricDefinition(MetricSource s) {

        super(s);

        if (FAIL_IN_CONSTRUCTOR) {

            throw new RuntimeException("SYNTHETIC");
        }
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getId() {
        return definition;
    }

    @Override
    public String getSimpleLabel() {
        throw new RuntimeException("getSimpleLabel() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setDefinition(String s) {
        this.definition = s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
