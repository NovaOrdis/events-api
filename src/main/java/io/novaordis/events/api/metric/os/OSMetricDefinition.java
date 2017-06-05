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

import io.novaordis.events.api.event.Property;
import io.novaordis.events.api.metric.MetricDefinition;
import io.novaordis.utilities.os.OS;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/5/17
 */
public interface OSMetricDefinition extends MetricDefinition {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @param os the OS to get the command for.
     *
     * @return the OS command to be executed in order to produce a value for this metric definition, for the specified
     * OS.
     */
    String getOSCommand(OS os);

    /**
     * @param os the OS the command was obtained for and executed on.
     * @param commandOutput the output of the command.
     *
     * Turns the output of the OS command specified by the getOSCommand() to a Property for this metric definition.
     */
    Property commandOutputToProperty(OS os, String commandOutput);

}
