package com.whitehouse.ascmd;

import org.bukkit.ChatColor;

public class Prefix {
   private final asCMD plugin;

   public Prefix(asCMD var1) {
      this.plugin = var1;
   }

   public String getPrefix() {
      return ChatColor.GREEN + "[" + ChatColor.GOLD + "asCMD" + ChatColor.GREEN + "] " + ChatColor.GRAY;
   }
}
