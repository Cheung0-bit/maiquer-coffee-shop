import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './Main.vue'
import router from './router'
import 'animate.css'
// const vConsole = new VConsole();
import 'element-plus/dist/index.css'
createApp(App).use(createPinia()).use(router).mount('#app')
