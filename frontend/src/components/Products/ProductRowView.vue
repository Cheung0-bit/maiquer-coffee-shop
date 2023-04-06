<script lang="ts" setup>
import { computed } from 'vue'
import { ProductStore } from '../../store/products'

const products = ProductStore()
const listing = computed(() => {
  return products.productLs
})
</script>

<template>
  <el-scrollbar height="220px" style="height: 220px;">
    <div class="row">
      <div v-for="i in listing" :key="i.id">
        <product-card-col :product="i" />
      </div>
    </div>
  </el-scrollbar>
</template>

<style lang="scss" scoped>
.row {
  display: flex;
  flex-direction: row;
}
</style>
