<template>
    <div class="Create">
      <p class="title">创建文件夹</p>
          <div class="flex">
          <label>
              <input 
              placeholder="请输入文件名" 
              type="text" class="input"
              v-model="input" ref="inputFocus"
              >
          </label>
      </div>  
      <button class="confirm" @click="commit">确定</button>
    
    </div>



  </template>
  
  <script lang="ts">
  import { defineComponent, nextTick, onMounted, ref } from "vue";
  import { notice } from "@/utils/common";
  export default defineComponent({
    emits: ["createFolder"],
    props: {
      folderNames: {
        type: Array,
      },
    },
    setup(props, context) {
      const input = ref("");
      const inputFocus = ref<HTMLElement>();
      const commit = () => {
        let flag = true;
        props.folderNames?.forEach((item) => {
          if (item === input.value) {
            flag = false;
          }
        });
        if (flag) {
          context.emit("createFolder", input.value);
        } else {
          notice("warning", "警告", "文件重名!");
        }
      };
      nextTick(() => {
        inputFocus.value?.focus();
      });
  
      return {
        input,
        commit,
        inputFocus,
      };
    },
  });
</script>
  
<style lang="scss" scoped>
     .Create {
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-width: 350px;
      background-color: #fff;
      padding: 20px;
      border-radius: 25px;
      position: relative;
    }

    .title {
      font-size: 25px;
      color: royalblue;
      font-weight: 600;
      letter-spacing: -1px;
      position: relative;
      display: flex;
      align-items: center;
      padding-left: 30px;
      margin: 5px 0px;
    }

    .title::before,.title::after {
      position: absolute;
      content: "";
      height: 16px;
      width: 16px;
      border-radius: 50%;
      left: 0px;
      background-color: royalblue;
    }

    .title::after {
      width: 18px;
      height: 18px;
      animation: pulse 1s linear infinite;
    }

    .Create label .input {
      width: 80%;
      padding: 10px 10px 10px 10px;
      outline: 0;
      border: 1px solid rgba(105, 105, 105, 0.397);
      border-radius: 10px;
    }


    .confirm{
      border: none;
      outline: none;
      width: 90%;
      background-color: royalblue;
      padding: 10px;
      border-radius: 10px;
      color: #fff;
      font-size: 16px;
      transform: .3s ease;
    }

    .confirm:hover {
      background-color: rgb(56, 90, 194);
    }

    @keyframes pulse {
      from {
        transform: scale(0.9);
        opacity: 1;
      }
      to {
        transform: scale(1.8);
        opacity: 0;
      }
    }
</style>