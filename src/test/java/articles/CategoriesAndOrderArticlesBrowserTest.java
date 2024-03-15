package articles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CategoriesAndOrderArticlesBrowserTest {
  
  private static HtmlUnitDriver browser;
  
  @LocalServerPort
  private int port;
  
  @Autowired
  TestRestTemplate rest;
  
  @BeforeClass
  public static void setup() {
    browser = new HtmlUnitDriver();
    browser.manage().timeouts()
        .implicitlyWait(10, TimeUnit.SECONDS);
  }
  
  @AfterClass
  public static void closeBrowser() {
    browser.quit();
  }
  
  @Test
  public void testDesignATacoPage_HappyPath() throws Exception {
    browser.get(homePageUrl());
    clickDesignAArticle();
    assertDesignPageElements();
    buildAndSubmitAArticle("Basic Article", "DIND", "FMHB", "PWSH", "WLPL", "BSCH");
    clickBuildAnotherArticle();
    buildAndSubmitAArticle("Another Article", "KNAD", "MTAG", "SPQN", "DMBK", "ASUS");
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }
  
  @Test
  public void testDesignAArticlePage_EmptyOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignAArticle();
    assertDesignPageElements();
    buildAndSubmitAArticle("Basic Article", "DIND", "FMHB", "PWSH", "WLPL", "BSCH");
    submitEmptyOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }

  @Test
  public void testDesignAArticlePage_InvalidOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignAArticle();
    assertDesignPageElements();
    buildAndSubmitAArticle("Basic Article", "DIND", "FMHB", "PWSH", "WLPL", "BSCH");
    submitInvalidOrderForm();
    fillInAndSubmitOrderForm();
    assertEquals(homePageUrl(), browser.getCurrentUrl());
  }

  //
  // Browser test action methods
  //
  private void buildAndSubmitAArticle(String name, String... articles) {
    assertDesignPageElements();

    for (String article : articles) {
      browser.findElementByCssSelector("input[value='" + article + "']").click();
    }
    browser.findElementByCssSelector("input#name").sendKeys(name);
    browser.findElementByCssSelector("form").submit();
  }

  private void assertDesignPageElements() {
    assertEquals(designPageUrl(), browser.getCurrentUrl());
    List<WebElement> articleGroups = browser.findElementsByClassName("article-group");
    assertEquals(5, articleGroups.size());
    
    WebElement refrigeratorGroup = browser.findElementByCssSelector("div.article-group#refrigerators");
    List<WebElement> refrigerators = refrigeratorGroup.findElements(By.tagName("div"));
    assertEquals(2, refrigerators.size());
    assertArticle(refrigeratorGroup, 0, "DIND", "Door-in-Door");
    assertArticle(refrigeratorGroup, 1, "FMHB", "Family Hub");
    
    WebElement cookerGroup = browser.findElementByCssSelector("div.article-group#cookers");
    List<WebElement> cookers = cookerGroup.findElements(By.tagName("div"));
    assertEquals(2, cookers.size());
    assertArticle(cookerGroup, 0, "PWSH", "PowerWash");
    assertArticle(cookerGroup, 1, "WLPL", "Whirlpool");

    WebElement dishwasherGroup = browser.findElementByCssSelector("div.article-group#dishwashers");
    List<WebElement> dishwashers = cookerGroup.findElements(By.tagName("div"));
    assertEquals(2, dishwashers.size());
    assertArticle(dishwasherGroup, 0, "BSCH", "Bosch");
    assertArticle(dishwasherGroup, 1, "KNAD", "KitchenAid");

    WebElement washingGroup = browser.findElementByCssSelector("div.article-group#washings");
    List<WebElement> washings = cookerGroup.findElements(By.tagName("div"));
    assertEquals(2, washings.size());
    assertArticle(washingGroup, 0, "MTAG", "Maytag");
    assertArticle(washingGroup, 1, "SPQN", "Speed Queen");

    WebElement computerGroup = browser.findElementByCssSelector("div.article-group#computers");
    List<WebElement> computers = cookerGroup.findElements(By.tagName("div"));
    assertEquals(2, computers.size());
    assertArticle(computerGroup, 0, "Redmi book", "Salsa");
    assertArticle(computerGroup, 1, "ASUS", "Sour Cream");
  }
  

  private void fillInAndSubmitOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#name", "Ima Hungry");
    fillField("input#street", "1234 Culinary Blvd.");
    fillField("input#city", "Foodsville");
    fillField("input#state", "CO");
    fillField("input#zip", "81019");
    fillField("input#ccNumber", "4111111111111111");
    fillField("input#ccExpiration", "10/19");
    fillField("input#ccCVV", "123");
    browser.findElementByCssSelector("form").submit();
  }

  private void submitEmptyOrderForm() {
    assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());
    browser.findElementByCssSelector("form").submit();
    
    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(9, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Name is required"));
    assertTrue(validationErrors.contains("Street is required"));
    assertTrue(validationErrors.contains("City is required"));
    assertTrue(validationErrors.contains("State is required"));
    assertTrue(validationErrors.contains("Zip code is required"));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));    
  }

  private List<String> getValidationErrorTexts() {
    List<WebElement> validationErrorElements = browser.findElementsByClassName("validationError");
      return validationErrorElements.stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  private void submitInvalidOrderForm() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    fillField("input#name", "I");
    fillField("input#street", "1");
    fillField("input#city", "F");
    fillField("input#state", "C");
    fillField("input#zip", "8");
    fillField("input#ccNumber", "1234432112344322");
    fillField("input#ccExpiration", "14/91");
    fillField("input#ccCVV", "1234");
    browser.findElementByCssSelector("form").submit();
    
    assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertEquals(4, validationErrors.size());
    assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
    assertTrue(validationErrors.contains("Not a valid credit card number"));
    assertTrue(validationErrors.contains("Must be formatted MM/YY"));
    assertTrue(validationErrors.contains("Invalid CVV"));    
  }

  private void fillField(String fieldName, String value) {
    WebElement field = browser.findElementByCssSelector(fieldName);
    field.clear();
    field.sendKeys(value);
  }
  
  private void assertArticle(WebElement articleGroup,
                                int articleIdx, String id, String name) {
    List<WebElement> cookers = articleGroup.findElements(By.tagName("div"));
    WebElement article = cookers.get(articleIdx);
    assertEquals(id, 
        article.findElement(By.tagName("input")).getAttribute("value"));
    assertEquals(name,
        article.findElement(By.tagName("span")).getText());
  }

  private void clickDesignAArticle() {
    assertEquals(homePageUrl(), browser.getCurrentUrl());
    browser.findElementByCssSelector("a[id='design']").click();
  }

  private void clickBuildAnotherArticle() {
    assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
    browser.findElementByCssSelector("a[id='another']").click();
  }


  //
  // URL helper methods
  //
  private String designPageUrl() {
    return homePageUrl() + "design";
  }

  private String homePageUrl() {
    return "http://localhost:" + port + "/";
  }

  private String orderDetailsPageUrl() {
    return homePageUrl() + "orders";
  }

  private String currentOrderDetailsPageUrl() {
    return homePageUrl() + "orders/current";
  }

}
