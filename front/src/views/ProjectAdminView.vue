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
      <project-class @changeType="changeType"></project-class>
      <el-skeleton :rows="5" animated v-if="skeletonFlag" />
      <div class="cards" v-else>
        <div v-if="projectList.length">
          <div v-for="(item, index) in projectList" :key="index">
            <project-card
              :projectInfo="item"
              :keyword="keyword"
              @clickHandle="clickHandle"
              @deleteHandle="deleteHandle"
            ></project-card>
          </div>
          <div class="page">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :total="total"
              @current-change="pageChange"
              v-model:current-page="currentPage"
            />
          </div>
        </div>
        <el-empty description="暂无数据" v-else />
      </div>
    </div>
    <el-affix :offset="200" class="add">
      <el-button type="primary" size="small" @click="addProjectDialog = true">
        <el-icon><Plus /></el-icon>
      </el-button>
    </el-affix>
  </div>
  <el-backtop :right="100" :bottom="100" />
  <page-copyright />
  <el-dialog v-model="addProjectDialog" title="添加新的监测项目" width="30%">
    <add-project
      @cancel="addProjectDialog = false"
      @commit="commitHandle"
    ></add-project>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import ProjectClass from "@/components/admin/ResourceClass.vue";
import ProjectCard from "@/components/admin/ResourceCard.vue";
import { pageQueryProject, createProject, deleteProject } from "@/api/request";
import { ProjectType } from "@/type";
import AddProject from "@/components/admin/AddProject.vue";
import { notice } from "@/utils/common";
import PageCopyright from "@/layout/PageCopyright.vue";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import router from "@/router";
export default defineComponent({
  components: { ProjectClass, ProjectCard, AddProject, PageCopyright },
  setup() {
    const skeletonFlag = ref(true);
    const input = ref("");
    const total = ref(0);
    const projectList = ref<ProjectType[]>([]);
    const currentPage = ref(1);

    const addProjectDialog = ref(false);

    let type = "history";
    let keyword = ref("");

    const pageQuery = async (
      keyword: string,
      type: string,
      page: number,
      size: number
    ) => {
      NProgress.start();
      const jsonData = {
        keyword: keyword,
        type: type,
        page: page,
        size: size,
      };
      const res = await pageQueryProject(jsonData);
      if (res && res.code === 0) {
        projectList.value = res.data.list;
        total.value = res.data.total;
      }
      NProgress.done();
    };

    const pageChange = async (val: number) => {
      await pageQuery(keyword.value, type, val - 1, 10);
      input.value = keyword.value;
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    };

    const changeType = async (val: number) => {
      if (val === 0) type = "history";
      else type = "realTime";
      currentPage.value = 1;
      await pageQuery(keyword.value, type, currentPage.value - 1, 10);
      input.value = keyword.value;
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    };

    const commitHandle = async (val: {
      projectName: string;
      description: string;
      institution: string;
      time: string;
      location: string;
      avatar: string;
      type: string;
    }) => {
      const res = await createProject(val);
      if (res && res.code === 0) {
        notice("success", "成功", "新项目添加成功!");
        addProjectDialog.value = false;
        // currentPage.value = 1;
        // await pageQuery(keyword, type, currentPage.value - 1, 10);
        // window.scrollTo({
        //   top: 0,
        //   behavior: "smooth",
        // });
      }
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      currentPage.value = 1;
      await pageQuery(keyword.value, type, currentPage.value - 1, 10);
    };

    const deleteHandle = async (id: string) => {
      console.log(id);
      const res = await deleteProject(id);
      if (res && res.code === 0) {
        notice("success", "成功", "删除成功");
      }
    };

    const clickHandle = (id: string) => {
      console.log(id);
      router.push({ name: "projectDetail", params: { id: id } });
    };

    onMounted(async () => {
      await pageQuery("", "history", 0, 10);
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      keyword,
      input,
      total,
      projectList,
      currentPage,
      addProjectDialog,
      pageChange,
      changeType,
      commitHandle,
      searchHandle,
      deleteHandle,
      clickHandle,
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
    // align-items: flex-start;
    min-height: calc(100vh - 17rem);

    // :deep() .el-affix > div {
    //   height: 100%;
    // }
    .project-class {
      padding: 1rem 2rem;
      width: 14rem;
    }
    .cards {
      width: calc(100% - 14rem - 4rem);

      .project-card {
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