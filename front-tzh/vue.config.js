const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8050,
    proxy: {
      "/monitor": {
        target: "http://localhost:8700/",
        changeOrigin: true,
        pathRewrite: {
          "^/monitor": "",
        },
      },
    },
  },
});
