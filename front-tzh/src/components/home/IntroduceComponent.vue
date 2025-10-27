<template>
  <el-row class="row-bg" justify="space-evenly">
    <el-col :span="4.8">
      <div class="grid-content ep-bg-purple" />
      <el-card class="box-card" :class="{ Large: this.isLarge[1] }" id="1" @mouseover="jumpToimg" @click="routeToPage(1)">
        <div class="card-header">
          <div class="iconclass resource-center"></div>
          <h2 class="part-title">资源门户</h2>
        </div>
        <div class="text item part-desc">
          集中管理和提供各种水沙数据资源，用户可方便地浏览、查询和下载所需的资源，提高工作效率
        </div>
      </el-card>
    </el-col>

    <el-col :span="4.8">
      <div class="grid-content ep-bg-purple" />
      <el-card class="box-card" :class="{ Large: this.isLarge[2] }" id="2" @mouseover="jumpToimg" @click="routeToPage(2)">
        <div class="card-header">
          <div class="iconclass daily-monitor"></div>
          <h2 class="part-title">日常监测</h2>
        </div>
        <div class="text item part-desc">
          展示实时潮位站水位数据，并根据历史和实时数据，预报未来一段时间内潮汐水位变化情况
        </div>
      </el-card>
    </el-col>

    <el-col :span="4.8">
      <div class="grid-content ep-bg-purple" />

      <el-card class="box-card" :class="{ Large: this.isLarge[3] }" id="3" @mouseover="jumpToimg"  @click="routeToPage(3)">
        <div class="card-header">
          <div class="iconclass ship-water"></div>
          <h2 class="part-title">水运一张图</h2>
        </div>
        <div class="text item part-desc">
          汇集水运数据，利用地图可视化展示船舶位置及信息、航标和海事等信息，助力海事决策
        </div>
      </el-card>
    </el-col>

    <el-col :span="4.8">
      <div class="grid-content ep-bg-purple" />

      <el-card class="box-card" :class="{ Large: this.isLarge[4] }" id="4" @mouseover="jumpToimg"  @click="routeToPage(4)">
        <div class="card-header">
          <div class="iconclass project-visual"></div>
          <h2 class="part-title">工程监测可视化</h2>
        </div>
        <div class="text item part-desc">
          工程监测成果数据聚合，利用数据图表、地图、表格等在大屏上实时呈现，提供全局数据洞察
        </div>
      </el-card>
    </el-col>

    <el-col :span="6">
      <div class="grid-content ep-bg-purple" />

      <el-card class="box-card" :class="{ Large: this.isLarge[5] }" id="5" @mouseover="jumpToimg"  @click="routeToPage(5)">
        <div class="card-header">
          <div class="iconclass geo-analysis"></div>
          <h2 class="part-title">分析中心</h2>
        </div>
        <div class="text item part-desc">
          基于GIS算法，进行在线水沙分析，能够进行断面分析、河床分析等并保存结果
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>
  
<script>
import router from "@/router";

export default {
  props: {
    messagefromparent: 0,
  },
  data() {
    return {
      isLarge: [null, false, false, false, false, false],
    };
  },
  watch: {
    messagefromparent(newValue) {
      // newValue存储了当前的轮播图索引，据此进行高亮
      if (!(newValue == 0 || newValue == -1)) {
        for (let i = 1; i < this.isLarge.length; i++) {
          if (i == newValue) this.isLarge[i] = true;
          else this.isLarge[i] = false;
        }
      } else {
        for (let i = 1; i < this.isLarge.length; i++) {
          this.isLarge[i] = false;
        }
      }
    },
  },
  methods: {
    jumpToimg(event) {
      // console.log("aaaa")
      var targetImgIndex = event.target.closest(".box-card").id;
      this.$emit("targetImgIndex", targetImgIndex);
    },

    routeToPage(pageIndex) {
      switch (pageIndex) {
        case 1:
          // console.log("home")
          router.push({ path: "/resource/list" });
          break;
        case 2:
          // console.log("home")
          router.push({ path: "/waterForecast" });
          break;
        case 3:
          // console.log("home")
          router.push({ path: "/waterway" });
          break;
        case 4:
          // console.log("home")
          router.push({ path: "/dataViewer" });
          break;
        case 5:
          // console.log("home")
          router.push({ path: "/analysis/list" });
          break;
        default:
          router.push({ path: "/" });
          break;
      }
    }
  },
};
</script>
  

<style lang="scss" scoped>
.el-row {
  margin-top: 1vh;
  margin-bottom: 0px;
  margin-left: 7vw;
}

.el-col {
  border-radius: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 15vw;
  height: 20vh;
  color: rgb(203, 235, 255);
  border-radius: 12px;
  background-color: rgba(0, 3, 21, 0.4);
  backdrop-filter: blur(4px);
  border: none;
  box-shadow: 3px 3px 3px rgba(108, 122, 210, 0.6);

  &:hover {
    // background-color: rgb(0, 19, 104);
    background-image: linear-gradient(30deg, #0b61f6bc 10%, #003bc5ab 30%, #000066b4 100%);
    color: rgb(255, 255, 255);
    cursor: pointer;
    box-shadow: 6px 6px 4px rgba(137, 143, 181, 0.8);


    .part-title {
      font-size: calc(0.9vh + 0.8vw);
      transition: all ease-in-out 0.3s;
    }

    .part-desc {
      padding-top: 1vh;
      font-size: calc(0.6vh + 0.6vw);
      transition: all ease-in-out 0.3s;
    }
  }

  &.Large {
    background-image: linear-gradient(30deg, #0b61f6bc 10%, #003bc5ab 30%, #000066b4 100%);
    color: rgb(255, 255, 255);
    box-shadow: 6px 6px 4px rgba(137, 143, 181, 0.8);

    .part-title {
      font-size: calc(0.9vh + 0.8vw);
      transition: all ease-in-out 0.3s;
    }

    .part-desc {
      padding-top: 1vh;
      font-size: calc(0.6vh + 0.6vw);
      transition: all ease-in-out 0.3s;
    }
  }

  .iconclass {
    width: 4em;
    height: 4em;
    background-size: cover;

    &.project-visual {
      background-image: url('../../assets/project.png');
    }

    &.resource-center {
      background-image: url('../../assets/backup.png');
    }

    &.daily-monitor {
      background-image: url('../../assets/monitoring.png');
    }

    &.ship-water {
      background-image: url('../../assets/ship-water.png');
    }

    &.geo-analysis {
      background-image: url('../../assets/location-mark.png');
    }
  }

  .part-title {
    font-weight: 600;
    font-size: calc(0.8vh + 0.7vw);
    transition: all ease-in-out 0.5s;
  }

  .part-desc {
    padding-top: 1vh;
    font-size: calc(0.6vh + 0.5vw);
    transition: all ease-in-out 0.5s;
  }
}
</style>