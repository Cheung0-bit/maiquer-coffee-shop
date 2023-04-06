// export default class Order {
//   constructor(
//     public readonly name: string,
//     public readonly date: string,
//     public readonly price: number,
//     public readonly isSuccessful: string,
//   ) {
//   }
// }

export default interface Order {
  name: string
  date: string
  price: number
  isSuccessful: string
}
