<template>
  <div class="main">
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div class="body" v-else>
      <div class="backTitle">分析中心</div>
      <!-- <div class="backIcon"><DataLine class="iconn"/></div> -->
      <div class="control">
        <button class="Btn" @click="createAnalysisList">
          <div class="sign">+</div>
          <div class="text">创建分析条目</div>
        </button>
      </div>

      <el-dialog v-model="createDialogVisible" width="20%">
        <div class="dialog">
          <div class="head">创建分析条目</div>
          <div class="dialog_text">条目名称：</div>
          <el-input
            v-model="newListName"
            placeholder="Please input"
            clearable
          />
          <div class="dialog_text">上传图片:</div>
          <div class="avatar">
            <UploadAvatar @returnPicture="returnPicture"> </UploadAvatar>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button
              @click="
                createDialogVisible = false;
                newListName = '';
              "
              >取消</el-button
            >
            <el-button type="primary" @click="createConfirm"> 确定 </el-button>
          </span>
        </template>
      </el-dialog>

      <el-empty description="暂无数据" v-if="analysisList.length === 0" />
      <el-row v-else :gutter="50">
        <!-- OG -->
        <el-col :span="6" v-for="(item, index) in analysisList" :key="index">
          <analysis-card
            :info="item"
            :keyword="keyword"
            @click="handleClick(index, item)"
            @refresh-flag="getFlagFromChild"
          ></analysis-card>
        </el-col>
      </el-row>
      <!-- 分页 -->
      <div class="page">
        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="total"
          @current-change="currentChange"
          v-model:current-page="currentPage"
          :page-size="8"
          :pager-count="5"
          :background="true"
          
        >
        </el-pagination>
      </div>
    </div>
    <el-backtop :right="100" :bottom="100" />

    <!-- 版权 -->
    <copyright />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch } from "vue";
import NProgress from "nprogress";
import { uuid } from "@/utils/common";
import AnalysisCard from "@/components/analysis/AAnalysisCard.vue";
import UploadAvatar from "@/components/upload/UploadAvatar.vue";
import copyright from "@/layout/PageCopyright.vue";
import {
  pageQueryAnalysis,
  addAnalysisList,
} from "@/api/request";
import { ElMessage, ElMessageBox } from "element-plus";

NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: { AnalysisCard, copyright, UploadAvatar },
  setup() {
    const analysisList = ref<any[]>([]);
    const search = ref("");
    const total = ref(0);
    const keyword = ref("");
    const currentPage = ref(1);
    const skeletonFlag = ref(true);
    // const offset = ref(0);
    const affixFlag = ref(true);
    const createDialogVisible = ref(false);

    const selectedIndex = ref(-1);
    const newListName = ref("");
    const newListAvatar = ref("");

    const refreshFlag = ref(false);

    const getFlagFromChild = (val: boolean) => {
      refreshFlag.value = val;
    };

    // const selectCard;
    // let selectedItem;
    onMounted(async () => {
      // computeOffset();
      skeletonFlag.value = true;
      // const data = await pageQueryAnalysis({
      //   size: 8,
      //   page: 0,
      //   keyword: keyword.value,
      // });
      // if (data != null && (data as any).code === 0) {
      //   analysisList.value = data.data.list;
      //   total.value = data.data.total;
      // }
      await refresh();
      skeletonFlag.value = false;
    });

    watch(refreshFlag, async (val) => {
      if (val) {
        await refresh();
        refreshFlag.value = false;
      }
    });

    // const searchClick = async () => {
    //   NProgress.start();
    //   keyword.value = search.value;
    //   const data = await pageQueryAnalysis({
    //     size: 8,
    //     page: 0,
    //     keyword: keyword.value,
    //   });

    //   if (data != null) {
    //     if ((data as any).code === 0) {
    //       analysisList.value = data.data.list;
    //       total.value = data.data.total;
    //       currentPage.value = 1;
    //     }
    //   }
    //   NProgress.done();
    // };

    const currentChange = async (page: number) => {
      NProgress.start();
      const data = await pageQueryAnalysis({
        size: 8,
        page: page - 1,
        keyword: keyword.value,
      });
      if (data != null && (data as any).code === 0) {
        analysisList.value = data.data.list;
        total.value = data.data.total;
      }
      search.value = keyword.value;
      NProgress.done();
    };

    const returnPicture = (val: string) => {
      newListAvatar.value = val;
    };
    // const computeOffset = () => {
    //   let div = document.createElement("div");
    //   div.style.height = "7vh";
    //   div.style.maxHeight = "none";
    //   div.style.boxSizing = "content-box";
    //   document.body.appendChild(div);
    //   let h = div.clientHeight;
    //   document.body.removeChild(div);
    //   console.log(h);
    //   offset.value = h;
    // };

    // const ClickHandle = () => {
    //   router.push({
    //     name: "UserSpaceProject",
    //   });
    // };

    const createAnalysisList = () => {
      createDialogVisible.value = true;
      selectedIndex.value = -1; //新建后重置选择
    };

    const handleClick = (index: number, item: any) => {
      // 记录所选条目
      selectedIndex.value = index;
    };

    const createConfirm = async () => {
      let jsondata = {
        id: uuid(),
        projectName: newListName.value,
        avatar: newListAvatar.value,
      };

      await addAnalysisList(jsondata);
      await refresh();

      ElMessage({
        type: "success",
        message: "创建成功",
      });

      createDialogVisible.value = false;
    };

    const refresh = async () => {
      const data = await pageQueryAnalysis({
        size: 8,
        page: 0,
        keyword: keyword.value,
      });
      if (data != null && data.code === 0) {
        analysisList.value = data.data.list;
        total.value = data.data.total;
      }
    };

    // const deleteAnalysisList = async () => {
    //   if (selectedIndex.value === -1) {
    //     ElMessage({
    //       type: 'warning',
    //       message: '请先选择要删除的条目'
    //     });
    //     return;
    //   }

    //   ElMessageBox.confirm(
    //     '是否确认删除选中条目：' + analysisList.value[selectedIndex.value].projectName,
    //     'Warning',
    //     {
    //       confirmButtonText: 'OK',
    //       cancelButtonText: 'Cancel',
    //       type: 'warning',
    //     }
    //   ).then(async() => {
    //       // Delete 操作

    //       await deleteAnalysisCase(analysisList.value[selectedIndex.value].id);

    //       const data = await pageQueryAnalysis({
    //         size: 8,
    //         page: 0,
    //         keyword: keyword.value,
    //       });
    //       if (data != null && data.code === 0) {
    //         analysisList.value = data.data.list;
    //         total.value = data.data.total;
    //       }

    //       ElMessage({
    //         type: 'success',
    //         message: '删除成功',
    //       })
    //     })
    //     .catch(() => {
    //       ElMessage({
    //         type: 'info',
    //         message: '删除失败',
    //       })
    //     })
    //   selectedIndex.value = -1;
    // }

    return {
      analysisList,
      search,
      total,
      currentChange,
      // searchClick,
      currentPage,
      skeletonFlag,
      // offset,
      keyword,
      selectedIndex,
      // ClickHandle,
      affixFlag,
      createAnalysisList,
      createDialogVisible,
      newListName,
      createConfirm,
      handleClick,
      returnPicture,
      getFlagFromChild,
    };
  },
});
</script>

<style lang="scss" scoped>
@keyframes ibannerbg {
  50% {
    transform: scale(1.2, 1.2);
  }

  100% {
    transform: scale(1, 1);
  }
}

.main {
  height: calc(100% - 5rem);
  position: relative;

  .body {
    height: 100%;
    padding: 0 200px;
    // height: 100%;
    background-color: black;
    // background-color: #29a3a36e;
    background-image: url("../assets/turquoiseBackImg1.jpg");
    background-size: 100vw 100vh;
    background-repeat: no-repeat;
  }

  .page {
    position: absolute;
    bottom: 0;
    width: 85%;
    display: flex;
    justify-content: center;
    padding-bottom: 30px;
    padding-top: 35px;

    :deep().el-pagination__jump {
      color: #ffffff;
    }

    :deep().el-pagination__total {
      color: #ffffff;
    }
  }
}

.backTitle {
  position: absolute;
  right: 1vw;
  top: 15vh;
  writing-mode: vertical-rl;
  text-orientation: upright;
  font-size: 8vh;
  font-family: "Microsoft YaHei";
  color: rgba(255, 255, 255, 0.671);
  font-weight: 900;
}

// .backIcon{
//   position:absolute;
//   left: 1vw;
//   bottom: 15vh;
//   color: rgba(194, 194, 194, 0.671);
//   .iconn{
//     width: 7vw;
//     height: 7vw;
//   }
// }

:deep().el-dialog {
  .el-dialog__header {
    padding: 0px;
    margin: 0;
    background: #ffffff;
    // .el-dialog__title {
    //   color: rgb(114, 168, 250);
    // }
  }

  .el-dialog__body {
    padding: 20px;
  }
}

.dialog {
  .head {
    font-size: 30px;
    color: royalblue;
    font-weight: 600;
    letter-spacing: -1px;
    position: relative;
    display: flex;
    align-items: center;
    padding-bottom: 10px;
  }

  .dialog_text {
    font-size: 18px;
    color: rgb(20, 20, 20);
    padding-top: 10px;
    padding-bottom: 5px;
  }

  .avatar {
    margin-left: 35%;
  }
}

.control {
  display: inline-flex;

  .Btn {
    position: relative;
    display: flex;
    top: 10px;

    margin-left: 15px;
    align-items: center;
    justify-content: flex-start;
    width: 50px;
    height: 50px;
    overflow: hidden;
    border-radius: 25px;
    cursor: pointer;
    box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.199);
    transition-duration: 0.5s;
    background: linear-gradient(144deg, #01ffc0, #197bec 50%, #00ddeb);

    .sign {
      width: 100%;
      font-size: 2.2em;
      color: white;
      transition-duration: 0.5s;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .text {
      position: absolute;
      right: 0%;
      width: 0%;
      opacity: 0;
      color: white;
      font-size: 0em;
      font-weight: 500;
      transition-duration: 0.5s;
    }

    &:hover {
      width: 200px;
      transition-duration: 0.5s;

      .sign {
        width: 25%;
        transition-duration: 0.5s;
        padding-left: 10px;
      }

      .text {
        width: 75%;
        padding-right: 10px;
        font-size: 1.4em;
        opacity: 1;
        transition-duration: 0.5s;
      }
    }
  }
}
</style>