import axios from 'axios'

/**
 * @description 手机号登录
 * @param phone 手机号
 * @return success 成功
 **/
export async function smsSend(phone: string): Promise<boolean> {
  const resp = await axios
    .post(`/api/sms/smsCodeSend?phone=${phone}`)
  return resp.data.code === 0
}

/**
 * @description 绑定手机号接口
 * @param userid 用户ID
 * @param phone 手机号
 * @param code 验证码
 * @return success,message 成功与否和消息
 */
export async function smsAuth(userid: number, phone: string, code: string): Promise<{ success: boolean; message: string }> {
  const resp = await axios
    .post(`/api/sms/login/${userid}?phone=${phone}&code=${code}`)

  const success = resp.data.code === 0

  const { message } = resp.data
  return { success, message }
}
