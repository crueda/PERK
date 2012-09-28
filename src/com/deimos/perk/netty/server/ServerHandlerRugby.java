package com.deimos.perk.netty.server;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;


public class ServerHandlerRugby extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();

		String message = buf.toString(Charset.defaultCharset());

		System.out.println(message);
		
		MessageHandlerRugby messageHandlerRugby = new MessageHandlerRugby(message);
		String answer = messageHandlerRugby.execute();
		byte[] answerBytes = answer.getBytes();		
		ChannelBuffer cbuf = ChannelBuffers.buffer(answer.length()); 
		cbuf.writeBytes(answerBytes);
		e.getChannel().write(cbuf);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}
}