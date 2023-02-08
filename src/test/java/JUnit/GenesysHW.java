package JUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenesysHW {
    private static WebDriver driver = new ChromeDriver();

    @BeforeClass
    public static void setUp(){
        System.setProperty("webdriver.chrome.driver", "/selenium-driver/chromedriver");
        driver = new ChromeDriver();
    }

    @After
    public static void cleanUp() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void test1() {
        String URL = "https://www.saucedemo.com/inventory.html";

        driver.get(URL);

        String username = "";
        String password = "";

        try {
            Path jsonFile = Paths.get("src\\test\\resources\\credential.json");
            String contents = new String(Files.readAllBytes(jsonFile));
            JSONObject credential = new JSONObject(contents);

            username = credential.get("username").toString();
            password = credential.get("password").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket")).click();
        String number = driver.findElement(By.className("shopping_cart_badge")).getText();

        assertEquals(2, Integer.valueOf(number));

        driver.findElement(By.id("shopping_cart_container")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("Teszt");
        driver.findElement(By.id("last-name")).sendKeys("Elek");
        driver.findElement(By.id("postal-code")).sendKeys("1234");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();

        String complete = driver.findElement(By.className("complete-header")).getText();

        assertEquals("THANK YOU FOR YOUR ORDER", complete);

        driver.quit();
    }

    @Test
    public void test2() {
        String URL = "https://www.saucedemo.com/inventory.html";

        driver.get(URL);

        driver.findElement(By.id("login-button")).click();

        String errorMessage = driver.findElement(By.cssSelector("div.error-message-container.error > h3")).getText();

        assertEquals("Epic sadface: Username is required", errorMessage);

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();


        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

        String footerText = driver.findElement(By.className("footer_copy")).getText();

        boolean isTextContains2023 = footerText.contains("2023");
        boolean isTextContainsTerms = footerText.contains("Terms of Service");

        assertTrue(isTextContains2023);
        assertTrue(isTextContainsTerms);

        driver.quit();
    }

    @Test
    public void test3() {
        String URL = "https://onlinehtmleditor.dev";

        driver.get(URL);

        driver.findElement(By.id("cke_19")).click();
        driver.switchTo().frame(0);
        driver.switchTo().activeElement().sendKeys("Automation ");


        driver.switchTo().defaultContent();
        driver.findElement(By.id("cke_19")).click();
        driver.findElement(By.id("cke_21")).click();
        driver.switchTo().frame(0);
        driver.switchTo().activeElement().sendKeys("Test");

        driver.switchTo().defaultContent();
        driver.findElement(By.id("cke_21")).click();
        driver.switchTo().frame(0);
        driver.switchTo().activeElement().sendKeys(" Example");

        String text = driver.switchTo().activeElement().getText();

        driver.switchTo().defaultContent();

        assertEquals("Automation Test Example", text);

        driver.quit();
    }

    @Test
    public void test4() {
        String URL = "http://demo.guru99.com/test/guru99home/";

        driver.get(URL);
        driver.manage().window().maximize();

        driver.switchTo().frame("gdpr-consent-notice");

        driver.findElement(By.cssSelector("#save > span.mat-button-wrapper > div > span")).click();

        driver.switchTo().defaultContent();

        driver.switchTo().frame("a077aa5e");
        driver.findElement(By.xpath("html/body/a/img")).click();
        // új tab
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));

        String title = driver.getTitle();

        assertEquals("Selenium Live Project: FREE Real Time Project for Practice", title);

        driver.close();

        driver.switchTo().window(tabs2.get(0));

        Actions action = new Actions(driver);

        action.moveToElement(driver.findElement(By.cssSelector("li.item118.parent"))).perform();
        driver.findElement(By.cssSelector("li.item121")).click();

        driver.findElement(By.cssSelector("button.fc-button.fc-cta-consent.fc-primary-button")).click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

        WebElement wideRedButton = driver.findElement(By.cssSelector("#af-body-1742405067 > div.af-element.buttonContainer > input"));
        Boolean isWideRedButtonDisplayed = wideRedButton.isDisplayed();

        Assert.assertTrue(isWideRedButtonDisplayed);

        driver.quit();
    }

    @Test
    public void test5() {
        driver.quit();
        HttpURLConnection connection = null;
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/users");

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            JSONArray jsonArray = new JSONArray(responseContent.toString());

            System.out.println("---------------------------------------------------------");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String fixedLengthName = String.format("%1$" + 25 + "s", jsonObj.get("name"));
                String fixedLengthEmail = String.format("%1$" + 25 + "s", jsonObj.get("email"));
                System.out.println("| " + fixedLengthName + " | " + fixedLengthEmail + " |");
            }
            System.out.println("---------------------------------------------------------");

            String firstEmail = jsonArray.getJSONObject(0).get("email").toString();
            boolean isFirstEmailContainAtChar = firstEmail.contains("@");

            Assert.assertTrue(isFirstEmailContainAtChar);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
