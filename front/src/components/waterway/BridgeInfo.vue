<template>
  <div :class="active ? 'bridge-info active' : 'bridge-info'">
    <div class="title">
      <strong>桥梁信息</strong>
      <div class="close">
        <el-button type="info" link @click="closeClick"
          ><el-icon><CircleClose /></el-icon
        ></el-button>
      </div>
    </div>
    <div class="content">
      <div class="bridge-name">
        {{ bridgeInfo === undefined ? "" : bridgeInfo["bridgeName"] }}
      </div>
      <div class="info">
        <el-scrollbar>
          <el-descriptions border :column="1">
            <el-descriptions-item
              label="桥区水域范围"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["bridgeAreaRange"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥涵标"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["bridgeCulvertMarker"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域水上航路航法"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["navRulesWaterRoutes"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="航评批复文件"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["navAssessmentApproval"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="海事管理机构"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["maritimeAdminAgency"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁属性"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["bridgeAttributes"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="设计通航代表船型、船队"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["designNavRepTypeFleet"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥柱灯"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["bridgePillarLights"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="区段通航代表船型、船队"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["sectionNavRepTypeFleet"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁主动防撞设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["bridgeActiveCollisionPrev"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁运行管理单位"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["bridgeOpMgmtUnit"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="通车时间"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["openingDate"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥梁被动防撞设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["bridgePassiveCollisionPrev"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="日均断面流量"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["avgDailyCrossFlow"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区视频设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["bridgeAreaVideoFacilities"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="已建立的桥区水域水上交通安全管理制度"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["waterTrafficSafetyMgmt"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内临河设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["riverFacilitiesInArea"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="主通航孔编号"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["mainNavSpanNumber"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内锚地、停泊区"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["anchorageBerthingArea"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水域内跨河设施"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["crossRiverFacilities"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="桥区水上航标布置"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["waterNavMarksArrangement"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="小轮通航孔编号"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["smallVesselNavSpanNumber"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="航道养护单位"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined ? "" : bridgeInfo["navMaintenanceUnit"]
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label="主通航孔桥墩防船撞能力"
              label-align="center"
              align="center"
              >{{
                bridgeInfo === undefined
                  ? ""
                  : bridgeInfo["mainNavPierCollisionPrev"]
              }}</el-descriptions-item
            >
          </el-descriptions>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, PropType, ref } from "vue";
import { BridgeType } from "@/type";
export default defineComponent({
  props: {
    bridgeInfo: {
      type: Object as PropType<BridgeType>,
    },
  },
  setup(props) {
    
    const active = ref(false);

    const bridgeInfo = computed(() => {
      return props.bridgeInfo;
    });

    const popupClick = () => {
      active.value = true;
    };

    const closeClick = () => {
      active.value = false;
    };

    return {
      active,
      popupClick,
      bridgeInfo,
      closeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.bridge-info {
  position: absolute;
  left: 0vw;
  width: 360px;
  height: 74vh;
  top: 100px;
  z-index: 99;
  border-top-left-radius: 6px;
  border-bottom-left-radius: 6px;
  transform: translateX(-400px);
  background: white;
  &.active {
    transform: translateX(0);
  }
  transition: transform 0.4s ease-in-out;
  padding: 20px;

  .title {
    color: #1e88e5;
    margin-bottom: 10px;
    height: 20px;
    position: relative;
    .close {
      position: absolute;
      right: 0px;
      top: 0px;
    }
  }
  .content {
    width: 340px;
    padding: 10px;
    border: solid 1px #c8c8c8;
    .bridge-name {
      margin-bottom: 10px;
      font-weight: 400;
    }
    .info {
      text-align: center;
      height: calc(74vh - 100px);
      .el-scrollbar {
        height: 100%;
      }
    }
  }
}
</style>