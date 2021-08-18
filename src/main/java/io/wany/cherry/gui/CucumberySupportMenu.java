package io.wany.cherry.gui;

import com.jho5245.cucumbery.customrecipe.recipeinventory.RecipeInventoryMainMenu;
import com.jho5245.cucumbery.gui.GUI;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import io.wany.cherry.Config;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.ItemTool;
import io.wany.cherry.amethyst.Skull;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CucumberySupportMenu {

  public static class Main {

    public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;큐컴버리 메뉴"));

    public static Inventory inventory(Player player) {
      Inventory inventory = Bukkit.createInventory(null, 45, title);

      inventory.setItem(13, ItemTool.meta(Material.CRAFTING_TABLE, Message.parse(Message.effect("&e아이템 제작")), List.of(Message.parse("§7아이템 제작 메뉴를 엽니다."), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));
      inventory.setItem(35, ItemTool.meta(Material.TRIPWIRE_HOOK, Message.parse(Message.effect("&b개인 설정")), List.of(Message.parse("§7서버에서 제공하는 몇몇 기능들을 설정합니다."), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));

      inventory.setItem(29, ItemTool.meta(Skull.SETTINGS.toItemStack(), Message.parse(Message.effect("#52EE52;일반 설정")), List.of(Message.parse("§7일반적인 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));

      inventory.setItem(30, ItemTool.meta(Skull.NOTE_BLOCK.toItemStack(), Message.parse(Message.effect("#52EE52;소리 설정")), List.of(Message.parse("§7소리와 관련된 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));

      inventory.setItem(31, ItemTool.meta(Skull.MESSAGE.toItemStack(), Message.parse(Message.effect("#52EE52;메시징 설정")), List.of(Message.parse("§7출력되는 메시지와 관련된 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));


      if (player.getGameMode().equals(GameMode.CREATIVE)) {
        inventory.setItem(32, ItemTool.meta(Skull.GRASS_BLOCK.toItemStack(), Message.parse(Message.effect("#52EE52;크리에이티브 설정")), List.of(Message.parse("§7크리이에티브 모드와 관련된 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));
      }

      if (player.hasPermission("cucumbery.gui.serversettingsadmin")) {
        inventory.setItem(33, ItemTool.meta(Skull.COMMAND_BLOCK.toItemStack(), Message.parse(Message.effect("#52EE52;관리자 설정")), List.of(Message.parse("§7관리자를 위한 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§e열려면 클릭하세요!"))));
      }
      else {
        inventory.setItem(33, ItemTool.meta(Skull.COMMAND_BLOCK.toItemStack(), Message.parse(Message.effect("&7&m관리자 설정")), List.of(Message.parse("§7관리자를 위한 큐컴버리 개인 설정들을"), Message.parse("§7확인하고 변경합니다"), Message.parse("§r"), Message.parse("§c사용 불가!"))));
      }

      inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#D2B0DD;메인 메뉴로 돌어가기")), List.of(Message.parse("§7메인 메뉴로 돌아갑니다"), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

      return inventory;
    }

    public static void click(InventoryClickEvent event) {
      event.setCancelled(true);
      Player player = (Player) event.getWhoClicked();
      int slot = event.getSlot();

      switch (slot) {

        case 13 -> {
          RecipeInventoryMainMenu.openRecipeInventory(player, 1, true);
        }
        case 35 -> {
          GUI.openGUI(player, GUI.GUIType.SERVER_SETTINGS);
        }

        case 29 -> {
          Menu.show(player, Setting.Normal.inventory(player));
        }
        case 30 -> {
          Menu.show(player, Setting.Sound.inventory(player));
        }
        case 31 -> {
          Menu.show(player, Setting.Messaging.inventory(player));
        }
        case 32 -> {
          Menu.show(player, Setting.Creative.inventory(player));
        }
        case 33 -> {
          if (player.hasPermission("cucumbery.gui.serversettingsadmin")) {
            Menu.show(player, Setting.Admin.inventory(player));
          }
        }

        case 36 -> {
          Menu.show(player, Menu.Main.inventory(player));
        }

      }
    }

  }

  public static class Setting {

    public static class Normal {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;일반 - 큐컴버리 개인 설정"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 공중에서-폭죽-발사
        Menu.booleanStateButton(userDataConfig.getBoolean("공중에서-폭죽-발사"), inventory, 10, new ItemStack(Material.FIREWORK_ROCKET), "공중에서 폭죽 발사", List.of(Message.parse("§7공중에서 ", Component.keybind("key.use").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE), "§7을(를) 사용하여 폭죽을 바로 발사할지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 서버-리소스팩-사용
        Menu.booleanStateButton(userDataConfig.getBoolean("서버-리소스팩-사용"), inventory, 11, Skull.GRASS_BLOCK.toItemStack(), "서버 리소스팩 사용", List.of(Message.parse("§7서버 리소스팩을 사용 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 10, 19 -> {
            userDataConfig.toggle("공중에서-폭죽-발사");
            Menu.show(player, inventory(player));
          }
          case 11, 20 -> {
            userDataConfig.toggle("서버-리소스팩-사용");
            Menu.show(player, inventory(player));
          }

          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

    public static class Sound {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;소리 - 큐컴버리 개인 설정"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 입장-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-소리-들음"), inventory, 1, Skull.CHARACTER_J_WHITE.toItemStack(), "입장음", List.of(Message.parse("§7서버 입장 알림음 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 퇴장-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("퇴장-소리-들음"), inventory, 2, Skull.CHARACTER_Q_WHITE.toItemStack(), "퇴장음", List.of(Message.parse("§7서버 퇴장 알림음 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 채팅-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("채팅-소리-들음"), inventory, 3, Skull.CHARACTER_C_WHITE.toItemStack(), "채팅 알림음", List.of(Message.parse("§7채팅시 알림음 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 명령어-입력-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("명령어-입력-소리-들음"), inventory, 4, Skull.CHARACTER_C_WHITE.toItemStack(), "명령어 입력 알림음", List.of(Message.parse("§7명령어 입력시 소리 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 손에-든-아이템-바꾸는-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("손에-든-아이템-바꾸는-소리-들음"), inventory, 5, Skull.CHARACTER_S_WHITE.toItemStack(), "손에 든 아이템 바꾸는 소리", List.of(Message.parse("§7단축바에서 손에 든 아이템을 바꿀 시 소리 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 컨테이너-열고-닫는-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("컨테이너-열고-닫는-소리-들음"), inventory, 6, Skull.CHARACTER_C_WHITE.toItemStack(), "컨테이너 여닫는 소리", List.of(Message.parse("§7컨테이너 여닫는 소리 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 아이템-버리는-소리-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("아이템-버리는-소리-들음"), inventory, 7, Skull.CHARACTER_Q_WHITE.toItemStack(), "아이템 버리는 소리", List.of(Message.parse("§7아이템 버리는 소리 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 서버-라디오-들음
        Menu.booleanStateButton(userDataConfig.getBoolean("서버-라디오-들음"), inventory, 28, Skull.RADIO.toItemStack(), "서버 라디오", List.of(Message.parse("§7서버 라디오 청취 여부를 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 1, 10 -> {
            userDataConfig.toggle("입장-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 2, 11 -> {
            userDataConfig.toggle("퇴장-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 3, 12 -> {
            userDataConfig.toggle("채팅-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 4, 13 -> {
            userDataConfig.toggle("명령어-입력-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 5, 14 -> {
            userDataConfig.toggle("손에-든-아이템-바꾸는-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 6, 15 -> {
            userDataConfig.toggle("컨테이너-열고-닫는-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 7, 16 -> {
            userDataConfig.toggle("아이템-버리는-소리-들음");
            Menu.show(player, inventory(player));
          }
          case 28, 37 -> {
            userDataConfig.toggle("서버-라디오-들음");
            Menu.show(player, inventory(player));
          }

          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

    public static class Messaging {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;메시징 - 큐컴버리 개인 설정"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 아이템-주울때-액션바-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("아이템-주울때-액션바-띄움"), inventory, 10, Skull.CHARACTER_A_WHITE.toItemStack(), "아이템 획득 시 액션바", List.of(Message.parse("§7아이템 획득 시 액션바를 띄울 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 아이템-버릴때-액션바-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("아이템-버릴때-액션바-띄움"), inventory, 11, Skull.CHARACTER_A_WHITE.toItemStack(), "아이템 버릴 시 액션바", List.of(Message.parse("§7아이템을 버릴 시 액션바를 띄울 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 공격할-때-액션바-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("공격할-때-액션바-띄움"), inventory, 12, Skull.CHARACTER_A_WHITE.toItemStack(), "대미지 가할 때 액션바", List.of(Message.parse("§7대미지를 가할 때 액션바를 띄울 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // PVP할-때-액션바-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("PVP할-때-액션바-띄움"), inventory, 13, Skull.CHARACTER_A_WHITE.toItemStack(), "PvP시 액션바", List.of(Message.parse("§7PvP를 할 때 액션바를 띄울 지 설정합니다"), Message.parse("§7§e§o대미지 가할 때 액션바§7 기능이"), Message.parse("§7꺼져 있다면 활성화되지 않습니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 입장-타이틀-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-타이틀-띄움"), inventory, 14, Skull.CHARACTER_T_WHITE.toItemStack(), "서버 입장 시 타이틀", List.of(Message.parse("§7서버 입장 시 타이틀을 띄울 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 아이템-파괴-타이틀-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("아이템-파괴-타이틀-띄움"), inventory, 15, Skull.CHARACTER_T_WHITE.toItemStack(), "아이템 파괴 시 타이틀", List.of(Message.parse("§7아이템 파괴 시 타이틀을 띄울 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 아이템-설명-기능-사용
        Menu.booleanStateButton(userDataConfig.getBoolean("아이템-설명-기능-사용"), inventory, 16, Skull.CHARACTER_I_WHITE.toItemStack(), "아이템 설명", List.of(Message.parse("§7아이템 설명 기능을 사용할 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 10, 19 -> {
            userDataConfig.toggle("아이템-주울때-액션바-띄움");
            Menu.show(player, inventory(player));
          }
          case 11, 20 -> {
            userDataConfig.toggle("아이템-버릴때-액션바-띄움");
            Menu.show(player, inventory(player));
          }
          case 12, 21 -> {
            userDataConfig.toggle("공격할-때-액션바-띄움");
            Menu.show(player, inventory(player));
          }
          case 13, 22 -> {
            userDataConfig.toggle("PVP할-때-액션바-띄움");
            Menu.show(player, inventory(player));
          }
          case 14, 23 -> {
            userDataConfig.toggle("입장-타이틀-띄움");
            Menu.show(player, inventory(player));
          }
          case 15, 24 -> {
            userDataConfig.toggle("아이템-파괴-타이틀-띄움");
            Menu.show(player, inventory(player));
          }
          case 16, 25 -> {
            userDataConfig.toggle("아이템-설명-기능-사용");
            Menu.show(player, inventory(player));
          }

          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

    public static class Creative {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;크리에이티브 - 큐컴버리 개인 설정"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 픽블록으로-소리-블록-음높이-복사
        Menu.booleanStateButton(userDataConfig.getBoolean("픽블록으로-소리-블록-음높이-복사"), inventory, 10, Skull.NOTE_BLOCK.toItemStack(), "소리 블록 음높이 복사", List.of(Message.parse(Component.keybind("key.pickItem").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE), "§7을(를) 사용하여"), Message.parse("§7소리 블록의 음높이 값을 복사할 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 픽블록으로-소리-블록-악기-복사
        Menu.booleanStateButton(userDataConfig.getBoolean("픽블록으로-소리-블록-악기-복사"), inventory, 11, Skull.NOTE_BLOCK.toItemStack(), "소리 블록 악기 복사", List.of(Message.parse(Component.keybind("key.pickItem").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE), "§7을(를) 사용하여"), Message.parse("§7소리 블록의 악기 값을 복사할 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 웅크리기-상태에서만-소리-블록-값-복사
        Menu.booleanStateButton(userDataConfig.getBoolean("웅크리기-상태에서만-소리-블록-값-복사"), inventory, 12, Skull.NOTE_BLOCK.toItemStack(), "웅크리기 상태에서만 소리 블록 값 복사", List.of(Message.parse("§e§o웅크리기", " §7상태에서만"), Message.parse("§7소리 블록 값을 복사할 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤
        Menu.booleanStateButton(userDataConfig.getBoolean("크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤"), inventory, 13, Skull.NOTE_BLOCK.toItemStack(), "웅크리기 + 아이템 사용 키로 소리 블록 음높이 낮춤", List.of(Message.parse("§e§o웅크리기", " §7상태에서 ", Component.keybind("key.use").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE), "§7을(를) 사용하여"), Message.parse("§7소리 블록의 음높이를 낮출 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 크리에이티브-모드에서-소리-블록-클릭으로-소리-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("크리에이티브-모드에서-소리-블록-클릭으로-소리-재생"), inventory, 14, Skull.NOTE_BLOCK.toItemStack(), "소리 블록 클릭으로 재생", List.of(Message.parse("§e§o크리에이티브", " §7모드에서 ", Component.keybind("key.attack").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE), "§7을(를) 사용하여"), Message.parse("§7소리 블록을 재생할 지 설정합니다"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 10, 19 -> {
            userDataConfig.toggle("픽블록으로-소리-블록-음높이-복사");
            Menu.show(player, inventory(player));
          }
          case 11, 20 -> {
            userDataConfig.toggle("픽블록으로-소리-블록-악기-복사");
            Menu.show(player, inventory(player));
          }
          case 12, 21 -> {
            userDataConfig.toggle("웅크리기-상태에서만-소리-블록-값-복사");
            Menu.show(player, inventory(player));
          }
          case 13, 22 -> {
            userDataConfig.toggle("크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤");
            Menu.show(player, inventory(player));
          }
          case 14, 23 -> {
            userDataConfig.toggle("크리에이티브-모드에서-소리-블록-클릭으로-소리-재생");
            Menu.show(player, inventory(player));
          }

          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

    public static class Admin {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;관리자 - 큐컴버리 개인 설정 (1)"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 플러그인-개발-디버그-메시지-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("플러그인-개발-디버그-메시지-띄움"), inventory, 1, Skull.MESSAGE.toItemStack(), "플러그인 개발 디버그 메시지 띄움", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 플러그인-대량-디버그-메시지-간소화
        Menu.booleanStateButton(userDataConfig.getBoolean("플러그인-대량-디버그-메시지-간소화"), inventory, 2, Skull.MESSAGE.toItemStack(), "플러그인 대량 디버그 메시지 간소화", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 명령-블록-실행-위치-출력
        Menu.booleanStateButton(userDataConfig.getBoolean("명령-블록-실행-위치-출력"), inventory, 3, Skull.COMMAND_BLOCK.toItemStack(), "명령 블록 실행 위치 출력", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 빠른-명령-블록-사용
        Menu.booleanStateButton(userDataConfig.getBoolean("빠른-명령-블록-사용"), inventory, 4, Skull.COMMAND_BLOCK.toItemStack(), "빠른 명령 블록 사용", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 명령-블록-명령어-미리보기
        Menu.booleanStateButton(userDataConfig.getBoolean("명령-블록-명령어-미리보기"), inventory, 5, Skull.COMMAND_BLOCK.toItemStack(), "명령 블록 명령어 미리보기", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 웅크린-상태에서-명령-블록-파괴-방지
        Menu.booleanStateButton(userDataConfig.getBoolean("웅크린-상태에서-명령-블록-파괴-방지"), inventory, 6, Skull.COMMAND_BLOCK.toItemStack(), "웅크린 상태에서 명령 블록 파괴 방지", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 무적-모드
        Menu.booleanStateButton(userDataConfig.getBoolean("무적-모드"), inventory, 7, Skull.GOD.toItemStack(), "무적 모드", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // PVP-할-때-상대방에게-액션바-띄우지-않음
        Menu.booleanStateButton(userDataConfig.getBoolean("PVP-할-때-상대방에게-액션바-띄우지-않음"), inventory, 28, Skull.CHARACTER_P_WHITE.toItemStack(), "PVP 할 때 상대방에게 액션바 띄우지 않음", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(26, ItemTool.meta(Skull.NEXT.toItemStack(), Message.parse(Message.effect("&e다음 페이지")), List.of(Message.parse("§7다음 페이지로 이동합니다"), Message.parse("§r"), Message.parse("§e이동하려면 클릭하세요!"))));
        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 1, 10 -> {
            userDataConfig.toggle("플러그인-개발-디버그-메시지-띄움");
            Menu.show(player, inventory(player));
          }
          case 2, 11 -> {
            userDataConfig.toggle("플러그인-대량-디버그-메시지-간소화");
            Menu.show(player, inventory(player));
          }
          case 3, 12 -> {
            CustomConfig.UserData.SHOW_COMMAND_BLOCK_EXECUTION_LOCATION.setToggle(player);
            Menu.show(player, inventory(player));
          }
          case 4, 13 -> {
            userDataConfig.toggle("빠른-명령-블록-사용");
            Menu.show(player, inventory(player));
          }
          case 5, 14 -> {
            userDataConfig.toggle("명령-블록-명령어-미리보기");
            Menu.show(player, inventory(player));
          }
          case 6, 15 -> {
            userDataConfig.toggle("웅크린-상태에서-명령-블록-파괴-방지");
            Menu.show(player, inventory(player));
          }
          case 7, 16 -> {
            userDataConfig.toggle("무적-모드");
            Menu.show(player, inventory(player));
          }
          case 28, 37 -> {
            userDataConfig.toggle("PVP-할-때-상대방에게-액션바-띄우지-않음");
            Menu.show(player, inventory(player));
          }

          case 26 -> {
            Menu.show(player, Setting.Admin2.inventory(player));
          }
          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

    public static class Admin2 {

      public static final Component title = Message.parse(Menu.keyboardCat, Message.effect("#0E8B0E;관리자 - 큐컴버리 개인 설정 (2)"));

      public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, title);

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        // 입장-소리-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-소리-재생"), inventory, 1, Skull.CHARACTER_J_WHITE.toItemStack(), "입장 소리 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 입장-소리-무조건-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-소리-무조건-재생"), inventory, 2, Skull.CHARACTER_J_WHITE.toItemStack(), "입장 소리 무조건 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 퇴장-소리-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("퇴장-소리-재생"), inventory, 3, Skull.CHARACTER_Q_WHITE.toItemStack(), "퇴장 소리 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 퇴장-소리-무조건-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("퇴장-소리-무조건-재생"), inventory, 4, Skull.CHARACTER_Q_WHITE.toItemStack(), "퇴장 소리 무조건 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 채팅-소리-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("채팅-소리-재생"), inventory, 5, Skull.CHARACTER_C_WHITE.toItemStack(), "채팅 소리 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 채팅-소리-무조건-재생
        Menu.booleanStateButton(userDataConfig.getBoolean("채팅-소리-무조건-재생"), inventory, 6, Skull.CHARACTER_C_WHITE.toItemStack(), "채팅 소리 무조건 재생", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 입장-메시지-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-메시지-띄움"), inventory, 28, Skull.CHARACTER_J_WHITE.toItemStack(), "입장 메시지 띄움", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 입장-메시지-무조건-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("입장-메시지-무조건-띄움"), inventory, 29, Skull.CHARACTER_J_WHITE.toItemStack(), "입장 메시지 무조건 띄움", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 퇴장-메시지-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("퇴장-메시지-띄움"), inventory, 30, Skull.CHARACTER_Q_WHITE.toItemStack(), "퇴장 메시지 띄움", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        // 퇴장-메시지-무조건-띄움
        Menu.booleanStateButton(userDataConfig.getBoolean("퇴장-메시지-무조건-띄움"), inventory, 31, Skull.CHARACTER_Q_WHITE.toItemStack(), "퇴장 메시지 무조건 띄움", List.of(Message.parse("§7"), Message.parse("§r"), Message.parse("§e변경하려면 클릭하세요!")));

        inventory.setItem(18, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("&e이전 페이지")), List.of(Message.parse("§7이전 페이지로 이동합니다"), Message.parse("§r"), Message.parse("§e이동하려면 클릭하세요!"))));
        inventory.setItem(36, ItemTool.meta(Skull.PREV.toItemStack(), Message.parse(Message.effect("#52EE52;큐컴버리 메뉴로 돌어가기")), List.of(Message.parse(Message.effect("#52EE52;큐컴버리 메뉴§7로 돌아갑니다")), Message.parse("§r"), Message.parse("§e돌아가려면 클릭하세요!"))));

        return inventory;
      }

      public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Config userDataConfig = CucumberySupport.getUserDataConfig(player);

        int slot = event.getSlot();
        switch (slot) {

          case 1, 10 -> {
            userDataConfig.toggle("입장-소리-재생");
            Menu.show(player, inventory(player));
          }
          case 2, 11 -> {
            userDataConfig.toggle("입장-소리-무조건-재생");
            Menu.show(player, inventory(player));
          }
          case 3, 12 -> {
            userDataConfig.toggle("퇴장-소리-재생");
            Menu.show(player, inventory(player));
          }
          case 4, 13 -> {
            userDataConfig.toggle("퇴장-소리-무조건-재생");
            Menu.show(player, inventory(player));
          }
          case 5, 14 -> {
            userDataConfig.toggle("채팅-소리-재생");
            Menu.show(player, inventory(player));
          }
          case 6, 15 -> {
            userDataConfig.toggle("채팅-소리-무조건-재생");
            Menu.show(player, inventory(player));
          }
          case 28, 37 -> {
            userDataConfig.toggle("입장-메시지-띄움");
            Menu.show(player, inventory(player));
          }
          case 29, 38 -> {
            userDataConfig.toggle("입장-메시지-무조건-띄움");
            Menu.show(player, inventory(player));
          }
          case 30, 39 -> {
            userDataConfig.toggle("퇴장-메시지-띄움");
            Menu.show(player, inventory(player));
          }
          case 31, 40 -> {
            userDataConfig.toggle("퇴장-메시지-무조건-띄움");
            Menu.show(player, inventory(player));
          }

          case 18 -> {
            Menu.show(player, Setting.Admin.inventory(player));
          }
          case 36 -> {
            Menu.show(player, Main.inventory(player));
          }

        }
      }

    }

  }

}
