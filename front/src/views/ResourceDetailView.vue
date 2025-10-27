<template>
  <div class="main">
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <data-detail :fileInfo="fileInfo" v-else></data-detail>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import router from "@/router";
import { getFileInfo, addWatchCount } from "@/api/request";
import { DataListType } from "@/type";
import DataDetail from "@/components/resource/DataDetail.vue";
export default defineComponent({
  components: { DataDetail },
  setup() {
    const skeletonFlag = ref(true);
    const fileInfo = ref<DataListType>();
    const getDataInfo = async (id: string) => {
      const res = await getFileInfo(id);
      if (res && res.code === 0) {
        fileInfo.value = res.data;
        await addWatchCount(id);
      }
    };

    watch(
      () => router.currentRoute.value.params.id,
      async (newVal, oldVal) => {
        if (newVal !== undefined) {
          await getDataInfo(newVal as string);
          skeletonFlag.value = false;
        }
      },
      {
        immediate: true,
      }
    );

    return { skeletonFlag, fileInfo };
  },
});
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
}
</style>