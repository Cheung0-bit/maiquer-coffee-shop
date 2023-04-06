export interface Questionare {
  id: number
  name: string
  question: Question[]
}

export interface Question {
  index: number
  question: string
  choice: { option: string; optionContent: string }[]
}

export const Test: Question[] = [
  {
    question: '第一道题',
    index: 1,
    choice: [
      {
        option: 'A',
        optionContent: '1第一个',
      },
      {
        option: 'B',
        optionContent: '1选项',
      },
      {
        option: 'C',
        optionContent: '1lalala',
      },
      {
        option: 'D',
        optionContent: '1lalala',
      },
    ],
  },
  {
    question: '第二道题',
    index: 1,
    choice: [
      {
        option: 'A',
        optionContent: '第2一个',
      },
      {
        option: 'B',
        optionContent: '选2项',
      },
      {
        option: 'C',
        optionContent: 'l2alala',
      },
      {
        option: 'D',
        optionContent: 'lal2ala',
      },
    ],
  },
  {
    question: '第三道题',
    index: 1,
    choice: [
      {
        option: 'A',
        optionContent: '第3一个',
      },
      {
        option: 'B',
        optionContent: '选项4',
      },
      {
        option: 'C',
        optionContent: 'lal3ala',
      },
      {
        option: 'D',
        optionContent: 'lala3la',
      },
    ],
  },
  {
    question: '第四道题',
    index: 1,
    choice: [
      {
        option: 'A',
        optionContent: '第一4个',
      },
      {
        option: 'B',
        optionContent: '选4',
      },
      {
        option: 'C',
        optionContent: 'lal4ala',
      },
      {
        option: 'D',
        optionContent: 'lal4ala',
      },
    ],
  },
  {
    question: '标题',
    index: 1,
    choice: [
      {
        option: 'A',
        optionContent: '第一个',
      },
      {
        option: 'B',
        optionContent: '选项',
      },
      {
        option: 'C',
        optionContent: 'lalala',
      },
      {
        option: 'D',
        optionContent: 'lalala',
      },
    ],
  },
]
