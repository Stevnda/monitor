<template>
  <div class="basemap">
    <div
      v-for="(item, index) in list"
      :key="index"
      class="image"
      @click="basemapClick(index)"
    >
      <img :src="item.image" />
      <div :class="active === index ? 'text active' : 'text'">
        {{ item.name }}
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
export default defineComponent({
  emits: ["selectBasemap"],
  setup(_, context) {
    const list = reactive([
      {
        image: "/tianditu.png",
        name: "天地图",
      },
      {
        image: "/satellite.png",
        name: "卫星图",
      },

      {
        image: "/osm.png",
        name: "osm底图",
      },
    ]);
    const active = ref(0);

    const basemapClick = (index: number) => {
      if (index !== active.value) {
        active.value = index;
        context.emit("selectBasemap", active.value);
      }
    };

    return { list, active, basemapClick };
  },
});
</script>


<style lang="scss" scoped>
.basemap {
  display: flex;
  position: absolute;
  bottom: 1.5rem;
  right: 2rem;
  z-index: 1000;
  .image {
    position: relative;
    cursor: pointer;
    img {
      border: solid 0.5px white;
    }
    &:nth-child(2n) {
      margin: 0 0.5rem;
    }
    .text {
      height: 1.2rem;
      position: absolute;
      bottom: 5px;
      background: rgba($color: #000000, $alpha: 0.5);
      color: white;
      padding: 0px 3px;
    }
    .active {
      background: rgba($color: #2770d4, $alpha: 1);
    }
  }
}
</style>