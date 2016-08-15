package com.strangeone101.pkconfigeditor.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.GeneralMethods;

public class ConfigMenu extends MenuBase {

	public Player player;
	public MenuBase prev;
	public boolean changesMade = false;
	
	public String path;
	
	public ConfigMenu(Player player, MenuBase previous) {
		super("PK Config Editor", 5);
		
		this.player = player;
		this.prev = previous;
		
		update();
		
		if (previous == null) {
			openMenu(player);
		} else {
			switchMenu(player, this);
		}		
	}
	
	public MenuItem getBackArrow() {
		MenuItem item = new MenuItem(ChatColor.YELLOW + (this.prev == null ? "Exit Menu" : "Back"), new MaterialData(Material.ARROW)) {
			@Override
			public void onClick(Player player) {
				if (prev == null) {
					closeMenu(player);
				} else {
					switchMenu(player, prev);
				}
			}
		};
		if (prev == null) {
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
					GeneralMethods.reloadPlugin(player);
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

	protected void update() {

		this.addMenuItem(getBackArrow(), 0, this.size / 9);
		this.addMenuItem(getSaveItem(), 8, this.size / 9);
	}

}
