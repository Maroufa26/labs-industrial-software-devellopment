// tag::testShowDesignForm[]
package articles;
import static org.mockito.Mockito.verify;
import static 
    org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static 
    org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import articles.CategoriesArticle.Type;
import articles.web.CategoriesArticleController;

//tag::testProcessForm[]
@RunWith(SpringRunner.class)
@WebMvcTest(CategoriesArticleController.class)
public class CategoriesArticleControllerTest {
//end::testProcessForm[]

  @Autowired
  private MockMvc mockMvc;
  
  private List<CategoriesArticle> categories;

//end::testShowDesignForm[]

  /*
//tag::testProcessForm[]
   ...

//end::testProcessForm[]
 */

//tag::testProcessForm[]
  private Article design;

//end::testProcessForm[]

//tag::testShowDesignForm[]
  @Before
  public void setup() {
    categories = Arrays.asList(
      new CategoriesArticle("DIND", "Door-in-Door", Type.REFRIGERATORS),
      new CategoriesArticle("FMHB", "Family Hub", Type.REFRIGERATORS),
      new CategoriesArticle("PWSH", "PowerWash", Type.COOKERS),
      new CategoriesArticle("WLPL", "Whirlpool", Type.COOKERS),
      new CategoriesArticle("BSCH", "Bosch", Type.DISHWASHERS),
      new CategoriesArticle("KNAD", "KitchenAid", Type.DISHWASHERS),
      new CategoriesArticle("MTAG", "Maytag", Type.WASHING),
      new CategoriesArticle("SPQN", "Speed Queen", Type.WASHING),
      new CategoriesArticle("DMBK", "Redmi book", Type.COMPUTERS),
      new CategoriesArticle("ASUS", "ASUS", Type.COMPUTERS)
    );
    
//end::testShowDesignForm[]
    
    design = new Article();
    design.setName("Test Article");
    design.setArticles(Arrays.asList("DIND", "FMHB", "PWSH"));
//tag::testShowDesignForm[]
  }

  @Test
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(get("/design"))
        .andExpect(status().isOk())
        .andExpect(view().name("design"))
        .andExpect(model().attribute("refrigerators", categories.subList(0, 2)))
        .andExpect(model().attribute("cookers", categories.subList(2, 4)))
        .andExpect(model().attribute("washing", categories.subList(4, 6)))
        .andExpect(model().attribute("dishwasher", categories.subList(6, 8)))
        .andExpect(model().attribute("computers", categories.subList(8, 10)));
  }
//end::testShowDesignForm[]

  /*
//tag::testProcessForm[]
   ...

//end::testProcessForm[]
 */
  
//tag::testProcessForm[]
  @Test
  public void processDesign() throws Exception {
    mockMvc.perform(post("/design")
        .content("name=Test+Article&categories=DIND,FMHB,PWSH")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().stringValues("Location", "/orders/current"));
  }

//tag::testShowDesignForm[]
}
//end::testShowDesignForm[]
//end::testProcessForm[]
