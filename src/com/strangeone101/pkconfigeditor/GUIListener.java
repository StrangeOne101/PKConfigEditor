package com.strangeone101.pkconfigeditor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.strangeone101.pkconfigeditor.gui.MenuBase;
import com.strangeone101.pkconfigeditor.gui.MenuItem;

public class GUIListener implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onMenuItemClicked(InventoryClickEvent event) 
	{
		try
		{
			Inventory inventory = event.getInventory();
	        if (inventory.getHolder() instanceof MenuBase) 
	        {
	            MenuBase menu = (MenuBase) inventory.getHolder();
	            if (event.getWhoClicked() instanceof Player) 
	            {
	                Player player = (Player) event.getWhoClicked();
	                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) 
	                {
	                	int index = event.getRawSlot();
	                    if (index < inventory.getSize()) 
	                    {
	                    	if (event.getCursor().getType() != Material.AIR)
	                    	{
	                    		event.setCancelled(true);
	                    		menu.closeMenu(player);
	                    		player.sendMessage(ChatColor.RED + "The bending gui cannot be tampered with!");
	                    	}
	                    	MenuItem item = menu.getMenuItem(index);
	                    	if (item != null)
	                    	{
	                    		item.isShiftClicked = event.isShiftClick();
	                    		item.onClick(player);
	                    		event.setCancelled(true);
	                    	}
	                    }
	                    else
	                    {
	                    	menu.setLastClickedSlot(index);
	                    }
	                }
	            }
	        }
		}
		catch (Exception e)
		{
			event.getWhoClicked().closeInventory();
			event.getWhoClicked().sendMessage(ChatColor.RED + "An error occured while processing the clickevent. Please report this to your admin or the plugin developer!");
			e.printStackTrace();
		}
    }
}
