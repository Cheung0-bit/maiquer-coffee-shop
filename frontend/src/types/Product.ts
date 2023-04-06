export default interface Product {
  bgPic: string
  buyCount: number
  clickCount: number
  coverPic: string
  createTime: string
  id: number
  likeCount: number
  name: string
  price: string
  realUrl: string
  type: number
  updateTime: string
  alreadyHave?: boolean
  isLiked?: boolean
}
// export default class Product {
//   constructor(
//     public readonly bgPic: string,
//     public readonly buyCount: number,
//     public readonly clickCount: number,
//     public readonly coverPic: string,
//     public readonly createTime: string,
//     public readonly id: number,
//     public readonly likeCount: number,
//     public readonly name: string,
//     public readonly price: string,
//     public readonly realUrl: string,
//     public readonly type: number,
//     public readonly updateTime: string,
//     public alreadyHave?: boolean,
//     public isLiked?: boolean,
//   ) {
//   }
// }

//
// 0: {createTime: "2022-03-12 20:58:43", updateTime: "2022-04-20 07:07:13", id: 1, name: "恋爱人格", type: 1,…}
// bgPic: "https://img-cdn.dustella.net/sundry/2.webp"
// buyCount: 0
// clickCount: 0
// coverPic: "https://img-cdn.dustella.net/sundry/2.webp"
// createTime: "2022-03-12 20:58:43"
// id: 1
// likeCount: 0
// name: "恋爱人格"
// price: 5.99
// realUrl: "https://jinshuju.net/f/tJduAB"
// type: 1
// updateTime: "2022-04-20 07:07:13"
