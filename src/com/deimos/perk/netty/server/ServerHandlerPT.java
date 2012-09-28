package com.deimos.perk.netty.server;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.deimos.perk.utils.LatitudeLongitude;
import com.deimos.perk.utils.ServiceParameters;

import com.deimos.perk.utils.geocoding.*;

public class ServerHandlerPT extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();

		//String message = buf.toString(Charset.defaultCharset());
		
		byte[] message = buf.array();
		String a = message.toString();
		
		System.out.println("->: " + a);
		
		//MessageHandlerPT messageHandlerPT = new MessageHandlerPT(message);
		//String answer = messageHandlerPT.execute();
		
		// No es necesaria respuesta al picotrack
		/*
		byte[] answerBytes = answer.getBytes();		
		ChannelBuffer cbuf = ChannelBuffers.buffer(answer.length()); 
		cbuf.writeBytes(answerBytes);
		e.getChannel().write(cbuf);*/

		
		/*
		  // Fecha en gmt0 ojo el wonde envia en la hora de la zona en la que se encuentra
			long date = TimeAndDate.parse(getFecha()).getTime();
			date  = ConvertDateUtils.getInstance().convertFromTimeZoneToGMT(netDevice.getTimeZone().getIdTimeZone(), date).getTime();
			salida.setPosDate(date);

			double latitud = Double.parseDouble(getLatitud());
			double longitud = Double.parseDouble(getLongitud());

			LatitudeLongitude latitudeLongitude = new LatitudeLongitude(latitud, longitud);
			
		 */
		
		/*List<GeocodingDto> geocodingDtoList = new ArrayList<GeocodingDto>();
		GeocodingDto geocodingDto = new GeocodingDto(1, lat, lon);
		geocodingDtoList.add(geocodingDto);
		
		GeocodingGoogle geocodingGoogle;
		ServiceParameters geocodingParametersGoogle;
		
		List<GeocodingDto> geocodingDtoListFull = GeocodingGoogle.getInstance(geocodingParametersGoogle).getGeocoding(geocodingDtoList);
		*/
		

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();

		Channel ch = e.getChannel();
		ch.close();
	}
}