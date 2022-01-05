package me.polishkrowa.BetterMsgPlus;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TellCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            if (label.equalsIgnoreCase("tell"))
                sender.sendMessage(ChatColor.RED + "No player name was entered. Correct usage: /tell <player-name> <message>");
            else
                sender.sendMessage(ChatColor.RED + "No player name was entered. Correct usage: /msg <player-name> <message>");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "No message was entered !");
            return true;
        }

        Player to = Bukkit.getPlayerExact(args[0]);
        if (to == null) {
            sender.sendMessage(ChatColor.RED + "No Player Found !");
            return true;
        }

        String message = StringUtils.join(args, " ");
        message = message.trim();
        message = message.substring(args[0].length() + 1);

        MsgPlus.sendMessage(sender, to, message);

        if (sender instanceof Player) {
            Player player = (Player) sender;

            MsgPlus.lastReceived.put(player.getUniqueId(), to.getUniqueId());
            MsgPlus.lastReceived.put(to.getUniqueId(), player.getUniqueId());
        } else {
            MsgPlus.lastReceived.put(to.getUniqueId(), null);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returns = new ArrayList<>();
        if (args.length <= 1) {
            Bukkit.getOnlinePlayers().forEach(player -> returns.add(player.getName()));
            return returns;
        }

        return returns;
    }
}
