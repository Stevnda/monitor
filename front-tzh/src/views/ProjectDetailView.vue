<template>
  <div>hahah</div>
</template>

<script lang="ts">
import { defineComponent, watch } from "vue";
import router from "@/router";
export default defineComponent({
  setup() {
    watch(
      () => router.currentRoute.value.params.id,
      (newVal, oldVal) => {
        console.log(newVal, oldVal);
        if (newVal !== undefined) {
            
        }
      },
      {
        immediate: true,
      }
    );
  },
});
</script>


<style lang="scss" scoped>
</style>