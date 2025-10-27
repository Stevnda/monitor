<template>
  <el-affix :offset="0">
    <div class="header-main">
      <el-row>
        <el-col :span="1">
          <div class="grid-content" />
        </el-col>
        <el-col :span="3">
          <div class="grid-content name">
            长江南京以下深水航道<br />基础设施数据服务平台
          </div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content index" @click="nav('home')">首页</div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content data" @click="nav('resource')">资源门户</div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content amap" @click="nav('waterForecast')">
            监测数据
          </div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content analysis" @click="nav('waterway')">
            水运一张图
          </div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content analysis" @click="nav('dataview')">
            工程可视化
          </div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content prediction" @click="nav('stormPrediction')">
            潮位预报
          </div>
        </el-col>
        <el-col :span="2">
          <div class="grid-content help" @click="nav('analysis')">分析中心</div>
        </el-col>
        <el-col :span="2">
          <el-dropdown
            trigger="hover"
            @command="userNav"
            v-if="adminFlag"
            class="admin"
          >
            <span class="grid-content">管理员</span>
            <template #dropdown>
              <el-dropdown-menu :router="true">
                <el-dropdown-item command="1">数据仓库</el-dropdown-item>
                <el-dropdown-item command="2">资源条目管理</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
        <el-col :span="2" :offset="2">
          <el-dropdown trigger="hover" @command="userNav" v-if="logined">
            <el-button
              type="primary"
              color="rgba(219, 219, 219, 0.5)"
              :dark="false"
            >
              <el-avatar :size="32" :src="avatarUrl" />
              &nbsp;&nbsp;&nbsp;账号
            </el-button>
            <template #dropdown>
              <el-dropdown-menu :router="true">
                <el-dropdown-item command="3">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <div class="login" @click="toLogin" v-else>登录</div>
        </el-col>
      </el-row>
    </div>
  </el-affix>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import router from "@/router";
import { usePermissionStore } from "@/store/permission-store";
export default defineComponent({
  setup() {
    const store = usePermissionStore();
    const avatarUrl = computed(() => {
      return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
    });
    const adminFlag = computed(() => {
      if (store.role === "admin") {
        return true;
      } else {
        return false;
      }
    });

    const logined = computed(() => {
      if (store.role === "") {
        return false;
      } else {
        return true;
      }
    });

    const toLogin = () => {
      router.push({ path: "/login" });
    };

    const nav = (param: string) => {
      switch (param) {
        case "home":
          router.push({ path: "/" });
          return;
        case "resource":
          router.push({ path: "/resource/list" });
          return;
        case "dataview":
          router.push({ path: "/dataViewer" });
          return;
        case "waterway":
          router.push({ path: "/waterway" });
          return;
        case "waterForecast":
          router.push({ path: "/waterForecast" });
          return;
        case "stormPrediction":
          router.push({ path: "/stormPrediction" });
          return;
        case "analysis":
          router.push({ path: "/analysis/list" });
      }
    };

    const userNav = (param: string) => {
      console.log(param);
      switch (param) {
        case "1":
          router.push({ name: "folderAdmin" });
          return;
        case "2":
          router.push({ name: "resourceAdmin" });
          return;
        case "3":
          store.logout();
          return;
        case "4":
          router.push({ name: "folderAdmin" });
      }
    };

    return { avatarUrl, adminFlag, logined, toLogin, nav, userNav };
  },
});
</script>

<style lang="scss" scoped>
div.header-main {
  height: calc(5rem - 3px);
  z-index: 1000;
  padding: 0;
  // position: fixed;

  width: 96%;
  padding-left: 1%;
  padding-right: 3%;
  padding-top: 3px;
  background-image: linear-gradient(
    rgba(13, 21, 27, 0.5),
    rgba(24, 64, 95, 0.2)
  );
  background-color: rgb(0, 3, 26);
  transition: 0.7s all;
  z-index: 999;
  font-family: "Microsoft YaHei";

  .el-row {
    // margin-top: 1vh;
    height: 6vh;
  }
  .el-row:last-child {
    margin-bottom: 0;
  }
  .el-col {
    border-radius: 4px;
    height: 6vh;

    .grid-content {
      border-radius: 4px;
      min-height: 36px;
      height: 6vh;
      color: #ffffff;
      line-height: 6vh;
      text-align: center;
      font-size: 2vh;
      font-weight: 600;
      transition-duration: 0.5s;
      &:focus-visible {
        outline: none !important;
      }
      &:hover {
        cursor: pointer;
        transition-duration: 0.3s;
        &.index {
          font-size: 2.5vh;
          background-image: linear-gradient(
            45deg,
            #ffffff 0%,
            #9edcfc 40%,
            #39d6fd 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.data {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #fdfdd7 40%,
            #fff23a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.amap {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #d5ffd5 40%,
            #3aff3a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.prediction {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #bad4e2 0%,
            #e6b3f0 40%,
            #834765 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.analysis {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #ffe0bc 40%,
            #ffa33a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.help {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #ebbfb0 40%,
            #ff443a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
      }
      &.name {
        width: 14vw;
        font-weight: 500;
        text-align: left;
        font-size: 2vh;
        line-height: 3vh;
        margin-left: 2vh;
        color: aliceblue;
        background-image: linear-gradient(
          45deg,
          #ffffff 0%,
          #9edcfc 40%,
          #39d6fd 100%
        );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
      }
    }
    &:first-child {
      background-color: transparent;
      .grid-content {
        background-color: transparent;
        background-image: url(/public/png/logo.png);
        background-size: contain;
        background-repeat: no-repeat;
      }
    }
    .admin {
      display: block;
      // position: none;
      text-align: center;
    }
    &:last-child {
      border-radius: 3vh;
      .el-dropdown {
        border-radius: 3vh;
        width: 100%;
        height: 100%;

        .el-button {
          border-radius: 3vh;
          border-width: 1px;
          border-color: #b4b4b4c9;
          margin-top: 7%;
          width: 90%;
          height: 76%;
          font-size: 2vh;
          font-weight: 600;
          background-image: linear-gradient(
            30deg,
            #ffffff 0%,
            #d5f1ff 40%,
            #b8f1ff 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;

          &:hover {
            border-width: 2px;
            border-color: #b4b4b4;
            background-image: linear-gradient(
              30deg,
              #ffffff 0%,
              #d5f1ff 40%,
              #39d6fd 100%
            );
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-animation: hue 60s infinite linear;
          }
        }
      }
    }
  }
  .login {
    color: white;
    font-size: 2vh;
    font-weight: 600;
    line-height: 6vh;
    text-align: center;
    cursor: pointer;
    transition-duration: 0.5s;
    &:hover {
      font-size: 2.5vh;
      transition-duration: 0.3s;
      background-image: linear-gradient(
        30deg,
        #fffcd2 0%,
        #ebbfb0 40%,
        #ff443a 100%
      );
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      -webkit-animation: hue 60s infinite linear;
    }
  }
}

ul.el-dropdown-menu {
  background-color: rgb(2, 9, 76);
  font-size: 2vh;
  width: 8vw;

  :deep().el-dropdown-menu__item {
    font-size: 2vh;
    font-family: "Microsoft YaHei";
    color: rgb(224, 253, 255);
    font-weight: 500;
    text-align: center;
  }

  :deep().el-dropdown-menu__item:focus {
    background-color: rgb(0, 6, 37);
    color: #f3f3f3;
    font-weight: 600;
  }
}

:deep().el-scrollbar__wrap {
  background-color: #39d6fd !important;
}
</style>