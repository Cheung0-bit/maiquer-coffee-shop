import { defineStore } from 'pinia'
import { Test } from '../types/Questionare'

export const questionStore = defineStore('questionStore', {
  state: () => {
    return {
      CurrentQuestionare: Test,
    }
  },
  actions: {
    async PostQuestionare() {

    },
  },
})

