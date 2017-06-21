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


import io.novaordis.events.api.event.DateProperty;
import io.novaordis.events.api.event.DoubleProperty;
import io.novaordis.events.api.event.FloatProperty;
import io.novaordis.events.api.event.IntegerProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.StringProperty;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A CSVField implementation based on a string definition, similar to "test-field-name (int)"
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/6/16
 */
public class CSVFieldImpl implements CSVField {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static String typeToCommandLineLiteral(Class type, Format format) {

        String s = "(";

        if (String.class.equals(type)) {

            s += "string";
        }
        else if (Integer.class.equals(type)) {

            s += "int";
        }
        else if (Long.class.equals(type)) {

            s += "long";
        }
        else if (Float.class.equals(type)) {

            s += "float";
        }
        else if (Double.class.equals(type)) {

            s += "double";
        }
        else if (Date.class.equals(type)) {

            s += "time";

            if (format != null) {

                s += ":" + format.toString();
            }
        }

        s += ")";

        return s;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;
    private Class type;
    private Format format;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Build a CSVField instance based on the given specification
     */
    public CSVFieldImpl(String specification) throws CSVFormatException {

        parseFieldSpecification(specification);
    }

    /**
     * Builds a "string" CSVField.
     */
    public CSVFieldImpl(String name, Class type) {

        this.name = name;
        this.type = type;
    }

    // CSVField implementation -----------------------------------------------------------------------------------------

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Class getType() {

        return type;
    }

    @Override
    public Format getFormat() {

        return format;
    }

    @Override
    public Property toProperty(String s) throws IllegalArgumentException {

        if (String.class.equals(getType())) {

            return new StringProperty(getName(), s);
        }
        else if (Integer.class.equals(getType())) {

            int i;

            try {

                i = Integer.parseInt(s);
            }
            catch (Exception e) {
                throw new IllegalArgumentException("invalid int value \"" + s + "\"", e);
            }

            return new IntegerProperty(getName(), i);
        }
        else if (Long.class.equals(getType())) {

            long l;

            try {

                l = Long.parseLong(s);
            }
            catch (Exception e) {

                throw new IllegalArgumentException("invalid long value \"" + s + "\"", e);
            }

            return new LongProperty(getName(), l);
        }
        else if (Float.class.equals(getType())) {

            float f;

            try {

                f = Float.parseFloat(s);
            }
            catch (Exception e) {

                throw new IllegalArgumentException("invalid float value \"" + s + "\"", e);
            }

            return new FloatProperty(getName(), f);
        }
        else if (Double.class.equals(getType())) {

            double d;

            try {

                d = Double.parseDouble(s);
            }
            catch (Exception e) {

                throw new IllegalArgumentException("invalid double value \"" + s + "\"", e);
            }

            return new DoubleProperty(getName(), d);
        }
        else if (Date.class.equals(getType())) {

            Date date;

            try {

                date = ((DateFormat)getFormat()).parse(s);
            }
            catch (Exception e) {

                throw new IllegalArgumentException("invalid time value \"" + s + "\"", e);
            }

            return new DateProperty(getName(), date);
        }

        throw new RuntimeException("toProperty() does not know how to handle " + getType());
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setName(String name) {

        this.name = name;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @Override
    public String toString() {

        return getName() + typeToCommandLineLiteral(getType(), getFormat());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parseFieldSpecification(String fieldSpecification) throws CSVFormatException {

        if (fieldSpecification == null) {

            throw new IllegalArgumentException("null field specification");
        }

        int leftParenthesis = fieldSpecification.indexOf('(');
        int rightParenthesis = fieldSpecification.indexOf(')');

        if (leftParenthesis == -1) {

            //
            // no type information
            //
            if (rightParenthesis != -1) {

                throw new CSVFormatException("unbalanced parentheses");
            }

            this.name = fieldSpecification;
            this.type = String.class;
        }
        else {

            //
            // there is type information
            //

            this.name = fieldSpecification.substring(0, leftParenthesis).trim();

            String typeSpecification = fieldSpecification.substring(leftParenthesis + 1, rightParenthesis);

            if ("string".equals(typeSpecification)) {

                this.type = String.class;
            }
            else if ("int".equals(typeSpecification)) {

                this.type = Integer.class;
            }
            else if ("long".equals(typeSpecification)) {

                this.type = Long.class;
            }
            else if ("float".equals(typeSpecification)) {

                this.type = Float.class;
            }
            else if ("double".equals(typeSpecification)) {

                this.type = Double.class;
            }
            else if (typeSpecification.startsWith("time")) {

                this.type = Date.class;
                parseTimeSpecification(typeSpecification);

            }
            else {

                throw new CSVFormatException("invalid field type specification \"" + typeSpecification + "\"");
            }
        }
    }

    private void parseTimeSpecification(String timeSpecification) throws CSVFormatException {

        //
        // must start with "time"
        //

        if (!timeSpecification.startsWith("time")) {

            throw new IllegalArgumentException("invalid time specification " + timeSpecification);
        }

        timeSpecification = timeSpecification.substring("time".length());

        if (!timeSpecification.startsWith(":")) {

            throw new CSVFormatException("invalid time specification \"" + timeSpecification + "\", missing ':'");
        }

        timeSpecification = timeSpecification.substring(1);

        try {

            format = new SimpleDateFormat(timeSpecification);
        }
        catch(Exception e) {

            throw new CSVFormatException("invalid timestamp format \"" + timeSpecification + "\"", e);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
