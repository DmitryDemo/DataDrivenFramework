package listeners;

import base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;
import utilities.MonitoringMail;
import utilities.TestConfig;
import utilities.TestUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static base.BaseTest.*;

public class CustomListeners implements ITestListener, ISuiteListener {
    @Override
    public void onTestStart(ITestResult testInfo) {
        setExtentTest(getExtentReport().startTest(testInfo.getName()));

        if (!TestUtil.isTestRunable(testInfo.getName(), excel)) {
            throw new SkipException(String.format("Test %s is not included into execution list.", testInfo.getName()));
        }
    }

    @Override
    public void onTestSuccess(ITestResult testInfo) {
        getExtentTest().log(LogStatus.PASS, testInfo.getName() + " Test Passed");
        getExtentReport().endTest(getExtentTest());
        getExtentReport().flush();
    }

    @Override
    public void onTestFailure(ITestResult testInfo) {

        System.setProperty("org.uncommons.reportng.escape-output", "false");
        try {
            TestUtil.captureScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getExtentTest().log(LogStatus.FAIL, testInfo.getName() + " Test Failed. <br>  Exception: " + testInfo.getThrowable().getLocalizedMessage());
        getExtentTest().log(LogStatus.FAIL, getExtentTest().addScreenCapture(TestUtil.screenshotName));
        getExtentReport().endTest(getExtentTest());
        getExtentReport().flush();
    }

    @Override
    public void onTestSkipped(ITestResult test) {
        extentTest.log(LogStatus.SKIP, String.format(
                "Skipped test %s as it is not included into the list of tests to run.", test.getName()));
        getExtentReport().endTest(getExtentTest());
        getExtentReport().flush();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        String link = null;
        try {
            link = "http://" + InetAddress.getLocalHost().getHostAddress()
                    + ":8080/job/DataDrivenLiveProject/EXTENT_20Report/";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            new MonitoringMail().sendMail(TestConfig.server,
                    TestConfig.from,
                    TestConfig.to,
                    "Test Report",
                    "THe link is " + link);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
