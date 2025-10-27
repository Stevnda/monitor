<template>
  <div class="data-table-wrapper">
    <div class="table-title">{{ tableData.name }}</div>
    <table class="tb-container" ref="dataTable">
      <thead>
        <tr>
          <th v-for="head in tableData.tHead" :key="tableData.tHead.indexOf(head)">
            <h1>{{ head }}</h1>
          </th>
        </tr>
      </thead>
      <tbody ref="tableBody">
        <tr v-for="row in tableData.tBody">
          <td v-for="cell in row">{{ cell }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { type StringKeyObject, ChartDataPreparer } from "@/utils/viewerData";

interface Props {
    projectId: string
}

const props = defineProps<Props>();

console.log("table project id", props.projectId)

const chartDatapreparer = new ChartDataPreparer(0);
let curTableData = await chartDatapreparer.buildChartOption(props.projectId);

// const data = await 

let tableData = ref({
    'name': (curTableData as StringKeyObject)["name"]+'表面流轨迹',
    'tHead': [
      '时间', 'X', 'Y', '流速'
    ],
    'tBody': (curTableData as StringKeyObject)["data"]
})
// const rowNum = tableData.value.tBody.length;

const dataTable = ref<HTMLTableElement>();
const tableBody = ref<HTMLElement>();

// const upRowNum = 0;
let scrollTable = (() => {
  let firstRow = dataTable.value?.tBodies[0].firstChild;
  // console.log('table scrolled...')
  dataTable.value?.tBodies[0].removeChild(firstRow as Node);
  dataTable.value?.tBodies[0].appendChild(firstRow as Node);
  // tableBody.value?.style.setProperty('--upRowNum', upRowNum.toString());
})

let tableInterval = -1;

const changeTableData = async (curProjectId: string) => {
  clearInterval(tableInterval);
  curTableData = await chartDatapreparer.buildChartOption(curProjectId);
  tableData = ref({
    'name': (curTableData as StringKeyObject)["name"]+'表面流轨迹',
    'tHead': [
      '时间', 'X', 'Y', '流速'
    ],
    'tBody': (curTableData as StringKeyObject)["data"]
  })
  tableInterval = setInterval(scrollTable, 1000);
}

onMounted(() => {
  tableInterval = setInterval(scrollTable, 1000)
    
})


defineExpose({changeTableData});
</script>


<style lang='scss' scoped>
// :root {
//   --upRowNum: 0;
// }

div.data-table-wrapper {
  // position: absolute;
  background-color: transparent;
  // left: 1vw;
  // top: 10vh;
  height: 100%;
  width: 100%;
  overflow: hidden;

  div.table-title {
    position: relative;
    height: 10%;
    // line-height: 15%;
    text-align: center;
    font-size: calc(0.5vw + 0.6vh);
    color: rgba(255, 255, 255, 0.8);
    font-weight: 600;
  }

  table.tb-container {
    overflow: hidden;
    width: 100%;
    height: 88%;
    // margin: -0.1em -0.3em -0.1em -0.1em;
    display: table;
    h1 {
      font-size: 0.8vw;
      line-height: 0.5em;
      text-align: center;
      color: rgba(255, 255, 255, 0.8);
    }
    td {
      font-weight: normal;
      font-size: 0.7vw;
      box-shadow: 0 2px 2px -2px #0e1119;
      width: 28%;
    }
    td:first-child {
      color: #b7dfff;
      font-weight: 600;
      width: 6%;
    }
    td:last-child {
      color: #aafff1;
      font-weight: 600;
      width: 16%;
    }
    td,
    th {
      // padding-bottom: 1.2%;
      // padding-top: 1.2%;
      padding-left: 0%;
      padding-right: 0%;
      text-align: center;
      color: rgba(255, 255, 255, 1);
    }
    thead {
      position: relative;
      z-index: 10;
      tr:first-child {
        th:first-child {
          border-top-left-radius: 0.6em;
        }
        th:last-child {
          border-top-right-radius: 0.6em;
        }
      }
    }
    tbody {
      position: relative;
      // transform: translateY(calc((-2vh) * var(--upRowNum)));
      // transition: all 2s ease-in-out;
      z-index: 1;
    }
    tr {
      line-height: 2vh;
      &:nth-child(odd) {
        background-color: rgba(5, 28, 56, 0.9);
      }
      &:nth-child(even) {
        background-color: rgba(9, 30, 80, 0.9);
      }
      &:last-child {
        td:first-child {
          border-bottom-left-radius: 0.6em;
        }
        td:last-child {
          border-bottom-right-radius: 0.6em;
        }
      }
      &:hover {
        background-color: rgba(10, 76, 255, 0.8);
        box-shadow: 0 4px 4px -4px #0e1119;
      }
    }
    th {
      background-color: rgba(0, 41, 155, 0.8);
    }
    
    td:hover {
      cursor: pointer;
      background-color: #42aaff;
      color: #dceaff;
      font-weight: bold;
      box-shadow: #21427f -1px 1px, #21427f -2px 2px, #21427f -3px 3px,
        #21427f -4px 4px, #21427f -5px 5px, #21427f -6px 6px;
      transform: translate3d(3px, -3px, 0);
      transition-delay: 0s;
      transition-duration: 0.4s;
      transition-property: all;
      transition-timing-function: line;
    }
  }
}
</style>