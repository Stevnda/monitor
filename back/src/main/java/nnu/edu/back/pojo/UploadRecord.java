package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/09/06/14:56
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRecord {
    String id;
    String fileName;
    String uploadTime;
    String size;
}
