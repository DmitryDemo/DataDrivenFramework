package testcases;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.TestUtil;

import java.util.Map;

public class OpenAccountTest extends BaseTest {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void openAccountTest(Map<String, String> data) {
        click("open-account-button-css");
        select("customer-dropdown-css", data.get("Customer"));
        select("currency-dropdown-css", data.get("Currency"));
        click("process-button-css");

        Alert accountOpenedAlert = wait.until(ExpectedConditions.alertIsPresent());
            Assert.assertTrue(accountOpenedAlert.getText().contains(data.get("AlertText")));
        accountOpenedAlert.accept();
    }
}
