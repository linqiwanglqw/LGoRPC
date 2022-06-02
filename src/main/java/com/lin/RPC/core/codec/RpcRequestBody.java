package com.lin.RPC.core.codec;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求体的boby部分
 */
@Data
@Builder
public class RpcRequestBody implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    //返回的对象类型可能不同。
    private Class<?>[] parameterTypes;
}
