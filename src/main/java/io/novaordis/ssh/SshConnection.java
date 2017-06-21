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

package io.novaordis.ssh;

import io.novaordis.utilities.address.Address;
import io.novaordis.utilities.os.NativeExecutionException;
import io.novaordis.utilities.os.NativeExecutionResult;
import io.novaordis.utilities.os.NativeExecutor;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public interface SshConnection extends NativeExecutor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Same semantics as OS.execute(command), except that the execution takes place on the remote system.
     *
     * @see io.novaordis.utilities.os.OS#execute(String)
     */
    NativeExecutionResult execute(String command) throws NativeExecutionException;

    Address getAddress();

    // Lifecycle -------------------------------------------------------------------------------------------------------

    void connect();

    boolean isConnected();

    void disconnect();

}
