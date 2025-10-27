package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/24/11:02
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SandTransport {
    String time;
    String name;
    String type;
    Double value;
    String tableId;
}
