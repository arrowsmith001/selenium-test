package com.arrowsmith.tvtropestest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class LinksTest
{
    String url = "https://tvtropes.org/";
    String sopranos = "https://tvtropes.org/pmwiki/pmwiki.php/Series/TheSopranos";

    public LinksTest(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @Test
    public void spoilerTest()
    {
        WebDriver driver = new ChromeDriver();
        driver.get(sopranos);
        driver.manage().window().maximize();

        confirmAllCookies(driver);

        String spoilerToggleId = "sidebar-toggle-showspoilers";
        WebElement spoilerToggleClass = driver.findElement(By.id(spoilerToggleId));

        String elementClass = spoilerToggleClass.getAttribute("class");
        String[] classes = elementClass.split(" ");
        boolean isActive = Arrays.asList(classes).contains("active");

        assert(!isActive);

        spoilerToggleClass.click();

        elementClass = spoilerToggleClass.getAttribute("class");
        classes = elementClass.split(" ");
        isActive = Arrays.asList(classes).contains("active");

        assert(isActive);

        // TODO: Confirm all spoiler tags were set to spoiler off

//        spoilers.forEach(s ->
//        {
//            String cssClass1 = s.getAttribute("class");
//            assert(Objects.equals(cssClass1, "spoiler"));
//            s.click();
//            String cssClass2 = s.getAttribute("class");
//            assert(Objects.equals(cssClass2, "spoiler off"));
//        });
    }

    @Test
    public void successfulMinimalCookieConfirmTest()
    {
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();

        try
        {
            String dialogClass = "fc-dialog-container";

            WebElement dialog = driver.findElement(By.className(dialogClass));

            assert(dialog != null && dialog.isDisplayed());

            confirmMinimalCookies(driver);

            List<WebElement> dialogsAfter = driver.findElements(By.className(dialogClass));

            assert(dialogsAfter.isEmpty());
        }
        catch(Exception e)
        {
            driver.close();
            assert(false);
        }

        driver.close();
    }

    @Test
    public void successfulSpoilerToggleTest()
    {
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();

        confirmAllCookies(driver);

        driver.close();
    }

    private void confirmMinimalCookies(WebDriver driver) {

        String dialogClass = "fc-dialog-container";
        WebElement dialog = driver.findElement(By.className(dialogClass));

        String manageConsentClass = "fc-cta-manage-options";
        dialog.findElement(By.className(manageConsentClass)).click();

        String sliderParentClass = "fc-preference-slider";
        List<WebElement> sliderParents = dialog.findElements(By.className(sliderParentClass));

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

        String confirmChoices = "fc-confirm-choices";
        driver.findElement(By.className(confirmChoices)).click();
    }

    private void confirmAllCookies(WebDriver driver) {
        String consentClass = "fc-cta-consent";
        driver.findElement(By.className(consentClass)).click();
    }

}
