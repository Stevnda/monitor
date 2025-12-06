package nnu.edu.station.dao.level;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface WaterDataMapper {
    List<Map<String, Object>> getWaterStationData(@Param("station") String station, @Param("startTime") String time, @Param("endTime") String endTime);
}
