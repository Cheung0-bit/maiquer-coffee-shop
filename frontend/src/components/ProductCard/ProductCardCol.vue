<script setup lang="ts">
import { BuyProduct } from '../../apis/products/buyProduct'
import { loginState } from '../../store/loginStatus'
import type Product from '../../types/Product'

const props = defineProps<{ product: Product }>()

const login = loginState()

const ClickCard = async () => {
  if (!login.isLoggedIn) {
    window.open('https://api.maiquer.tech/api/wechat/login')
    return
  }
  if (props.product.alreadyHave) {
    window.open(props.product.realUrl)
  }
  else {
    const success = await BuyProduct(props.product.id, login.userid)
    if (success)
      location.reload()
  }
}
</script>

<template>
  <el-card shadow="always" class="product" @click="ClickCard()">
    <div>
      <img :src="props.product.coverPic" alt="">
    </div>
    <div class="description">
      <p class="title">
        {{ props.product.name }}
      </p>
      <p class="more-info">
        {{ props.product.name }}
      </p>
      <span v-if="!props.product.alreadyHave" class="price">价格：¥ {{ props.product.price }}</span>
      <span v-else class="have">已经购买</span>
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
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
  margin: 20px 10px 10px;
}

:deep(.el-card__body) {  /* stylelint-disable-line*/
  padding: 0 !important;
  width: 100px;
  height: 150px;
  display: flex;
  flex-direction: column;
  background: #eceeee;

  & img {
    width: 100px !important;
    height: 100px;
    position: relative;
  }
}

.description {
  top: 100px;

  & > .title {
    font-size: 14px;
    text-align: center;
    font-weight: bold;
    line-height: 12px;
  }

  & > .more-info {
    font-size: 13px;
    text-align: center;
    color: grey;
    line-height: 6px;
  }

  & > .price {
    color: red;
    font-size: 12px;
    line-height: 8px;
    background-color: #f6d2d2;
    padding: 6px;
    border-radius: 9px;
  }

  & > .have {
    color: white;
    font-size: 12px;
    line-height: 8px;
    background-color: #7cad44;
    padding: 6px;
    border-radius: 9px;
    text-align: center;
  }
}
</style>
