package com.arrowsmith.tvtropestests;

import com.arrowsmith.base.TvTropesTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksTest extends TvTropesTestBase
{
    final Pattern linkPattern = Pattern.compile("^https://");

    @Parameters("url")
    @Test
    private void testAllHyperlinksSucceed(String url)
    {
        getUrlAndMaximize(url);
        consentToCookies();

        int total = 0;
        int succeeded = 0;
        int failed = 0;
        int broken = 0;

        List<WebElement> links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) {

            String linkUrl = link.getAttribute("href");

            final Matcher matcher = linkPattern.matcher(linkUrl);
            if(matcher.find())
            {
                final LinkTestResult result = verifyLink(linkUrl);
                switch(result)
                {
                    case success: succeeded++; break;
                    case broken: broken++; break;
                    case failed: failed++; break;
                }
                total++;
            }
            else
            {
                System.out.println("Invalid link: " + linkUrl);
            }

        }

        System.out.println(total + " links tested:");
        System.out.println("\t" + succeeded + " succeeded");
        System.out.println("\t" + failed + " failed");
        System.out.println("\t" + broken + " broken");

        Assert.assertEquals(broken, 0, "Some links broken - number broken: " + broken);
        Assert.assertEquals(failed, 0, "Some links failed - number failed: " + failed);
    }

    private LinkTestResult verifyLink(String url) {
        try {
            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                System.out.println(url + " - " + httpURLConnection.getResponseMessage());
                return LinkTestResult.success;
            } else {
                System.out.println(url + " - " + httpURLConnection.getResponseMessage() + " - " + "FAILED - response code: " + httpURLConnection.getResponseCode());
                return LinkTestResult.failed;
            }
        } catch (Exception e) {
            System.out.println(url + " - " + "FAILED - no response");
            return LinkTestResult.broken;
        }
    }

}

enum LinkTestResult {
    success, failed, broken
}