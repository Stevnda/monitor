package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/28/21:42
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    String id;
    String projectName;
    String avatar;
    String description;
    String institution;
    String location;
    String time;
    String type;
    String uploadTime;
    Integer visual;
}
