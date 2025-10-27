<template>
    <div class="main">
        <div class="header">
            <svg t="1694154128190" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7915"
                width="200" height="200">
                <path
                    d="M892.181695 850.874576a176.162712 176.162712 0 0 0-63.435932-12.235932c-47.728814 0-73.762712 16.141017-108.474577 54.410848l-3.471186 3.991864v-178.766102h153.6V616.135593H716.8V495.945763h153.6V393.458983H716.8V273.529492h153.6v-102.4H716.8V0H614.4v887.235254c-31.761356-34.191186-57.708475-48.59661-103.181017-48.59661-51.460339 0-78.101695 18.484068-116.024407 63.002034-21.434576 25.079322-28.029831 29.678644-42.782373 29.678644s-21.347797-4.599322-42.782372-29.678644c-38.096271-44.517966-64.477288-63.002034-115.937628-63.002034a207.056271 207.056271 0 0 0-120.10305 38.53017 197.944407 197.944407 0 0 0-40.612882 36.447457Q16.141017 929.670508 0 945.898305l70.117966 62.828475c6.595254-7.029153 17.96339-18.397288 31.327458-30.89356l5.03322-5.640678a122.012203 122.012203 0 0 1 87.300339-41.220339c14.665763 0 21.347797 4.599322 42.782373 29.678644 38.183051 44.517966 64.477288 63.002034 115.937627 63.002034s78.101695-18.570847 116.024407-63.002034c21.434576-25.079322 28.029831-29.678644 42.782373-29.678644s21.347797 4.599322 42.695593 29.678644S616.135593 1024 676.881356 1024c52.067797 0 84.43661-34.711864 108.995254-63.002034 21.434576-25.079322 28.11661-29.678644 42.869153-29.678644 21.521356 0 49.898305 13.190508 79.40339 36.100339a338.440678 338.440678 0 0 1 42.695593 39.918644l73.155254-59.357288q-82.093559-75.237966-131.818305-97.106441z"
                    p-id="7916"></path>
                <path
                    d="M200.027119 445.179661C182.237288 482.321356 173.559322 512.867797 173.559322 535.864407c0 104.135593 84.870508 188.485424 190.915254 192.564068 106.044746-4.078644 190.915254-88.602034 190.915255-192.564068 0-22.99661-8.677966-53.543051-26.033899-90.684746a838.031186 838.031186 0 0 0-58.663051-101.879322c-22.562712-34.104407-45.125424-64.824407-60.138305-84.52339-17.355932-22.649492-29.071186-37.054915-30.63322-38.964068a18.136949 18.136949 0 0 0-30.025763 0c-1.562034 1.909153-13.450847 16.314576-30.63322 38.964068-15.012881 19.698983-37.575593 50.418983-60.138305 84.52339a838.031186 838.031186 0 0 0-59.096949 101.879322z m176.249491 167.745085a97.36678 97.36678 0 0 0 60.745763-89.209492A22.475932 22.475932 0 0 1 459.932203 501.412881a22.389153 22.389153 0 0 1 22.389153 22.475933 142.318644 142.318644 0 0 1-88.949153 130.169491 22.99661 22.99661 0 0 1-8.677966 1.735593 22.475932 22.475932 0 0 1-8.677966-43.38983zM156.810847 286.372881A106.131525 106.131525 0 0 0 260.338983 182.237288a124.615593 124.615593 0 0 0-14.318644-49.204068 467.915932 467.915932 0 0 0-31.848136-55.191864 834.117424 834.117424 0 0 0-32.629152-45.906441C172.257627 19.351864 165.749153 11.454915 164.881356 10.500339a9.719322 9.719322 0 0 0-16.227797 0c-0.867797 0.954576-7.376271 8.677966-16.661695 21.087458-8.157288 10.673898-20.39322 27.335593-32.629152 45.90644a467.915932 467.915932 0 0 0-31.848136 55.191865A124.615593 124.615593 0 0 0 53.195932 182.237288a106.131525 106.131525 0 0 0 103.614915 104.135593z m6.334916-62.654915a52.848814 52.848814 0 0 0 33.23661-48.423051A12.149153 12.149153 0 0 1 208.271186 163.232542a12.149153 12.149153 0 0 1 12.149153 12.149153 77.233898 77.233898 0 0 1-48.249492 70.812203 12.930169 12.930169 0 0 1-4.686101 0.954577 12.149153 12.149153 0 0 1-4.686102-23.430509z"
                    p-id="7917"></path>
            </svg>
            <text>潮位数据详情</text>
        </div>

        <div class="back-shadow">
            <div class="Position">
                    <text class="larger" style="margin-top: 10px;">
                        当前位置:
                    </text><br>
                    <div class="box">
                        <text class="default">[ 经度 , 纬度 ] : </text>
                        <div class="data" style="width: 60%;">{{ '[ '+props.lng!.value+' , '+props.lat!.value+' ]' }}</div>
                    </div>
            </div>

            <div class="Water">
                <text class="larger">
                    临近站点：</text><br>
                <div class="card2">
                    <div class="one-Satation-info">
                        <div style="display: flex;"><text class="default" style="flex: 2;text-align: center;">站点名:</text>
                            <div class="data" style="flex: 3;">{{ props.station_1!.name }}</div>
                        </div>

                    </div>

                    <div class="one-Satation-info">
                        <div style="display: flex;"><text class="default" style="flex: 2;text-align: center;">站点名:</text>
                            <div class="data" style="flex: 3;">{{ props.station_2!.name }}</div>
                        </div>
                    </div>

                </div>
            </div>
            
            <text class="larger" style="padding: 10px;">水位预报：</text>
            <div id="chart"></div>
        </div>

        

    </div>
</template>
<script setup lang="ts">
import { WaterStation } from '@/type';
import { PropType, onMounted, watch, computed, ref } from 'vue';
import { ElSelect,ElOption } from 'element-plus';
import * as echarts from 'echarts';
import { updateDataList } from '@/api/request';

const selected = ref(0);
var myChart:any = null;
const props = defineProps({
    flag: Boolean,
    lng: Object as PropType<Number>,
    lat: Object as PropType<Number>,
    water: Array,
    station_1: Object as PropType<WaterStation>,
    station_2: Object as PropType<WaterStation>,
    startTime: Object
})

const getTimeObj = (y: number, m: number, d: number, h: number) => {
    let timeObj = {
        y, m, d, h,
        str: '',
    }
    timeObj.str = h + ':00';
    return timeObj;
}

const options:Array<{value:number,label:string}> = [];
const xAxis_dt :Array<string> = [];
const yAxis_dt :Array<number> = [];



watch(props,(newV)=>{
    console.log(newV);
    updateData();
    setChartOptions();
})

const updateData=()=>{
    let startT = getTimeObj(props.startTime!.year, props.startTime!.month, props.startTime!.day, props.startTime!.hours);
    xAxis_dt.splice(0);
    yAxis_dt.splice(0);

    for (let i = 0; i < 12; i++) {
    
        let tempObj = getTimeObj(startT.y, startT.m, startT.d, (startT.h + i)%24);
        xAxis_dt.push(tempObj.str);
    }

    
    for (let i=0;i<12;i++){
        let wat = props.water!.value[i] as number;
        yAxis_dt.push(wat);
    }
}

const setChartOptions=()=>{
    // console.log('x data', xAxis_dt);
    let option = {
        title: {
            left: 'center',
            text: '12小时水位预报折线图'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis_dt
        },
        yAxis: {
            type: 'value'
        },
        tooltip: { show: true, trigger: 'axis' },
        series: [
            {
                data: yAxis_dt,
                type: 'line',
                emphasis: {
                    focus: 'series'
                },
                areaStyle: {
                    color:'#85C1E9'
                },
            }
        ],
   
    };
    option && myChart.setOption(option);
}


onMounted(() => {
  
    
    var chartDom = document.getElementById('chart');
    myChart = echarts.init(chartDom);

})


</script>
<style lang="scss" scoped>
.main {
    height: auto;
    margin-top: 4px;

    .back-shadow {
        width: calc(100% - 2px);
        border: #dbdbdb50 1px solid;
        border-radius: 7px;
        // border-radius: 25px;
        background: rgb(194, 227, 255);

        box-shadow: 0px 0px 5px rgba(65, 105, 225, 0.315);
    }

    .header {
        line-height: 30px;
        display: flex;
        width: 100%;
        background-color: aliceblue;
        border-radius: 7px;
        margin-bottom: 5px;

        text {
            font-size: 22px;
            font-weight: 700;
            color: #303133;
            top: 0%;
            align-items: center;
            padding: 5px;

        }

        svg {
            width: 2rem;
            height: 2rem;
            fill: rgb(21, 60, 168);
            padding: 5px;
        }
    }

    .Position {
        padding: 10px;
        .box{
            display: flex;
        }
    }

    .Water {
        padding: 10px;

        .card2 {
            width: 100%;
            display: flex;

            .one-Satation-info {
                flex: 1;
                width: 200px;

            }
        }

    }
}

text.larger {
    margin-top: 5px;
    font-size: 18px;
    font-weight: 600;
}

text.default {
    font-size: 16px;
}

.data {
    font-size: 16px;
    border: 2px solid #b1dcf3;
    background-color: aliceblue;
    text-align: center;
}

.card {
    margin-top: 1rem;
    margin-bottom: 1rem;
}

#chart {
    width:390px;
    height: 280px;
}
</style>