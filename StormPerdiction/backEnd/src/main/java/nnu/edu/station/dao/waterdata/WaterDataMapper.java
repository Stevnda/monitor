package nnu.edu.station.dao.waterdata;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface WaterDataMapper {
    List<Map<String, Object>> getWaterStationData(@Param("station") String station, @Param("startTime") String time, @Param("endTime") String endTime);
}