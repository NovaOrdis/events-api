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

package io.novaordis.events.api.metric.jmx;

import io.novaordis.events.api.metric.MetricSourceDefinition;
import io.novaordis.jmx.JmxAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/20/17
 */
public class JmxMetricSourceDefinitionUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    protected File scratchDirectory;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());
    }

    @After
    public void after() throws Exception {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));

    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // extractAdditionalConfigurationElements() ------------------------------------------------------------------------

    @Test
    public void extractAdditionalConfigurationElements_NullAddress() throws Exception {

        try {

            JmxMetricSourceDefinitionUtil.extractAdditionalConfigurationElements(null, new HashMap<>());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null address"));
        }
    }

    @Test
    public void extractAdditionalConfigurationElements_NoClasspath() throws Exception {

        JmxAddress a = new JmxAddress("jmx://localhost:1000");

        Map m = new HashMap<>();

        JmxMetricSourceDefinitionUtil.extractAdditionalConfigurationElements(a, m);

        //
        // will warn
        //
    }

    @Test
    public void extractAdditionalConfigurationElements() throws Exception {

        JmxAddress a = new JmxAddress("jmx://localhost:1000");

        Map m = new HashMap<>();

        //noinspection unchecked
        m.put(MetricSourceDefinition.CLASSPATH_YAML_KEY, Collections.singletonList("/some/class/path/element.jar"));

        JmxMetricSourceDefinitionUtil.extractAdditionalConfigurationElements(a, m);

    }

    // processClasspathElementHeuristics() -----------------------------------------------------------------------------

    @Test
    public void processClasspathElementHeuristics_Null() throws Exception {

        JmxAddress address = new JmxAddress("jmx://localhost:1000");

        // nothing happens, no exception
        JmxMetricSourceDefinitionUtil.processClasspathElementHeuristics(address, null);
    }

    @Test
    public void processClasspathElementHeuristics_JBossEAP6_DirectoryDoesNotExist() throws Exception {

        JmxAddress address = new JmxAddress("jmx://localhost:1000");

        String classpathElement = "/some/nonexistent/directory/jboss-cli-client.jar";

        // nothing happens, no exception, will warn
        JmxMetricSourceDefinitionUtil.processClasspathElementHeuristics(address, classpathElement);
    }

    @Test
    public void processClasspathElementHeuristics_JBossEAP6() throws Exception {

        JmxAddress address = new JmxAddress("jmx://localhost:1000");

        assertNull(address.getJmxServiceUrlProtocol());

        //
        // create a valid JBoss home simulation with a version file
        //

        File f = new File(System.getProperty("basedir"),
                "src/test/resources/data/miscellaneous/simulation-of-eap-6-jboss-cli-client.jar");

        assertTrue(f.isFile());

        String classpathElement = f.getPath();

        JmxMetricSourceDefinitionUtil.processClasspathElementHeuristics(address, classpathElement);

        String expected = JmxAddress.EAP6_JMX_SERVICE_URL_PROTOCOL;
        assertEquals(expected, address.getJmxServiceUrlProtocol());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
