package com.wang.concurrent.readwritelock;

/**
 * 模仿 Service 层
 */
public interface GoodsInfoService {

    // 获取商品信息
    public GoodsInfo getGoods();

    // 设置商品数量
    public void setNum(int number);

}
