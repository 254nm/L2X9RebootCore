package me.l2x9.core.misc.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 2023/08/01 4:03 AM
 * This file was created as a part of L2X9RebootCore
 */
public class PlayerVoteListener implements VoteListener {
    @Override
    public void voteMade(Vote vote) {

        Utils.run(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex group voters user add %s", vote.getUsername())));

        Player player = Bukkit.getPlayerExact(vote.getUsername());
        if (player != null) Utils.sendPrefixedLocalizedMessage(player, "vote_thanks");

        Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(vote.getUsername())).forEach(p -> {
            Utils.sendPrefixedLocalizedMessage(p, "vote_announcement", vote.getUsername(), vote.getServiceName());
        });
    }
}
