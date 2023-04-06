<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { loginState } from '../../store/loginStatus'

const auth = reactive({
  phone: '',
  code: '',
  time: 0,
})
const result = reactive({
  success: false,
  message: '',
})
const login = loginState()

const getCode = async () => {
  await login.doSendSms(auth.phone)
  const timer = setInterval(() => {
    if (--auth.time < 0) {
      clearInterval(timer)
      auth.time = 60
    }
  }, 1000)
}

const retry = () => {
  result.message = ''
  result.success = false
}

const bind = async () => {
  const r = await login.doSmsBind(auth.phone, auth.code)
  if (r.success) {
    result.success = r.success
    setTimeout(() => {
      location.href = '/editinfo'
    }, 300)
  }
  else {
    result.success = false
    result.message = r.message
  }
}
</script>

<template>
  <div v-if="result.message !== ''">
    <el-result
      icon="error"
      title="出错了"
      :sub-title="result.message"
    >
      <template #extra>
        <el-button type="primary" @click="retry">
          重试
        </el-button>
      </template>
    </el-result>
  </div>
  <div v-else-if="result.success">
    <el-result
      icon="success"
      title="绑定成功"
      sub-title="正在跳转..."
    />
  </div>

  <el-card v-else>
    <div class="form">
      <el-input v-model="auth.phone" placeholder="输入手机号" />
      <el-input v-model="auth.code" class="code" placeholder="输入验证码" />
      <el-button :disabled="auth.time < 60" @click="getCode">
        {{
          auth.time >= 60 ? "获得验证码" : `${auth.time}后再试`
        }}
      </el-button>
    </div>
    <div class="form">
      <el-button type="primary" class="auth" @click="bind">
        绑定
      </el-button>
    </div>
    <img src="../../assests/phone.svg">
  </el-card>
</template>

<style lang="scss" scoped>
.auth {
  width: 100%;
  margin: 10px auto;

  :deep(.el-input__inner) {  /* stylelint-disable-line*/
    width: 100%;
    height: 40px;
  }
}

.el-card {
  margin: 20px;
}

.el-input {
  width: 100%;
  margin: 10px auto;
}

.form {
  position: relative;
  width: 80%;
  margin: auto;
}

.code {
  width: 60%;
  margin: 0 auto 0 10%;
}
</style>
