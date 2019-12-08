package com.zman.net.pull;

import java.util.function.Consumer;

public interface IClient<T> {

    /**
     * 连接目标服务地址
     * @param ip    ip
     * @param port  端口
     */
    void connect(String ip, int port) ;

    /**
     * 主动断开连接
     */
    void disconnect();

    /**
     * 成功连接到server时的回调函数
     * @param callback 回调函数
     */
    IClient onConnected(Consumer<T> callback);

    /**
     * 连接断开时回调
     * @param callback 回调函数
     */
    IClient onDisconnected(Runnable callback);

    /**
     * 连接发生异常时回调
     * @param callback  回调函数
     */
    IClient onThrowable(Consumer<Throwable> callback);

}
