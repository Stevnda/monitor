package nnu.edu.back.dao.main;

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
public interface AnalysisFileRelationalMapper {
    void addRecord(@Param("id") String id, @Param("list") List<Map<String, String>> list);

    List<Map<String, Object>> getData(@Param("caseId") String caseId);

    void delData(@Param("caseId") String caseId, @Param("fileId") String fileId, @Param("dataListId") String dataListId);

    void delByCaseId(@Param("caseId") String caseId);
}
