package com.arrowsmith.tvtropestests;

import com.arrowsmith.base.TvTropesTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageCookiesTest extends TvTropesTestBase
{
    final Map<String, Integer> browsersToMinimumAdditionalCookies = new HashMap<String, Integer>() {{
        put("chrome", 3);
        put("edge", 3);
    }};


    @Parameters({"url", "browser"})
    @Test(priority = 1, enabled = true, groups = {"testGroup"})
    public void confirmMinimalCookiesTest(String url, String browser) {

        driver.get(url);
        driver.manage().window().maximize();

        final Set<Cookie> cookiesBefore = getCookies();

        String dialogClass = "fc-dialog-container";
        WebElement dialog = driver.findElement(By.className(dialogClass));
        Assert.assertTrue(dialog.isDisplayed(), "Cookie consent dialog box is not displayed");

        selectAndConfirmMinimalCookies();


        List<WebElement> dialogsAfter = driver.findElements(By.className(dialogClass));
        Assert.assertTrue(dialogsAfter.isEmpty(), "Cookie consent dialog box was found, should have been dismissed");

        final Set<Cookie> cookiesAfter = getCookies();
        final int minimalCookies = browsersToMinimumAdditionalCookies.get(browser);
        final int expectedNumberOfCookies = cookiesBefore.size() + minimalCookies;

        Assert.assertEquals(cookiesAfter.size(), expectedNumberOfCookies, "Number of cookies has not increased to minimum expected: "
                + cookiesBefore.size() + " before, "
                + cookiesAfter.size() + " after,"
                + expectedNumberOfCookies + " expected");
    }

    @Parameters({"url", "browser"})
    @Test(enabled = true)
    public void confirmAllCookiesTest(String url, String browser)
    {
        driver.get(url);
        driver.manage().window().maximize();

        final Set<Cookie> cookiesBefore = getCookies();

        String dialogClass = "fc-dialog-container";

        WebElement dialog = driver.findElement(By.className(dialogClass));
        Assert.assertTrue(dialog.isDisplayed(), "Cookie consent dialog box is not displayed");

        consentToCookies();

        List<WebElement> dialogsAfter = driver.findElements(By.className(dialogClass));
        Assert.assertTrue(dialogsAfter.isEmpty(), "Cookie consent dialog box was found, should have been dismissed");


        final Set<Cookie> cookiesAfter = getCookies();
        final int minimalCookies = browsersToMinimumAdditionalCookies.get(browser);
        final int expectedMinimalNumberOfCookies = cookiesBefore.size() + minimalCookies;
        boolean hasCookiesIncreasedToAboveMinimum = expectedMinimalNumberOfCookies <= cookiesAfter.size();

        Assert.assertTrue(hasCookiesIncreasedToAboveMinimum, "Number of cookies has not increased above minimum expected: "
                + cookiesBefore.size() + " before, "
                + cookiesAfter.size() + " after,"
                + " at least " + expectedMinimalNumberOfCookies + " expected");

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
