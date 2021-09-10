package com.zalpha.FantasyNetwork.PvP.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.zalpha.FantasyNetwork.PvP.FantasyKits;

public abstract class MenuPaged implements Listener {
	private Player player;
	private List<Inventory> inventorys;
	protected int currentPage;
	protected int inventorySize;
	protected String inventoryTitle;
	protected boolean openPage;
	Map<Integer, ItemStack> noUse;
	public static ItemStack next;
	public static ItemStack previous;
	public static ItemStack kits;
	public static ItemStack warps;

	static {
		MenuPaged.next = Manager.criarItem2(Material.ARROW, "§a\u25ba Pr\u00f3xima pagina", (short) 0, 1,
				new String[] { "§7Clique para ir para o proxima pagina" });
		MenuPaged.previous = Manager.criarItem2(Material.ARROW, "§b\u25ba Pagina anterior", (short) 0, 1,
				new String[] { "§7Clique para ir para o pagina anterior" });
		MenuPaged.kits = Manager.criarItem2(Material.CHEST, "§a\u25ba Kits", (short) 0, 1,
				new String[] { "§7Aqui est\u00e3o os kits que voc\u00ea possui." });
		MenuPaged.warps = Manager.criarItem2(Material.BOOK, "§a\u25ba Warps", (short) 0, 1,
				new String[] { "§7Aqui est\u00e3o as warps." });
	}

	public Player getPlayer() {
		return this.player;
	}

	public List<Inventory> getInventorys() {
		return this.inventorys;
	}

	public MenuPaged(final Player p, final String title, final int size) {
		this.currentPage = 1;
		this.inventorySize = 54;
		this.openPage = false;
		this.noUse = new HashMap<Integer, ItemStack>();
		this.player = p;
		this.inventorySize = size;
		this.inventorys = new ArrayList<Inventory>();
		this.inventoryTitle = title.replace("&", "§");
		Bukkit.getPluginManager().registerEvents(this, FantasyKits.getPlugin());
	}

	public boolean addNoItems(final ItemStack item, final int... slots) {
		if (slots.length == 0) {
			return false;
		}
		final int[] arrayOfInt = slots;
		for (int j = slots.length, i = 0; i < j; ++i) {
			final int i2 = arrayOfInt[i];
			this.noUse.put(i2, (item != null) ? item : new ItemStack(Material.AIR));
		}
		return true;
	}

	public boolean addItems(final List<ItemStack> list, final int firstSlot, final int endSlot, final Type t) {
		if (list.isEmpty()) {
			final ItemStack[] itens = this.generatePage(this.inventorySize);
			for (final Map.Entry<Integer, ItemStack> entry : this.noUse.entrySet()) {
				itens[entry.getKey()] = entry.getValue();
			}
			final Inventory inv = Bukkit.createInventory((InventoryHolder) null, itens.length,
					this.inventoryTitle.replace("<pag>", String.valueOf(this.inventorys.size() + 1))
							.replace("<max>", new StringBuilder().append(this.inventorys.size()).toString())
							.replace("<max>", new StringBuilder().append(this.inventorys.size()).toString()));
			inv.setContents(itens);
			this.inventorys.add(inv);
			if (t == Type.KITS) {
				inv.setItem(4, MenuPaged.kits);
			}
			if (t == Type.WARPS) {
				inv.setItem(4, MenuPaged.warps);
			}
		} else {
			int currentSlot = firstSlot;
			ItemStack[] itens2 = this.generatePage(this.inventorySize);
			for (final Map.Entry<Integer, ItemStack> entry2 : this.noUse.entrySet()) {
				itens2[entry2.getKey()] = entry2.getValue();
			}
			for (int i = 0; i < list.size(); ++i) {
				if (itens2 == null) {
					itens2 = this.generatePage(this.inventorySize);
					currentSlot = firstSlot;
					for (final Map.Entry<Integer, ItemStack> entry3 : this.noUse.entrySet()) {
						itens2[entry3.getKey()] = entry3.getValue();
					}
				}
				if (this.noUse.containsKey(currentSlot)) {
					--i;
					if (++currentSlot == endSlot) {
						final Inventory inv2 = Bukkit.createInventory((InventoryHolder) null, itens2.length,
								this.inventoryTitle.replace("<pag>", String.valueOf(this.inventorys.size() + 1))
										.replace("<max>",
												new StringBuilder().append(this.inventorys.size()).toString()));
						inv2.setContents(itens2);
						if (t == Type.KITS) {
							inv2.setItem(4, MenuPaged.kits);
						}
						if (t == Type.WARPS) {
							inv2.setItem(4, MenuPaged.warps);
						}
						final int size = this.inventorys.size();
						if (size > 0) {
							inv2.setItem(27, MenuPaged.previous);
							this.inventorys.get(size - 1).setItem(35, MenuPaged.next);
						}
						this.inventorys.add(inv2);
						itens2 = null;
					}
				} else if (currentSlot == endSlot) {
					final Inventory inv2 = Bukkit.createInventory((InventoryHolder) null, itens2.length,
							this.inventoryTitle.replace("<pag>", String.valueOf(this.inventorys.size() + 1))
									.replace("<max>", new StringBuilder().append(this.inventorys.size()).toString()));
					inv2.setContents(itens2);
					if (t == Type.KITS) {
						inv2.setItem(4, MenuPaged.kits);
					}
					if (t == Type.WARPS) {
						inv2.setItem(4, MenuPaged.warps);
					}
					final int size = this.inventorys.size();
					if (size > 0) {
						inv2.setItem(27, MenuPaged.previous);
						this.inventorys.get(size - 1).setItem(35, MenuPaged.next);
					}
					this.inventorys.add(inv2);
					itens2 = null;
				} else {
					try {
						itens2[currentSlot++] = list.get(i);
					} catch (Exception e) {
						--i;
						final Inventory inv3 = Bukkit.createInventory((InventoryHolder) null, itens2.length,
								this.inventoryTitle.replace("<pag>", String.valueOf(this.inventorys.size() + 1))
										.replace("<max>",
												new StringBuilder().append(this.inventorys.size()).toString()));
						inv3.setContents(itens2);
						if (t == Type.KITS) {
							inv3.setItem(4, MenuPaged.kits);
						}
						if (t == Type.WARPS) {
							inv3.setItem(4, MenuPaged.warps);
						}
						final int size2 = this.inventorys.size();
						if (size2 > 0) {
							inv3.setItem(27, MenuPaged.previous);
							this.inventorys.get(size2 - 1).setItem(35, MenuPaged.next);
						}
						this.inventorys.add(inv3);
						itens2 = null;
						continue;
					}
					if (i + 1 == list.size()) {
						final Inventory inv4 = Bukkit.createInventory((InventoryHolder) null, itens2.length,
								this.inventoryTitle.replace("<pag>", String.valueOf(this.inventorys.size() + 1))
										.replace("<max>",
												new StringBuilder().append(this.inventorys.size()).toString()));
						for (final Map.Entry<Integer, ItemStack> entry4 : this.noUse.entrySet()) {
							itens2[entry4.getKey()] = entry4.getValue();
						}
						inv4.setContents(itens2);
						if (t == Type.KITS) {
							inv4.setItem(4, MenuPaged.kits);
						}
						if (t == Type.WARPS) {
							inv4.setItem(4, MenuPaged.warps);
						}
						final int size3 = this.inventorys.size();
						if (size3 > 0) {
							inv4.setItem(27, MenuPaged.previous);
							this.inventorys.get(size3 - 1).setItem(35, MenuPaged.next);
						}
						this.inventorys.add(inv4);
						break;
					}
				}
			}
		}
		this.currentPage = 1;
		return true;
	}

	public boolean openPage(final Player p, final int page) {
		if (page < 1 || page > this.inventorys.size()) {
			return false;
		}
		if (p == null || !p.isOnline()) {
			return false;
		}
		this.currentPage = page;
		final Inventory inv = this.inventorys.get(this.currentPage - 1);
		this.openPage = true;
		this.openPage = false;
		p.openInventory(inv);
		return true;
	}

	public boolean inventoryEquals(final Inventory inv) {
		return this.inventorys.get(this.currentPage - 1).equals(inv);
	}

	private ItemStack[] generatePage(int pageSize) {
		pageSize = (int) Math.ceil(pageSize / 9.0) * 9;
		return new ItemStack[pageSize];
	}

	public enum Type {
		KITS("KITS", 0, 54), CAIXAS("CAIXAS", 1, 54), PERFIL("PERFIL", 2, 54), WARPS("WARPS", 3, 45);

		private int size;

		private Type(final String s, final int n, final int size) {
			this.size = size;
		}

		public int getSize() {
			return this.size;
		}
	}
}
