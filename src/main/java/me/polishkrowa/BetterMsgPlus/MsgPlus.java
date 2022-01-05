package me.polishkrowa.BetterMsgPlus;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class MsgPlus extends JavaPlugin {

    //player, lastReceived
    public static HashMap<UUID, UUID> lastReceived = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(this.getCommand("tell")).setExecutor(new TellCommand());
        Objects.requireNonNull(this.getCommand("tell")).setTabCompleter(new TellCommand());

        Objects.requireNonNull(this.getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(this.getCommand("reply")).setTabCompleter(new ReplyCommand());
    }

    public static TextComponent parseURL(String string) {
        TextComponent output = new TextComponent();
        String [] parts = string.split("\\s+");

        for (int i = 0; i < parts.length; i++) {
            try {
                new URL(parts[i]);
                TextComponent link = new TextComponent(parts[i]);
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, parts[i]));
                output.addExtra(link);

            } catch (MalformedURLException e) {
                // If there was a URL that was not it!...
                output.addExtra(parts[i]);
            }
            if (i != parts.length - 1)
                output.addExtra(" ");
        }
        return output;
    }

    public static void sendMessage(CommandSender sender, Player to, String message) {
        TextComponent output = parseURL(message);

        TranslatableComponent outgoing = new TranslatableComponent( "commands.message.display.outgoing" );
        outgoing.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        outgoing.addWith(to.getName());
        outgoing.addWith(output);
        sender.spigot().sendMessage(outgoing);

        TranslatableComponent incoming = new TranslatableComponent( "commands.message.display.incoming" );
        incoming.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        incoming.addWith(sender.getName());
        incoming.addWith(output);
        to.spigot().sendMessage(incoming);
    }
}
