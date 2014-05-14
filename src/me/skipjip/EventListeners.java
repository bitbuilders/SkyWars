package me.skipjip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EventListeners implements Listener{

	public static Location pos1 = new Location(Bukkit.getWorld("world") , 969.5, 45.0, 1000.5);
	public static Location pos2 = new Location(Bukkit.getWorld("world") , 1000.5, 45.0, 1031.5);
	public static Location pos3 = new Location(Bukkit.getWorld("world") , 1031.5, 45.0, 1000.5);
	public static Location pos4 = new Location(Bukkit.getWorld("world") , 1000.5, 45.0, 969.5);
	public static Location lobby = new Location(Bukkit.getWorld("world") , 0.5, 135.0, 1.5);
	public static Location spectator = new Location(Bukkit.getWorld("world") , 1000.5, 55.0, 1000.5);
	public static boolean pos1Full = false;
	public static boolean pos2Full = false;
	public static boolean pos3Full = false;
	public static boolean pos4Full = false;
	public List<Boolean> chestFull = new ArrayList<Boolean>();
	public boolean running = Main.running;
	public static ArrayList<Player> archerPlayers = new ArrayList<Player>();
	public static ArrayList<Player> jumperPlayers = new ArrayList<Player>();
	public static ArrayList<Player> tankPlayers = new ArrayList<Player>();
	public static ArrayList<Player> wizardPlayers = new ArrayList<Player>();
	public static ArrayList<Player> firemanPlayers = new ArrayList<Player>();
	public static ArrayList<Player> spectators = new ArrayList<Player>();
	public static List<Material> specialItems = Arrays.asList(Material.ENDER_PEARL, Material.EXP_BOTTLE, Material.DIAMOND, Material.IRON_INGOT);

	public static List<Material> food = Arrays.asList(Material.BREAD, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_FISH
			, Material.COOKIE, Material.RAW_BEEF, Material.RAW_FISH, Material.APPLE, Material.BAKED_POTATO, Material.CAKE, Material.ARROW);

	public static List<Material> materials = Arrays.asList(Material.WOOD, Material.COBBLESTONE, Material.SANDSTONE);

	public static List<Material> items = Arrays.asList(Material.BOW, Material.IRON_HELMET, Material.IRON_BOOTS
			, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS
			, Material.FISHING_ROD, Material.IRON_SWORD, Material.WOOD_SWORD, Material.WOOD_AXE, Material.LEATHER_BOOTS
			, Material.LEATHER_CHESTPLATE, Material.STONE_AXE, Material.IRON_AXE, Material.LEATHER_HELMET
			, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.STONE_SWORD, Material.IRON_CHESTPLATE
			, Material.IRON_LEGGINGS, Material.WATER_BUCKET, Material.LAVA_BUCKET);
	static Map<String, Integer> killers = new HashMap<String, Integer>();
	public GameState state = Main.getState();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		List<Player> players = Main.playerss;
		Player player = event.getPlayer();
		if (!players.contains(player.getName())) {
			players.add(player);
		}
		if (running == false) {
			player.sendMessage(ChatColor.GREEN + "Welcome, " + ChatColor.BLUE + ChatColor.BOLD + player.getName() + ChatColor.GREEN + "!");
			player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.BLUE + "[" + ChatColor.GRAY + "Archer" + ChatColor.BLUE + ", "
					+ ChatColor.GRAY + "Jumper" + ChatColor.BLUE + ", " + ChatColor.GRAY + "Tank" + ChatColor.BLUE + ", "
					+ ChatColor.GRAY + "Wizard" + ChatColor.BLUE + ", " + ChatColor.GRAY + "Fireman" + ChatColor.BLUE + "]");
			player.sendMessage(ChatColor.GREEN + "To select a kit, simply type " + ChatColor.GOLD + "/" + ChatColor.GRAY + "{Kit Name}"
					+ ChatColor.GREEN + ", or select it from the kit menu in the " + ChatColor.GOLD + "Nether Star" + ChatColor.GREEN + "!");
			player.teleport(new Location(Bukkit.getWorld("world") , 0.5, 135.0, 1.5));
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			player.setMaxHealth(20);
			player.setHealth(20);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			Inventory inventory = player.getInventory();
			ItemStack star = new ItemStack(Material.NETHER_STAR);
			ItemStack potion = new ItemStack(Material.BLAZE_ROD);
			ItemMeta meta = star.getItemMeta();
			ItemMeta meta2 = potion.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GRAY + "||" + ChatColor.DARK_GREEN + ChatColor.BOLD + "KITS" + ChatColor.DARK_GRAY + "||");
			meta2.setDisplayName(ChatColor.DARK_GRAY + "||" + ChatColor.GOLD + ChatColor.BOLD + "EFFECTS" + ChatColor.DARK_GRAY + "||");
			List<String> lore = new ArrayList<String>();
			List<String> lore2 = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Right click me to open the kit menu!");
			lore2.add(ChatColor.GRAY + "Right click me to open the effect menu!");
			meta.setLore(lore);
			meta2.setLore(lore2);
			star.setItemMeta(meta);
			potion.setItemMeta(meta2);
			if (!inventory.contains(star)) {
				inventory.setItem(0, star);
			}
			if (!inventory.contains(potion)) {
				inventory.setItem(8, potion);
			}
		}
		else {
			spectators.add(player);
			player.teleport(spectator);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			player.sendMessage(ChatColor.DARK_GREEN + "You joined the game late, so you are now a spectator!");
			player.sendMessage(ChatColor.DARK_RED + "DO NOT attack other players!!!");
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		if (item.getItemStack().getType().equals(Material.NETHER_STAR)) {
			event.setCancelled(true);
		}
		if (item.getItemStack().getType().equals(Material.BLAZE_ROD)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void playerCraft(PrepareItemCraftEvent event) {
		Player player = (Player) event.getView().getPlayer();
		if (event.getInventory().getResult().getType()==Material.MOB_SPAWNER) {
			event.getInventory().setResult(null);
			player.sendMessage(ChatColor.DARK_RED + "NO CRAFTING CHESTS!");
		}
	}
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		ItemStack hand = player.getItemInHand();
		List<Location> locations = new ArrayList<Location>();
		if (action == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.CHEST) {
			//Blue chest
			//Red chest
			//Green chest
			//Purple chest
			//Purple-Blue right chest
			//Purple-Blue left chest
			//Blue-Red right chest
			//Blue-Red left chest
			//Red-Green right chest
			//Red-Green left chest
			//Green-Purple right chest
			//Green-Purple left chest
			locations.add(new Location(Bukkit.getWorld("world"), 967.0, 41.0, 1001.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1001.0, 41.0, 1033.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1033.0, 41.0, 999.0));
			locations.add(new Location(Bukkit.getWorld("world"), 999.0, 41.0, 967.0));
			locations.add(new Location(Bukkit.getWorld("world"), 980.0, 42.0, 978.0));
			locations.add(new Location(Bukkit.getWorld("world"), 978.0, 42.0, 980.0));
			locations.add(new Location(Bukkit.getWorld("world"), 978.0, 42.0, 1020.0));
			locations.add(new Location(Bukkit.getWorld("world"), 980.0, 42.0, 1022.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1020.0, 42.0, 1022.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1022.0, 42.0, 1020.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1022.0, 42.0, 980.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1020.0, 42.0, 978.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1002.0, 44.0, 1000.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1000.0, 44.0, 998.0));
			locations.add(new Location(Bukkit.getWorld("world"), 998.0, 44.0, 1000.0));
			locations.add(new Location(Bukkit.getWorld("world"), 1000.0, 44.0, 1002.0));
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			chestFull.add(true);
			for (Integer i=0; i<=locations.size() + 1; i++) {
				locations.get(i).getBlock().setType(Material.CHEST);
				Chest chest = (Chest)locations.get(i).getBlock().getState();
				Inventory inventory = chest.getInventory();
				Random r = new Random(System.currentTimeMillis());
				if (chestFull.get(i) == true) {
					inventory.addItem(new ItemStack(items.get(r.nextInt(items.size())),r.nextInt(1) + 1));
					inventory.addItem(new ItemStack(food.get(r.nextInt(food.size())),r.nextInt(10) + 1));
					inventory.addItem(new ItemStack(items.get(r.nextInt(items.size())),r.nextInt(1) + 1));
					inventory.addItem(new ItemStack(food.get(r.nextInt(food.size())),r.nextInt(10) + 1));
					inventory.addItem(new ItemStack(items.get(r.nextInt(items.size())),r.nextInt(1) + 1));
					inventory.addItem(new ItemStack(materials.get(r.nextInt(materials.size())),r.nextInt(30) + 1));
					inventory.addItem(new ItemStack(specialItems.get(r.nextInt(specialItems.size())),r.nextInt(3) + 1));
					inventory.addItem(new ItemStack(materials.get(r.nextInt(materials.size())),r.nextInt(30) + 1));
					inventory.addItem(new ItemStack(materials.get(r.nextInt(materials.size())),r.nextInt(30) + 1));
					inventory.addItem(new ItemStack(specialItems.get(r.nextInt(specialItems.size())),r.nextInt(3) + 1));
					inventory.addItem(new ItemStack(food.get(r.nextInt(food.size())),r.nextInt(10) + 1));
					inventory.addItem(new ItemStack(specialItems.get(r.nextInt(specialItems.size())),r.nextInt(3) + 1));
					chestFull.set(i, false);
				}
			}
		}
		else if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) { 
			if (hand.getType() == Material.NETHER_STAR) {
				player.openInventory(Main.kitInventory());
			}
			if (hand.getType() == Material.BLAZE_ROD) {
				player.openInventory(Main.funInventory());
			}
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Location location = player.getLocation();
		if (entity.getType() == EntityType.ZOMBIE) {
			player.openInventory(Main.kitInventory());
			player.playNote(location, Instrument.PIANO, Note.natural(1, Tone.A));
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		Player damager = (Player) event.getDamager();
		if (spectators.contains(damager)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerFall(EntityDamageEvent event) {
		DamageCause dc = event.getCause();
		if (dc == DamageCause.FALL)
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (running == true) {
			spectators.add(player);
			event.setRespawnLocation(new Location(Bukkit.getWorld("world") , 1000.5, 55.0, 1000.5));
			player.sendMessage(ChatColor.DARK_GREEN + "You joined the game late, so you are now a spectator!");
			player.sendMessage(ChatColor.DARK_RED + "DO NOT attack other players!!!");
			player.setMaxHealth(20);
			player.setHealth(20);
			player.setFlying(true);
			player.setCanPickupItems(false);
			player.setFoodLevel(20);
			player.teleport(spectator);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
		}
		else {
			event.setRespawnLocation(new Location(Bukkit.getWorld("world") , 903.5, 154.0, -585.5));
			player.setMaxHealth(20);
			player.setHealth(20);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		EntityDamageEvent evt = player.getLastDamageCause();
		DamageCause dc = evt.getCause();
		if (player instanceof Player) {
			Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " was killed by " + ChatColor.GOLD + killer.getName()
					+ ChatColor.BLUE + " from" + dc.name());
		}
		Main.p.removePlayer(player);
		Main.s.addPlayer(player);
		//		if (killers.containsKey(killer.getName()) && killer instanceof Player) {
		//			Integer killCount = killers.get(killer.getName());
		//			killers.put(killer.getName(), killCount +1);
		//			Bukkit.broadcastMessage(ChatColor.RED + killers.toString() + "Second Time");
		//		}
		//		else {
		//			killers.put(killer.getName(), 1);
		//			String kill = killers.toString();
		//			Bukkit.broadcastMessage(ChatColor.RED + kill + "First Time");
		//		}
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (spectators.contains(player)) {
			event.setCancelled(true);
		}
		else {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onClickItem(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();
		Inventory inventory = entity.getInventory();
		if (event.getInventory().getName().equals(inventory.getName())) {
			ItemStack clicked = event.getCurrentItem();
			if (clicked.getType() == Material.NETHER_STAR) {
				event.setCancelled(true);
			}
			if (clicked.getType() == Material.BLAZE_ROD) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();
		if ((entity instanceof Player)){
			Player player = (Player)entity;
			if (event.getInventory().getName().equals(Main.kitInventory().getName())) {
				event.setCancelled(true);
				ItemStack clicked = event.getCurrentItem();
				if (clicked!=null) {
					if (clicked.getType() == Material.BOW) {
						player.closeInventory();
						event.setCancelled(true);
						archerPlayers.add(player);
						if (jumperPlayers.contains(player) || tankPlayers.contains(player) || wizardPlayers.contains(player) 
								|| firemanPlayers.contains(player)) {
							jumperPlayers.remove(player);
							tankPlayers.remove(player);
							wizardPlayers.remove(player);
							firemanPlayers.remove(player);
						}
						player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "archer" + ChatColor.GREEN + "!");
					}
					else if (clicked.getType() == Material.ENDER_PEARL) {
						player.closeInventory();
						event.setCancelled(true);
						jumperPlayers.add(player);
						if (archerPlayers.contains(player) || tankPlayers.contains(player) || wizardPlayers.contains(player) 
								|| firemanPlayers.contains(player)) {
							archerPlayers.remove(player);
							tankPlayers.remove(player);
							wizardPlayers.remove(player);
							firemanPlayers.remove(player);
						}
						player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "jumper" + ChatColor.GREEN + "!");
					}
					else if (clicked.getType() == Material.LEATHER_CHESTPLATE) {
						player.closeInventory();
						event.setCancelled(true);
						tankPlayers.add(player);
						if (jumperPlayers.contains(player) || archerPlayers.contains(player) || wizardPlayers.contains(player) 
								|| firemanPlayers.contains(player)) {
							jumperPlayers.remove(player);
							archerPlayers.remove(player);
							wizardPlayers.remove(player);
							firemanPlayers.remove(player);
						}
						player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "tank" + ChatColor.GREEN + "!");
					}
					else if (clicked.getType() == Material.POTION) {
						player.closeInventory();
						event.setCancelled(true);
						wizardPlayers.add(player);
						if (jumperPlayers.contains(player) || tankPlayers.contains(player) || archerPlayers.contains(player) 
								|| firemanPlayers.contains(player)) {
							jumperPlayers.remove(player);
							tankPlayers.remove(player);
							archerPlayers.remove(player);
							firemanPlayers.remove(player);
						}
						player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "wizard" + ChatColor.GREEN + "!");
					}
					else if (clicked.getType() == Material.WATER_BUCKET) {
						player.closeInventory();
						event.setCancelled(true);
						firemanPlayers.add(player);
						if (jumperPlayers.contains(player) || tankPlayers.contains(player) || wizardPlayers.contains(player) 
								|| archerPlayers.contains(player)) {
							jumperPlayers.remove(player);
							tankPlayers.remove(player);
							wizardPlayers.remove(player);
							archerPlayers.remove(player);
						}
						player.sendMessage(ChatColor.GREEN + "You have selected the kit: " + ChatColor.BLUE + ChatColor.BOLD + "fireman" + ChatColor.GREEN + "!");
					}
				}
			}
		}
	}
	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();
		if ((entity instanceof Player)){
			Player player = (Player)entity;
			if (event.getInventory().getName().equals(Main.funInventory().getName())) {
				event.setCancelled(true);
				ItemStack clicked = event.getCurrentItem();
				if (clicked!=null) {
					if (clicked.getType() == Material.FEATHER) {
						player.setVelocity(new Vector(0,1,0));
						player.closeInventory();
						event.setCancelled(true);
					}
					else if (clicked.getType() == Material.ANVIL) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
						player.closeInventory();
						player.sendMessage(ChatColor.GRAY + "You are as slow as a snail!");
						event.setCancelled(true);
					}
					else if (clicked.getType() == Material.RAW_FISH) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
						player.closeInventory();
						player.sendMessage(ChatColor.BLUE + "You just got a useless potion effect!");
						event.setCancelled(true);
					}
					else if (clicked.getType() == Material.WOOL) {
						for (PotionEffect potion : player.getActivePotionEffects())
							player.removePotionEffect(potion.getType());
						player.closeInventory();
						player.sendMessage(ChatColor.GREEN + "Effects reset!");
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
