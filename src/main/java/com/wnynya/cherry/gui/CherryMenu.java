package com.wnynya.cherry.gui;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.amethyst.Skull;
import com.wnynya.cherry.amethyst.SkullBank;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.wand.Wand;
import me.jho5245.cucumbery.command.sound.NoteBlockAPISong;
import me.jho5245.cucumbery.util.CustomConfig;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class CherryMenu {

  public static final String cat = "§g§g§h§j§h§g§g§v";

  public static class MainMenu {

    public static final String title = cat + "§5메뉴";

    public static void showMenu(Player player) {
      Inventory inv = Bukkit.createInventory(null, 45, title);

      ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
      skullMeta.setOwningPlayer((OfflinePlayer) player);
      skullMeta.setDisplayName(Msg.n2s("&fHello " + player.getDisplayName() + "&f!"));
      skull.setItemMeta(skullMeta);

      inv.setItem(10, skull);

      inv.setItem(12, setMeta(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), Msg.n2s("&e&l딱히"), new ArrayList<>()));
      inv.setItem(13, setMeta(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), Msg.n2s("&6&l아무"), new ArrayList<>()));
      inv.setItem(14, setMeta(new ItemStack(Material.PINK_STAINED_GLASS_PANE), Msg.n2s("&c&l생각"), new ArrayList<>()));
      inv.setItem(15, setMeta(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), Msg.n2s("&d&l없어도"), new ArrayList<>()));
      inv.setItem(16, setMeta(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), Msg.n2s("&5&l괜찮아"), new ArrayList<>()));

      inv.setItem(28, setMeta(new ItemStack(Material.SWEET_BERRIES), Msg.n2s("&d플레이어 세부 설정"), new ArrayList<>()));

      if (com.wnynya.cherry.amethyst.CucumberySupport.exist()) {
        ItemStack csi = new ItemStack(Material.LIME_BANNER);
        BannerMeta csiMeta = (BannerMeta) csi.getItemMeta();
        csiMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.STRIPE_LEFT));
        csiMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));
        csiMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
        csiMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        csi.setItemMeta(csiMeta);
        inv.setItem(34, setMeta(new ItemStack(Material.SEA_PICKLE), Msg.n2s("&a큐컴버리 메뉴"), Collections.emptyList()));
      }

      player.openInventory(inv);

      player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
    }

    public static void interactMenu(InventoryClickEvent event) {
      event.setCancelled(true);
      Player player = (Player) event.getWhoClicked();

      int slotNum = event.getSlot();

      if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
        return;
      }
      if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
        return;
      }
      if (player.getOpenInventory().getType() != InventoryType.CHEST) {
        return;
      }

      if (slotNum == 28) {
        SubMenu.CherrySetting.showMenu(player);
      }

      if (slotNum == 34) {
        if (com.wnynya.cherry.amethyst.CucumberySupport.exist()) {
          CucumberySupport.Main.showMenu(player);
        }
      }

    }

  }

  public static class SubMenu {

    public static class CherrySetting {

      public static final String title = cat + "§d플레이어 세부 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, title);

        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);

        // 체리 완드
        setBoolBtn(inv, 10, pm.is(PlayerMeta.Path.WAND_ENABLE),
          new ItemStack(new ItemStack(Material.SWEET_BERRIES)), "체리 완드",
          Arrays.asList("&f체리 완드의 포지셔너 사용 여부를 설정합니다"),
          new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.GRAY_DYE));


        // 노트 툴즈
        setBoolBtn(inv, 11, pm.is(PlayerMeta.Path.NOTETOOL_ENABLE),
          new ItemStack(new ItemStack(Material.NOTE_BLOCK)), "노트 툴즈",
          Arrays.asList("&f노트 툴즈의 사용 여부를 설정합니다"),
          new ItemStack(Material.PURPLE_DYE), new ItemStack(Material.GRAY_DYE));

        inv.setItem(36, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&f메인 메뉴로 돌어가기", new ArrayList<>()));
        //inv.setItem(38, setMeta(Skull.get(Skull.URL.PREVIOUS_PAGE), "&d&l이전 페이지로", new ArrayList<>()));
        //inv.setItem(42, setMeta(Skull.get(Skull.URL.NEXT_PAGE), "&d&l다음 페이지로", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 36) {
          MainMenu.showMenu(player);
        }
        else if (n == 38) {
          //MainMenu.showMenu(player);
        }
        else if (n == 42) {
          //MainMenu.showMenu(player);
        }
        if (n == 10 || n == 19) {
          if (pm.is(PlayerMeta.Path.WAND_ENABLE)) {
            pm.set(PlayerMeta.Path.WAND_ENABLE, false);
            Wand.getWand(player).disableParticleArea();
          }
          else {
            pm.set(PlayerMeta.Path.WAND_ENABLE, true);
            Wand.getWand(player).enableParticleArea();
          }
          SubMenu.CherrySetting.showMenu(player);
        }
        if (n == 11 || n == 20) {
          if (pm.is(PlayerMeta.Path.NOTETOOL_ENABLE)) {
            pm.set(PlayerMeta.Path.NOTETOOL_ENABLE, false);
          }
          else {
            pm.set(PlayerMeta.Path.NOTETOOL_ENABLE, true);
          }
          SubMenu.CherrySetting.showMenu(player);
        }
      }

    }

  }

  public static class CucumberySupport {

    public static class Main {

      public static final String title = cat + "§2큐컴버리 메뉴";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, title);

        inv.setItem(10, setMeta(Skull.get(Skull.URL.SETTING.getValue()), Msg.n2s("&a일반 설정"), new ArrayList<>()));
        inv.setItem(11, setMeta(Skull.get(Skull.URL.NOTE_BLOCK.getValue()), Msg.n2s("&a소리 설정"), new ArrayList<>()));
        inv.setItem(12, setMeta(Skull.get(Skull.URL.SPEECH_BUBBLE.getValue()), Msg.n2s("&a메시징 설정"), new ArrayList<>()));

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
          inv.setItem(13, setMeta(Skull.get(Skull.URL.GRASS_BLOCK.getValue()), Msg.n2s("&a크리에이티브 설정"), new ArrayList<>()));
        }

        if (player.hasPermission("cucumbery.gui.serversettingsadmin")) {
          inv.setItem(14, setMeta(Skull.get(Skull.URL.COMMAND_BLOCK.getValue()), Msg.n2s("&a관리자 설정"), new ArrayList<>()));
        }

        inv.setItem(36, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&f메인 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 10) {
          Setting_Normal.showMenu(player);
        }

        if (n == 11) {
          Setting_Sound.showMenu(player);
        }

        if (n == 12) {
          Setting_Messaging.showMenu(player);
        }

        if (n == 13) {
          if (player.getGameMode().equals(GameMode.CREATIVE)) {
            Setting_Creative.showMenu(player);
          }
        }

        if (n == 14) {
          if (player.hasPermission("cucumbery.gui.serversettingsadmin")) {
            Setting_Admin.showMenu(player);
          }
        }

        if (n == 36) {
          MainMenu.showMenu(player);
        }

      }

    }

    public static class Setting_Normal {

      public static final String title = cat + "§2큐컴버리 메뉴 - 일반 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, title);

        YamlConfiguration cConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player).getConfig();

        // 공중에서-폭죽-발사
        setBoolBtn(inv, 10, cConfig.getBoolean("공중에서-폭죽-발사"),
          new ItemStack(Skull.get(Skull.URL.FIREWORK_ROCKET.getValue())), "공중에서 폭죽 발사",
          Arrays.asList("&f공중에서 &o아이템 사용 키&r&f로 폭죽을 바로 발사할지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 서버-리소스팩-사용
        setBoolBtn(inv, 11, cConfig.getBoolean("서버-리소스팩-사용"),
          new ItemStack(Skull.get(Skull.URL.GRASS_BLOCK.getValue())), "서버 리소스팩 사용",
          Arrays.asList("&f서버 리소스팩을 사용할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        inv.setItem(36, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&2큐컴버리 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        CustomConfig ccConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player);
        YamlConfiguration cConfig = ccConfig.getConfig();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 36) {
          CucumberySupport.Main.showMenu(player);
        }

        //1
        if (n == 19 || n == 10) {
          if (cConfig.getBoolean("공중에서-폭죽-발사")) {
            cConfig.set("공중에서-폭죽-발사", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("공중에서-폭죽-발사", true);
            ccConfig.saveConfig();
          }
          Setting_Normal.showMenu(player);
        }
        //2
        if (n == 20 || n == 11) {
          if (cConfig.getBoolean("서버-리소스팩-사용")) {
            cConfig.set("서버-리소스팩-사용", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("서버-리소스팩-사용", true);
            ccConfig.saveConfig();
          }
          Setting_Normal.showMenu(player);
        }

      }

    }

    public static class Setting_Sound {

      public static final String title = cat + "§2큐컴버리 메뉴 - 소리 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, title);

        YamlConfiguration cConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player).getConfig();

        // 입장-소리-들음
        setBoolBtn(inv, 1, cConfig.getBoolean("입장-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.LIME_J.getValue())), "서버 입장 알림음",
          Arrays.asList("&f서버 입장 알림음 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_J.getValue())));

        // 퇴장-소리-들음
        setBoolBtn(inv, 2, cConfig.getBoolean("퇴장-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.LIME_Q.getValue())), "서버 퇴장 알림음",
          Arrays.asList("&f서버 퇴장 알림음 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_Q.getValue())));

        // 채팅-소리-들음
        setBoolBtn(inv, 3, cConfig.getBoolean("채팅-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.SPEECH_BUBBLE.getValue())), "채팅 알림음",
          Arrays.asList("&f채팅시 알림음 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 명령어-입력-소리-들음
        setBoolBtn(inv, 4, cConfig.getBoolean("명령어-입력-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.COMMAND_BLOCK.getValue())), "명령어 입력시 소리",
          Arrays.asList("&f명령어 입력시 소리 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 손에-든-아이템-바꾸는-소리-들음
        setBoolBtn(inv, 5, cConfig.getBoolean("손에-든-아이템-바꾸는-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.SWAP.getValue())), "손에 든 아이템 바꾸는 소리",
          Arrays.asList("&f단축바에서 손에 든 아이템 바꾸는 소리 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 컨테이너-열고-닫는-소리-들음
        setBoolBtn(inv, 6, cConfig.getBoolean("컨테이너-열고-닫는-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.CHEST.getValue())), "컨테이너 여닫는 소리",
          Arrays.asList("&f컨테이너 여닫는 소리 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 아이템-버리는-소리-들음
        setBoolBtn(inv, 7, cConfig.getBoolean("아이템-버리는-소리-들음"),
          new ItemStack(Skull.get(Skull.URL.DROPPER.getValue())), "아이템 버리는 소리",
          Arrays.asList("&f아이템 버리는 소리 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 서버-라디오-들음
        setBoolBtn(inv, 28, cConfig.getBoolean("서버-라디오-들음"),
          new ItemStack(Skull.get(Skull.URL.RADIO.getValue())), "서버 라디오",
          Arrays.asList("&f서버 라디오 청취 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));



        inv.setItem(45, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&2큐컴버리 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        CustomConfig ccConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player);
        YamlConfiguration cConfig = ccConfig.getConfig();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 45) {
          CucumberySupport.Main.showMenu(player);
        }

        //1
        if (n == 1 || n == 10) {
          if (cConfig.getBoolean("입장-소리-들음")) {
            cConfig.set("입장-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("입장-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //2
        if (n == 2 || n == 11) {
          if (cConfig.getBoolean("퇴장-소리-들음")) {
            cConfig.set("퇴장-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("퇴장-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //3
        if (n == 3 || n == 12) {
          if (cConfig.getBoolean("채팅-소리-들음")) {
            cConfig.set("채팅-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("채팅-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //4
        if (n == 4 || n == 13) {
          if (cConfig.getBoolean("명령어-입력-소리-들음")) {
            cConfig.set("명령어-입력-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("명령어-입력-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //5
        if (n == 5 || n == 14) {
          if (cConfig.getBoolean("손에-든-아이템-바꾸는-소리-들음")) {
            cConfig.set("손에-든-아이템-바꾸는-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("손에-든-아이템-바꾸는-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //6
        if (n == 6 || n == 15) {
          if (cConfig.getBoolean("컨테이너-열고-닫는-소리-들음")) {
            cConfig.set("컨테이너-열고-닫는-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("컨테이너-열고-닫는-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //7
        if (n == 7 || n == 16) {
          if (cConfig.getBoolean("아이템-버리는-소리-들음")) {
            cConfig.set("아이템-버리는-소리-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("아이템-버리는-소리-들음", true);
            ccConfig.saveConfig();
          }
          Setting_Sound.showMenu(player);
        }
        //28
        if (n == 28 || n == 37) {
          if (cConfig.getBoolean("서버-라디오-들음")) {
            cConfig.set("서버-라디오-들음", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("서버-라디오-들음", true);
            ccConfig.saveConfig();
          }
          if (NoteBlockAPISong.radioSongPlayer != null) {
            if (cConfig.getBoolean(CustomConfig.Key.LISTEN_GLOBAL.getKey()) || cConfig.getBoolean(CustomConfig.Key.LISTEN_GLOBAL_FORCE.getKey())) {
              NoteBlockAPISong.radioSongPlayer.addPlayer(player);
            }
            else {
              NoteBlockAPISong.radioSongPlayer.removePlayer(player);
            }
          }
          Setting_Sound.showMenu(player);
        }
      }

    }

    public static class Setting_Messaging {

      public static final String title = cat + "§2큐컴버리 메뉴 - 메시징 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, title);

        YamlConfiguration cConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player).getConfig();

        // 아이템-주울때-액션바-띄움
        setBoolBtn(inv, 10, cConfig.getBoolean("아이템-주울때-액션바-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_I.getValue())), "아이템 획득 시 액션바",
          Arrays.asList("&f아이템을 주울 때 액션바 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_I.getValue())));

        // 아이템-버릴때-액션바-띄움
        setBoolBtn(inv, 11, cConfig.getBoolean("아이템-버릴때-액션바-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_I.getValue())), "아이템 버릴 시 액션바",
          Arrays.asList("&f아이템을 버릴 때 액션바 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_I.getValue())));

        // 공격할-때-액션바-띄움
        setBoolBtn(inv, 12, cConfig.getBoolean("공격할-때-액션바-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_D.getValue())), "대미지 가할 때 액션바",
          Arrays.asList("&f대미지를 가할 때 액션바를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_D.getValue())));

        // PVP할-때-액션바-띄움
        setBoolBtn(inv, 13, cConfig.getBoolean("PVP할-때-액션바-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_P.getValue())), "PvP시 액션바",
          Arrays.asList("&fPvP를 할 때 액션바를 띄울 지 설정합니다", "&f&대미지 가할 때 액션바&r&f 기능이 꺼져 있다면 활성화되지 않습니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_P.getValue())));

        // 입장-타이틀-띄움
        setBoolBtn(inv, 14, cConfig.getBoolean("입장-타이틀-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_E.getValue())), "서버 입장 시 타이틀",
          Arrays.asList("&f서버에 입창할 때 타이틀 메시지를 표시할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_E.getValue())));

        // 아이템-설명-기능-사용
        setBoolBtn(inv, 15, cConfig.getBoolean("아이템-설명-기능-사용"),
          new ItemStack(Skull.get(SkullBank.LIME_T.getValue())), "아이템 설명",
          Arrays.asList("&f아이템에 대한 정보를 함께 표시할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_T.getValue())));

        // 아이템-파괴-타이틀-띄움
        setBoolBtn(inv, 16, cConfig.getBoolean("아이템-파괴-타이틀-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_B.getValue())), "아이템 파괴 시 타이틀",
          Arrays.asList("&f아이템이 파괴될 때 타이틀이 뜰 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_B.getValue())));

        inv.setItem(36, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&2큐컴버리 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        CustomConfig ccConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player);
        YamlConfiguration cConfig = ccConfig.getConfig();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 36) {
          CucumberySupport.Main.showMenu(player);
        }

        //1
        if (n == 19 || n == 10) {
          if (cConfig.getBoolean("아이템-주울때-액션바-띄움")) {
            cConfig.set("아이템-주울때-액션바-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("아이템-주울때-액션바-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }
        //2
        if (n == 20 || n == 11) {
          if (cConfig.getBoolean("아이템-버릴때-액션바-띄움")) {
            cConfig.set("아이템-버릴때-액션바-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("아이템-버릴때-액션바-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }
        //3
        if (n == 21 || n == 12) {
          if (cConfig.getBoolean("공격할-때-액션바-띄움")) {
            cConfig.set("공격할-때-액션바-띄움", false);
            ccConfig.saveConfig();
            cConfig.set("PVP할-때-액션바-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("공격할-때-액션바-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }
        //4
        if (n == 22 || n == 13) {
          if (cConfig.getBoolean("PVP할-때-액션바-띄움")) {
            cConfig.set("PVP할-때-액션바-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("PVP할-때-액션바-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }
        //5
        if (n == 23 || n == 14) {
          if (cConfig.getBoolean("입장-타이틀-띄움")) {
            cConfig.set("입장-타이틀-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("입장-타이틀-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }
        //6
        if (n == 24 || n == 15) {
          if (cConfig.getBoolean("아이템-설명-기능-사용")) {
            cConfig.set("아이템-설명-기능-사용", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("아이템-설명-기능-사용", true);
            ccConfig.saveConfig();
          }
          me.jho5245.cucumbery.util.Method.updateInventory(player);
          Setting_Messaging.showMenu(player);
        }
        //7
        if (n == 25 || n == 16) {
          if (cConfig.getBoolean("아이템-파괴-타이틀-띄움")) {
            cConfig.set("아이템-파괴-타이틀-띄움", false);
            ccConfig.saveConfig();
          }
          else {
            cConfig.set("아이템-파괴-타이틀-띄움", true);
            ccConfig.saveConfig();
          }
          Setting_Messaging.showMenu(player);
        }

      }

    }

    public static class Setting_Creative {

      public static final String title = cat + "§2큐컴버리 메뉴 - 크리에이티브 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, title);

        YamlConfiguration cConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player).getConfig();

        // 픽블록으로-소리-블록-음높이-복사
        setBoolBtn(inv, 10, cConfig.getBoolean("픽블록으로-소리-블록-음높이-복사"),
          new ItemStack(Skull.get(Skull.URL.NOTE_BLOCK.getValue())), "소리 블록 음높이 복사",
          Arrays.asList("&f&o픽블록 키&r&f를 사용하여", "&f소리 블록의 음높이 값을 복사할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 픽블록으로-소리-블록-악기-복사
        setBoolBtn(inv, 11, cConfig.getBoolean("픽블록으로-소리-블록-악기-복사"),
          new ItemStack(Skull.get(Skull.URL.NOTE_BLOCK.getValue())), "소리 블록 악기 복사",
          Arrays.asList("&f&o픽블록 키&r&f를 사용하여", "&f소리 블록의 악기 값을 복사할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 웅크리기-상태에서만-소리-블록-값-복사
        setBoolBtn(inv, 12, cConfig.getBoolean("웅크리기-상태에서만-소리-블록-값-복사"),
          new ItemStack(Skull.get(Skull.URL.NOTE_BLOCK.getValue())), "웅크리기 상태에서만 소리 블록 값 복사",
          Arrays.asList("&f&o웅크리기 &r&f상태에서만", "&f소리 블록 값을 복사할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤
        setBoolBtn(inv, 13, cConfig.getBoolean("크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤"),
          new ItemStack(Skull.get(Skull.URL.NOTE_BLOCK.getValue())), "웅크리기 + 아이템 사용 키로 소리 블록 음높이 낮춤",
          Arrays.asList("&f&o웅크리기 + 아이템 사용 키&r&f로", "&f소리 블록 음높이 낮출 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 크리에이티브-모드에서-소리-블록-클릭으로-소리-재생
        setBoolBtn(inv, 14, cConfig.getBoolean("크리에이티브-모드에서-소리-블록-클릭으로-소리-재생"),
          new ItemStack(Skull.get(Skull.URL.NOTE_BLOCK.getValue())), "소리 블록 클릭으로 재생",
          Arrays.asList("&f크리에이티브 모드에서 &o공격 / 파괴 키&r&f로", "&f소리 블록을 재생할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        inv.setItem(36, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&2큐컴버리 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        CustomConfig ccConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player);
        YamlConfiguration cConfig = ccConfig.getConfig();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 36) {
          CucumberySupport.Main.showMenu(player);
        }

        //1
        if (n == 19 || n == 10) {
          String path = "픽블록으로-소리-블록-음높이-복사";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Creative.showMenu(player);
        }
        //2
        if (n == 20 || n == 11) {
          String path = "픽블록으로-소리-블록-악기-복사";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Creative.showMenu(player);
        }
        //3
        if (n == 21 || n == 12) {
          String path = "웅크리기-상태에서만-소리-블록-값-복사";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Creative.showMenu(player);
        }
        //4
        if (n == 22 || n == 13) {
          String path = "크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Creative.showMenu(player);
        }
        //5
        if (n == 23 || n == 14) {
          String path = "크리에이티브-모드에서-소리-블록-클릭으로-소리-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Creative.showMenu(player);
        }

      }

    }

    public static class Setting_Admin {

      public static final String title = cat + "§2큐컴버리 메뉴 - 관리자 설정";

      public static void showMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, title);

        YamlConfiguration cConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player).getConfig();

        // 입장-소리-재생
        setBoolBtn(inv, 1, cConfig.getBoolean("입장-소리-재생"),
          new ItemStack(Skull.get(Skull.URL.LIME_J.getValue())), "입장 소리 재생",
          Arrays.asList("&f서버 입장 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_J.getValue())));

        // 입장-소리-무조건-재생
        setBoolBtn(inv, 2, cConfig.getBoolean("입장-소리-무조건-재생"),
          new ItemStack(Skull.get(Skull.URL.LIME_J.getValue())), "입장 소리 무조건 재생",
          Arrays.asList("&f서버 입장 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_J.getValue())));

        // 퇴장-소리-재생
        setBoolBtn(inv, 3, cConfig.getBoolean("퇴장-소리-재생"),
          new ItemStack(Skull.get(Skull.URL.LIME_Q.getValue())), "퇴장 소리 재생",
          Arrays.asList("&f서버 퇴장 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_Q.getValue())));

        // 퇴장-소리-무조건-재생
        setBoolBtn(inv, 4, cConfig.getBoolean("퇴장-소리-무조건-재생"),
          new ItemStack(Skull.get(Skull.URL.LIME_Q.getValue())), "퇴장 소리 무조건 재생",
          Arrays.asList("&f서버 입장 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_Q.getValue())));

        // 채팅-소리-재생
        setBoolBtn(inv, 5, cConfig.getBoolean("채팅-소리-재생"),
          new ItemStack(Skull.get(Skull.URL.SPEECH_BUBBLE.getValue())), "채팅 소리 재생",
          Arrays.asList("&f채팅 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 채팅-소리-무조건-재생
        setBoolBtn(inv, 6, cConfig.getBoolean("채팅-소리-무조건-재생"),
          new ItemStack(Skull.get(Skull.URL.SPEECH_BUBBLE.getValue())), "채팅 소리 무조건 재생",
          Arrays.asList("&f채팅 알림음 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // PVP-할-때-상대방에게-액션바-띄우지-않음
        setBoolBtn(inv, 7, cConfig.getBoolean("PVP-할-때-상대방에게-액션바-띄우지-않음"),
          new ItemStack(Skull.get(Skull.URL.LIME_D.getValue())), "PvP 할 때 상대방의 액션바 메시지",
          Arrays.asList("&f아이템 버리는 소리 재생 여부를 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_D.getValue())));

        // 입장-메시지-띄움
        setBoolBtn(inv, 28, cConfig.getBoolean("입장-메시지-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_J.getValue())), "입장 메시지 띄움",
          Arrays.asList("&f서버 입장 알림 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_J.getValue())));

        // 입장-메시지-무조건-띄움
        setBoolBtn(inv, 29, cConfig.getBoolean("입장-메시지-무조건-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_J.getValue())), "입장 메시지 무조건 띄움",
          Arrays.asList("&f서버 입장 알림 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 퇴장-메시지-띄움
        setBoolBtn(inv, 30, cConfig.getBoolean("퇴장-메시지-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_Q.getValue())), "퇴장 메시지 띄움",
          Arrays.asList("&f서버 퇴장 알림 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_Q.getValue())));

        // 퇴장-메시지-무조건-띄움
        setBoolBtn(inv, 31, cConfig.getBoolean("퇴장-메시지-무조건-띄움"),
          new ItemStack(Skull.get(Skull.URL.LIME_Q.getValue())), "퇴장 메시지 무조건 띄움",
          Arrays.asList("&f서버 퇴장 알림 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE),
          new ItemStack(Skull.get(Skull.URL.LIGHT_GRAY_Q.getValue())));

        // 빠른-명령-블록-사용
        setBoolBtn(inv, 32, cConfig.getBoolean("빠른-명령-블록-사용"),
          new ItemStack(Skull.get(Skull.URL.COMMAND_BLOCK.getValue())), "빠른 명령 블록 실행",
          Arrays.asList("&f명령 블록을 &o아이템 사용 키&r&f를 통해 바로 실행할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 플러그인-개발-디버그-메시지-띄움
        setBoolBtn(inv, 33, cConfig.getBoolean("플러그인-개발-디버그-메시지-띄움"),
          new ItemStack(Skull.get(Skull.URL.COMMAND_BLOCK.getValue())), "플러그인 디버그 메시지",
          Arrays.asList("&f플러그인 개발용 디버그 메시지를 띄울 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));

        // 플러그인-대량-디버그-메시지-간소화
        setBoolBtn(inv, 34, cConfig.getBoolean("플러그인-대량-디버그-메시지-간소화"),
          new ItemStack(Skull.get(Skull.URL.COMMAND_BLOCK.getValue())), "플러그인 디버그 메시지 간소화",
          Arrays.asList("&f플러그인의 대량 디버그 메시지를 간소화 할 지 설정합니다"),
          new ItemStack(Material.LIME_DYE), new ItemStack(Material.GRAY_DYE));



        inv.setItem(45, setMeta(Skull.get(Skull.URL.PREVIOUS_CHAPTER.getValue()), "&2큐컴버리 메뉴로 돌어가기", new ArrayList<>()));

        player.openInventory(inv);

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_BUTTON_CLICK, SoundCategory.VOICE, 1f, 1f);
      }

      public static void interactMenu(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        CustomConfig ccConfig = me.jho5245.cucumbery.util.CustomConfig.getPlayerConfig(player);
        YamlConfiguration cConfig = ccConfig.getConfig();

        int n = event.getSlot();

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
          return;
        }
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
          return;
        }
        if (player.getOpenInventory().getType() != InventoryType.CHEST) {
          return;
        }

        if (n == 45) {
          CucumberySupport.Main.showMenu(player);
        }

        //1
        if (n == 1 || n == 10) {
          String path = "입장-소리-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //2
        if (n == 2 || n == 11) {
          String path = "입장-소리-무조건-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //3
        if (n == 3 || n == 12) {
          String path = "퇴장-소리-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //4
        if (n == 4 || n == 13) {
          String path = "퇴장-소리-무조건-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //5
        if (n == 5 || n == 14) {
          String path = "채팅-소리-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //6
        if (n == 6 || n == 15) {
          String path = "채팅-소리-무조건-재생";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //7
        if (n == 7 || n == 16) {
          String path = "PVP-할-때-상대방에게-액션바-띄우지-않음";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //28
        if (n == 28 || n == 37) {
          String path = "입장-메시지-띄움";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //29
        if (n == 29 || n == 38) {
          String path = "입장-메시지-무조건-띄움";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //30
        if (n == 30 || n == 39) {
          String path = "퇴장-메시지-띄움";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //31
        if (n == 31 || n == 40) {
          String path = "퇴장-메시지-무조건-띄움";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //32
        if (n == 32 || n == 41) {
          String path = "빠른-명령-블록-사용";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //33
        if (n == 33 || n == 42) {
          String path = "플러그인-개발-디버그-메시지-띄움";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
        //34
        if (n == 34 || n == 43) {
          String path = "플러그인-대량-디버그-메시지-간소화";
          cConfig.set(path, !cConfig.getBoolean(path));
          ccConfig.saveConfig();
          Setting_Admin.showMenu(player);
        }
      }

    }

  }

  public static ItemStack setMeta(ItemStack item, String name, List<String> lore) {
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(Msg.n2s(name));
    int index = 0;
    for (String l : lore) {
      lore.set(index, Msg.n2s(l));
      index++;
    }
    meta.setLore(lore);
    item.setItemMeta(meta);
    return item;
  }

  public static void setBoolBtn(Inventory i, int s, Boolean b, ItemStack titleItem, String itemName, List<String> titleLore, ItemStack btnTrue, ItemStack btnFalse) {
    ItemStack title = null;
    ItemStack btn = null;
    if (b) {
      title = setMeta(titleItem, "&a&l" + itemName, titleLore);
      btn = setMeta(new ItemStack(btnTrue), "&a&l" + itemName, Arrays.asList("&f클릭하여 &c비활성화"));
    }
    else {
      title = setMeta(titleItem, "&c&l" + itemName, titleLore);
      btn = setMeta(new ItemStack(btnFalse), "&c&l" + itemName, Arrays.asList("&f클릭하여 &a활성화"));
    }
    i.setItem(s, title);
    i.setItem(s + 9, btn);
  }
  public static void setBoolBtn(Inventory i, int s, Boolean b, ItemStack titleItem, String itemName, List<String> titleLore, ItemStack btnTrue, ItemStack btnFalse, ItemStack titleItemFalse) {
    ItemStack title = null;
    ItemStack btn = null;
    if (b) {
      title = setMeta(titleItem, "&a&l" + itemName, titleLore);
      btn = setMeta(new ItemStack(btnTrue), "&a&l" + itemName, Arrays.asList("&f클릭하여 &c비활성화"));
    }
    else {
      title = setMeta(titleItemFalse, "&c&l" + itemName, titleLore);
      btn = setMeta(new ItemStack(btnFalse), "&c&l" + itemName, Arrays.asList("&f클릭하여 &a활성화"));
    }
    i.setItem(s, title);
    i.setItem(s + 9, btn);
  }

}






























