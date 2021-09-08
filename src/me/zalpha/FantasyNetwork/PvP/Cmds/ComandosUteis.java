package me.zalpha.FantasyNetwork.PvP.Cmds;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import java.util.Iterator;
import org.bukkit.potion.PotionEffect;
import com.zalpha.FantasyNetwork.PvP.util.MenuPaged;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import com.zalpha.FantasyNetwork.PvP.util.Inventarios;
import com.zalpha.FantasyNetwork.PvP.util.ConfigAPI;
import org.bukkit.Bukkit;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import org.bukkit.entity.Entity;
import com.zalpha.FantasyNetwork.PvP.util.Manager;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ComandosUteis implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Manager.CONSOLE);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("skit")) {
            final Player jogador = (Player)sender;
            if (jogador.hasPermission("fantasy.skit")) {
                if (args.length == 1) {
                    if (Manager.argumentoNumero(args[0])) {
                        final int raio = Integer.valueOf(args[0]);
                        jogador.sendMessage(String.valueOf(Manager.prefix) + " §7Seu inventario foi setado em um raio de §a" + raio + " " + ((raio == 1) ? "bloco" : "blocos") + "§7.");
                        for (final Entity entidades : jogador.getNearbyEntities((double)raio, (double)raio, (double)raio)) {
                            if (entidades instanceof Player) {
                                final Player jogadores = (Player)entidades;
                                if (Duelx1.isIn1v1(jogadores) || Manager.pegarKit(jogadores).equalsIgnoreCase("1v1")) {
                                    continue;
                                }
                                jogadores.getInventory().clear();
                                jogadores.getInventory().setArmorContents(jogador.getInventory().getArmorContents());
                                jogadores.getInventory().setContents(jogador.getInventory().getContents());
                                jogadores.sendMessage(String.valueOf(Manager.prefix) + " §7Seu inventario foi trocado para o invent\u00c3¡rio de §f" + jogador.getName() + "§7.");
                            }
                        }
                    }
                    else {
                        jogador.sendMessage(String.valueOf(Manager.prefix) + " §7O argumento \"" + args[0] + "\" nao se trata de um numero inteiro.");
                    }
                }
                else {
                    jogador.sendMessage(String.valueOf(Manager.use) + "skit <raio>");
                }
            }
            else {
                jogador.sendMessage(Manager.SEMPERM);
            }
        }
        final Player p = (Player)sender;
        if (label.equalsIgnoreCase("sc") || label.equalsIgnoreCase("staffchat")) {
            if (p.hasPermission("fantasy.staffchat")) {
                if (Manager.comStaffAtivado(p)) {
                    Manager.StaffChat.remove(p.getName());
                    p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce Saiu Do Staff Chat.");
                }
                else if (!Manager.comStaffAtivado(p)) {
                    Manager.StaffChat.add(p.getName());
                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Entrou No Staff Chat.");
                }
            }
            else {
                p.sendMessage(Manager.SEMPERM);
            }
        }
        if (cmd.getName().equalsIgnoreCase("1v1") && args.length == 0) {
            if (Manager.pegarKit(p).equalsIgnoreCase("1v1")) {
                p.getInventory().clear();
                Manager.darItens(p);
                p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce Saiu Da 1v1.");
                p.getInventory().setArmorContents((ItemStack[])null);
                p.teleport(p.getWorld().getSpawnLocation());
                Manager.removerArrays(p);
                Manager.darItens(p);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                p.setGameMode(GameMode.SURVIVAL);
                return true;
            }
            final Location below = p.getLocation().subtract(0.0, 1.0, 0.0);
            final Block blockBelow = below.getBlock();
            if (blockBelow.getType() == Material.AIR) {
                p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce N\u00c3O Pode Teleportar Para A 1v1 Enquanto Estiver No Ar.");
                return true;
            }
            p.getInventory().clear();
            Manager.darItens(p);
            Manager.removerArrays(p);
            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce entrou na 1v1.");
            p.getInventory().setArmorContents((ItemStack[])null);
            Duelx1.teleport1v1(p);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            p.setGameMode(GameMode.SURVIVAL);
            return true;
        }
        else {
            if (label.equalsIgnoreCase("score")) {
                if (PlayerData.get(p).getScoreB()) {
                    PlayerData.get(p).setScore(false);
                    p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce desativou a scoreboard.");
                }
                else if (!PlayerData.get(p).getScoreB()) {
                    PlayerData.get(p).setScore(true);
                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce ativou a scoreboard");
                }
            }
            if (cmd.getName().equalsIgnoreCase("limpar")) {
                if (!p.hasPermission("fantasy.clear")) {
                    p.sendMessage("§5§lFANTASY §8\u279c§c Voc\u00ea nao possui permiss\u00f5es para isso.");
                    return true;
                }
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("§5§lFANTASY §8\u279c§a Iniciando uma varredura geral...");
                Bukkit.broadcastMessage(" ");
                final Runtime r2 = Runtime.getRuntime();
                final long Lused2 = (r2.totalMemory() - r2.freeMemory()) / 5120L / 5120L;
                System.gc();
                final long Lused3 = (r2.totalMemory() - r2.freeMemory()) / 5120L / 5120L;
                for (final Player s : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("fantasy.clear")) {
                        s.sendMessage("§5§lFANTASY §8\u279c§a Limpos §f" + Long.toString(Lused2 - Lused3) + "M §7De RAM§a do servidor.");
                    }
                }
                Bukkit.broadcastMessage("§5§lFANTASY §8\u279c§e A Varredura foi finalizada com sucesso.");
                return false;
            }
            else {
                if (label.equalsIgnoreCase("setwarp")) {
                    if (!(p instanceof Player)) {
                        p.sendMessage(Manager.CONSOLE);
                        return true;
                    }
                    if (!p.hasPermission("fantasy.setwarp")) {
                        p.sendMessage(Manager.SEMPERM);
                        return true;
                    }
                    if (args.length == 0) {
                        sender.sendMessage(String.valueOf(Manager.use) + "setwarp <ArenaMaxx,ArenaMinx,ArenaMaxz,ArenaMinz,Fps,Lava,Main,Potion,1v1,PosP1,PosP2,Feast>].");
                        return true;
                    }
                    if (args.length > 1) {
                        sender.sendMessage(String.valueOf(Manager.use) + "setwarp <ArenaMaxx,ArenaMinx,ArenaMaxz,ArenaMinz,Fps,Lava,Main,Potion,1v1,PosP1,PosP2,Feast>].");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("feast")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Feast", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aFeast Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("fps")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Fps", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aFps Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("lava")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Lava", (Object)Manager.serializeLocation(p.getLocation()));
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aLava Setado Com Sucesso.");
                        ConfigAPI.pegarConfig().salvarCords();
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("main")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Main", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aMain Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("1v1")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.1v1", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §a1v1 Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("potion")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Potion", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aPotion Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("mdr")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.Mdr", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aMdr Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("PosP1")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.1v1_Position1", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §a1v1 PosP1 Setado Com Sucesso.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("PosP2")) {
                        ConfigAPI.pegarConfig().pegarconfigCords().set("Coordenadas.1v1_Position2", (Object)Manager.serializeLocation(p.getLocation()));
                        ConfigAPI.pegarConfig().salvarCords();
                        p.sendMessage(String.valueOf(Manager.prefix) + " §a1v1 PosP2 Setado Com Sucesso.");
                        return true;
                    }
                    sender.sendMessage(String.valueOf(Manager.use) + "setwarp <ArenaMaxx,ArenaMinx,ArenaMaxz,ArenaMinz,Fps,Lava,Main,Potion,1v1,PosP1,PosP2,Feast>].");
                }
                if (label.equalsIgnoreCase("status")) {
                    if (args.length == 0) {
                        Inventarios.inventarioestatisticas(p);
                        return true;
                    }
                    if (args.length == 1) {
                        final Player target = Bukkit.getPlayerExact(args[0]);
                        if (target != null) {
                            Inventarios.inventarioestatisticassecond(p, target.getUniqueId(), target.getName());
                            return true;
                        }
                        p.sendMessage(Manager.JOGADORFF);
                        return true;
                    }
                }
                if (label.equalsIgnoreCase("chat")) {
                    if (p.hasPermission("fantasy.chat")) {
                        if (args.length == 0) {
                            p.sendMessage(String.valueOf(Manager.use) + "chat <on,off,clear>.");
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("on")) {
                            if (!Manager.Chat) {
                                Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aChat Ativado!");
                                Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7ativou o chat]", "veravisos", false);
                                Manager.Chat = true;
                            }
                            else {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas O Chat Ja Esta Ativado.");
                            }
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("off")) {
                            if (Manager.Chat) {
                                Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §cChat Desativado!");
                                Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7desativou o chat]", "veravisos", false);
                                Manager.Chat = false;
                            }
                            else {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas O Chat Ja Esta Desativado.");
                            }
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("clear")) {
                            for (int x = 1; x < 100; ++x) {
                                Manager.mandarMensagem("");
                            }
                            Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aChat Limpo!");
                            Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7limpou chat]", "veravisos", false);
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.use) + "chat <on,off,clear>.");
                    }
                    else {
                        p.sendMessage(Manager.SEMPERM);
                    }
                }
                if (label.equalsIgnoreCase("bc")) {
                    if (p.hasPermission("fantasy.bc")) {
                        if (args.length >= 1) {
                            String bcast = "";
                            for (int x2 = 0; x2 < args.length; ++x2) {
                                bcast = String.valueOf(bcast) + args[x2] + " ";
                            }
                            bcast = ChatColor.translateAlternateColorCodes('&', bcast);
                            Manager.mandarMensagem("");
                            Manager.mandarMensagem(String.valueOf(Manager.prefix) + " " + bcast.replace("&", "§"));
                            Manager.mandarMensagem("");
                        }
                        else {
                            p.sendMessage(String.valueOf(Manager.use) + "bc <mensagem...>");
                        }
                    }
                    else {
                        p.sendMessage(Manager.SEMPERM);
                    }
                }
                if (label.equalsIgnoreCase("youtuber")) {
                    for (final String req : ConfigAPI.pegarConfig().pegarconfigMensagens().getStringList("Requisitos")) {
                        p.sendMessage(req);
                    }
                }
                if (label.equalsIgnoreCase("report")) {
                    if (args.length == 0) {
                        p.sendMessage(String.valueOf(Manager.use) + "report <jogador> <motivo>.");
                        return true;
                    }
                    if (args.length < 2) {
                        p.sendMessage(String.valueOf(Manager.use) + "report <jogador> <motivo>.");
                        return true;
                    }
                    if (!Manager.CooldownReport.contains(p)) {
                        final Player jogadorencontrado = Bukkit.getPlayer(args[0]);
                        final StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; ++i) {
                            sb.append(args[i]).append(" ");
                        }
                        final String Motivo = sb.toString().trim();
                        if (jogadorencontrado != null) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aDenuncia Envianda Com Sucesso§a.");
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Entrou Em Um Cooldown De §f10 Segundos§a Para Usar O Report Novamente.");
                            Manager.mandarMensagemComPermissao("", "veravisos", true);
                            Manager.mandarMensagemComPermissao("§c§m---(-)--------------------(-)---", "veravisos", true);
                            Manager.mandarMensagemComPermissao("   §a§lNOVA DENUNCIA§7 ", "veravisos", true);
                            Manager.mandarMensagemComPermissao("", "veravisos", true);
                            Manager.mandarMensagemComPermissao("§7» Suspeito: §a" + jogadorencontrado.getName(), "veravisos", true);
                            Manager.mandarMensagemComPermissao("§7» Vitima: §e" + p.getName(), "veravisos", true);
                            Manager.mandarMensagemComPermissao("§7» Motivo: §e" + Motivo, "veravisos", true);
                            Manager.mandarMensagemComPermissao("§c§m---(-)--------------------(-)---", "veravisos", true);
                            Manager.CooldownReport.add(p);
                            new BukkitRunnable() {
                                public void run() {
                                    if (p.isOnline()) {
                                        Manager.CooldownReport.remove(p);
                                    }
                                }
                            }.runTaskLater(FantasyKits.getPlugin(), 200L);
                        }
                        else {
                            p.sendMessage(Manager.JOGADORFF);
                        }
                    }
                    else {
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cAguarde Para Reportar Alguem Novamente.");
                        p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 2.0f);
                    }
                }
                if (label.equalsIgnoreCase("construir")) {
                    if (p.hasPermission("fantasy.construir")) {
                        if (Manager.construir.contains(p)) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cAgora Voce Nao Pode Mas Construir.");
                            p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                            Manager.construir.remove(p);
                        }
                        else if (!Manager.construir.contains(p)) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aAgora Voce Pode Construir.");
                            p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                            Manager.construir.add(p);
                        }
                    }
                    else {
                        p.sendMessage(Manager.SEMPERM);
                    }
                }
                if (label.equalsIgnoreCase("toggle")) {
                    Inventarios.inventariotoggle(p, MenuPaged.Type.CAIXAS);
                }
                if (cmd.getName().equalsIgnoreCase("tag")) {
                    if (args.length == 0) {
                        if (p.hasPermission("tag.*")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DONO,DEV,COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.dev")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DONO,DEV,COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.coord")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.admin")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.modplus")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.youtuberplus")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <EMERALD,DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DIAMOND,GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <GOLD,NORMAL>");
                            return true;
                        }
                        if (p.hasPermission("tag.normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <NORMAL>");
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.*")) {
                        if (args[0].toLowerCase().equals("dono")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DONO);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("dev")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DEV);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("coord")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.COORD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("admin")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.ADMIN);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MODPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DONO,DEV,COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.dev")) {
                        if (args[0].toLowerCase().equals("dev")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DEV);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("coord")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.COORD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("admin")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.ADMIN);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MODPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DEV,COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.coord")) {
                        if (args[0].toLowerCase().equals("coord")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.COORD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("admin")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.ADMIN);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MODPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <COORD,ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.admin")) {
                        if (args[0].toLowerCase().equals("admin")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.ADMIN);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MODPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <ADMIN,MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.modplus")) {
                        if (args[0].toLowerCase().equals("mod+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MODPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <MOD+,MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.mod")) {
                        if (args[0].toLowerCase().equals("mod")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.MOD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <MOD,YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.youtuberplus")) {
                        if (args[0].toLowerCase().equals("youtuber+")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERPLUS);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBER+,TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.trial")) {
                        if (args[0].toLowerCase().equals("trial")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.TRIAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <TRIAL,AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.ajudante")) {
                        if (args[0].toLowerCase().equals("ajudante")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.AJUDANTE);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <AJUDANTE,BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.builder")) {
                        if (args[0].toLowerCase().equals("builder")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.BUILDER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <BUILDER,YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.youtuber")) {
                        if (args[0].toLowerCase().equals("youtuber")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBER);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBER,YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.youtubermirim")) {
                        if (args[0].toLowerCase().equals("youtubermirim")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.YOUTUBERMIRIM);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <YOUTUBERMIRIM,EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.emerald")) {
                        if (args[0].toLowerCase().equals("emerald")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.EMERALD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <EMERALD,DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.diamond")) {
                        if (args[0].toLowerCase().equals("diamond")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.DIAMOND);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <DIAMOND,GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0 && p.hasPermission("tag.gold")) {
                        if (args[0].toLowerCase().equals("gold")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.GOLD);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <GOLD,NORMAL>");
                        return true;
                    }
                    else if (args.length > 0) {
                        if (args[0].toLowerCase().equals("normal")) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea agora esta com a tag: §f" + args[0].toUpperCase());
                            Manager.setarTag(p, Manager.Tags.NORMAL);
                            p.setDisplayName(String.valueOf(Manager.pegarTagDisplay(p)) + "§7" + p.getName());
                            return true;
                        }
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tag <NORMAL>");
                        return true;
                    }
                }
                if (label.equalsIgnoreCase("spawn")) {
                    if (!Manager.comKit(p)) {
                        for (int msg = 1; msg < 100; ++msg) {
                            p.sendMessage("");
                        }
                        p.teleport(p.getWorld().getSpawnLocation());
                        for (final PotionEffect effect : p.getActivePotionEffects()) {
                            p.removePotionEffect(effect.getType());
                        }
                        Manager.removerArrays(p);
                        Manager.darItens(p);
                        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    }
                    if (Manager.comKit(p)) {
                        final Location below = p.getLocation().subtract(0.0, 1.0, 0.0);
                        final Block blockBelow = below.getBlock();
                        if (blockBelow.getType() == Material.AIR) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea N\u00c3O Pode Teleportar Para O Spawn Enquanto Estiver No Ar.");
                            return true;
                        }
                        Boolean encontrou = false;
                        final List<?> entidades2 = (List<?>)p.getNearbyEntities(5.0, 5.0, 5.0);
                        for (final Object entidadeEncontrada : entidades2) {
                            if (entidadeEncontrada instanceof Player && entidadeEncontrada != null) {
                                encontrou = true;
                            }
                        }
                        if (encontrou) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cExiste Jogadores Perto De Voce.");
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cAguarde 5 Segundos Para Teleportar-se,Nao Se Mova.");
                            Manager.teleportnaomover.add(p);
                            new BukkitRunnable() {
                                public void run() {
                                    if (Manager.teleportnaomover.contains(p)) {
                                        for (int msg = 1; msg < 100; ++msg) {
                                            p.sendMessage("");
                                        }
                                        p.teleport(p.getWorld().getSpawnLocation());
                                        for (final PotionEffect effect : p.getActivePotionEffects()) {
                                            p.removePotionEffect(effect.getType());
                                        }
                                        Manager.teleportnaomover.remove(p);
                                        Manager.removerArrays(p);
                                        Manager.darItens(p);
                                        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                                    }
                                    else {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce Se Moveu E O Teleporte Foi Cancelado.");
                                        Manager.teleportnaomover.remove(p);
                                    }
                                }
                            }.runTaskLater(FantasyKits.getPlugin(), 100L);
                        }
                        if (!encontrou) {
                            if (blockBelow.getType() == Material.AIR) {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea N\u00c3o Pode Teleportar Para O Spawn Enquanto Estiver No Ar.");
                                return true;
                            }
                            for (int msg2 = 1; msg2 < 100; ++msg2) {
                                p.sendMessage("");
                            }
                            p.teleport(p.getWorld().getSpawnLocation());
                            for (final PotionEffect effect2 : p.getActivePotionEffects()) {
                                p.removePotionEffect(effect2.getType());
                            }
                            Manager.removerArrays(p);
                            Manager.darItens(p);
                            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                        }
                    }
                }
                if (label.equalsIgnoreCase("inv")) {
                    if (!p.hasPermission("fantasy.inv")) {
                        p.sendMessage(Manager.SEMPERM);
                        return true;
                    }
                    if (args.length == 0) {
                        p.sendMessage(String.valueOf(Manager.use) + "inv <jogador>.");
                        return true;
                    }
                    final Player jogadorencontrado = Bukkit.getPlayer(args[0]);
                    if (jogadorencontrado != null) {
                        if (jogadorencontrado != p) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Esta Vendo O Inventario De §f" + jogadorencontrado.getName() + "§a.");
                            Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7esta vendo o inventario de " + jogadorencontrado.getName() + "§7]", "veravisos", false);
                            p.openInventory((Inventory)jogadorencontrado.getInventory());
                        }
                        else {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce N\u00c3O Pode Ver Seu Proprio Inventario.");
                        }
                    }
                    else {
                        p.sendMessage(Manager.JOGADORFF);
                    }
                }
                if (cmd.getName().equalsIgnoreCase("tell")) {
                    if (args.length == 0) {
                        p.sendMessage(String.valueOf(Manager.use) + "tell <jogador> <mensagem>");
                    }
                    else if (args.length == 1) {
                        p.sendMessage(String.valueOf(Manager.use) + "tell <jogador> <mensagem>");
                    }
                    else if (args.length > 1) {
                        String message = "";
                        for (int j = 1; j < args.length; ++j) {
                            message = String.valueOf(message) + args[j] + " ";
                        }
                        final Player targetPlayer = p.getServer().getPlayer(args[0]);
                        if (targetPlayer == null) {
                            p.sendMessage(Manager.JOGADORFF);
                        }
                        else if (Manager.comTellAtivado(targetPlayer)) {
                            targetPlayer.sendMessage(String.valueOf(Manager.prefix) + " §aMensagem De §f" + p.getName() + " §8\ufffdª§a " + message + "§8\ufffd«");
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aMensagem Enviada Para §f" + targetPlayer.getName() + " §8\ufffdª§a " + message + "§8\ufffd«");
                        }
                        else {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Este Jogador Esta Com O Tell Desativado.");
                        }
                    }
                }
                if (!label.equalsIgnoreCase("gm") && !label.equalsIgnoreCase("gamemode")) {
                    if (cmd.getName().equalsIgnoreCase("fly")) {
                        final Player player = (Player)sender;
                        if (args.length == 0) {
                            if (!sender.hasPermission("fantasy.fly")) {
                                p.sendMessage(Manager.SEMPERM);
                                return true;
                            }
                            if (!Manager.pegarKit(player).equalsIgnoreCase("none")) {
                                player.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas O Fly So Pode Ser Usado No Spawn.");
                                return true;
                            }
                            if (!player.getAllowFlight()) {
                                player.sendMessage(String.valueOf(Manager.prefix) + " §aSeu §fFly §aFoi Habilitado.");
                                player.setAllowFlight(true);
                            }
                            else {
                                player.sendMessage(String.valueOf(Manager.prefix) + " §cSeu §fFly §cFoi Desabilitado.");
                                player.setAllowFlight(false);
                            }
                        }
                        if (args.length == 1) {
                            if (!sender.hasPermission("fantasy.darfly")) {
                                p.sendMessage(Manager.SEMPERM);
                                return true;
                            }
                            final Player player2 = Bukkit.getPlayer(args[0]);
                            if (player2 == null) {
                                sender.sendMessage(Manager.JOGADORFF);
                                return true;
                            }
                            if (!Manager.pegarKit(player2).equalsIgnoreCase("none")) {
                                player.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas O Fly So Pode Ser Usado No Spawn.");
                                return true;
                            }
                            if (!player.getAllowFlight()) {
                                player.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Habilitou O §fFly §aDe §f" + player2.getName() + "§a.");
                                player2.setAllowFlight(true);
                                player2.sendMessage(String.valueOf(Manager.prefix) + " §aSeu §fFly §aFoi Habilitado Por §f" + player.getName() + "§a.");
                            }
                            else {
                                sender.sendMessage(String.valueOf(Manager.prefix) + " §cVoce Desabilitou O §fFly §cDe §f" + player2.getName() + "§a.");
                                player2.setAllowFlight(false);
                                player2.sendMessage(String.valueOf(Manager.prefix) + " §cSeu §fFly §cFoi Desabilitado Por §f" + player.getName() + "§c.");
                            }
                        }
                    }
                    if (cmd.getName().equalsIgnoreCase("dinheiro")) {
                        final PlayerData pd = PlayerData.get(p);
                        if (pd == null) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §CSua conta n\u00c3O esta registrada, relogue para resolver.");
                            return true;
                        }
                        if (args.length == 0) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Possui §f" + Manager.modificarCoins(pd.getMoney()) + "$ §aNa Sua Conta.");
                            return true;
                        }
                        if (args.length == 1) {
                            final Player target2 = Bukkit.getPlayerExact(args[0]);
                            if (target2 == null) {
                                p.sendMessage(Manager.JOGADORFF);
                                return true;
                            }
                            final PlayerData pdtarget = PlayerData.get(target2);
                            if (pdtarget == null) {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §CEste player n\u00c3O tem uma conta registrada.");
                                return true;
                            }
                            p.sendMessage(String.valueOf(Manager.prefix) + " §f" + target2.getName() + "§a Possui §f" + Manager.modificarCoins(pdtarget.getMoney()) + "$ §aNa Sua Conta.");
                            return true;
                        }
                        else {
                            if (args.length == 2) {
                                if (!p.hasPermission("fantasy.coinsadd") || !p.hasPermission("fantasy.coinsset")) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <jogador> ou /dinherio <doar> <jogador> <valor>.");
                                    return true;
                                }
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <setar,add> <jogador> <valor>.");
                            }
                            if (args.length > 3) {
                                if (!p.hasPermission("fantasy.coinsadd") || !p.hasPermission("fantasy.coinsset")) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <jogador> ou /dinherio <doar> <jogador> <valor>.");
                                    return true;
                                }
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <setar,add> <jogador> <valor>.");
                            }
                            if (args.length == 3) {
                                if (args[0].equals("setar")) {
                                    if (!p.hasPermission("fantasy.coinsset")) {
                                        p.sendMessage(Manager.SEMPERM);
                                        return true;
                                    }
                                    final Player target2 = Bukkit.getPlayerExact(args[1]);
                                    if (target2 == null) {
                                        p.sendMessage(Manager.JOGADORFF);
                                        return true;
                                    }
                                    try {
                                        final Integer numero = Integer.valueOf(args[2]);
                                        final PlayerData pdtarget2 = PlayerData.get(target2);
                                        if (pdtarget2 == null) {
                                            p.sendMessage(String.valueOf(Manager.prefix) + " §CEste player n\u00c3O tem uma conta registrada.");
                                            return true;
                                        }
                                        pdtarget2.setMoney(numero);
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Setou O Saldo De §f" + target2.getName() + " §aPara §f" + Manager.modificarCoins(numero) + "$§a.");
                                        target2.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + " §aSetou Seu Saldo Para §f" + Manager.modificarCoins(numero) + "$§a.");
                                    }
                                    catch (Exception e) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                    }
                                    return true;
                                }
                                else if (args[0].equals("doar")) {
                                    final Player target2 = Bukkit.getPlayerExact(args[1]);
                                    if (target2 == null) {
                                        p.sendMessage(Manager.JOGADORFF);
                                        return true;
                                    }
                                    if (target2 == p) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Doar Para Si Mesmo.");
                                        return true;
                                    }
                                    final PlayerData pdtarget = PlayerData.get(target2);
                                    if (pdtarget == null) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §CEste player n\u00c3O tem uma conta registrada.");
                                        return true;
                                    }
                                    try {
                                        final Integer numero2 = Integer.valueOf(args[2]);
                                        if (pd.getMoney() >= numero2) {
                                            pdtarget.addMoney(numero2);
                                            pd.removeMoney(numero2);
                                            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Doou Para §f" + target2.getName() + " §aUm Valor De§f" + Manager.modificarCoins(numero2) + "$§a.");
                                            target2.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + " §aVoce Doou Para Voce §f" + Manager.modificarCoins(numero2) + "$§a.");
                                        }
                                        else {
                                            p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voc\u00ea N\u00c3O Possui §f" + Manager.modificarCoins(numero2) + "$§c.");
                                        }
                                    }
                                    catch (Exception e2) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                    }
                                    return true;
                                }
                                else if (args[0].equals("add")) {
                                    if (!p.hasPermission("fantasy.coinsadd")) {
                                        p.sendMessage(Manager.SEMPERM);
                                        return true;
                                    }
                                    final Player target2 = Bukkit.getPlayerExact(args[1]);
                                    if (target2 == null) {
                                        p.sendMessage(Manager.JOGADORFF);
                                        return true;
                                    }
                                    final PlayerData pdtarget = PlayerData.get(target2);
                                    if (pdtarget == null) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §CEste player n\u00c3O tem uma conta registrada.");
                                        return true;
                                    }
                                    try {
                                        final Integer numero2 = Integer.valueOf(args[2]);
                                        if (pdtarget.getMoney() + numero2 >= 200000000) {
                                            p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Adicionar Mas Dinheiro.");
                                            return true;
                                        }
                                        pdtarget.addMoney(numero2);
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Adicionou §f" + Manager.modificarCoins(numero2) + "$ §aNa Conta De §f" + target2.getName() + "§a.");
                                        target2.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + "§a Adicionou §f" + Manager.modificarCoins(numero2) + "$ §aNa Sua Conta§a.");
                                    }
                                    catch (Exception e2) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                    }
                                    return true;
                                }
                                else {
                                    if (!p.hasPermission("fantasy.coinsadd") || !p.hasPermission("fantasy.coinsset")) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <jogador> ou /dinherio <doar> <jogador> <valor>.");
                                        return true;
                                    }
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /dinheiro <setar,add> <jogador> <valor>.");
                                }
                            }
                        }
                    }
                    if (cmd.getName().equalsIgnoreCase("caixas")) {
                        if (!p.hasPermission("fantasy.caixasset")) {
                            p.sendMessage(Manager.SEMPERM);
                            return true;
                        }
                        if (args.length == 0) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /caixas <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 1) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /caixas <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 2) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /caixas <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length > 3) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /caixas <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 3) {
                            if (args[0].equals("setar")) {
                                final Player target = Bukkit.getPlayerExact(args[1]);
                                if (target == null) {
                                    p.sendMessage(Manager.JOGADORFF);
                                    return true;
                                }
                                try {
                                    final Integer numero3 = Integer.valueOf(args[2]);
                                    final int numerototal = Manager.pegarChaves(p) + Manager.pegarCaixas(p);
                                    if (numerototal + numero3 > 28 && numero3 != 0) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Setar §f" + numero3 + " §cCaixas.");
                                        return true;
                                    }
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Setou O Numero De Caixas De §f" + target.getName() + " §aPara §f" + Manager.modificarCoins(numero3) + " Caixas§a.");
                                    target.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + " §aSetou Seu Numero De Caixas Para §f" + Manager.modificarCoins(numero3) + " Caixas§a.");
                                }
                                catch (Exception e3) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                }
                                return true;
                            }
                            else if (args[0].equals("add")) {
                                final Player target = Bukkit.getPlayerExact(args[1]);
                                if (target == null) {
                                    p.sendMessage(Manager.JOGADORFF);
                                    return true;
                                }
                                try {
                                    final Integer numero3 = Integer.valueOf(args[2]);
                                    final int numerototal = Manager.pegarChaves(p) + Manager.pegarCaixas(p);
                                    if (numerototal + numero3 > 28 && numero3 != 0) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Adicionar Mas §f" + numero3 + " §cCaixas.");
                                        return true;
                                    }
                                    Manager.adicionarCaixas(target, numero3);
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Adicionou §f" + Manager.modificarCoins(numero3) + " Caixas §aNa Conta De §f" + target.getName() + "§a.");
                                    target.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + "§a Adicionou §f" + Manager.modificarCoins(numero3) + " Caixas §aNa Sua Conta§a.");
                                }
                                catch (Exception e3) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                }
                                return true;
                            }
                            else {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /caixas <setar,add> <jogador> <valor>.");
                            }
                        }
                    }
                    if (cmd.getName().equalsIgnoreCase("chaves")) {
                        if (!p.hasPermission("fantasy.chavesset")) {
                            p.sendMessage(Manager.SEMPERM);
                            return true;
                        }
                        if (args.length == 0) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /chaves <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 1) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /chaves <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 2) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /chaves <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length > 3) {
                            p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /chaves <setar,add> <jogador> <valor>.");
                            return true;
                        }
                        if (args.length == 3) {
                            if (args[0].equals("setar")) {
                                final Player target = Bukkit.getPlayerExact(args[1]);
                                if (target == null) {
                                    p.sendMessage(Manager.JOGADORFF);
                                    return true;
                                }
                                try {
                                    final Integer numero3 = Integer.valueOf(args[2]);
                                    final int numerototal = Manager.pegarCaixas(p) + Manager.pegarChaves(p);
                                    if (numerototal + numero3 > 28 && numero3 != 0) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Setar §f" + numero3 + " §cChaves.");
                                        return true;
                                    }
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce O Numero De Chaves De §f" + target.getName() + " §aPara §f" + Manager.modificarCoins(numero3) + " Chaves§a.");
                                    target.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + " §aSetou Seu Numero De Chaves Para §f" + Manager.modificarCoins(numero3) + " Chaves§a.");
                                }
                                catch (Exception e3) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                }
                                return true;
                            }
                            else if (args[0].equals("add")) {
                                final Player target = Bukkit.getPlayerExact(args[1]);
                                if (target == null) {
                                    p.sendMessage(Manager.JOGADORFF);
                                    return true;
                                }
                                try {
                                    final Integer numero3 = Integer.valueOf(args[2]);
                                    final int numerototal = Manager.pegarCaixas(p) + Manager.pegarChaves(p);
                                    if (numerototal + numero3 > 28 && numero3 != 0) {
                                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas Voce N\u00c3O Pode Adicionar Mas §f" + numero3 + " §cChaves.");
                                        return true;
                                    }
                                    Manager.adicionarChaves(target, numero3);
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Adicionou §f" + Manager.modificarCoins(numero3) + " Chaves §aNa Conta De §f" + target.getName() + "§a.");
                                    target.sendMessage(String.valueOf(Manager.prefix) + " §f" + p.getName() + "§a Adicionou §f" + Manager.modificarCoins(numero3) + " chaves §aNa Sua Conta§a.");
                                }
                                catch (Exception e3) {
                                    p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe Mas " + args[2] + " Nao Em Um Numero.");
                                }
                                return true;
                            }
                            else {
                                p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /chaves <setar,add> <jogador> <valor>.");
                            }
                        }
                    }
                    return false;
                }
                if (!p.hasPermission("fantasy.gm")) {
                    p.sendMessage(Manager.SEMPERM);
                    return true;
                }
                if (args.length != 1) {
                    if (args.length == 2) {
                        final String modo = args[0];
                        final Player target2 = Bukkit.getPlayer(args[1]);
                        if (target2 == null) {
                            sender.sendMessage(Manager.JOGADORFF);
                            return true;
                        }
                        if (modo.equalsIgnoreCase("0")) {
                            if (target2.getGameMode() == GameMode.SURVIVAL) {
                                sender.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas §f" + target2.getName() + " §cJa Esta No Modo §fSurvival§c.");
                            }
                            else {
                                target2.setGameMode(GameMode.SURVIVAL);
                                target2.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fSurvival§a.");
                                p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo De §f" + target2.getName() + " §aAlterado Para §fSurvival§a.");
                            }
                            return true;
                        }
                        if (modo.equalsIgnoreCase("1")) {
                            if (target2.getGameMode() == GameMode.CREATIVE) {
                                sender.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas §f" + target2.getName() + " §cJa Esta No Modo §fCriativo§c.");
                            }
                            else {
                                target2.setGameMode(GameMode.CREATIVE);
                                target2.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fCriativo§a.");
                                p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo De §f" + target2.getName() + " §aAlterado Para §fCriativo§a.");
                            }
                            return true;
                        }
                        if (modo.equalsIgnoreCase("2")) {
                            if (target2.getGameMode() == GameMode.ADVENTURE) {
                                sender.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas §f" + target2.getName() + " §cJa Esta No Modo §fAdventure§c.");
                            }
                            else {
                                target2.setGameMode(GameMode.ADVENTURE);
                                target2.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fAdventure§a.");
                                p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo De §f" + target2.getName() + " §aAlterado Para §fAdventure§a.");
                            }
                            return true;
                        }
                        sender.sendMessage(String.valueOf(Manager.use) + " gm <0,1,2> | <jogador> ou gamemode <0,1,2> | <jogador>");
                    }
                    else {
                        sender.sendMessage(String.valueOf(Manager.use) + " gm <0,1,2> | <jogador> ou gamemode <0,1,2> | <jogador>");
                    }
                    return true;
                }
                final String modo = args[0];
                if (modo.equalsIgnoreCase("0")) {
                    if (p.getGameMode() == GameMode.SURVIVAL) {
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce Ja Esta No Modo §fSurvival§c.");
                    }
                    else {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fSurvival§a.");
                    }
                    return true;
                }
                if (modo.equalsIgnoreCase("1")) {
                    if (p.getGameMode() == GameMode.CREATIVE) {
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce Ja Esta No Modo §fCriativo§c.");
                    }
                    else {
                        p.setGameMode(GameMode.CREATIVE);
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fCriativo§a.");
                    }
                }
                else if (modo.equalsIgnoreCase("2")) {
                    if (p.getGameMode() == GameMode.ADVENTURE) {
                        p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce Ja Esta No Modo §fAdventure§c.");
                    }
                    else {
                        p.setGameMode(GameMode.ADVENTURE);
                        p.sendMessage(String.valueOf(Manager.prefix) + " §aModo De Jogo Alterado Para §fAdventure§a.");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Manager.use) + " gm <0,1,2> | <jogador> ou gamemode <0,1,2> | <jogador>");
                }
                return true;
            }
        }
    }
}
