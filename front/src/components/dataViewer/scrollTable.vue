<template>
    <div class="scroll-table">
        <div class="table-title">{{ chartData.name }}</div>
        <div class="table-head">
            <div v-for="head in chartData.head" class="head-item">{{ head }}</div>
        </div>
        <div class="table-body">
            <div v-for="row in chartData.body" class="row-item" :order="row.order">
                <div v-for="cell in row.data" class="cell-item">{{ cell }}</div>
            </div>
        </div>
    </div>
</template>
  
  
<script setup lang='ts'>
import { onMounted } from '@vue/runtime-core';
// import { frontData } from '../../frontData';
import { ref } from 'vue';

// interface Props {
//     mapId: string, 
//     order: string
// }

let chartData = ref({
    name: 'P1', head: ['时间', 'X', 'Y', '流速'], 
    body: [
        {data: ['18:05:25', '578797.43', '3529961.32', '0.4'], order: 1},
        {data: ['18:05:49',	'578798.37', '3529951.74', '0.42' ], order: 2},
        {data: ['18:05:49',	'578798.37', '3529951.74', '0.42' ], order: 3},
        {data: ['18:05:49',	'578798.37', '3529951.74', '0.42' ], order: 4},
    ]
});

// const props = defineProps<Props>();


onMounted(() => {

});

</script>
  
<style lang='scss'>
$orders: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10;


div.scroll-table {
    cursor: pointer;
    background-color: #01275c84;
    backdrop-filter: blur(8px);
    border-radius: 4px;
    width: 100%;
    height: 100%;
    display: flex;
    flex-flow: column nowrap;

    div.table-title {
        font-size: 1.2vw;
        text-align: center;
        height: 10%;
        line-height: 10%;
    }
    
    div.table-head {
        display: flex;
        flex-flow: row nowrap;
        justify-content: space-between;
        height: 12%;
        width: 100%;
        div.head-item {
            font-size: 0.9vw;
            text-align: center;
            height: 100%;
            width: fit-content;
        }
    }

    @each $order in $orders {
        &[order='#{$order}'] {
            order: $order;
        }
    }
}
</style>