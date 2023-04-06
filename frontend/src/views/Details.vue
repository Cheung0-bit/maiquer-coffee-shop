<script setup lang="ts">
import { useRoute } from 'vue-router'
import { computed, onMounted, ref } from 'vue'
import { ProductStore } from '../store/products'

const route = useRoute()
const Products = ProductStore()

const id = computed(() => {
  return Number(route.params.id)
})
const img = ref('')

onMounted(async () => {
  const p = Products.productLs.find(r => r.id === id.value)!
  img.value = p.bgPic
})
</script>

<template>
  <div>
    <div class="top" />
    <el-card class="intro">
      <img :src="img">
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.top {
  height: 120px;
  background-color: #2ecc71;
}

.intro {
  position: relative;
  border-radius: 20px 20px 0 0;
  top: -20px;

  p {
    font-size: 20px;
  }
}

.background {
  height: 1000vh;
  background-repeat: no-repeat;

  // background-image: v-bind(backGd);
}
</style>
