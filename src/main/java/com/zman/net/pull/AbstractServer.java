package com.zman.net.pull;

import com.zman.pull.stream.IDuplex;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractServer implements IServer {

    protected BiConsumer<Integer, IDuplex> onAcceptCallback;
    protected Runnable onClosedCallback;
    protected Consumer<Throwable> onThrowableCallback;
    protected Consumer<Integer> onDisconnectedCallback;

    /**
     * 停止server，断开所有连接
     */
    @Override
    public void close() {}

    /**
     * 有新client接入时，回调callback函数
     *
     * @param callback 回调函数
     */
    @Override
    public IServer onAccept(BiConsumer<Integer, IDuplex> callback) {
        this.onAcceptCallback = callback;
        return this;
    }

    /**
     * 连接断开时回调
     *
     * @param callback 连接唯一id
     */
    @Override
    public IServer onDisconnect(Consumer<Integer> callback) {
        onDisconnectedCallback = callback;
        return this;
    }

    /**
     * server停止成功时回调
     *
     * @param callback 回调函数
     */
    @Override
    public IServer onClosed(Runnable callback) {
        this.onClosedCallback = callback;
        return this;
    }

    /**
     * server运行发生异常时回调
     *
     * @param callback 回调函数
     */
    @Override
    public IServer onThrowable(Consumer<Throwable> callback) {
        this.onThrowableCallback = callback;
        return this;
    }
}
