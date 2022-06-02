package com.lin.RPC.core.codec;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应体bobu内容
 */
@Data
@Builder
public class RpcResponseBody implements Serializable {
    private Object retObject;
}
