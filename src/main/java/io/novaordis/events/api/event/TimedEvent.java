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

import io.novaordis.utilities.time.Timestamp;

/**
 * https://kb.novaordis.com/index.php/Events-api_Concepts#Timed_Event
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/24/16
 */
public interface TimedEvent extends Event {

    // Constants -------------------------------------------------------------------------------------------------------

    String TIME_PROPERTY_NAME = "time";

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the universal time (UTC) - timestamp in milliseconds from 01/01/70 00:00:00 UTC, not accounting for
     * timezone and daylight saving offsets. May return null.
     */
    Long getTime();

    Timestamp getTimestamp();

    void setTimestamp(Timestamp timestamp);

}
