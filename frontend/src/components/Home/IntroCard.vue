<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import GetHitokoto from '../../apis/utils/hitokoto'

const hitokoto = ref('愿你我都是星河滚烫的人')

onMounted(async () => {
  await GetHitokoto().then(r => hitokoto.value = r)
})
</script>

<template>
  <div class="home">
    <el-card class="intro">
      <h2>嘿！这里是麦趣心理咖啡厅</h2>
      <p>
        {{ hitokoto }}
      </p>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.intro {
  position: relative;
  border-radius: 20px 20px 0 0;
  top: -20px;

  & p {
    font-size: 15px;
  }
}

.iconc {
  width: 100%;
  background-repeat: no-repeat;
  background-position: center;
}

.todo {
  //   border: 1px solid #949494;
  width: 200px;
  margin: 20px 20px 0 auto;
  box-shadow: 1px 1px #949494;
  background-color: #fff8f8;
  height: 100px;
}
</style>
