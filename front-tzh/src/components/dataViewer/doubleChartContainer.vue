<template>
  <div
    class="part-container"
    :styleId="styleId"
    :order="order"
    :index="chartOptId"
  >
    <div class="double-chart-container">
      <div class="table-container left" ref="chartLeft">
        <dataTable :project-id="$props.projectId" />
      </div>
      <div class="chart right" ref="chartLeft"></div>
    </div>
    <div
      class="play-button"
      :class="{ paused: isPaused }"
      v-if="dynamicControl"
      :index="chartOptId"
      @click="changePlayStatus"
    ></div>
  </div>
</template>
  
<script lang="ts">
export default {
  name: "chartContainer",
};
</script>
  
<script setup lang='ts'>
import * as echarts from "echarts";
import {
  onActivated,
  onDeactivated,
  onMounted,
  onUnmounted,
  ref,
  watch,
} from "vue";
import { chartOptionTest, ChartDataPreparer } from "@/utils/viewerData";
import dataTable from "./dataTable.vue";

type EChartsOption = echarts.EChartsOption;

interface Props {
  chartId: string;
  order: string;
  styleType: string;
  projectId: string;
}

const props = defineProps<Props>();

const chartOptId = ref(props.chartId);
const styleId = ref(props.styleType);
const order = ref(props.order);
let projectId = ref(props.projectId);

const chartLeft = ref<HTMLElement>();
const chartRight = ref<HTMLElement>();

const chartDatapreparer = new ChartDataPreparer(
  +chartOptId.value.substring(0, 1)
);

let isPaused = ref(true);
let dynamicControl = ref(false);
let dynamicInterval = -1;
let changePlayStatus = () => {
  isPaused.value = !isPaused.value;
};
//   let noShown = ref(props.notShown);

//   watch(()=>props.notShown, (oldShown, newShown)=> {
//       noShown.value = !newShown;
//       console.log(noShown.value);
//   });

let optionIndex = 0;
let echartRight: echarts.ECharts;
const loadChartOfCurPorject = async (
  chart: echarts.ECharts,
  projectIdStr: string
) => {
  const chartOption = await chartDatapreparer.buildChartOption(projectIdStr);
  function arrayDynamic() {
    chart.setOption((chartOption as EChartsOption[])[optionIndex + 1]);
    optionIndex =
      (optionIndex + 1) % ((chartOption as EChartsOption[]).length - 1);
  }

  let reqeustDynamic = async () => {
    chartDatapreparer.dynamicIndex += 1;
    const newSeries = await chartDatapreparer.buildChartOption(projectIdStr);
    chart.clear();
    for (let key in newSeries) {
      (chartOption as EChartsOption)[key] = (newSeries as EChartsOption)[key];
    }
    // chartOption.series = (newSeries as EChartsOption).series;
    // console.log(chartOption);
    chart.setOption(chartOption as EChartsOption);
  };
  if (Array.isArray(chartOption)) {
    // console.log("options", chartOption)
    dynamicControl.value = true;
    chart.setOption(chartOption[0]);
    dynamicInterval = setInterval(arrayDynamic, 4000);
    changePlayStatus = () => {
      isPaused.value = !isPaused.value;
      if (dynamicInterval != -1) {
        clearInterval(dynamicInterval);
        dynamicInterval = -1;
      } else {
        dynamicInterval = setInterval(arrayDynamic, 4000);
      }
    };
  } else {
    // console.log("option", chartOption)
    chart.setOption(chartOption);
    if (chartDatapreparer.isDynamic === true) {
      dynamicControl.value = true;
      dynamicInterval = setInterval(reqeustDynamic, 4000);
      changePlayStatus = () => {
        isPaused.value = !isPaused.value;
        if (dynamicInterval != -1) {
          clearInterval(dynamicInterval);
          dynamicInterval = -1;
        } else {
          dynamicInterval = setInterval(reqeustDynamic, 4000);
        }
      };
    }
  }
};

onMounted(async () => {
  // console.log(chartDom.value);
  // let echartLeft = echarts.init(chartLeft.value as HTMLElement);
  echartRight = echarts.init(chartLeft.value as HTMLElement);
  await loadChartOfCurPorject(echartRight, projectId.value);

  // TODO: window.onsize doesn't work on components
  // window.onresize = function () {
  //     chart.resize();
  // };
});

onActivated(async () => {
  console.log("activated");
  await loadChartOfCurPorject(echartRight, projectId.value);
});

onDeactivated(() => {
  if (dynamicInterval != -1) {
    clearInterval(dynamicInterval);
  }
});

onUnmounted(() => {
  if (dynamicInterval != -1) {
    clearInterval(dynamicInterval);
  }
});
</script>
  
<style lang='scss' scoped>
$orders: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10;
$playButtonSize: calc(0.75vw + 0.8vh);

div.part-container {
  width: 30%;
  height: 32%;
  border-radius: 3px;
  background-color: transparent;
  overflow: hidden;
  // backdrop-filter: blur(4px);
  flex-grow: 0;

  &[styleId="1"] {
    width: 28%;
    height: 32%;
  }

  &[styleId="2"] {
    width: 14%;
    height: 22%;
  }

  &[styleId="3"] {
    height: 32%;
    width: 38%;
  }

  @each $order in $orders {
    &[order="#{$order}"] {
      order: $order;
    }
  }

  div.double-chart-container {
    width: 100%;
    height: 100%;
    border-radius: 3px;
    transition: transform 1s ease-in-out;
    display: flex;
    overflow: hidden;
    flex-flow: row no wrap;
    justify-content: space-around;

    div {
      canvas {
        border-radius: 3px;
      }
    }

    div.chart {
      width: 49%;
      height: 100%;
      background-color: rgba(0, 1, 67, 0.5);
      border-radius: 3px;

      div {
        canvas {
          border-radius: 3px;
        }
      }
    }

    div.table-container {
      width: 49%;
      height: 100%;
      background-color: rgba(0, 1, 67, 0.5);
      border-radius: 3px;
      margin-right: 0;

      div {
        canvas {
          border-radius: 3px;
        }
      }
    }
  }
  div.play-button {
    position: absolute;
    right: 1%;
    top: 35%;
    border: 0;
    background: transparent;
    box-sizing: border-box;
    width: $playButtonSize;
    height: $playButtonSize;
    // background-color: aqua;
    border-color: transparent transparent transparent #4a8cfd;
    transition: 100ms all ease;
    cursor: pointer;

    border-style: solid;
    border-width: calc($playButtonSize/2) 0 calc($playButtonSize/2)
      calc($playButtonSize * 0.8);

    &.paused {
      border-style: double;
      border-width: 0px 0 0px calc($playButtonSize * 0.8);
    }

    &:hover {
      border-color: transparent transparent transparent #60ffdf;
    }

    &[index="9"] {
      right: 32%;
    }
  }
}
</style>