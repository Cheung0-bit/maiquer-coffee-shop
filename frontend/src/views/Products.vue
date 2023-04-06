<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import { useRouter } from 'vue-router'
import { loginState } from '../store/loginStatus'

const activeName = ref('all')
const router = useRouter()

const handleClick = (tab: TabsPaneContext, event: Event) => {
  router.push(tab.paneName === 'all' ? '/products/all/All' : '/products/mine')
}

const login = () => {
  const l = loginState()
  if (!l.isLoggedIn)
    window.open('https://api.maiquer.tech/api/wechat/login')
}
</script>

<template>
  <div class="main-content">
    <el-main>
      <el-tabs
        v-model="activeName"
        type="border-card"
        class="demo-tabs"
        stretch
        @tab-click="handleClick"
      >
        <el-tab-pane label="全部" name="all">
          <router-view />
        </el-tab-pane>
        <el-tab-pane label="我的结果" name="mine">
          <router-view />
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </div>
</template>

<style lang="scss" scoped>
@keyframes card-fade-in-up {
  from {
    transform: translateY(-50%);
    opacity: 0.5;
  }

  to {
    opacity: 1;
  }
}

@keyframes card-fade-in-down {
  from {
    transform: translateY(50%);
    opacity: 0.5;
  }

  to {
    opacity: 1;
  }
}

.el-main {
  height: 95vh;
}

.demo-tabs {
  position: relative;
  top: 25px;
  animation: card-fade-in-down 1s;

  // background-color: white;
  & > .el-tabs__content {  /* stylelint-disable-line*/
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }
}

:deep(.el-card__body) {  /* stylelint-disable-line*/
  padding: 5px 10px;
}

.main-card {
  position: relative;
  margin: 0 auto;
  top: 20px;
  width: 90%;
  height: 85px;
  animation: card-fade-in-up 1s;

  & img {
    border-radius: 50%;
    object-fit: cover;
  }

  p {
    color: grey;
  }
}

.menu-card {
  width: 100%;
  background: rgb(255 255 255 / 10%);
  backdrop-filter: saturate(100%) blur(40px);
  background-clip: content-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  // box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 10px 0 rgba(0, 0, 0, 0.19);
}

.option-btn {
  display: flex;
  justify-content: center;
  width: 100%;
  height: 8vh;
  transition: all 0.5s;

  :hover {
    width: 100%;
    font-weight: bold;
    text-align: center;
    box-shadow: 0 4px 8px 0 rgb(0 0 0 / 20%), 0 6px 20px 0 rgb(0 0 0 / 19%);
  }

  overflow: visible;

  a {
    color: grey;
    font-size: 18px;
    text-decoration: none;
  }

  ::before {
    display: inline-block;
    height: 100%;
    content: "";
    vertical-align: middle;
  }
}

.main-content {
  background: linear-gradient(#ededed, #fff);
  height: 100vh;
}
</style>
