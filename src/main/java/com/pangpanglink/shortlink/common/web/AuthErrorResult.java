package com.pangpanglink.shortlink.common.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 鉴权失败返回信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorResult {

    /**
     * HTTP 状态码
     */
    private Integer status;

    /**
     * 返回信息
     */
    private String message;
}
