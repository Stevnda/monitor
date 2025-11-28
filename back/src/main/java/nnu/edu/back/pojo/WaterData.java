package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterData {
    private String swz_id;
    private String clsj; //采集时间
    private String clsw; //测量基面下水位
    private String scsw; //85基面下水位
    private String scsw56; //56基面下水位
    private String scswws; //吴淞基面下水位
    private String scswsd; //水位基面
    private String swzmc; //站点中文名
}