import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DebitCardApplicationTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void bevoreEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void successfullySubmittedApplicationTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Путин Владимир");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        String actualText = actualElement.getText().trim();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void validationOfFieldsNameTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Vladimir Putin");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();

        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void phoneNumberValidationTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Путин Владимир");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();

        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }


    @Test
    public void emptyNameFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();

        Assertions.assertEquals("Поле обязательно для заполнения", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }


    @Test
    public void emptyNumberFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Путин Владимир");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button")).click();

        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();

        Assertions.assertEquals("Поле обязательно для заполнения", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void theCheckboxIsNotCheckedTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Путин Владимир");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector(".button")).click();

        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text"));
        String actualText = actualElement.getText().trim();

        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", actualText);
        Assertions.assertTrue(actualElement.isDisplayed());
    }
}
