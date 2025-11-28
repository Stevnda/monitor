package nnu.edu.back.dao.waterdata;

import nnu.edu.back.pojo.WaterData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterDataMapper {
        List<WaterData> getWaterDataByStationAndTimeRange(@Param("stationName") String stationName,
                                              @Param("startTime") String startTime,
                                              @Param("endTime") String endTime);
}
