<template>
  <div class="admin-view" ref="adminView">
    <div class="top">
      <el-input
        v-model="input"
        placeholder="输入要检索的内容"
        size="large"
        @keydown.enter="searchHandle"
      />
    </div>
    <div class="content">
      <resource-class @changeType="changeType"></resource-class>
      <el-skeleton :rows="5" animated v-if="skeletonFlag" />
      <div class="cards" v-else>
        <div v-if="dataList.length">
          <div class="config">
            <div class="total">检索到 {{ total }} 条记录</div>
            <div class="sort">
              <el-dropdown trigger="click" @command="sortHandle">
                <el-button color="#5d6ab1" size="small">
                  {{ sortText
                  }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="update_time"
                      >更新时间</el-dropdown-item
                    >
                    <el-dropdown-item command="download"
                      >下载量</el-dropdown-item
                    >
                    <el-dropdown-item command="watch">浏览量</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-dropdown trigger="click" @command="showHandle">
                <el-button color="#5d6ab1" size="small">
                  {{ sizeText
                  }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="8"
                      >每页显示条数（8）</el-dropdown-item
                    >
                    <el-dropdown-item command="16"
                      >每页显示条数（16）</el-dropdown-item
                    >
                    <el-dropdown-item command="24"
                      >每页显示条数（24）</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-button color="#5d6ab1" size="small" @click="createHandle">
                创建新条目<el-icon><Plus /></el-icon>
              </el-button>
            </div>
          </div>
          <div v-for="(item, index) in dataList" :key="index">
            <resource-card
              :dataListInfo="item"
              :keyword="keyword"
              @clickHandle="clickHandle"
              @commandHandle="commandHandle"
            ></resource-card>
          </div>
          <div class="page">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :total="total"
              v-model:page-size="pageSize"
              @current-change="pageChange"
              v-model:current-page="currentPage"
            />
          </div>
        </div>
        <el-empty description="暂无数据" v-else />
      </div>
    </div>
  </div>
  <el-backtop :right="100" :bottom="100" />
  <page-copyright />
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import ResourceClass from "@/components/admin/ResourceClass.vue";
import ResourceCard from "@/components/admin/ResourceCard.vue";

import { deleteDataList, fuzzyQueryDataList } from "@/api/request";
import { DataListType } from "@/type";
import { notice } from "@/utils/common";
import PageCopyright from "@/layout/PageCopyright.vue";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import router from "@/router";
export default defineComponent({
  components: { ResourceClass, ResourceCard, PageCopyright },
  setup() {
    const skeletonFlag = ref(true);
    const input = ref("");
    const total = ref(0);
    const dataList = ref<DataListType[]>([]);
    const currentPage = ref(1);
    const pageSize = ref(8);
    const sortText = ref("更新时间");
    const sizeText = ref("每页显示条数（8）");

    const addProjectDialog = ref(false);

    const type = ref("");
    const keyword = ref("");
    const property = ref("update_time");

    const searchData = async (
      page: number,
      size: number,
      titleKeyword: string,
      property: string,
      type: string
    ) => {
      const jsonData = {
        page: page,
        size: size,
        titleKeyword: titleKeyword,
        property: property,
        flag: false,
        type: type,
      };
      NProgress.start();
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null && (data as any).code === 0) {
        dataList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done();
    };

    const pageChange = async (val: number) => {
      await searchData(val - 1, 8, keyword.value, property.value, type.value);
      input.value = keyword.value;
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    };

    const changeType = async (val: string) => {
      if (val === "all") type.value = "";
      else type.value = val;
      keyword.value = input.value;
      currentPage.value = 1;
      await searchData(0, 8, keyword.value, property.value, type.value);
      input.value = keyword.value;
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      currentPage.value = 1;
      await searchData(
        currentPage.value - 1,
        8,
        keyword.value,
        property.value,
        type.value
      );
    };

    const commandHandle = async (val: { type: string; id: string }) => {
      if (val.type === "update") {
        router.push({ name: "updateResource", params: { id: val.id } });
      } else if (val.type === "delete") {
        const res = await deleteDataList(val.id);
        if (res && res.code === 0) notice("success", "成功", "条目删除成功");
      }
    };

    const clickHandle = (id: string) => {
      console.log(id);
      router.push({ name: "resourceDetail", params: { id: id } });
    };

    const sortHandle = async (val: string) => {
      property.value = val;
      await searchData(
        0,
        pageSize.value,
        keyword.value,
        property.value,
        type.value
      );
      currentPage.value = 1;
      switch (val) {
        case "update_time":
          sortText.value = "更新时间";
          break;
        case "download":
          sortText.value = "下载量";
          break;
        case "watch":
          sortText.value = "浏览量";
          break;
      }
    };

    const showHandle = async (val: string) => {
      const old = pageSize.value;
      pageSize.value = parseInt(val);
      await searchData(
        Math.floor((old * currentPage.value) / pageSize.value),
        pageSize.value,
        keyword.value,
        property.value,
        type.value
      );
      currentPage.value =
        Math.floor((old * currentPage.value) / pageSize.value) + 1;
      switch (val) {
        case "8":
          sizeText.value = "每页显示条数（8）";
          break;
        case "16":
          sizeText.value = "每页显示条数（16）";
          break;
        case "24":
          sizeText.value = "每页显示条数（24）";
          break;
      }
    };

    const createHandle = () => {
      router.push({ name: "createResource" });
    };

    onMounted(async () => {
      await searchData(0, 8, "", "update_time", "");
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      keyword,
      input,
      total,
      pageSize,
      dataList,
      currentPage,
      addProjectDialog,
      sortText,
      sizeText,
      pageChange,
      changeType,
      searchHandle,
      commandHandle,
      clickHandle,
      showHandle,
      sortHandle,
      createHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-view {
  min-height: calc(100% - 5rem);
  position: relative;
  .top {
    height: 12rem;
    background: #232539;
    position: relative;
    .el-input {
      width: 68%;
      position: relative;
      top: 4rem;
      left: 16%;
    }
  }
  .content {
    display: flex;
    min-height: calc(100vh - 17rem);

    .resource-class {
      padding: 1rem 2rem;
      width: 14rem;
    }
    .cards {
      width: calc(100% - 14rem - 4rem);
      .config {
        padding: 0 1rem;
        height: 2rem;
        margin-top: 1rem;
        display: flex;
        position: relative;
        .total {
          color: #ff7049;
        }
        .sort {
          position: absolute;
          right: 4rem;
          .el-dropdown {
            margin-right: 1rem;
          }
        }
      }
      .dataList-card {
        width: calc(100% - 5rem);
        margin-left: 1rem;
      }
      .page {
        height: 7rem;
        display: flex;
        width: 100%;
        justify-content: center;
      }
    }
  }
  .add {
    position: absolute;
    right: 0.8rem;
    top: 12.5rem;
  }
}
</style>