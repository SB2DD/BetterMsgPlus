package me.polishkrowa.BetterMsgPlus;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
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

        HumanEntity to = Bukkit.getPlayerExact(args[0]);
        if (to == null) {
            sender.sendMessage(ChatColor.RED + "No Player Found !");
            return true;
        }

        String message = StringUtils.join(args, " ");
        message = message.trim();
        message = message.substring(args[0].length() + 1);

        //  "commands.message.display.outgoing": "You whisper to %s: %s",
        //  "commands.message.display.incoming": "%s whispers to you: %s",

        TranslatableComponent outgoing = new TranslatableComponent( "commands.message.display.outgoing" );
        outgoing.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        outgoing.addWith(args[0]);
        outgoing.addWith(message);
        sender.spigot().sendMessage(outgoing);

        TranslatableComponent incoming = new TranslatableComponent( "commands.message.display.incoming" );
        incoming.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        incoming.addWith(sender.getName());
        incoming.addWith(message);
        to.spigot().sendMessage(incoming);

        if (to instanceof Player && sender instanceof Player) {
            Player player = (Player) sender;
            Player receiver = (Player) to;

            MsgPlus.lastReceived.put(player.getUniqueId(), receiver.getUniqueId());
            MsgPlus.lastReceived.put(receiver.getUniqueId(), player.getUniqueId());
        } else {
            MsgPlus.lastReceived.put(to.getUniqueId(), null);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returns = new ArrayList<>();
        if (args.length <= 1) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                returns.add(player.getName());
            });
            return returns;
        }

        return returns;
    }
}
