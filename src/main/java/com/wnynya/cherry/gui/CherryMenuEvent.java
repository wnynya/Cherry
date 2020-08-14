package com.wnynya.cherry.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public class CherryMenuEvent {

  public static void inventoryClick(InventoryClickEvent event) {

    String name = event.getView().getTitle();

    switch (name) {

      case CherryMenu.MainMenu.title:
        CherryMenu.MainMenu.interactMenu(event);
        break;

      case CherryMenu.SubMenu.CherrySetting.title:
        CherryMenu.SubMenu.CherrySetting.interactMenu(event);
        break;

      case CherryMenu.SubMenu.WandSetting.title:
        CherryMenu.SubMenu.WandSetting.interactMenu(event);
        break;

      case CherryMenu.SubMenu.WandColorSetting.title:
        CherryMenu.SubMenu.WandColorSetting.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Main.title:
        CherryMenu.CucumberySupport.Main.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Setting_Normal.title:
        CherryMenu.CucumberySupport.Setting_Normal.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Setting_Sound.title:
        CherryMenu.CucumberySupport.Setting_Sound.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Setting_Messaging.title:
        CherryMenu.CucumberySupport.Setting_Messaging.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Setting_Creative.title:
        CherryMenu.CucumberySupport.Setting_Creative.interactMenu(event);
        break;

      case CherryMenu.CucumberySupport.Setting_Admin.title:
        CherryMenu.CucumberySupport.Setting_Admin.interactMenu(event);
        break;

    }

  }
}
