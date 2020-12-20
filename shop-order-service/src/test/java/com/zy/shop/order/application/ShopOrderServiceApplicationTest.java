package com.zy.shop.order.application;
import com.zy.shop.order.applicaton.ShopOrderServiceApplication;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author Jong
 * @Date 2020/12/13 16:10
 * @Version 1.0
 */
@SpringBootTest(classes = {ShopOrderServiceApplication.class})
public class ShopOrderServiceApplicationTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendMsg() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message("orderTopic", "order_cancel", "20201213161234567890", "this is a test".getBytes());
        SendResult send = rocketMQTemplate.getProducer().send(message);
        if (send.getSendStatus() == SendStatus.SEND_OK){
            System.out.print("ok");
        }
    }

}
