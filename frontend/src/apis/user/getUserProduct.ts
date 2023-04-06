import axios from 'axios'
import type Product from '../../types/product'

export async function getUserProduct(userid: number): Promise<Product[]> {
  if (userid === 0)
    return []

  const resp = await axios({
    url: `/api/user/queryById/${userid}`,
    method: 'GET',
  })
  if (resp.data.code !== 0)
    return []

  return resp.data.data.myEvaluations as Product[]
}

export async function getFavProduct(userid: number): Promise<Product[]> {
  if (userid === 0)
    return []

  const resp = await axios({
    url: `/api/user/queryById/${userid}`,
    method: 'GET',
  })
  if (resp.data.code !== 0)
    return []

  return resp.data.data.likeEvaluations
}

