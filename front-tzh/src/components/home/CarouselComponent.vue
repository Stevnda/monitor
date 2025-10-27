<template>
    <el-carousel :interval="5000" type="card" height="67vh" @change="carouselChange" ref="carousel">
    <el-carousel-item id="first">
        <img class="picture" src="/resource/pic.jpg" />
        <div class="describe">
            <h1 class="title-text">长江深水航道水沙监测分析研究服务系统</h1>
            <p class="title-desc">高效监测 高效管理</p>
        </div>
    </el-carousel-item>
    <el-carousel-item id="1">
      <!-- 门户图片 -->
      <div class="carousel-pic-title">资源门户</div>
      <img class="picture" src="/resource/resource.png" />
    </el-carousel-item>
    <el-carousel-item id="2">
      <!-- 大屏图片 -->
      <div class="carousel-pic-title">日常监测可视化</div>
      <img class="picture" src="/resource/daily-monitor.png" />
    </el-carousel-item>
    <el-carousel-item id="3">
      <!-- 一张图图片 -->
      <div class="carousel-pic-title">水运一张图</div>
        <img class="picture" src="/resource/ship-all.png" />
    </el-carousel-item>
    <el-carousel-item id="4">
      <!-- 分析中心图片 -->
      <div class="carousel-pic-title">工程监测可视化</div>
      <img class="picture" src="/resource/project-m.png" />
    </el-carousel-item>
    <el-carousel-item id="5">
      <!-- 潮位预报图片 -->
      <div class="carousel-pic-title">分析中心</div>
        <img class="picture" src="/resource/analysis-pro.png" />
    </el-carousel-item>
  </el-carousel>
  <!-- <button @click="setActiveItem(2)">设置第三张轮播图为当前项</button> -->
</template>

<script>

export default {
    props: {
        ImgIndexfromParent: -1
    },
    data() {
      return {
        nowID:'0'
      }
    },
    methods: {
        carouselChange(a){
            this.$emit('message-to-parent',a);
        },
        setNowImg(){
            if(this.ImgIndexfromParent!=-1){
              // 要使用 setActiveItem 方法，需要先通过 ref 获取走马灯组件实例，再调用 setActiveItem 方法即可。
              this.$refs.carousel.setActiveItem(this.ImgIndexfromParent);
            }
        } 
    },
    watch: {
        ImgIndexfromParent(newValue){
          if (newValue != -1) {
            this.setNowImg();
          }
        }
    }
}
</script>
<style lang="scss">
.el-carousel {
  padding-top: 1vh;
}
.carousel-pic-title {
  position: relative;
  left: -10%;
  top: 15%;
  color: #fff;
  font-weight: 600;
  font-size: calc(1.2vw + 1.2vh);
  z-index: 10;
}
.el-carousel__item.is-active {
  height: 65vh;
  width: 70vw;
  transform: translateX(15vh) scale(1);
  overflow: visible;
  img.picture {
    transform: translateX(-10vw);
  }
}
.picture {
    object-fit: fill;
    width: 100%;
    height: 100%;
    z-index: 5;
}

.describe {
  position: absolute;
  z-index: 2;
  color: #fff;
  right: 13vw;
  top: 4vh;
  text-align: right;

  h1.title-text {
    font-size: 5vh;
  }
  p.title-desc {
    font-size: 3vh;
  }
  
}

div.el-carousel__mask {
  background-color: transparent;
}
</style>