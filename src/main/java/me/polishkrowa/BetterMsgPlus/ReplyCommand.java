package me.polishkrowa.BetterMsgPlus;

import net.md_5.bungee.api.chat.TranslatableComponent;
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
            sender.sendMessage(ChatColor.RED + "Console can't reply to messages !");
            return true;
        }
        Player player = (Player) sender;

        if (!MsgPlus.lastReceived.containsKey(player.getUniqueId()) || MsgPlus.lastReceived.get(player.getUniqueId()) == null) {
            player.sendMessage(ChatColor.RED + "You have no one to reply to !");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "No message was entered !");
            return true;
        }


        String message = StringUtils.join(args, " ");
        message = message.trim();

        //  "commands.message.display.outgoing": "You whisper to %s: %s",
        //  "commands.message.display.incoming": "%s whispers to you: %s",
        Player to = Bukkit.getPlayer(MsgPlus.lastReceived.get(player.getUniqueId()));

        TranslatableComponent outgoing = new TranslatableComponent( "commands.message.display.outgoing" );
        outgoing.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        outgoing.addWith(to.getName());
        outgoing.addWith(message);
        sender.spigot().sendMessage(outgoing);

        TranslatableComponent incoming = new TranslatableComponent( "commands.message.display.incoming" );
        incoming.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        incoming.addWith(sender.getName());
        incoming.addWith(message);
        to.spigot().sendMessage(incoming);


        MsgPlus.lastReceived.put(player.getUniqueId(), to.getUniqueId());
        MsgPlus.lastReceived.put(to.getUniqueId(), player.getUniqueId());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returns = new ArrayList<>();
        return returns;
    }
}
