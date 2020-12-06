package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author: jogin
 * @date: 2020/12/5 14:00
 */
@Data
public class ShopMQProducerLog implements Serializable {

    private Long msgId;

    private String groupName;

    private String msgTopic;

    private String msgKey;

    private String msgTag;

    private String msgBody;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
}
