package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d/order", testBaseUrl, serverPort);
    }

    @Test
    void createOrderAndShowInHistory_isCorrect(ChromeDriver driver) {
        driver.get(baseUrl + "/create");

        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.clear();
        authorInput.sendKeys("Safira Sudrajat");

        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Create']"));
        submitButton.click();

        driver.get(baseUrl + "/history");

        WebElement historyAuthorInput = driver.findElement(By.id("authorInput"));
        historyAuthorInput.clear();
        historyAuthorInput.sendKeys("Safira Sudrajat");

        WebElement searchButton = driver.findElement(By.xpath("//button[text()='Search']"));
        searchButton.click();

        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        boolean found = false;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2 && cells.get(1).getText().equals("Safira Sudrajat")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Order author was not found in history list.");
    }

    @Test
    void historyWithLowercaseAuthor_shouldNotShowData(ChromeDriver driver) {
        driver.get(baseUrl + "/history");

        WebElement historyAuthorInput = driver.findElement(By.id("authorInput"));
        historyAuthorInput.clear();
        historyAuthorInput.sendKeys("safira sudrajat");

        WebElement searchButton = driver.findElement(By.xpath("//button[text()='Search']"));
        searchButton.click();

        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
        assertTrue(rows.isEmpty(), "Order list should be empty for lowercase author query.");
    }
}
