<template>
  <div>
    <video
      poster="/login/video-cover.jpeg"
      loop
      autoplay
      muted
      class="video-class"
    >
      <source src="/login/night.mp4" />
    </video>
    <div class="form">
      <p class="title">系统登录</p>
      <el-form :model="loginForm" :rules="rules" ref="ruleFormRef">
        <el-form-item prop="email">
          <el-input v-model="loginForm.email" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="Lock"
            show-password
            type="password"
            size="large"
          />
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="loginClick(ruleFormRef)" class="btn"
        >登录</el-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import { login } from "@/api/request";
import { setToken } from "@/utils/common";
import { Base64 } from "js-base64";
import router from "@/router";
import type { FormInstance } from "element-plus";
import { debounce } from "@/utils/common";
export default defineComponent({
  setup() {
    const ruleFormRef = ref<FormInstance>();
    const loginForm = reactive({
      email: "",
      password: "",
    });

    const rules = reactive({
      email: [
        { required: true, message: "邮箱不能为空", trigger: "blur" },
        { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
      ],
      password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
    });

    const loginClick = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate(async (valid) => {
        if (valid) {
          debounce(async () => {
            const jsonData = {
              email: loginForm.email,
              password: Base64.btoa(loginForm.password),
            };
            const res = await login(jsonData);
            if (res && res.code === 0) {
              setToken(res.data);
              router.push({ path: "/" });
            }
          }, 400)();
        }
      });
    };

    return { ruleFormRef, loginForm, rules, loginClick };
  },
});
</script>

<style lang="scss" scoped>
.video-class {
  position: absolute;
  /* Vertical and Horizontal center*/
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
}
.form {
  position: absolute;
  width: 450px;
  top: 25vh;
  left: calc(50vw - 225px);
  text-align: center;
  .btn {
    width: 450px;
  }
  .title {
    color: white;
    font-size: 25px;
  }
  :deep() .el-form-item .el-input__inner {
    background-color: rgba(255, 255, 255, 0.247);
    // color: white;
  }
}
</style>