package nnu.edu.back.dao.sensor;

import nnu.edu.back.pojo.SensorAdcp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AdcpDataMapper {
    List<SensorAdcp> getAdcpDataByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<SensorAdcp> getAllAdcpData();
}