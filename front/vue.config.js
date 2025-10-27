const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8050,
    // https: true,
    proxy: {
      "/monitor": {
        target: "http://127.0.0.1:8700/",
        // target: "http://192.168.30.3:8700/",
        secure: false,
        changeOrigin: true,
        pathRewrite: {
          "^/monitor": "",
        },
      },
    },
  },
});

// lszh2021060801
// 20210608cm
