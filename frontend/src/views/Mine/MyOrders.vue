<script lang="ts" setup>
import { computed, onMounted } from 'vue'
import { userInfo } from '../../store/userInfo'

const info = userInfo()
const tableData = computed(() => {
  return info.Order
})
onMounted(async () => {
  await info.getOrder()
})
</script>

<template>
  <el-card>
    <el-table :data="tableData" style="width: 350px; margin: 0 auto;" height="550">
      <el-table-column fixed prop="name" label="产品名" width="100" />
      <el-table-column prop="date" label="日期" width="100" />
      <el-table-column prop="price" label="金额" width="60" />
      <el-table-column prop="isSuccessful" label="状态" width="60" />
    </el-table>
  </el-card>
</template>

<style lang="scss" scoped>
.el-card {
  margin: 20px;
}
</style>
