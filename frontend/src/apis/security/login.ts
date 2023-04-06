import axios from 'axios'

/**
 @why lalala
 @description 登录接口
 @param username 用户名
 @param password 密码
 @return {jwt,userid} 返回jwt和用户ID
**/
export default async function login(
  username: string,
  password: string,
): Promise<{ jwt: string; userid: number }> {
  const resp = await axios({
    url: '/api/login',
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    },
    data: `username=${username}&password=${password}`,
  })
  if (resp.data.code === 0)
    return { jwt: '', userid: 0 }
  const { jwt, userId } = resp.data.data
  return { jwt, userid: userId }
}
