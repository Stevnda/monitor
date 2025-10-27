package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/10:03
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    String mmsi;
    String name;
    String nameCn;
    String updateTime;
    Double longitude;
    Double latitude;
    String speed;
    String course;
    String draft;
    String length;
    String width;
    String classType;
}
