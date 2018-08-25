package testcases;

import base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import utilities.TestUtil;

import java.util.Map;

public class AddCustomerTest extends BaseTest {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void addCustomerTest(Map<String, String> data) throws InterruptedException {

        if (!"Y".equalsIgnoreCase(data.get("RunMode"))) {
            throw new SkipException("Iteration is skipped.");
        }

        click("add-customer-button-css");
        type("first-name-input-css", data.get("firstName"));
        type("last-name-input-css", data.get("lastName"));
        type("post-code-input-css", data.get("postCode"));
        click("submit-customer-button-css");

        Alert userCreatedAlert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertTrue(userCreatedAlert.getText().contains(data.get("alertText")));
        userCreatedAlert.accept();
    }
}
