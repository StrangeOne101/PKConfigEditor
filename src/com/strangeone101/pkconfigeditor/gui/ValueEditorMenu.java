package com.strangeone101.pkconfigeditor.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.strangeone101.easygui.MenuBase;
import com.strangeone101.easygui.MenuItem;

public class ValueEditorMenu extends MenuBase {

	public ConfigMenu prev;
	public String path;
	public Player player;
	public Object baseObj;
	public Object obj;
	
	public ValueEditorMenu(Player player, ConfigMenu previous, String path) {
		super("PK Config Editor", 5);
		
		this.prev = previous;
		this.path = path;
		this.player = player;
		
		baseObj = ConfigManager.defaultConfig.get().get(path);
		obj = baseObj;
		
		update();
		openMenu(player);
	}

	private void update() {
		this.clearInventory();
		
		this.addMenuItem(getBackArrow(), 0, 4);
		this.addMenuItem(getSaveItem(), 8, 4);
		//player.sendMessage(path);
		Object o = obj;
		Element element = Element.fromString(path.split("\\.")[1]);
		
		this.addMenuItem(getAjustArrow(o, (byte)-3), 1, 2);
		this.addMenuItem(getAjustArrow(o, (byte)-2), 2, 2);
		this.addMenuItem(getAjustArrow(o, (byte)-1), 3, 2);
		this.addMenuItem(getValueIcon(element, o), 4, 2);
		this.addMenuItem(getAjustArrow(o, (byte)1), 5, 2);
		this.addMenuItem(getAjustArrow(o, (byte)2), 6, 2);
		this.addMenuItem(getAjustArrow(o, (byte)3), 7, 2);
		
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
	
	public MenuItem getAjustArrow(final Object originalValue, byte type) {
		
		Object toAdd = null;
		
		if (originalValue instanceof Integer) {
			if ((int)originalValue < 10) {
				if (Math.abs(type) == 1) {
					toAdd = 1;
				} else if (Math.abs(type) == 2) {
					toAdd = 2;
				} if (Math.abs(type) == 3) {
					toAdd = 5;
				}
			} else if ((int)originalValue < 50) {
				if (Math.abs(type) == 1) {
					toAdd = 1;
				} else if (Math.abs(type) == 2) {
					toAdd = 5;
				} if (Math.abs(type) == 3) {
					toAdd = 10;
				}
			} else if ((int)originalValue < 100) {
				if (Math.abs(type) == 1) {
					toAdd = 1;
				} else if (Math.abs(type) == 2) {
					toAdd = 10;
				} if (Math.abs(type) == 3) {
					toAdd = 50;
				}
			} else if ((int)originalValue < 1000) {
				if (Math.abs(type) == 1) {
					toAdd = 100;
				} else if (Math.abs(type) == 2) {
					toAdd = 200;
				} if (Math.abs(type) == 3) {
					toAdd = 500;
				}
			} else if ((int)originalValue < 5000) {
				if (Math.abs(type) == 1) {
					toAdd = 500;
				} else if (Math.abs(type) == 2) {
					toAdd = 1000;
				} if (Math.abs(type) == 3) {
					toAdd = 2000;
				}
			} else if ((int)originalValue < 10000) {
				if (Math.abs(type) == 1) {
					toAdd = 500;
				} else if (Math.abs(type) == 2) {
					toAdd = 1000;
				} if (Math.abs(type) == 3) {
					toAdd = 5000;
				}
			} else if ((int)originalValue < 60000) {
				if (Math.abs(type) == 1) {
					toAdd = 1000;
				} else if (Math.abs(type) == 2) {
					toAdd = 5000;
				} if (Math.abs(type) == 3) {
					toAdd = 10000;
				}
			}
			
			if (type < 0) toAdd = ((int)toAdd) * -1;
		} else if (originalValue instanceof Double || originalValue instanceof Float) {
			if ((double)originalValue <= 0.05) {
				if (Math.abs(type) == 1) {
					toAdd = 0.005D;
				} else if (Math.abs(type) == 2) {
					toAdd = 0.01D;
				} if (Math.abs(type) == 3) {
					toAdd = 0.05D;
				}
			} else if ((double)originalValue <= 0.2) {
				if (Math.abs(type) == 1) {
					toAdd = 0.01D;
				} else if (Math.abs(type) == 2) {
					toAdd = 0.05D;
				} if (Math.abs(type) == 3) {
					toAdd = 0.1D;
				}
			} else if ((double)originalValue < 0.5) {
				if (Math.abs(type) == 1) {
					toAdd = 0.05D;
				} else if (Math.abs(type) == 2) {
					toAdd = 0.1D;
				} if (Math.abs(type) == 3) {
					toAdd = 0.2D;
				}
			} else if ((double)originalValue < 2) {
				if (Math.abs(type) == 1) {
					toAdd = 0.1D;
				} else if (Math.abs(type) == 2) {
					toAdd = 0.5D;
				} if (Math.abs(type) == 3) {
					toAdd = 1D;
				}
			} else if ((double)originalValue < 5) {
				if (Math.abs(type) == 1) {
					toAdd = 0.1D;
				} else if (Math.abs(type) == 2) {
					toAdd = 0.5D;
				} if (Math.abs(type) == 3) {
					toAdd = 2D;
				}
			} else if ((double)originalValue < 10) {
				if (Math.abs(type) == 1) {
					toAdd = 0.5D;
				} else if (Math.abs(type) == 2) {
					toAdd = 1D;
				} if (Math.abs(type) == 3) {
					toAdd = 5D;
				}
			} else if ((double)originalValue < 50) {
				if (Math.abs(type) == 1) {
					toAdd = 0.5D;
				} else if (Math.abs(type) == 2) {
					toAdd = 5D;
				} if (Math.abs(type) == 3) {
					toAdd = 10D;
				}
			} else if ((double)originalValue >= 50) {
				if (Math.abs(type) == 1) {
					toAdd = 0.5D;
				} else if (Math.abs(type) == 2) {
					toAdd = 10D;
				} if (Math.abs(type) == 3) {
					toAdd = 20D;
				}
			} 
			
			if (type < 0) toAdd = ((double)toAdd) * -1;
		} else if (originalValue instanceof Long) {
			if ((long)originalValue < 500) {
				if (Math.abs(type) == 1) {
					toAdd = 50L;
				} else if (Math.abs(type) == 2) {
					toAdd = 100L;
				} if (Math.abs(type) == 3) {
					toAdd = 200L;
				}
			} else if ((long)originalValue < 1000) {
				if (Math.abs(type) == 1) {
					toAdd = 100L;
				} else if (Math.abs(type) == 2) {
					toAdd = 200L;
				} if (Math.abs(type) == 3) {
					toAdd = 500L;
				}
			} else if ((long)originalValue < 5000) {
				if (Math.abs(type) == 1) {
					toAdd = 500L;
				} else if (Math.abs(type) == 2) {
					toAdd = 1000L;
				} if (Math.abs(type) == 3) {
					toAdd = 2000L;
				}
			} else if ((long)originalValue < 10000) {
				if (Math.abs(type) == 1) {
					toAdd = 500L;
				} else if (Math.abs(type) == 2) {
					toAdd = 1000L;
				} if (Math.abs(type) == 3) {
					toAdd = 5000L;
				}
			} else if ((long)originalValue < 60000) {
				if (Math.abs(type) == 1) {
					toAdd = 1000L;
				} else if (Math.abs(type) == 2) {
					toAdd = 5000L;
				} if (Math.abs(type) == 3) {
					toAdd = 10000L;
				}
			}
			
			if (type < 0) toAdd = ((long)toAdd) * -1;
		}
		
		final Object newToAdd = toAdd;
		
		String title = toAdd.toString() + (originalValue instanceof Integer && (int)originalValue > 500 ? " (" + (((int)toAdd) / 1000) + " seconds)" : "");
		
		if (type > 0) {
			title = "+" + title;
		}
		
		MenuItem item = new MenuItem(ChatColor.YELLOW + title, new MaterialData(Material.ARROW), Math.abs(type)) {
			@Override
			public void onClick(Player player) {
				Object newObject = -1;
				
				if (originalValue instanceof Integer) {
					newObject = ((int)originalValue) + (int)newToAdd;
				} else if (originalValue instanceof Long) {
					newObject = ((long)originalValue) + (long)newToAdd;
				} else if (originalValue instanceof Double || originalValue instanceof Float) {
					newObject = ((double)originalValue) + (double)newToAdd;
				}
				
				obj = newObject;
				prev.changesMade = true;
				update();
			}
		};
		
		item.addDescription(ChatColor.GRAY + "Click to add this value");
		item.addDescription(ChatColor.GRAY + "Current value: " + ChatColor.YELLOW + originalValue.toString());
		if (!obj.equals(baseObj))
		item.addDescription(ChatColor.GRAY + "Original value: " + ChatColor.YELLOW + baseObj.toString());

		return item;
	}
	
	public MenuItem getValueIcon(Element element, Object value) {
		MenuItem item = new MenuItem(element.getColor() + path.split("\\.")[path.split("\\.").length - 1] + ": " + value.toString(), ConfigMenu.getElementMaterial(element)) {
			@Override
			public void onClick(Player player) {}
		};
		
		item.addDescription(ChatColor.GRAY + "Adjust the value with the arrows");
		return item;
	}
	
	public MenuItem getSaveItem() {
		@SuppressWarnings("deprecation")
		MenuItem item = new MenuItem((prev.changesMade ? ChatColor.GREEN : ChatColor.YELLOW) + "Save Changes", new MaterialData(Material.STAINED_GLASS_PANE, (byte) (prev.changesMade ? 5 : 7))) {
			@Override
			public void onClick(Player player) {
				if (prev.changesMade) {
					ConfigManager.defaultConfig.get().set(path, obj);
					baseObj = obj;
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

	
}
