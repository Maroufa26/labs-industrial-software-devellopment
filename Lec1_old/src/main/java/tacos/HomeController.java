package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
/* Контроллер */

public class HomeController {

    @GetMapping("/")
    /* Обрабатывает запросы корневого пути */
    public String home() {
        return "home"; /* Возвращает имя представления */
    }
}
