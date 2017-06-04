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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricCollectionException;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.events.api.metric.MetricSource;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.OS;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * The implementations must correctly implement equals() and hashCode()
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/5/16
 */
public abstract class OSCommand implements MetricSource {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // the command to execute (without space-separated options) to get the metrics
    private String command;

    // the arguments as a space-separated String
    private String arguments;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param command the command to execute (without space-separated options) to get the metrics. Must not be null.
     * @param  arguments the arguments as a space-separated String. Can be null and in this case is interpreted as
     *                   "no arguments"
     */
    public OSCommand(String command, String arguments) {

        if (command == null) {
            throw new IllegalArgumentException("null command");
        }

        this.command = command;
        this.arguments = arguments;
    }

    // MetricSource implementation -------------------------------------------------------------------------------------

    /**
     * noop implementation, override in subclasses if you need a more specific behavior.
     */
    @Override
    public List<Property> collectMetrics(List<MetricDefinition> metricDefinitions) throws MetricCollectionException {

        List<Property> result = new ArrayList<>();

        for(int i = 0; i < metricDefinitions.size(); i ++) {
            result.add(null);
        }

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the command to execute (without space-separated options) to get the metrics. Never null.
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return the arguments as a space-separated String. May be null.
     */
    public String getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!o.getClass().equals(getClass())) {

            return false;
        }

        OSCommand that = (OSCommand)o;

        if (!command.equals(that.command)) {
            return false;
        }

        String normalizedArg = arguments == null ? "" : arguments;
        String thatNormalizedArg = that.arguments == null ? "" : that.arguments;

        normalizedArg = normalizedArg.trim();
        thatNormalizedArg = thatNormalizedArg.trim();

        if (normalizedArg.length() == 0 && thatNormalizedArg.length() == 0) {
            return true;
        }

        String[] na = normalizedArg.split(" +");
        String[] tna = thatNormalizedArg.split(" +");

        if (na.length != tna.length) {
            return false;
        }

        for(int i = 0; i < na.length; i ++) {
            if (!na[i].equals(tna[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {

        String normalizedArg = arguments == null ? "" : arguments;
        normalizedArg = normalizedArg.trim();
        String[] na = normalizedArg.split(" +");

        int hashCode = 7 + 17 * command.hashCode();
        for(String s: na) {
            hashCode += 19 * s.hashCode();
        }
        return  hashCode;
    }

    @Override
    public String toString() {

        return getCommand() + (arguments == null || arguments.length() == 0 ? "" : " " + arguments);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected String executeCommandAndReturnStdout(OS os) throws MetricCollectionException {

        String commandAndArguments = command + " " + arguments;

        NativeExecutionResult r;

        try {

            r = os.execute(commandAndArguments);
        }
        catch(NativeExecutionException e) {
            throw new MetricCollectionException(e);
        }

        if (r.isSuccess()) {
            return r.getStdout();
        }

        String msg = command + " execution failed";
        String stderr = r.getStderr();
        msg = stderr == null || stderr.isEmpty() ? msg : msg + ": " + stderr;
        throw new MetricCollectionException(msg);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
