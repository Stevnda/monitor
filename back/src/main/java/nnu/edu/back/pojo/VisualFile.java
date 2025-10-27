package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:27
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisualFile {
    String id;
    String fileName;
    String type;
    String content;
    String view;
}
