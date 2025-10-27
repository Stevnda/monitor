package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowseHistory {
    String id;
    String userId;
    String browseTime;
    String dataId;
}
