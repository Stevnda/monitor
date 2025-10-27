package nnu.edu.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackApplicationTests {

    @Test
    void contextLoads() {
        String str = "1.png";
        String suffix = str.substring(str.lastIndexOf("."));
        System.out.println(suffix);
    }

}
