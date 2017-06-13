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

import io.novaordis.utilities.address.LocalOSAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class LocalOSTest extends OSSourceBaseTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(LocalOSTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void equalsTest() throws Exception {

        LocalOS los = new LocalOS();
        LocalOS los2 = new LocalOS();

        assertEquals(los, los2);
        assertEquals(los2, los);
    }

    @Test
    @Override
    public void hashCodeTest() throws Exception {

        LocalOS los = new LocalOS();
        assertEquals(1, los.hashCode());
    }

    @Test
    @Override
    public void collectMetrics_DefinitionsHaveDifferentSources() throws Exception {

        //
        // this test does not make sense of LocalOS, all LocalOS instances are equivalent
        //

        log.info("noop override");
    }

    // getAddress() ----------------------------------------------------------------------------------------------------

    @Test
    public void getAddress() throws Exception {

        LocalOS os = new LocalOS();

        LocalOSAddress losa = (LocalOSAddress)os.getAddress();

        assertNotNull(losa);
    }

    // lifecycle -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void start() throws Exception {

        //
        // start operation is a noop for a LocalOS, a LocalOS is always "started"
        //

        LocalOS os = getMetricSourceToTest();

        assertTrue(os.isStarted());

        // noop
        os.start();

        assertTrue(os.isStarted());

        // noop
        os.start();

        assertTrue(os.isStarted());
    }

    @Test
    @Override
    public void stop() throws Exception {

        LocalOS os = getMetricSourceToTest();

        assertTrue(os.isStarted());

        // noop
        os.stop();

        assertTrue(os.isStarted());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected LocalOS getMetricSourceToTest(String... address) throws Exception {

        if (address.length > 1) {
            // at most one argument is expected
            throw new IllegalArgumentException(address.length + " arguments");
        }

        return new LocalOS();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
