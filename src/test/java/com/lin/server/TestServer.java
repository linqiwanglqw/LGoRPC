package com.lin.server;

import com.lin.RPC.IDL.Hello.HelloService;
import com.lin.RPC.core.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer(); // 真正的rpc server
        HelloService helloService = new HelloServiceImpl(); // 包含需要处理的方法的对象
        rpcServer.register(helloService); // 向rpc server注册对象里面的所有方法


        rpcServer.serve(9000);
    }
}
