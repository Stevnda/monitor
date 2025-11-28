package nnu.edu.back.pojo;

import lombok.Data;

@Data
public class SensorZhuoduyi {
    private Long id;
    private String serialNumber;
    private String model;
    private Double internalVoltage;
    private Double externalVoltage;
    private Double temperature;
    private Double pressure;
    private Double turbidity;
    private Double depth;
    private Double ssc;
    private String timestamp;
}