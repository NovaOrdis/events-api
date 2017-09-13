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

package io.novaordis.events.api.metric.os.mdefs;

import io.novaordis.events.api.event.PropertyFactory;
import io.novaordis.utilities.address.OSAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deprecated alias for CpuSystemTime, which is the recommended nomenclature.
 *
 * Left in place so the configuration files won't break.
 *
 * Introduced in 1.0.7. Should be removed after the 2.0 release.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
@Deprecated
public class CpuKernelTime extends CpuSystemTime {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(CpuKernelTime.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public CpuKernelTime(PropertyFactory propertyFactory, OSAddress osAddress) {

        super(propertyFactory, osAddress);

        log.warn("'CpuKernelTime' metric definition is deprecated, '" + CpuSystemTime.class.getSimpleName() + "' should be used instead");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
