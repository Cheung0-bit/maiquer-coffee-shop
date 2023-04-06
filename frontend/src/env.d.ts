/// <reference types="vite/client" />

declare module "*.vue" {
    import type {DefineComponent} from "vue";
    // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
    const component: DefineComponent<{}, {}, any>;
    export default component;
}

declare type payd = {
    timeStamp: string;
    orderNo: string;
    package: string;
    appId: string;
    paySign: string;
    nonceStr: string;
};

declare module "weixin-js-sdk";
declare module "qs";

declare module WeixinJSBridge {
    // @ts-ignore
    async function invoke(arg0: string, arg1: any, arg3: (res: any) => void):Promise<void>
}
