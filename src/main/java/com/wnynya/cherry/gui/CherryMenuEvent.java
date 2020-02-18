package com.wnynya.cherry.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CherryMenuEvent {

  public static void inventoryClick(InventoryClickEvent event) {

    String name = event.getView().getTitle();
    if (name.equals(CherryMenu.MainMenu.title)) {
      CherryMenu.MainMenu.interactMenu(event);
    }
    else if (name.equals(CherryMenu.SubMenu.CherrySetting.title)) {
      CherryMenu.SubMenu.CherrySetting.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Main.title)) {
      CherryMenu.CucumberySupport.Main.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Setting_Normal.title)) {
      CherryMenu.CucumberySupport.Setting_Normal.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Setting_Sound.title)) {
      CherryMenu.CucumberySupport.Setting_Sound.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Setting_Messaging.title)) {
      CherryMenu.CucumberySupport.Setting_Messaging.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Setting_Creative.title)) {
      CherryMenu.CucumberySupport.Setting_Creative.interactMenu(event);
    }
    else if (name.equals(CherryMenu.CucumberySupport.Setting_Admin.title)) {
      CherryMenu.CucumberySupport.Setting_Admin.interactMenu(event);
    }
    else {
      return;
    }
  }
}
