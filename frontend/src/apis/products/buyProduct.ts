import axios from 'axios'
import qs from 'qs'

/**
 * @description 在购买商品之前的预处理，为了拿到支付的各种凭证
 * @param evaId 商品的ID
 * @param userId 用户ID
 */
export async function BuyProduct(evaId: number, userId: number): Promise<{ success: boolean; orderNo: string }> {
  const res = (await axios({
    url: `/api/wx-pay/jsapi/${evaId}`,
    method: 'POST',
    data: qs.stringify({ userId }),
  })).data.data as payd
  const ret = { success: false, orderNo: res.orderNo }
  await WeixinJSBridge.invoke(
    'getBrandWCPayRequest',
    {
      ...res,
      signType: 'RSA', // 微信签名方式：
    },
    async (r: any) => {
      if (r.err_msg !== 'get_brand_wcpay_request:ok')
        return
      await axios.post(`/api/wx-pay/fontNotify?orderNo=${res.orderNo}&userId=${userId}&evaId=${evaId}`)
      // notify backend
      ret.success = true
      location.reload()
    })
  return ret
}

