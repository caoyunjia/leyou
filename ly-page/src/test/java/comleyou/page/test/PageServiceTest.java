package comleyou.page.test;

import com.leyou.page.service.PageService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    private PageService pageService;


    public void createHtml(){
        pageService.createHtml(141L);
    }
}
