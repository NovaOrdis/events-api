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

package io.novaordis.events.api.metric.source;

import io.novaordis.events.api.event.FloatProperty;
import io.novaordis.events.api.event.LongProperty;
import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.measure.Percentage;
import io.novaordis.events.api.metric.os.CpuHardwareInterruptTime;
import io.novaordis.events.api.metric.os.CpuIdleTime;
import io.novaordis.events.api.metric.os.CpuIoWaitTime;
import io.novaordis.events.api.metric.os.CpuKernelTime;
import io.novaordis.events.api.metric.os.CpuNiceTime;
import io.novaordis.events.api.metric.os.CpuSoftwareInterruptTime;
import io.novaordis.events.api.metric.os.CpuStolenTime;
import io.novaordis.events.api.metric.os.CpuUserTime;
import io.novaordis.events.api.metric.os.LoadAverageLastFiveMinutes;
import io.novaordis.events.api.metric.os.LoadAverageLastMinute;
import io.novaordis.events.api.metric.os.LoadAverageLastTenMinutes;
import io.novaordis.events.api.metric.os.PhysicalMemoryFree;
import io.novaordis.events.api.metric.os.PhysicalMemoryTotal;
import io.novaordis.events.api.metric.os.PhysicalMemoryUsed;
import io.novaordis.events.api.metric.os.SwapFree;
import io.novaordis.events.api.metric.os.SwapTotal;
import io.novaordis.events.api.metric.os.SwapUsed;
import io.novaordis.utilities.Files;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class TopTest extends OSCommandTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // other -----------------------------------------------------------------------------------------------------------

//    @Test
//    public void parseLoadAverage() throws Exception {
//
//        List<Property> props = Top.parseLoadAverage(" 1.11, 2.22, 3.33");
//        assertEquals(3, props.size());
//
//        LoadAverageLastMinute metric = new LoadAverageLastMinute(null);
//        FloatProperty p = (FloatProperty)props.get(0);
//        assertNull(p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1.11d, p.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p.getType());
//
//        LoadAverageLastFiveMinutes metric2 = new LoadAverageLastFiveMinutes(null);
//        FloatProperty p2 = (FloatProperty)props.get(1);
//        assertNull(p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(2.22d, p2.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p2.getType());
//
//        LoadAverageLastTenMinutes metric3 = new LoadAverageLastTenMinutes(null);
//        FloatProperty p3 = (FloatProperty)props.get(2);
//        assertNull(p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(3.33d, p3.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p3.getType());
//    }
//
//    // Linux static ----------------------------------------------------------------------------------------------------
//
//    // top format 1 ----------------------------------------------------------------------------------------------------
//
//    @Test
//    public void parseLinuxCommandOutput() throws Exception {
//
//        File f = new File(System.getProperty("basedir"), "src/test/resources/data/metric/top-linux.out");
//        assertTrue(f.isFile());
//        String content = Files.read(f);
//
//        List<Property> ps = Top.parseLinuxCommandOutput(content);
//
//        assertEquals(3 + 8 + 3 + 3, ps.size());
//
//        int i = 0;
//        assertEquals(new LoadAverageLastMinute(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.11f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 1;
//        assertEquals(new LoadAverageLastFiveMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.22f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 2;
//        assertEquals(new LoadAverageLastTenMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.33f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 3;
//        assertEquals(new CpuUserTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.0f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 4;
//        assertEquals(new CpuKernelTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.1f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 5;
//        assertEquals(new CpuNiceTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.2f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 6;
//        assertEquals(new CpuIdleTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(99.8f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 7;
//        assertEquals(new CpuIoWaitTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.3f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 8;
//        assertEquals(new CpuHardwareInterruptTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.4f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 9;
//        assertEquals(new CpuSoftwareInterruptTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.5f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 10;
//        assertEquals(new CpuStolenTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.6f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 11;
//        assertEquals(new PhysicalMemoryTotal(null).getDefinition(), ps.get(i).getName());
//        assertEquals(1040326656L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 12;
//        assertEquals(new PhysicalMemoryFree(null).getDefinition(), ps.get(i).getName());
//        assertEquals(821522432L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 13;
//        assertEquals(new PhysicalMemoryUsed(null).getDefinition(), ps.get(i).getName());
//        assertEquals(88944640L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 14;
//        assertEquals(new SwapTotal(null).getDefinition(), ps.get(i).getName());
//        assertEquals(3072L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 15;
//        assertEquals(new SwapFree(null).getDefinition(), ps.get(i).getName());
//        assertEquals(2048L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 16;
//        assertEquals(new SwapUsed(null).getDefinition(), ps.get(i).getName());
//        assertEquals(1024L, ((Long) ps.get(i).getValue()).longValue());
//    }
//
//    @Test
//    public void parseLinuxCpuInfo() throws Exception {
//
//        List<Property> props = Top.parseLinuxCpuInfo(
//                "  1.1 us,  2.2 sy,  3.3 ni, 44.4 id,  5.5 wa,  6.6 hi,  7.7 si,  8.8 st");
//
//        assertEquals(8, props.size());
//
//        CpuUserTime metric = new CpuUserTime(null);
//        FloatProperty p = (FloatProperty)props.get(0);
//        assertEquals(Percentage.getInstance(), p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1.1f, p.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p.getType());
//
//        CpuKernelTime metric2 = new CpuKernelTime(null);
//        FloatProperty p2 = (FloatProperty)props.get(1);
//        assertEquals(Percentage.getInstance(), p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(2.2f, p2.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p2.getType());
//
//        CpuNiceTime metric3 = new CpuNiceTime(null);
//        FloatProperty p3 = (FloatProperty)props.get(2);
//        assertEquals(Percentage.getInstance(), p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(3.3f, p3.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p3.getType());
//
//        CpuIdleTime metric4 = new CpuIdleTime(null);
//        FloatProperty p4 = (FloatProperty)props.get(3);
//        assertEquals(Percentage.getInstance(), p4.getMeasureUnit());
//        assertEquals(metric4.getDefinition(), p4.getName());
//        assertEquals(44.4f, p4.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p4.getType());
//
//        CpuIoWaitTime metric5 = new CpuIoWaitTime(null);
//        FloatProperty p5 = (FloatProperty)props.get(4);
//        assertEquals(Percentage.getInstance(), p5.getMeasureUnit());
//        assertEquals(metric5.getDefinition(), p5.getName());
//        assertEquals(5.5f, p5.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p5.getType());
//
//        CpuHardwareInterruptTime metric6 = new CpuHardwareInterruptTime(null);
//        FloatProperty p6 = (FloatProperty)props.get(5);
//        assertEquals(Percentage.getInstance(), p6.getMeasureUnit());
//        assertEquals(metric6.getDefinition(), p6.getName());
//        assertEquals(6.6f, p6.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p6.getType());
//
//        CpuSoftwareInterruptTime metric7 = new CpuSoftwareInterruptTime(null);
//        FloatProperty p7 = (FloatProperty)props.get(6);
//        assertEquals(Percentage.getInstance(), p7.getMeasureUnit());
//        assertEquals(metric7.getDefinition(), p7.getName());
//        assertEquals(7.7f, p7.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p7.getType());
//
//        CpuStolenTime metric8 = new CpuStolenTime(null);
//        FloatProperty p8 = (FloatProperty)props.get(7);
//        assertEquals(Percentage.getInstance(), p8.getMeasureUnit());
//        assertEquals(metric8.getDefinition(), p8.getName());
//        assertEquals(8.8f, p8.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p8.getType());
//    }
//
//    @Test
//    public void parseLinuxMemoryInfo() throws Exception {
//
//        List<Property> props = Top.parseLinuxMemoryInfo(
//                "  1015944 total,   802268 free,    86860 used,   126816 buff/cache", MemoryMeasureUnit.KILOBYTE);
//
//        assertEquals(3, props.size());
//
//        PhysicalMemoryTotal metric = new PhysicalMemoryTotal(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1015944l * 1024, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//        PhysicalMemoryFree metric2 = new PhysicalMemoryFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(802268L * 1024, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//
//        PhysicalMemoryUsed metric3 = new PhysicalMemoryUsed(null);
//        LongProperty p3 = (LongProperty)props.get(2);
//        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(86860L * 1024, p3.getLong().longValue());
//        assertEquals(Long.class, p3.getType());
//    }
//
//    @Test
//    public void parseLinuxMemoryInfo_DifferentMeasureUnit() throws Exception {
//
//        List<Property> props = Top.parseLinuxMemoryInfo(
//                "  1015944 total,   802268 free,    86860 used,   126816 buff/cache", MemoryMeasureUnit.BYTE);
//
//        assertEquals(3, props.size());
//
//        PhysicalMemoryTotal metric = new PhysicalMemoryTotal(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1015944L, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//        PhysicalMemoryFree metric2 = new PhysicalMemoryFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(802268L, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//
//        PhysicalMemoryUsed metric3 = new PhysicalMemoryUsed(null);
//        LongProperty p3 = (LongProperty)props.get(2);
//        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(86860L, p3.getLong().longValue());
//        assertEquals(Long.class, p3.getType());
//    }
//
//    @Test
//    public void parseLinuxMemoryInfo_MeasureUnitNotSpecified() throws Exception {
//
//        List<Property> props = Top.parseLinuxMemoryInfo(
//                "  1015945k total,   802269k free,    86861k used,   126817k buff/cache", null);
//
//        assertEquals(3, props.size());
//
//        PhysicalMemoryTotal metric = new PhysicalMemoryTotal(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1015945l * 1024, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//        PhysicalMemoryFree metric2 = new PhysicalMemoryFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(802269L * 1024, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//
//        PhysicalMemoryUsed metric3 = new PhysicalMemoryUsed(null);
//        LongProperty p3 = (LongProperty)props.get(2);
//        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(86861L * 1024, p3.getLong().longValue());
//        assertEquals(Long.class, p3.getType());
//    }
//
//    @Test
//    public void parseLinuxSwapInfo() throws Exception {
//
//        List<Property> props = Top.parseLinuxSwapInfo(
//                "  100 total,        80 free,        20 used.   791404 avail Mem", MemoryMeasureUnit.KILOBYTE);
//
//        assertEquals(3, props.size());
//
//        SwapTotal metric = new SwapTotal(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(100L * 1024, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//        SwapFree metric2 = new SwapFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(80L * 1024, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//
//        SwapUsed metric3 = new SwapUsed(null);
//        LongProperty p3 = (LongProperty)props.get(2);
//        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(20L * 1024, p3.getLong().longValue());
//        assertEquals(Long.class, p3.getType());
//    }
//
//    @Test
//    public void parseLinuxSwapInfo_DifferentMeasureUnit() throws Exception {
//
//        List<Property> props = Top.parseLinuxSwapInfo(
//                "  100 total,        80 free,        20 used.   791404 avail Mem", MemoryMeasureUnit.GIGABYTE);
//
//        assertEquals(3, props.size());
//
//        SwapTotal metric = new SwapTotal(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(100L * 1024 * 1024 * 1024, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//        SwapFree metric2 = new SwapFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(80L * 1024 * 1024 * 1024, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//
//        SwapUsed metric3 = new SwapUsed(null);
//        LongProperty p3 = (LongProperty)props.get(2);
//        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(20L * 1024 * 1024 * 1024, p3.getLong().longValue());
//        assertEquals(Long.class, p3.getType());
//    }
//
//    // top format 2 ----------------------------------------------------------------------------------------------------
//
//    @Test
//    public void parseLinuxCommandOutput2() throws Exception {
//
//        File f = new File(System.getProperty("basedir"), "src/test/resources/data/metric/top-linux-2.out");
//        assertTrue(f.isFile());
//        String content = Files.read(f);
//
//        List<Property> ps = Top.parseLinuxCommandOutput(content);
//
//        assertEquals(3 + 8 + 3 + 3, ps.size());
//
//        int i = 0;
//        assertEquals(new LoadAverageLastMinute(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.24f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 1;
//        assertEquals(new LoadAverageLastFiveMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.35f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 2;
//        assertEquals(new LoadAverageLastTenMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.43f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 3;
//        assertEquals(new CpuUserTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.5f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 4;
//        assertEquals(new CpuKernelTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.1f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 5;
//        assertEquals(new CpuNiceTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.0f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 6;
//        assertEquals(new CpuIdleTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(99.2f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 7;
//        assertEquals(new CpuIoWaitTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.1f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 8;
//        assertEquals(new CpuHardwareInterruptTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.0f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 9;
//        assertEquals(new CpuSoftwareInterruptTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.0f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 10;
//        assertEquals(new CpuStolenTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0.0f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 11;
//        assertEquals(new PhysicalMemoryTotal(null).getDefinition(), ps.get(i).getName());
//        assertEquals(24607916L * 1024, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 12;
//        assertEquals(new PhysicalMemoryUsed(null).getDefinition(), ps.get(i).getName());
//        assertEquals(9873232L * 1024, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 13;
//        assertEquals(new PhysicalMemoryFree(null).getDefinition(), ps.get(i).getName());
//        assertEquals(14734684L * 1024, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 14;
//        assertEquals(new SwapTotal(null).getDefinition(), ps.get(i).getName());
//        assertEquals(4194300L * 1024, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 15;
//        assertEquals(new SwapUsed(null).getDefinition(), ps.get(i).getName());
//        assertEquals(0L, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 16;
//        assertEquals(new SwapFree(null).getDefinition(), ps.get(i).getName());
//        assertEquals(4194300L * 1024, ((Long) ps.get(i).getValue()).longValue());
//    }
//
//    @Test
//    public void parseLinuxCpuInfo2() throws Exception {
//
//        List<Property> props = Top.parseLinuxCpuInfo(
//                "  1.1%us,  2.2%sy,  3.3%ni, 44.4%id,  5.5%wa,  6.6%hi,  7.7%si,  8.8%st");
//
//        assertEquals(8, props.size());
//
//        CpuUserTime metric = new CpuUserTime(null);
//        FloatProperty p = (FloatProperty)props.get(0);
//        assertEquals(Percentage.getInstance(), p.getMeasureUnit());
//        assertEquals(metric.getDefinition(), p.getName());
//        assertEquals(1.1f, p.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p.getType());
//
//        CpuKernelTime metric2 = new CpuKernelTime(null);
//        FloatProperty p2 = (FloatProperty)props.get(1);
//        assertEquals(Percentage.getInstance(), p2.getMeasureUnit());
//        assertEquals(metric2.getDefinition(), p2.getName());
//        assertEquals(2.2f, p2.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p2.getType());
//
//        CpuNiceTime metric3 = new CpuNiceTime(null);
//        FloatProperty p3 = (FloatProperty)props.get(2);
//        assertEquals(Percentage.getInstance(), p3.getMeasureUnit());
//        assertEquals(metric3.getDefinition(), p3.getName());
//        assertEquals(3.3f, p3.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p3.getType());
//
//        CpuIdleTime metric4 = new CpuIdleTime(null);
//        FloatProperty p4 = (FloatProperty)props.get(3);
//        assertEquals(Percentage.getInstance(), p4.getMeasureUnit());
//        assertEquals(metric4.getDefinition(), p4.getName());
//        assertEquals(44.4f, p4.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p4.getType());
//
//        CpuIoWaitTime metric5 = new CpuIoWaitTime(null);
//        FloatProperty p5 = (FloatProperty)props.get(4);
//        assertEquals(Percentage.getInstance(), p5.getMeasureUnit());
//        assertEquals(metric5.getDefinition(), p5.getName());
//        assertEquals(5.5f, p5.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p5.getType());
//
//        CpuHardwareInterruptTime metric6 = new CpuHardwareInterruptTime(null);
//        FloatProperty p6 = (FloatProperty)props.get(5);
//        assertEquals(Percentage.getInstance(), p6.getMeasureUnit());
//        assertEquals(metric6.getDefinition(), p6.getName());
//        assertEquals(6.6f, p6.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p6.getType());
//
//        CpuSoftwareInterruptTime metric7 = new CpuSoftwareInterruptTime(null);
//        FloatProperty p7 = (FloatProperty)props.get(6);
//        assertEquals(Percentage.getInstance(), p7.getMeasureUnit());
//        assertEquals(metric7.getDefinition(), p7.getName());
//        assertEquals(7.7f, p7.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p7.getType());
//
//        CpuStolenTime metric8 = new CpuStolenTime(null);
//        FloatProperty p8 = (FloatProperty)props.get(7);
//        assertEquals(Percentage.getInstance(), p8.getMeasureUnit());
//        assertEquals(metric8.getDefinition(), p8.getName());
//        assertEquals(8.8f, p8.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p8.getType());
//    }
////
////    @Test
////    public void parseLinuxMemoryInfo() throws Exception {
////
////        List<Property> props = Top.parseLinuxMemoryInfo(
////                "  1015944 total,   802268 free,    86860 used,   126816 buff/cache");
////
////        assertEquals(3, props.size());
////
////        PhysicalMemoryTotal metric = new PhysicalMemoryTotal();
////        LongProperty p = (LongProperty)props.get(0);
////        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
////        assertEquals(metric.getDefinition(), p.getName());
////        assertEquals(1015944l * 1024, p.getLong().longValue());
////        assertEquals(Long.class, p.getType());
////
////        PhysicalMemoryFree metric2 = new PhysicalMemoryFree();
////        LongProperty p2 = (LongProperty)props.get(1);
////        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
////        assertEquals(metric2.getDefinition(), p2.getName());
////        assertEquals(802268L * 1024, p2.getLong().longValue());
////        assertEquals(Long.class, p2.getType());
////
////        PhysicalMemoryUsed metric3 = new PhysicalMemoryUsed();
////        LongProperty p3 = (LongProperty)props.get(2);
////        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
////        assertEquals(metric3.getDefinition(), p3.getName());
////        assertEquals(86860L * 1024, p3.getLong().longValue());
////        assertEquals(Long.class, p3.getType());
////    }
////
////    @Test
////    public void parseLinuxSwapInfo() throws Exception {
////
////        List<Property> props = Top.parseLinuxSwapInfo(
////                "  100 total,        80 free,        20 used.   791404 avail Mem");
////
////        assertEquals(3, props.size());
////
////        SwapTotal metric = new SwapTotal();
////        LongProperty p = (LongProperty)props.get(0);
////        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
////        assertEquals(metric.getDefinition(), p.getName());
////        assertEquals(100L * 1024, p.getLong().longValue());
////        assertEquals(Long.class, p.getType());
////
////        SwapFree metric2 = new SwapFree();
////        LongProperty p2 = (LongProperty)props.get(1);
////        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
////        assertEquals(metric2.getDefinition(), p2.getName());
////        assertEquals(80L * 1024, p2.getLong().longValue());
////        assertEquals(Long.class, p2.getType());
////
////        SwapUsed metric3 = new SwapUsed();
////        LongProperty p3 = (LongProperty)props.get(2);
////        assertEquals(MemoryMeasureUnit.BYTE, p3.getMeasureUnit());
////        assertEquals(metric3.getDefinition(), p3.getName());
////        assertEquals(20L * 1024, p3.getLong().longValue());
////        assertEquals(Long.class, p3.getType());
////    }
//
//    // Mac static ------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void parseMacCommandOutput() throws Exception {
//
//        File f = new File(System.getProperty("basedir"), "src/test/resources/data/metric/top-mac.out");
//        assertTrue(f.isFile());
//        String content = Files.read(f);
//
//        List<Property> ps = Top.parseMacCommandOutput(content);
//
//        assertEquals(3 + 3 + 2, ps.size());
//
//        int i = 0;
//        assertEquals(new LoadAverageLastMinute(null).getDefinition(), ps.get(i).getName());
//        assertEquals(1.57f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 1;
//        assertEquals(new LoadAverageLastFiveMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(1.59f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 2;
//        assertEquals(new LoadAverageLastTenMinutes(null).getDefinition(), ps.get(i).getName());
//        assertEquals(1.69f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 3;
//        assertEquals(new CpuUserTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(2.94f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 4;
//        assertEquals(new CpuKernelTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(10.29f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 5;
//        assertEquals(new CpuIdleTime(null).getDefinition(), ps.get(i).getName());
//        assertEquals(86.76f, (Float)ps.get(i).getValue(), 0.0001);
//
//        i = 6;
//        assertEquals(new PhysicalMemoryUsed(null).getDefinition(), ps.get(i).getName());
//        assertEquals(13L * 1024 * 1024 * 1024, ((Long) ps.get(i).getValue()).longValue());
//
//        i = 7;
//        assertEquals(new PhysicalMemoryFree(null).getDefinition(), ps.get(i).getName());
//        assertEquals(2563L * 1024 * 1024, ((Long) ps.get(i).getValue()).longValue());
//    }
//
//    @Test
//    public void parseMacCpuInfo() throws Exception {
//
//        List<Property> props = Top.parseMacCpuInfo(" 2.94% user, 10.29% sys, 86.76% idle");
//        assertEquals(3, props.size());
//
//        CpuUserTime m = new CpuUserTime(null);
//        FloatProperty p = (FloatProperty)props.get(0);
//        assertEquals(Percentage.getInstance(), p.getMeasureUnit());
//        assertEquals(m.getDefinition(), p.getName());
//        assertEquals(2.94f, p.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p.getType());
//
//        CpuKernelTime m2 = new CpuKernelTime(null);
//        FloatProperty p2 = (FloatProperty)props.get(1);
//        assertEquals(Percentage.getInstance(), p2.getMeasureUnit());
//        assertEquals(m2.getDefinition(), p2.getName());
//        assertEquals(10.29f, p2.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p2.getType());
//
//        CpuIdleTime m3 = new CpuIdleTime(null);
//        FloatProperty p3 = (FloatProperty)props.get(2);
//        assertEquals(Percentage.getInstance(), p3.getMeasureUnit());
//        assertEquals(m3.getDefinition(), p3.getName());
//        assertEquals(86.76f, p3.getFloat().floatValue(), 0.00001);
//        assertEquals(Float.class, p3.getType());
//    }
//
//    @Test
//    public void parseMacMemoryInfo() throws Exception {
//
//        List<Property> props = Top.parseMacMemoryInfo("   13G used (1470M wired), 2563M unused.");
//
//        assertEquals(2, props.size());
//
//        PhysicalMemoryUsed m = new PhysicalMemoryUsed(null);
//        LongProperty p = (LongProperty)props.get(0);
//        assertEquals(MemoryMeasureUnit.BYTE, p.getMeasureUnit());
//        assertEquals(m.getDefinition(), p.getName());
//        assertEquals(13L * 1024 * 1024 * 1024, p.getLong().longValue());
//        assertEquals(Long.class, p.getType());
//
//
//        PhysicalMemoryFree m2 = new PhysicalMemoryFree(null);
//        LongProperty p2 = (LongProperty)props.get(1);
//        assertEquals(MemoryMeasureUnit.BYTE, p2.getMeasureUnit());
//        assertEquals(m2.getDefinition(), p2.getName());
//        assertEquals(2563L * 1024 * 1024, p2.getLong().longValue());
//        assertEquals(Long.class, p2.getType());
//    }
//
//    // accessors -------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void accessors() throws Exception {
//
//        Top top = new Top("something something else");
//
//        assertEquals("top", top.getCommand());
//        assertEquals("something something else", top.getArguments());
//    }
//
//    // extractMemoryMeasureUnitHeuristics() ----------------------------------------------------------------------------
//
//    @Test
//    public void extractMemoryMeasureUnitHeuristics_Default() throws Exception {
//
//        Top.StringMeasureUnitPair p = Top.extractMemoryMeasureUnitHeuristics("123");
//        assertEquals("123", p.s);
//        assertEquals(MemoryMeasureUnit.BYTE, p.memoryMeasureUnit);
//    }
//
//    @Test
//    public void extractMemoryMeasureUnitHeuristics_Kilobytes() throws Exception {
//
//        Top.StringMeasureUnitPair p = Top.extractMemoryMeasureUnitHeuristics("123k");
//        assertEquals("123", p.s);
//        assertEquals(MemoryMeasureUnit.KILOBYTE, p.memoryMeasureUnit);
//    }
//
//    @Test
//    public void extractMemoryMeasureUnitHeuristics_Kilobytes_EndsInSpace() throws Exception {
//
//        Top.StringMeasureUnitPair p = Top.extractMemoryMeasureUnitHeuristics("123k ");
//        assertEquals("123", p.s);
//        assertEquals(MemoryMeasureUnit.KILOBYTE, p.memoryMeasureUnit);
//    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Top getMetricSourceToTest(String arguments) throws Exception {

        return new Top(arguments);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
