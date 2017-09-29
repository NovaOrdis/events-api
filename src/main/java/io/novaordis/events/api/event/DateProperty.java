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

package io.novaordis.events.api.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.novaordis.events.api.measure.MeasureUnit;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/1/16
 */
public class DateProperty extends PropertyBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String DEFAULT_DATE_FORMAT_STRING = "yy/MM/dd HH:mm:ss";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * We must build a SimpleDateFormat instance every time we need it instead of relying on a shared static instance
     * because SimpleDateFormat is not thread safe.
     */
    public static SimpleDateFormat getDefaultDateFormat() {

        return new SimpleDateFormat(DEFAULT_DATE_FORMAT_STRING);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public DateProperty(String name) {

        this(name, null);
    }

    public DateProperty(String name, Date value) {

        this(name, value, null);
    }

    public DateProperty(String name, Date value, MeasureUnit mu) {

        super(name, value, mu);
        setFormat(getDefaultDateFormat());
    }

    // Property implementation -----------------------------------------------------------------------------------------

    @Override
    public Class getType() {
        return Date.class;
    }

    @Override
    public Property fromString(String s) throws IllegalArgumentException {

        try {
            Date d = ((DateFormat)getFormat()).parse(s);
            return new DateProperty(getName(), d);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("\"" + s + "\" cannot be converted to a DateProperty value");
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Date getDate() {

        return (Date)getValue();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
