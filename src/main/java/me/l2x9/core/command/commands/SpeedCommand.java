package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand() {
        super(
                "speed",
                "/speed <number>",
                "l2x9core.command.speed",
                "Turn up your fly speed");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender).orElse(null);
        if (player != null) {
            try {
                if (args.length > 0) {
                    float speed = Float.parseFloat(args[0]);
                    if (speed <= 1) {
                        player.setFlySpeed(speed);
                        sendMessage(player, "&3Fly speed set to&r&a %f", speed);
                    } else sendMessage(player, "Flying speed must not be above 1");
                } else {
                    sendMessage(sender, "&3Please note that the default flight speed is&r&a 0.1");
                    sendErrorMessage(sender, getUsage());
                }
            } catch (NumberFormatException e) {
                sendErrorMessage(player, getUsage());
            }
        } else sendErrorMessage(sender, PLAYER_ONLY);
    }
}
