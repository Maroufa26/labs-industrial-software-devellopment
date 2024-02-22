package tacos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)

@WebMvcTest(HomeController.class) /*Тест для HomeController*/
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc; /*Внедряет MockMvc*/

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/")) /*Выполнение GET-запроса*/
        .andExpect(status().isOk()) /*Ожидание HTTP-ответа 200*/
        .andExpect(view().name("home")) /*Ожидание ответа "home"*/
        .andExpect(content().string(containsString("Магазин электротоваров!")));
    }

}