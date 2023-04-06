<script lang="ts" setup>
import { onBeforeMount, onMounted } from 'vue'
import axios from 'axios'
import MainFooter from './components/Base/Footer.vue'
import { loginState } from './store/loginStatus'
import { userInfo } from './store/userInfo'
import { ProductStore } from './store/products'
// begin init login state
const login = loginState()
const info = userInfo()
const products = ProductStore()
axios.defaults.baseURL = 'https://api.maiquer.tech'

onBeforeMount(async () => {
// after mounted, init products
  login.loadfromLocal()
  if (login.isLoggedIn) {
    axios.defaults.headers.common.Authorization = login.jwtToken
    await info.fetchInfo(login.userid)
  }
  await products.getAll()
},
)
</script>

<template>
  <div>
    <MainFooter />
    <router-view />
  </div>
</template>
