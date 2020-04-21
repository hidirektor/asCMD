package com.whitehouse.ascmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class SimpleThread extends Thread {
   public Player pl;
   public String ver;
   public asCMD plugin;
   boolean fromCommand;

   public SimpleThread(String var1, Player var2, asCMD var3, boolean var4) {
      super(var1);
      this.pl = var2;
      this.ver = var3.ver;
      this.plugin = var3;
      this.fromCommand = var4;
   }

   public void run() {
      try {
         Thread.sleep(1000L);
         URL var1 = new URL("http://zombieapocalypse.eu/app/ascmdinfo.txt/?p=" + Bukkit.getPort() + (this.fromCommand ? "&c=m" : ""));
         BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.openStream()));

         String var3;
         while((var3 = var2.readLine()) != null) {
            String[] var4 = var3.split("\\|");
            if (!var4[0].equals(this.ver)) {
               if (!this.fromCommand) {
                  this.plugin.sendMsgtoPl.put(this.pl, "New version " + var4[0] + " of &aasCMD &ravailable! Check it at https://www.spigotmc.org/resources/ascmd-armor-stand-command-great-for-lobby-servers.41127/ " + var4[1]);
               }

               if (this.fromCommand && var4.length == 3) {
                  this.plugin.sendMsgtoPl.put(this.pl, "." + var4[2]);
               }
            }
         }

         var2.close();
      } catch (Exception var5) {
         this.plugin.sendMsgtoPl.put(this.pl, "&cAn error occured whilst trying to check for a new version! Check it manually at https://www.spigotmc.org/resources/ascmd-armor-stand-command-great-for-lobby-servers.41127/ (The warning in console can be ignored)");
         var5.printStackTrace();
      }

   }
}
