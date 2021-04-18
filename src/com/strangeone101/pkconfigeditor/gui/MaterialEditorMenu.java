package com.strangeone101.pkconfigeditor.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.strangeone101.easygui.MenuBase;
import com.strangeone101.easygui.MenuItem;

public class MaterialEditorMenu extends MenuBase {

	private List<Material> materials = new ArrayList<Material>();
	private Player player;
	private ConfigMenu prev;
	
	public MaterialEditorMenu(Player player, ConfigMenu previous, List<Material> materials) {
		super("PK Config Editor", 5);
		
		this.materials.addAll(materials);
		this.player = player;
		this.prev = previous;
	}
	
	public MenuItem getMaterialIcon(Material material) {
		@SuppressWarnings("deprecation")
		MenuItem item = new MenuItem("GLITCH AHHHHHHHHHh", new MaterialData(material, (byte)0)) {

			@Override
			public void onClick(Player player) {
				
			}
			
			@Override
			public ItemStack getItemStack() {
				ItemStack stack = super.getItemStack();
				stack.getItemMeta().setDisplayName(""); //removes the custom name
				return stack;
			}
			
		};
		item.addDescription(ChatColor.GRAY + "Shift click to remove. Place items from");
		item.addDescription(ChatColor.GRAY + "your inventory here to add them to this list.");
		return item;
	}
	
	public MenuItem getSaveItem() {
		@SuppressWarnings("deprecation")
		MenuItem item = new MenuItem((prev.changesMade ? ChatColor.GREEN : ChatColor.YELLOW) + "Save Changes", new MaterialData(Material.STAINED_GLASS_PANE, (byte) (prev.changesMade ? 5 : 7))) {
			@Override
			public void onClick(Player player) {
				if (prev.changesMade) {
					ConfigManager.defaultConfig.save();
					GeneralMethods.reloadPlugin(player);
					prev.changesMade = false;
					update();
				}
				
			}
		};
		
		if (prev.changesMade) {
			item.addDescription(ChatColor.GRAY + "Click to save changes to the config!");
			item.addDescription(ChatColor.RED + "Warning: " + ChatColor.GRAY + "This will reload bending!");
		} else {
			item.addDescription(ChatColor.GRAY + "You have not made any changes to the config!");
		}
		
		return item;
	}
	
	public MenuItem getBackArrow() {
		MenuItem item = new MenuItem(ChatColor.YELLOW + "Back", new MaterialData(Material.ARROW)) {
			@Override
			public void onClick(Player player) {
				switchMenu(player, prev);
				prev.update();
			}
		};
		
		item.addDescription(ChatColor.GRAY + "Click to go back to the previous menu");

		return item;
	}
	
	public void addMaterial(Material material) {
		if (materials.contains(material)) {
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
			player.sendMessage(ChatColor.RED + "This material is already in the list!");
			return;
		}
		
		materials.add(material);
		update();
		
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
	}

	private void update() {
		this.clearInventory();
		
		this.addMenuItem(getBackArrow(), 0, 4);
		this.addMenuItem(getSaveItem(), 8, 4);
		
		int i = 0;
		
		
		
		for (Material mat : materials) {
			this.addMenuItem(getMaterialIcon(mat), ConfigMenu.getIndex(i, materials.size()));
			i++;
		}
		
	}

}
