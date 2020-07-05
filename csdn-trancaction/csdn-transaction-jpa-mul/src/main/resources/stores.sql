-- id自增做物理主键，是mysql聚簇索引必须要有的；
-- order_id订单主键，假若分库分表，那么可以全局唯一；自增ID不安全。
create table `orders`.`mulOrders`(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
   `order_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '订单流水号',
   `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户ID',
   `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户ID',
   `order_status` int(11) NOT NULL DEFAULT 0 COMMENT '订单状态',
   `order_note` varchar(256) NOT NULL DEFAULT '' COMMENT '订单备注',
   PRIMARY KEY (`id`)
 );
 create table `orders`.`orders_detail`(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
   `order_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '订单流水号',
   `goods_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商品ID',
   `goods_note` varchar(256) NOT NULL DEFAULT '' COMMENT '商品备注',
   PRIMARY KEY (`id`)
 );
 -- 商品库存 数据表设计，模拟多数据源下，SpringBoot对事物的管理机制
 create table `stores`.`stores`(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
   `goods_id` bigint(20) DEFAULT '0' COMMENT '货品ID',
   `store` bigint(20) DEFAULT '0' COMMENT '货品库存',
   PRIMARY KEY (`id`)
 );