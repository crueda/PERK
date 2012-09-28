package com.deimos.perk.netty.server;

import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.deimos.perk.utils.Utils;


public class ServerHandlerJWM extends SimpleChannelHandler {
	public final byte[] ANSWER = { 0x55, (byte) 0xAA };

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();

		byte[] message = buf.array();
		MessageHandlerJWM messageHandler = new MessageHandlerJWM(message);
		
		//Si sincro=1 el equipo necesita que le mandemos la fecha y hora
		int sincro = messageHandler.execute();
		System.out.flush();

		if (sincro == 0) {
			ChannelBuffer cbuf = ChannelBuffers.buffer(2); 
			cbuf.writeBytes(ANSWER);
			e.getChannel().write(cbuf);
		} else if (sincro == 1) {
			ChannelBuffer cbuf = ChannelBuffers.buffer(8); 
			cbuf.writeBytes(Utils.GetTimeFrameToSincro(Calendar.getInstance().getTime()));
			e.getChannel().write(cbuf);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}
}