<script lang="ts" setup>
import { useRoute } from 'vue-router'
import { onMounted, reactive } from 'vue'
import { ElLoading } from 'element-plus'
import { loginState } from '../../store/loginStatus'

const state = reactive({
  isOK: false,
  code: '',
})

const route = useRoute()
const login = loginState()
state.code = route.query.code as string
onMounted(async () => {
  setTimeout(async () => {
    state.isOK = await login.wxLogin(state.code)
    if (state.isOK)
      location.href = '/'
  }, 500)
})
</script>

<template>
  <div v-if="!state.isOK">
    正在处理登录.....
  </div>
  <div v-else>
    正在跳转......
  </div>
</template>

<style scoped>
div {
  text-align: center;
  font-size: 16px;
}
</style>
