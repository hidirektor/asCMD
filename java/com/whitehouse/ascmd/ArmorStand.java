package com.whitehouse.ascmd;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.help.HelpTopic;

public class ArmorStand implements Listener {
   private final asCMD plugin;

   public ArmorStand(asCMD var1) {
      this.plugin = var1;
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent var1) {
      Entity var2 = var1.getRightClicked();
      Player var3 = var1.getPlayer();
      String var4 = this.plugin.prefix.getPrefix();
      if (var2.getType() == EntityType.valueOf("ARMOR_STAND")) {
         Location var5 = var2.getLocation();
         int var6 = (int)var5.getX();
         int var7 = (int)var5.getY();
         int var8 = (int)var5.getZ();
         String[] var10;
         String var20;
         if (this.plugin.ascmdHasInHand(var3)) {
            String var19 = this.plugin.ascmdGetInHand(var3);
            var10 = var19.split("\\s+");
            var20 = var10[0];
            if (var20.equals("clear")) {
               var3.sendMessage(var4 + "All commands cleared!");
               this.plugin.getConfig().set("armor_stands.loc(" + var6 + "," + var7 + "," + var8 + ").commands", (Object)null);
               var1.setCancelled(true);
            } else if (var20.equals("info")) {
               List var21 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var6 + "," + var7 + "," + var8 + ").commands");
               String[] var23 = (String[])var21.toArray(new String[var21.size()]);
               if (var23.length < 1) {
                  var3.sendMessage(var4 + ChatColor.RED + "There are none commands on this armor stand!");
                  this.plugin.ascmdDelInHand(var3);
                  return;
               }

               var3.sendMessage(var4 + "Commands on this armor stand:");
               var1.setCancelled(true);

               for(int var26 = 0; var26 < var23.length; ++var26) {
                  String[] var29 = var23[var26].split("\\s+");
                  String var30 = var29[0];
                  String var32 = "";

                  for(int var33 = 1; var33 < var29.length; ++var33) {
                     if (!var29[var33].equals("")) {
                        var32 = var32 + var29[var33];
                        if (var33 + 1 < var29.length) {
                           var32 = var32 + " ";
                        }
                     }
                  }

                  var3.sendMessage("- " + ChatColor.GREEN + var30 + " " + ChatColor.BLUE + var32);
               }
            } else {
               var1.setCancelled(true);
               String var22 = "";

               for(int var24 = 1; var24 < var10.length; ++var24) {
                  var22 = var22 + var10[var24] + " ";
               }

               List var25 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var6 + "," + var7 + "," + var8 + ").commands");
               var25.add(var20 + " " + var22);
               var3.sendMessage(var4 + "Command added!");
               this.plugin.getConfig().set("armor_stands.loc(" + var6 + "," + var7 + "," + var8 + ").commands", var25);
            }

            this.plugin.saveConfig();
            this.plugin.ascmdDelInHand(var3);
         } else if (var3.isSneaking() && var3.getItemInHand().getType() == Material.AIR && Math.round(var3.getLocation().getPitch()) == 90) {
            var1.setCancelled(true);
         } else {
            if (!this.plugin.ascmdIsEditing(var3)) {
               List var9 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var6 + "," + var7 + "," + var8 + ").commands");
               var10 = (String[])var9.toArray(new String[var9.size()]);
               if (((String)this.plugin.sendMsgtoPl.getOrDefault(var3, "")).indexOf(".") == 0 && var3.isSneaking()) {
                  var20 = ((String)this.plugin.sendMsgtoPl.get(var3)).substring(1);
                  this.plugin.sendMsgtoPl.put(var3, "");
                  this.plugin.entityListener.setPrefix(var20);
                  var1.setCancelled(true);
                  return;
               }

               if (var10.length < 1) {
                  return;
               }

               var1.setCancelled(true);
               if (this.plugin.checkCooldown(var3)) {
                  return;
               }

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  String[] var12 = var10[var11].split("\\s+");
                  String var13 = var12[0];
                  String var14 = "";

                  for(int var15 = 1; var15 < var12.length; ++var15) {
                     if (!var12[var15].equals("")) {
                        var14 = var14 + var12[var15];
                        if (var15 + 1 < var12.length) {
                           var14 = var14 + " ";
                        }
                     }
                  }

                  var14 = var14.replace("%player%", var3.getName());
                  if (this.plugin.getConfig().getBoolean("config.msgToConsole", true)) {
                     this.plugin.getLogger().info("Ran command '" + var14 + "' with method '" + var13 + "' by player '" + var3.getName() + "'");
                  }

                  if (var13.equals("console")) {
                     Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var14);
                  }

                  if (var13.equals("player")) {
                     PluginCommand var27 = Bukkit.getServer().getPluginCommand(var14);
                     if (var27 != null) {
                        Bukkit.getServer().dispatchCommand(var3, var14);
                     } else {
                        ArrayList var16 = new ArrayList();
                        Iterator var17 = Bukkit.getServer().getHelpMap().getHelpTopics().iterator();

                        while(var17.hasNext()) {
                           HelpTopic var18 = (HelpTopic)var17.next();
                           var16.add(var18.getName().split(" ")[0].toLowerCase());
                        }

                        if (var16.contains("/" + var14.split(" ")[0].toLowerCase())) {
                           Bukkit.getServer().dispatchCommand(var3, var14);
                        } else {
                           PlayerCommandPreprocessEvent var31 = new PlayerCommandPreprocessEvent(var3, "/" + var14);
                           Bukkit.getServer().getPluginManager().callEvent(var31);
                        }
                     }
                  }

                  if (var13.equals("bungee")) {
                     ByteArrayDataOutput var28 = ByteStreams.newDataOutput();
                     var28.writeUTF("Connect");
                     var28.writeUTF(var12[1]);
                     var3.sendPluginMessage(this.plugin, "BungeeCord", var28.toByteArray());
                  }
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onEntityDamageByEntity(EntityDamageByEntityEvent var1) {
      if (this.plugin.getConfig().getBoolean("config.enableLeftClick", false)) {
         if (var1.getDamager() instanceof Player) {
            Player var2 = (Player)var1.getDamager();
            EntityType var3 = var1.getEntityType();
            Entity var4 = var1.getEntity();
            String var5 = this.plugin.prefix.getPrefix();
            if (var4.getType() == EntityType.valueOf("ARMOR_STAND")) {
               Location var6 = var4.getLocation();
               int var7 = (int)var6.getX();
               int var8 = (int)var6.getY();
               int var9 = (int)var6.getZ();
               String[] var11;
               if (this.plugin.ascmdHasInHand(var2)) {
                  String var20 = this.plugin.ascmdGetInHand(var2);
                  var11 = var20.split("\\s+");
                  String var21 = var11[0];
                  if (var21.equals("clear")) {
                     var2.sendMessage(var5 + "All commands cleared!");
                     this.plugin.getConfig().set("armor_stands.loc(" + var7 + "," + var8 + "," + var9 + ").commands", (Object)null);
                     var1.setCancelled(true);
                  } else if (var21.equals("info")) {
                     List var22 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var7 + "," + var8 + "," + var9 + ").commands");
                     String[] var24 = (String[])var22.toArray(new String[var22.size()]);
                     if (var24.length < 1) {
                        var2.sendMessage(var5 + ChatColor.RED + "There are none commands on this armor stand!");
                        this.plugin.ascmdDelInHand(var2);
                        return;
                     }

                     var2.sendMessage(var5 + "Commands on this armor stand:");
                     var1.setCancelled(true);

                     for(int var27 = 0; var27 < var24.length; ++var27) {
                        String[] var30 = var24[var27].split("\\s+");
                        String var31 = var30[0];
                        String var33 = "";

                        for(int var34 = 1; var34 < var30.length; ++var34) {
                           if (!var30[var34].equals("")) {
                              var33 = var33 + var30[var34];
                              if (var34 + 1 < var30.length) {
                                 var33 = var33 + " ";
                              }
                           }
                        }

                        var2.sendMessage("- " + ChatColor.GREEN + var31 + " " + ChatColor.BLUE + var33);
                     }
                  } else {
                     var1.setCancelled(true);
                     String var23 = "";

                     for(int var25 = 1; var25 < var11.length; ++var25) {
                        var23 = var23 + var11[var25] + " ";
                     }

                     List var26 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var7 + "," + var8 + "," + var9 + ").commands");
                     var26.add(var21 + " " + var23);
                     var2.sendMessage(var5 + "Command added!");
                     this.plugin.getConfig().set("armor_stands.loc(" + var7 + "," + var8 + "," + var9 + ").commands", var26);
                  }

                  this.plugin.saveConfig();
                  this.plugin.ascmdDelInHand(var2);
               } else {
                  if (!this.plugin.ascmdIsEditing(var2)) {
                     List var10 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var7 + "," + var8 + "," + var9 + ").commands");
                     var11 = (String[])var10.toArray(new String[var10.size()]);
                     if (var11.length < 1) {
                        return;
                     }

                     var1.setCancelled(true);
                     if (this.plugin.checkCooldown(var2)) {
                        return;
                     }

                     for(int var12 = 0; var12 < var11.length; ++var12) {
                        String[] var13 = var11[var12].split("\\s+");
                        String var14 = var13[0];
                        String var15 = "";

                        for(int var16 = 1; var16 < var13.length; ++var16) {
                           if (!var13[var16].equals("")) {
                              var15 = var15 + var13[var16];
                              if (var16 + 1 < var13.length) {
                                 var15 = var15 + " ";
                              }
                           }
                        }

                        var15 = var15.replace("%player%", var2.getName());
                        if (this.plugin.getConfig().getBoolean("config.msgToConsole", true)) {
                           this.plugin.getLogger().info("Ran command '" + var15 + "' with method '" + var14 + "' by player '" + var2.getName() + "'");
                        }

                        if (var14.equals("console")) {
                           Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var15);
                        }

                        if (var14.equals("player")) {
                           PluginCommand var28 = Bukkit.getServer().getPluginCommand(var15);
                           if (var28 != null) {
                              Bukkit.getServer().dispatchCommand(var2, var15);
                           } else {
                              ArrayList var17 = new ArrayList();
                              Iterator var18 = Bukkit.getServer().getHelpMap().getHelpTopics().iterator();

                              while(var18.hasNext()) {
                                 HelpTopic var19 = (HelpTopic)var18.next();
                                 var17.add(var19.getName().split(" ")[0].toLowerCase());
                              }

                              if (var17.contains("/" + var15.split(" ")[0].toLowerCase())) {
                                 Bukkit.getServer().dispatchCommand(var2, var15);
                              } else {
                                 PlayerCommandPreprocessEvent var32 = new PlayerCommandPreprocessEvent(var2, "/" + var15);
                                 Bukkit.getServer().getPluginManager().callEvent(var32);
                              }
                           }
                        }

                        if (var14.equals("bungee")) {
                           ByteArrayDataOutput var29 = ByteStreams.newDataOutput();
                           var29.writeUTF("Connect");
                           var29.writeUTF(var13[1]);
                           var2.sendPluginMessage(this.plugin, "BungeeCord", var29.toByteArray());
                        }
                     }
                  }

               }
            }
         }
      }
   }

   @EventHandler
   public void onEntityDamageByEntityOriginal(EntityDamageByEntityEvent var1) {
      if (!this.plugin.getConfig().getBoolean("config.enableLeftClick", false)) {
         if (var1.getDamager() instanceof Player) {
            Player var2 = (Player)var1.getDamager();
            if (this.plugin.ascmdIsEditing(var2)) {
               return;
            }
         }

         EntityType var9 = var1.getEntityType();
         if (var9 == EntityType.valueOf("ARMOR_STAND")) {
            Location var3 = var1.getEntity().getLocation();
            int var4 = (int)var3.getX();
            int var5 = (int)var3.getY();
            int var6 = (int)var3.getZ();
            List var7 = this.plugin.getConfig().getStringList("armor_stands.loc(" + var4 + "," + var5 + "," + var6 + ").commands");
            String[] var8 = (String[])var7.toArray(new String[var7.size()]);
            if (var8.length < 1) {
               return;
            }

            var1.setCancelled(true);
         }

      }
   }

   public void setPrefix(String var1) {
      Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var1);
   }
}
