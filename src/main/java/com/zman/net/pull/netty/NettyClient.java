package com.zman.net.pull.netty;

import com.zman.net.pull.AbstractClient;
import com.zman.net.pull.IClient;
import com.zman.pull.stream.IDuplex;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.function.Consumer;

import static io.netty.channel.ChannelOption.AUTO_READ;

public class NettyClient extends AbstractClient {

    private Consumer<IDuplex> connectCallback;

    /**
     * 成功连接到server时的回调函数
     *
     * @param callback 回调函数
     */
    @Override
    public IClient onConnected(Consumer<IDuplex> callback) {
        this.connectCallback = callback;
        return this;
    }

    @Override
    public void connect(String ip, int port) {

        new Bootstrap()
                .group(new NioEventLoopGroup())
                .option(AUTO_READ, false)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(handler);
                    }
                })
                .connect(ip, port);
    }

    private ChannelInboundHandlerAdapter handler = new ChannelInboundHandlerAdapter() {
        private NettyDuplex duplex;
        public void channelActive(ChannelHandlerContext ctx) {
            duplex = new NettyDuplex(ctx.channel());
            connectCallback.accept(duplex);
        }
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            duplex.push(msg);
        }
    };

}
