import axios from 'axios'

export async function addFavourite(evaId: number, userId: number): Promise<boolean> {
  const r = await axios({
    url: `/api/user/addLikeEva?userId=${userId}&evaId=${evaId}`,
    method: 'POST',
  })
  return r.data.code === 0
}

export async function delFavourite(evaId: number, userId: number): Promise<boolean> {
  const r = await axios({
    url: `/api/user/deleteLikeEva?userId=${userId}&evaId=${evaId}`,
    method: 'POST',
  })
  return r.data.code === 0
}
