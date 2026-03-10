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
class PaymentFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d/payment", testBaseUrl, serverPort);
    }

    @Test
    void getPaymentDetailByInvalidId_shouldShowNotFoundMessage(ChromeDriver driver) {
        driver.get(baseUrl + "/detail/invalid-id");

        WebElement heading = driver.findElement(By.id("paymentDetailTitle"));
        String detailText = driver.findElement(By.id("paymentDetailValue")).getText();
        assertTrue(heading.getText().contains("Payment Detail"));
        assertTrue(detailText.contains("not found"));
    }

    @Test
    void adminList_shouldOpenPaymentListPage(ChromeDriver driver) {
        driver.get(baseUrl + "/admin/list");

        WebElement heading = driver.findElement(By.id("paymentAdminListTitle"));
        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
        assertTrue(heading.getText().contains("Payment Admin List"));
        assertTrue(rows.size() >= 1);
    }
}
