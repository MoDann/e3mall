# 前言
近年来，中国的电子商务快速发展，交易额连创新高，电子商务在各领域的应用不断拓展和深化、相关服务业蓬勃发展、支撑体系不断健全完善、创新的动力和能力
不断增强。电子商务正在与实体经济深度融合，进入规模性发展阶段，对经济社会生活的影响不断增大，正成为我国经济发展的新引擎。

# 宜立方商城
宜立方网上商城是一个综合性的B2C平台，类似京东商城、天猫商城。会员可以在商城浏览商品、下订单，以及参加各种活动。<br>
管理员、运营可以在平台后台管理系统中管理商品、订单、会员等。<br>
客服可以在后台管理系统中处理用户的询问以及投诉。

### 功能列表

![](https://github.com/MoDann/image/blob/master/e3mall/design/%E5%9B%BE%E7%89%8731.png) <br>

>后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。<br>
前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。<br>
会员系统：用户可以在该系统中查询已下的订单、收藏的商品、我的优惠券、团购等信息。<br>
订单系统：提供下单、查询订单、修改订单状态、定时处理订单。<br>
搜索系统：提供商品的搜索功能。<br>
单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。
<br>

### soa的架构
>SOA：Service Oriented Architecture面向服务的架构。也就是把工程拆分成服务层、表现层两个工程。<br>
服务层中包含业务逻辑，只需要对外提供服务即可。<br>
表现层只需要处理和页面的交互，业务逻辑都是调用服务层的服务来实现。
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/design/%E5%9B%BE%E7%89%8733.png) <br>

### 系统架构
![](https://github.com/MoDann/image/blob/master/e3mall/design/%E5%9B%BE%E7%89%8734.png) 

#### Dubbo
随着互联网的发展，网站应用的规模不断扩大，常规的垂直应用架构已无法应对，
分布式服务架构以及流动计算架构势在必行，亟需一个治理系统确保架构有条不紊的演进。

    单一应用架构 
        当网站流量很小时，只需一个应用，将所有功能都部署在一起，以减少部署节点和成本。
        此时，用于简化增删改查工作量的 数据访问框架(ORM) 是关键。
    垂直应用架构 
        当访问量逐渐增大，单一应用增加机器带来的加速度越来越小，将应用拆成互不相干的几个应用，以提升效率。
        此时，用于加速前端页面开发的 Web框架(MVC) 是关键。
    分布式服务架构 
        当垂直应用越来越多，应用之间交互不可避免，将核心业务抽取出来，作为独立的服务，逐渐形成稳定的服务中心，
        使前端应用能更快速的响应多变的市场需求。
        此时，用于提高业务复用及整合的 分布式服务框架(RPC) 是关键。
    流动计算架构 
        当服务越来越多，容量的评估，小服务资源的浪费等问题逐渐显现，此时需增加一个调度中心基于访问压力实时
        管理集群容量，提高集群利用率。
        此时，用于提高机器利用率的 资源调度和治理中心(SOA) 是关键。

    Dubbo就是资源调度和治理中心的管理工具。
    
### 系统运行图
>运行环境：Eclipse + Maven + Tomcat + Dubbo + Nginx + SVN + MySQL <br>
 主要技术：SpringMVC + Redis + Solr + ActiveMQ
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E7%BB%93%E6%9E%84.png) 
<br>

>后台管理系统
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8739.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8740.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8741.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8742.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8743.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8744.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8745.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8746.png)
<br>

>前台系统
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8747.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8749.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8750.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8751.png) 
<br>

>搜索系统
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8748.png) <br>
<br>

>订单系统
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8751.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8753.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8754.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8755.png) <br>
![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8756.png) <br>

>单点登录系统
<br>

![](https://github.com/MoDann/image/blob/master/e3mall/result/%E5%9B%BE%E7%89%8752.png)
<br>
