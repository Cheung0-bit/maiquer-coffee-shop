# 麦趣咖啡厅 微信浏览器内版本

## 开发环境 

请准备好 `pnpm` 作为包管理工具，并且在VSCode中安装好 `volar` `eslint` `stylelint` 插件

我们应当使用如下指令安装包管理(需要你的 nodejs 版本大于 v16.13)

```bash
corepack enable
corepack prepare pnpm@7.11.0 --activate
```

上面我们安装好了 pnpm

### 安装依赖

接下来我们使用这个指令安装依赖

```bash
pnpm i
```

### 启动调试环境

```shell
pnpm dev
```

### 打包

```shell
pnpm build
```

## 项目结构

### 技术栈

项目使用   
`Vue3` 组合式API进行开发    
`Pinia` 进行状态管理   
`vue-router` 进行路由管理  
`axios` 进行网络请求   
`vite` 进行开发和打包  

对于这些工具的使用，我们应当参考官方文档 [Vue3组合式API](https://cn.vuejs.org/guide/introduction.html#api-styles) , [Pinia](https://pinia.esm.dev/) , [vue-router](https://next.router.vuejs.org/zh/) [axios](https://axios-http.com/zh/),  [vite](https://cn.vitejs.dev/guide/)

### 文件结构

```
├─apis // api接口
│  ├─products
│  ├─security
│  ├─user
│  └─utils
├─assests // 静态资源
├─components // 组件 
│  ├─Base
│  ├─Home
│  ├─Mine
│  ├─ProductCard
│  ├─Products
│  └─Questionare
├─store // pinia 状态管理
├─types // 类型定义
└─views // 页面，应当只有页面才能绑定路由
    ├─Login
    ├─Mine
    ├─Products
    └─Questionare
```