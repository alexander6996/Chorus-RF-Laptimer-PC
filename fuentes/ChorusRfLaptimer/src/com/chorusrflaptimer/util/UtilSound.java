package com.chorusrflaptimer.util;

import java.io.FileInputStream;
import java.util.HashMap;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class UtilSound {
	
	public static final String BEP = "sound/beep_01.wav";
	
	private HashMap<String, AudioStream> sonds = new HashMap<String, AudioStream>();
	
	private static UtilSound utilSound;
	
	private UtilSound() {
		try {
			sonds.put(BEP, new AudioStream(new FileInputStream(BEP)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static UtilSound getInstance() {
		if(utilSound == null) {
			utilSound = new UtilSound();
		}
		return utilSound;
	}
	
	@SuppressWarnings("restriction")
	public void playSound(String[] sondFiles) throws Exception {
		// play the audio clip with the audioplayer class
		for(String file : sondFiles) {
			AudioPlayer.player.start(sonds.get(file));	
		}
	}
}
