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

import io.novaordis.events.api.measure.MemoryMeasureUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/2/16
 */
public class PropertyFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(PropertyFactoryTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // createInstance() ------------------------------------------------------------------------------------------------

    @Test
    public void createInstance_String() throws Exception {

        StringProperty sp = (StringProperty)PropertyFactory.createInstance("test", String.class, "something", null);

        assertEquals("test", sp.getName());
        assertEquals(String.class, sp.getType());
        assertEquals("something", sp.getString());
    }

    @Test
    public void createInstance_String_Null() throws Exception {

        StringProperty sp = (StringProperty)PropertyFactory.createInstance("test", String.class, null, null);
        assertEquals("test", sp.getName());
        assertEquals(String.class, sp.getType());
        assertNull(sp.getString());
    }

    @Test
    public void createInstance_String_TypeMismatch() throws Exception {

        try {
            PropertyFactory.createInstance("test", String.class, 1, null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_Integer() throws Exception {

        IntegerProperty ip = (IntegerProperty)PropertyFactory.createInstance("test", Integer.class, 1, null);

        assertEquals("test", ip.getName());
        assertEquals(Integer.class, ip.getType());
        assertEquals(1, ip.getInteger().intValue());
    }

    @Test
    public void createInstance_Integer_NullMultiplicationFactor() throws Exception {

        IntegerProperty ip = (IntegerProperty)PropertyFactory.createInstance("test", Integer.class, 1, null, null);

        assertEquals("test", ip.getName());
        assertEquals(Integer.class, ip.getType());
        assertEquals(1, ip.getInteger().intValue());
    }

    @Test
    public void createInstance_Integer_MultiplicationFactor() throws Exception {

        IntegerProperty ip = (IntegerProperty)PropertyFactory.createInstance("test", Integer.class, 1, 10d, null);

        assertEquals("test", ip.getName());
        assertEquals(Integer.class, ip.getType());
        assertEquals(10, ip.getInteger().intValue());
    }

    @Test
    public void createInstance_Integer_Null() throws Exception {

        IntegerProperty ip = (IntegerProperty)PropertyFactory.createInstance("test", Integer.class, null, null);

        assertEquals("test", ip.getName());
        assertEquals(Integer.class, ip.getType());
        assertNull(ip.getInteger());
    }

    @Test
    public void createInstance_Integer_TypeMismatch() throws Exception {

        try {
            PropertyFactory.createInstance("test", Integer.class, "1", null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_Long() throws Exception {

        LongProperty ip = (LongProperty)PropertyFactory.createInstance("test", Long.class, 1L, null);

        assertEquals("test", ip.getName());
        assertEquals(Long.class, ip.getType());
        assertEquals(1L, ip.getLong().longValue());
    }

    @Test
    public void createInstance_Long_NullMultiplicationFactor() throws Exception {

        LongProperty ip = (LongProperty)PropertyFactory.createInstance("test", Long.class, 1L, null, null);

        assertEquals("test", ip.getName());
        assertEquals(Long.class, ip.getType());
        assertEquals(1L, ip.getLong().longValue());
    }

    @Test
    public void createInstance_Long_MultiplicationFactor() throws Exception {

        LongProperty ip = (LongProperty)PropertyFactory.createInstance("test", Long.class, 1L, 10d, null);

        assertEquals("test", ip.getName());
        assertEquals(Long.class, ip.getType());
        assertEquals(10L, ip.getLong().longValue());
    }

    @Test
    public void createInstance_Long_Null() throws Exception {

        LongProperty ip = (LongProperty)PropertyFactory.createInstance("test", Long.class, null, null);

        assertEquals("test", ip.getName());
        assertEquals(Long.class, ip.getType());
        assertNull(ip.getLong());
    }

    @Test
    public void createInstance_Long_TypeMismatch() throws Exception {

        try {
            PropertyFactory.createInstance("test", Long.class, "1", null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_Double() throws Exception {

        DoubleProperty dp = (DoubleProperty)PropertyFactory.createInstance("test", Double.class, 1d, null);

        assertEquals("test", dp.getName());
        assertEquals(Double.class, dp.getType());
        assertEquals(1.0, dp.getDouble().doubleValue(), 0.0001);
    }

    @Test
    public void createInstance_Double_NullMultiplicationFactor() throws Exception {

        DoubleProperty dp = (DoubleProperty)PropertyFactory.createInstance("test", Double.class, 1d, null, null);

        assertEquals("test", dp.getName());
        assertEquals(Double.class, dp.getType());
        assertEquals(1L, dp.getDouble().doubleValue(), 0.0001);
    }

    @Test
    public void createInstance_Double_MultiplicationFactor() throws Exception {

        DoubleProperty dp = (DoubleProperty)PropertyFactory.createInstance("test", Double.class, 1d, 10d, null);

        assertEquals("test", dp.getName());
        assertEquals(Double.class, dp.getType());
        assertEquals(10.0, dp.getDouble().doubleValue(), 0.0001);
    }

    @Test
    public void createInstance_Double_Null() throws Exception {

        DoubleProperty dp = (DoubleProperty)PropertyFactory.createInstance("test", Double.class, null, null);

        assertEquals("test", dp.getName());
        assertEquals(Double.class, dp.getType());
        assertNull(dp.getDouble());
    }

    @Test
    public void createInstance_Double_TypeMismatch() throws Exception {

        try {
            PropertyFactory.createInstance("test", Double.class, "1", null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_Float() throws Exception {

        FloatProperty fp = (FloatProperty)PropertyFactory.createInstance("test", Float.class, 1.1f, null);

        assertEquals("test", fp.getName());
        assertEquals(Float.class, fp.getType());
        assertEquals(1.1f, fp.getFloat().floatValue(), 0.0001);
    }

    @Test
    public void createInstance_Float_NullMultiplicationFactor() throws Exception {

        FloatProperty fp = (FloatProperty)PropertyFactory.createInstance("test", Float.class, 1.1f, null, null);

        assertEquals("test", fp.getName());
        assertEquals(Float.class, fp.getType());
        assertEquals(1.1f, fp.getFloat().floatValue(), 0.0001);
    }

    @Test
    public void createInstance_Float_MultiplicationFactor() throws Exception {

        FloatProperty fp = (FloatProperty)PropertyFactory.createInstance("test", Float.class, 1.1f, 10d, null);

        assertEquals("test", fp.getName());
        assertEquals(Float.class, fp.getType());
        assertEquals(11.0f, fp.getFloat().floatValue(), 0.0001);
    }

    @Test
    public void createInstance_Float_Null() throws Exception {

        FloatProperty fp = (FloatProperty)PropertyFactory.createInstance("test", Float.class, null, null);

        assertEquals("test", fp.getName());
        assertEquals(Float.class, fp.getType());
        assertNull(fp.getFloat());
    }

    @Test
    public void createInstance_Float_TypeMismatch() throws Exception {

        try {
            PropertyFactory.createInstance("test", Float.class, "1", null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_Date() throws Exception {

        DateProperty ip = (DateProperty)PropertyFactory.createInstance("test", Date.class, 1L, null);

        assertEquals("test", ip.getName());
        assertEquals(Date.class, ip.getType());
        assertEquals(1L, ip.getDate().getTime());
    }

    @Test
    public void createInstance_Map() throws Exception {

        Map<String, String> map = new HashMap<>();
        MapProperty mp = (MapProperty)PropertyFactory.createInstance("test", Map.class, map, null);

        assertEquals("test", mp.getName());
        assertEquals(Map.class, mp.getType());
        assertEquals(map, mp.getMap());
    }

    @Test
    public void createInstance_Map_Null() throws Exception {

        MapProperty mp = (MapProperty)PropertyFactory.createInstance("test", Map.class, null, null);

        assertEquals("test", mp.getName());
        assertEquals(Map.class, mp.getType());
        Map<String, Object> map = mp.getMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void createInstance_Map_TypeMismatch() throws Exception {

        try {

            PropertyFactory.createInstance("test", Map.class, "1", null);
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void createInstance_UnknownType_NullValue_NullMeasureUnit() throws Exception {

        Property p = PropertyFactory.createInstance("test", null, null, null);

        assertTrue(p instanceof UndefinedTypeProperty);

        UndefinedTypeProperty utp = (UndefinedTypeProperty)p;

        assertEquals("test", utp.getName());
        assertNull(utp.getType());
        assertNull(utp.getValue());
        assertNull(utp.getMeasureUnit());
        assertNull(utp.getFormat());
    }

    @Test
    public void createInstance_UnknownType_NullValue_NonNullMeasureUnit() throws Exception {

        try {

            // this is an invalid mode of invoking the method
            PropertyFactory.createInstance("test", null, null, MemoryMeasureUnit.BYTE);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(
                    "cannot create an " + UndefinedTypeProperty.class.getSimpleName() +
                            " instance when the measure unit is specified"));
        }
    }

    @Test
    public void createInstance_TypeHeuristics_Integer() throws Exception {

        IntegerProperty p = (IntegerProperty)PropertyFactory.createInstance("test", null, 1, null);
        assertEquals("test", p.getName());
        assertEquals(1, p.getInteger().intValue());
    }

    //
    // more heuristic typing testing under "createTypeHeuristicsInstance()"
    //

    // createTypeHeuristicsInstance() ----------------------------------------------------------------------------------

    @Test
    public void createTypeHeuristicsInstance_Null() throws Exception {

        try {

            PropertyFactory.createTypeHeuristicsInstance("test", null, null, null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null value"));
            assertTrue(msg.contains("cannot infer type"));
        }
    }

    @Test
    public void createTypeHeuristicsInstance_Integer() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", 7, null, null);

        assertNotNull(p);

        IntegerProperty ip = (IntegerProperty)p;

        assertEquals("test", ip.getName());
        assertEquals(7, ip.getValue());
        assertEquals(7, ip.getInteger().intValue());
        assertEquals(Integer.class, ip.getType());
        assertNull(ip.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_IntegerFromString() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", "7", null, null);

        assertNotNull(p);

        IntegerProperty ip = (IntegerProperty)p;

        assertEquals("test", ip.getName());
        assertEquals(7, ip.getValue());
        assertEquals(7, ip.getInteger().intValue());
        assertEquals(Integer.class, ip.getType());
        assertNull(ip.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_Long() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", 7L, null, null);

        assertNotNull(p);

        LongProperty ip = (LongProperty)p;

        assertEquals("test", ip.getName());
        assertEquals(7L, ip.getValue());
        assertEquals(7L, ip.getLong().longValue());
        assertEquals(Long.class, ip.getType());
        assertNull(ip.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_String() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", "something", null, null);

        assertNotNull(p);

        StringProperty p2 = (StringProperty)p;

        assertEquals("test", p2.getName());
        assertEquals("something", p2.getValue());
        assertEquals("something", p2.getString());
        assertEquals(String.class, p2.getType());
        assertNull(p2.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_Float() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", 1.0f, null, null);

        assertNotNull(p);

        FloatProperty p2 = (FloatProperty)p;

        assertEquals("test", p2.getName());
        assertEquals(1.0f, p2.getFloat().floatValue(), 0.000001);
        assertEquals(Float.class, p2.getType());
        assertNull(p2.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_FloatFromString() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", "7.0", null, null);

        assertNotNull(p);

        FloatProperty p2 = (FloatProperty)p;

        assertEquals("test", p2.getName());
        assertEquals(7.0f, p2.getFloat().floatValue(), 0.000001);
        assertEquals(Float.class, p2.getType());
        assertNull(p2.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_Double() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("test", 1.0d, null, null);

        assertNotNull(p);

        DoubleProperty p2 = (DoubleProperty)p;

        assertEquals("test", p2.getName());
        assertEquals(1.0d, p2.getDouble().floatValue(), 0.000001);
        assertEquals(Double.class, p2.getType());
        assertNull(p2.getMeasureUnit());
    }

    @Test
    public void createTypeHeuristicsInstance_Timestamp_Pattern1() throws Exception {

        Property p = PropertyFactory.createTypeHeuristicsInstance("something", "12/31/16 23:00:01", null, null);

        assertNotNull(p);

        TimestampProperty p2 = (TimestampProperty)p;

        assertEquals("something", p2.getName());

        String expectedFormat = "MM/dd/yy HH:mm:ss";

        SimpleDateFormat f = (SimpleDateFormat)p2.getFormat();
        assertEquals(expectedFormat, f.toPattern());

        assertEquals((new SimpleDateFormat(expectedFormat)).parse("12/31/16 23:00:01").getTime(), p2.getValue());

        assertEquals(Long.class, p2.getType());

        assertNull(p2.getMeasureUnit());
    }

    // conversions -----------------------------------------------------------------------------------------------------

    @Test
    public void createInstance_StringToIntegerConversion() throws Exception {

        IntegerProperty ip = (IntegerProperty)PropertyFactory.createInstance("test", Integer.class, "1", null);

        assertEquals("test", ip.getName());
        assertEquals(Integer.class, ip.getType());
        assertEquals(1, ip.getInteger().intValue());
    }

    @Test
    public void createInstance_StringToIntegerConversionFails() throws Exception {

        try {
            PropertyFactory.createInstance("test", Integer.class, "blah", null);
            fail("should throw Exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("cannot convert \"blah\" to an integer", msg);
        }
    }

    @Test
    public void createInstance_StringToLongConversion() throws Exception {

        LongProperty lp = (LongProperty)PropertyFactory.createInstance("test", Long.class, "1", null);

        assertEquals("test", lp.getName());
        assertEquals(Long.class, lp.getType());
        assertEquals(1L, lp.getLong().longValue());
    }

    @Test
    public void createInstance_StringToLongConversionFails() throws Exception {

        try {
            PropertyFactory.createInstance("test", Long.class, "blah", null);
            fail("should throw Exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("cannot convert \"blah\" to a long", msg);
        }
    }

    @Test
    public void createInstance_StringToDoubleConversion() throws Exception {

        DoubleProperty dp = (DoubleProperty)PropertyFactory.createInstance("test", Double.class, "1.1", null);

        assertEquals("test", dp.getName());
        assertEquals(Double.class, dp.getType());
        assertEquals(1.1d, dp.getDouble().doubleValue(), 0.0001);
    }

    @Test
    public void createInstance_StringToDoubleConversionFails() throws Exception {

        try {
            PropertyFactory.createInstance("test", Double.class, "blah", null);
            fail("should throw Exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("cannot convert \"blah\" to a double", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
