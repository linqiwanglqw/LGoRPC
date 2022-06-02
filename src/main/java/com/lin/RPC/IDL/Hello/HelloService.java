package com.lin.RPC.IDL.Hello;

public interface HelloService {
    HelloResponse hello(HelloRequest request);
    HelloResponse hi(HelloRequest request);
}
