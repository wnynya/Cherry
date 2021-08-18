package io.wany.cherry.amethyst;

import io.wany.cherry.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemTool {

  public static ItemStack meta(ItemStack itemStack, Component title, List<Component> lore) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(title);
    itemStack.setItemMeta(itemMeta);
    itemStack.lore(lore);
    return itemStack;
  }

  public static ItemStack meta(ItemStack itemStack, Component title) {
    List<Component> componentLore = new ArrayList<>();
    return meta(itemStack, title, componentLore);
  }

  public static ItemStack meta(ItemStack itemStack, String title, List<String> lore) {
    List<Component> componentLore = new ArrayList<>();
    for (String l : lore) {
      componentLore.add(Message.parse(Message.effect(l)));
    }
    return meta(itemStack, Message.parse(Message.effect(title)), componentLore);
  }

  public static ItemStack meta(ItemStack itemStack, String title) {
    return meta(itemStack, Message.parse(Message.effect(title)));
  }

  public static ItemStack meta(Material material, Component title, List<Component> lore) {
    ItemStack itemStack = new ItemStack(material);
    return meta(itemStack, title, lore);
  }

  public static ItemStack meta(Material material, Component title) {
    List<Component> componentLore = new ArrayList<>();
    return meta(material, title, componentLore);
  }

  public static ItemStack meta(Material material, String title, List<String> lore) {
    List<Component> componentLore = new ArrayList<>();
    for (String l : lore) {
      componentLore.add(Message.parse(Message.effect(l)));
    }
    return meta(material, Message.parse(Message.effect(title)), componentLore);
  }

  public static ItemStack meta(Material material, String title) {
    return meta(material, Message.parse(Message.effect(title)));
  }

  public static ItemStack bool(boolean bool, ItemStack trueObject, ItemStack falseObject) {
    if (bool) {
      return trueObject;
    }
    else {
      return falseObject;
    }
  }

}
