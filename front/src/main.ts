import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { createPinia } from "pinia";
import '@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css'
import "@/permission";
import '@/assets/iconfont/iconfont.css'
import '@/assets/iconfont/iconfont.js'

const pinia = createPinia();
const app = createApp(App);
app.use(ElementPlus, {
  locale: zhCn,
});
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
app.use(router);
app.use(pinia);
app.mount("#app");
