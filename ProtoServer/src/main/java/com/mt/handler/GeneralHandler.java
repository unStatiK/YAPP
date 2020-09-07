package com.mt.handler;

import com.mt.generator.MsgGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
public class GeneralHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            System.out.println("receive msg");
            ByteBuf buffer = ctx.channel().alloc().buffer();
            buffer.writeBytes(MsgGenerator.generate().toByteArray());
            ctx.writeAndFlush(buffer);
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
        ReferenceCountUtil.release(in);
    }
}
