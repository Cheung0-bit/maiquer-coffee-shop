import { defineStore } from 'pinia'
import login from '../apis/security/login'
import wxauth from '../apis/security/wxauth'
import { smsAuth, smsSend } from '../apis/security/smsAuth'

export const loginState = defineStore('login', {
  state: () => ({
    isLoggedIn: false,
    hasJWTs: false,
    jwtToken: '',
    userid: 0,
  }),
  actions: {
    async doLogin(username: string, password: string): Promise<boolean> {
      const session = await login(username, password)
      if (session.jwt === '')
        return false
      this.isLoggedIn = true
      this.jwtToken = session.jwt
      this.userid = session.userid
      this.save2Local()
      return true
    },
    async wxLogin(code: string): Promise<boolean> {
      const jwt = await wxauth(code, 'maiqu')
      if (jwt === '')
        return false
      this.jwtToken = jwt
      this.extractUserId()
      this.isLoggedIn = true
      this.save2Local()
      return true
    },
    async doSendSms(phone: string): Promise<boolean> {
      await smsSend(phone)
      return true
    },
    async doSmsBind(phone: string, code: string): Promise<{ success: boolean; message: string }> {
      return await smsAuth(this.userid, phone, code)
    },
    save2Local() {
      localStorage.clear()
      localStorage.setItem('jwt', this.jwtToken.toString())
    },
    loadfromLocal() {
      if (!localStorage.getItem('jwt'))
        return
      this.jwtToken = String(localStorage.getItem('jwt'))
      this.extractUserId()
      if (this.extractExpireTime() - this.getTimeStamp() < 6000)
        this.logout()
      else
        this.isLoggedIn = true
    },
    logout() {
      localStorage.clear()
    },
    extractUserId() {
      const data = this.jwtToken.slice(this.jwtToken.indexOf('.') + 1, this.jwtToken.lastIndexOf('.'))
      const id = JSON.parse(`(${window.atob(data)})`)
      this.userid = id.jti
    },
    extractExpireTime(): number {
      const data = this.jwtToken.slice(this.jwtToken.indexOf('.') + 1, this.jwtToken.lastIndexOf('.'))
      const id = JSON.parse(`(${window.atob(data)})`)
      return id.exp
    },
    getTimeStamp(): number {
      return Math.round(new Date().getTime() / 1000)
    },
  },
})
