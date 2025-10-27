<template>
  <el-skeleton :rows="5" animated v-if="skeleton" />
  <div class="upload-page" v-else>
    <div class="gif">
      <img src="/gif/upload.gif" alt="" />
    </div>
    <el-empty
      description="暂无记录"
      v-if="
        uploadedList.length === 0 &&
        fileList.length === 0 &&
        Object.keys(uploadingObj).length === 0
      "
    />
    <div class="scroll" v-else>
      <div class="btn">
        <el-button type="primary" link @click="delClick"
          >删除所有记录</el-button
        >
      </div>
      <el-scrollbar>
        <div class="card" v-for="(value, key) of uploadingObj" :key="key">
          <div class="file-name" :title="value.file.name">
            {{ value.file.name }}
          </div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="value.finished"
          />
          <div class="icon" @click="closeHandle('uploading', key)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>

        <div class="card" v-for="(value, index) in fileList" :key="index">
          <div class="file-name" :title="value.file.name">
            {{ value.file.name }}
          </div>
          <el-progress :text-inside="true" :stroke-width="16" :percentage="0">
            <span>等待上传...</span>
          </el-progress>

          <div class="icon" @click="closeHandle('wait', index)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>

        <div class="card" v-for="(item, index) in uploadedList" :key="index">
          <div class="file-name" :title="item.fileName">
            {{ item.fileName }}
          </div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="100"
            status="success"
          />
          <div class="size-time">
            <div class="file-size">
              {{ item.size }}
            </div>
            <div class="file-time">
              {{ item.time }}
            </div>
          </div>

          <div class="icon" @click="closeHandle('uploaded', index, item.id)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div></div>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onMounted,
  ref,
  reactive,
  watch,
} from "vue";
import { dateFormat, notice } from "@/utils/common";
import { delAllRecord } from "@/api/request";
import { useUploadFileStore } from "@/store/upload-file-store";
import { storeToRefs } from "pinia";

export default defineComponent({
  setup() {
    const store = useUploadFileStore();
    const skeleton = ref(true);

    // const uploadingObj = computed(() => {
    //   return store.uploadingObj;
    // });
    const { uploadingObj } = storeToRefs(store);

    const fileList = computed(() => {
      return store.fileList;
    });

    const uploadedList = computed(() => {
      return store.uploadedList;
    });

    const getDate = (date: string) => {
      return dateFormat(date, "yyyy-MM-dd hh:mm");
    };

    const closeHandle = async (param: string, index: number, id: string) => {
      if (param === "wait") {
        store.fileList.splice(index, 1);
      } else if (param === "uploaded") {
        await store.deleteUploaded(id, index);
      } else {
        store.deleteUploading(id);
      }
    };

    const delClick = async () => {
      const data = await delAllRecord();
      if (data != null && (data as any).code === 0) {
        await store.setUploadedList();
        notice("success", "成功", "删除成功");
      }
    };

    // watch(uploadingObj.value, (newVal) => {
    //   console.log(newVal);
    // });

    onMounted(async () => {
      console.log(store.uploadingObj, Object.keys(uploadingObj.value));
      skeleton.value = true;
      console.log("123");
      //   await store.dispatch("initUploadedList", undefined);
      await store.setUploadedList();
      skeleton.value = false;
    });

    return {
      skeleton,
      uploadingObj,
      fileList,
      uploadedList,
      closeHandle,
      getDate,
      delClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.upload-page {
  height: 100%;
  .gif {
    margin: 0 auto;
    width: 300px;
    height: 300px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .scroll {
    .btn {
      height: 25px;
      position: relative;
      .el-button {
        position: absolute;
        right: 0px;
      }
    }
    height: calc(100% - 350px);
    .card {
      height: 100px;
      border: 1px solid #c4c4c4;
      box-shadow: 0px 2px 4px rgba(196, 196, 196, 0.5);
      border-radius: 10px;
      margin-bottom: 15px;
      position: relative;
      .file-name {
        height: 40px;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 18px;
        line-height: 45px;
        margin-left: 10%;
        width: 80%;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        // margin-bottom: 5px;
      }
      .el-progress {
        width: 80%;
        margin: 0 auto;
        :deep() .el-progress-bar__innerText {
          color: black;
          line-height: 12px;
        }
      }
      .size-time {
        height: 20px;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 20px;
        margin: 5px 10% 0;
        color: #8c8c8c;
        position: relative;
        .file-size {
          position: absolute;
          left: 0;
        }
        .file-time {
          position: absolute;
          right: 0;
        }
      }

      .icon {
        position: absolute;
        top: 10px;
        right: 10px;
        color: #8c8c8c;
        cursor: pointer;
      }
    }
  }
}
</style>