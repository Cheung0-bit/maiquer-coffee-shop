import axios from 'axios'
import type Order from '../../types/Order'

export default async function getOrders(userid: number, jwt: string): Promise<Order[]> {
  const resp = await axios({
    url: `/api/order/queryPersonal/${userid}`,
    headers: {
      Authorization: jwt,
    },
    method: 'POST',
  })
  if (resp.data.code !== 0)
    return []

  const data = resp.data.data as { title: string; createTime: string;totalFee: number; orderStatus: string }[]
  return data.map(({
    title, createTime, totalFee, orderStatus,
  }) => ({
    name: title,
    date: createTime,
    price: totalFee / 100,
    isSuccessful: orderStatus,
  }))
}
