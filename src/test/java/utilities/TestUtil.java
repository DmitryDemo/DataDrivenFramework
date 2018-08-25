package utilities;

import base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestUtil extends BaseTest {

    public static String screenshotPath;
    public static String screenshotName;

    public static void captureScreenshot() throws IOException {

        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        screenshotName = new Date().toString().replace(":", "_").replace(" ", "_") + ".jpg";
        FileUtils.copyFile(scrFile, new File(projectDir + "\\target\\surefire-reports\\html\\"
                + screenshotName));
    }



    @DataProvider(name="dp")
    public Object[][] getData(Method m) {

        String sheetName = m.getName();
        int rows = excel.getRowCount(sheetName);
        int cols = excel.getColumnCount(sheetName);
        Object[][] data = new Object[rows-1][1];
        Map<String, String> map = null;

        for (int rowNum = 2; rowNum <= rows; rowNum++) {
            map = new HashMap<String, String>();
            for (int colNum = 0; colNum < cols; colNum++) {
                map.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
                data[rowNum-2][0] = map;
            }
        }
        return data;
    }

    public static boolean isTestRunable(final String testName, ExcelReader reader) {
        final String sheetName = "TestSuite";
        int rows = excel.getRowCount(sheetName);

        for (int rowNum = 2; rowNum <= rows; rowNum++) {
            if (testName.equalsIgnoreCase(excel.getCellData(sheetName, "TestCaseID", rowNum))) {
                return  "Y".equalsIgnoreCase(excel.getCellData(sheetName, "RunMode", rowNum));
            }
        }
        return false;
    }
}
