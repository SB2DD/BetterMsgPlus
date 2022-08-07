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

public class ReplyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgPlus.getTranslation("console-error"));
            return true;
        }
        Player player = (Player) sender;

        if (!MsgPlus.lastReceived.containsKey(player.getUniqueId()) || MsgPlus.lastReceived.get(player.getUniqueId()) == null) {
            player.sendMessage(MsgPlus.getTranslation("no-reply"));
            return true;
        }

        Player to = Bukkit.getPlayer(MsgPlus.lastReceived.get(player.getUniqueId()));
        if (to == null) {
            player.sendMessage(MsgPlus.getTranslation("offline-player"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(MsgPlus.getTranslation("no-message"));
            return true;
        }


        String message = StringUtils.join(args, " ");
        message = message.trim();

        MsgPlus.sendMessage(sender, to, message);


        MsgPlus.lastReceived.put(player.getUniqueId(), to.getUniqueId());
        MsgPlus.lastReceived.put(to.getUniqueId(), player.getUniqueId());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
