package com.deimos.perk.parser;

public interface Parser {
	public abstract void parse(byte[] copyOfRange, int deviceID);
}
