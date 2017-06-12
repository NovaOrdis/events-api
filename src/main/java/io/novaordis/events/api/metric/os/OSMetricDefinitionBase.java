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

import io.novaordis.events.api.event.DoubleProperty;
import io.novaordis.events.api.event.FloatProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.utilities.address.Address;

import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/6/17
 */
public abstract class OSMetricDefinitionBase extends MetricDefinitionBase implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    protected Class TYPE;
    protected String DESCRIPTION;
    protected MeasureUnit BASE_UNIT;
    protected String LABEL;
    protected String LINUX_COMMAND; // null means the metric is not available on Linux
    protected Pattern LINUX_PATTERN;
    protected String MAC_COMMAND;  // null means the metric is not available on Mac
    protected Pattern MAC_PATTERN;
    protected String WINDOWS_COMMAND;  // null means the metric is not available on Windows
    protected Pattern WINDOWS_PATTERN;

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    protected OSMetricDefinitionBase(Address metricSourceAddress) {

        super(metricSourceAddress);
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

    /**
     * For all OS metrics, the ID is conventionally the simple name of the class implementing the metric definition.
     */
    @Override
    public String getId() {

        return getClass().getSimpleName();
    }

    @Override
    public Class getType() {

        return TYPE;
    }

    @Override
    public MeasureUnit getBaseUnit() {

        return BASE_UNIT;
    }

    @Override
    public String getDescription() {

        return DESCRIPTION;
    }

    // OSMetricDefinition implementation -------------------------------------------------------------------------------

    @Override
    public String getCommand() {

        OSType t = OSType.getCurrent();

        if (OSType.LINUX.equals(t)) {

            return LINUX_COMMAND;
        }
        else if (OSType.MAC.equals(t)) {

            return MAC_COMMAND;
        }
        else if (OSType.WINDOWS.equals(t)) {

            return WINDOWS_COMMAND;
        }
        else {

            throw new IllegalStateException(t + " not supported yet");
        }
    }

    @Override
    public String getLinuxCommand() {

        return LINUX_COMMAND;
    }

    @Override
    public String getMacCommand() {

        return MAC_COMMAND;
    }

    @Override
    public String getWindowsCommand() {

        return WINDOWS_COMMAND;
    }

    @Override
    public Property parseCommandOutput(String commandExecutionStdout) {

        Property result = getPropertyInstance(getId(), getType(), getBaseUnit());

        if (commandExecutionStdout == null) {

            //
            // this means the metric is not available on the target system, and the method must be prepared to handle
            // this situation by manufacturing a null-value property.
            //

            return result;
        }

        String methodName = null;
        Object value = null;
        boolean knownOS = true;

        OSType t = OSType.getCurrent();

        try {

            if (OSType.LINUX.equals(t)) {

                methodName = "parseLinuxCommandOutput";
                value = parseLinuxCommandOutput(commandExecutionStdout);
            }
            else if (OSType.MAC.equals(t)) {

                methodName = "parseMacCommandOutput";
                value = parseMacCommandOutput(commandExecutionStdout);
            }
            else if (OSType.WINDOWS.equals(t)) {

                methodName = "parseWindowsCommandOutput";
                value = parseWindowsCommandOutput(commandExecutionStdout);
            }
            else {

                knownOS = false;
                log.warn("OS type " + t + " not supported yet");
            }

            //
            // the method must always return a non-null value, if null is seen here, it is an implementation error
            //

            if (knownOS && value == null) {

                log.warn(getClass().getName() + "." + methodName + "(...) incorrectly implemented, it returns null");
            }

            result.setValue(value);

        }
        catch(Exception e) {

            log.warn("failed to parse \"" + getCommand() + "\" output: \n\n" + commandExecutionStdout + "\n", e);
        }

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String getSimpleLabel() {

        return LABEL;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    //
    // Command output parsing for various OSes -------------------------------------------------------------------------
    //

    /**
     * @return the value corresponding to this metric definition, as extracted from the output of the associated
     * MacOS command. The type of the value must match TYPE for this class, as declared above, otherwise the calling
     * layer may throw an IllegalStateException. The value must be expressed in the BASE_UNIT declared above,
     * otherwise the calling layer may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * command output, the method must throw an exception containing a human readable message. Any exceptions, checked
     * or unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on MacOS, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException if the expected patters cannot be matched.
     */
    protected abstract Object parseMacCommandOutput(String commandOutput) throws Exception;

    /**
     * @return the value corresponding to this metric definition, as extracted from the output of the associated
     * Linux command. The type of the value must match TYPE for this class, as declared above, otherwise the calling
     * layer may throw an IllegalStateException. The value must be expressed in the BASE_UNIT declared above,
     * otherwise the calling layer may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * command output, the method must throw an exception containing a human readable message. Any exceptions, checked
     * or unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on Linux, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException if the expected patters cannot be matched.
     */
    protected abstract Object parseLinuxCommandOutput(String commandOutput) throws Exception;

    /**
     * @return the value corresponding to this metric definition, as extracted from the output of the associated
     * Windows command. The type of the value must match TYPE for this class, as declared above, otherwise the calling
     * layer may throw an IllegalStateException. The value must be expressed in the BASE_UNIT declared above,
     * otherwise the calling layer may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * command output, the method must throw an exception containing a human readable message. Any exceptions, checked
     * or unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on Windows, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException if the expected patters cannot be matched.
     */
    protected abstract Object parseWindowsCommandOutput(String commandOutput) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    private Property getPropertyInstance(String id, Class c, MeasureUnit u) {

        //
        // (.) TODO if I am ever in the situation to modify this, add a static Property.getInstance() to
        // io.novaordis.events.api.event.Property and fully implement there
        //

        if (Long.class.equals(c)) {

            LongProperty p = new LongProperty(id);
            p.setMeasureUnit(u);
            return p;
        }
        else if (Float.class.equals(c)) {

            FloatProperty p = new FloatProperty(id);
            p.setMeasureUnit(u);
            return p;
        }
        else if (Double.class.equals(c)) {

            DoubleProperty p = new DoubleProperty(id);
            p.setMeasureUnit(u);
            return p;
        }

        throw new RuntimeException("NOT YET IMPLEMENTED: getPropertyInstance(...) for " + c);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
