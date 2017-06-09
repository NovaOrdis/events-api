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


import io.novaordis.events.api.event.MockProperty;
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

    private static boolean FAIL_IN_CONSTRUCTOR = false;

    public static void setFailInConstructor(boolean b) {

        FAIL_IN_CONSTRUCTOR = b;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String id;
    private OSSourceBase source;
    private String command;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Invoked by reflection.
     */
    @SuppressWarnings("unused")
    public MockOSMetricDefinition(OSSourceBase s) {

        this(MockOSMetricDefinition.class.getSimpleName(), s, null);
    }

    public MockOSMetricDefinition(String id, OSSourceBase s, String command) {

        if (FAIL_IN_CONSTRUCTOR) {

            throw new RuntimeException("SYNTHETIC");
        }

        this.id = id;
        this.source = s;
        this.command = command;
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    @Override
    public String getId() {

        return id;
    }

    @Override
    public MetricSource getSource() {

        return source;
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

    @Override
    public String getCommand() {

        return command;
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

        //
        // we return the command execution stdout as value of the property, to allow for extra consistency testing
        //
        return new MockProperty(getId(), commandExecutionStdout);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setId(String s) {
        this.id = s;
    }

    @Override
    public String toString() {

        return "" + getId();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
