<template>
  <el-upload
    action="/monitor/project/uploadAvatar"
    :show-file-list="false"
    :on-success="successHandle"
    :on-change="changeHandle"
  >
    <img v-if="imageUrl != ''" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
  </el-upload>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { Plus } from "@element-plus/icons-vue";
import type { UploadFile } from "element-plus";
export default defineComponent({
  components: { Plus },
  props: {
    pictureName: {
      type: String,
    },
  },
  emits: ["returnPicture"],
  setup(props, context) {
    const imageUrl = ref("");

    const successHandle = (response: any) => {
      if (response != null && response.code === 0) {
        context.emit("returnPicture", response.data);
      }
    };

    const changeHandle = (uploadFile: UploadFile) => {
      if (uploadFile.status === "ready" && uploadFile.raw) {
        imageUrl.value = URL.createObjectURL(uploadFile.raw);
      }
    };

    const clearAvatar = () => {
      imageUrl.value = "";
    };

    const updateImage = (image: string) => {
      imageUrl.value = image;
    };

    onMounted(() => {
      if (props.pictureName) {
        imageUrl.value = props.pictureName;
      }
    });
    return { imageUrl, changeHandle, successHandle, clearAvatar, updateImage };
  },
});
</script>

<style lang="scss" scoped>
:deep() .el-upload {
  border: 1px dashed #8c939d;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  .avatar {
    width: 100%;
    height: 100%;
    display: block;
  }
}
:deep() .el-upload:hover {
  border-color: #a6bed7;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}
</style>