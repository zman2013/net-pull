[![Travis Build](https://api.travis-ci.org/zman2013/net-pull.svg?branch=master)](https://api.travis-ci.org/zman2013/net-pull.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/zman2013/net-pull/badge.svg?branch=master)](https://coveralls.io/github/zman2013/net-pull?branch=master)


# net-pull
Providing idiomatic interfaces of the client & server to implement the net pull-stream.

## dependency
```xml
<dependency>
    <groupId>com.zmannotes</groupId>
    <artifactId>net-pull</artifactId>
    <version>0.0.6</version>
</dependency>
```

## interfaces
### IClient
```java
public interface IClient {

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
    IClient onConnected(Consumer<IDuplex> callback);

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
```
### IServer
```java
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
     *
     * @param callback  回调函数，参数：Integer: 连接唯一id，IDuplex：双工流
     */
    IServer onAccept(BiConsumer<Integer, IDuplex> callback);

    /**
     * 连接断开时回调
     * @param callback 回调函数，参数为：connectionId 连接唯一id
     *
     */
    IServer onDisconnect(Consumer<Integer> callback);

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
```

## example
```java
clientTest
        .onConnected(connectCallback)
        .onDisconnected(disconnectCallback)
        .onThrowable(throwableCallback)
        .connect("ip", 0);
```