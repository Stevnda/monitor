package nnu.edu.back.pojo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:16
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataList {
    String id;
    String name;
    List<String> location;
    String description;
    List<String> tags;
    String createTime;
    String updateTime;
    Integer download;
    Integer watch;
    String avatar;
    String provider;
    String range;
    String type;
    String providerPhone;
    String providerEmail;
    String providerAddress;
    String detail;
    String timeStamp;
}
