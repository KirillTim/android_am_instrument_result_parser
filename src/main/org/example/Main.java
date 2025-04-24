package org.example;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IShellEnabledDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.testrunner.InstrumentationResultParser;
import com.android.ddmlib.testrunner.RemoteAndroidTestRunner;
import com.android.ddmlib.testrunner.XmlTestRunListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jspecify.annotations.NonNull;

public class Main {

  private static class FakeShellRunnerDevice implements IShellEnabledDevice {

    private final List<String> adbCmd;

    private FakeShellRunnerDevice(List<String> adbCmd) {
      this.adbCmd = adbCmd;
    }

    @Override
    public String getName() {
      return "FakeShellRunnerDevice";
    }

    @Override
    public void executeShellCommand(String command, IShellOutputReceiver receiver,
        long maxTimeToOutputResponse, TimeUnit maxTimeUnits) {
      executeShellCommand(command, receiver);
      System.out.println("executeShellCommand, command: " + command + ", receiver: " + receiver
          + ", maxTimeToOutputResponse: " + maxTimeToOutputResponse);
    }

    @Override
    public void executeShellCommand(String command, IShellOutputReceiver receiver, long maxTimeout,
        long maxTimeToOutputResponse, TimeUnit maxTimeUnits) {
      executeShellCommand(command, receiver);
    }

    private void executeShellCommand(String command, IShellOutputReceiver receiver) {
      System.out.println("executeShellCommand, command: " + command + ", receiver: " + receiver);
      List<String> cmdList = Arrays.stream(command.split(" ")).filter(s -> !s.isBlank()).toList();
      ArrayList<String> combinedCmdList = new ArrayList<>();
      combinedCmdList.addAll(adbCmd);
      combinedCmdList.addAll(cmdList);
      System.out.println("Final cmd: '" + String.join(" ", combinedCmdList) + "'");
      try {
        byte[] output = executeShellCommandAndReadOutput(combinedCmdList);
        String outputString = new String(output, StandardCharsets.UTF_8);
        System.out.println("output:\n" + outputString + "\n==========");
        receiver.addOutput(output, 0, outputString.length());
        receiver.flush();
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static byte[] executeShellCommandAndReadOutput(List<String> command)
      throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectErrorStream(true);
    Process p = pb.start();
    p.waitFor();
    return p.getInputStream().readAllBytes();
  }

  final static String BENCHMARK_OUTPUT_WITH_ERROR =
      "INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark\n" +
      "INSTRUMENTATION_STATUS: current=1\n" +
      "INSTRUMENTATION_STATUS: id=AndroidJUnitRunner\n" +
      "INSTRUMENTATION_STATUS: numtests=1\n" +
      "INSTRUMENTATION_STATUS: stream=\n" +
      "com.simple.SimpleBenchmark:\n" +
      "INSTRUMENTATION_STATUS: test=runBenchmark\n" +
      "INSTRUMENTATION_STATUS_CODE: 1\n" +
      "INSTRUMENTATION_STATUS: additionalTestOutputFile_SimpleBenchmark_runBenchmark_2025-04-24-13-40-14.perfetto-trace=/storage/emulated/0/Android/media/com.simple/SimpleBenchmark_runBenchmark_2025-04-24-13-40-14.perfetto-trace\n"
      +
      "INSTRUMENTATION_STATUS_CODE: 2\n" +
      "INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark\n" +
      "INSTRUMENTATION_STATUS: current=1\n" +
      "INSTRUMENTATION_STATUS: id=AndroidJUnitRunner\n" +
      "INSTRUMENTATION_STATUS: numtests=1\n" +
      "INSTRUMENTATION_STATUS: stack=java.lang.AssertionError: ERRORS (not suppressed): EMULATOR\n"
      +
      "(Suppressed errors: ACTIVITY-MISSING LOW-BATTERY UNLOCKED)\n" +
      "\n" +
      "WARNING: Running on Emulator\n" +
      "    Benchmark is running on an emulator, which is not representative of\n" +
      "    real user devices. Use a physical device to benchmark. Emulator\n" +
      "    benchmark improvements might not carry over to a real user's\n" +
      "    experience (or even regress real device performance). \n" +
      "\n" +
      "While you can suppress these errors (turning them into warnings)\n" +
      "PLEASE NOTE THAT EACH SUPPRESSED ERROR COMPROMISES ACCURACY\n" +
      "\n" +
      "// Sample suppression, in a benchmark module's build.gradle:\n" +
      "android {\n" +
      "    defaultConfig {\n" +
      "        // Enable measuring on an emulator, or devices with low battery\n" +
      "        testInstrumentationRunnerArguments[\"androidx.benchmark.suppressErrors\"] = \"EMULATOR,LOW-BATTERY\"\n"
      +
      "    }\n" +
      "}\n" +
      "	at androidx.benchmark.Errors.throwIfError(Errors.kt:283)\n" +
      "	at androidx.benchmark.BenchmarkState.beforeBenchmark(BenchmarkState.kt:471)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunningInternal(BenchmarkState.kt:459)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunning(BenchmarkState.kt:405)\n" +
      "	at com.simple.SimpleBenchmark.runBenchmark(SimpleBenchmark.java:19)\n" +
      "\n" +
      "INSTRUMENTATION_STATUS: stream=\n" +
      "Error in runBenchmark(com.simple.SimpleBenchmark):\n" +
      "java.lang.AssertionError: ERRORS (not suppressed): EMULATOR\n" +
      "(Suppressed errors: ACTIVITY-MISSING LOW-BATTERY UNLOCKED)\n" +
      "\n" +
      "WARNING: Running on Emulator\n" +
      "    Benchmark is running on an emulator, which is not representative of\n" +
      "    real user devices. Use a physical device to benchmark. Emulator\n" +
      "    benchmark improvements might not carry over to a real user's\n" +
      "    experience (or even regress real device performance). \n" +
      "\n" +
      "While you can suppress these errors (turning them into warnings)\n" +
      "PLEASE NOTE THAT EACH SUPPRESSED ERROR COMPROMISES ACCURACY\n" +
      "\n" +
      "// Sample suppression, in a benchmark module's build.gradle:\n" +
      "android {\n" +
      "    defaultConfig {\n" +
      "        // Enable measuring on an emulator, or devices with low battery\n" +
      "        testInstrumentationRunnerArguments[\"androidx.benchmark.suppressErrors\"] = \"EMULATOR,LOW-BATTERY\"\n"
      +
      "    }\n" +
      "}\n" +
      "	at androidx.benchmark.Errors.throwIfError(Errors.kt:283)\n" +
      "	at androidx.benchmark.BenchmarkState.beforeBenchmark(BenchmarkState.kt:471)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunningInternal(BenchmarkState.kt:459)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunning(BenchmarkState.kt:405)\n" +
      "	at com.simple.SimpleBenchmark.runBenchmark(SimpleBenchmark.java:19)\n" +
      "\n" +
      "INSTRUMENTATION_STATUS: test=runBenchmark\n" +
      "INSTRUMENTATION_STATUS_CODE: -2\n" +
      "INSTRUMENTATION_RESULT: stream=\n" +
      "\n" +
      "Time: 4.898\n" +
      "There was 1 failure:\n" +
      "1) runBenchmark(com.simple.SimpleBenchmark)\n" +
      "java.lang.AssertionError: ERRORS (not suppressed): EMULATOR\n" +
      "(Suppressed errors: ACTIVITY-MISSING LOW-BATTERY UNLOCKED)\n" +
      "\n" +
      "WARNING: Running on Emulator\n" +
      "    Benchmark is running on an emulator, which is not representative of\n" +
      "    real user devices. Use a physical device to benchmark. Emulator\n" +
      "    benchmark improvements might not carry over to a real user's\n" +
      "    experience (or even regress real device performance). \n" +
      "\n" +
      "While you can suppress these errors (turning them into warnings)\n" +
      "PLEASE NOTE THAT EACH SUPPRESSED ERROR COMPROMISES ACCURACY\n" +
      "\n" +
      "// Sample suppression, in a benchmark module's build.gradle:\n" +
      "android {\n" +
      "    defaultConfig {\n" +
      "        // Enable measuring on an emulator, or devices with low battery\n" +
      "        testInstrumentationRunnerArguments[\"androidx.benchmark.suppressErrors\"] = \"EMULATOR,LOW-BATTERY\"\n"
      +
      "    }\n" +
      "}\n" +
      "	at androidx.benchmark.Errors.throwIfError(Errors.kt:283)\n" +
      "	at androidx.benchmark.BenchmarkState.beforeBenchmark(BenchmarkState.kt:471)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunningInternal(BenchmarkState.kt:459)\n" +
      "	at androidx.benchmark.BenchmarkState.keepRunning(BenchmarkState.kt:405)\n" +
      "	at com.simple.SimpleBenchmark.runBenchmark(SimpleBenchmark.java:19)\n" +
      "\n" +
      "FAILURES!!!\n" +
      "Tests run: 1,  Failures: 1\n" +
      "\n" +
      "\n" +
      "INSTRUMENTATION_CODE: -1\n";
  final static String BENCHMARK_OUTPUT_WITH_WARNING =
      "INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark\n" +
      "INSTRUMENTATION_STATUS: current=1\n" +
      "INSTRUMENTATION_STATUS: id=AndroidJUnitRunner\n" +
      "INSTRUMENTATION_STATUS: numtests=1\n" +
      "INSTRUMENTATION_STATUS: stream=\n" +
      "com.simple.SimpleBenchmark:\n" +
      "INSTRUMENTATION_STATUS: test=runBenchmark\n" +
      "INSTRUMENTATION_STATUS_CODE: 1\n" +
      "INSTRUMENTATION_STATUS: additionalTestOutputFile_profiling_trace=/storage/emulated/0/Android/media/com.simple/SimpleBenchmark_runBenchmark-methodTracing-2025-04-24-12-43-51.trace\n"
      +
      "INSTRUMENTATION_STATUS_CODE: 2\n" +
      "INSTRUMENTATION_STATUS: additionalTestOutputFile_SimpleBenchmark_runBenchmark_2025-04-24-12-43-52.perfetto-trace=/storage/emulated/0/Android/media/com.simple/SimpleBenchmark_runBenchmark_2025-04-24-12-43-52.perfetto-trace\n"
      +
      "INSTRUMENTATION_STATUS_CODE: 2\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_allocation_count_median=0.0\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_allocation_count_min=0.0\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_allocation_count_stddev=0.0\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_time_nanos_median=5721.055404130412\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_time_nanos_min=5412.270992766537\n" +
      "INSTRUMENTATION_STATUS: EMULATOR_time_nanos_stddev=647.6251180138073\n" +
      "INSTRUMENTATION_STATUS: android.studio.display.benchmark=\n" +
      "WARNING: Running on Emulator\n" +
      "    Benchmark is running on an emulator, which is not representative of\n" +
      "    real user devices. Use a physical device to benchmark. Emulator\n" +
      "    benchmark improvements might not carry over to a real user's\n" +
      "    experience (or even regress real device performance). \n" +
      "\n" +
      "        5,412   ns           0 allocs    EMULATOR_SimpleBenchmark.runBenchmark\n" +
      "INSTRUMENTATION_STATUS: android.studio.v2display.benchmark=\n" +
      "WARNING: Running on Emulator\n" +
      "    Benchmark is running on an emulator, which is not representative of\n" +
      "    real user devices. Use a physical device to benchmark. Emulator\n" +
      "    benchmark improvements might not carry over to a real user's\n" +
      "    experience (or even regress real device performance). \n" +
      "\n" +
      "        5,412   ns           0 allocs    [Trace](file://SimpleBenchmark_runBenchmark_2025-04-24-12-43-52.perfetto-trace)    [Method Trace](file://SimpleBenchmark_runBenchmark-methodTracing-2025-04-24-12-43-51.trace)    EMULATOR_SimpleBenchmark.runBenchmark\n"
      +
      "INSTRUMENTATION_STATUS: android.studio.v2display.benchmark.outputDirPath=/storage/emulated/0/Android/media/com.simple\n"
      +
      "INSTRUMENTATION_STATUS_CODE: 2\n" +
      "INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark\n" +
      "INSTRUMENTATION_STATUS: current=1\n" +
      "INSTRUMENTATION_STATUS: id=AndroidJUnitRunner\n" +
      "INSTRUMENTATION_STATUS: numtests=1\n" +
      "INSTRUMENTATION_STATUS: stream=.\n" +
      "INSTRUMENTATION_STATUS: test=runBenchmark\n" +
      "INSTRUMENTATION_STATUS_CODE: 0\n" +
      "INSTRUMENTATION_RESULT: stream=\n" +
      "\n" +
      "Time: 11.487\n" +
      "\n" +
      "OK (1 test)\n" +
      "\n" +
      "\n" +
      "INSTRUMENTATION_CODE: -1\n";

  public static void main(String[] args)
      throws ShellCommandUnresponsiveException, AdbCommandRejectedException, IOException, TimeoutException, InterruptedException {

    File tmpDir1 = createTmpDir();
    System.out.println("tempDir1: " + tmpDir1);

    XmlTestRunListener xmlTestRunListener = new XmlTestRunListener();
    xmlTestRunListener.setReportDir(tmpDir1);
    InstrumentationResultParser parser = new InstrumentationResultParser("MyFakeRun", xmlTestRunListener);
    byte[] benchmarkOutputWithWarningBytes = BENCHMARK_OUTPUT_WITH_ERROR.getBytes(StandardCharsets.UTF_8); //BENCHMARK_OUTPUT_WITH_WARNING.getBytes(StandardCharsets.UTF_8);
    parser.addOutput(benchmarkOutputWithWarningBytes, 0, benchmarkOutputWithWarningBytes.length);
    parser.flush();

    xmlTestRunListener.getRunResult().getTestResults().forEach((test, result) -> {
      System.out.println("test: " + test);
      System.out.println("result: " + result);
    });

    System.exit(0);

    String adbPath = "/usr/local/google/home/ktimofeev/Android/Sdk/platform-tools/adb";

    String output = new String(executeShellCommandAndReadOutput(List.of(adbPath, "devices")),
        StandardCharsets.UTF_8);
    System.out.println("adb devices output:\n" + output + "\n==========");

    List<String> cmd = List.of(adbPath, "-s", "emulator-5554", "shell");

    FakeShellRunnerDevice remoteDevice = new FakeShellRunnerDevice(cmd);
    RemoteAndroidTestRunner testRunner = new RemoteAndroidTestRunner("dev.perfetto.sdk.test",
        "androidx.test.runner.AndroidJUnitRunner",
        remoteDevice);
    XmlTestRunListener listener = new XmlTestRunListener();
    File tmpDir = createTmpDir();
    listener.setReportDir(tmpDir);
    System.out.println("tempDir: " + tmpDir);
    testRunner.run(listener);
    listener.getRunResult().getCompletedTests().forEach(System.out::println);
  }

  private static File createTmpDir() throws IOException {
    // create a temp file with unique name, then make it a directory
    File tmpDir = File.createTempFile("foo", "dir");
    tmpDir.delete();
    if (!tmpDir.mkdirs()) {
      throw new IOException("unable to create directory");
    }
    return tmpDir;
  }

  public static int constant4() {
    return 4;
  }

  public static String someNonNullFunc(@NonNull String arg) {
    return "foo" + arg;
  }

}