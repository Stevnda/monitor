<template>
  <!-- <iframe class="iframe" src="http://127.0.0.1:5173"></iframe> -->
  <iframe class="iframe" ref="iframeRef"></iframe>
  <!-- <iframe class="iframe" src="http://172.21.212.166:5173"></iframe> -->
</template>

<script setup>
  import { onMounted, ref } from 'vue';
  const iframeRef = ref(null);
  const ip = window.location.hostname;
  let iframeUrl = '';
  onMounted(() => {
    // iframeUrl = `http://${ip}:8052/stormPrediction`;
    iframeUrl = `http://${ip}:5173`;
    iframeRef.value.src = iframeUrl;
  });
</script>

<style scoped>
iframe.iframe{
    border: none;
    width: 99.8%;
    height: 91.9%;
    overflow: hidden;
}
</style>
