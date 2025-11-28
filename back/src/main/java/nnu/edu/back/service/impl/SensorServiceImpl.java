package nnu.edu.back.service.impl;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.dao.sensor.*;
import nnu.edu.back.pojo.SensorAdcp;
import nnu.edu.back.pojo.SensorChaoweiyi;
import nnu.edu.back.pojo.SensorLuojing;
import nnu.edu.back.pojo.SensorZhuoduyi;
import nnu.edu.back.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SensorServiceImpl implements SensorDataService {
    @Autowired
    private AdcpDataMapper adcpDataMapper;

    @Autowired
    private ChaoweiyiDataMapper chaoweiyiDataMapper;

    @Autowired
    private LuojingDataMapper luojingDataMapper;

    @Autowired
    private ZhuoduyiDataMapper zhuoduyiDataMapper;

    @Override
    public List<SensorAdcp> getAdcpDataByTimeRange(String startTime, String endTime) {
        return adcpDataMapper.getAdcpDataByTimeRange(startTime, endTime);
    }

    @Override
    public List<SensorAdcp> getAllAdcpData() {
        return adcpDataMapper.getAllAdcpData();
    }
    
    @Override
    public List<SensorChaoweiyi> getChaoweiyiDataByStationAndTime(String startTime, String endTime) {
        return chaoweiyiDataMapper.getChaoweiyiDataByTimeRange(startTime, endTime);
    }

    @Override
    public List<SensorChaoweiyi> getAllChaoweiyiData() {
        return chaoweiyiDataMapper.getAllChaoweiyiData();
    }
    
    @Override
    public List<SensorLuojing> getLuojingDataByTimeRange(String startTime, String endTime) {
        return luojingDataMapper.getLuojingDataByTimeRange(startTime, endTime);
    }

    @Override
    public List<SensorLuojing> getAllLuojingData() {
        return luojingDataMapper.getAllLuojingData();
    }
    
    @Override
    public List<SensorZhuoduyi> getZhuoduyiDataByTimeRange(String startTime, String endTime) {
        return zhuoduyiDataMapper.getZhuoduyiDataByTimeRange(startTime, endTime);
    }

    @Override
    public List<SensorZhuoduyi> getAllZhuoduyiData() {
        return zhuoduyiDataMapper.getAllZhuoduyiData();
    }
}