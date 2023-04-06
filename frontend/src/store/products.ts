import { defineStore } from 'pinia'
import getAllProducts from '../apis/products/queryAllProducts'
import { getFavProduct, getUserProduct } from '../apis/user/getUserProduct'
import type Product from '../types/product'
import { addFavourite, delFavourite } from '../apis/user/editFav'
import { loginState } from './loginStatus'

export const ProductStore = defineStore('pd', {
  state: () => ({
    productLs: [] as Product[],
    myFav: [] as Product[],
  }),
  getters: {
    getById: ({ productLs }) => (id: number) =>
      productLs.find(r => r.id === id)!
    ,
  },
  actions: {
    async getAll() {
      const login = loginState()
      const Fall = getAllProducts()
      const Fusr = getUserProduct(login.userid)
      const Ffav = getFavProduct(login.userid)
      // next we will mark those
      const [all, usr, fav] = await Promise.all([Fall, Fusr, Ffav])
      all.forEach((r) => {
        if (usr.map(({ id }) => id).includes(r.id))
          r.alreadyHave = true
        if (fav.map(({ id }) => id).includes(r.id))
          r.isLiked = true
      })
      // store products
      this.productLs = all
    },
    async getFav() {
      const login = loginState()
      const fav = await getFavProduct(login.userid)
      this.myFav = fav
    },
    async addFav(evaId: number) {
      const login = loginState()
      await addFavourite(evaId, login.userid)
      await this.getAll()
    },
    async delFav(evaId: number) {
      const login = loginState()
      await delFavourite(evaId, login.userid)
      await this.getAll()
    },
  },
})
