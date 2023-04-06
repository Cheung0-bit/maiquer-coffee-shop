<script lang="ts" setup>
import IntroCard from '../components/Home/IntroCard.vue'
import RecommendProducts from '../components/Home/RecommendProducts.vue'
import HaveATry from '../components/Home/HaveATry.vue'
</script>

<template>
  <top-car />
  <IntroCard />
  <HaveATry />
  <RecommendProducts />
  <el-empty description="咖啡店正在施工哦" />
</template>
