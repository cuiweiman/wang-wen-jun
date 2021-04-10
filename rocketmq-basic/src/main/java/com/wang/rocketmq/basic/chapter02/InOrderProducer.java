package com.wang.rocketmq.basic.chapter02;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单创建流程包括：订单创建-->订单付款-->订单推送-->订单完成。
 *
 * @description: 顺序消息 生产：分区有序 生产者
 * @author: wei·man cui
 * @date: 2021/3/26 16:45
 */
public class InOrderProducer {

    private static final String NAME_SERVER = "192.168.0.108:9876";

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("Order_Msg_Producer");
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        String[] tags = {"TagA", "TagB", "TagC"};
        List<OrderStep> orderList = new InOrderProducer().buildOrders();
        Date date = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);

        for (int i = 0; i < 10; i++) {
            String msgBody = dateStr.concat(" Hello RocketMQ ").concat(orderList.get(i).toString());
            final Message message = new Message("Msg_In_Order_Topic", tags[i % tags.length], "Key_" + i,
                    msgBody.getBytes(StandardCharsets.UTF_8));
            final SendResult sendResult = producer.send(message, (mqs, message1, arg) -> {
                // 根据 订单 ID, 选择 发送的 queue。arg：来源于 逗号后面的 orderList.get(i).getOrderId()。
                Long id = (Long) arg;
                long index = id % mqs.size();
                return mqs.get((int) index);
            }, orderList.get(i).getOrderId());

            System.out.println(String.format("SendResult status: %s, queueId: %d , body: %s",
                    sendResult.getSendStatus(), sendResult.getMessageQueue().getQueueId(), msgBody));
        }
        producer.shutdown();
    }

    /**
     * 订单的步骤
     */
    private static class OrderStep {
        private long orderId;
        private String desc;

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "OrderStep{" +
                    "orderId=" + orderId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    /**
     * 生成模拟订单数据
     */
    private List<OrderStep> buildOrders() {
        List<OrderStep> orderList = new ArrayList<>();

        OrderStep orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderStep();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }
}
