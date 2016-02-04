package nidhogglike.game;

import gameframework.assets.Sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;

public class NidhoggAnnouncer {

	protected static final Integer PAYBACK_THRESHOLD = 5;
	protected List<Player> players;
	protected List<Integer> killingSprees;
	protected List<Integer> kills;
	protected Sound sound;

	protected HashMap<Integer, Sound> spreeSounds;
	protected HashMap<Integer, Sound> killSounds;
	protected Sound paybackSound;
	protected Sound suicideSound;
	protected Sound firstBloodSound;

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

		try {
			firstBloodSound = new Sound("/sounds/announcer/firstblood.wav");
			paybackSound = new Sound("/sounds/announcer/payback.wav");
			spreeSounds.put(3, new Sound("/sounds/announcer/killingspree.wav"));
			spreeSounds.put(5, new Sound("/sounds/announcer/ownage.wav"));
			spreeSounds.put(10, new Sound("/sounds/announcer/unstoppable.wav"));
			spreeSounds.put(15, new Sound("/sounds/announcer/godlike.wav"));

			killSounds.put(25, new Sound("/sounds/announcer/ludicrouskill.wav"));
			killSounds.put(42, new Sound("/sounds/announcer/monsterkill.wav"));
			killSounds.put(60, new Sound("/sounds/announcer/holyshit.wav"));
		} catch (final Exception e) {
			System.out.println("Could not load announcer sounds");
		}
	}

	public void handleKill(final Player killee, final Sword sword) {
		registerKill(inferKiller(sword));
		registerDeath(killee);
	}

	protected Player inferKiller(final Sword sword) {
		return sword.getHolder() != null ? sword.getHolder() : sword.getLastHolder();
	}

	protected void registerKill(final Player killer) {
		final int pos = players.indexOf(killer);

		if (!firstBlood) {
			firstBlood = true;
			playSound(firstBloodSound);
		}

		killingSprees.set(pos, killingSprees.get(pos) + 1);
		playSound(spreeSounds.get(killingSprees.get(pos)));
		kills.set(pos, kills.get(pos) + 1);
		playSound(killSounds.get(kills.get(pos)));
	}

	protected void registerDeath(final Player killee) { // haha
		final int pos = players.indexOf(killee);

		if (killingSprees.get(pos) >= PAYBACK_THRESHOLD) {
			playSound(paybackSound);
		}

		killingSprees.set(pos, 0);
	}

	public void registerSuicide(final Player player) {
		playSound(suicideSound);
		registerDeath(player);
	}

	protected void playSound(final Sound sound) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (sound == null || sound.isPlaying())
					return;
				try {
					sound.play();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void addPlayer(final Player p) {
		players.add(p);
		killingSprees.add(0);
		kills.add(0);
	}
}
