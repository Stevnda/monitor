package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.AnalysisResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:21
 * @Description:
 */
@Repository
public interface AnalysisResultMapper {
    List<AnalysisResult> getAnalysisResult(@Param("caseId") String caseId);

    void addAnalysisResult(@Param("analysisResult") AnalysisResult analysisResult);

    void delAnalysisResult(@Param("id") String id);

    void delAnalysisResultByCaseId(@Param("caseId") String caseId);

    AnalysisResult getInfoById(@Param("id") String id);

    void rename(@Param("id") String id, @Param("name") String name);
}
