package org.gluu.adminui.api.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.jans.as.client.AuthorizationRequest;
import io.jans.as.client.AuthorizationResponse;
import io.jans.as.model.common.ResponseType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SeleniumTestUtils {

    private static long WAIT_OPERATION_TIMEOUT = 60;
    private static String UTF8_STRING_ENCODING = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(SeleniumTestUtils.class);

    public static String authorizeClient(
            String opHost, String userId, String userSecret, String clientId, String redirectUrls, String state, String nonce, List<String> responseTypes, List<String> scopes) {
        WebDriver driver = initWebDriver(true, true);

        String redirectUrl = loginGluuServer(driver, opHost, userId, userSecret, clientId, redirectUrls, state, nonce, responseTypes, scopes);
        //AuthorizationResponse authorizationResponse = acceptAuthorization(driver);
        driver.quit();
        return redirectUrl;
    }

    private static String loginGluuServer(
            WebDriver driver, String opHost, String userId, String userSecret, String clientId, String redirectUrls, String state, String nonce, List<String> responseTypes, List<String> scopes) {
        //navigate to opHost
        driver.navigate().to(getAuthorizationUrl(opHost, clientId, redirectUrls, state, nonce, responseTypes, scopes));
        //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(WAIT_OPERATION_TIMEOUT, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement loginButton = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver d) {
                //System.out.println(d.getCurrentUrl());
                //System.out.println(d.getPageSource());
                return d.findElement(By.id("loginForm:loginButton"));
            }
        });

        LOG.info("Login page loaded. The current url is: " + driver.getCurrentUrl());
        //username field
        WebElement usernameElement = driver.findElement(By.id("loginForm:username"));
        usernameElement.sendKeys(userId);
        //password field
        WebElement passwordElement = driver.findElement(By.id("loginForm:password"));
        passwordElement.sendKeys(userSecret);
        //click on login button
        loginButton.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver.getCurrentUrl();
    }

    private static AuthorizationResponse acceptAuthorization(WebDriver driver) {
        String authorizationResponseStr = driver.getCurrentUrl();
        AuthorizationResponse authorizationResponse = null;
        // Check for authorization form if client has no persistent authorization
        if (!authorizationResponseStr.contains("#")) {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(WAIT_OPERATION_TIMEOUT, TimeUnit.SECONDS)
                    .pollingEvery(500, TimeUnit.MILLISECONDS)
                    .ignoring(NoSuchElementException.class);

            WebElement allowButton = wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver d) {
                    //System.out.println(d.getCurrentUrl());
                    //System.out.println(d.getPageSource());
                    return d.findElement(By.id("authorizeForm:allowButton"));
                }
            });

            // We have to use JavaScript because target is link with onclick
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("scroll(0, 1000)");

            String previousURL = driver.getCurrentUrl();

            Actions actions = new Actions(driver);
            actions.click(allowButton).perform();

            authorizationResponseStr = driver.getCurrentUrl();
            authorizationResponse = new AuthorizationResponse(authorizationResponseStr);

            LOG.info("Authorization Response url is: " + driver.getCurrentUrl());
        } else {
            Assertions.fail("The authorization form was expected to be shown.");
        }
        return authorizationResponse;
    }

    private static WebDriver initWebDriver(boolean enableJavascript, boolean cleanupCookies) {
        HtmlUnitDriver currentDriver = new HtmlUnitDriver(enableJavascript);
        try {
            if (cleanupCookies) {
                currentDriver.manage().deleteAllCookies();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentDriver;
    }

    private static String getAuthorizationUrl(String opHost, String clientId, String redirectUrls, String state, String nonce, List<String> responseTypes, List<String> scopes) {
        if (CollectionUtils.isEmpty(responseTypes)) {
            responseTypes = Lists.newArrayList("code");
        }

        if (CollectionUtils.isEmpty(scopes)) {
            scopes = Lists.newArrayList("openid", "profile", "email", "user_name");
        }
        List<ResponseType> resTypes = responseTypes.stream().map(item -> ResponseType.fromString(item)).collect(Collectors.toList());
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(resTypes, clientId, scopes, redirectUrls.split(" ")[0], nonce);
        authorizationRequest.setResponseTypes(responseTypes.stream().map(item -> ResponseType.fromString(item)).collect(Collectors.toList()));
        authorizationRequest.setState(state);
        String authzUrl = opHost + "/jans-auth/restv1/authorize?" + authorizationRequest.getQueryString(), UTF8_STRING_ENCODING;
        return URLDecoder.decode(authzUrl);

    }
}
