package io.wany.cherry.commands;

import org.apache.commons.lang.UnhandledException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ThrowCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length <= 1) {
      return true;
    }

    if (!args[0].equals("new")) {
      return true;
    }

    if (!sender.hasPermission("cherry.throw")) {
      return true;
    }

    switch (args[1]) {
      
      case "UnhandledException": {
        throw new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new UnhandledException(new NullPointerException()))))))))))))))))));
      }
      case "NullPointerException": {
        throw new NullPointerException();
      }
      case "StackOverflowError": {
        stackOverflow();
      }
      case "ArrayIndexOutOfBoundsException": {
        throw new ArrayIndexOutOfBoundsException();
      }
      case "ClassCastException": {
        throw new ClassCastException();
      }
      case "IllegalArgumentException": {
        throw new IllegalArgumentException();
      }
      case "ArithmeticException": {
        throw new ArithmeticException();
      }
      case "UnsupportedOperationException": {
        throw new UnsupportedOperationException();
      }

      default: {
        return true;
      }

    }

  }

  private static void stackOverflow() {
    stackOverflow();
  }

}
