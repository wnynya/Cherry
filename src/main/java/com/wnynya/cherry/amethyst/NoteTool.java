package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.player.PlayerMeta;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteTool {

  public static void clickNoteBlock(PlayerInteractEvent event) {

    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.NOTE_BLOCK)) {
        Player player = event.getPlayer();
        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);
        if (!pm.is(PlayerMeta.Path.NOTETOOL_ENABLE)) {
          return;
        }
        if (player.isSneaking()) {
          if ( event.getItem() == null
            || event.getItem().getType().equals(Material.AIR)
            || !event.getItem().getType().isBlock() ) {
          }
          else {
          }
          if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
          }
          event.setCancelled(true);
          Block block = event.getClickedBlock();
          BlockData blockData = block.getBlockData();
          NoteBlock noteBlock = (NoteBlock) block.getBlockData();

          // update block data
          if (noteBlock.getNote().equals(Note.sharp(0, Note.Tone.F))) {
            noteBlock.setNote(Note.sharp(2, Note.Tone.F));
          }
          else {
            noteBlock.setNote(noteBlock.getNote().flattened());
          }

          block.setBlockData(noteBlock);

          // playsound
          Sound sound;
          switch (noteBlock.getInstrument()) {
            case PIANO:
              sound = Sound.BLOCK_NOTE_BLOCK_HARP;
              break;
            case BASS_DRUM:
              sound = Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
              break;
            case SNARE_DRUM:
              sound = Sound.BLOCK_NOTE_BLOCK_SNARE;
              break;
            case STICKS:
              sound = Sound.BLOCK_NOTE_BLOCK_HAT;
              break;
            case BASS_GUITAR:
              sound = Sound.BLOCK_NOTE_BLOCK_BASS;
              break;
            case FLUTE:
              sound = Sound.BLOCK_NOTE_BLOCK_FLUTE;
              break;
            case BELL:
              sound = Sound.BLOCK_NOTE_BLOCK_BELL;
              break;
            case GUITAR:
              sound = Sound.BLOCK_NOTE_BLOCK_GUITAR;
              break;
            case CHIME:
              sound = Sound.BLOCK_NOTE_BLOCK_CHIME;
              break;
            case XYLOPHONE:
              sound = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
              break;
            case IRON_XYLOPHONE:
              sound = Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
              break;
            case COW_BELL:
              sound = Sound.BLOCK_NOTE_BLOCK_COW_BELL;
              break;
            case DIDGERIDOO:
              sound = Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
              break;
            case BIT:
              sound = Sound.BLOCK_NOTE_BLOCK_BIT;
              break;
            case BANJO:
              sound = Sound.BLOCK_NOTE_BLOCK_BANJO;
              break;
            case PLING:
              sound = Sound.BLOCK_NOTE_BLOCK_PLING;
              break;
            default:
              sound = Sound.BLOCK_NOTE_BLOCK_HARP;
          }
          int pitchNum = 0;
          String bds = noteBlock.getAsString();
          String pattern = "note=([0-9]{1,2})";
          Pattern r = Pattern.compile(pattern);
          Matcher m = r.matcher(bds);
          if (m.find()) {
            pitchNum = Integer.parseInt(m.group(1));
          }
          float pitch = (float) Math.pow(2d, (pitchNum / 12d)) / 2f;
          for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            targetPlayer.playSound(block.getLocation(), sound, SoundCategory.BLOCKS, (float) Math.PI, pitch);
          }

          // particle
          Location loc = block.getLocation();
          loc.setX(loc.getX() + 0.5);
          loc.setY(loc.getY() + 1.25);
          loc.setZ(loc.getZ() + 0.5);
          double colorNum = 0.041667 * pitchNum;
          block.getWorld().spawnParticle(Particle.NOTE, loc, 0, colorNum, 0, 0, 1);

        }
      }
    }

    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
      if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.NOTE_BLOCK)) {
        Player player = event.getPlayer();
        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);
        if (!pm.is(PlayerMeta.Path.NOTETOOL_ENABLE)) {
          return;
        }
        if ((player.isSneaking()
          || (event.getItem() != null && (event.getItem().getType().equals(Material.WOODEN_SWORD)
            || event.getItem().getType().equals(Material.STONE_SWORD)
            || event.getItem().getType().equals(Material.IRON_SWORD)
            || event.getItem().getType().equals(Material.GOLDEN_SWORD)
            || event.getItem().getType().equals(Material.DIAMOND_SWORD)) ))
          && player.getGameMode().equals(GameMode.CREATIVE)) {
          event.setCancelled(true);
          Block block = event.getClickedBlock();
          NoteBlock noteBlock = (NoteBlock) block.getBlockData();

          // playsound
          Sound sound;
          switch (noteBlock.getInstrument()) {
            case PIANO:
              sound = Sound.BLOCK_NOTE_BLOCK_HARP;
              break;
            case BASS_DRUM:
              sound = Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
              break;
            case SNARE_DRUM:
              sound = Sound.BLOCK_NOTE_BLOCK_SNARE;
              break;
            case STICKS:
              sound = Sound.BLOCK_NOTE_BLOCK_HAT;
              break;
            case BASS_GUITAR:
              sound = Sound.BLOCK_NOTE_BLOCK_BASS;
              break;
            case FLUTE:
              sound = Sound.BLOCK_NOTE_BLOCK_FLUTE;
              break;
            case BELL:
              sound = Sound.BLOCK_NOTE_BLOCK_BELL;
              break;
            case GUITAR:
              sound = Sound.BLOCK_NOTE_BLOCK_GUITAR;
              break;
            case CHIME:
              sound = Sound.BLOCK_NOTE_BLOCK_CHIME;
              break;
            case XYLOPHONE:
              sound = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
              break;
            case IRON_XYLOPHONE:
              sound = Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
              break;
            case COW_BELL:
              sound = Sound.BLOCK_NOTE_BLOCK_COW_BELL;
              break;
            case DIDGERIDOO:
              sound = Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
              break;
            case BIT:
              sound = Sound.BLOCK_NOTE_BLOCK_BIT;
              break;
            case BANJO:
              sound = Sound.BLOCK_NOTE_BLOCK_BANJO;
              break;
            case PLING:
              sound = Sound.BLOCK_NOTE_BLOCK_PLING;
              break;
            default:
              sound = Sound.BLOCK_NOTE_BLOCK_HARP;
          }
          int pitchNum = 0;
          String bds = noteBlock.getAsString();
          String pattern = "note=([0-9]{1,2})";
          Pattern r = Pattern.compile(pattern);
          Matcher m = r.matcher(bds);
          if (m.find()) {
            pitchNum = Integer.parseInt(m.group(1));
          }
          float pitch = (float) Math.pow(2d, (pitchNum / 12d)) / 2f;
          for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            targetPlayer.playSound(block.getLocation(), sound, SoundCategory.BLOCKS, (float) Math.PI, pitch);
          }

          // particle
          Location loc = block.getLocation();
          loc.setX(loc.getX() + 0.5);
          loc.setY(loc.getY() + 1.25);
          loc.setZ(loc.getZ() + 0.5);
          double colorNum = 0.041667 * pitchNum;
          block.getWorld().spawnParticle(Particle.NOTE, loc, 0, colorNum, 0, 0, 1);

        }
      }
    }
  }

}
