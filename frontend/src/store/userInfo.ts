import { defineStore } from 'pinia'
import getInfo from '../apis/user/getInfo'
import type UserInfo from '../types/UserInfo'
import { editAvatar, editBackground, editSignature, editUsername } from '../apis/user/editInfo'
import type Order from '../types/Order'
import getOrders from '../apis/user/queryOrders'
import { loginState } from './loginStatus'

export const userInfo = defineStore('userInfo', {
  state: () => ({
    userInfo: {
      name: '请登录',
      userid: 0,
      backgd_url: 'https://tg-1252147446.cos.ap-shanghai.myqcloud.com/2019/10/Surface-Pro7.jpg',
      avtr_url: 'https://pic4.zhimg.com/50/v2-6afa72220d29f045c15217aa6b275808_hd.jpg',
      email: '请登录',
      phone: '00000',
      signiture: '',
    } as UserInfo,
    Order: [] as Order[],
  }),
  actions: {
    async fetchInfo(userid: number): Promise<boolean> {
      const data = await getInfo(userid)
      this.userInfo = data
      return true
    },
    async editNickname(username: string): Promise<void> {
      const login = loginState()
      await editUsername(username, login.userid)
      await this.fetchInfo(login.userid)
    },
    async editSign(sign: string): Promise<void> {
      const login = loginState()
      await editSignature(sign, login.userid)
      await this.fetchInfo(login.userid)
    },
    async editAvtr(url: string): Promise<void> {
      const login = loginState()
      await editAvatar(url, login.userid)
      await this.fetchInfo(login.userid)
    },
    async editBack(url: string): Promise<void> {
      const login = loginState()
      await editBackground(url, login.userid)
      await this.fetchInfo(login.userid)
    },
    async getOrder(): Promise<void> {
      const login = loginState()
      this.Order = await getOrders(login.userid, login.jwtToken)
    },
  },
})
