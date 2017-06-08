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

import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.NativeExecutor;

import java.io.File;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public class MockNativeExecutor implements NativeExecutor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private RuntimeException uncheckedCause;
    private NativeExecutionException checkedCause;
    private int failureExitCode;

    private String stdout;
    private String stderr;

    // Constructors ----------------------------------------------------------------------------------------------------

    // NativeExecutor implementation -----------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {

        return execute(null, command);
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        if (uncheckedCause != null) {

            throw uncheckedCause;
        }

        if (checkedCause != null) {

            throw checkedCause;
        }

        if (failureExitCode > 0) {

            return new NativeExecutionResult(
                    failureExitCode, "synthetic stdout content", "synthetic stderr content", true, true);
        }

        return new NativeExecutionResult(0, stdout, stderr, true, true);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void failWith(RuntimeException uncheckedCause) {

        this.uncheckedCause = uncheckedCause;
    }

    public void failWith(NativeExecutionException checkedCause) {

        this.checkedCause = checkedCause;
    }

    public void failWith(int failureExitCode) {

        if (failureExitCode <= 0) {

            throw new IllegalArgumentException("invalid failure exit code " + failureExitCode);
        }

        this.failureExitCode = failureExitCode;
    }

    public void setStdout(String s) {

        this.stdout = s;
    }

    public void setStderr(String s) {

        this.stderr = s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
