package com.arrowsmith.tvtropestests;

import com.arrowsmith.base.TvTropesTestBase;
import com.arrowsmith.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest extends TvTropesTestBase {

    @Parameters({"url", "username", "password"})
    @Test(enabled = true, groups = {"positiveTest"})
    private void successfulLoginTest(String url, String username, String password)
    {
        getUrlAndMaximize(url);

        consentToCookies();

        login(username, password);

        Assert.assertTrue(doesUserProfileExist(), "User profile not found - login unsuccessful");
    }

    @Parameters({"url", "username", "password"})
    @Test(enabled = true, groups = {"negativeTest"})
    private void unsuccessfulLoginTest(String url, String username, String password)
    {
        getUrlAndMaximize(url);

        consentToCookies();

        login(username, password);

        Assert.assertFalse(doesUserProfileExist(), "User profile found - login successful");
    }


    private void login(String username, String password) {
        WebElement loginBox = driver.findElement(By.id("signup-login-box"));
        WebElement loginButton = loginBox.findElement(By.xpath ("//*[contains(text(),'Login')]"));
        loginButton.click();

        WebElement modalBox = driver.findElement(By.id("modal-box"));

        WebElement usernameInput = modalBox.findElement(By.id("modal-login-username"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = modalBox.findElement(By.id("modal-login-password"));
        passwordInput.sendKeys(password);

        WebElement formLogin = driver.findElement(By.id("form-login"));
        WebElement submitButton = formLogin.findElement(By.className("login-form-submit"));

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(s -> !TestUtils.doesElementContainClass(s.findElement(By.id("modal-box")), "paused"));
    }

    private boolean doesUserProfileExist() {
        try
        {
            WebElement mainHeaderBarRight = driver.findElement(By.id("main-header-bar-right"));
            WebElement userImage = mainHeaderBarRight.findElement(By.id("user-image-box"));
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
