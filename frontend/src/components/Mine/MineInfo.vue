<script lang="ts" setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { userInfo } from '../../store/userInfo'
import { loginState } from '../../store/loginStatus'

const UserInfo = userInfo()
const login = loginState()

const info = computed(() => {
  return UserInfo.userInfo
})

const backgd = computed(() => {
  return `url(${UserInfo.userInfo.backgd_url})`
})

const router = useRouter()
const editInfo = async () => {
  await router.push({ path: '/editinfo' })
}

const logout = () => {
  login.logout()
  router.push('/')
  setTimeout(() => {
    location.reload()
  }, 1000)
}
</script>

<template>
  <div>
    <div class="back" />
    <el-card class="main-card">
      <img width="100" height="100" :src="info.avtr_url" alt="头像">
      <el-button @click="editInfo">
        编辑信息
      </el-button>
      <el-button @click="logout">
        登出
      </el-button>
      <h4>{{ info.name }}</h4>
      <p>{{ info.signiture }}</p>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
@keyframes fade-in-down {
  from {
    opacity: 0.5;
    transform: translateY(-50%);
  }

  to {
    opacity: 1;
  }
}

.back {
  height: 300px;
  background-image: linear-gradient(to top, #f0f0f0, rgb(255 255 255 / 0%)), v-bind(backgd) !important;
  background-repeat: no-repeat;
  background-size: cover;
}

:deep(.el-card__body) {  /* stylelint-disable-line*/
  padding-top: 0;
}

.main-card {
  position: relative;
  margin: -170px auto 0;
  top: 20px;
  width: 90%;
  border-radius: 20px 20px 0 0;
  animation: fade-in-down 1s;
  overflow: visible;

  & img {
    border-radius: 50%;
    margin-top: -50%;
    overflow: visible;
    object-fit: cover;
  }

  & .el-button {
    float: right;
    margin: 25px 0 0 10px;
  }

  p {
    color: grey;
  }
}
</style>
