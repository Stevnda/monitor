import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";

const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        name: "home",
        component: () => import("@/views/HomeView.vue"),
        meta: {
          keepAlive: true,
        },
      },
    ],
  },
  {
    path: "/register",
    name: "register",
    component: () => import("@/views/RegisterView.vue"),
  },
  {
    path: "/test",
    name: "test",
    component: () => import("@/views/test.vue"),
  },
  {
    path: "/dataViewer",
    name: "dataViewer",
    component: () => import("@/views/DataViewWrapper.vue"),
    meta: {
      keepAlive: true,
    },
  },
  {
    path: "/flow",
    name: "flow",
    component: () => import("@/views/FlowView.vue"),
    meta: {
      keepAlive: false,
    },
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue"),
  },

  {
    path: "/404",
    name: "404",
    component: () => import("@/views/404.vue"),
  },
];

export const asyncRouters: Array<RouteRecordRaw> = [
  {
    path: "/resource",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        redirect: "/resource/list",
      },
      {
        path: "list",
        name: "resourceList",
        component: () => import("@/views/ResourceListView.vue"),
        meta: {
          requiresAuth: "member",
          keepAlive: true,
        },
      },
      {
        path: ":id",
        name: "resourceDetail",
        component: () => import("@/views/ResourceDetailView.vue"),
        meta: {
          requiresAuth: "member",
        },
      },
    ],
  },
  {
    path: "/waterway",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        name: "waterway",
        component: () => import("@/views/WaterwayView.vue"),
        meta: {
          requiresAuth: "member",
          keepAlive: true,
        },
      },
    ],
  },
  {
    path: "/analysis",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        redirect: "/analysis/list",
      },
      {
        path: "list",
        name: "analysisList",
        component: () => import("@/views/AnalysisListView.vue"),
        meta: {
          requiresAuth: "member",
          keepAlive: true,
        },
      },
      {
        path: ":id",
        name: "analysisDetail",
        component: () => import("@/views/AnalysisDetailView.vue"),
        meta: {
          requiresAuth: "member",
        },
      },
    ],
  },
  {
    path: "/admin",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        redirect: "/admin/folder",
      },
      {
        path: "folder",
        name: "folderAdmin",
        component: () => import("@/views/FolderAdminView.vue"),
        meta: {
          requiresAuth: "admin",
          keepAlive: true,
        },
      },
      {
        path: "resource",
        name: "resourceAdmin",
        component: () => import("@/views/ResourceAdminView.vue"),
        meta: {
          requiresAuth: "admin",
          // keepAlive: true,
        },
      },
      {
        path: "createResource",
        name: "createResource",
        component: () => import("@/views/CreateResourceView.vue"),
        meta: {
          requiresAuth: "admin",
          keepAlive: true,
        },
      },
      {
        path: "updateResource/:id",
        name: "updateResource",
        component: () => import("@/views/UpdateResourceView.vue"),
        meta: {
          requiresAuth: "admin",
          keepAlive: true,
        },
      },
    ],
  },
  {
    path: "/waterForecast",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        name: "WaterForecast",
        component: () => import("@/views/WaterForecastWrapper.vue"),
        meta: {
          requiresAuth: "member",
          keepAlive: true,
        },
      },
    ],
  },
  {
    path: "/stormPrediction",
    component: () => import("@/layout/Index.vue"),
    children: [
      {
        path: "",
        name: "StormPrediction",
        component: () => import("@/views/StormPrediction.vue"),
        meta: {
          requiresAuth: "member",
          keepAlive: true,
        },
      },
    ],
  },
  {
    path: "/:catchAll(.*)",
    name: "Redirect404",
    redirect: "/404",
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
});

export const resetRouters = () => {
  const newRouter = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes,
  });
  const removeList: string[] = [];
  router.getRoutes().forEach((item) => {
    for (let i = 0; i < newRouter.getRoutes().length; i++) {
      if (item.name === newRouter.getRoutes()[i].name) {
        return;
      }
    }
    removeList.push(item.name as string);
  });
  removeList.forEach((item) => {
    router.removeRoute(item);
  });
};

export default router;
