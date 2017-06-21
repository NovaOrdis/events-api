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

package io.novaordis.events.csv;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.FaultEvent;
import io.novaordis.events.api.event.MapProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricDefinitionParser;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.utilities.time.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Instances of this class convert events into comma-separated value lines containing event's properties values, usually
 * according to a specific CSVFormat.
 *
 * If the CSVFormat is available, the formatter will only include the specified properties in the output. Otherwise, all
 * properties carried by the event are introspected and included in the output.
 *
 * The class contains support for generating headers. A header line is generated when the first event is received and
 * inserted *before* the first event representation. The header starts with a "#" and it contains comma-separated
 * event's properties names. Also see:
 *
 * @see io.novaordis.events.csv.CSVFormatter#setHeaderOn()
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/24/16
 */
public class CSVFormatter {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(CSVFormatter.class);

    private static final boolean debug = log.isDebugEnabled();

    // MM/dd/yy HH:mm:ss (07/25/16 14:00:00) is the default time format so it works straight away with Excel

    public static final DateFormat DEFAULT_TIMESTAMP_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final String NULL_EXTERNALIZATION = "";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return a header. The line starts with "#", then it lists a "timestamp" field and comma-separated property names.
     *
     * We first attempt to resolve the property names by matching the property name to known metric definition IDs.
     * If a known metric definition whose ID matches the property name is identified, we use that metric definition
     * label, instead of teh property name.
     *
     * TODO: should we attempt to parse the property name every time, or we should introduce a "metric repository"?
     *
     * @param outputFormat See CSVFormatter#setOutputFormat(String).
     *
     * @see CSVFormatter#setFormat(CSVFormat)
     */
    public static String outputFormatToHeader(String outputFormat) {

        String headerLine = "# ";

        for(StringTokenizer st = new StringTokenizer(outputFormat, ","); st.hasMoreTokens(); ) {

            String fieldHeader;

            String propertyName = st.nextToken().trim();

            //
            // attempt to identify a known metric definition
            //

            try {

                MetricDefinition md = MetricDefinitionParser.parse(propertyName);
                fieldHeader = md.getLabel();
            }
            catch (Exception e) {

                //
                // that's fine, no known metric definition, use the property name as provided
                //

                fieldHeader = propertyName;
            }

            headerLine += fieldHeader;

            if (st.hasMoreTokens()) {

                headerLine += ", ";
            }
        }

        return headerLine;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    /**
     * @see CSVFormatter#setFormat(CSVFormat)
     */
    private List<String> outputFormat;

    private boolean headerOn;

    private boolean ignoreFaults;

    // Constructors ----------------------------------------------------------------------------------------------------

    public CSVFormatter() {

        outputFormat = null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null for some types of events.
     */
    public String format(Event event) {

        String s = "";

        if (headerOn) {

            String header = getHeader(event);

            if (header != null) {

                s += header;
                s += "\n";
            }

            //
            // automatically turn off header generation after the header is generated
            //
            headerOn = false;
        }

        if (event instanceof FaultEvent) {

            //
            // TODO we may want to consider to send the fault events to stderr so we don't interfere with stdout
            //

            if (isIgnoreFaults()) {

                // we drop the fault - we simply ignore it

                return null;
            }

            s += externalizeFault((FaultEvent)event);
            s += "\n";
        }
        else {

            s += toString(event);
            s += "\n";
        }

        return s;
    }

    /**
     * @param format - a comma separated list of property names and a "timestamp" field. For map properties, we accept
     *               a "map-property-name.key" dot-separated syntax.
     */
    public void setFormat(CSVFormat format) {

//        if (format == null) {
//
//            this.outputFormat = null;
//
//            log.debug(this + " clearing the format");
//            return;
//        }
//
//        outputFormat = new ArrayList<>();
//
//        for(StringTokenizer st = new StringTokenizer(format, ","); st.hasMoreTokens(); ) {
//
//            outputFormat.add(st.nextToken().trim());
//        }

        log.debug(this + " setting output format to ");

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * @see io.novaordis.events.csv.CSVFormatter#setFormat(CSVFormat)
     */
    public CSVFormat getFormat() {

//        if (outputFormat == null) {
//
//            return null;
//        }
//
//        String s = "";
//
//        for(Iterator<String> is = outputFormat.iterator(); is.hasNext(); ) {
//
//            s += is.next();
//
//            if (is.hasNext()) {
//                s += ", ";
//            }
//        }
//
//        return s;

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * Upon invocation, the formatter will output a header line when the next event is received. The setting will
     * reset automatically and immediately after generation of the header line.
     */
    public void setHeaderOn() {

        this.headerOn = true;
    }

    /**
     * @return whether the next event will cause a header to be generated.
     */
    public boolean isHeaderOn() {

        return headerOn;
    }

    public void setIgnoreFaults(boolean b) {
        this.ignoreFaults = b;
    }

    /**
     * @return false if this formatted DOES NOT ignore faults, but renders them as it receives them. Return
     * <tt>true</tt> if this format will simply discard the faults. Faults are controlled by the "--ignore-faults"
     * global option.
     */
    public boolean isIgnoreFaults() {
        return ignoreFaults;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * This method was designed to be overridden by more specialized sub-classes, if they choose so. The method
     * receives the Event and returns a comma-separated string generated based on the sub-class instance state.
     */
    protected String toString(Event event) {

        if (outputFormat != null) {

            return externalizeEventInOutputFormat(outputFormat, event);
        }
        else {

            return externalizeEventViaIntrospection(event);
        }
    }

    /**
     * Subclasses should override this to provide custom headers. The default header is the composed of the name
     * of the fields.
     */
    protected String getHeader(Event event) {

//        String outputFormat = getFormat();
//
//        if (outputFormat != null) {
//
//            return outputFormatToHeader(outputFormat);
//        }
//
//        //
//        // based on the event introspection
//        //
//        return getHeaderViaIntrospection(event);

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private String externalizeEventInOutputFormat(List<String> outputFormat, Event event) {

        String s = "";

        for(Iterator<String> fni = outputFormat.iterator(); fni.hasNext(); ) {

            String fieldName = fni.next();

            if (TimedEvent.TIMESTAMP_PROPERTY_NAME.equals(fieldName)) {

                Timestamp timestamp = null;

                if (event instanceof TimedEvent) {

                    timestamp = ((TimedEvent)event).getTimestamp();
                }

                if (timestamp == null) {

                    s += NULL_EXTERNALIZATION;
                }
                else {

                    s += timestamp.format(DEFAULT_TIMESTAMP_FORMAT);
                }
            }
            else {

                int i;
                String mapKey = null;
                Object externalizedValue = null;

                if ((i = fieldName.indexOf('.')) != -1) {

                    //
                    // map property
                    //

                    mapKey = fieldName.substring(i + 1);
                    fieldName  = fieldName.substring(0, i);
                }


                Property p = event.getProperty(fieldName);

                if (p instanceof MapProperty) {

                    externalizedValue = ((MapProperty)p).externalizeValue(mapKey);
                }
                else if (p != null) {

                    externalizedValue = p.externalizeValue();
                }

                if (externalizedValue == null) {

                    s += NULL_EXTERNALIZATION;
                }
                else {
                    s += externalizedValue;
                }
            }

            if (fni.hasNext()) {
                s += ", ";
            }
        }

        return s;
    }

    private String externalizeEventViaIntrospection(Event event) {

        String s = "";

        Set<Property> properties = event.getProperties();
        List <Property> orderedProperties = new ArrayList<>(properties);
        Collections.sort(orderedProperties);

        if (event instanceof TimedEvent) {

            //
            // if it's a timed event, always start with the timestamp
            //

            Long timestamp = ((TimedEvent)event).getTime();

            if (timestamp == null) {
                s += NULL_EXTERNALIZATION;
            }
            else {
                s += DEFAULT_TIMESTAMP_FORMAT.format(timestamp);
            }

            if (!properties.isEmpty()) {
                s += ", ";
            }
        }

        for(int i = 0; i < orderedProperties.size(); i++) {

            Property p = orderedProperties.get(i);
            String ev = p.externalizeValue();

            if (ev == null) {
                ev = NULL_EXTERNALIZATION;
            }
            s += ev;

            if (i < orderedProperties.size() - 1) {
                s += ", ";
            }

            //
            // TODO Map Handling
            //
//                    int dot = propertyName.indexOf('.');
//                    if (dot != -1) {
//
//                        // map
//
//                        String mapPropertyName = propertyName.substring(0, dot);
//                        MapProperty mp = (MapProperty)event.getProperty(mapPropertyName);
//                        if (mp != null) {
//
//                            String key = propertyName.substring(dot + 1);
//                            Object value = mp.getMap().get(key);
//
//                            if (value != null) {
//                                line += value;
//                            }
//                        }
//                    }
//                    else {
//
//                        Property p = event.getProperty(propertyName);
//
//                        if (p != null) {
//
//                            Object o = p.getValue();
//
//                            if (o instanceof Map) {
//
//                                line += "<>";
//                            } else {
//                                line += o;
//                            }
//                        }
//                    }
//                }

        }

        return s;
    }

    private String getHeaderViaIntrospection(Event event) {

        String s = "# ";

        Set<Property> properties = event.getProperties();
        List <Property> orderedProperties = new ArrayList<>(properties);
        Collections.sort(orderedProperties);

        if (event instanceof TimedEvent) {

            //
            // if it's a timed event, always start with the timestamp
            //

            s += TimedEvent.TIMESTAMP_PROPERTY_NAME;
            if (!properties.isEmpty()) {
                s += ", ";
            }
        }

        for(int i = 0; i < orderedProperties.size(); i++) {

            s += orderedProperties.get(i).getName();
            if (i < orderedProperties.size() - 1) {
                s += ", ";
            }

            //
            // TODO Map Handling
            //
        }

        return s;
    }

    /**
     * @return a String representing the fault, to be sent to output, or null if the formatter was configured to ignore
     * Faults.
     */
    private String externalizeFault(FaultEvent f) {

        if (isIgnoreFaults()) {
            return null;
        }

        String s;

        //
        // make parsing errors easy to read
        //

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable cause = f.getCause();

        if (cause instanceof ParsingException) {

            s = "parsing error";

            ParsingException pe = (ParsingException)cause;
            Long lineNumber = pe.getLineNumber();

            if (lineNumber != null) {
                s += " at line " + lineNumber;
            }

            Integer position = pe.getPositionInLine();

            if (position != null) {

                s += ", position " + position;
            }

            s += ": " + pe.getMessage();
        }
        else {

            //
            // default to toString()
            //

            s = f.toString();
        }

        return s;
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

    // Constants -------------------------------------------------------------------------------------------------------



}
