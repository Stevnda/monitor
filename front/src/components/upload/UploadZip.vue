<template>
  <el-upload
    drag
    action="#"
    :auto-upload="false"
    :show-file-list="false"
    :on-change="changeHandle"
    :on-exceed="exceedHandle"
    :limit="1"
  >
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      Drop file here or <em>click to upload</em>
    </div>
  </el-upload>
  <div v-if="file">{{ file.name }}</div>
  <div>
    <el-button @click="deleteHandle">删除</el-button>
    <el-button @click="uploadHandle">上传</el-button>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { UploadFilled } from "@element-plus/icons-vue";
import type { UploadFile } from "element-plus";
import { createFileChunk } from "@/utils/file";
import { v4 as uuidv4 } from "uuid";
import { multipartUpload, mergeMultipartFile } from "@/api/request";
export default defineComponent({
  components: { UploadFilled },
  setup() {
    const file = ref<File | undefined>(undefined);

    const changeHandle = (uploadFile: UploadFile) => {
      file.value = uploadFile.raw;
    };

    const exceedHandle = (files: File[]) => {
      file.value = files[0];
    };

    const deleteHandle = () => {
      file.value = undefined;
    };

    const uploadHandle = () => {
      let flag = true;
      if (file.value !== undefined) {
        const fileChunkList = createFileChunk(file.value);
        const key = uuidv4();
        const total = fileChunkList.length;
        const handle = async () => {
          if (fileChunkList.length && flag) {
            const file = fileChunkList.pop();
            const number = fileChunkList.length;
            const formData = new FormData();
            formData.append("file", file!);
            formData.append("key", key);
            formData.append("number", number.toString());
            const res = await multipartUpload(formData);
            if (res !== null && res.code === 0) {
              await handle();
            } else flag = false;
          }
        };
        const promiseArr = [];
        for (let i = 0; i < 5; i++) {
          promiseArr.push(
            new Promise(async (res) => {
              await handle();
              res(undefined);
            })
          );
        }
        Promise.all(promiseArr).then(async () => {
          if (flag && !fileChunkList.length) {
            await mergeMultipartFile({ key: key, total });
          }
        });
      }
    };

    return { file, changeHandle, exceedHandle, deleteHandle, uploadHandle };
  },
});
</script>

<style lang="scss" scoped>
</style>