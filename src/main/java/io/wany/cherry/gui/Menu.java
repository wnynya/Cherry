package io.wany.cherry.gui;

import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.ItemTool;
import io.wany.cherry.amethyst.Skull;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Menu {

  public static final TranslatableComponent keyboardCat = Component.translatable("").args(Message.parse("cherry-menu-title"));

  public static class Main {

    public static final Component title = Message.parse(keyboardCat, Message.effect("#5C2E6B;메뉴"));

    public static Inventory inventory(Player player) {
      Inventory inventory = Bukkit.createInventory(null, 45, title);

      inventory.setItem(10, ItemTool.meta(new Skull(player).toItemStack(), Message.parse("§fHello ", Message.formatPlayer(player, "§r{prefix}§r{displayname}§r{suffix}§r"), "§f!")));

      inventory.setItem(12, ItemTool.meta(Material.YELLOW_STAINED_GLASS_PANE, "&e&l딱히"));
      inventory.setItem(13, ItemTool.meta(Material.ORANGE_STAINED_GLASS_PANE, "&6&l아무"));
      inventory.setItem(14, ItemTool.meta(Material.PINK_STAINED_GLASS_PANE, "&c&l생각"));
      inventory.setItem(15, ItemTool.meta(Material.MAGENTA_STAINED_GLASS_PANE, "&d&l없어도"));
      inventory.setItem(16, ItemTool.meta(Material.PURPLE_STAINED_GLASS_PANE, "&5&l괜찮아"));

      inventory.setItem(28, ItemTool.meta(Skull.CHARACTER_P_PURPLE.toItemStack(), Message.parse(Message.effect("#D2B0DD;개인 설정")), List.of(Message.parse("§7플레이어 개인 설정"), Message.parse("§r"), Message.parse("§c사용 불가"))));

      if (CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        inventory.setItem(34, ItemTool.meta(Skull.CHARACTER_C_LIME.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴")), List.of(Message.parse("§7큐컴버리 메뉴"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));
      }

      return inventory;
    }

    public static void click(InventoryClickEvent event) {
      event.setCancelled(true);
      Player player = (Player) event.getWhoClicked();
      int slot = event.getSlot();

      switch (slot) {

        case 28 -> {
          Console.log("a");
        }

        case 34 -> {
          if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
            return;
          }
          show(player, CucumberySupportMenu.Main.inventory(player));
        }

      }
    }

  }

  public static void onInventoryClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Component title = event.getView().title();

    if (title.equals(Main.title)) {
      Main.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Main.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Main.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Normal.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Normal.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Sound.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Sound.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Messaging.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Messaging.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Creative.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Creative.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Admin.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Admin.click(event);
    }
    else if (title.equals(CucumberySupportMenu.Setting.Admin2.title)) {
      if (!CucumberySupport.ENABLE && CucumberySupport.EXIST) {
        return;
      }
      CucumberySupportMenu.Setting.Admin2.click(event);
    }

  }

  public static void show(Player player, Inventory inventory) {
    player.openInventory(inventory);
    player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1f, 1f);
  }

  public static void stateButton(Inventory inventory, int index, String title, List<Component> lore, ItemStack titleItem, ItemStack stateItem) {
    if (inventory.getSize() < index + 9) {
      return;
    }
    inventory.setItem(index, ItemTool.meta(titleItem, Message.parse(Message.effect(title)), lore));
    inventory.setItem(index + 9, ItemTool.meta(stateItem, Message.parse(Message.effect(title)), lore));
  }

  public static void booleanStateButton(boolean bool, Inventory inventory, int index, ItemStack titleItem, String title, List<Component> lore) {
    if (inventory.getSize() < index + 9) {
      return;
    }
    if (bool) {
      inventory.setItem(index, ItemTool.meta(titleItem, Message.parse(Message.effect("&a" + title)), lore));
      inventory.setItem(index + 9, ItemTool.meta(new ItemStack(Material.LIME_DYE), Message.parse(Message.effect("&a" + title)), lore));
    }
    else {
      inventory.setItem(index, ItemTool.meta(titleItem, Message.parse(Message.effect("&c" + title)), lore));
      inventory.setItem(index + 9, ItemTool.meta(new ItemStack(Material.GRAY_DYE), Message.parse(Message.effect("&c" + title)), lore));
    }
  }

}
