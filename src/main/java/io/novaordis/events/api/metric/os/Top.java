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

package io.novaordis.events.api.metric.os;

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.events.api.measure.MeasureUnit;
import io.novaordis.events.api.measure.MemoryMeasureUnit;
import io.novaordis.events.api.metric.MetricException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.source.OSCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
@Deprecated
public class Top extends OSCommand {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Top.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Works both on Linux and Mac.
     * @param s - expected format " 1.57, 1.59, 1.69"
     */
    public static List<Property> parseLoadAverage(String s) throws MetricException {

        List<Property> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s, ", ");
        MetricDefinition[] expected = new MetricDefinition[] {
                new LoadAverageLastMinute(null),
                new LoadAverageLastFiveMinutes(null),
                new LoadAverageLastTenMinutes(null)
        };

        for(MetricDefinition m: expected) {
            if (st.hasMoreTokens()) {
                String tok = st.nextToken();
                result.add(PropertyFactory.createInstance(m.getId(), m.getType(), tok, null, m.getBaseUnit()));
            }
        }
        return result;
    }

    private static List<Property> parseCpuInfo(String s, Object[][] expectedLabelsAndMetrics)
            throws MetricException {

        List<Property> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(s, ",");

        while(st.hasMoreTokens()) {
            String tok = st.nextToken();
            for (Object[] e : expectedLabelsAndMetrics) {
                String label = (String) e[0];
                MetricDefinition m = (MetricDefinition) e[1];
                int i = tok.indexOf(label);
                if (i != -1) {
                    tok = tok.substring(0, i).replace("%", "").trim();
                    result.add(PropertyFactory.createInstance(m.getId(), m.getType(), tok, null, m.getBaseUnit()));
                }
            }
        }
        return result;
    }

    public static List<Property> parseLinuxCommandOutput(String output) throws MetricException {

        List<Property> result = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(output, "\n");
        while(st.hasMoreTokens()) {

            String line = st.nextToken();

            int i = line.indexOf("load average:");
            if (i != -1) {

                List<Property> loadAverage = parseLoadAverage(line.substring(i + "load average:".length()));
                result.addAll(loadAverage);
            }
            else if (line.matches(".*Cpu.*:.*")) {
                i = line.indexOf(":");
                List<Property> cpu = parseLinuxCpuInfo(line.substring(i + 1));
                result.addAll(cpu);
            }
            else if (line.matches(".*Mem.*:.*")) {

                //
                // we get a chance to figure out the measure unit
                //
                MemoryMeasureUnit mmu = null;
                i = line.indexOf("Mem");
                if (i != 0) {
                    //
                    // measure unit leads
                    //
                    String measureUnit = line.substring(0, i).trim();
                    mmu = MemoryMeasureUnit.parse(measureUnit);
                }
                i = line.indexOf(":");
                List<Property> memory = parseLinuxMemoryInfo(line.substring(i + 1), mmu);
                result.addAll(memory);
            }
            else if (line.matches(".*Swap.*:.*")) {

                //
                // we get a chance to figure out the measure unit
                //
                MemoryMeasureUnit mmu = null;
                i = line.indexOf("Swap");
                if (i != 0) {
                    //
                    // measure unit leads
                    //
                    String measureUnit = line.substring(0, i).trim();
                    mmu = MemoryMeasureUnit.parse(measureUnit);
                }
                i = line.indexOf(":");
                List<Property> swap = parseLinuxSwapInfo(line.substring(i + 1), mmu);
                result.addAll(swap);
            }
        }
        return result;
    }

    public static List<Property> parseLinuxCpuInfo(String s) throws MetricException {

        Object[][] expected = new Object[][] {
                { "us", new CpuUserTime(null)},
                { "sy",  new CpuKernelTime(null)},
                { "ni",  new CpuNiceTime(null)},
                { "id", new CpuIdleTime(null)},
                { "wa", new CpuIoWaitTime(null)},
                { "hi", new CpuHardwareInterruptTime(null)},
                { "si", new CpuSoftwareInterruptTime(null)},
                { "st", new CpuStolenTime(null)},
        };

        return parseCpuInfo(s, expected);
    }

    /**
     * Parses memory info lines that usually start with "KiB Mem :" or "Mem: "
     *
     * "1015944 total,   802268 free,    86860 used,   126816 buff/cache"
     * "24607916k total,  9873232k used, 14734684k free,   788776k buffers"
     *
     * @param mmu - optional, null is acceptable, in which case the memory measure unit should be inferable from the
     *            string
     */
    public static List<Property> parseLinuxMemoryInfo(String s, MemoryMeasureUnit mmu)
            throws MetricException {

        List<Property> result = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(s, ",");

        while(st.hasMoreTokens()) {

            String tok = st.nextToken();
            int i;
            if ((i = tok.indexOf("total")) != -1) {
                PhysicalMemoryTotal m = new PhysicalMemoryTotal(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, defaultMeasureUnit));
            }
            else if ((i = tok.indexOf("free")) != -1) {
                PhysicalMemoryFree m = new PhysicalMemoryFree(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, m.getBaseUnit()));
            }
            else if ((i = tok.indexOf("used")) != -1) {
                PhysicalMemoryUsed m = new PhysicalMemoryUsed(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, m.getBaseUnit()));
            }

            //
            // we're ignoring buff/cache for the time being
            //

        }

        return result;
    }

    /**
     * Parses "10 total,        10 free,        10 used.   791404 avail Mem" (line usually starts with  KiB Swap :"
     *
     * @param mmu - optional, null is acceptable, in which case the memory measure unit should be inferable from the
     *            string
     */
    public static List<Property> parseLinuxSwapInfo(String s, MemoryMeasureUnit mmu) throws MetricException {

        List<Property> result = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(s, ",");

        while(st.hasMoreTokens()) {

            //  10 total,        10 free,        10 used.   791404 avail Mem

            String tok = st.nextToken();
            int i;
            if ((i = tok.indexOf("total")) != -1) {
                SwapTotal m = new SwapTotal(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, m.getBaseUnit()));
            }
            else if ((i = tok.indexOf("free")) != -1) {
                SwapFree m = new SwapFree(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, m.getBaseUnit()));
            }
            else if ((i = tok.indexOf("used")) != -1) {
                SwapUsed m = new SwapUsed(null);
                tok = tok.substring(0, i).trim();
                MeasureUnit defaultMeasureUnit = m.getBaseUnit();
                double conversionFactor;
                if (mmu != null) {
                    conversionFactor = defaultMeasureUnit.getConversionFactor(mmu);
                }
                else {
                    StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(tok);
                    tok = p.s;
                    conversionFactor = defaultMeasureUnit.getConversionFactor(p.memoryMeasureUnit);
                }
                result.add(PropertyFactory.createInstance(
                        m.getId(), m.getType(), tok, conversionFactor, m.getBaseUnit()));
            }

            //
            // we're ignoring avail Mem for the time being
            //
        }

        return result;
    }

    public static List<Property> parseMacCommandOutput(String output) throws MetricException {

        List<Property> result = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(output, "\n");
        while(st.hasMoreTokens()) {

            String line = st.nextToken();

            if (line.startsWith("Load Avg:")) {
                result.addAll(parseLoadAverage(line.substring("Load Avg:".length())));
            }
            else if (line.startsWith("CPU usage:")) {
                result.addAll(parseMacCpuInfo(line.substring("CPU usage:".length())));
            }
            else if (line.startsWith("PhysMem:")) {
                result.addAll(parseMacMemoryInfo(line.substring("PhysMem:".length())));
            }
        }
        return result;
    }

    /**
     * @param s expected format "2.94% user, 10.29% sys, 86.76% idle"
     */
    public static List<Property> parseMacCpuInfo(String s) throws MetricException {

        Object[][] expected = new Object[][] {
                { "user", new CpuUserTime(null)},
                { "sys",  new CpuKernelTime(null)},
                { "idle", new CpuIdleTime(null)},
        };
        return parseCpuInfo(s, expected);
    }

    /**
     * Parses "13G used (1470M wired), 2563M unused"
     */
    public static List<Property> parseMacMemoryInfo(String s) throws MetricException {

        List<Property> result = new ArrayList<>();
        Object[][] expected = new Object[][] {
                { "used", new PhysicalMemoryUsed(null)},
                { "unused",  new PhysicalMemoryFree(null)},
        };

        for(Object[] e: expected) {

            String label = (String)e[0];
            MetricDefinition m = (MetricDefinition)e[1];

            int i = s.indexOf(" " + label);
            if (i == -1) {
                continue;
            }
            String ms = s.substring(0, i);
            i = ms.lastIndexOf(' ');
            ms = i == -1 ? ms : ms.substring(i);
            StringMeasureUnitPair p = extractMemoryMeasureUnitHeuristics(ms);
            ms = p.s;
            MemoryMeasureUnit mmu = p.memoryMeasureUnit;
            MeasureUnit defaultMemoryUnit = m.getBaseUnit();
            double conversionFactor =  defaultMemoryUnit.getConversionFactor(mmu);
            result.add(PropertyFactory.createInstance(
                    m.getId(), m.getType(), ms, conversionFactor, defaultMemoryUnit));
        }
        return result;
    }

    // Package protected static ----------------------------------------------------------------------------------------

    /**
     * Attempts to figure out a memory measure unit in the given string.
     *
     * @return never returns null, and the wrapped string and the memory unit are never null.
     */
    static StringMeasureUnitPair extractMemoryMeasureUnitHeuristics(String s) {

        s = s.trim();

        //
        // skip all digits
        //
        int i;
        for(i = 0; i < s.length(); i ++) {

            char crt = s.charAt(i);

            if (crt < '0' || crt > '9') {

                // not a digit
                break;
            }
        }

        if (i == s.length()) {
            return new StringMeasureUnitPair(s, MemoryMeasureUnit.BYTE);
        }

        MemoryMeasureUnit mmu = MemoryMeasureUnit.parse(s.substring(i));
        return new StringMeasureUnitPair(s.substring(0, i), mmu);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public Top(String arguments) {
        super("top", arguments);
    }

    // OSCommand implementation ----------------------------------------------------------------------------------------

//    @Override
//    public List<Property> collectAllMetrics(OS os) throws MetricException {
//
//        String stdout = executeCommandAndReturnStdout(os);
//
//        if (OS.Linux.equals(os.getName())) {
//            return parseLinuxCommandOutput(stdout);
//        }
//        else if (OS.MacOS.equals(os.getName())) {
//            return parseMacCommandOutput(stdout);
//        }
//        else {
//
//            log.warn(os.toString() + " operating system not currently supported");
//            return Collections.emptyList();
//        }
//    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

    static class StringMeasureUnitPair {

        public String s;
        public MemoryMeasureUnit memoryMeasureUnit;

        public StringMeasureUnitPair(String s, MemoryMeasureUnit mmu) {
            this.s = s;
            this.memoryMeasureUnit = mmu;
        }
    }

}
