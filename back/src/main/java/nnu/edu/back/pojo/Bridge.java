package nnu.edu.back.pojo;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/23:18
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bridge {
    String id;
    String bridgeAreaRange;
    String bridgeCulvertMarker;
    String navRulesWaterRoutes;
    String navAssessmentApproval;
    String maritimeAdminAgency;
    String bridgeAttributes;
    String designNavRepTypeFleet;
    String bridgePillarLights;
    String sectionNavRepTypeFleet;
    String bridgeActiveCollisionPrev;
    String bridgeOpMgmtUnit;
    String openingDate;
    String bridgePassiveCollisionPrev;
    String avgDailyCrossFlow;
    String bridgeAreaVideoFacilities;
    String waterTrafficSafetyMgmt;
    String riverFacilitiesInArea;
    String other;
    String mainNavSpanNumber;
    String anchorageBerthingArea;
    String crossRiverFacilities;
    String waterNavMarksArrangement;
    String smallVesselNavSpanNumber;
    JSONObject polygon;
    String navMaintenanceUnit;
    String bridgeName;
    String name;
    String mainNavPierCollisionPrev;

}
