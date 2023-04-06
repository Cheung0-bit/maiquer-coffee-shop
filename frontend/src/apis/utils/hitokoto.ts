import axios from 'axios'

export default async function GetHitokoto() {
  let res = ''
  await axios.get('https://v1.hitokoto.cn/').then((r) => {
    res = r.data.hitokoto
  })
  return res
}
