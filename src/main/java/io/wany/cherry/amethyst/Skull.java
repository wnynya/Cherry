package io.wany.cherry.amethyst;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.wany.cherry.Cherry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class Skull {

  private final String name;
  private final UUID uuid;
  private String texture = null;
  private PlayerProfile playerProfile = null;

  public Skull(String name, Player player) {
    this.name = name;
    this.uuid = player.getUniqueId();
    this.playerProfile = player.getPlayerProfile();
  }

  public Skull(Player player) {
    this(player.getName(), player);
  }

  public Skull(String name, String texture) {
    this.name = name;
    this.uuid = Cherry.UUID;
    this.texture = texture;
  }

  public Skull(String texture) {
    this("Unknown", texture);
  }

  public ItemStack toItemStack() {
    ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
    skullMeta.getPlayerProfile();
    skullMeta.setPlayerProfile(this.toPlayerProfile());
    itemStack.setItemMeta(skullMeta);
    return itemStack;
  }

  public PlayerProfile toPlayerProfile() {
    ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayers()[0]);
    PlayerProfile playerProfile = skullMeta.getPlayerProfile();
    if (this.playerProfile == null) {
      this.playerProfile = playerProfile;
      this.playerProfile.clearProperties();
      this.playerProfile.setName(this.name);
      this.playerProfile.setId(this.uuid);
      this.playerProfile.setProperty(new ProfileProperty("textures", this.texture));
    }
    return this.playerProfile;
  }

  public static Skull PREV = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ3M2NmNjZkMzFiODNjZDhiODY0NGMxNTk1OGMxYjczYzhkOTczMjNiODAxMTcwYzFkODg2NGJiNmE4NDZkIn19fQ==");
  public static Skull NEXT = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ==");

  public static Skull SETTINGS = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTk0OWExOGNiNTJjMjkzZmU3ZGU3YmExMDE0NjcxMzQwZWQ3ZmY4ZTVkNzA1YjJkNjBiZjg0ZDUzMTQ4ZTA0In19fQ==");
  public static Skull SPEECH_BUBBLE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ4Y2UxY2YxOGFmMDVhNTc2ZDYwODEyMzAwMWI3OTFmZWRiNjIyOTExZWY4ZDM4YTMyMGRhM2JjYmY2ZmQyMCJ9fX0=");
  public static Skull MESSAGE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFlN2JmNDUyMmIwM2RmY2M4NjY1MTMzNjNlYWE5MDQ2ZmRkZmQ0YWE2ZjFmMDg4OWYwM2MxZTYyMTZlMGVhMCJ9fX0=");
  public static Skull GOD = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjZkNDI3YTg5YzRjYmZkOTk4ODI2YzY0MjViNjNiZmM3ZmJkMmVkZjgxZWI3ZjUyODE4NmJlNjZjNTQ1ZTc5In19fQ==");
  public static Skull RADIO = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ4YThjNTU4OTFkZWM3Njc2NDQ0OWY1N2JhNjc3YmUzZWU4OGEwNjkyMWNhOTNiNmNjN2M5NjExYTdhZiJ9fX0=");
  public static Skull APPLE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNhZmVmMjhjNzQ1NjIzMGU2ZDI2MTgwMmJjNjI2ZTBkOTRhNjU5YmIyYmJmNTljMjdmZjZhNTU0ZWIxOWQ4NSJ9fX0=");

  public static Skull NOTE_BLOCK = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZWI3N2Q0ZDI1NzI0YTljYWYyYzdjZGYyZDg4Mzk5YjE0MTdjNmI5ZmY1MjEzNjU5YjY1M2JlNDM3NmUzIn19fQ==");
  public static Skull GRASS_BLOCK = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE5ZTM2YTg3YmFmMGFjNzYzMTQzNTJmNTlhN2Y2M2JkYjNmNGM4NmJkOWJiYTY5Mjc3NzJjMDFkNGQxIn19fQ==");
  public static Skull COMMAND_BLOCK = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19");

  public static Skull CHARACTER_A_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0MTc0ODEyMTYyNmYyMmFlMTZhNGM2NjRjNzMwMWE5ZjhlYTU5MWJmNGQyOTg4ODk1NzY4MmE5ZmRhZiJ9fX0=");
  public static Skull CHARACTER_B_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJiOWUxNmUyNjIwNmE3MDliZjA3YzI0OTNjYTRjNWQyNGY1Njc1NjU0ZmMxMzBkMWQ1ZWM1ZThjNWJlNSJ9fX0=");
  public static Skull CHARACTER_C_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjJhNTg3NjExMzMyMmYzOWFhMmJiZWY0YmQ2Yjc5ZWM2YjUyYTk3YmI2ZmFiNjc0YmRkYmQ3YjZlYWIzYmEifX19");
  public static Skull CHARACTER_D_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE2NjE0MTlkZTQ5ZmY0YTJjOTdiMjdmODY4MDE0ZmJkYWViOGRkN2Y0MzkyNzc3ODMwYjI3MTRjYWFmZDFmIn19fQ==");
  public static Skull CHARACTER_E_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFlZWY4OGUyYzkyOGI0NjZjNmVkNWRlYWE0ZTE5NzVhOTQzNmMyYjFiNDk4ZjlmN2NiZjkyYTliNTk5YTYifX19");
  public static Skull CHARACTER_I_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM5OWRmYjI3MDRlMWJkNmU3ZmFjZmI0M2IzZTZmYmFiYWYxNmViYzdlMWZhYjA3NDE3YTZjNDY0ZTFkIn19fQ==");
  public static Skull CHARACTER_J_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgxZWZhYTliYzNiNjA3NDdhNzUwYTY0OGIxOTg3ODdmMTg2ZWI5Mzg1OWFlYTUyMDMxZDVhOGM4ODEwNzUifX19");
  public static Skull CHARACTER_P_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0YzIxZDE3YWQ2MzYzODdlYTNjNzM2YmZmNmFkZTg5NzMxN2UxMzc0Y2Q1ZDliMWMxNWU2ZTg5NTM0MzIifX19");
  public static Skull CHARACTER_Q_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyNGQyOGM4MmYzNzExYTk3NTAxNDExZWNjM2NiNDY2ODc3NDgzYjEyMmEyNjU2YzhlZWFkZmI4ZDIxIn19fQ==");
  public static Skull CHARACTER_S_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxMDEzODQxNjUyODg4OTgxNTU0OGI0NjIzZDI4ZDg2YmJiYWU1NjE5ZDY5Y2Q5ZGJjNWFkNmI0Mzc0NCJ9fX0=");
  public static Skull CHARACTER_T_WHITE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNmYjUwZmU3NTU5YmM5OWYxM2M0NzM1NmNjZTk3ZmRhM2FhOTIzNTU3ZmI1YmZiMTdjODI1YWJmNGIxZDE5In19fQ==");

  public static Skull CHARACTER_C_LIME = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWZlNmE0ZjdmYzMxOTU0YWM3ZDE3ZjcwMmYyMjgzNWUzMjVmOGJiNTMyNmNmZjYyNzNjN2I5Y2MxOTIxY2ExIn19fQ==");

  public static Skull CHARACTER_P_PURPLE = new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYzYzRiZDZmZDRlMTVkZmZhYzM0NjgwYTc2NmI4YTJiNWQ1MjM0NTFjYmQzZjk4MzQyOTRjYzhmMmM5MmJmIn19fQ==");

}
