package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/30/21:18
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    String id;
    String folderName;
    String parentId;
}
