package com.lin.RPC.core.client;

import com.lin.RPC.core.codec.RpcRequestBody;
import com.lin.RPC.core.codec.RpcResponseBody;
import com.lin.RPC.core.rpc_protocol.RpcRequest;
import com.lin.RPC.core.rpc_protocol.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {

    @SuppressWarnings("unchecked")//告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息。
    public <T> T getService(Class<T> clazz) {
        //返回目标对象的代理对象
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }

    /**
     *
     * @param proxy :JDK创建的代理对象
     * @param method ：目标类中的方法，JDK提供method对象
     * @param args ：目标类中的方法的参数，JDK提供args对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1、将调用所需信息编码成bytes[]，即有了调用编码【codec层】
        RpcRequestBody rpcRequestBody = RpcRequestBody.builder()
                .interfaceName(method.getDeclaringClass().getName())//返回表示该对象表示的可执行文件的类或接口的Class对象的名称
                .methodName(method.getName())//返回表示此方法的Method对象的名称
                .parameterTypes(method.getParameterTypes())//返回表示此方法的Method对象的参数类型的数组
                .parameters(args)//传入的参数
                .build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(rpcRequestBody);
        byte[] bytes = baos.toByteArray();

        // 2、创建RPC协议，将Header、Body的内容设置好（Body中存放调用编码）【protocol层】
        RpcRequest rpcRequest = RpcRequest.builder()
                .header("version=1")
                .body(bytes)
                .build();

        // 3、发送RpcRequest，获得RpcResponse
        RpcClientTransfer rpcClient = new RpcClientTransfer();
        RpcResponse rpcResponse = rpcClient.sendRequest(rpcRequest);

        // 4、解析RpcResponse，也就是在解析rpc协议【protocol层】
        String header = rpcResponse.getHeader();
        byte[] body = rpcResponse.getBody();
        if (header.equals("version=1")) {
            // 将RpcResponse的body中的返回编码，解码成我们需要的对象Object并返回【codec层】
            ByteArrayInputStream bais = new ByteArrayInputStream(body);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RpcResponseBody rpcResponseBody = (RpcResponseBody) ois.readObject();
            Object retObject = rpcResponseBody.getRetObject();
            return retObject;
        }
        return null;
    }
}
