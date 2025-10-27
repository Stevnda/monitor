<template>
  <div class="dataList-card" @click="clickHandle">
    <div>
      <img :src="pictureUrl" />
    </div>
    <div class="text-info">
      <div class="title">
        <span v-html="replaceHandle(dataListInfo.name)"></span>
      </div>
      <div class="update-time">
        <strong>上次更新：</strong><span>{{ updateTime }}</span>
      </div>

      <div class="watch-download">
        <el-icon style="margin-right: 5px"><View /></el-icon>
        <span>{{ dataListInfo.watch }}</span>
        <el-icon style="margin: 0 5px"><Download /></el-icon>
        <span>{{ dataListInfo.download }}</span>
      </div>
      <div class="tags">
        <div
          class="tag-png"
          v-for="(item, index) in dataListInfo.tags"
          :key="index"
        >
          <span v-html="replaceHandle(item)"></span>
        </div>
      </div>

      <div class="des">
        <strong>简介：</strong
        ><span v-html="replaceHandle(dataListInfo.description)"></span>
      </div>

      <div @click.stop>
        <el-dropdown trigger="click" @command="commandHandle">
          <el-button type="primary" text>操作</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="update"
                ><el-icon><Edit /></el-icon>修改</el-dropdown-item
              >
              <el-dropdown-item command="delete"
                ><el-icon><DeleteFilled /></el-icon>删除</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, PropType } from "vue";
import { DataListType } from "@/type";
import { dateFormat, imgBase64 } from "@/utils/common";
export default defineComponent({
  props: {
    dataListInfo: {
      type: Object as PropType<DataListType>,
    },
    keyword: {
      type: String,
    },
  },
  emits: ["commandHandle", "clickHandle"],
  setup(props, context) {
    const dataListInfo = computed(() => {
      return props.dataListInfo;
    });

    const updateTime = computed(() => {
      return dateFormat(props.dataListInfo!.updateTime, "yyyy-MM-dd hh:mm");
    });

    const pictureUrl = computed(() => {
      return props.dataListInfo!.avatar
        ? `/monitor/visual/getAvatar/${props.dataListInfo!.avatar}`
        : imgBase64(props.dataListInfo!.name);
    });

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + props.keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + props.keyword + "</span>"
      );
      return currentStr;
    };

    const commandHandle = (val: string) => {
      context.emit("commandHandle", { type: val, id: dataListInfo.value!.id });
    };

    const clickHandle = () => {
      context.emit("clickHandle", dataListInfo.value!.id);
    };

    return {
      dataListInfo,
      updateTime,
      pictureUrl,
      replaceHandle,
      clickHandle,
      commandHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.dataList-card {
  height: 14rem;
  background: #f5f5f7;
  border: solid 1px #d6d6d6;
  border-radius: 8px;
  margin-bottom: 2rem;
  display: flex;
  cursor: pointer;

  img {
    width: 18rem;
    height: 12rem;
    border-radius: 8px;
    margin: 1rem;
  }
  .text-info {
    width: calc(100% - 21rem);
    padding: 1rem 2rem 1rem 0;
    position: relative;
    .el-dropdown {
      position: absolute;
      top: 1rem;
      right: 1rem;
    }
    .title {
      font-size: 1.6rem;
      color: #0066cc;
      font-weight: 600;
    }
    .update-time {
      margin-top: 1rem;
    }

    .watch-download,
    .tags,
    .des {
      margin-top: 0.6rem;
    }
    .des {
      word-break: break-all;
      overflow: hidden;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      line-height: 1.5rem;
    }
    .update-time,
    .des {
      font-size: 0.9rem;
    }
    .tags {
      display: flex;
      .tag-png {
        background: url("/public/png/label-blue-new.png");
        padding: 0 10px;
        background-size: 100% 100%;
        height: 30px;
        margin-right: 5px;
        color: white;
        text-align: center;
      }
    }
  }
}
</style>