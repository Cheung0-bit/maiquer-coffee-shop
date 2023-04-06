<script lang="ts" setup>
import type { PropType } from 'vue'
import { computed, ref } from 'vue'
import type { Question } from '../../types/Questionare'

const props = defineProps({
  questions: { type: Object as PropType<Question>, required: true },
})

const emits = defineEmits(['next'])

const q = computed(() => {
  return props.questions
})

const ans = ref('')

const select = () => {
  setTimeout(() => {
    emits('next', props.questions.index, ans.value)
    ans.value = ''
  }, 500)
}
</script>

<template>
  <el-card>
    <el-radio-group v-model="ans" @change="select">
      <el-radio v-for="i in q.choice" :key="i.option" border size="large" :label="i.option">
        {{ `${i.option}、 ${i.optionContent}` }}
      </el-radio>
    </el-radio-group>
  </el-card>
</template>

<style lang="scss" scoped>
.el-card {
  margin: 30px;
}

.el-radio-group {
  display: flex;
  flex-direction: column;

  & > .el-radio {
    width: 90%;
    height: 50px;
    margin: 20px;
  }
}
</style>
