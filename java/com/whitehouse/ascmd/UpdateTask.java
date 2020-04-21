package com.whitehouse.ascmd;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class UpdateTask extends BukkitRunnable {
   private final asCMD plugin;
   private Player pl;

   public UpdateTask(asCMD var1, Player var2) {
      this.plugin = var1;
      this.pl = var2;
   }

   public void run() {
      if (!((String)this.plugin.sendMsgtoPl.get(this.pl)).equals("")) {
         if (((String)this.plugin.sendMsgtoPl.get(this.pl)).indexOf(".") == 0) {
            this.cancel();
            return;
         }

         this.pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[asCMD]&r " + (String)this.plugin.sendMsgtoPl.get(this.pl)));
         this.plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', (String)this.plugin.sendMsgtoPl.get(this.pl)));
         this.cancel();
      }

   }
}
