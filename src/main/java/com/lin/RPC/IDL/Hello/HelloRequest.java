package com.lin.RPC.IDL.Hello;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor // 自动生成构造函数
public class HelloRequest implements Serializable {
    private String name;
}
