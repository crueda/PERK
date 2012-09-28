package com.deimos.perk.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.deimos.perk.utils.PropertiesManager;

public class Server {
	private static final int portJWM = PropertiesManager.getInstance().getPortJWM();
	private static final int portRugby = PropertiesManager.getInstance().getPortRugby();
	private static final int portPT = PropertiesManager.getInstance().getPortPT();
	
	private static Logger log = Logger.getLogger(Server.class);
	
	public static void main(String[] args) throws Exception {
		log.info("Iniciamos la aplicacion");
		
		ChannelFactory factory =
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool());

		ServerBootstrap bootstrapChino = new ServerBootstrap(factory);
		ServerBootstrap bootstrapArgentino = new ServerBootstrap(factory);
		ServerBootstrap bootstrapPT = new ServerBootstrap(factory);

		bootstrapChino.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new ServerHandlerJWM());
			}
		});

		bootstrapChino.setOption("child.tcpNoDelay", true);
		bootstrapChino.setOption("child.keepAlive", true);

		bootstrapChino.bind(new InetSocketAddress(portJWM));
		
		
		bootstrapArgentino.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new ServerHandlerRugby());
			}
		});

		bootstrapArgentino.setOption("child.tcpNoDelay", true);
		bootstrapArgentino.setOption("child.keepAlive", true);

		bootstrapPT.bind(new InetSocketAddress(portRugby));
		
		bootstrapPT.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new ServerHandlerPT());
			}
		});

		bootstrapPT.setOption("child.tcpNoDelay", true);
		bootstrapPT.setOption("child.keepAlive", true);

		bootstrapPT.bind(new InetSocketAddress(portPT));
	}
}
