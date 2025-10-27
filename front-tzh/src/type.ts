export interface ResponseType {
  code: number;
  msg: string;
  data: any;
}

export interface ProjectType {
  id: string;
  projectName: string;
  avatar: string;
  description: string;
  institution: string;
  location: string;
  time: string;
  type: string;
}

export interface DataListType {
  id: string;
  name: string;
  location: string[];
  description: string;
  tags: string[];
  createTime: string;
  updateTime: string;
  download: number;
  watch: number;
  avatar: string;
  provider: string;
  range: string;
  type: string;
  providerPhone: string;
  providerEmail: string;
  providerAddress: string;
  detail: string;
  timeStamp: string;
}

export interface BridgeType {
  id: string;
  bridgeAreaRange: string;
  bridgeCulvertMarker: string;
  navRulesWaterRoutes: string;
  navAssessmentApproval: string;
  maritimeAdminAgency: string;
  bridgeAttributes: string;
  designNavRepTypeFleet: string;
  bridgePillarLights: string;
  sectionNavRepTypeFleet: string;
  bridgeActiveCollisionPrev: string;
  bridgeOpMgmtUnit: string;
  openingDate: string;
  bridgePassiveCollisionPrev: string;
  avgDailyCrossFlow: string;
  bridgeAreaVideoFacilities: string;
  waterTrafficSafetyMgmt: string;
  riverFacilitiesInArea: string;
  other: string;
  mainNavSpanNumber: string;
  anchorageBerthingArea: string;
  crossRiverFacilities: string;
  waterNavMarksArrangement: string;
  smallVesselNavSpanNumber: string;
  polygon: { coordinates: number[][][]; type: string };
  navMaintenanceUnit: string;
  bridgeName: string;
  name: string;
  mainNavPierCollisionPrev: string;
}

export type WaterLevelChartType = {
  timeList: string[];
  legend: string[];
  yAxis: {
    alignTicks: boolean;
    type: string;
    offset?: number;
    axisLine: {
      show: boolean;
      lineStyle: {
        color: string;
      };
    };
  }[];
  series: {
    name: string;
    data: number[];
    type: string;
    smooth: boolean;
    yAxisIndex: number;
    itemStyle: {
      normal: {
        color: string; //改变折线点的颜色
        lineStyle: {
          color: string; //改变折线颜色
        };
      };
    };
  }[];
};

export type WaterLevel =
  | {
      time: string;
      waterLevel: number;
      flow: number;
    }
  | {
      upstreamWaterLevel: number;
      downstreamWaterLevel: number;
      flow: number;
      time: string;
    }
  | {
      rainfall: number;
      waterLevel: number;
      input: number;
      output: number;
      time: string;
    };

export type StationType = {
  name: string;
  nameEn: string;
  keys: { key: string[] };
  keysCn: { key: string[] };
  longitude: number;
  latitude: number;
  type: string;
  startTime: { key: number[] };
  prediction: number;
};

export type SearchTable =
  | {
      mmsi: string;
      name: string;
    }
  | { sshd: string; hbmc: string }
  | { mc: string; yt: string }
  | { mdmc: string; yt: string }
  | { 桥梁属性: string; 桥梁名称: string }
  | { name: string };

export type BuoyType = {
  id: string;
  picture: string;
  photo: string;
  color: string;
  des: string;
  name: string;
  shape: string;
  longitude: number;
  latitude: number;
  waterway: string;
  noMeaning: string;
};

export type AnchorType = {
  id: string;
  anchorName: string;
  waterwayName: string;
  type: string;
  longitude: number;
  latitude: number;
  picture: string;
  management: string;
  hdHdlyName: string;
  tyAnbName: string;
  usage: string;
  buildTime: string;
  hdlc: number;
  region: {
    type: string;
    points: number[][];
  };
  hdHdly: string;
  tyAnb: string;
  sdName: string;
};

export type ParkType = {
  id: string;
  name: string;
  hdHdly: string;
  hdHdlyName: string;
  hdlc: number;
  tyAnb: string;
  tyAnbName: string;
  type: string;
  width: string;
  usage: string;
  longitude: number;
  latitude: number;
  picture: string;
  waterwayId: string;
  waterwayName: string;
  management: string;
  region: {
    type: string;
    point?: number[];
    points?: number[][];
  };
  shipWay: string;
};

export type ShipType = {
  mmsi: string;
  name: string;
  nameCn: string;
  updateTime: string;
  longitude: number;
  latitude: number;
  speed: string;
  course: string;
  draft: string;
  length: string;
  width: string;
  classType: string;
};

export type Meteorology = {
  description: string;
  effective: string;
  headline: string;
  id: string;
  longitude: number;
  latitude: number;
  title: string;
  type: string;
};

export type Section = {
  id: string;
  name: string;
};

export type AnalysisDataset = {
  id: string;
  fileName: string;
  visualId: string;
  visualType: string;
};

export type AnalysisParameter = {
  fileId: string;
  fileName: string;
  dataListId: string;
  dataListName: string;
  visualId: string;
  visualType: string;
};

export type TreeData = {
  label: string;
  flag: boolean;
  children: TreeData[];
  id: string;
  checkFlag?: boolean;
  visualId?: string;
  visualType?: string;
  parentId?: string;
  parentName?: string;
};

export type BindDataFileInfo = {
  id: string;
  fileName: string;
  address: string;
  size: string;
  visualType: string;
  visualId: string;
  view: string;
};

export type FolderType = {
  id: string;
  folderName: string;
  parentId: string;
  flag: boolean;
};
export type FileType = {
  id: string;
  fileName: string;
  address: string;
  visualType: string;
  size: string;
  parentId: string;
  visualId: string;
  view?: string;
};

export type WaterStation={
  name?:string,
  nameEn:string,
  lng:number,
  lat:number,
  water?:Array<number>,
}

export type UploadRecord = {
  id: string;
  fileName: string;
  uploadTime: string;
  size: string;
};
