package testcases;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class BankManagerLoginTest extends BaseTest {

    @Test
    public void bankManagerLoginTest() throws IOException {

        log.debug("Login test started");
        click("bank-manager-login-button-css");
        Assert.assertTrue(isElementPresent(By.cssSelector(locators.getProperty("add-customer-button-css"))), "Login was not performed");
        log.debug("Login test executed");
    }
}
