<template>
  <div class="home">
    <CarouselComponent
      @message-to-parent="handleChilMessage"
      :ImgIndexfromParent="targetImgIndex"
    ></CarouselComponent>
    <introduce-component
      :messagefromparent="messageFromParent"
      @targetImgIndex="handletargetImgIndex"
    ></introduce-component>
  </div>
</template>

<script lang="ts">
import CarouselComponent from "@/components/home/CarouselComponent.vue";
import { defineComponent, onMounted } from "vue";
import IntroduceComponent from "@/components/home/IntroduceComponent.vue";

export default defineComponent({
  components: { IntroduceComponent, CarouselComponent },
  setup() {
    
  },
  data() {
    return {
      CarouselIndex: -1,
      messageFromParent: 0,
      targetImgIndex: -1,
    };
  },
  methods: {
    handleChilMessage(msg: any) {
      this.CarouselIndex = msg;
    },
    handletargetImgIndex(msg: any) {
      this.targetImgIndex = msg;
    },
  },
  watch: {
    CarouselIndex(nowValue) {
      //监视属性把轮播图索引传给Introduce组件
      this.messageFromParent = nowValue;
    },
  },
});
</script>

<style scoped>
div.home {
  /* background-image: linear-gradient(#0026a3, #000e3b); */
  height: calc(100vh - 5rem);
  background-image: url("../assets/wave-back.jpg");
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
}
</style>
