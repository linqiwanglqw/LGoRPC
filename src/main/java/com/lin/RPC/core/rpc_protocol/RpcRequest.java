package com.lin.RPC.core.rpc_protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder // 作用主要是用来生成对象，并且可以为对象链式赋值
public class RpcRequest implements Serializable {
    //协议头部分
    private String header;
    //协议体部分
    private byte[] body;
}
