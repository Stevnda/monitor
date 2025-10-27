package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/31/15:23
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Locus {
    String time;
    String name;
    Double locationX;
    Double locationY;
    Double speed;
}
