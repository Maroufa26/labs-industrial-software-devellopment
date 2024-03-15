// tag::head[]
package articles.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import articles.Article;
import articles.CategoriesArticle;
import articles.CategoriesArticle.Type;

@Slf4j
@Controller
@RequestMapping("/design")
public class CategoriesArticleController {

//end::head[]

@ModelAttribute
public void addArticlesToModel(Model model) {
	List<CategoriesArticle> categories = Arrays.asList(
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
	
	Type[] types = CategoriesArticle.Type.values();
	for (Type type : types) {
	  model.addAttribute(type.toString().toLowerCase(),
	      filterByType(categories, type));
	}
}
	
//tag::showDesignForm[]
  @GetMapping
  public String showDesignForm(Model model) {
    model.addAttribute("design", new Article());
    return "design";
  }

//end::showDesignForm[]

/*
//tag::processDesign[]
  @PostMapping
  public String processDesign(Design design) {
    // Save the article design...
    // We'll do this in chapter 3
    log.info("Processing design: " + design);

    return "redirect:/orders/current";
  }

//end::processDesign[]
 */

//tag::processDesignValidated[]
  @PostMapping
  public String processDesign(@Valid @ModelAttribute("design") Article design, Errors errors, Model model) {
    if (errors.hasErrors()) {
      return "design";
    }

    // Save the article design...
    // We'll do this in chapter 3
    log.info("Processing design: " + design);

    return "redirect:/orders/current";
  }

//end::processDesignValidated[]

//tag::filterByType[]
  private List<CategoriesArticle> filterByType(
		  List<CategoriesArticle> categories, Type type) {
    return categories
              .stream()
              .filter(x -> x.getType().equals(type))
              .collect(Collectors.toList());
  }

//end::filterByType[]
// tag::foot[]
}
// end::foot[]
