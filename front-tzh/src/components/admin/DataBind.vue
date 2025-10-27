<template>
  <div class="data-bind-main">
    <el-table :data="tableData" height="300px">
      <el-table-column label="名称" prop="name" />
      <el-table-column label="大小" prop="size" />
      <el-table-column align="right">
        <template #header>
          <el-button type="primary" circle size="small" @click="addClick">
            <el-icon><Plus /></el-icon>
          </el-button>
        </template>
        <template #default="scope">
          <el-button
            size="small"
            type="primary"
            v-if="isView(scope.row)"
            @click="viewClick(scope.row)"
            ><el-icon><View /></el-icon
          ></el-button>
          <el-button
            size="small"
            type="success"
            @click="downloadClick(scope.row)"
            ><el-icon><Download /></el-icon
          ></el-button>
          <el-button size="small" type="danger" @click="deleteClick(scope.row)"
            ><el-icon><Delete /></el-icon
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogFlag" width="800px" :show-close="false">
      <p class="title">数据绑定</p>
      <div class="dialogTable">
        <div class="table-head">
          <el-icon size="20px" @click="backClick"><arrow-left /></el-icon>
          <div class="path">
            <div class="path-item">user</div>
            <div class="path-item separate">/</div>
            <div v-for="(item, index) in path" :key="index" class="item">
              <div class="path-item">{{ item.name }}</div>
              <div class="path-item separate">/</div>
            </div>
          </div>
        </div>

        <el-table
          :data="dialogTableData"
          height="300px"
          @cell-dblclick="dblclick"
          v-loading="loading"
          :row-class-name="tableRowClassName"
        >
          <el-table-column label="名称" prop="name">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <svg style="width: 20px; height: 20px">
                  <use :xlink:href="getIcon(scope.row.folder)"></use>
                </svg>
                <span style="margin-left: 10px">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="大小" prop="size" width="120" />
          <el-table-column align="right" width="140">
            <template #header>
              <p @click="listShow = true">
                已选：{{ tempTableData.length }} 条数据
              </p>
            </template>
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.flag"
                size="large"
                v-if="!scope.row.folder"
                @change="checkboxChange(scope.row)"
              />
            </template>
          </el-table-column>
        </el-table>
        <div class="btn">
          <el-button type="primary" plain @click="commit">确定</el-button>
        </div>
      </div>

      <el-dialog v-model="listShow" width="500px" :show-close="false">
        <div class="list">
          <el-table :data="tempTableData" height="250px">
            <el-table-column label="名称" prop="name" />
            <el-table-column align="right" width="120">
              <template #header> {{ tempTableData.length }} 条 </template>
              <template #default="scope">
                <el-button
                  size="small"
                  type="danger"
                  @click="listDelete(scope.row)"
                  ><el-icon><Delete /></el-icon
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-dialog>
    </el-dialog>

    <el-dialog v-model="dataPreviewFlag" width="900px">
      <DataPreview :fileInfo="fileInfo" v-if="dataPreviewFlag"></DataPreview>
    </el-dialog>
  </div>
</template>

<script lang="ts">
type DialogTableType = {
  id: string;
  name: string;
  folder: boolean;
  size: string;
  flag: boolean;
  parentId: string;
  visualType?: string;
  visualId?: string;
  // view?: string;
};
import { defineComponent, onMounted, ref } from "vue";
import { findByFolderId } from "@/api/request";
import DataPreview from "./DataPreviewDialog.vue";
import { BindDataFileInfo } from "@/type";
export default defineComponent({
  components: { DataPreview },

  emits: ["changeData"],
  setup(props, context) {
    const dialogFlag = ref(false);
    const listShow = ref(false);
    const dialogTableData = ref<DialogTableType[]>([]);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const loading = ref(false);
    const tempTableData = ref<DialogTableType[]>([]);
    const tableData = ref<DialogTableType[]>([]);

    const fileInfo = ref<DialogTableType>();
    const dataPreviewFlag = ref(false);
    const visualCompareFlag = ref(false);

    const getIcon = (floder: boolean) => {
      if (floder) {
        return "#icon-wenjianjia";
      } else {
        return "#icon-wenjian";
      }
    };

    const addClick = () => {
      tempTableData.value = [];
      tableData.value.forEach((item) => {
        tempTableData.value.push(item);
      });
      dialogFlag.value = true;
    };

    const isView = (param: DialogTableType) => {
      if (param.visualType != "" && param.visualType != "audit") return true;
      else return false;
    };

    const viewClick = (val: any) => {
      fileInfo.value = val;
      dataPreviewFlag.value = true;
    };

    const downloadClick = async (val: DialogTableType) => {
      console.log(val);
      window.location.href = `/monitor/files/downloadFile/${val.id}`
    };

    const deleteClick = (val: DialogTableType) => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (val.id === tableData.value[i].id) {
          tableData.value.splice(i, 1);
          val.flag = false;
          context.emit("changeData", tableData.value);
        }
      }
    };

    const checkboxChange = (val: DialogTableType) => {
      if (val.flag) {
        tempTableData.value.push(val);
      } else {
        for (let i = 0; i < tempTableData.value.length; i++) {
          if (tempTableData.value[i].id === val.id) {
            tempTableData.value.splice(i, 1);
            return;
          }
        }
      }
    };

    const checkFlag = (id: string) => {
      for (let i = 0; i < tempTableData.value.length; i++) {
        if (tempTableData.value[i].id === id) {
          return true;
        }
      }
      return false;
    };

    const listDelete = (row: DialogTableType) => {
      for (let i = 0; i < tempTableData.value.length; i++) {
        if (tempTableData.value[i].id === row.id) {
          tempTableData.value.splice(i, 1);
          row.flag = false;
          return;
        }
      }
    };

    const tableRowClassName = ({
      row,
      rowIndex,
    }: {
      row: DialogTableType;
      rowIndex: number;
    }) => {
      if (row.flag) {
        return "selected";
      } else {
        return "";
      }
    };

    const transitionData = (val: any) => {
      if ("fileName" in val) {
        dialogTableData.value.push({
          id: val.id,
          name: val.fileName,
          folder: false,
          size: val.size,
          flag: checkFlag(val.id),
          parentId: val.folderId,
          visualId: val.visualId,
          visualType: val.visualType,

        });
      } else {
        dialogTableData.value.push({
          id: val.id,
          name: val.folderName,
          folder: true,
          size: "",
          flag: false,
          parentId: val.parentId,
        });
      }
    };

    const dblclick = async (row: DialogTableType) => {
      if (row.folder) {
        loading.value = true;
        const data = await findByFolderId(row.id);
        if (data != null && data.code === 0) {
          dialogTableData.value = [];
          (data.data as any[]).forEach((item) => {
            transitionData(item);
          });
          path.value.push({
            id: row.id,
            name: row.name,
            parentId: row.parentId,
          });
        }
        loading.value = false;
      }
    };

    const backClick = async () => {
      if (path.value.length > 0) {
        loading.value = true;
        let parentId: string = "-1";
        if (path.value[path.value.length - 1].parentId != "") {
          parentId = path.value[path.value.length - 1].parentId;
        }
        const dataList = await findByFolderId(parentId);
        if (dataList != null && (dataList as any).code === 0) {
          dialogTableData.value = [];
          (dataList.data as any[]).forEach((item) => {
            transitionData(item);
          });
          path.value.pop();
        }
        loading.value = false;
      }
    };

    const getTableData = (files: BindDataFileInfo[]) => {
      tableData.value = [];
      files.forEach((item) => {
        tableData.value.push({
          id: item.id,
          name: item.fileName,
          folder: false,
          size: item.size,
          flag: true,
          parentId: "",
          visualId: item.visualId,
          visualType: item.visualType,
        });
      });
    };

    const clearData = () => {
      tableData.value = [];
      tempTableData.value = [];
      dialogTableData.value.forEach((item) => {
        item.flag = false;
      });
    };

    const updateData = (files: BindDataFileInfo[]) => {
      clearData();
      if (files) {
        console.log("updateData", files);
        getTableData(files);
      }
    };

    const commit = () => {
      tableData.value = [];
      tempTableData.value.forEach((item) => {
        tableData.value.push(item);
      });
      context.emit("changeData", tableData.value);
      dialogFlag.value = false;
    };

    onMounted(async () => {
      const data = await findByFolderId("-1");
      if (data != null && (data as any).code === 0) {
        dialogTableData.value = [];
        (data.data as any[]).forEach((item) => {
          transitionData(item);
        });
      }
    });

    return {
      tableData,
      addClick,
      deleteClick,
      dialogFlag,
      getIcon,
      dialogTableData,
      path,
      backClick,
      dblclick,
      loading,
      checkboxChange,
      tempTableData,
      tableRowClassName,
      listShow,
      listDelete,
      commit,
      clearData,
      getTableData,
      isView,
      viewClick,
      downloadClick,
      updateData,
      fileInfo,
      dataPreviewFlag,
      visualCompareFlag,
      DataPreview,
    };
  },
});
</script>


<style lang="scss" scoped>
.data-bind-main {
  width: 100%;
  border: solid 0.5px #ebeef5;
  padding: 0 5px;

  .title {
    font-size: 25px;
    color: royalblue;
    font-weight: 600;
    letter-spacing: -1px;
    position: relative;
    display: flex;
    align-items: center;
    padding-left: 40px;
    padding-top: 10px;
    margin: 5px 0px;
  }
  .title::before,
  .title::after {
    position: absolute;
    content: "";
    height: 16px;
    width: 16px;
    border-radius: 50%;
    left: 10px;
    background-color: royalblue;
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

  .title::after {
    width: 18px;
    height: 18px;
    animation: pulse 1s linear infinite;
  }

  .dialogTable {
    padding: 15px 10px 10px 10px;
    cursor: pointer;
    .table-head {
      height: 25px;
      display: flex;
      position: relative;
      .el-icon {
        cursor: pointer;
      }
      .path {
        width: 60%;
        display: flex;
        .item {
          display: flex;
        }
        .path-item {
          height: 20px;
          line-height: 20px;
          font-size: 15px;
        }
        .separate {
          color: #b7bbc3;
          margin: 0 5px;
        }
      }
      .btn {
        position: absolute;
        right: 0px;
      }
    }
    .btn {
      text-align: center;
      margin-top: 10px;
    }
    .el-checkbox {
      height: 20px;
    }
    .el-table {
      :deep() .selected {
        --el-table-tr-bg-color: #f0f9eb;
      }
    }

    .list {
      padding: 0px 10px;
    }
  }
}
</style>