package me.skipjip;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Main extends JavaPlugin {

	public static Team p = null;
	public static Team s = null;
	public static String WORLD_NAME = "world";
	private static GameState state;
	public boolean gameRunning = false;
	public static boolean running = false;
	public static boolean pregame = false;
	public static boolean ingame = false;
	public static boolean endgame = false;
	public int ticks = 0;
	public int gameTicks = 0;
	private final EventListeners listeners = new EventListeners();
	public int onlinePlayers = Bukkit.getOnlinePlayers().length;
	public static List<Player> playerss = new ArrayList<Player>();

	public static GameState getState() {
		return state;
	}

	public static void setState(GameState state) {
		Main.state = state;
	}

	//	@Override
	//	public void onLoad() {
	//		deleteDir(new File(WORLD_NAME));
	//	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(listeners, this);
		pregame = true;
		state = GameState.PREGAME;
		ticks = 60;
		gameRunning = true;
		gameClock();
		ScoreboardPregame();
		Bukkit.getWorld("world").setPVP(false);
		Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
		Bukkit.getWorld("world").setDifficulty(Difficulty.EASY);
		Location location = new Location(Bukkit.getWorld("world") , 0.5, 135.0, 14.5);
		Location location2 = new Location(Bukkit.getWorld("world") , 0.5, 135.0, -11.5);
		if (location.getBlock().getType() == Material.AIR) {
			Zombie mob = (Zombie)Bukkit.getWorld("world").spawn(location, Zombie.class);
			mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 50));
			mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 50));
			mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 50));
			mob.setCustomName("" + ChatColor.DARK_GRAY + ChatColor.BOLD +  "<" + ChatColor.DARK_GREEN + ChatColor.BOLD + "KITS" 
					+ ChatColor.DARK_GRAY + ChatColor.BOLD + ">");
		}
		Zombie mob2 = (Zombie)Bukkit.getWorld("world").spawn(location2, Zombie.class);
		mob2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 50));
		mob2.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 50));
		mob2.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 50));
		mob2.setCustomName("" + ChatColor.DARK_GRAY + ChatColor.BOLD +  "<" + ChatColor.DARK_GREEN + ChatColor.BOLD + "KITS" 
				+ ChatColor.DARK_GRAY + ChatColor.BOLD + ">");
	}

	@Override
	public void onDisable() {

	}

	//	public static void deleteDir(File dir) {
	//		if (dir.isDirectory()) {
	//			String[] children = dir.list();
	//			for (Integer i = 0; i < children.length; i++) {
	//				deleteDir(new File(dir, children[i]));
	//			}
	//		}
	//		dir.delete();
	//	}

	public void ScoreboardPregame() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
//		Team p = board.registerNewTeam("players");
//		Team s = board.registerNewTeam("spectators");
		Objective objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.BOLD + "SkyWars");
		Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Time:"));
		score.setScore(ticks);
		for(Player online : Bukkit.getOnlinePlayers()){
			online.setScoreboard(board);
			score.setScore(ticks);
		}
	}
	public void ScoreboardIngame() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
//		Team p = board.registerNewTeam("players");
//		Team s = board.registerNewTeam("spectators");
		Objective objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.BOLD + "SkyWars");
		Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN + "Players:"));
		Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Spectators:"));
		Score score3 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Time Left:"));
		score.setScore(p.getSize());
		score2.setScore(s.getSize());
		score3.setScore(gameTicks);
		p.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "ALIVE" + ChatColor.DARK_GRAY + "]");
		p.setDisplayName(ChatColor.WHITE + "");
		s.setPrefix(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "DEAD" + ChatColor.DARK_GRAY + "]");
		s.setDisplayName(ChatColor.GRAY + "");
		s.setCanSeeFriendlyInvisibles(true);
		s.setAllowFriendlyFire(false);
		for(Player online : Bukkit.getOnlinePlayers()){
			online.setScoreboard(board);
			score.setScore(score.getScore());
			score2.setScore(score2.getScore());
			score3.setScore(gameTicks);
		}
	}
	public void clock() {
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			public void run() {
				if (gameTicks == 900 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 15 minutes!");
				if (gameTicks == 600 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 10 minutes!");
				if (gameTicks == 300 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 5 minutes!");
				if (gameTicks == 240 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 4 minutes!");
				if (gameTicks == 180 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 3 minutes!");
				if (gameTicks == 120 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 2 mintues!");
				if (gameTicks == 60 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 1 mintue!");
				if (gameTicks == 30 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 30 seconds!");
				if (gameTicks == 10 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 10 seconds!");
				if (gameTicks == 5 && running == true)
					Bukkit.broadcastMessage(ChatColor.BOLD + "Game ends in 5 seconds!");
				if (gameTicks == 0 && running == true) {
					running = false;
					Bukkit.broadcastMessage(ChatColor.GOLD + "Game Over!");
				}
				if (running)
					gameTicks--;
			}
		}, 20L, 20L);
	}
	public void gameClock() {
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {

				if (ticks == 60 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 1 minute!");
				if (ticks == 30 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 30 seconds!");
				if (ticks == 15 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 15 seconds!");
				if (ticks == 10 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 10 seconds!");
				if (ticks == 5 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 5 seconds!");
				if (ticks == 4 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 4 seconds!");
				if (ticks == 3 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 3 seconds!");
				if (ticks == 2 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 2 seconds!");
				if (ticks == 1 && gameRunning == true)
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 1 second!");
				if (ticks == 0 && onlinePlayers < 2 && gameRunning == true) {
					ticks = 30;
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "There is not enough people to start the game!");
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "There needs to be at least 2 players in order to start!");
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "Game starting in 30 seconds!");
				}
				if (ticks == 0 && onlinePlayers >= 2 && gameRunning == true) {
					ScoreboardManager manager = Bukkit.getScoreboardManager();
					state = GameState.INGAME;
					gameRunning = false;
					gameTicks = 900;
					running = true;
					clock();
					ScoreboardIngame();
					Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Let the games begin!");
					Bukkit.getWorld("world").setPVP(true);
					for (Player players : Bukkit.getOnlinePlayers()) {
						players.setScoreboard(manager.getNewScoreboard());
						p.addPlayer(players);
						Inventory inventory = players.getInventory();
						Player player1 = playerss.get(0);
						Player player2 = playerss.get(1);
						if (onlinePlayers == 3 || onlinePlayers == 4) {
							Player player3 = playerss.get(2);
							Player player4 = playerss.get(3);
							player3.teleport(EventListeners.pos3);
							player4.teleport(EventListeners.pos4);
						}
						player1.teleport(EventListeners.pos1);
						player2.teleport(EventListeners.pos2);

						for (PotionEffect effect : players.getActivePotionEffects()) {
							players.removePotionEffect(effect.getType());
						}
						inventory.clear();
						if (!EventListeners.archerPlayers.contains(players) && !EventListeners.jumperPlayers.contains(players) 
								&& !EventListeners.tankPlayers.contains(players) && !EventListeners.wizardPlayers.contains(players) 
								&& !EventListeners.firemanPlayers.contains(players)) {
							players.sendMessage(ChatColor.RED + "You forgot to pick a kit!");
							players.sendMessage(ChatColor.GREEN + "Next time, use the " + ChatColor.GOLD + "Nether Star");
						}
						if (EventListeners.archerPlayers.contains(players)) {
							players.closeInventory();
							inventory.clear();
							ItemStack weapon = new ItemStack(Material.BOW);
							ItemStack arrow = new ItemStack(Material.ARROW, 10);
							for (PotionEffect effect : players.getActivePotionEffects()) {
								players.removePotionEffect(effect.getType());
							}
							players.getInventory().setHelmet(null);
							players.getInventory().setChestplate(null);
							players.getInventory().setLeggings(null);
							players.getInventory().setBoots(null);
							inventory.addItem(weapon);
							inventory.addItem(arrow);
						}
						if (EventListeners.jumperPlayers.contains(players)) {
							players.closeInventory();
							inventory.clear();
							ItemStack weapon = new ItemStack(Material.ENDER_PEARL, 2);
							for (PotionEffect effect : players.getActivePotionEffects()) {
								players.removePotionEffect(effect.getType());
							}
							players.getInventory().setHelmet(null);
							players.getInventory().setChestplate(null);
							players.getInventory().setLeggings(null);
							players.getInventory().setBoots(null);
							inventory.addItem(weapon);
						}
						if (EventListeners.tankPlayers.contains(players)) {
							players.closeInventory();
							inventory.clear();
							ItemStack weapon = new ItemStack(Material.WOOD_AXE);
							for (PotionEffect effect : players.getActivePotionEffects()) {
								players.removePotionEffect(effect.getType());
							}
							players.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
							players.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
							players.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
							players.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
							inventory.addItem(weapon);
						}
						if (EventListeners.wizardPlayers.contains(players)) {
							players.closeInventory();
							inventory.clear();
							Potion potion = new Potion(PotionType.INSTANT_HEAL, 2, false);
							ItemStack potions = potion.toItemStack(2);
							Potion potion2 = new Potion(PotionType.INSTANT_DAMAGE, 2, true);
							ItemStack potions2 = potion2.toItemStack(2);
							Potion potion3 = new Potion(PotionType.POISON, 1, true, false);
							ItemStack potions3 = potion3.toItemStack(2);
							Potion potion4 = new Potion(PotionType.SPEED, 2, true, true);
							ItemStack potions4 = potion4.toItemStack(1);
							Potion potion5 = new Potion(PotionType.REGEN, 1, true, false);
							ItemStack potions5 = potion5.toItemStack(1);
							for (PotionEffect effect : players.getActivePotionEffects()) {
								players.removePotionEffect(effect.getType());
							}
							players.getInventory().setHelmet(null);
							players.getInventory().setChestplate(null);
							players.getInventory().setLeggings(null);
							players.getInventory().setBoots(null);
							inventory.addItem(potions);
							inventory.addItem(potions2);
							inventory.addItem(potions3);
							inventory.addItem(potions4);
							inventory.addItem(potions5);
						}
						if (EventListeners.firemanPlayers.contains(players)) {
							players.closeInventory();
							inventory.clear();
							ItemStack weapon = new ItemStack(Material.WATER_BUCKET, 3);
							ItemStack arrow = new ItemStack(Material.LAVA_BUCKET, 3);
							for (PotionEffect effect : players.getActivePotionEffects()) {
								players.removePotionEffect(effect.getType());
							}
							players.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
							players.getInventory().setHelmet(null);
							players.getInventory().setChestplate(null);
							players.getInventory().setLeggings(null);
							players.getInventory().setBoots(null);
							inventory.addItem(weapon);
							inventory.addItem(arrow);
						}
					}
				}

				if (gameRunning)
					ticks--;
			}
		},20L,20L);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player)sender;
		if (args.length == 0 && label.equalsIgnoreCase("chicken")) {
			player.sendMessage(ChatColor.GREEN + "chicken");
		}
		if (args.length == 2 && args[1].equalsIgnoreCase("pie")) {
			player.sendMessage(ChatColor.RED + "chicken pie");
		}
		if (command.getName().equalsIgnoreCase("arenapos1")) {
			//			EventListeners.startLocations.set(0, player.getLocation());
			player.sendMessage(ChatColor.BOLD + "Position 1 set");
		}
		else if (command.getName().equalsIgnoreCase("arenapos2")) {
			//			EventListeners.startLocations.set(1, player.getLocation());
			player.sendMessage(ChatColor.BOLD + "Position 2 set");
		}
		else if (command.getName().equalsIgnoreCase("arenapos3")) {
			//			EventListeners.startLocations.set(2, player.getLocation());
			player.sendMessage(ChatColor.BOLD + "Position 3 set");
		}
		else if (command.getName().equalsIgnoreCase("arenapos4")) {
			//			EventListeners.startLocations.set(3, player.getLocation());
			player.sendMessage(ChatColor.BOLD + "Position 4 set");
		}
		else if (command.getName().equalsIgnoreCase("arenaposreset")) {
			//			EventListeners.startLocations.clear();
			player.sendMessage(ChatColor.BOLD + "Arena positions reset");
		}
		else if (command.getName().equalsIgnoreCase("startgame")) {
			gameRunning = true;
			ticks = 10;
			Bukkit.broadcastMessage(ChatColor.GOLD + "The time was shortend by " + ChatColor.DARK_PURPLE + ChatColor.BOLD + sender.getName());
		}
		else if (command.getName().equalsIgnoreCase("endgame")) {
			running = true;
			gameTicks = 60;
			Bukkit.broadcastMessage(ChatColor.GOLD + "The time was shortend by " + ChatColor.DARK_PURPLE + ChatColor.BOLD + sender.getName());
		}
		else if (command.getName().equalsIgnoreCase("mytimer")) {
			if (!gameRunning) {
				gameRunning = true;
				sender.sendMessage(ChatColor.GREEN + "Clock unpaused");
			}
			else {
				gameRunning = false;
				sender.sendMessage(ChatColor.RED + "Clock stopped");
			}
		}
		else if (command.getName().equalsIgnoreCase("archer")) {
			if (state == GameState.PREGAME) {
				EventListeners.archerPlayers.add(player);
				if (EventListeners.jumperPlayers.contains(player) || EventListeners.tankPlayers.contains(player) 
						|| EventListeners.wizardPlayers.contains(player) || EventListeners.firemanPlayers.contains(player)) {
					EventListeners.jumperPlayers.remove(player);
					EventListeners.tankPlayers.remove(player);
					EventListeners.wizardPlayers.remove(player);
					EventListeners.firemanPlayers.remove(player);
				}
				player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "archer" + ChatColor.GREEN + "!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't select a kit mid-game!");
			}
		}
		else if (command.getName().equalsIgnoreCase("jumper")) {
			if (state == GameState.PREGAME) {
				EventListeners.jumperPlayers.add(player);
				if (EventListeners.archerPlayers.contains(player) || EventListeners.tankPlayers.contains(player) 
						|| EventListeners.wizardPlayers.contains(player) || EventListeners.firemanPlayers.contains(player)) {
					EventListeners.archerPlayers.remove(player);
					EventListeners.tankPlayers.remove(player);
					EventListeners.wizardPlayers.remove(player);
					EventListeners.firemanPlayers.remove(player);
				}
				player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "jumper" + ChatColor.GREEN + "!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't select a kit mid-game!");
			}
		}
		else if (command.getName().equalsIgnoreCase("tank")) {
			if (state == GameState.PREGAME) {
				EventListeners.tankPlayers.add(player);
				if (EventListeners.jumperPlayers.contains(player) || EventListeners.archerPlayers.contains(player) 
						|| EventListeners.wizardPlayers.contains(player) || EventListeners.firemanPlayers.contains(player)) {
					EventListeners.jumperPlayers.remove(player);
					EventListeners.archerPlayers.remove(player);
					EventListeners.wizardPlayers.remove(player);
					EventListeners.firemanPlayers.remove(player);
				}
				player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "tank" + ChatColor.GREEN + "!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't select a kit mid-game!");
			}
		}
		else if (command.getName().equalsIgnoreCase("wizard")) {
			if (state == GameState.PREGAME) {
				EventListeners.wizardPlayers.add(player);
				if (EventListeners.jumperPlayers.contains(player) || EventListeners.tankPlayers.contains(player) 
						|| EventListeners.archerPlayers.contains(player) || EventListeners.firemanPlayers.contains(player)) {
					EventListeners.jumperPlayers.remove(player);
					EventListeners.tankPlayers.remove(player);
					EventListeners.archerPlayers.remove(player);
					EventListeners.firemanPlayers.remove(player);
				}
				player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "wizard" + ChatColor.GREEN + "!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't select a kit mid-game!");
			}
		}
		else if (command.getName().equalsIgnoreCase("fireman")) {
			if (state == GameState.PREGAME) {
				EventListeners.firemanPlayers.add(player);
				if (EventListeners.jumperPlayers.contains(player) || EventListeners.tankPlayers.contains(player) 
						|| EventListeners.wizardPlayers.contains(player) || EventListeners.archerPlayers.contains(player)) {
					EventListeners.jumperPlayers.remove(player);
					EventListeners.tankPlayers.remove(player);
					EventListeners.wizardPlayers.remove(player);
					EventListeners.archerPlayers.remove(player);
				}
				player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "fireman" + ChatColor.GREEN + "!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't select a kit mid-game!");
			}
		}
		return true;
	}
	public static Inventory kitInventory(){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "" + ChatColor.DARK_GRAY + ChatColor.BOLD +  "Pick a kit!");

		{
			ItemStack item = new ItemStack(Material.BOW);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Archer");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "- Gives you a ranged advantage!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(0, item);
		}
		{
			ItemStack item = new ItemStack(Material.ENDER_PEARL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Jumper");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "- Jump from island to island!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(2, item);
		}
		{
			ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Tank");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "- Gives you some starter gear!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(4, item);
		}
		{
			ItemStack item = new ItemStack(Material.POTION);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Wizard");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "- Gives you devastating potions!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(6, item);
		}
		{
			ItemStack item = new ItemStack(Material.WATER_BUCKET);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Fireman");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "- FIRE!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(8, item);
		}
		return inv;
	}
	public static Inventory funInventory(){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "" + ChatColor.DARK_GRAY + ChatColor.BOLD +  "Pick an effect!");

		{
			ItemStack item = new ItemStack(Material.FEATHER);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Jump");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "Click me to get a leap!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(0, item);
		}
		{
			ItemStack item = new ItemStack(Material.ANVIL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Slow");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "Click me to becom super slow!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(2, item);
		}
		{
			ItemStack item = new ItemStack(Material.RAW_FISH);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Breath");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "Click me to breathe underwater!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(4, item);
		}
		{
			ItemStack item = new ItemStack(Material.WOOL);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_RED + "Reset");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.BLUE + "Click me to get rid of all potion effects!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(8, item);
		}
		return inv;
	}

}




