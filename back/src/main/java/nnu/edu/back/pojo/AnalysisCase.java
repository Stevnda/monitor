package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:13
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisCase {
    String id;
    String projectName;
    String creator;
    String createTime;
    String avatar;
    List<String> layerManage;
}
