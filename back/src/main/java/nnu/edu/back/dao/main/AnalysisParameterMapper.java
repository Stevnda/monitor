package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.AnalysisParameter;
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
public interface AnalysisParameterMapper {
    List<Map<String, Object>> findByType(@Param("type") String type);

    String findAddressByBenchmarkIdAndReferId(@Param("benchmarkId") String benchmarkId, @Param("referId") String referId, @Param("type") String type);

    AnalysisParameter findByBenchmarkIdAndReferId(@Param("benchmarkId") String benchmarkId, @Param("referId") String referId, @Param("type") String type);

    AnalysisParameter findSlope(@Param("demId") String demId);

    AnalysisParameter findInfoById(@Param("id") String id);
}
