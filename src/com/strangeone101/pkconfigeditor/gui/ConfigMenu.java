package com.strangeone101.pkconfigeditor.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class ConfigMenu extends MenuBase {

	public Player player;
	
	public boolean changesMade = false;
	
	public String path;
	public String prevPath;
	
	public Set<String> pastPaths = new HashSet<String>();
	
	public ConfigMenu(Player player) {
		this(player, null);
	}
	
	public ConfigMenu(Player player, String path) {
		super("PK Config Editor", 5);
		
		this.player = player;
		
		this.path = path;
		
		update();
		
		openMenu(player);
		
	}
	
	public MenuItem getBackArrow() {
		MenuItem item = new MenuItem(ChatColor.YELLOW + (pastPaths.size() == 0 ? "Exit Menu" : "Back"), new MaterialData(Material.ARROW)) {
			@Override
			public void onClick(Player player) {
				if (path == null) {
					closeMenu(player);
				} else {
					changeLocation(prevPath);
				}
				
			}
		};
		if (pastPaths.size() == 0) {
			item.addDescription(ChatColor.GRAY + "Click to exit the menu. Don't forget to save!");
		} else {
			item.addDescription(ChatColor.GRAY + "Click to go back to the previous menu");
		}
		
		return item;
	}
	
	public MenuItem getSaveItem() {
		@SuppressWarnings("deprecation")
		MenuItem item = new MenuItem((this.changesMade ? ChatColor.GREEN : ChatColor.YELLOW) + "Save Changes", new MaterialData(Material.STAINED_GLASS_PANE, (byte) (this.changesMade ? 5 : 7))) {
			@Override
			public void onClick(Player player) {
				if (changesMade) {
					ConfigManager.defaultConfig.save();
					GeneralMethods.reloadPlugin(player);
					changesMade = false;
					update();
				}
				
			}
		};
		
		if (this.changesMade) {
			item.addDescription(ChatColor.GRAY + "Click to save changes to the config!");
			item.addDescription(ChatColor.RED + "Warning: " + ChatColor.GRAY + "This will reload bending!");
		} else {
			item.addDescription(ChatColor.GRAY + "You have not made any changes to the config!");
		}
		
		return item;
	}
	
	public MenuItem getAbilityConfig(final Element element) {
		MenuItem item = new MenuItem(element.getColor() + "Edit " + element.getName() + " Configuration", getElementMaterial(element)) {

			@Override
			public void onClick(Player player) {
				changeLocation("Abilities." + element.getName());
			}
		};
		item.addDescription(ChatColor.GRAY + "Allows you to edit all config options for " + element.getName());
		
		return item;
	}
	
	@SuppressWarnings("deprecation")
	public void addOptionsForMove(final String path, Element element) {
		ConfigurationSection section = ConfigManager.defaultConfig.get().getConfigurationSection(path);
		
		int count = 0;
		
		for (final String key : section.getKeys(false)) {
			MenuItem item = null;
			if (ConfigManager.defaultConfig.get().get(path + "." + key) instanceof Boolean) {
				final boolean enabled = ConfigManager.defaultConfig.get().getBoolean(path + "." + key);
				String name = enabled ? (ChatColor.GREEN + key + ": True") : (ChatColor.RED + key + ": False");
				item = new MenuItem(name, new MaterialData(Material.WOOL, enabled ? (byte) 5 : (byte) 14)) {

					@Override
					public void onClick(Player player) {
						ConfigManager.defaultConfig.get().set(path + "." + key, !enabled);
						changesMade = true;
						update();
					}
				};
				item.addDescription(ChatColor.GRAY + "Click to toggle the " + key + " option");
			} else if (ConfigManager.defaultConfig.get().get(path + "." + key) instanceof ConfigurationSection) {
				item = new MenuItem(element.getColor() + key, getElementMaterial(element)) {

					@Override
					public void onClick(Player player) {
						changeLocation(path + "." + key);						
					}
				};
				item.addDescription(ChatColor.GRAY + "Click to edit the " + key + " options");
			} else if (ConfigManager.defaultConfig.get().get(path + "." + key) instanceof List) {
				item = new MenuItem(element.getColor() + key, getElementMaterial(element)) {

					@Override
					public void onClick(Player player) {	
						//Do nothing as of yet
					}
				};
				item.addDescription(ChatColor.RED + "Must be editted in the config");
			} else if (ConfigManager.defaultConfig.get().get(path + "." + key) instanceof String) {
				item = new MenuItem(element.getColor() + key, getElementMaterial(element)) {

					@Override
					public void onClick(Player player) {	
						//Do nothing as of yet
					}
				};
				item.addDescription(ChatColor.RED + "Must be editted in the config");
			} else {
				final ConfigMenu menu_ = this;
				item = new MenuItem(element.getColor() + key + ": " + ConfigManager.defaultConfig.get().get(path + "." + key).toString(), getElementMaterial(element)) {

					@Override
					public void onClick(Player player) {
						switchMenu(player, new ValueEditorMenu(player, menu_, path + "." + key));		
					}
				};
				item.addDescription(ChatColor.GRAY + "Click to edit the value");
			}
			
			addMenuItem(item, getIndex(count, section.getKeys(false).size()));
			count++;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static MaterialData getElementMaterial(Element element) {
		if (element == Element.AIR) return new MaterialData(Material.QUARTZ_BLOCK);
		if (element == Element.FIRE) return new MaterialData(Material.NETHERRACK);
		if (element == Element.EARTH) return new MaterialData(Material.GRASS);
		if (element == Element.WATER) return new MaterialData(Material.STAINED_CLAY, (byte) 11);
		if (element == Element.CHI) return new MaterialData(Material.STAINED_CLAY, (byte) 4);
		if (element == Element.BLOOD) return new MaterialData(Material.STAINED_CLAY, (byte) 14);
		if (element == Element.ICE) return new MaterialData(Material.ICE);
		if (element == Element.PLANT) return new MaterialData(Material.LEAVES, (byte) 3);
		if (element == Element.HEALING) return new MaterialData(Material.STAINED_CLAY, (byte) 11);
		if (element == Element.COMBUSTION) return new MaterialData(Material.NETHERRACK);
		if (element == Element.LIGHTNING) return new MaterialData(Material.NETHERRACK);
		if (element == Element.LAVA) return new MaterialData(Material.STAINED_CLAY, (byte) 1);
		if (element == Element.METAL) return new MaterialData(Material.IRON_BLOCK);
		if (element == Element.SAND) return new MaterialData(Material.SAND);
		if (element == Element.SPIRITUAL) return new MaterialData(Material.QUARTZ_BLOCK);
		if (element == Element.FLIGHT) return new MaterialData(Material.QUARTZ_BLOCK);
		if (element instanceof SubElement) {
			Element parent = ((SubElement)element).getParentElement();
			if (parent == Element.AIR) return new MaterialData(Material.QUARTZ_BLOCK);
			if (parent == Element.FIRE) return new MaterialData(Material.NETHERRACK);
			if (parent == Element.WATER) return new MaterialData(Material.STAINED_CLAY, (byte) 11);
			if (parent == Element.EARTH) return new MaterialData(Material.GRASS);
			if (parent == Element.CHI) return new MaterialData(Material.STAINED_CLAY, (byte) 4);
		}
		return new MaterialData(Material.BEACON);
	}
	
	public static int getIndex(int counter, int max) {
		if (max <= 7){
			return 9 + counter + 1; //1 row down, 1 across
		} else if (max <= 14){
			return ((counter % 7) + 1) + ((counter / 7) * 9) + 9; //1 row down, 1 across but supports 2 rows & up to 14 icons
		} else if (max <= 21) {
			return ((counter % 7) + 1) + ((counter / 7) * 9); //Just 3 rows of 7
		} else if (max <= 32) {
			return counter + (counter >= 26 ? 2 : 0); //Now do rows of 9 with 5 in the middle at the bottom
		}
		
		return 0;
	}

	@SuppressWarnings("deprecation")
	protected void update() {
		this.clearInventory();
		
		this.addMenuItem(getBackArrow(), 0, 4);
		this.addMenuItem(getSaveItem(), 8, 4);
		
		if (path == null || path.equals("")) {
			this.addMenuItem(getAbilityConfig(Element.AIR), 2, 1);
			this.addMenuItem(getAbilityConfig(Element.EARTH), 3, 1);
			this.addMenuItem(getAbilityConfig(Element.CHI), 4, 1);
			this.addMenuItem(getAbilityConfig(Element.FIRE), 5, 1);
			this.addMenuItem(getAbilityConfig(Element.WATER), 6, 1);
			this.addMenuItem(getAbilityConfig(Element.AVATAR), 4, 2);
		} else if (path.startsWith("Abilities.")) {
			Element element = Element.fromString(path.split("\\.")[1]);
			if (element == null) {
				this.addMenuItem(new MenuItem(ChatColor.RED + "" + ChatColor.MAGIC + "ERRORRRRRRR", new MaterialData(Material.APPLE)) {

					@Override
					public void onClick(Player player) {
						closeMenu(player);
						
					}}, 0);
				return;
			}
			
			//If the user is only in the Abilities.ELEMENT menu OR if they are in an element combo menu.
			if (path.split("\\.").length == 2 || (path.split("\\.").length == 3 && path.split("\\.")[2].toLowerCase().contains("combo"))) { 
				ConfigurationSection section = ConfigManager.defaultConfig.get().getConfigurationSection(path);
				int index = 0;
				Set<String> keys = section.getKeys(false);
				final boolean isCombo = path.split("\\.").length == 3 && path.split("\\.")[2].toLowerCase().contains("combo");
				for (String key : keys) {
					final String key_ = key;
					String name = key;
					String meta = name;
					MaterialData icon;
					int indexToUse = index;
					if (isCombo && key.equalsIgnoreCase("enabled")) {
						boolean enabled = ConfigManager.defaultConfig.get().getBoolean(path + ".Enabled");
						icon = new MaterialData(Material.WOOL, (byte) (enabled ? 5 : 14));
						name = enabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
						meta = enabled ? "enabled" : "disabled";
						indexToUse = 0;
					}
					if (key.equalsIgnoreCase("Passive")) {
						icon = getElementMaterial(element);
						name = element.getName() + " Passive";
						meta = element.getName() + "'s Passive Ability";
					} else if (key.toLowerCase().contains("combo")) {
						icon = getElementMaterial(element);
						name = element.getName() + " Combos";
						meta = name;
					}  else if (CoreAbility.getAbility(name) != null) {
						element = CoreAbility.getAbility(name).getElement();
						icon = getElementMaterial(element);
					} else {
						icon = getElementMaterial(element);
					}
					
					MenuItem item = new MenuItem(element.getColor() + name, icon) {

						@Override
						public void onClick(Player player) {
							if (isCombo && key_.equalsIgnoreCase("enabled")) {
								ConfigManager.defaultConfig.get().set(path + ".Enabled", !ConfigManager.defaultConfig.get().getBoolean(path + ".Enabled"));
								changesMade = true;
								update();
							} else {
								changeLocation(path + "." + key_);
							}
							
						}
					};
					
					item.addDescription(ChatColor.GRAY + "Edit the config options for " + meta);
					this.addMenuItem(item, getIndex(indexToUse, keys.size()) + (isCombo ? 1 : 0)); //Combos are offset by 1 because of the enabled/disabled button
					index++;
				}
			} else if (path.split("\\.").length > 2) {
				addOptionsForMove(path, element);
			}
		}
	}
	
	public void changeLocation(String newPath) {
		if (newPath == null || newPath.equals(this.prevPath)) {
			this.path = newPath;
			this.prevPath = newPath == null ? null : (String) this.pastPaths.toArray()[this.pastPaths.size() - 1];
			this.pastPaths.remove(this.prevPath);
		} else {
			this.pastPaths.add(this.prevPath);
			this.prevPath = path;
			this.path = newPath;
		}
		
		update();
	}

}
