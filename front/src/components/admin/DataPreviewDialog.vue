<template>
  <div>
    <div class="head">数据预览</div>
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div style="width: 900px" v-if="mapVisualFlag">
      <map-visual
        :shpArray="shpArray"
        :movePngArray="movePngArray"
        :pngArray="pngArray"
        :rasterTileArray="rasterTileArray"
      />
    </div>
    <div style="width: 900px" v-if="excelVisualFlag">
      <excel-visual
        :tableNameList="tableNameList"
        :sandContentList="sandContentList"
        :suspensionList="suspensionList"
        :rateDirectionList="rateDirectionList"
        :salinityList="salinityList"
        :flowSandZList="flowSandZList"
      />
    </div>
    <div v-if="photoVisualFlag">
      <photo-visual :photoList="photoList" />
    </div>
    <div v-if="videoVisualFlag">
      <video-visual :urls="videoURLs" />
    </div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, ref, PropType, onMounted } from "vue";
import MapVisual from "@/components/visual/MapVisual.vue";
import ExcelVisual from "@/components/visual/ExcelVisual.vue";
import { getCoordinates, getVisualFileByVisualId } from "@/api/request";
import PhotoVisual from "@/components/visual/PhotoVisual.vue";
import VideoVisual from "@/components/visual/VideoVisual.vue";
import { FileType } from "@/type";
import "mapbox-gl/dist/mapbox-gl.css";

export default defineComponent({
  props: {
    fileInfo: {
      type: Object as PropType<FileType>,
    },
  },
  components: {
    MapVisual,
    ExcelVisual,
    PhotoVisual,
    VideoVisual,
  },
  setup(props) {
    const skeletonFlag = ref(true);
    const mapVisualFlag = ref(false);
    const excelVisualFlag = ref(false);
    const photoVisualFlag = ref(false);
    const videoVisualFlag = ref(false);

    const videoURLs = ref<{ fileName: string; url: string }[]>([]);
    const shpArray = ref<
      {
        visualId: string;
        type: "line" | "fill" | "circle";
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const movePngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const pngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const rasterTileArray = ref<
      { visualId: string; view: { zoom: number; center: number[] } }[]
    >([]);
    const tableNameList = ref<string[]>([]);
    const sandContentList = ref<string[]>([]);
    const suspensionList = ref<string[]>([]);
    const rateDirectionList = ref<string[]>([]);
    const salinityList = ref<string[]>([]);
    const flowSandZList = ref<string[]>([]);
    const photoList = ref<string[]>([]);

    const initVisual = async () => {
      let view = {
        zoom: 0,
        center: [],
      };
      if (props.fileInfo?.visualId) {
        const visualFile = await getVisualFileByVisualId(
          props.fileInfo.visualId
        );
        view = visualFile?.data.view ? JSON.parse(visualFile?.data.view) : view;
      }
      //获取file文件的可视化方法
      let MapFlag = false;
      let photoFlag = false;
      let excelFlag = false;
      let videoFlag = false;
      if (props.fileInfo) {
        if (
          props.fileInfo.visualType === "lineVectorTile3D" ||
          props.fileInfo.visualType === "lineVectorTile"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "line",
            view: view,
          });
          MapFlag = true;
        }
        if (
          props.fileInfo.visualType === "pointVectorTile" ||
          props.fileInfo.visualType === "pointVectorTile3D"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "circle",
            view: view,
          });
          MapFlag = true;
        }
        if (
          props.fileInfo.visualType === "polygonVectorTile" ||
          props.fileInfo.visualType === "polygonVectorTile3D"
        ) {
          shpArray.value.push({
            visualId: props.fileInfo.visualId,
            type: "fill",
            view: view,
          });
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "rasterTile") {
          rasterTileArray.value.push({
            visualId: props.fileInfo.visualId,
            view: view,
          });
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "png") {
          const coordinates = await getCoordinates(props.fileInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            pngArray.value.push({
              visualId: props.fileInfo.visualId,
              coordinates: coordinates.data,
              view: view,
            });
          }
          MapFlag = true;
        }
        if (props.fileInfo.visualType == "movePng") {
          const coordinates = await getCoordinates(props.fileInfo.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            movePngArray.value.push({
              visualId: props.fileInfo.visualId,
              coordinates: coordinates.data,
              view: view,
            });
          }
          MapFlag = true;
        }

        if (props.fileInfo.visualType === "photo") {
          photoList.value.push(`/monitor/visual/getPhoto/${props.fileInfo.id}`);
          photoFlag = true;
        }
        if (props.fileInfo.visualType === "video") {
          videoURLs.value.push({
            url: `/monitor/visual/video/${props.fileInfo.id}`,
            fileName: props.fileInfo.fileName,
          });
          videoFlag = true;
        }
        if (props.fileInfo.visualType === "sandContent") {
          sandContentList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "suspension") {
          suspensionList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "rateDirection") {
          rateDirectionList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "salinity") {
          salinityList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
        if (props.fileInfo.visualType === "flowSand_Z") {
          flowSandZList.value.push(props.fileInfo.visualId);
          tableNameList.value.push(props.fileInfo.fileName);
          excelFlag = true;
        }
      }
      mapVisualFlag.value = MapFlag;
      excelVisualFlag.value = excelFlag;
      photoVisualFlag.value = photoFlag;
      videoVisualFlag.value = videoFlag;
      skeletonFlag.value = false;
    };

    onMounted(async () => {
      await initVisual();
    });

    return {
      skeletonFlag,
      mapVisualFlag,
      excelVisualFlag,
      shpArray,
      movePngArray,
      pngArray,
      rasterTileArray,
      tableNameList,
      sandContentList,
      suspensionList,
      rateDirectionList,
      salinityList,
      flowSandZList,
      photoVisualFlag,
      photoList,
      videoVisualFlag,
      videoURLs,
    };
  },
});
</script>
  
  <style lang="scss" scoped>
.head {
  font-size: 30px;
  color: royalblue;
  font-weight: 600;
  letter-spacing: -1px;
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 50px;
  padding-top: 20px;
  padding-bottom: 20px;
}

.head::before,
.head::after {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  border-radius: 50%;
  left: 20px;
  background-color: royalblue;
}

.head::after {
  width: 18px;
  height: 18px;
  animation: pulse 1s linear infinite;
}

@keyframes pulse {
  from {
    transform: scale(0.9);
    opacity: 1;
  }
  to {
    transform: scale(1.8);
    opacity: 0;
  }
}
</style>