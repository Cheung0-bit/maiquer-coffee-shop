import axios from 'axios'

/**
 *
 * @param code
 * @param state
 */

export default async function wxauth(
  code: string,
  state: string,
): Promise<string> {
  const resp = await axios
    .get(`/callback?code=${code}&state=${state}`)
  if (resp.data.code !== 0)
    return ''
  return resp.data.data.jwt
}
