package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/23/21:18
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flux {
    String time;
    String name;
    String type;
    Double value;
    String tableId;
}
