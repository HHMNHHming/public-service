# 网关服务

## Global Filter
* CorsResponseHeader
  - 当gateway配置为可跨域时，处理重复header的问题
  - Order: 0
* JwtQuery
  * 将url中jwt参数，放入header Authorization
  * Order: 1
* Authority
  * 鉴权
  * Order: 3