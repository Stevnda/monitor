package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisFileRelational {
    String caseId;
    String fileId;
    String dataListId;
}
