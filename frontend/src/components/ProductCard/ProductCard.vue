<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { loginState } from '../../store/loginStatus'
import { ProductStore } from '../../store/products'
import type Product from '../../types/Product'
import { BuyProduct } from '../../apis/products/buyProduct'

const props = defineProps<{ product: Product; showLike?: boolean }>()

const PromptLogin = () => {
  ElMessageBox.confirm(
    '登录之后才可以做测评哦',
    'Warning',
    {
      confirmButtonText: '好哦',
      cancelButtonText: '算了',
      type: 'warning',
    },
  )
    .then(() => {
      location.href = 'https://api.maiquer.tech/api/wechat/login'
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消了登录',
      })
    })
}
const login = loginState()

const PromptPayment = async () => {
  ElMessageBox.confirm(
        `需要支付${props.product.price}，继续？`,
        'Warning',
        {
          confirmButtonText: '好哦',
          cancelButtonText: '算了',
          type: 'info',
        },
  )
    .then(async () => {
      const result = await BuyProduct(props.product.id, login.userid)
      if (result.success) {
        location.reload()
        window.open(props.product.realUrl)
      }
    })
    .catch(() => {
      ElMessage({
        type: 'warning',
        message: '取消了支付',
      })
    })
}
const ClickCard = async () => {
  if (!login.isLoggedIn) {
    // 如果没有登录，就跳转登录页面；
    PromptLogin()
  }
  if (props.product.alreadyHave) {
    // 如果已经买过，就跳转金数据
    window.open(props.product.realUrl)
  }
  else {
    // 如果没买，就提示购买
    await PromptPayment()
  }
}
const products = ProductStore()
const like = async () => {
  await products.addFav(props.product.id)
}
const unlike = async () => {
  await products.delFav(props.product.id)
}
</script>

<template>
  <div>
    <el-card shadow="always" class="product" @click.stop="ClickCard()">
      <img :src="props.product.coverPic" alt="">
      <div class="description">
        <div style="width: 100%; margin-top: 0;">
          <div class="like">
            <span class="title">{{ props.product.name }}</span>
            <svg
              v-show="props.showLike" v-if="!props.product.isLiked" class="icon"
              viewBox="0 0 1024 1024"
              xmlns="http://www.w3.org/2000/svg" p-id="6323" width="24" height="24" @click.stop="like"
            >
              <path
                d="M512 928c-28.928 0-57.92-12.672-86.624-41.376L106.272 564C68.064 516.352 32 471.328 32 384c0-141.152 114.848-256 256-256 53.088 0 104 16.096 147.296 46.592 14.432 10.176 17.92 30.144 7.712 44.608-10.176 14.432-30.08 17.92-44.608 7.712C366.016 204.064 327.808 192 288 192c-105.888 0-192 86.112-192 192 0 61.408 20.288 90.112 59.168 138.688l315.584 318.816C486.72 857.472 499.616 863.808 512 864c12.704 0.192 24.928-6.176 41.376-22.624l316.672-319.904C896.064 493.28 928 445.696 928 384c0-105.888-86.112-192-192-192-48.064 0-94.08 17.856-129.536 50.272l-134.08 134.112c-12.512 12.512-32.736 12.512-45.248 0s-12.512-32.736 0-45.248l135.104-135.136C610.56 151.808 671.904 128 736 128c141.152 0 256 114.848 256 256 0 82.368-41.152 144.288-75.68 181.696l-317.568 320.8C569.952 915.328 540.96 928 512 928z"
                p-id="6324" fill="#d81e06"
              />
            </svg>
            <svg
              v-show="props.showLike" v-else class="icon"
              viewBox="0 0 1024 1024"
              xmlns="http://www.w3.org/2000/svg" p-id="6857" width="24" height="24" @click.stop="unlike"
            >
              <path
                d="M736 128c-65.952 0-128.576 25.024-176.384 70.464-4.576 4.32-28.672 28.736-47.328 47.68L464.96 199.04C417.12 153.216 354.272 128 288 128c-141.152 0-256 114.848-256 256 0 82.432 41.184 144.288 76.48 182.496l316.896 320.128C450.464 911.68 478.304 928 512 928c33.696 0 61.568-16.32 86.752-41.504l316.736-320 2.208-2.464C955.904 516.384 992 471.392 992 384 992 242.848 877.152 128 736 128z"
                p-id="6858" fill="#d81e06"
              />
            </svg>
          </div>
          <div class="below-content">
            <span class="more-info">{{ props.product.name }}</span>
            <span v-if="!props.product.alreadyHave" class="price">价格：¥ {{ props.product.price }}</span>
            <span v-else class="have">已经购买</span>
          </div>
        </div>
      </div>
    </el-card>
    <div class="tags">
      <span>{{ 5 }}人测过</span>
      <span>需要{{ 15 }}分钟</span>
      <span>恋爱真难</span>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  transition: all 0.5s ease;
}

.product {
  .like {
    position: relative;
    margin-top: 10px;
  }

  .icon {
    margin-right: 0;
    float: right;
    margin-left: auto;
  }
}

.below-content {
  margin-bottom: 0;
  margin-top: 40px;
}

.tags {
  & > span {
    font-size: 13px;
    color: grey;
    padding: 5px;
    background-color: #eceeee;
    border-radius: 9px;
    margin-right: 10px;
  }
}

.el-card {
  border-radius: 15px;
  margin: 30px 0 10px;
}

:deep(.el-card__body) {   /* stylelint-disable-line*/
  padding: 0 !important;
  width: 100%;
  height: 100px;
  display: flex;
  flex-direction: row;

  & img {
    margin: 10px;
    width: 80px !important;
    height: 80px;
    position: relative;
    border-radius: 10px;
  }
}

.description {
  margin-left: 5px;
  width: 230px;
  margin-right: 0;

  & > .title {
    font-size: 17px;
    font-weight: bold;
    line-height: 12px;
  }

  & .more-info {
    font-size: 12px;
    color: grey;
    line-height: 6px;
  }

  & .price {
    color: red;
    font-size: 12px;
    line-height: 8px;
    background-color: #f6d2d2;
    padding: 6px;
    border-radius: 9px;
    text-align: right;
    float: right;
  }

  & .have {
    color: white;
    font-size: 12px;
    line-height: 8px;
    background-color: #7cad44;
    padding: 6px;
    border-radius: 9px;
    text-align: center;
    float: right;
  }
}
</style>
