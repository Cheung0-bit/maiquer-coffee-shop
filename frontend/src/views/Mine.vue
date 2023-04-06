<script lang="ts" setup>
import MineInfo from '../components/Mine/MineInfo.vue'
import MineCard from '../components/Mine/MineCard.vue'
</script>

<template>
  <div class="mine">
    <MineInfo />
    <MineCard />
  </div>
</template>

<style lang="scss" scoped>
.mine {
  margin-top: 0;
  height: 100vh;
  background-color: #f0f0f0;
}
</style>
