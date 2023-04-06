# 麦趣咖啡厅

> 麦趣咖啡厅是我在[迈趣尔科技有限公司](http://www.maiquer.tech)期间，于2021-2022年度打造的一款结合GBA测评等多维度测评的评测系统。现已属于历史废弃项目。出于纪念，在此开源记录。

本项目属于笔者早期实践项目，采用多模块单体架构，采用MAVEN框架管理，SpringBoot框架开发。采用MySQL作为持久层数据库，Redis缓存手机验证码，MongoDB存储测评信息。前端使用Vue3脚手架，Vite编译打包，TypeScript作为主要编程语言，Pinia执行数据状态管理，Element-UI作为UI库。本人主要参与后端研发，此项目有完整的PC端+WEBAPP端+微信小程序实现，并作为参赛作品，在创业计划书、互联网+，青年大学生创新创业等比赛中获得佳绩。同时，承载了我大学期间诸多的回忆......

## 开发背景

近年来，随着社会在各行各业对人才的迫切需要，同时包括大量求职者在内的青年人对自己的个人能力以及适合承担什么样的工作而感到困惑已经成为随处可见的普遍现象，而市面上的一些测评问卷、软件要么内容欠佳，要么专业性偏差,且绝大部分做起来都枯燥无味。为了打造出一款具有趣味性且能够帮助年轻人真正地认识自己，了解自己的多元化能力并最终找到适合自己的工作，麦趣测评软件应运而生。在中国，人才分类评价、雇佣适合的人做适合的事情，是这个时代人才管理的最前沿理念。人才测评领域方兴未艾，估值有800亿左右。在市场竞争中，有90%的人才测评公司服务于企业的组织管理，10%左右的人才测评公司服务于个体。其中，专注于服务年轻求职者的测评产品更少，这个领域市场估值14亿左右。且在中国，非常缺乏本土化原创测评产品，麦趣在这个市场竞争格局中，将深耕校园求职市场，力图打造原创测评产品，短期目标和长期目标相结合，服务于年轻人的高质量就业，促进人才强国的社会发展目标实现

## 产品市场

### 人才测评市场的整体发展趋势

UCA时代，人才竞争越加激烈，用工方式更加多元化，雇佣关系也会愈加灵活化，个体与组织的协同共创关系由于认知等各类基础实施的高度完善有了很大的变化，人力资源管理中越来越多的呼声是关于”把人当人”。为此，人才测评已从招聘筛选工具，转向-种引导企业开展人才管理甚至战略管理的工具，广泛应用于人才招聘、人员盘点、培训与发展等各个环节，通过了解人才的优劣势，性格特质、潜质、胜等胜任力情况，作为人力资源决策可靠、客观的依据，为人力资源决策提供参考性建议。

### 游戏化测评适用于年轻求职者

人才甄选模式需要紧跟互联网技术高速发展的时代。据统计显示，现在的 00 后们在大学毕业前，玩游戏的平均时间已经超过 10000 小时，其中 47%会完全依赖手机申请工作。当代年轻人更追求自由和开放，对于自身的职业生涯规划，他们希望走出自己的路，但未必都真正了解自己。游戏化测评工具认可偏好采用游戏代替线上测试作为测评工具，就是求职者通过玩一系列的小游戏，来测评求职者的快速反应、逻辑推理、专注度、风险偏好等能力。游戏就是创造一个令你身临其境的小世界，带给被测者更好的掌控感，增强每个参与者的个性化体验，利用大脑最原始的反应系统，给与最真实的选择决定。游戏是一个过程，而不是一个简单的结果;游戏化是一种综合、全方位的体验，通过在专业的测评过程中添加适量的游戏化的元素（如决策过程、反应时间、点击频率等）收集被测者各种细微的差异数据点，描绘出候选人真实的性格特征井将其转化为候选人与某一特定工作岗位适配程度的有效建议，从而筛选出更符合企业要求的求职者。

### 后端架构

主要使用了 SpringBoot 框架作为基础，Spring Boot 可以轻松创建可以“直接运行”的独立的、生产级的基于 Spring 的应用程序。

~~~txt
.
|-- metric-common // 公共模块
|-- metric-core // 核心业务模块
|-- metric-log // 日志业务模块
|-- metric-oss // 对象存储模块
|-- metric-sms // 短信业务模块
|-- metric-wechat // 微信业务模块
`-- pom.xml // 总 Maven 集成管理
~~~

![image-20230406194442966](https://0-bit.oss-cn-beijing.aliyuncs.com/image-20230406194442966.png)

通过依赖关系可见，common 作为基础，向其他四个模块提供服务。包括且不限于对象的管理、数据持久化的储存、Service 业务的编写而 oss、sms、wechat、log 四个模块分别提供对象储存服务、阿里云短信服务、微信服务、日志管理服务，这四个模块之间互不依赖，彼此独立，高度解耦。最后，统一由 core 模块
提供 API 服务，直接与前端进行交互。

![1](https://0-bit.oss-cn-beijing.aliyuncs.com/1.png)

![2](https://0-bit.oss-cn-beijing.aliyuncs.com/2.png)

![3](https://0-bit.oss-cn-beijing.aliyuncs.com/3.png)

![4](https://0-bit.oss-cn-beijing.aliyuncs.com/4.png)

![5](https://0-bit.oss-cn-beijing.aliyuncs.com/5.png)

![6](https://0-bit.oss-cn-beijing.aliyuncs.com/6.png)

### 技术栈使用

在身份认证与鉴权方面，整合了SpringSecurity+Jwt，实现PermissionEvaluator接口自定义了hasPermission的鉴权逻辑，使用Jwt使身份信息无状态化，适合项目随时做分布式扩展。接通阿里云短信服务，并使用Redis存储验证码，设置5分钟过期时间，将用户信息存储做数据脱敏，并存储在ThreadLocal中。对于MVC拦截器，重写afterCompletion方法，在每次MVC请求线程结束后，均会销毁ThreadLocal中内容，避免内存泄漏。三方授权部分，接通了微信登录，与短信登录、密码登录共同组成不同的策略实现。同时，对接了微信支付SDK，并撰写了详细的实践过程发表在[掘金](https://juejin.cn/post/7073770349435256869)，收获一致好评。在微信支付成功后，使用RabbitMQ消息队列中间件，通过Fanout发布订阅模型，异步通知，将支付结果持久化到数据库，并通过Spring事务控制多个表的修改。在修改测评库存记录时，使用乐观锁保证多线程安全。文件上传统一使用又拍云OSS对象存储。日志记录模块会记录用户行为，API调用链路，存储在Redis中，对于危险的操作，会Email通知管理员。

### 图片展示

![10](https://0-bit.oss-cn-beijing.aliyuncs.com/10.png)

<div style="display: flex; flex-wrap: wrap; justify-content: space-between;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/8.jpg" alt="Image 1" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/9.jpg" alt="Image 2" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/11.png" alt="Image 3" style="width: 30%; margin-bottom: 10px;">
</div>
<div style="display: flex; flex-wrap: wrap; justify-content: space-between;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/12.png" alt="Image 1" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/13.jpg" alt="Image 2" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/14.png" alt="Image 3" style="width: 30%; margin-bottom: 10px;">
</div>
<div style="display: flex; flex-wrap: wrap; justify-content: space-between;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/15.png" alt="Image 1" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/16.png" alt="Image 2" style="width: 30%; margin-bottom: 10px;">
  <img src="https://0-bit.oss-cn-beijing.aliyuncs.com/17.png" alt="Image 3" style="width: 30%; margin-bottom: 10px;">
</div>

### 视频演示

打开本项目，访问`/doc/项目演示视频`，打开 `演示.mp4`即可观看