package com.zman.net.pull;

import com.zman.pull.stream.IDuplex;

import java.util.function.Consumer;

public interface IServer {

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
     * @param callback  回调函数
     */
    IServer onAccept(Consumer<IDuplex> callback);

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
