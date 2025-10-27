package nnu.edu.back.pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/28/20:06
 * @Description:
 */
@Data
public class User {
    String email;
    String name;

    String password;
    String role;
}
