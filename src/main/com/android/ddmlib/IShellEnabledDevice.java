/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmlib;

// import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * An abstract device that can receive shell commands.
 */
public interface IShellEnabledDevice {

    /**
     * Returns a (humanized) name for this device. Typically this is the AVD name for AVD's, and
     * a combination of the manufacturer name, model name &amp; serial number for devices.
     */
    String getName();

    /**
     * Executes a shell command on the device, and sends the result to a <var>receiver</var>.
     * <p><var>maxTimeToOutputResponse</var> is used as a maximum waiting time when expecting the
     * command output from the device.<br>
     * At any time, if the shell command does not output anything for a period longer than
     * <var>maxTimeToOutputResponse</var>, then the method will throw
     * {@link ShellCommandUnresponsiveException}.
     * <p>For commands like log output, a <var>maxTimeToOutputResponse</var> value of 0, meaning
     * that the method will never throw and will block until the receiver's
     * {@link IShellOutputReceiver#isCancelled()} returns <code>true</code>, should be
     * used.
     *
     * @param command the shell command to execute
     * @param receiver the {@link IShellOutputReceiver} that will receives the output of the shell
     *            command
     * @param maxTimeToOutputResponse the maximum amount of time during which the command is allowed
     *            to not output any response. A value of 0 means the method will wait forever
     *            (until the <var>receiver</var> cancels the execution) for command output and
     *            never throw.
     * @param maxTimeUnits Units for non-zero {@code maxTimeToOutputResponse} values.
     * @throws TimeoutException in case of timeout on the connection when sending the command.
     * @throws AdbCommandRejectedException if adb rejects the command.
     * @throws ShellCommandUnresponsiveException in case the shell command doesn't send any output
     *            for a period longer than <var>maxTimeToOutputResponse</var>.
     * @throws IOException in case of I/O error on the connection.
     *
     * @see DdmPreferences#getTimeOut()
     */
    void executeShellCommand(String command, IShellOutputReceiver receiver,
            long maxTimeToOutputResponse, TimeUnit maxTimeUnits)
            throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException,
            IOException;

    /**
     * Executes a shell command on the device, and sends the result to a <var>receiver</var>.
     *
     * <p><var>maxTimeToOutputResponse</var> is used as a maximum waiting time when expecting the
     * command output from the device.<br>
     * At any time, if the shell command does not output anything for a period longer than
     * <var>maxTimeToOutputResponse</var>, then the method will throw {@link
     * ShellCommandUnresponsiveException}.
     *
     * <p>For commands like log output, a <var>maxTimeToOutputResponse</var> value of 0, meaning
     * that the method will never throw and will block until the receiver's {@link
     * IShellOutputReceiver#isCancelled()} returns <code>true</code>, should be used.
     *
     * @param command the shell command to execute
     * @param receiver the {@link IShellOutputReceiver} that will receives the output of the shell
     *     command
     * @param maxTimeout the maximum timeout for the command to return. A value of 0 means no max
     *     timeout will be applied.
     * @param maxTimeToOutputResponse the maximum amount of time during which the command is allowed
     *     to not output any response. A value of 0 means the method will wait forever (until the
     *     <var>receiver</var> cancels the execution) for command output and never throw.
     * @param maxTimeUnits Units for non-zero {@code maxTimeout} and {@code maxTimeToOutputResponse}
     *     values.
     * @throws TimeoutException in case of timeout on the connection when sending the command.
     * @throws AdbCommandRejectedException if adb rejects the command.
     * @throws ShellCommandUnresponsiveException in case the shell command doesn't send any output
     *     for a period longer than <var>maxTimeToOutputResponse</var>.
     * @throws IOException in case of I/O error on the connection.
     * @see DdmPreferences#getTimeOut()
     */
    void executeShellCommand(
            String command,
            IShellOutputReceiver receiver,
            long maxTimeout,
            long maxTimeToOutputResponse,
            TimeUnit maxTimeUnits)
            throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException,
                    IOException;

    /**
     * Do a potential asynchronous query for a system property.
     *
     * @param name the name of the value to return.
     * @return a {@link ListenableFuture ListenableFuture.} {@link Future#get() get} may return
     *     null.
     */
    // @NonNull
    // ListenableFuture<String> getSystemProperty(@NonNull String name);
}
