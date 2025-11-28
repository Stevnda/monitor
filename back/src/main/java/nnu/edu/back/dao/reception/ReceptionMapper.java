package nnu.edu.back.dao.reception;

import nnu.edu.back.pojo.Reception;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Reception database mapper
 */
@Repository
public interface ReceptionMapper {

    /**
     * 根据站点编码和时间范围查询数据
     * @param stcd 站点编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合条件的数据列表
     */
    List<Reception> getByStcdAndTimeRange(@Param("stcd") String stcd,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);
}
