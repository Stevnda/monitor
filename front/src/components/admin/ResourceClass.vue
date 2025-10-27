<template>
  <div class="resource-class">
    <el-affix :offset="100">
      <div class="title">项目分类</div>
      <div
        :class="active === 'all' ? 'class-all active' : 'class-all'"
        @click="clickHandle('all')"
      >
        {{ "全部类别" }}
      </div>
      <div v-for="(item, index) in classList" :key="index" class="class-item">
        <div class="class-title">{{ item.label }}</div>
        <div class="child">
          <div
            v-for="(childItem, childIndex) in item.value"
            :key="childIndex"
            :class="active === childItem ? 'child-label active' : 'child-label'"
            @click="clickHandle(childItem)"
          >
            {{ childItem }}
          </div>
        </div>
      </div>
    </el-affix>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { debounce } from "@/utils/common";
import { classList } from "@/common-config";
export default defineComponent({
  emits: ["changeType"],
  setup(_, context) {
    const active = ref("all");

    const clickHandle = (val: string) => {
      const handle = () => {
        active.value = val;
        context.emit("changeType", val);
      };
      debounce(handle, 100)();
    };

    return { active, classList, clickHandle };
  },
});
</script>

<style lang="scss" scoped>
.resource-class {
  background: #f5f5f7;

  .title {
    font-size: 2rem;
    cursor: pointer;
  }
  .class-all {
    margin-top: 1rem;
    cursor: pointer;
    font-size: 1.2rem;
    font-weight: 600;
    &::before {
      content: "";
      width: 4px;
      height: 4px;
      border-radius: 2px;
      display: inline-block;
      background: black;
      margin-right: 0.4rem;
      top: -0.3rem;
      position: relative;
    }
  }
  .class-item {
    margin-top: 1rem;
    cursor: pointer;
    .class-title {
      font-size: 1.2rem;
      font-weight: 600;
      &::before {
        content: "";
        width: 4px;
        height: 4px;
        border-radius: 2px;
        display: inline-block;
        background: black;
        margin-right: 0.4rem;
        top: -0.3rem;
        position: relative;
      }
    }
    .child {
      display: flex;
      flex-flow: row wrap;
      .child-label {
        margin-left: 0.5rem;
        line-height: 1.8rem;
        font-style: italic;
      }
    }
  }
  .active {
    color: #ff7049;
  }
}
</style>