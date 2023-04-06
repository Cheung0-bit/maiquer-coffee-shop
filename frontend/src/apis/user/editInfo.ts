import axios from 'axios'

export async function editUsername(username: string, userid: number): Promise<void> {
  await axios(
    {
      url: `/api/user/updateNickName?userId=${userid}&nickname=${username}`,
      method: 'PUT',
    },
  )
}

export async function editSignature(sign: string, userid: number): Promise<void> {
  await axios({
    url: `/api/user/updateSignature?userId=${userid}&signature=${sign}`,
    method: 'PUT',
  })
}

export async function editAvatar(url: string, userId: number): Promise<void> {
  await axios({
    url: `/api/user/updateAvatar?userId=${userId}&avatar=${url}`,
    method: 'PUT',
  })
}

export async function editBackground(url: string, userId: number): Promise<void> {
  await axios({
    url: `/api/user/updateBackImg?userId=${userId}&backImg=${url}`,
    method: 'PUT',
  })
}
