package nnu.edu.back.service;

import nnu.edu.back.pojo.SensorAdcp;
import nnu.edu.back.pojo.SensorChaoweiyi;
import nnu.edu.back.pojo.SensorLuojing;
import nnu.edu.back.pojo.SensorZhuoduyi;

import java.util.Date;
import java.util.List;

public interface SensorDataService {
    List<SensorAdcp> getAdcpDataByTimeRange(String startTime, String endTime);

    List<SensorAdcp> getAllAdcpData();
    
    List<SensorChaoweiyi> getChaoweiyiDataByStationAndTime(String startTime, String endTime);

    List<SensorChaoweiyi> getAllChaoweiyiData();
    
    List<SensorLuojing> getLuojingDataByTimeRange(String startTime, String endTime);

    List<SensorLuojing> getAllLuojingData();
    
    List<SensorZhuoduyi> getZhuoduyiDataByTimeRange(String startTime, String endTime);

    List<SensorZhuoduyi> getAllZhuoduyiData();
}