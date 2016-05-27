package model;

import view.GameFrame;

public class Timer implements Cloneable {
	private long timePassed;
	private long currentTime;
	private int h;
	private int m;
	private int s;
	private long ns;
	
	public Timer() {
		restart();
	}
	
	private Timer(Timer other) {
		timePassed = other.timePassed;
		currentTime = other.currentTime;
		h = other.h;
		m = other.m;
		s = other.s;
		ns = other.ns;
	}
	
	private long getTime() {
		return GameFrame.getInstance().getTime();
	}
	
	private void add(long ns) {
		timePassed += ns;
		this.ns += ns;
		s += this.ns / 1000000000;
		this.ns = this.ns % 1000000000;
		m += this.s / 60;
		s = s % 60;
		h = m / 60;
		m = m % 60;
	}
	
	public void newTime() {
		long time = getTime();
		add(time - currentTime);
		currentTime = time;
	}
	
	private static String leftPad(String s, int width) {
		return String.format("%" + width + "s", s).replace(' ', '0');
	}
	
	@Override
	public String toString() {
		return leftPad(Integer.toString(m+60*h), 2) + "." + leftPad(Integer.toString(s), 2) + "." + leftPad(Long.toString(ns/1000000), 3);
	}
	
	public final void restart() {
		timePassed = 0;
		h = 0;
		m = 0;
		s = 0;
		ns = 0;
		currentTime = getTime();
	}
	
	public void unPause() {
		currentTime = getTime();
	}
	
	public long getTimePassed() {
		return timePassed;
	}
	
	public static String longToTimeString(long time) {
		long ns = time;
		int s = (int)(ns / 1000000000);
		ns = ns % 1000000000;
		int m = s / 60;
		s = s % 60;
		int h = m / 60;
		m = m % 60;
		return leftPad(Integer.toString(m+60*h), 2) + "." + leftPad(Integer.toString(s), 2) + "." + leftPad(Long.toString(ns/1000000), 3);
	}
	
	@Override
	public Timer clone() {
		return new Timer(this);
	}
}
