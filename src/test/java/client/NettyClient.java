package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.netty.channel.ChannelOption.AUTO_READ;


public class NettyClient {

    final ChannelInboundHandlerAdapter handler = new ChannelInboundHandlerAdapter(){
        private Channel channel;
        private boolean schedulerStart;
        public void channelActive(ChannelHandlerContext ctx) {
            ctx.channel().read();

            this.channel = ctx.channel();

            if(!schedulerStart) {
                schedulerStart = true;

                Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                        () -> channel.read(),
                        1, 5, TimeUnit.SECONDS
                );

            }
        }

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf i = (ByteBuf) msg;
            byte[] buf = new byte[4];
            i.readBytes(buf);

            System.out.println(new Date()
                    + " received signal: "
                    + new String(buf, StandardCharsets.UTF_8));

        }
    };

    public NettyClient() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        new Thread(()-> {
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.option(AUTO_READ, false);
                b.channel(NioSocketChannel.class);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(handler);
                    }
                });

                ChannelFuture f = b.connect("localhost", 8081).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    workerGroup.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Test
    public void testClient() throws InterruptedException, IOException {
        System.in.read();
    }
}
