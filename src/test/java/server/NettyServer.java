package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Promise;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyServer {

    @Sharable
    class ServerHandler extends ChannelInboundHandlerAdapter{

        private boolean schedulerIsStarted = false;

        private Channel channel;

        private AtomicInteger signal = new AtomicInteger(0);

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            ByteBuf b = Unpooled.buffer(4);
            b.writeInt(signal.getAndIncrement());

            ctx.channel().writeAndFlush(b);

            System.out.println("accept client");

            channel = ctx.channel();
            if(!schedulerIsStarted) {
                schedulerIsStarted = true;
                Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                        ()->{
                            if( channel != null ){
                                ByteBuf b2 = Unpooled.buffer(4);
                                String buf = String.format("%04d", signal.get());
                                b2.writeBytes(buf.getBytes(StandardCharsets.UTF_8));
                                channel.writeAndFlush(b2).addListener((ChannelFutureListener) future -> {
                                    if(future.isSuccess()){
                                        System.out.println("write to socket successfully " + signal.getAndIncrement() );
                                    }else{
                                        System.out.println(new Date()+" write to socket failed " + signal);
                                    }
                                });
                                System.out.println(new Date() + " write to socket: " + buf + " channel : " + channel);
                            }
                        }, 1, 1, TimeUnit.SECONDS
                );
            }
        }

        /**
         * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
         * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
         * <p>
         * Sub-classes may override this method to change behavior.
         *
         * @param ctx
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            channel = null;
            System.out.println("client discconect");
        }
    }

    private ChannelInboundHandlerAdapter serverHandler = new ServerHandler();

    public NettyServer() {
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();

            try{
                ServerBootstrap b = new ServerBootstrap();
                b.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(8081))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                socketChannel.pipeline().addLast(serverHandler);
                            }
                        });

                ChannelFuture f = b.bind().sync();
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }){}.start();

    }

    @Test
    public void startServer() throws IOException {
        System.in.read();
    }


}
