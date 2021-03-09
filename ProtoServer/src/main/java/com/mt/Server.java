package com.mt;

import com.mt.handler.GeneralHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

public class Server {
    private final int port;
    private final String host;

    public Server(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: <server> <port>");
            return;
        }
        String host = String.valueOf(args[0]);
        int port = Integer.parseInt(args[1]);
        new Server(port, host).start();
    }

    public void start() throws Exception {
        final GeneralHandler serverHandler = new GeneralHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(host, port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(serverHandler);
                    }
                });
            ChannelFuture channel = bootstrap.bind().sync()
                .addListener(future -> System.out.println(String.format("Server start on %s:%s", host, port)));
            channel.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
