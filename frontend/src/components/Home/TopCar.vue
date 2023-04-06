<script lang="ts" setup>
const urls = [
  'https://s.newtalk.tw/album/news/274/5d2d42e961097.jpg',
  'https://wowlavie-aws.hmgcdn.com/file/article_all/A1560997636.jpg',
  'https://okogreen.com.tw/sites/okogreen.com.tw/files/coffee-622495_1920%20%E6%8B%B7%E8%B2%9D.jpg',
]
</script>

<template>
  <el-carousel trigger="click" height="200px">
    <el-carousel-item
      v-for="item of urls"
      :key="item"
      class="iconc"
      :style="{ backgroundImage: `url(${item})` }"
    />
  </el-carousel>
</template>

<style lang="scss" scoped>
.el-carousel__item {   /* stylelint-disable-line*/
  background-repeat: no-repeat;
  background-size: cover;
}
</style>
