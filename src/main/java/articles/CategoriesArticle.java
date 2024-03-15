package articles;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CategoriesArticle {
  
  private final String id;
  private final String name;
  private final Type type;
  
  public static enum Type {
    REFRIGERATORS, COOKERS, DISHWASHERS, WASHING, COMPUTERS
  }

}
