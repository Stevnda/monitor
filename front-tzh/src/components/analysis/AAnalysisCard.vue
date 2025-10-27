<template>

<div class="card ">

    <div class="img">
      
        <Delete class="icon" @click="deleteHandle" /> 

        <el-avatar shape="square" fit="cover" v-if="info!.avatar === ''">{{
            info!.projectName
        }}</el-avatar>

        <img :src="'/monitor/visual/getAvatar/' + info!.avatar" v-else />
    </div>

    <div class="text">
        <p class="h3">{{ info!.projectName }}</p>
        <p class="p">{{ userName }}</p> 
        <p class="p">{{ time }}</p> 
        
        <div class="icon-box">
            <p class="span" @click="clickHandle">查看详情</p>
        </div>
    </div>
</div>
</template>

<script lang="ts" setup>

import { computed } from 'vue';
import { dateFormat } from '@/utils/common';
import roter from '@/router';
import { usePermissionStore } from "@/store/permission-store";
import {deleteAnalysisCase} from "@/api/request";
import { ElMessage, ElMessageBox } from 'element-plus';

const props = defineProps({
  info: {
    type: Object,
  },
  keyword: {
    type: String,
  },
  selected:{
    type: Boolean
  }
});

const emits = defineEmits(["refreshFlag"]);


const store = usePermissionStore();
const info = computed(() => {
  return props.info;
});

const time = computed(() => {
  return dateFormat((props.info as any).createTime, "yyyy-MM-dd hh:mm");
});

const userName = computed(() => {
  return store.name;
});

const clickHandle = ()=>{
    roter.push({
      name: "analysisDetail",
      params: {
        id: (info.value as any).id,
      }
    })
}


const deleteHandle = async()=>{
  // console.log(props.info!.id);
  ElMessageBox.confirm(
        '是否确认删除选中条目：' + props.info!.projectName,
        'Warning',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      ).then(async() => {
          // Delete 操作
          await deleteAnalysisCase(props.info!.id);
          emits("refreshFlag", true);


          ElMessage({
            type: 'success',
            message: '删除成功',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '删除失败',
          })
        })

 

}



</script>

<style lang="scss" scoped>
.card {
  margin-top: 3svh;
  margin-left:1vw;
  margin-bottom: 5vh;
  width: 16vw;
  background: #CCFFFF;
  border-radius: 10px;
  box-shadow: 0px 0px 13px -2px #bebebe;
  transition: 0.2s ease-in-out;
  position: relative;

  .icon{
    width: 27px;
    height: 27px;
    position: absolute;
    left: 4px;
    top: 4px;
    &:hover{
      cursor:pointer;
      color: rgb(0, 47, 255);
      scale: 1.1;
      transition: 0.2s;
    }
  }




  .img {
  width: 100%;
  height: 7em;
  border-top-left-radius: 100px;
  border-top-right-radius: 10px;
  background: linear-gradient(to right, #66CCCC, #CCFFFF);
  display: flex;
  align-items: top;
  justify-content: right;
  }

  &:hover {

    .img {
      border-top-left-radius: 10px;
      transition: 0.5s ease-in-out;
    }
    transition: 0.5s ease-in-out;
  }

  .icon-box {
    margin: 10px;
    /* padding: 12px; */
    cursor: pointer;
    background-color: #66CCCC;
    border-radius: 10px;
    text-align: center;
    .span{
      font-family: system-ui;
      font-size: small;
      font-weight: 500;
      color: #fff;
    };
    &:hover{
      background-color:  #009999;
      transition-duration: .2s;
    }


  }

  .text {
    padding: 0px 20px;
    display: flex;
    flex-direction: column;
    align-items: space-around;
    .h3 {
      font-family: system-ui;
      font-size: 3vh;
      font-weight: 600;
      color: black;
      text-align: center;
    }
    .p {
      font-family: system-ui;
      color: #999999;
      font-size: 13px;
      margin: 0px;
      text-align: center;
      padding: 5px;
    }
  }

}

</style>