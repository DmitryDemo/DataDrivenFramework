package base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utilities.ExcelReader;
import utilities.ExtentManager;
import utilities.TestUtil;
import utilities.enums.Browser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected static String projectDir = System.getProperty("user.dir");
    public static WebDriver driver;
    public static Properties config = new Properties();
    public static Properties locators = new Properties();
    public static InputStream is;
    public static Logger log = Logger.getLogger("devpinoyLogger");
    public static ExcelReader excel = new ExcelReader(projectDir + "\\src\\test\\resources\\excel\\TestData.xlsx");
    public static WebDriverWait wait;
    public static ExtentReports extentReport = ExtentManager.getInstance();
    public static ExtentTest extentTest;

    @BeforeSuite
    public void setUp() throws IOException {

        if (driver == null) {
            is = new FileInputStream(projectDir + "\\src\\test\\resources\\properties\\config.properties");
            config.load(is);
            log.debug("config.properties is loaded");
            is = new FileInputStream(projectDir + "\\src\\test\\resources\\properties\\locators.properties");
            locators.load(is);
            log.debug("locators.properties is loaded");
            String br = System.getenv("browser");

            if (StringUtils.isNotEmpty(br)) {
                config.setProperty("browser", br);
            }

            switch (Browser.getByName(config.getProperty("browser"))) {
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", projectDir + "\\src\\test\\resources\\executables\\chromedriver.exe");
                    driver = new ChromeDriver();
                    log.debug("Chrome is started.");
                    break;
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver", projectDir + "\\src\\test\\resources\\executables\\geckodriver.exe");
                    driver = new FirefoxDriver();
                    log.debug("Firefox is started.");
                    break;
                case IE:
                    System.setProperty("webdriver.ie.driver", projectDir + "\\src\\test\\resources\\executables\\IEDriverServer.exe");
                    driver = new InternetExplorerDriver();
                    log.debug("Internet explorer is started.");
                    break;
                default:
                    throw new IllegalArgumentException("No browser matches found for " + config.getProperty("browser"));
            }

            driver.get(config.getProperty("testsiteurl"));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Long.valueOf(config.getProperty("implicit.wait")), TimeUnit.SECONDS);
            wait = new WebDriverWait(driver,5 );
        }
    }

    protected boolean isElementPresent(final By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        log.debug("Test execution finished");
    }

    protected void click(final String locatorName) {
        get(locatorName).click();
        extentTest.log(LogStatus.INFO, String.format("Clicking the %s element", locatorName));
    }

    protected void type(final String locatorName, final String text) {
        get(locatorName).sendKeys(text);
        extentTest.log(LogStatus.INFO, String.format("Typing text \"%s\" into the %s field", text, locatorName));
    }

    protected void select(final String locatorName, final String dropdownOption) {
        new Select(get(locatorName)).selectByVisibleText(dropdownOption);
        extentTest.log(LogStatus.INFO, String.format("Selecting from dropdown %s option %s", locatorName, dropdownOption));
    }

    protected static void verifyEquals(final String expected, final String actual) throws IOException {

        try {
            Assert.assertEquals(expected, actual);
        } catch (Throwable t) {
            TestUtil.captureScreenshot();

            // This is for ReportNG
            Reporter.log("<br>Verification failure: " + t.getMessage() + "<br");
            Reporter.log(String.format("<a href=%s target=\"_blank\"><img src =%s width = \"196\" height = \"262\"></img></a><br>",
                    TestUtil.screenshotName, TestUtil.screenshotName));
            // This is for ExtentReport
            getExtentTest().log(LogStatus.FAIL, "Verification failed. Message: " + t.getMessage());
            getExtentTest().log(LogStatus.FAIL, getExtentTest().addScreenCapture(TestUtil.screenshotName));
        }
    }

    public static ExtentReports getExtentReport() {
        return extentReport;
    }

    public static ExtentTest getExtentTest() {
        return extentTest;
    }

    public static void setExtentTest(ExtentTest extentTest) {
        BaseTest.extentTest = extentTest;
    }

    private WebElement get(final String locatorName) {
        WebElement result = null;
        if (locatorName.endsWith("css")) {
            result = driver.findElement(By.cssSelector(locators.getProperty(locatorName)));
        } else if (locatorName.endsWith("xpath")) {
            result = driver.findElement(By.xpath(locators.getProperty(locatorName)));
        } else {
            throw new IllegalArgumentException("Unknown locator type. Please, add locator type to it's name.");
        }
        return result;
    }
}
