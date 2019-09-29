import com.senhua.my.wonderland.web.entity.User;
import com.senhua.my.wonderland.web.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;



/**
 * Created by Administrator on 2019/7/20.
 */
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class TestTransaction extends AbstractJUnit4SpringContextTests {

    @Autowired
    private UserService userService;



    @Test
    public void testSave(){
        User user = new User();
        user.setNickName("2crandy森");
        user.setEmail("1139140535@qq.com");
        userService.regist(user);
    }





}

