package nnu.edu.back.pojo;

import com.alibaba.fastjson2.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/24/9:47
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Substrate {
    String time;
    String location;
    String type;
    String soil;
    JSONArray level;
}
