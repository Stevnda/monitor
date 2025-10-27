package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:18
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    String id;
    String fileName;
    String address;
    String createTime;
    String creator;
    String visualType;
    String visualId;
    String caseId;
}
