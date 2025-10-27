<template>
  <div class="search-component">
    <el-input v-model="inputValue" size="large" @focus="focusHandle">
      <template #prepend>
        <el-select
          v-model="selectValue"
          style="width: 120px"
          size="large"
          @change="changeHandle"
        >
          <el-option
            v-for="(item, index) in options"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </template>
      <template #append>
        <el-button @click="searchClick"
          ><el-icon><Search /></el-icon
        ></el-button>
      </template>
    </el-input>

    <div class="content" v-if="showFlag">
      <div>
        <div class="title">
          <span>{{ optionMap[selectValue] }}</span>
          <div class="switch" v-if="selectValue === 'ship'">
            <span>( </span>
            <el-switch
              v-model="switchValue"
              active-text="实时数据"
              inactive-text="模拟数据"
              @change="switchChange"
            />
            <span> )</span>
          </div>
          <el-icon @click="closeHandle"><Close /></el-icon>
        </div>
        <div>
          <el-skeleton :rows="5" animated v-if="skeletonFlag" />
          <el-table
            :data="tableData"
            border
            style="width: 100%"
            v-else
            @row-click="rowClickHnadle"
          >
            <el-table-column
              :label="item.label"
              v-for="(item, index) in propLabelMap[selectValue]"
              :key="index"
            >
              <template #default="scope">
                <span v-html="replaceHandle(scope.row[item.prop])"></span>
              </template>
            </el-table-column>
          </el-table>
          <div class="page">
            <el-pagination
              small
              background
              layout="total, prev, pager, next"
              :total="total"
              hide-on-single-page
              :pager-count="5"
              @current-change="pageChange"
              v-model:current-page="currentPage"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import {
  SearchTable,
  BuoyType,
  AnchorType,
  ParkType,
  BridgeType,
  StationType,
  ShipType,
} from "@/type";
import { pageList } from "@/api/request";
export default defineComponent({
  emits: ["returnPoint", "shipDataModeChange"],
  setup(_, context) {
    const inputValue = ref("");
    let keyword = "";
    const selectValue = ref("ship");
    const showFlag = ref(false);
    const skeletonFlag = ref(false);
    const tableData = ref<SearchTable[]>([]);

    const switchValue = ref(true);

    const currentPage = ref(1);
    const total = ref(0);

    const options = [
      {
        label: "AIS船舶",
        value: "ship",
      },
      {
        label: "航标",
        value: "buoy",
      },
      {
        label: "停泊区",
        value: "park",
      },
      {
        label: "锚地",
        value: "anchor",
      },
      {
        label: "桥梁",
        value: "bridge",
      },
      {
        label: "水位站点",
        value: "station",
      },
    ];
    const optionMap = {
      ship: "AIS船舶",
      buoy: "航标",
      park: "停泊区",
      anchor: "锚地",
      bridge: "桥梁",
      station: "水位站点",
    };
    const propLabelMap = {
      ship: [
        { prop: "mmsi", label: "mmsi" },
        { prop: "nameCn", label: "船舶名称" },
      ],
      buoy: [
        { prop: "waterway", label: "所属航道" },
        { prop: "name", label: "航标名称" },
      ],
      park: [
        { prop: "name", label: "停泊区名称" },
        { prop: "usage", label: "用途" },
      ],
      anchor: [
        { prop: "anchorName", label: "锚地名称" },
        { prop: "usage", label: "用途" },
      ],
      bridge: [
        { prop: "bridgeName", label: "桥梁名称" },
        { prop: "bridgeAttributes", label: "桥梁属性" },
      ],
      station: [{ prop: "name", label: "站点" }],
    };

    const focusHandle = async () => {
      if (showFlag.value == false) {
        showFlag.value = true;
        skeletonFlag.value = true;
        await getData(0, 10);
        skeletonFlag.value = false;
      }
    };

    const closeHandle = () => {
      showFlag.value = false;
    };

    const getData = async (page: number, size: number) => {
      let type = selectValue.value;
      if (switchValue.value && selectValue.value == "ship") {
        type = "realShip";
      }
      const data = await pageList({ type, page, size, keyword });
      if (data != null && (data as any).code === 0) {
        tableData.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const pageChange = async () => {
      skeletonFlag.value = true;

      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
    };

    const changeHandle = async () => {
      skeletonFlag.value = true;
      keyword = "";
      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
      currentPage.value = 1;
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + keyword + "</span>"
      );
      return currentStr;
    };

    const searchClick = async () => {
      keyword = inputValue.value;
      skeletonFlag.value = true;
      await getData(currentPage.value - 1, 10);
      skeletonFlag.value = false;
      currentPage.value = 1;
    };

    const rowClickHnadle = (
      row:
        | BuoyType
        | BridgeType
        | ParkType
        | AnchorType
        | StationType
        | ShipType
    ) => {
      if ("noMeaning" in row) {
        context.emit("returnPoint", {
          point: [row.longitude, row.latitude],
          info: row,
        });
      } else if ("keysCn" in row) {
        context.emit("returnPoint", {
          point: [row.longitude, row.latitude],
          info: row,
        });
      } else if ("polygon" in row) {
        context.emit("returnPoint", {
          point: row.polygon.coordinates[0][0],
          info: row,
        });
      } else if ("waterwayId" in row) {
        console.log("1", row);
        context.emit("returnPoint", {
          point: [row.longitude, row.latitude],
          info: row,
        });
      } else if ("anchorName" in row) {
        context.emit("returnPoint", {
          point: [row.longitude, row.latitude],
          info: row,
        });
      } else if ("mmsi" in row) {
        if (switchValue) {
          context.emit("returnPoint", {
            point: [row.longitude, row.latitude],
            info: row,
          });
        }
      }
    };

    const switchChange = async (val: boolean) => {
      context.emit("shipDataModeChange", val);
      if (selectValue.value === "ship") {
        skeletonFlag.value = true;
        await getData(0, 10);
        skeletonFlag.value = false;
      }
    };


    return {
      inputValue,
      selectValue,
      switchValue,
      options,
      showFlag,
      focusHandle,
      closeHandle,
      optionMap,
      propLabelMap,
      tableData,
      skeletonFlag,
      total,
      currentPage,
      pageChange,
      changeHandle,
      replaceHandle,
      searchClick,
      rowClickHnadle,
      switchChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.search-component {
  width: 420px;
  .el-input {
    box-shadow: 0 0 3px 3px #ccc;
  }

  .content {
    border-radius: 4px;
    padding: 10px;
    width: 400px;
    min-height: 300px;
    background: white;
    margin-top: 10px;
    box-shadow: 0 0 3px 3px #ccc;
    .title {
      position: relative;
      border-bottom: solid 1px;
      height: 30px;
      margin-bottom: 10px;
      .switch {
        position: absolute;
        left: 65px;
        top: -3px;
      }
      .el-icon {
        position: absolute;
        right: 0px;
        cursor: pointer;
      }
    }
    .el-table {
      cursor: pointer;
    }
    .page {
      display: flex;
      justify-content: center;
      margin-top: 5px;
    }
  }
}
</style>