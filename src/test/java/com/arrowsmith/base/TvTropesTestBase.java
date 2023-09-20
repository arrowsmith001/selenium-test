package com.arrowsmith.base;

import org.openqa.selenium.By;

public class TvTropesTestBase extends TestBase
{
    protected void consentToCookies()
    {
        String consentClass = "fc-cta-consent";
        driver.findElement(By.className(consentClass)).click();
    }
}
