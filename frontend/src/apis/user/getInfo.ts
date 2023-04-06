import axios from 'axios'
import type UserInfo from '../../types/UserInfo'

export default async function getInfo(userid: number): Promise<UserInfo> {
  const resp = await axios({
    url: `/api/user/queryById/${userid}`, method: 'GET',
  })
  const info = resp.data.data
  return {
    name: info.nickname,
    userid: info.id,
    backgd_url: info.backImg,
    avtr_url: info.avatar,
    email: info.email,
    phone: info.phone,
    signiture: info.signature,
  }
}
