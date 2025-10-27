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
public class Files {
    String id;
    String fileName;
    String address;
    String size;
    String visualType;
    String visualId;
    String parentId;
}
