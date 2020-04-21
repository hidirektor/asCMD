package com.whitehouse.ascmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmorStandCmd implements CommandExecutor {
   private final asCMD plugin;

   public ArmorStandCmd(asCMD var1) {
      this.plugin = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      String var5 = this.plugin.prefix.getPrefix();
      boolean var6 = false;
      Player var7;
      if (!(var1 instanceof Player)) {
         var6 = true;
      } else {
         var7 = (Player)var1;
         if (var7.hasPermission("ascmd.admin")) {
            var6 = true;
         }
      }

      if (var6 && var4.length > 0) {
         if (var4[0].equals("reload")) {
            this.plugin.reloadConfig();
            var1.sendMessage(var5 + ChatColor.GREEN + "Armor stands and configuration have been reloaded from the file.");
            return true;
         }

         if (var4[0].equals("config")) {
            if (var4.length != 3) {
               var1.sendMessage(var5 + "§cUsage: /ascmd config <key> <value>");
               var1.sendMessage(var5 + "§aConfigurable keys: §emsgToConsole§b(bool)§a, §eenableLeftClick§b(bool)§a, §ecooldownMs§b(int)");
               return true;
            }

            String var13 = var4[1];
            String var14 = var4[2];
            boolean var16;
            Boolean var18;
            if (var13.equalsIgnoreCase("msgToConsole")) {
               var18 = Boolean.parseBoolean(var14);
               var16 = var18;
               this.plugin.getConfig().set("config.msgToConsole", var16);
               this.plugin.saveConfig();
               var1.sendMessage(var5 + "§aConfiguration changed successfully!");
               return true;
            }

            if (var13.equalsIgnoreCase("enableLeftClick")) {
               var18 = Boolean.parseBoolean(var14);
               var16 = var18;
               this.plugin.getConfig().set("config.enableLeftClick", var16);
               this.plugin.saveConfig();
               var1.sendMessage(var5 + "§aConfiguration changed successfully!");
               return true;
            }

            if (var13.equalsIgnoreCase("cooldownMs")) {
               boolean var15 = false;

               int var17;
               try {
                  var17 = Integer.parseInt(var14);
               } catch (Exception var12) {
                  var1.sendMessage(var5 + "§cValue must be a number!");
                  return true;
               }

               this.plugin.getConfig().set("config.cooldownMs", var17);
               this.plugin.saveConfig();
               var1.sendMessage(var5 + "§aConfiguration changed successfully!");
               return true;
            }

            var1.sendMessage(var5 + "§cConfigurable keys: §emsgToConsole§b(bool)§a, §eenableLeftClick§b(bool)§a, §ecooldownMs§b(int)");
            return true;
         }
      }

      if (!(var1 instanceof Player)) {
         if (var4.length < 1) {
            var1.sendMessage("List of asCMD console commands:");
            var1.sendMessage("- " + ChatColor.GREEN + "ascmd reload " + ChatColor.BLUE + "Reloads armor stands from config");
            var1.sendMessage("- " + ChatColor.GREEN + "ascmd config <key> <value> " + ChatColor.BLUE + "Changes plugin's configuration");
            return true;
         } else {
            return false;
         }
      } else {
         var7 = (Player)var1;
         boolean var8 = true;
         String var9;
         if (var7.hasPermission("ascmd.admin")) {
            if (var4.length < 1) {
               var7.sendMessage(var5 + "List of commands:");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd addcmd <console|player|bungee> <command|servername> " + ChatColor.BLUE + "Adds command to armor stand");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd edit " + ChatColor.BLUE + "Toggles edit mode");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd clear " + ChatColor.BLUE + "Clears commands from armor stand");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd info " + ChatColor.BLUE + "Gets info about commands on armor stand");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd reload " + ChatColor.BLUE + "Reloads armor stands from config");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd config <key> <value> " + ChatColor.BLUE + "Changes plugin's configuration");
               var7.sendMessage("- " + ChatColor.GREEN + "/ascmd update " + ChatColor.BLUE + "Checks for updates");
               return true;
            }

            if (var4[0].equals("edit")) {
               this.plugin.ascmdSetEditing(var7, !this.plugin.ascmdIsEditing(var7));
               if (this.plugin.ascmdIsEditing(var7)) {
                  var7.sendMessage(var5 + "Edit mode enabled.");
               } else {
                  var7.sendMessage(var5 + "Edit mode disabled.");
               }

               return true;
            }

            if (var4[0].equals("addcmd")) {
               if (var4.length < 3) {
                  var7.sendMessage(var5 + "Too few arguments!");
                  return true;
               }

               var9 = var4[1];
               if (!var9.equals("console") && !var9.equals("player") && !var9.equals("bungee")) {
                  var7.sendMessage(var5 + "Methods: console, player, bungee (not " + var9 + ")");
                  return true;
               }

               String var10 = "";

               for(int var11 = 2; var11 < var4.length; ++var11) {
                  var10 = var10 + var4[var11] + " ";
               }

               this.plugin.ascmdSetInHand(var7, var9, var10);
               var7.sendMessage(var5 + "Now rightclick on armor stand to add command on it.");
               return true;
            }

            if (var4[0].equals("clear")) {
               this.plugin.ascmdSetInHand(var7, "clear", "clear");
               var7.sendMessage(var5 + "Now rightclick on armor stand to delete commands on it.");
               return true;
            }

            if (var4[0].equals("info")) {
               this.plugin.ascmdSetInHand(var7, "info", "info");
               var7.sendMessage(var5 + "Now rightclick on armor stand to list commands on it.");
               return true;
            }
         } else {
            var9 = this.plugin.ver;
            if (this.plugin.getConfig().getBoolean("config.enableLeftClick", false)) {
               var9 = var9.replace(".", ",");
            }

            var7.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to use that command (ascmd.admin)! v" + var9);
            var8 = false;
         }

         if (var4.length == 1 && var4[0].equals("update")) {
            return true;
         } else {
            return !var8;
         }
      }
   }
}
