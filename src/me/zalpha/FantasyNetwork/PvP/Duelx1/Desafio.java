package me.zalpha.FantasyNetwork.PvP.Duelx1;

import org.bukkit.entity.Player;

public class Desafio {
	private Player desafiante;
	private Player desafiado;
	private String espada;
	private String armadura;
	private String regen;
	private boolean speed;
	private boolean strenght;
	private boolean sharp;
	private boolean normal;
	private long expire;
	private int level;

	public Desafio(final Player desafiante, final Player desafiado) {
		this.level = 1;
		this.desafiante = desafiante;
		this.desafiado = desafiado;
		this.espada = "DIAMOND_SWORD";
		this.armadura = "IRON_CHESTPLATE";
		this.regen = "Uma HotBar de sopa";
		this.speed = false;
		this.strenght = false;
		this.sharp = true;
		this.normal = true;
		this.level = 1;
		this.expire = System.currentTimeMillis() + 10000L;
	}

	public Desafio(final Player desafiante, final Player desafiado, final String espada, final String armadura,
			final String refil, final boolean speed, final boolean strenght, final boolean sharp, final int level) {
		this.level = 1;
		this.desafiante = desafiante;
		this.desafiado = desafiado;
		this.espada = espada;
		this.armadura = armadura;
		this.regen = refil;
		this.speed = speed;
		this.strenght = strenght;
		this.sharp = sharp;
		this.normal = false;
		this.level = level;
		this.expire = System.currentTimeMillis() + 20000L;
	}

	public Player getDesafiante() {
		return this.desafiante;
	}

	public Player getDesafiado() {
		return this.desafiado;
	}

	public String getSwordType() {
		return this.espada;
	}

	public String getArmorType() {
		return this.armadura;
	}

	public String getRefil() {
		return this.regen;
	}

	public boolean isSpeed() {
		return this.speed;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean isStreght() {
		return this.strenght;
	}

	public boolean isNormal() {
		return this.normal;
	}

	public boolean hasSharp() {
		return this.sharp;
	}

	public boolean hasExpire() {
		return this.expire < System.currentTimeMillis();
	}
}
