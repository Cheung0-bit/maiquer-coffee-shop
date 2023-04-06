<script lang="ts" setup>
import { reactive, ref } from 'vue'
import RadioChoice from '../../components/Questionare/RadioChoice.vue'
import { Question, Test } from '../../types/Questionare'

const currentIndex = ref(0)
const questions = Test
const animateActive = ref(true)

const next = (index: number, value: string) => {
  if (currentIndex.value + 1 >= questions.length) {
    // finish
  }
  else {
    animateActive.value = !animateActive.value
    setTimeout(() => {
      animateActive.value = !animateActive.value
    }, 500)
    currentIndex.value++
  }
}

const saveProgress = () => {

}
</script>

<template>
  <p>{{ questions[currentIndex].question }}</p>
  <RadioChoice :questions="questions[currentIndex]" :class="{ entering: animateActive, leaving: !animateActive }" @next="next" />
</template>

<style lang="scss" scoped>
p {
  text-align: center;
  font-size: 20px;
}

@keyframes leaving {
  from {
    opacity: 1;
  }

  to {
    opacity: 0;
    transform: translateX(-50%) scale(0.9);
  }
}

.leaving {
  animation: leaving 1s;
}

@keyframes entering {
  from {
    transform: translateX(50%) scale(0.9);
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

.entering {
  animation: entering 1s;
}
</style>
