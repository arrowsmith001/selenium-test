package com.arrowsmith.tvtropestests;

import com.arrowsmith.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class SpoilerTest {

    String sopranos = "https://tvtropes.org/pmwiki/pmwiki.php/Series/TheSopranos";

    public void spoilerTest() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.get(sopranos);
        driver.manage().window().maximize();


        // Get "show spoiler" toggle switch
        String spoilerToggleId = "sidebar-toggle-showspoilers";
        WebElement spoilerToggleClass = driver.findElement(By.id(spoilerToggleId));

        boolean isActive = TestUtils.isElementActive(spoilerToggleClass);
        assert( ! isActive); // Spoilers are hidden by default

        String hiddenSpoilerClass = "spoiler";
        String visibleSpoilerClass = "spoiler-off";

        List<WebElement> hiddenSpoilers = driver.findElements(By.className(hiddenSpoilerClass));
        List<WebElement> visibleSpoilers = driver.findElements(By.className(visibleSpoilerClass));

        System.out.println(hiddenSpoilers.size());
        System.out.println(visibleSpoilers.size());

        assert( ! hiddenSpoilers.isEmpty());
        assert(visibleSpoilers.isEmpty());

        hiddenSpoilers.forEach(s -> s.click());

        final int numberOfHiddenSpoilers = hiddenSpoilers.size();

        // Show spoilers and confirm is active
        spoilerToggleClass.click();
        boolean isActiveAfterClick = TestUtils.isElementActive(spoilerToggleClass);
        assert(isActiveAfterClick);

        List<WebElement> hiddenSpoilersAfterToggle = driver.findElements(By.className(hiddenSpoilerClass));
        List<WebElement> visibleSpoilersAfterToggle = driver.findElements(By.className(visibleSpoilerClass));

        System.out.println(hiddenSpoilersAfterToggle.size());
        System.out.println(visibleSpoilersAfterToggle.size());

        // Spoilers that were previously hidden should now show
        assert(hiddenSpoilersAfterToggle.isEmpty());
        assert(visibleSpoilersAfterToggle.size() == numberOfHiddenSpoilers);

    }

}
