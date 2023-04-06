<script lang="ts" setup>
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'
import { userInfo } from '../../store/userInfo'
import { loginState } from '../../store/loginStatus'

// 初始化 信息等等
const UserInfo = userInfo()
const login = loginState()
const info = computed(() => {
  return UserInfo.userInfo
})
const token = computed(() => {
  return { Authorization: login.jwtToken }
})
const pushSmsAuth = () => {
  location.href = '/bindphone'
}
// 初始化页面逻辑要用到的信息
const isEditingName = ref(false)
const isEditingSign = ref(false)
const name = ref(UserInfo.userInfo.name)
const sign = ref(UserInfo.userInfo.signiture)
const loading = ref(false)
// 修改用户基本信息
const editName = async () => {
  await UserInfo.editNickname(name.value)
  isEditingName.value = false
}
const editSign = async () => {
  await UserInfo.editSign(sign.value)
  isEditingSign.value = false
}

// 上传文件逻辑
const handleAvatarSuccess: UploadProps['onSuccess'] = async (response) => {
  loading.value = false
  await UserInfo.editAvtr(response.data)
}

const handleBackSuccess: UploadProps['onSuccess'] = async (response) => {
  loading.value = false
  await UserInfo.editBack(response.data)
}
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  loading.value = true
  return true
}
</script>

<template>
  <div>
    <el-card class="edit">
      <table class="info">
        <tr v-loading="loading">
          <td>头像</td>
          <!--          <td><img width="65" :src="myinfo.avtr_url" @click="uploadAvtr"></td> -->
          <td>
            <el-upload
              class="avatar-uploader"
              action="https://api.maiquer.tech/api/upload/image"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              name="imgFile"
              :headers="token"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="info.avtr_url" width="65" :src="info.avtr_url" class="avatar">
            </el-upload>
          </td>

          <td> ›</td>
        </tr>
        <tr>
          <td>背景图</td>
          <td>
            <el-upload
              class="avatar-uploader"
              action="https://api.maiquer.tech/api/upload/image"
              :show-file-list="false"
              :on-success="handleBackSuccess"
              name="imgFile"
              :headers="token"
              :before-upload="beforeAvatarUpload"
            >
              <img width="105" :src="info.backgd_url">
            </el-upload>
          </td>
          <td> ›</td>
        </tr>
        <tr>
          <td>用户名</td>
          <td>
            <span v-if="!isEditingName" @click="isEditingName = true">{{ info.name }}</span>
            <el-input v-else v-model="name" @blur="editName" />
          </td>
          <td> ›</td>
        </tr>
        <tr>
          <td>个性签名</td>
          <td>
            <span v-if="!isEditingSign" @click="isEditingSign = true">{{ info.signiture }}</span>
            <el-input v-else v-model="sign" @blur="editSign" />
          </td>
          <td> ›</td>
        </tr>
        <tr>
          <td>绑定手机号</td>
          <td @click="pushSmsAuth">
            {{ info.phone ? info.phone : "未绑定" }}
          </td>
          <td> ›</td>
        </tr>
        <tr>
          <td>绑定微信</td>
          <td>已绑定</td>
        </tr>
      </table>
    </el-card>
    <el-empty description="这里正在施工哦" />
  </div>
</template>

<style scoped lang="scss">
.edit {
  margin: 20px;
  padding: 4px;

  & > {
    width: 100%;
  }
}

.avatar-uploader {
  position: relative;
  height: 100%;
}

.info {
  width: 100%;

  & tr {
    height: 50px;
    border: 2px solid black;

    & td:first-child {
      color: gray;
    }

    & td:nth-of-type(2) {
      text-align: end;
    }

    & td:nth-of-type(3) {
      position: relative;
      margin-top: 2px !important;
      text-align: end;
      color: #6b778c;
      font-size: 16px;
    }
  }
}
</style>
