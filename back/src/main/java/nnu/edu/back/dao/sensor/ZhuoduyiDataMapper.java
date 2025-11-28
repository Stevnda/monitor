package nnu.edu.back.dao.sensor;

import nnu.edu.back.pojo.SensorZhuoduyi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ZhuoduyiDataMapper {
    List<SensorZhuoduyi> getZhuoduyiDataByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<SensorZhuoduyi> getAllZhuoduyiData();
}