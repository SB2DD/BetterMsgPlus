package me.polishkrowa.BetterMsgPlus;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

import java.net.MalformedURLException;
import java.net.URL;
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

        BaseComponent output = new TextComponent();
        String [] parts = message.split("\\s+");

        for (int i = 0; i < parts.length; i++) {
            try {
                URL url = new URL(parts[i]);
                TextComponent link = new TextComponent(parts[i]);
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, parts[i]));
                output.addExtra(link);

            } catch (MalformedURLException e) {
                // If there was an URL that was not it!...
                output.addExtra(parts[i]);
            }
            if (i != parts.length - 1)
                output.addExtra(" ");
        }
        //  "commands.message.display.outgoing": "You whisper to %s: %s",
        //  "commands.message.display.incoming": "%s whispers to you: %s",

        TranslatableComponent outgoing = new TranslatableComponent( "commands.message.display.outgoing" );
        outgoing.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        outgoing.addWith(args[0]);
        outgoing.addWith(output);
        sender.spigot().sendMessage(outgoing);

        TranslatableComponent incoming = new TranslatableComponent( "commands.message.display.incoming" );
        incoming.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        incoming.addWith(sender.getName());
        incoming.addWith(output);
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
