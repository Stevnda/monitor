package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/23/15:28
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    String name;
    Double startLon;
    Double startLat;
    Double endLon;
    Double endLat;
    String angle;
}
