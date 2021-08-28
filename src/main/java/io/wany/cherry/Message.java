package io.wany.cherry;

import io.papermc.paper.inventory.ItemRarity;
import io.wany.cherry.amethyst.Color;
import io.wany.cherry.supports.vault.VaultChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

  public static void send(Player player, Component message) {
    player.sendMessage(message);
  }

  public static void send(CommandSender sender, Component message) {
    if (sender instanceof Player player) {
      send(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void info(Player player, String message) {
    Component component = parse(effect(message));
    player.sendMessage(component);
  }

  public static void error(Player player, String message) {
    Component component = Message.parse(effect(message));
    player.sendMessage(component);
  }

  public static void warn(Player player, String message) {
    Component component = Message.parse(effect(message));
    player.sendMessage(component);
  }

  public static void info(CommandSender sender, String message) {
    if (sender instanceof Player player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void warn(CommandSender sender, String message) {
    if (sender instanceof Player player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void error(CommandSender sender, String message) {
    if (sender instanceof Player player) {
      info(player, message);
    }
    else {
      Console.log(message);
    }
  }

  public static void info(CommandSender sender, String prefix, String message) {
    if (sender instanceof Player player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }

  public static void warn(CommandSender sender, String prefix, String message) {
    if (sender instanceof Player player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }

  public static void error(CommandSender sender, String prefix, String message) {
    if (sender instanceof Player player) {
      info(player, prefix + message);
    }
    else {
      if (prefix.equals(Cherry.PREFIX)) {
        prefix = "";
      }
      Console.log(prefix + message);
    }
  }


  public static String effect(String string) {
    string = Color.chatEffect(string);
    return string;
  }

  public static Style style(String string) {
    Style style = Component.empty().style();
    HashMap<TextDecoration, TextDecoration.State> decorations = new HashMap<>();
    int length = string.length();
    for (int i = 0; i < length; i++) {
      String character = String.valueOf(string.charAt(i));
      if (character.equals("§")) {
        i++;
        if (i >= length) {
          break;
        }

        String c = String.valueOf(string.charAt(i)).toLowerCase();

        if (c.equals("l")) {
          decorations.put(TextDecoration.BOLD, TextDecoration.State.TRUE);
        }
        else if (c.equals("m")) {
          decorations.put(TextDecoration.STRIKETHROUGH, TextDecoration.State.TRUE);
        }
        else if (c.equals("n")) {
          decorations.put(TextDecoration.UNDERLINED, TextDecoration.State.TRUE);
        }
        else if (c.equals("o")) {
          decorations.put(TextDecoration.ITALIC, TextDecoration.State.TRUE);
        }
        else if (c.equals("k")) {
          decorations.put(TextDecoration.OBFUSCATED, TextDecoration.State.TRUE);
        }
        else if (c.equals("r")) {
          style = Component.empty().style();
        }
        else if (c.equals("x")) {
          if (i + 12 >= length) {
            break;
          }
          StringBuilder color = new StringBuilder("#");
          for (int j = 0; j < 6; ++j) {
            color.append(string.charAt(i + 2 + j * 2));
          }
          style = Component.empty().style();
          style.color(TextColor.fromHexString(color.toString()));
          i += 12;
        }
        else if (c.matches("[0-9a-f]")) {
          style = Component.empty().style();
          style.color(TextColor.fromHexString(new Color(Color.Type.MFC, c).getHexString()));
        }
      }
    }
    if (!decorations.containsKey(TextDecoration.ITALIC)) {
      decorations.put(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
    style.decorations(decorations);
    return style;
  }


  public static Component parse(@NotNull Object... objects) {
    Component component = Component.empty();
    for (Object object : objects) {
      if (object instanceof Component) {
        component = component.append((Component) object);
      }
      else if (object instanceof String) {
        component = component.append(parse((String) object));
      }
      else if (object instanceof Entity) {
        component = component.append(parse((Entity) object));
      }
      else if (object instanceof ItemStack) {
        component = component.append(parse((ItemStack) object));
      }
    }
    return component;
  }

  public static Component parse(@NotNull String message) {
    List<TextComponent> components = parser(message);
    TextComponent component = null;
    for (TextComponent textComponent : components) {
      if (component == null) {
        component = components.get(0);
      }
      else {
        component = component.append(textComponent);
      }
    }
    return component;
  }

  public static Component parse(@NotNull Entity entity) {
    if (entity instanceof Player) {
      return parse((Player) entity);
    }
    else if (entity instanceof Item item) {
      ItemStack itemStack = item.getItemStack();
      return parse(itemStack);
    }
    Component component;
    if (entity.customName() != null) {
      component = entity.customName();
    }
    else {
      String key = "entity.minecraft." + entity.getType().getKey().value();
      component = translate(key);
    }
    return component;
  }

  public static Component parse(@NotNull Player player) {
    return player.displayName();
  }

  public static Component parse(@NotNull Material material) {
    return Component.translatable(material.getTranslationKey());
  }

  public static Component parse(@NotNull ItemStack itemStack) {
    Component component;
    ItemMeta itemMeta = itemStack.getItemMeta();
    Material material = itemStack.getType();
    if (itemMeta != null && itemMeta.hasDisplayName()) {
      component = itemMeta.displayName();
      if (component != null && component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
        component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE);
      }
    }
    else {
      String id = material.getKey().value();
      component = Component.translatable((material.isBlock() ? "block" : "item") + ".minecraft." + id);
      component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
      switch (material) {
        case PLAYER_HEAD, PLAYER_WALL_HEAD -> {
        }
        case POTION, SPLASH_POTION, LINGERING_POTION, TIPPED_ARROW -> {
          PotionMeta potionMeta = (PotionMeta) itemMeta;
          String potionId = potionMeta.getBasePotionData().getType().toString().toLowerCase();
          switch (potionMeta.getBasePotionData().getType()) {
            case AWKWARD, FIRE_RESISTANCE, INVISIBILITY, LUCK, MUNDANE, NIGHT_VISION, POISON, SLOW_FALLING, SLOWNESS, STRENGTH, THICK, TURTLE_MASTER, WATER, WATER_BREATHING, WEAKNESS -> component = Component.translatable("item.minecraft." + id + ".effect." + potionId);
            case UNCRAFTABLE -> component = Component.translatable("item.minecraft." + id + ".effect.empty");
            case JUMP -> component = Component.translatable("item.minecraft." + id + ".effect.leaping");
            case REGEN -> component = Component.translatable("item.minecraft." + id + ".effect.regeneration");
            case SPEED -> component = Component.translatable("item.minecraft." + id + ".effect.swiftness");
            case INSTANT_HEAL -> component = Component.translatable("item.minecraft." + id + ".effect.healing");
            case INSTANT_DAMAGE -> component = Component.translatable("item.minecraft." + id + ".effect.harming");
          }
        }
        case WRITTEN_BOOK -> {
          BookMeta bookMeta = (BookMeta) itemMeta;
          if (bookMeta.hasTitle()) {
            Component title = bookMeta.title();
            if (title instanceof TextComponent textComponentTitle) {
              if (textComponentTitle.content().equals("")) {
                component = Component.translatable("item.minecraft." + id);
              }
            }
            else {
              component = Objects.requireNonNull(bookMeta.title());
            }
          }
        }
        case COMPASS -> {
          CompassMeta compassMeta = (CompassMeta) itemMeta;
          if (compassMeta.hasLodestone()) {
            component = Component.translatable("item.minecraft.lodestone_compass");
          }
        }
        case SHIELD -> {
          BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
          if (blockStateMeta.hasBlockState()) {
            BlockState blockState = blockStateMeta.getBlockState();
            Banner bannerState = (Banner) blockState;
            DyeColor baseColor = bannerState.getBaseColor();
            component = Component.translatable("item.minecraft." + id + "." + baseColor.toString().toLowerCase());
          }
        }
      }
    }
    ItemRarity itemRarity = material.getItemRarity();
    TextColor textColor = component.color();
    if (textColor == null) {
      switch (itemRarity) {
        case UNCOMMON -> {
          if (itemMeta != null && itemMeta.hasEnchants()) {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&b").getHexString());
          }
          else {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&e").getHexString());
          }
        }
        case RARE -> {
          if (itemMeta != null && itemMeta.hasEnchants()) {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&d").getHexString());
          }
          else {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&b").getHexString());
          }
        }
        case EPIC -> {
          textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&d").getHexString());
        }
        default -> {
          if (itemMeta != null && itemMeta.hasEnchants()) {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&b").getHexString());
          }
          else {
            textColor = TextColor.fromHexString(new Color(Color.Type.MFC, "&f").getHexString());
          }
        }
      }
      component = component.color(textColor);
    }
    return component;
  }

  private static List<TextComponent> parser(String string) {
    List<TextComponent> components = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    TextComponent component = Component.empty();
    Matcher matcher = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$").matcher(string);

    int length = string.length();
    for (int i = 0; i < length; i++) {
      TextComponent old;
      String character = String.valueOf(string.charAt(i));
      if (character.equals("§")) {
        i++;
        if (i >= length) {
          break;
        }

        if (builder.length() > 0) {
          old = component;
          old = old.content(builder.toString());
          builder = new StringBuilder();
          components.add(old);
        }

        String c = String.valueOf(string.charAt(i)).toLowerCase();

        if (c.equals("l")) {
          component = component.decoration(TextDecoration.BOLD, true);
        }
        else if (c.equals("m")) {
          component = component.decoration(TextDecoration.STRIKETHROUGH, true);
        }
        else if (c.equals("n")) {
          component = component.decoration(TextDecoration.UNDERLINED, true);
        }
        else if (c.equals("o")) {
          component = component.decoration(TextDecoration.ITALIC, true);
        }
        else if (c.equals("k")) {
          component = component.decoration(TextDecoration.OBFUSCATED, true);
        }
        else if (c.equals("r")) {
          component = Component.empty();
          component = component.color(null);
          component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.ITALIC, false);
          component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
        }
        else if (c.equals("x")) {
          if (i + 12 >= length) {
            break;
          }
          StringBuilder color = new StringBuilder("#");
          for (int j = 0; j < 6; ++j) {
            color.append(string.charAt(i + 2 + j * 2));
          }
          component = Component.empty();
          component = component.color(TextColor.fromHexString(color.toString()));
          component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.ITALIC, false);
          component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
          i += 12;
        }
        else if (c.matches("[0-9a-f]")) {
          component = Component.empty();
          component = component.color(TextColor.fromHexString(new Color(Color.Type.MFC, c).getHexString()));
          component = component.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);
          component = component.decoration(TextDecoration.ITALIC, false);
          component = component.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET);
        }
      }
      else {
        int pos = string.indexOf(32, i);
        if (pos == -1) {
          pos = string.length();
        }

        if (matcher.region(i, pos).find()) {
          if (builder.length() > 0) {
            old = component;
            old = old.content(builder.toString());
            builder = new StringBuilder();
            components.add(old);
          }

          old = component;
          String urlString = string.substring(i, pos);
          component = component.content(urlString);
          component = component.clickEvent(ClickEvent.openUrl(urlString.startsWith("http") ? urlString : "http://" + urlString));
          components.add(component);
          i += pos - i - 1;
          component = old;
        }
        else {
          builder.append(character);
        }
      }
    }

    component = component.content(builder.toString());
    components.add(component);
    if (components.size() > 1) {
      List<TextComponent> returnValue = new ArrayList<>(Collections.singletonList(Component.empty()));
      returnValue.addAll(components);
      return returnValue;
    }
    return components;
  }

  public static TranslatableComponent translate(String key) {
    return Component.translatable(key);
  }



  public static String stringify(@NotNull Component component) {
    //return LegacyComponentSerializer.legacySection().serialize(component);
    StringBuilder stringBuilder = new StringBuilder();
    List<TextComponent> components = stringifier(component);
    for (TextComponent c : components) {
      boolean deco = false;
      if (c.color() != null) {
        stringBuilder.append(new Color(Color.Type.HEX, c.color().asHexString()).getMFC());
        deco = true;
      }
      if (c.decoration(TextDecoration.BOLD).equals(TextDecoration.State.TRUE)) {
        stringBuilder.append("§l");
        deco = true;
      }
      if (c.decoration(TextDecoration.STRIKETHROUGH).equals(TextDecoration.State.TRUE)) {
        stringBuilder.append("§m");
        deco = true;
      }
      if (c.decoration(TextDecoration.UNDERLINED).equals(TextDecoration.State.TRUE)) {
        stringBuilder.append("§n");
        deco = true;
      }
      if (c.decoration(TextDecoration.ITALIC).equals(TextDecoration.State.TRUE)) {
        stringBuilder.append("§o");
        deco = true;
      }
      if (c.decoration(TextDecoration.OBFUSCATED).equals(TextDecoration.State.TRUE)) {
        stringBuilder.append("§k");
        deco = true;
      }
      if (!deco) {
        stringBuilder.append("§r");
      }
      stringBuilder.append(c.content());
    }
    return stringBuilder.toString();
  }

  private static List<TextComponent> stringifier(@NotNull Component component) {
    List<TextComponent> components = new ArrayList<>();
    TextComponent textComponent = Component.empty();

    if (component instanceof TextComponent) {
      textComponent = textComponent.content(((TextComponent) component).content());
    }
    else if (component instanceof TranslatableComponent) {
      textComponent = textComponent.content(((TranslatableComponent) component).key());
    }
    textComponent = textComponent.color(component.color());
    textComponent = (TextComponent) textComponent.decorations(component.decorations());
    textComponent = textComponent.style(component.style());
    components.add(textComponent);
    if (component.children().size() > 0) {
      for (Component c : component.children()) {
        components.addAll(stringifier(c));
      }
    }

    return components;
  }



  private static class FormatterPattern {

    public static Pattern PREFIX = Pattern.compile("^\\{prefix}");
    public static Pattern SUFFIX = Pattern.compile("^\\{suffix}");
    public static Pattern NAME = Pattern.compile("^\\{name}");
    public static Pattern DISPLAYNAME = Pattern.compile("^\\{displayname}");
    public static Pattern UUID = Pattern.compile("^\\{uuid}");

  }

  public static Pattern formatPatternCompiler(String pat) {
    return Pattern.compile("^\\{" + pat + "}");
  }

  public static Component formatPlayer(Player player, String format) {
    Component component = Component.empty();
    StringBuilder stringBuilder = new StringBuilder();
    String processFormat = format;
    int length = format.length();
    for (int i = 0; i < length; i++) {
      if (format.charAt(i) == '{') {
        component = component.append(Message.parse(stringBuilder.toString()));
        stringBuilder = new StringBuilder();
        if (formatPatternCompiler("prefix").matcher(processFormat).find()) {
          Component part = Message.parse(Message.effect(VaultChat.getPrefix(player)));
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(8);
          i += 7;
          continue;
        }
        if (formatPatternCompiler("suffix").matcher(processFormat).find()) {
          Component part = Message.parse(Message.effect(VaultChat.getSuffix(player)));
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(8);
          i += 7;
          continue;
        }
        if (formatPatternCompiler("displayname").matcher(processFormat).find()) {
          component = component.append(player.displayName());
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("name").matcher(processFormat).find()) {
          Component part = Message.parse(player.getName());
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("uuid").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getUniqueId().toString()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("world").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getWorld().getName()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("x").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getX()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("y").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getY()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("z").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getZ()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("yaw").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getYaw()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("pitch").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getPitch()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("connection").matcher(processFormat).find()) {
          component = component.append(Message.parse((player.getAddress()).toString()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
      }
      else {
        stringBuilder.append(format.charAt(i));
        processFormat = processFormat.substring(1);
      }
    }
    component = component.append(Message.parse(stringBuilder.toString()));
    return component;
  }

  public static Component formatPlayerChat(Player player, Component message, String format) {
    Component component = Component.empty();
    StringBuilder stringBuilder = new StringBuilder();
    String processFormat = format;
    int length = format.length();
    for (int i = 0; i < length; i++) {
      if (format.charAt(i) == '{') {
        component = component.append(Message.parse(stringBuilder.toString()));
        stringBuilder = new StringBuilder();
        if (formatPatternCompiler("prefix").matcher(processFormat).find()) {
          Component part = Message.parse(Message.effect(VaultChat.getPrefix(player)));
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(8);
          i += 7;
          continue;
        }
        if (formatPatternCompiler("suffix").matcher(processFormat).find()) {
          Component part = Message.parse(Message.effect(VaultChat.getSuffix(player)));
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(8);
          i += 7;
          continue;
        }
        if (formatPatternCompiler("displayname").matcher(processFormat).find()) {
          component = component.append(player.displayName());
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("name").matcher(processFormat).find()) {
          Component part = Message.parse(player.getName());
          part = part.hoverEvent(player.displayName().hoverEvent());
          part = part.clickEvent(player.displayName().clickEvent());
          component = component.append(part);
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("uuid").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getUniqueId().toString()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("world").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getWorld().getName()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("x").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getX()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("y").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getY()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("z").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getZ()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("yaw").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getYaw()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("pitch").matcher(processFormat).find()) {
          component = component.append(Message.parse(player.getLocation().getPitch()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("connection").matcher(processFormat).find()) {
          component = component.append(Message.parse((player.getAddress()).toString()));
          processFormat = processFormat.substring(13);
          i += 12;
          continue;
        }
        if (formatPatternCompiler("message").matcher(processFormat).find()) {
          component = component.append(message);
          processFormat = processFormat.substring(9);
          i += 8;
          continue;
        }
      }
      else {
        stringBuilder.append(format.charAt(i));
        processFormat = processFormat.substring(1);
      }
    }
    component = component.append(Message.parse(stringBuilder.toString()));
    return component;
  }

  public static String formatPlayerd(Player player, String format) {
    format = format.replace("{name}", player.getName());
    //format = format.replace("{displayname}", Message.effect(Message.stringify(player.displayName())));
    format = format.replace("{prefix}", Message.effect(VaultChat.getPrefix(player)));
    format = format.replace("{suffix}", Message.effect(VaultChat.getSuffix(player)));
    format = format.replace("{uuid}", player.getUniqueId().toString());
    format = format.replace("{world}", player.getLocation().getWorld().getName());
    format = format.replace("{x}", player.getLocation().getX() + "");
    format = format.replace("{y}", player.getLocation().getY() + "");
    format = format.replace("{z}", player.getLocation().getZ() + "");
    format = format.replace("{pitch}", player.getLocation().getPitch() + "");
    format = format.replace("{yaw}", player.getLocation().getYaw() + "");
    format = format.replace("{connection}", player.getAddress().toString());
    return format;
  }

  public static String commandErrorArgs(String[] args, int i) {
    StringBuilder stringBuilder = new StringBuilder();
    for (; i < args.length; i++) {
      stringBuilder.append(args[i]);
      if (i != args.length - 1) {
        stringBuilder.append(" ");
      }
    }
    return stringBuilder.toString();
  }

  public static Component commandErrorArgsComponent(String label, String[] args, int i) {
    StringBuilder stringBuilder = new StringBuilder();
    if (i == -1) {
      stringBuilder.append("&c&n");
    }
    else {
      stringBuilder.append("&7");
    }
    stringBuilder.append(label);
    stringBuilder.append(" ");
    for (int j = 0; j < i; j++) {
      stringBuilder.append(args[j]);
      stringBuilder.append(" ");
    }
    if (i >= 0) {
      if (stringBuilder.length() > 10) {
        stringBuilder = new StringBuilder("&7..." + stringBuilder.substring(stringBuilder.length() - 10));
      }
    }
    stringBuilder.append("&c&n");
    stringBuilder.append(commandErrorArgs(args, i));
    return Message.parse(
      Message.effect(stringBuilder.toString()),
      Component.translatable("command.context.here")
        .color(TextColor.fromHexString("#FF5555"))
        .decoration(TextDecoration.ITALIC, TextDecoration.State.TRUE)
    );
  }

  public static Component commandErrorTranslatable(String key, ComponentLike... args) {
    return Component.translatable(key).args(args).color(TextColor.fromHexString("#FF5555"));
  }

  public static class CommandFeedback {
    public static String UNKNOWN = "알 수 없는 명령어입니다";
    public static String NO_PERMISSION = "명령어의 사용 권한이 없습니다";
    public static String NO_ARGS = "명령어의 구성 요소가 부족합니다";
    public static String NO_PLAYER = "플레이어를 찾을 수 없습니다";
    public static String ONLY_PLAYER = "플레이어만 사용 가능한 명령어입니다";
    public static String WAND_NO_WORLD = "월드를 찾을 수 없습니다";
    public static String WAND_NO_POSITION = "선택 영역이 없습니다";
    public static String WAND_ERROR_LOCATION = "선택할 수 없는 좌표입니다";
    public static String WAND_NO_INTEGER = "정수만 사용할 수 있습니다";
    public static String WAND_OUT_INTEGER = "사용할 수 없는 영역의 정수입니다";
  }

}
