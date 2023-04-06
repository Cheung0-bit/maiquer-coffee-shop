import type { RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'
import { loginState } from './store/loginStatus'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('./views/Home.vue'),
  },
  {
    path: '/products/all',
    redirect: '/products/all/All',
  },
  {
    path: '/products',
    name: 'Products',
    redirect: '/products/all',
    component: () => import('./views/Products.vue'),
    children: [
      {
        path: 'All',
        redirect: '/products/All/all',
      },
      {
        path: 'mine',
        redirect: '/products/mine/all',
      },
      {
        path: 'All/:type',
        component: () => import('./views/Products/All.vue'),
      },
      {
        path: 'mine/:type',
        component: () => import('./views/Products/Self.vue'),
      },
    ],
  },
  {
    path: '/question',
    component: () => import('./views/Questionare/ClassicQuestionare.vue'),
  },
  {
    path: '/mine',
    name: 'Mine',
    component: () => import('./views/Mine.vue'),
    beforeEnter: () => {
      const login = loginState()
      if (!login.isLoggedIn) {
        location.href = 'https://api.maiquer.tech/api/wechat/login'
        return false
      }
    },
  },
  {
    path: '/details/:id',
    name: 'Details',
    component: () => import('./views/Details.vue'),
  },
  {
    path: '/oauth2',
    name: 'OAuth',
    component: () => import('./views/Login/Oauth.vue'),
  },
  {
    path: '/editinfo',
    component: () => import('./views/Mine/EditInfo.vue'),
  },
  {
    path: '/myfav',
    component: () => import('./views/Mine/MyFavourite.vue'),
  }, {
    path: '/order',
    component: () => import('./views/Mine/MyOrders.vue'),
  }, {
    path: '/diary',
    component: () => import('./views/Mine/MyDiary.vue'),
  }, {
    path: '/present',
    component: () => import('./views/Mine/MyPresents.vue'),
  }, {
    path: '/redir',
    component: () => import('./views/Login/redir.vue'),
  }, {
    path: '/bindPhone',
    component: () => import('./views/Login/BindPhone.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
