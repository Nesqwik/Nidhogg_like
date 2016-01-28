package nidhogglike.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gameframework.assets.Sound;
import nidhogglike.entities.Player;

public class NidhoggAnnouncer {

	protected static final Integer PAYBACK_THRESHOLD = 5;
	protected List<Player> players;
	protected List<Integer> killingSprees;
	protected List<Integer> kills;
	protected Sound sound;

	protected HashMap<Integer, String> spreeSounds;
	protected HashMap<Integer, String> killSounds;
	protected String paybackSound;
	protected String suicideSound;
	protected String firstBloodSound;

	protected boolean firstBlood;

	public NidhoggAnnouncer() {
		players = new ArrayList<>();
		killingSprees = new ArrayList<>();
		kills = new ArrayList<>();
		firstBlood = false;
		initSounds();
	}

	protected void initSounds() {
		spreeSounds = new HashMap<>();
		killSounds = new HashMap<>();
		// TODO: put the names in the maps and initialize the sound vars
	}

	public void registerKill(Player killer) {
		int pos = players.indexOf(killer);

		if (!firstBlood) {
			firstBlood = true;
			playSound(firstBloodSound);
		}

		killingSprees.set(pos, killingSprees.get(pos) + 1);
		System.out.println(killingSprees.get(pos));
		playSound(spreeSounds.get(killingSprees.get(pos)));
		kills.set(pos, kills.get(pos) + 1);
		playSound(killSounds.get(kills.get(pos)));
	}

	public void registerDeath(Player killee) { // haha
		int pos = players.indexOf(killee);

		if (killingSprees.get(pos) >= PAYBACK_THRESHOLD) {
			playSound(paybackSound);
		}

		killingSprees.set(pos, 0);
	}

	public void registerSuicide(Player player) {
		playSound(suicideSound);
		registerDeath(player);
	}

	protected void playSound(String str) {
		if (str == null || (sound != null && sound.isPlaying()))
			return;

		try {
			sound = new Sound(str);
			sound.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPlayer(Player p) {
		players.add(p);
		killingSprees.add(0);
		kills.add(0);
	}
}
