package com.zman.net.pull;

import com.zman.pull.stream.IDuplex;

import java.util.function.Consumer;

public abstract class AbstractClient implements IClient {

    protected Consumer<IDuplex> onConnectedCallback;
    protected Runnable onDisconnectedCallback;
    protected Consumer<Throwable> onThrowableCallback;

    @Override
    public void disconnect() {}

    /**
     * 成功连接到server时的回调函数
     *
     * @param callback 回调函数
     */
    @Override
    public IClient onConnected(Consumer<IDuplex> callback) {
        this.onConnectedCallback = callback;
        return this;
    }

    @Override
    public IClient onDisconnected(Runnable callback) {
        this.onDisconnectedCallback = callback;
        return this;
    }

    @Override
    public IClient onThrowable(Consumer<Throwable> callback) {
        this.onThrowableCallback = callback;
        return this;
    }
}
