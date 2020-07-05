-- id自增做物理主键，是mysql聚簇索引必须要有的；
-- order_id订单主键，假若分库分表，那么可以全局唯一；自增ID不安全。
create table `mulOrders`.`mulOrders`(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
   `order_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '订单流水号',
   `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户ID',
   `merchant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商户ID',
   `order_status` int(11) NOT NULL DEFAULT 0 COMMENT '订单状态',
   `order_note` varchar(256) NOT NULL DEFAULT '' COMMENT '订单备注',
   PRIMARY KEY (`id`)
 );
 create table `mulOrders`.`orders_detail`(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
   `order_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '订单流水号',
   `goods_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '商品ID',
   `goods_note` varchar(256) NOT NULL DEFAULT '' COMMENT '商品备注',
   PRIMARY KEY (`id`)
 );