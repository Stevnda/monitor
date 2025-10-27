<template>
  <div ref="statisticChart" class="statisticChart"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import { getDataGroup } from "@/api/request";
import { getLastOrNextFewDateBy } from "@/utils/common";
import * as echarts from "echarts";
import router from "@/router";
export default defineComponent({
  setup() {
    const statisticChart = ref<HTMLElement>();
    let myChart: echarts.ECharts;
    let option: any = {};

    const initData = async (id: string) => {
      const data = await getDataGroup(id, -9);
      if (data && data.code === 0) {
        const nowDate = new Date();
        const dateList = getLastOrNextFewDateBy(
          nowDate.toLocaleDateString(),
          -10
        );
        const valueLiest: number[] = [];
        dateList.forEach((item) => {
          if (data.data.length === 0) {
            valueLiest.push(0);
          }
          for (let i = 0; i < data.data.length; i++) {
            if (item === data.data[i].date) {
              valueLiest.push(data.data[i].sum);
              return;
            }
            if (i === data.data.length - 1) {
              valueLiest.push(0);
            }
          }
        });

        option = {
          legend: {
            data: ["访问量"],
            top: 10,
          },
          xAxis: {
            type: "category",
            data: dateList,
            axisLabel: {
              interval: dateList.length - 2,
            },
            axisTick: {
              show: false,
            },
          },
          yAxis: {
            type: "value",
          },
          grid: {
            x: 40,
            y: 40,
            x2: 30,
            y2: 40,
          },
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "shadow",
            },
          },
          series: [
            {
              name: "访问量",
              data: valueLiest,
              type: "line",
              itemStyle: {
                color: "#60c696", //改变折线点的颜色
                lineStyle: {
                  color: "#60c696", //改变折线颜色
                },
              },
            },
          ],
        };
      }
    };

    const setData = async (id: string) => {
      await initData(id);
      myChart.setOption(option);
    };

    watch(
      () => router.currentRoute.value.params.id,
      async (newVal) => {
        if (newVal !== undefined) {
          await setData(newVal as string);
        }
      }
    );

    onMounted(async () => {
      await initData(router.currentRoute.value.params.id as string);
      myChart = echarts.init(statisticChart.value as HTMLElement);
      myChart.setOption(option);
    });

    return {
      setData,
      statisticChart,
    };
  },
});
</script>


<style lang="scss" scoped>
.statisticChart {
  height: 100%;
  width: 100%;
}
</style>