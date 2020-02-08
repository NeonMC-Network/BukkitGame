package me.naptie.bukkit.game.commands;

import me.naptie.bukkit.game.Messages;
import me.naptie.bukkit.game.utils.ServerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (ServerManager.isInGame(player)) {
				String lobby = ServerManager.getLobby(player.getServer().getPort());
				if (lobby != null) {
					me.naptie.bukkit.core.Main.getInstance().connect(player, lobby, false);
				} else {
					player.sendMessage(Messages.getMessage(player, "SERVER_UNAVAILABLE"));
				}
			} else {
				player.sendMessage(Messages.getMessage(player, "NOT_IN_GAME"));
			}
		} else {
			sender.sendMessage(Messages.getMessage("zh-CN", "NOT_A_PLAYER"));
		}
		return true;
	}
}
