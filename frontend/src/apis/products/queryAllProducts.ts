import axios from 'axios'
import type Product from '../../types/Product'

/**
 * @description 得到所有的产品。
 * @param type 产品种类，可以为空
 */
export default async function getAllProducts(
  type?: number,
): Promise<Product[]> {
  const resp = await axios
    .get(`/api/evaluation/queryAll${type ? '/type' : ''}`,
    )
  return resp.data.data as Product[]
}
