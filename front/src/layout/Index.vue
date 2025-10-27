<template>
  <div class="layout">
    <header-component></header-component>
    <router-view v-slot="{ Component }">
      <keep-alive>
        <component
          :is="Component"
          v-if="route.meta.keepAlive"
          :key="route.meta.key"
        />
      </keep-alive>
      <component
        :is="Component"
        :key="route.meta.key"
        v-if="!route.meta.keepAlive"
      />
    </router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import HeaderComponent from "./Header.vue";
import router from "@/router";
export default defineComponent({
  components: { HeaderComponent },
  setup() {
    const route = computed(() => {
      return router.currentRoute.value;
    });

    return { route };
  },
});
</script>

<style lang="scss" scoped>
.layout {
  height: 100%;
}
</style>