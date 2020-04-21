package com.whitehouse.ascmd;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class asCMD extends JavaPlugin {
   public final ArmorStand entityListener = new ArmorStand(this);
   public final Prefix prefix = new Prefix(this);
   private final HashMap<Player, String> ascmdinhandmethod = new HashMap();
   private final HashMap<Player, String> ascmdinhand = new HashMap();
   private final HashMap<Player, Boolean> ascmdisediting = new HashMap();
   public final ConcurrentHashMap<Player, String> sendMsgtoPl = new ConcurrentHashMap();
   private final HashMap<Player, Long> ascmdlastused = new HashMap();
   public String ver;

   public void onDisable() {
      this.getLogger().info("Disabling asCMD!");
   }

   public void onEnable() {
      PluginManager var1 = this.getServer().getPluginManager();
      var1.registerEvents(this.entityListener, this);
      this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
      ArmorStandCmd var2 = new ArmorStandCmd(this);
      this.getCommand("ascmd").setExecutor(var2);
      this.getCommand("ascommand").setExecutor(var2);
      this.getCommand("armorstandcmd").setExecutor(var2);
      this.getCommand("armorstandcommand").setExecutor(var2);
      PluginDescriptionFile var3 = this.getDescription();
      this.getLogger().info(var3.getName() + " version " + var3.getVersion() + " is enabled!");
      this.ver = var3.getVersion();
      if (this.getConfig().getString("format-version", "1").equals("1")) {
         this.getLogger().info(ChatColor.GREEN + "* This version of asCMD uses better config.yml, beginning conversion from older format *");
         Set var4 = this.getConfig().getKeys(true);
         Object[] var5 = var4.toArray();
         HashMap var6 = new HashMap();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            String var8 = var5[var7].toString();
            if (var8.contains(".commands")) {
               this.getLogger().info(ChatColor.BLUE + var8);
               var6.put(var8, this.getConfig().getStringList(var8));
            }
         }

         Iterator var10 = var6.entrySet().iterator();

         Entry var12;
         while(var10.hasNext()) {
            var12 = (Entry)var10.next();
            this.getLogger().info(ChatColor.GREEN + "armor_stands." + (String)var12.getKey() + " " + ChatColor.GOLD + ((List)var12.getValue()).toString());
         }

         this.getLogger().info("PWD=" + System.getProperty("user.dir"));

         try {
            Formatter var11 = new Formatter("plugins/asCMD/config.yml");
            var11.format("%s", "");
            var11.close();
         } catch (Exception var9) {
            this.getLogger().info(ChatColor.RED + "Error: " + var9.getMessage());
            var9.printStackTrace();
         }

         this.reloadConfig();
         this.getConfig().set("format-version", "2");
         this.getConfig().set("config.msgToConsole", true);
         this.getConfig().set("config.enableLeftClick", false);
         var10 = var6.entrySet().iterator();

         while(var10.hasNext()) {
            var12 = (Entry)var10.next();
            this.getConfig().set("armor_stands." + (String)var12.getKey(), var12.getValue());
         }

         this.saveConfig();
      }

      if (!this.getConfig().contains("config.cooldownMs")) {
         this.getConfig().set("config.cooldownMs", 100);
         this.saveConfig();
      }

   }

   public boolean ascmdHasInHand(Player var1) {
      return this.ascmdinhand.containsKey(var1) && this.ascmdinhand.get(var1) != "";
   }

   public String ascmdGetInHand(Player var1) {
      return this.ascmdinhand.containsKey(var1) ? (String)this.ascmdinhandmethod.get(var1) + " " + (String)this.ascmdinhand.get(var1) : "";
   }

   public void ascmdSetInHand(Player var1, String var2, String var3) {
      this.ascmdinhandmethod.put(var1, var2);
      this.ascmdinhand.put(var1, var3);
   }

   public void ascmdDelInHand(Player var1) {
      this.ascmdinhandmethod.put(var1, "");
      this.ascmdinhand.put(var1, "");
   }

   public boolean ascmdIsEditing(Player var1) {
      return this.ascmdisediting.containsKey(var1) ? (Boolean)this.ascmdisediting.get(var1) : false;
   }

   public void ascmdSetEditing(Player var1, boolean var2) {
      this.ascmdisediting.put(var1, var2);
   }

   public boolean checkCooldown(Player var1) {
      long var2 = System.currentTimeMillis();
      if (this.ascmdlastused.containsKey(var1)) {
         long var4 = this.getConfig().getLong("config.cooldownMs", 100L);
         long var6 = (Long)this.ascmdlastused.get(var1);
         if (var2 - var6 < var4) {
            return true;
         }
      }

      this.ascmdlastused.put(var1, var2);
      return false;
   }
}
