package nnu.edu.back.dao.sensor;

import nnu.edu.back.pojo.SensorChaoweiyi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChaoweiyiDataMapper {
    List<SensorChaoweiyi> getChaoweiyiDataByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<SensorChaoweiyi> getAllChaoweiyiData();
}