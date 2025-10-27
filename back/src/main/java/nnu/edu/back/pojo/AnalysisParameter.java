package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:16
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisParameter {
    String id;
    String type;
    String fileId;
    String dataListId;
    String address;
    String content;
    String benchmarkId;
    String referId;
}
