import com.gdc.avp.component.BCryptPasswordEncoder;
import com.gwm.avp.util.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(classes = SetPwdTest.class)
@ComponentScan("com.gdc.avp")
public class SetPwdTest {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void setPwdTest() {
        String pwdString = "xiaoM123.";
        String salt = passwordEncoder.encode(pwdString);
        String str = BCrypt.hashpw(pwdString, salt);
        //  $2a$10$uS4VullnR / cluf6mJxZVA.zQCCqIAzyR5HrPuSQkG6ErmCUTqd5vG
        System.out.println(str);

    }
}