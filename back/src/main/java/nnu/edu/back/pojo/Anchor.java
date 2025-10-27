package nnu.edu.back.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/22:27
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anchor {
    String id;
    String anchorName;
    String waterwayName;
    String type;
    Double longitude;
    Double latitude;
    String picture;
    String management;
    String hdHdlyName;
    String tyAnbName;
    String usage;
    String buildTime;
    Double hdlc;
    JSONObject region;
    String hdHdly;
    String tyAnb;
    String sdName;
}
