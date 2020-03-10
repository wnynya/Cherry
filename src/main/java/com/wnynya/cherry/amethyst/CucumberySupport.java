package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CucumberySupport {

  public static boolean exist() {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Cucumbery");
    return plugin != null;
  }

  public static void playerFirstJoin(Player player) {
    Config c = new Config("cucumbery");
    if (!exist()) {
      // notetools 충돌
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE") + " true");
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE") + " true");
      // etc
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING") + " true");
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_COPY_NOTE_BLOCK_PITCH " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_PITCH") + " true");
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT") + " true");
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setuserdata " + player.getName() + " TOGGLE_USE_HELPFUL_LORE_FEATURE_WHEN_CRAFTING_TABLE " + c.getConfig().getBoolean("default.setuserdata.TOGGLE_USE_HELPFUL_LORE_FEATURE_WHEN_CRAFTING_TABLE") + " true");
    }
  }

  public static void init() {
    Config c = new Config("cucumbery");
    // notetools 충돌
    // 크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE")) {
      c.set("default.setuserdata.TOGGLE_INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE", false);
    }
    // 크리에이티브-모드에서-소리-블록-클릭으로-소리-재생
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE")) {
      c.set("default.setuserdata.TOGGLE_PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE", false);
    }
    // etc
    // 웅크리기-상태에서만-소리-블록-값-복사
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING", true);
    }
    // 픽블록으로-소리-블록-음높이-복사
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_PITCH")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_PITCH", false);
    }
    // 픽블록으로-소리-블록-악기-복사
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", false);
    }
    // 제작대-창에서-아이템-설명-사용
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_USE_HELPFUL_LORE_FEATURE_WHEN_CRAFTING_TABLE")) {
      c.set("default.setuserdata.TOGGLE_USE_HELPFUL_LORE_FEATURE_WHEN_CRAFTING_TABLE", true);
    }
    /*
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }
    if (!c.getConfig().isBoolean("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT")) {
      c.set("default.setuserdata.TOGGLE_COPY_NOTE_BLOCK_INSTRUMENT", "false");
    }*/
  }

}
