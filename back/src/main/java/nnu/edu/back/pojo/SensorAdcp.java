package nnu.edu.back.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class SensorAdcp {
    private Long id;
    private String deviceTimestamp;
    private String headerData;
    private String fixedLeader;
    private String variableLeader;
    private String velocityData;
    private String correlationData;
    private String echoIntensity;
    private String percentGood;
    private String statusData;
    private String bottomTrack;
    private String ambientSound;
    private String ismData;
    private String checksum;
}