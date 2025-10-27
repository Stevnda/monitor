package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/23:44
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buoy {
    String id;
    String picture;
    String photo;
    String color;
    String des;
    String name;
    String shape;
    Double longitude;
    Double latitude;
    String waterway;
    String noMeaning;
}
