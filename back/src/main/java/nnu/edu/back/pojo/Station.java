package nnu.edu.back.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/17/9:52
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    String id;
    String name;
    String nameEn;
    JSONObject keys;
    JSONObject keysCn;
    Double longitude;
    Double latitude;
    String type;
    JSONObject startTime;
    Integer prediction;
}
