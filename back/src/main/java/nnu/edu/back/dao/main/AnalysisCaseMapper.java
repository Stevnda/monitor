package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.AnalysisCase;
import nnu.edu.back.pojo.Files;
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
public interface AnalysisCaseMapper {
    void addAnalysis(@Param("analysisCase") AnalysisCase analysisCase);

    List<AnalysisCase> fuzzyQuery(@Param("email") String email, @Param("keyword") String keyword, @Param("size") int size, @Param("start") int start);

    int count(@Param("email") String email, @Param("keyword") String keyword);

    AnalysisCase getAnalysisInfo(@Param("id") String id);

    void updateLayer(@Param("id") String id, @Param("layers") List<String> layers);

    List<Files> getLayersInfo(@Param("list") List<String> list);

    void updateBaseInfo(@Param("analysisCase") AnalysisCase analysisCase);

    void deleteAnalysis(@Param("id") String id);
}
