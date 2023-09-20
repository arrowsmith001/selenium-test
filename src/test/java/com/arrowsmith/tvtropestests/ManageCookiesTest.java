package com.arrowsmith.tvtropestests;

import com.arrowsmith.base.TvTropesTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class ManageCookiesTest extends TvTropesTestBase
{
    final String minimalCookieName = "FCCDCF";


    @Parameters("url")
    @Test(priority = 1, enabled = true, groups = {"testGroup"})
    public void confirmMinimalCookiesTest(String url) {

        driver.get(url);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        final Set<Cookie> cookiesBefore = driver.manage().getCookies();

        String dialogClass = "fc-dialog-container";
        WebElement dialog = driver.findElement(By.className(dialogClass));
        assert(dialog.isDisplayed());

        selectAndConfirmMinimalCookies();

        List<WebElement> dialogsAfter = driver.findElements(By.className(dialogClass));
        assert(dialogsAfter.isEmpty());

        final Set<Cookie> cookiesAfter = driver.manage().getCookies();
        assert(cookiesBefore.size() < cookiesAfter.size());

    }

    @Parameters("url")
    @Test
    public void confirmAllCookiesTest(String url)
    {
        driver.get(url);
        driver.manage().window().maximize();

        // TODO: Check actual cookies

        try
        {
            String dialogClass = "fc-dialog-container";

            WebElement dialog = driver.findElement(By.className(dialogClass));

            assert(dialog != null && dialog.isDisplayed());

            consentToCookies();

            List<WebElement> dialogsAfter = driver.findElements(By.className(dialogClass));

            assert(dialogsAfter.isEmpty());
        }
        catch(Exception e)
        {
            assert(false);
        }
    }


    private void selectAndConfirmMinimalCookies() {

        String dialogClass = "fc-dialog-container";
        WebElement dialog = driver.findElement(By.className(dialogClass));

        String manageConsentClass = "fc-cta-manage-options";
        dialog.findElement(By.className(manageConsentClass)).click();

        String preferencesContainerClass1 = "fc-data-preferences-dialog";
        String preferencesContainerClass2 = "fc-vendor-preferences-dialog";
        String manageVendorsButtonClass = "fc-manage-vendors";

        WebElement preferences1 = dialog.findElement(By.className(preferencesContainerClass1));
        togglePreferences(preferences1);

        dialog.findElement(By.className(manageVendorsButtonClass)).click();

        WebElement preferences2 = dialog.findElement(By.className(preferencesContainerClass2));
        togglePreferences(preferences2);

        String confirmChoices = "fc-confirm-choices";
        preferences2.findElement(By.className(confirmChoices)).click();

    }

    private void togglePreferences(WebElement root) {
        String sliderParentClass = "fc-preference-slider";
        List<WebElement> sliderParents = root.findElements(By.className(sliderParentClass));

        String inputTag = "input";
        String sliderElement = "fc-slider-el";



        sliderParents.forEach(parent ->
        {
            String isOn = parent.findElement(By.tagName(inputTag)).getAttribute("aria-pressed");

            if(isOn.equals("true"))
            {
                try
                {
                    parent.findElement(By.className(sliderElement)).click();
                }
                catch(Exception e)
                {
                    //System.out.println(e.toString());
                }
            }
        });
    }


}
