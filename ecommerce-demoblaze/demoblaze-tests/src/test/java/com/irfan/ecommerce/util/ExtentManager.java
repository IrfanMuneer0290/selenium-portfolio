package com.irfan.ecommerce.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;

/**
 * ExtentManager: Making sure we actually see what failed.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: When running tests fast in the pipeline, the reports kept
 * overwriting each other. I'd lose the failure proof from the previous run.
 * Also, parallel runs were making the report data look like a jumbled mess.
 * - WHAT I DID: I added timestamps so every run has its own file and made the
 * instance thread-safe so it doesn't get confused when 10 tests report at once.
 * - THE RESULT: We have a clean history of every single run. If something
 * breaks at 3 AM in the CI, the proof is right there waiting for me.
 */
public class ExtentManager  {

    private static volatile ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentManager.class) {
                if (extent == null) {
                    extent = createInstance();
                }
            }
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        // CI environments need explicit directory creation
        String reportDir = System.getProperty("report.dir", "target/reports");
        File directory = new File(reportDir);

        // FIXED: Create parents + directory (CI runners are strict)
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Verify writable (debug CI failures)
        if (!directory.canWrite()) {
            System.err.println("❌ Cannot write to reports dir: " + reportDir);
            return new ExtentReports(); // Fallback
        }

        String latestFile = new File(directory, "index.html").getAbsolutePath();
        String timestampedFile = new File(directory, "ExecutionReport_" +
                System.currentTimeMillis() + ".html").getAbsolutePath();

        ExtentSparkReporter latestReporter = new ExtentSparkReporter(latestFile);
        ExtentSparkReporter timestampedReporter = new ExtentSparkReporter(timestampedFile);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(latestReporter, timestampedReporter);

        // Theme for better visibility
        extent.setSystemInfo("CI Run ID", "${{ github.run_id }}");
        extent.setSystemInfo("Execution Env", System.getProperty("execution_env", "local"));

        return extent;
    }
}
