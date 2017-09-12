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
import io.novaordis.events.api.event.IntegerProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionBase;
import io.novaordis.linux.CPUStats;
import io.novaordis.linux.ProcStat;
import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.os.OSType;
import io.novaordis.utilities.parsing.ParsingException;
import io.novaordis.utilities.parsing.PreParsedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/6/17
 */
public abstract class OSMetricDefinitionBase extends MetricDefinitionBase implements OSMetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSMetricDefinitionBase.class);

    // Static ----------------------------------------------------------------------------------------------------------

    protected Class TYPE;

    protected String DESCRIPTION;

    protected MeasureUnit BASE_UNIT;

    protected String LABEL;

    //
    // source files
    //
    protected File LINUX_SOURCE_FILE; // null means the metric is not available in a file on Linux

    protected File MAC_SOURCE_FILE;  // null means the metric is not available in a file on MAC

    protected File WINDOWS_SOURCE_FILE;  // null means the metric is not available in a file on MAC

    //
    // OS commands
    //
    protected String LINUX_COMMAND; // null means the metric is not available as output of a command on Linux

    protected Pattern LINUX_PATTERN;

    protected String MAC_COMMAND;  // null means the metric is not available as output of a command on Mac

    protected Pattern MAC_PATTERN;

    protected String WINDOWS_COMMAND;  // null means the metric is not available as output of a command on Windows

    protected Pattern WINDOWS_PATTERN;

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    protected OSMetricDefinitionBase(PropertyFactory propertyFactory, Address metricSourceAddress) {

        super(propertyFactory, metricSourceAddress);

        //
        // For all OS metrics, the ID is conventionally the simple name of the class implementing the metric definition.
        //
        setId(getClass().getSimpleName());
    }

    // MetricDefinition implementation ---------------------------------------------------------------------------------

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
    public File getSourceFile(OSType osType) {

        File sourceFile;

        if (OSType.LINUX.equals(osType)) {

            sourceFile = LINUX_SOURCE_FILE;
        }
        else if (OSType.MAC.equals(osType)) {

            sourceFile = MAC_SOURCE_FILE;
        }
        else if (OSType.WINDOWS.equals(osType)) {

            sourceFile = WINDOWS_SOURCE_FILE;
        }
        else {

            throw new IllegalStateException(osType + " not supported yet");
        }

        return sourceFile;
    }

    @Override
    public Property parseSourceFileContent(OSType osTypes, byte[] sourceFileContent, PreParsedContent previousReading) {

        Property result = getPropertyInstance(getId(), getType(), getBaseUnit());

        if (sourceFileContent == null) {

            //
            // this means the metric cannot be read from a file on the target system, and the method must be prepared
            // to handle this situation by manufacturing a null-value property.
            //

            return result;
        }

        String methodName = null;
        Object value = null;
        boolean knownOS = true;

        try {

            if (OSType.LINUX.equals(osTypes)) {

                methodName = "parseLinuxSourceFileContent";
                value = parseLinuxSourceFileContent(sourceFileContent, previousReading);
            }
            else if (OSType.MAC.equals(osTypes)) {

                methodName = "parseMacSourceFileContent";
                value = parseMacSourceFileContent(sourceFileContent, previousReading);
            }
            else if (OSType.WINDOWS.equals(osTypes)) {

                methodName = "parseWindowsSourceFileContent";
                value = parseWindowsSourceFileContent(sourceFileContent, previousReading);
            }
            else {

                knownOS = false;
                log.warn("OS type " + osTypes + " not supported yet");
            }

            //
            // the method must always return a non-null value, if null is seen here, it is an implementation error
            //

            if (knownOS && value == null) {

                log.warn(getClass().getName() + "." + methodName + "(...) incorrectly implemented, it returns null");
            }

            result.setValue(value);

        }
        catch(ParsingException e) {

            String emsg = e.getMessage();

            String msg = "failed to parse " + getSourceFile(osTypes) + " content";
            if (emsg != null) {

                msg += ": " +  emsg;
            }

            log.warn(msg, e);
        }

        return result;
    }

    @Override
    public String getCommand(OSType osType) {

        String command;

        if (OSType.LINUX.equals(osType)) {

            command = LINUX_COMMAND;
        }
        else if (OSType.MAC.equals(osType)) {

            command = MAC_COMMAND;
        }
        else if (OSType.WINDOWS.equals(osType)) {

            command = WINDOWS_COMMAND;
        }
        else {

            throw new IllegalStateException(osType + " not supported yet");
        }

        return command;
    }

    @Override
    public Property parseCommandOutput(OSType osType, String commandExecutionStdout, PreParsedContent previousReading) {

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

        try {

            if (OSType.LINUX.equals(osType)) {

                methodName = "parseLinuxCommandOutput";
                value = parseLinuxCommandOutput(commandExecutionStdout);
            }
            else if (OSType.MAC.equals(osType)) {

                methodName = "parseMacCommandOutput";
                value = parseMacCommandOutput(commandExecutionStdout);
            }
            else if (OSType.WINDOWS.equals(osType)) {

                methodName = "parseWindowsCommandOutput";
                value = parseWindowsCommandOutput(commandExecutionStdout);
            }
            else {

                knownOS = false;
                log.warn("OS type " + osType + " not supported yet");
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

            log.warn("failed to parse \"" + getCommand(osType) + "\" output: \n\n" + commandExecutionStdout + "\n", e);
        }

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String getSimpleLabel() {

        return LABEL;
    }

    // Package protected static ----------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    //
    // Source file content parsing for various OSes --------------------------------------------------------------------
    //

    /**
     * @param previousReading optional pre-parsed content of the previous reading. May be null.
     *
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content, and the pre-parsed content. The type of the
     * value must match TYPE for this class, as declared above, otherwise the upper layer may throw an
     * IllegalStateException. The value must be expressed in the BASE_UNIT declared above, otherwise the calling layer
     * may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * content, the method must throw an exception containing a human readable message. Any exceptions, checked or
     * unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on Linux, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException on invalid content.
     * @exception IllegalArgumentException if the previous reading is of inappropriate type.
     *
     */
    protected abstract InternalMetricReadingContainer parseLinuxSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException;

    /**
     * @param previousReading optional pre-parsed content of the previous reading. May be null.
     *
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content, and the pre-parsed content. The type of the
     * value must match TYPE for this class, as declared above, otherwise the upper layer may throw an
     * IllegalStateException. The value must be expressed in the BASE_UNIT declared above, otherwise the calling layer
     * may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * content, the method must throw an exception containing a human readable message. Any exceptions, checked or
     * unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on Mac, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException on invalid content.
     * @exception IllegalArgumentException if the previous reading is of inappropriate type.
     *
     */
    protected abstract InternalMetricReadingContainer parseMacSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException;

    /**
     * @param previousReading optional pre-parsed content of the previous reading. May be null.
     *
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content, and the pre-parsed content. The type of the
     * value must match TYPE for this class, as declared above, otherwise the upper layer may throw an
     * IllegalStateException. The value must be expressed in the BASE_UNIT declared above, otherwise the calling layer
     * may throw an IllegalStateException.
     *
     * The method must ALWAYS return a non-null value. If the value cannot be successfully extracted because of invalid
     * content, the method must throw an exception containing a human readable message. Any exceptions, checked or
     * unchecked, should be thrown immediately - the calling layer will log appropriately.
     *
     * If the metric is not available on Windows, the calling layer must not invoke this method.
     *
     * @see MetricDefinition#getBaseUnit()
     * @see MetricDefinition#getBaseUnit()
     *
     * @exception ParsingException on invalid content.
     * @exception IllegalArgumentException if the previous reading is of inappropriate type.
     *
     */
    protected abstract InternalMetricReadingContainer parseWindowsSourceFileContent(byte[] content, PreParsedContent previousReading)
            throws ParsingException;

    //
    // Command output parsing for various OSes -------------------------------------------------------------------------
    //

    /**
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content. The type of the value must match TYPE for this
     * class, as declared above, otherwise the upper layer may throw an IllegalStateException. The value must be
     * expressed in the BASE_UNIT declared above, otherwise the calling layer may throw an IllegalStateException.
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
    protected abstract Object parseLinuxCommandOutput(String commandExecutionStdout) throws ParsingException;

    /**
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content. The type of the value must match TYPE for this
     * class, as declared above, otherwise the upper layer may throw an IllegalStateException. The value must be
     * expressed in the BASE_UNIT declared above, otherwise the calling layer may throw an IllegalStateException.
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
    protected abstract Object parseMacCommandOutput(String commandExecutionStdout) throws ParsingException;


    /**
     * @return the value (not Property, which will be assembled at the upper layer) corresponding to this metric
     * definition, as extracted from the associated source file content. The type of the value must match TYPE for this
     * class, as declared above, otherwise the upper layer may throw an IllegalStateException. The value must be
     * expressed in the BASE_UNIT declared above, otherwise the calling layer may throw an IllegalStateException.
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
    protected abstract Object parseWindowsCommandOutput(String commandExecutionStdout) throws ParsingException;

    // Protected static ------------------------------------------------------------------------------------------------

    /**
     * Performs common processing that needs to be done with the parsed content, in a way that is subclass agnostic;
     * the method can be used by all subclasses.
     *
     * @return a PreParsedContent[3], with the ProcStats instance on the first position, corresponding CPUStats instance
     * on the second position, and the previous CPUStats reading - which can be null, if not available - on the third
     * position.
     */
    protected static PreParsedContent[] distributePreParsedContent(
            byte[] content, PreParsedContent prevReading) throws ParsingException {

        // the previous reading must be a ProcStat, otherwise we throw an illegal argument

        if (prevReading != null && !(prevReading instanceof ProcStat)) {

            throw new IllegalArgumentException(
                    prevReading + " not a " + ProcStat.class.getSimpleName() + " instance");
        }

        ProcStat previousReading = (ProcStat) prevReading;

        PreParsedContent[] result = new PreParsedContent[3];

        ProcStat procStat = new ProcStat(content);
        result[0] = procStat;

        CPUStats cpuStats = procStat.getCumulativeCPUStatistics();
        result[1] = cpuStats;

        CPUStats previousCpuStats = previousReading == null ? null : previousReading.getCumulativeCPUStatistics();
        result[2] = previousCpuStats;

        return result;
    }

    protected Property getPropertyInstance(String id, Class c, MeasureUnit u) {

        //
        // (.) TODO if I am ever in the situation to modify this, add a static Property.getInstance() to
        // io.novaordis.events.api.event.Property and fully implement there
        //

        if (Integer.class.equals(c)) {

            IntegerProperty p = new IntegerProperty(id);
            p.setMeasureUnit(u);
            return p;
        }
        else if (Long.class.equals(c)) {

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

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
