<template>
  <div id="main">
    <div id="pop" v-show="false"></div>
    <div
      v-for="(station, index) in regionStationData"
      :id="station.name"
      v-show="false"
      :key="index"
    ></div>
    <div id="container"></div>
    <div class="legend" v-show="!chartInited">
      <div class="legend-sub legend-one">
        <span>上游水位</span>
      </div>
      <div class="legend-sub legend-two">
        <span>下游水位</span>
      </div>
    </div>
    <div class="over-look" @click="zoomOut"></div>
  </div>
</template>
<script lang="ts" setup>
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import {
  onMounted,
  createApp,
  App,
  ComponentPublicInstance,
  ref,
  reactive,
  Ref,
} from "vue";
import axios from "axios";
import * as turf from "@turf/turf";
import PopupVisual from "@/components/visual/popupVisual.vue";
import popUpChart from "@/components/dataViewer/popUpChart.vue";
import { WaterStation } from "@/type";
import {
  getPredictionStation,
  getAllPredictionValue,
  getRegionTideStation,
  // getWaterLevelByStationAndTime,
} from "@/api/request";
import {
  // type StringKeyObject,
  convertRegionTideStationData2Geojson,
} from "@/utils/viewerData";
import { number } from "echarts";
import { arrayToGlsl } from "ol/style/expressions";
// import LoginView from "./LoginView.vue";
import { styleDark } from "@/utils/mapStyleJson";

type PopupChartProps = {
  stationName: string;
  stationType: string;
  show: Ref<boolean>;
  curTimeStr: string;
  ysdTimeStr: string;
  chartWidth: Ref<number>;
  chartHeight: Ref<number>;
};

type StringKeyPopProps = {
  [key: string]: PopupChartProps;
};

const popUpChartsPropRefs: StringKeyPopProps = {};

const flag = ref(false);

const lng = ref(0);
const lat = ref(0);
const WaterVarying = ref([]);
const nearStation_1 = reactive({ name: "null", lng: 0, lat: 0, water: [] });
const nearStation_2 = reactive({ name: "null", lng: 0, lat: 0, water: [] });
// const info = reactive({ lnglat: { lng: 0, lat: 0 }, water: 0, nearStations: [] })
const pop = new mapboxgl.Popup({ maxWidth: "400px" });
pop.addClassName("pop-up");

let ap: App | null = null;
let apd: ComponentPublicInstance | null = null;

let popUpChartsApps: Map<string, App> = new Map();
let popUpChartsAppIns: Map<string, InstanceType<typeof popUpChart>> = new Map();
let popUpMap: Map<string, mapboxgl.Popup> = new Map();

// let CJ_Layerdata: any = null;

const stations: Map<string, WaterStation> = new Map();
var sysTime: {
  time: string;
  year: number;
  month: number;
  day: number;
  hours: number;
} = {
  time: "",
  year: 0,
  month: 0,
  day: 0,
  hours: 0,
};

const initStationData = async () => {
  const backend_stations = await getPredictionStation();
  const backend_values = await getAllPredictionValue();

  const date = new Date();
  sysTime.month = date.getMonth();
  sysTime.day = date.getDate();
  sysTime.hours = date.getHours() + 1;
  sysTime.year = date.getFullYear();

  sysTime.time = sysTime.time + date.getFullYear() + "-";
  sysTime.time = sysTime.time + date.getMonth() + "-";
  sysTime.time = sysTime.time + date.getDate() + " ";
  sysTime.time = sysTime.time + sysTime.hours + ":00:00";

  // console.log(sysTime);

  const stDATA = backend_stations?.data;

  for (let i = 0; i < stDATA.length; i++) {
    stations!.set(stDATA[i].nameEn, {
      name: stDATA[i].name,
      nameEn: stDATA[i].nameEn,
      lng: stDATA[i].longitude,
      lat: stDATA[i].latitude,
      water: [],
    });
  }
  const waterData = backend_values?.data;
  for (let i = 0; i < waterData.length; i++) {
    //console.log(waterData[i].name);//this name is nameEn
    for (let j = 0; j < 12; j++) {
      // stations!.get(waterData[i].name)!.water = waterData[i].res.value[0];
      stations!.get(waterData[i].name)!.water![j] = waterData[i].res.value[j];
    }
  }
};

const getTimeStr = (timer: Date) => {
  const year = timer.getFullYear();
  const month = timer.getMonth() + 1; // 由于月份从0开始，因此需加1
  const day = timer.getDate();
  const hour = timer.getHours();
  const minute = timer.getMinutes();
  const second = timer.getSeconds();
  return `${pad(year, 4)}-${pad(month)}-${pad(day)} ${pad(hour)}:${pad(
    minute
  )}:${pad(second)}`;
};

function pad(timeEl: number, total = 2, str = "0") {
  return timeEl.toString().padStart(total, str);
}
const curTime = new Date();
const curTimeStr = getTimeStr(curTime);
const ysdTime = new Date(+curTime - 24 * 3600 * 1000);
const ysdTimeStr = getTimeStr(ysdTime);

const markerHeight = 80;
const markerRadius = 10;
const linearOffset = 25;
const popupOffsets = {
  top: [0, markerHeight],
  "top-left": [0, 0],
  "top-right": [0, 0],
  bottom: [0, -markerHeight],
  "bottom-left": [
    linearOffset,
    (markerHeight - markerRadius + linearOffset) * -1,
  ],
  "bottom-right": [
    -linearOffset,
    (markerHeight - markerRadius + linearOffset) * -1,
  ],
  left: [markerRadius, (markerHeight - markerRadius) * -1],
  right: [-markerRadius, (markerHeight - markerRadius) * -1],
};
let zoomOut = () => {};

const initMap = async (map: mapboxgl.Map) => {
  await initStationData();

  map.on("load", async () => {
    // 添加长江面图层
    axios
      .get("/resource/changjiang2.geojson")
      .then((res) => {
        let feature = res.data.features[0];

        map.addSource("CJ", {
          type: "geojson",
          data: feature,
        });

        map.addLayer({
          id: "CJLayer",
          type: "fill",
          source: "CJ",
          paint: {
            "fill-color": "blue",
            "fill-opacity": 0,
          },
        });

        map.on("click", "CJLayer", (e) => {
          pop.remove();
          flag.value = true;
          const nearStation = findNearStation(e.lngLat.lng, e.lngLat.lat);

          const hereWater = Interpolate(
            nearStation!,
            e.lngLat.lng,
            e.lngLat.lat
          );

          showInfoWindow(
            map,
            e.lngLat.lng,
            e.lngLat.lat,
            hereWater!,
            nearStation!
          );
        });
        map.on("mouseenter", "CJLayer", () => {
          map.getCanvas().style.cursor = "crosshair";
        });

        map.on("mouseleave", "CJLayer", () => {
          // pop.remove();
          flag.value = false;
          map.getCanvas().style.cursor = "";
        });
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

const regionStation = await getRegionTideStation();
const regionStationData = regionStation?.data;
// console.log('stations', regionStation)

let chartInited = ref(false);
const initStationsLayer = (map: mapboxgl.Map) => {
  const regionStationGeojson =
    convertRegionTideStationData2Geojson(regionStationData);

  map.loadImage("/resource/tideStation.png", (error, image) => {
    if (error) throw error;

    map.addImage("tideStation", image as HTMLImageElement | ImageBitmap);

    map.addSource("stations", {
      type: "geojson",
      data: regionStationGeojson as any,
    });

    map.addLayer({
      id: "stations-icon",
      type: "symbol",
      source: "stations",
      layout: {
        "icon-image": "tideStation",
        "icon-size": [
          "interpolate",
          ["linear"],
          ["zoom"],
          0,
          0.007,
          5,
          0.0125,
          10,
          0.128,
          22,
          0.5,
        ],
        "icon-allow-overlap": true,
      },
    });

    map.addLayer({
      id: "station-label",
      type: "symbol",
      source: "stations",
      layout: {
        "text-field": ["format", ["get", "name"], { "font-scale": 0.8 }],
        "text-variable-anchor": ["bottom", "bottom-left", "bottom-right"],
        "text-radial-offset": [
          "interpolate",
          ["linear"],
          ["zoom"],
          2,
          0.5,
          5,
          2,
          10,
          2,
          22,
          4,
        ],
        "text-size": [
          "interpolate",
          ["linear"],
          ["zoom"],
          2,
          0,
          5,
          10,
          10,
          18,
          22,
          56,
        ],
        "text-font": ["Open Sans Bold"],
      },
      paint: {
        "text-color": "#ebe5ff",
        "text-halo-color": "#f0800f",
        "text-halo-width": [
          "interpolate",
          ["linear"],
          ["zoom"],
          1,
          0,
          5,
          0.2,
          9,
          0.1,
          10,
          0.5,
          22,
          1,
        ],
        "text-halo-blur": 0.3,
      },
    });

    map.on("mouseenter", "stations-icon", () => {
      map.getCanvas().style.cursor = "pointer";
    });
    map.on("mouseleave", "stations-icon", () => {
      map.getCanvas().style.cursor = "";
    });

    map.on("click", "stations-icon", (e) => {
      map.flyTo({
        zoom: 12.6,
        center: e.lngLat,
        essential: true,
      });
    });
  });

  for (let aStation of regionStationGeojson.features) {
    popUpChartsPropRefs[aStation.properties.name] = {
      stationName: aStation.properties.name,
      stationType: aStation.properties.type,
      show: ref(true),
      curTimeStr,
      ysdTimeStr,
      chartWidth: ref(100),
      chartHeight: ref(60),
    };
    popUpChartsApps.set(
      aStation.properties.name,
      createApp(popUpChart, popUpChartsPropRefs[aStation.properties.name])
    );
    popUpChartsAppIns.set(
      aStation.properties.name,
      popUpChartsApps
        .get(aStation.properties.name)
        ?.mount("#" + aStation.properties.name) as InstanceType<
        typeof popUpChart
      >
    );
    let popChart = new mapboxgl.Popup({
      maxWidth: "1000px",
      closeOnClick: false,
      offset: popupOffsets as any,
    });
    popChart.addClassName("pop-chart");
    popChart
      .setLngLat(aStation.geometry.coordinates)
      .setDOMContent(popUpChartsAppIns.get(aStation.properties.name)?.$el)
      .addTo(map);
    popUpMap.set(aStation.properties.name, popChart);
  }

  map.on("zoom", (e) => {
    const zoom = map.getZoom();
    if (zoom < 7.5) {
      for (let popUp of popUpMap.values()) {
        if (popUp.isOpen()) {
          popUp.remove();
        }
      }
    } else {
      for (let popUp of popUpMap.values()) {
        if (!popUp.isOpen()) {
          popUp.addTo(map);
        }
      }
      if (zoom < 10) {
        for (let station of regionStationData) {
          popUpChartsPropRefs[station.name].chartWidth.value = zoom * 10;
          popUpChartsPropRefs[station.name].chartHeight.value = zoom * 6;
          // console.log(zoom, popUpChartsPropRefs[station.name].chartWidth.value)
        }
        if (
          chartInited.value &&
          popUpChartsPropRefs[regionStationData[0].name].chartWidth.value < 240
        ) {
          // console.log("remove")
          for (let station of regionStationData) {
            popUpChartsAppIns.get(station.name)?.toggleChartStatus();
          }
          chartInited.value = false;
        }
      } else if (zoom < 12) {
        for (let station of regionStationData) {
          popUpChartsPropRefs[station.name].chartWidth.value = zoom * 24;
          popUpChartsPropRefs[station.name].chartHeight.value = zoom * 16;
          // console.log(zoom, popUpChartsPropRefs[station.name].chartWidth.value)
        }
        if (
          chartInited.value &&
          popUpChartsPropRefs[regionStationData[0].name].chartWidth.value < 240
        ) {
          // console.log("remove")

          for (let station of regionStationData) {
            popUpChartsAppIns.get(station.name)?.toggleChartStatus();
          }
          chartInited.value = false;
        } else if (
          !chartInited.value &&
          popUpChartsPropRefs[regionStationData[0].name].chartWidth.value >= 240
        ) {
          for (let station of regionStationData) {
            popUpChartsAppIns.get(station.name)?.toggleChartStatus();
          }
          chartInited.value = true;
        }
      } else {
        for (let station of regionStationData) {
          popUpChartsPropRefs[station.name].chartWidth.value = zoom * 42;
          popUpChartsPropRefs[station.name].chartHeight.value = zoom * 28;
          // console.log(zoom, popUpChartsPropRefs[station.name].chartWidth.value)
        }
        if (!chartInited.value) {
          for (let station of regionStationData) {
            popUpChartsAppIns.get(station.name)?.toggleChartStatus();
          }
          chartInited.value = true;
        }
      }
    }
    if (chartInited.value) {
      for (let station of regionStationData) {
        popUpChartsAppIns.get(station.name)?.resizeEchart();
      }
    }
  });
  // console.log(popUpChartsAppIns);
};

const findNearStation = (lng: number, lat: number) => {
  //find near station
  //request water

  //循环，求最近的两站点
  let from, to;
  let minDis1 = 100000,
    minDis2 = 100000;
  let minName1: string, minName2: string, tempName: string;
  from = turf.point([lng, lat]);

  for (let item of stations) {
    // console.log(item);
    to = turf.point([item[1].lng, item[1].lat]);
    let dis = turf.distance(from, to);
    if (dis < minDis1) {
      minDis1 = dis;
      minName1 = item[0];
    }
  }

  //request station water
  let station1: WaterStation = {
    nameEn: minName1!,
    name: stations.get(minName1!)!.name,
    lng: stations.get(minName1!)!.lng,
    lat: stations.get(minName1!)!.lat,
    water: stations.get(minName1!)!.water,
  };

  for (let item of stations) {
    to = turf.point([item[1].lng, item[1].lat]);
    let dis = turf.distance(from, to);
    if (dis < minDis2 && dis > minDis1) {
      let line = turf.lineString([
        [station1.lng, station1.lat],
        [item[1].lng, item[1].lat],
      ]);
      let snapped = turf.nearestPointOnLine(line, from, { units: "miles" });
      if (
        (snapped.geometry.coordinates[0] === station1.lng &&
          snapped.geometry.coordinates[1] === station1.lat) ||
        (snapped.geometry.coordinates[0] === to.geometry.coordinates[0] &&
          snapped.geometry.coordinates[1] === to.geometry.coordinates[1])
      ) {
        //没有找到两侧的站点,snapped == to 或者 station1
        tempName = item[0]; //特殊情况下，用该值
        continue;
      } else {
        minDis2 = dis;
        minName2 = item[0];
      }
    }
  }

  let station2: WaterStation;
  if (minDis2 === 100000) {
    // console.log('ERROR::Not find the 2nd station in another side');
    station2 = {
      nameEn: tempName!,
      name: stations.get(tempName!)!.name,
      lng: stations.get(tempName!)!.lng,
      lat: stations.get(tempName!)!.lat,
      water: stations.get(tempName!)?.water,
    };
    return [station1, station2];
  }

  station2 = {
    nameEn: minName2!,
    name: stations.get(minName2!)!.name,
    lng: stations.get(minName2!)!.lng,
    lat: stations.get(minName2!)!.lat,
    water: stations.get(minName2!)?.water,
  };
  return [station1, station2];
};

const Interpolate = (S: WaterStation[], herelng: number, herelat: number) => {
  //三角形计算
  let P = turf.point([herelng, herelat]);
  let A = turf.point([S[0].lng, S[0].lat]);
  let B = turf.point([S[1].lng, S[1].lat]);

  let AB_line = turf.lineString([
    [S[0].lng, S[0].lat],
    [S[1].lng, S[1].lat],
  ]);
  let snapped = turf.nearestPointOnLine(AB_line, P, { units: "miles" });

  let AS_dis = turf.distance(A, snapped);
  let BS_dis = turf.distance(B, snapped);

  let result = new Array(12);
  // if (AS_dis === 0) {
  //   result = S[0].water;
  // } else if (BS_dis === 0) {
  //   result = S[1].water;
  // } else {
  //   result =
  //     (S[0].water! * BS_dis) / (AS_dis + BS_dis) +
  //     (S[1].water! * AS_dis) / (AS_dis + BS_dis);
  // }

  for (let i = 0; i < 12; i++) {
    if (AS_dis === 0) {
      result[i] = S[0].water![i];
    } else if (BS_dis === 0) {
      result[i] = S[1].water![i];
    } else {
      result[i] =
        (S[0].water![i] * BS_dis) / (AS_dis + BS_dis) +
        (S[1].water![i] * AS_dis) / (AS_dis + BS_dis);
    }
  }

  return result;
};

const showInfoWindow = (
  map: mapboxgl.Map,
  elng: number,
  elat: number,
  ewater: Array<number>,
  enearStations: WaterStation[]
) => {
  lng.value = Number(elng.toFixed(6));
  lat.value = Number(elat.toFixed(6));
  WaterVarying.value = ewater;
  nearStation_1.name = enearStations[0].name!;
  nearStation_1.lng = Number(enearStations[0].lng.toFixed(6));
  nearStation_1.lat = Number(enearStations[0].lat.toFixed(6));
  // nearStation_1.water = Number(enearStations[0].water!.toFixed(6));
  nearStation_1.water = enearStations[0].water! as never[];

  nearStation_2.name = enearStations[1].name!;
  nearStation_2.lng = Number(enearStations[1].lng.toFixed(6));
  nearStation_2.lat = Number(enearStations[1].lat.toFixed(6));
  // nearStation_2.water = Number(enearStations[1].water!.toFixed(6));
  nearStation_2.water = enearStations[1].water! as never[];

  pop.setLngLat([elng, elat]).setDOMContent(apd!.$el).addTo(map);
};

onMounted(async () => {
  const dom = document.getElementById("container");
  dom!.style.background = styleDark.background;
  const map = new mapboxgl.Map({
    container: "container",
    // style: "mapbox://styles/johnnyt/clld6armr00f901q0dyqh7452",
    style:styleDark.styleJson as any,
    center: [120.001, 31.8813],
    zoom: 9.05,
    accessToken:
      "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg",
  });
  zoomOut = () => {
    map.flyTo({
      center: [120.001, 31.8813],
      zoom: 9.05,
      essential: true,
    });
  };
  await initMap(map);
  let info = {
    flag: flag,
    lng: lng,
    lat: lat,
    water: WaterVarying,
    station_1: nearStation_1,
    station_2: nearStation_2,
    startTime: sysTime,
  };
  ap = createApp(PopupVisual, info);
  apd = ap!.mount("#pop");

  initStationsLayer(map);

  // setInterval(() => {
  //     for(let station of regionStationData){
  //         popUpChartsPropRefs[station.name].chartWidth.value += 100;
  //         console.log(popUpChartsPropRefs[station.name].chartWidth.value)
  //     }
  // }, 5000)
  // ap!.unmount();
  //info 作为props创建了一个popupvisual实例，挂载到#pop上
  //解绑  成一个虚空的实例
  //再setDOMContent挂载到apd上
});
</script>
<style lang="scss">
#main {
  width: 100%;
  //   height: 100%;
  height: calc(100% - 5rem);

  #container {
    width: 100%;
    height: 100%;

    div.pop-chart {
      width: fit-content;
      height: fit-content;

      div.mapboxgl-popup-content {
        background-color: #cde5ffc8;
        backdrop-filter: blur(5px);
        padding: 3px 3px 3px 3px;
      }
    }

    div.pop-up {
      div.mapboxgl-popup-anchor-bottom {
        border-top-color: #b9d6f5d8; //no func
      }
      div.mapboxgl-popup-content {
        background-color: #cde5ffc8;
      }
    }
  }

  div.legend {
    position: absolute;
    right: calc(6vw + 2vh);
    bottom: 4.5vh;
    width: 8vw;
    height: 5vh;
    display: flex;
    flex-flow: row;
    border-radius: 6px;

    div.legend-sub {
      display: flex;
      width: 50%;
      height: 100%;
      align-items: center;
      justify-content: center;
      font-size: calc(0.5vw + 0.55vh);
      font-weight: 600;

      &.legend-one {
        background-color: rgb(21, 60, 168);
        color: rgb(234, 252, 255);
        border-top-left-radius: 6px;
        border-bottom-left-radius: 6px;
      }

      &.legend-two {
        background-color: rgb(73, 202, 231);
        color: rgb(0, 13, 42);
        border-top-right-radius: 6px;
        border-bottom-right-radius: 6px;
      }
    }
  }

  div.over-look {
    position: absolute;
    width: calc(2vw + 2vh);
    height: calc(2vw + 2vh);
    right: 2vw;
    bottom: 4vh;
    background-image: url("../assets/zoom-blue.png");
    background-size: cover;

    transition: all 0.5s ease-in-out;

    &:hover {
      cursor: pointer;
      width: calc(2.5vw + 2.5vh);
      height: calc(2.5vw + 2.5vh);
    }
  }
}
</style>