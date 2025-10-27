<template>
  <div class="add-project">
    <el-form
      label-width="100px"
      :model="formData"
      ref="ruleFormRef"
      :rules="rules"
    >
      <el-form-item label="项目名" prop="projectName">
        <el-input v-model="formData.projectName" />
      </el-form-item>
      <el-form-item label="项目简介">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="2"
          resize="none"
        />
      </el-form-item>
      <el-form-item label="施工单位">
        <el-input v-model="formData.institution" />
      </el-form-item>
      <el-form-item label="施工时间">
        <el-input v-model="formData.time" />
      </el-form-item>
      <el-form-item label="施工地点">
        <el-input v-model="formData.location" />
      </el-form-item>
      <el-form-item label="项目封面">
        <upload-avatar @returnPicture="returnPicture"></upload-avatar>
      </el-form-item>
      <el-form-item label="项目类型">
        <el-radio-group v-model="formData.type">
          <el-radio label="history">历史监测项目</el-radio>
          <el-radio label="realTime">实时监测项目</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
  </div>
  <div class="footer">
    <div class="btn">
      <el-button type="primary" @click="commitHandle(ruleFormRef)"
        >确定</el-button
      >
      <el-button @click="cancelHandle">取消</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import UploadAvatar from "@/components/upload/UploadAvatar.vue";
import { debounce } from "@/utils/common";
import type { FormInstance, FormRules } from "element-plus";
export default defineComponent({
  components: { UploadAvatar },
  emits: ["cancel", "commit"],
  setup(_, context) {
    const ruleFormRef = ref<FormInstance>();
    const formData = reactive({
      projectName: "",
      description: "",
      institution: "",
      time: "",
      location: "",
      avatar: "",
      type: "history",
    });

    const rules = reactive<FormRules>({
      projectName: [
        {
          required: true,
          message: "请输入项目名",
          trigger: "blur",
        },
      ],
    });

    const returnPicture = (val: string) => {
      formData.avatar = val;
    };

    const cancelHandle = () => {
      context.emit("cancel");
    };

    const commitHandle = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          console.log(formData);
          debounce(() => {
            console.log(1);
            context.emit("commit", formData);
          }, 500)();
        }
      });
    };

    return {
      formData,
      rules,
      ruleFormRef,
      returnPicture,
      cancelHandle,
      commitHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.add-project {
  :deep().zip {
    .el-form-item__content {
      display: block;
      .el-upload-dragger {
        padding-top: 20px;
        padding-bottom: 20px;
        .el-icon--upload {
          margin-bottom: 0.5rem;
        }
      }
    }
  }
}
.footer {
  height: 2rem;
  .btn {
    float: right;
  }
}
</style>