package nnu.edu.back.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class SensorLuojing {
    private Long id;
    private String timestamp;
    private String ggaData;
    private String hdtData;
    private String vtgData;
    private Integer gpsSatellites;
    private Integer glonassSatellites;
    private Integer beidouSatellites;
    private String createdAt;
}