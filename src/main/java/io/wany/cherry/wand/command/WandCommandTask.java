package io.wany.cherry.wand.command;

import io.wany.cherry.wand.Wand;
import org.bukkit.command.CommandSender;

public interface WandCommandTask {

  boolean run(Wand wand, CommandSender sender, String[] args);

}
