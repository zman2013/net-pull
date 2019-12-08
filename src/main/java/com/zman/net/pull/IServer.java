package com.zman.net.pull;

import com.zman.pull.stream.IDuplex;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IServer<T> {

    /**
     * 启动Server，监听端口
     * @param port 监听的端口
     */
    void listen(int port);

    /**
     * 停止server，断开所有连接
     */
    void close();

    /**
     * 有新client接入时，回调callback函数
     *
     * @param callback  回调函数，参数：String: 连接唯一id，IDuplex：双工流
     */
    IServer onAccept(BiConsumer<String, T> callback);

    /**
     * 连接断开时回调
     * @param callback 回调函数，参数为：connectionId 连接唯一id
     *
     */
    IServer onDisconnect(BiConsumer<String, T> callback);

    /**
     * server停止成功时回调
     * @param callback 回调函数
     */
    IServer onClosed(Runnable callback);

    /**
     * server运行发生异常时回调
     * @param callback 回调函数
     */
    IServer onThrowable(Consumer<Throwable> callback);

}
