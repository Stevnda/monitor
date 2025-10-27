<template>
  <div class="viewer-header">
    <div class="logo-text" @click="routeHome">NHRI</div>
    <div class="clock">
      <p class="time">{{ curTime.toLocaleTimeString() }}</p>
    </div>
    <div class="title-text-content">
      <div class="title-wrap-left">
        <svg
          class="title-svg-left"
          x="0"
          y="0"
          width="100%"
          height="100%"
          viewBox="0 0 600 600"
          preserveAspectRatio="none"
        >
          <path
            fill="rgb(6, 60, 112)"
            fill-opacity="1"
            d="M 600 0 Q 550 0 500 300 Q 450 600 0 600 L 600 600 Z"
          ></path>
        </svg>
      </div>
      <div class="title-text">
        <div class="title-text-up">工程监测可视化</div>
        <div class="title-text-bot">{{ currentProject?.projectName }}</div>
      </div>
      <div class="title-wrap-right">
        <svg
          class="title-svg-right"
          x="0"
          y="0"
          width="100%"
          height="100%"
          viewBox="0 0 600 600"
          preserveAspectRatio="none"
        >
          <path
            fill="rgb(6, 60, 112)"
            fill-opacity="1"
            d="M 0 0 Q 50 0 100 300 Q 150 600 600 600 L 0 600 Z"
          ></path>
        </svg>
      </div>
    </div>
    <div class="selector-container" :class="{ active: expanded }">
      <el-select
        v-model="currentProject"
        value-key="id"
        placeholder="请选择"
        size="large"
      >
        <el-option
          v-for="item in projectOptions"
          :key="item.id"
          :label="item.projectName"
          :value="item"
        >
          <span style="float: left">{{ item.projectName }}</span>
          <span style="float: right; color: var(--el-text-color-secondary)">
            {{ item.time }}</span
          >
        </el-option>
      </el-select>
    </div>
    <div class="selector-icon-container">
      <div
        class="selector-icon"
        :class="expanded ? 'squeeze' : 'expand'"
        @click="expandSelector"
      ></div>
      <!-- <div class="selector-icon squeeze" v-if="expanded"></div> -->
    </div>
  </div>
  <div class="content-container">
    <div class="content-container-wrapper">
      <Suspense>
        <chartContainer
          v-for="(chart, index) in chartObjects"
          :key="index"
          :chartId="chart.chartId"
          :styleType="chart.styleType"
          :order="chart.order"
          :project-id="currentProject.id"
          ref="chartConatinerRefs"
        />
      </Suspense>
      <Suspense>
        <doubleChartContainer
          v-for="(chart, index) in doubleChartObjects"
          :key="index"
          :chartId="chart.chartId"
          :styleType="chart.styleType"
          :order="chart.order"
          :project-id="currentProject.id"
        />
      </Suspense>
      <Suspense>
        <centerMap
          :mapId="mapIndex"
          order="4"
          :project-id="currentProject.id"
        ></centerMap>
      </Suspense>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";
import centerMap from "@/components/dataViewer/centerMap.vue";
import router from "@/router";
import chartContainer from "@/components/dataViewer/chartContainer.vue";
import doubleChartContainer from "@/components/dataViewer/doubleChartContainer.vue";
import { type ProjectOption } from "@/utils/viewerData";
import { getAllVisualProject, getFloatPointShape } from "@/api/request";

const projects = await getAllVisualProject();
console.log("projects", projects?.data);

let projectOptions = ref(projects?.data);

const chartConatinerRefs = ref<InstanceType<typeof chartContainer>[]>([]);

let currentProject = ref<ProjectOption>(projectOptions.value[0]);

console.log(
  "pt geojson",
  await getFloatPointShape(currentProject.value.id, "P1")
);
// const test = await getSandContentValue(currentProject.value.id, "LSSD-AD");
// console.log("test result", test);

let expanded = ref(false);

let expandSelector = () => {
  expanded.value = !expanded.value;
};

const curTime = ref(new Date());
const updataTime = (): void => {
  curTime.value = new Date();
  // currentProject.value.id += 1;
  // chartConatinerRefs.value[0].changeData();
};

let mapIndex = "0";

let chartObjects = [
  { chartId: "1", order: "1", styleType: "1" },
  { chartId: "2", order: "2", styleType: "1" },
  // {chartId: '3', order:'3', styleType:"2"},
  { chartId: "4", order: "3", styleType: "1" },
  { chartId: "5", order: "5", styleType: "3" },
  { chartId: "6", order: "6", styleType: "1" },
  // {chartId: '7', order:'8', styleType:"2"},
  // {chartId: '8', order:'9', styleType:"2"},
  { chartId: "9", order: "8", styleType: "1" },
];

let doubleChartObjects = [
  // {chartId: '23', order:'2', styleType:"1"},
  { chartId: "78", order: "7", styleType: "1" },
];

let routeHome = () => {
  router.push({ path: "/" });
};

let timeout: number;
onMounted(() => {
  timeout = setInterval(updataTime, 1000);
  // console.log(chartConatinerRefs.value[0].changeData());
});
onUnmounted(() => {
  clearInterval(timeout);
});
</script>

<style lang="scss" scoped>
$header-height: 10vh;
$content-height: calc(100vh - $header-height);
$title-text-height: calc($header-height/2);
$main-background-color: rgb(6, 60, 112);
$color-test: #c0dde2c1;

div.viewer-header {
  height: $header-height;
  width: 100vw;
  background-color: rgb(1, 15, 44);
  overflow: hidden;

  div.logo-text {
    position: absolute;
    width: fit-content;
    height: $header-height;
    left: 1.5vw;

    font-size: 6vh;
    line-height: $header-height;
    font-weight: 800;
    color: rgba(235, 235, 235, 0.5);

    background-position: left 1200px top 80px;
    -webkit-background-clip: text;
    background-repeat: repeat-x;
    background-image: url("../assets/sand-wave.png");
    animation: animate 15s linear infinite;

    &:hover {
      cursor: pointer;
    }
  }

  @keyframes animate {
    0% {
      background-position: left 0px top -400px;
    }
    20% {
      background-position: left 400px top -250px;
    }
    40% {
      background-position: left 800px top -250px;
    }
    60% {
      background-position: left 1200px top -300px;
    }
    80% {
      background-position: left 1600px top -300px;
    }
    100% {
      background-position: left 2000px top -400px;
    }
  }

  div.clock {
    text-align: center;
    position: absolute;
    left: 12vw;
    top: 0;
    line-height: 3vh;
    p {
      font-family: Impact, Haettenschweiler, "Arial Narrow Bold", sans-serif;
      color: rgba(255, 255, 255, 0.8);
      letter-spacing: 0.05em;
      font-size: 3vh;
      text-shadow: 0 0.5px 0 #ccc, 0 1px 0 #c9c9c9, 0 1.5px 0 #bbb,
        0 2px 0 #b9b9b9, 0 2.5px 0 #aaa, 0 3px 0.5px rgba(0, 0, 0, 0.1),
        0 0 2.5px rgba(0, 0, 0, 0.1), 0 0.5px 1.5px rgba(0, 0, 0, 0.3),
        0 1.5px 2.5px rgba(0, 0, 0, 0.2), 0 2.5px 5px rgba(0, 0, 0, 0.25),
        0 5px 5px rgba(0, 0, 0, 0.2), 0 10px 10px rgba(0, 0, 0, 0.15);
    }

    p {
      width: 100%;
      height: 100%;
      line-height: 3vh;
      text-align: center;
      vertical-align: middle;
    }
  }

  div.title-text-content {
    display: flex;
    position: absolute;
    width: fit-content;
    height: $header-height;
    margin: auto;
    left: 50%;
    transform: translate(-50%, 0);

    flex-flow: row nowrap;
    justify-content: center;
    overflow: hidden;
    background-color: rgb(1, 15, 44);

    div.title-wrap-left {
      width: 4vw;
      height: $header-height;
      background-color: rgb(1, 15, 44);
      overflow: hidden;
      box-shadow: 2px 0 0 1px $main-background-color;

      .title-svg-left {
        top: 0;
        left: 0;
        // width: 6vw;
        width: 4vw;
        height: $header-height;

        // height: $header-height;
        z-index: 10;
      }
    }
    div.title-wrap-right {
      width: 4vw;
      height: $header-height;
      background-color: rgb(1, 15, 44);
      overflow: hidden;
      box-shadow: inset 3px 0 0 -1px $main-background-color;
      .title-svg-right {
        top: 0;
        left: 0;
        // width: 6vw;
        width: 4vw;
        height: $header-height;

        // height: $header-height;
        z-index: 10;
      }
    }

    div.title-text {
      display: flex;
      flex-flow: column nowrap;
      justify-content: center;
      height: $header-height;
      width: fit-content;
      line-height: $header-height;
      text-align: center;
      color: rgb(202, 228, 228);
      // font-size: 4vh;
      font-weight: 600;
      letter-spacing: 0.5vh;
      overflow: hidden;
      background-color: $main-background-color;

      div.title-text-up {
        font-size: 4vh;
        line-height: $title-text-height;
        height: $title-text-height;

        background-image: linear-gradient(
          30deg,
          #ecfcff 0%,
          #b1feff 60%,
          #67fcff 100%
        );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
      }

      div.title-text-bot {
        font-size: 3vh;
        line-height: $title-text-height;
        height: $title-text-height;
        font-weight: 400;

        background-image: linear-gradient(
          30deg,
          #ffe4ae 0%,
          #d5af64 60%,
          #c89733 100%
        );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
      }
    }
  }

  div.selector-container {
    position: absolute;
    right: 2vw;
    top: 0;
    height: $header-height;
    overflow: hidden;

    .el-select {
      margin-top: $title-text-height;
      transform: translateY(-50%);
      right: -20vw;
      line-height: 2vh;
      height: 2vh;
      font-size: 1vh;
      transition: 0.5s ease-in-out;
    }

    &.active {
      .el-select {
        transform: translate(-20vw, -50%);
        transition: 0.5s ease-in-out;
      }
    }
  }

  div.selector-icon-container {
    position: absolute;
    right: 0;
    top: $title-text-height;
    transform: translateY(-50%);
    height: 4vh;
    width: 4vh;
    // background-color: rgba(240, 248, 255, 0.484);

    div.selector-icon {
      background-size: cover;
      background-repeat: no-repeat;
      background-position: center center;
      height: 4vh;
      width: 4vh;
      transition: 0.5s ease-in-out;
      background-image: url("../assets/arrow-left.png");

      &:hover {
        cursor: pointer;
      }
      &.squeeze {
        transform: rotateY(180deg);
      }
    }
  }
}
div.content-container {
  height: $content-height;
  width: 100vw;
  // background-color: $main-background-color;
  background-image: linear-gradient(#051e449e 0%, #033a88d7 60%, #074aa7d7 100%),
    url("../assets/gradient-bg3-rev.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
  z-index: 1;
  div.content-container-wrapper {
    position: absolute;
    height: $content-height;
    width: 100%;
    z-index: 2;
    backdrop-filter: blur(6px);

    display: flex;
    flex-flow: column wrap;
    justify-content: space-around;
    align-content: space-around;
  }
}
</style>
