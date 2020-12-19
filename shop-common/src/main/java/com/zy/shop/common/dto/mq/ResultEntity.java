package com.zy.shop.common.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity implements Serializable {

    private Integer code;

    private String message;
}
