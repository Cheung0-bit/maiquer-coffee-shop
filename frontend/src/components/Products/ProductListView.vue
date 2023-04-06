<script lang="ts" setup>
import type { PropType } from 'vue'
import type Product from '../../types/Product'

const props = defineProps({
  listing: { type: Array as PropType<Product[]> },
  showLike: Boolean,
})
</script>

<template>
  <transition-group
    enter-active-class="animate__animated animate__bounceInUp" appear
    appear-active-class="animate__animated animate__bounceInUp"
    mode="out-in"
  >
    <div v-for="i of props.listing " :key="i.id">
      <product-card :product="i" :show-like="props.showLike" />
    </div>
  </transition-group>
</template>

<style lang="scss">
/* .list-enter-active, .list-leave-active { */

/*  transition: all 0.5s; */

/* } */

/* .list-enter-from, .list-leave-to { */

/*  opacity: 0; */

/* } */
</style>
