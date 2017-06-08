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

package io.novaordis.events.api.metric.os.mdefs;


import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.LabelAttribute;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.events.api.metric.os.OSMetricDefinition;
import io.novaordis.events.api.metric.os.OSSourceBase;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/3/16
 */
public class MockOSMetricDefinition implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static boolean FAIL_IN_CONSTRUCTOR = false;

    // Attributes ------------------------------------------------------------------------------------------------------

    private String id;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockOSMetricDefinition(OSSourceBase s) {

        if (FAIL_IN_CONSTRUCTOR) {

            throw new RuntimeException("SYNTHETIC");
        }
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MetricSource getSource() {
        throw new RuntimeException("getSource() NOT YET IMPLEMENTED");
    }

    @Override
    public Class getType() {
        throw new RuntimeException("getType() NOT YET IMPLEMENTED");
    }

    @Override
    public MeasureUnit getBaseUnit() {
        throw new RuntimeException("getBaseUnit() NOT YET IMPLEMENTED");
    }

    @Override
    public String getLabel(LabelAttribute... attributes) {
        throw new RuntimeException("getLabel() NOT YET IMPLEMENTED");
    }

    @Override
    public String getDescription() {
        throw new RuntimeException("getDescription() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setId(String s) {
        this.id = s;
    }

    @Override
    public String getCommand() {
        throw new RuntimeException("getCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public String getLinuxCommand() {
        throw new RuntimeException("getLinuxCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public String getMacCommand() {
        throw new RuntimeException("getMacCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public String getWindowsCommand() {
        throw new RuntimeException("getWindowsCommand() NOT YET IMPLEMENTED");
    }

    @Override
    public Property parseCommandOutput(String commandExecutionStdout) {
        throw new RuntimeException("parseCommandOutput() NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
