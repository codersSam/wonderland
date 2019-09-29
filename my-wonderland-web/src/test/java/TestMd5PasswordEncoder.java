import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Created by Administrator on 2019/9/18.
 */
public class TestMd5PasswordEncoder {

    @Test
    public void testEncode(){
        String password = new Md5PasswordEncoder().encodePassword("123456789q","15775059838@sina.cn");
        System.out.println(password);
    }
}
