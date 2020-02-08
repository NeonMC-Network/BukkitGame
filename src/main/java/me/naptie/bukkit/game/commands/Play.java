package me.naptie.bukkit.game.commands;

import me.naptie.bukkit.core.utils.CoreStorage;
import me.naptie.bukkit.game.Messages;
import me.naptie.bukkit.game.utils.CU;
import me.naptie.bukkit.game.utils.ServerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Play implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!ServerManager.isInGame(player)) {
				if (args.length == 0) {
					if (ServerManager.isInLobby(player, true)) {
						String serverName = CoreStorage.getServerName(player.getServer().getPort());
						String ints = serverName.replaceAll("\\D+", "");
						args = new String[]{serverName.replaceAll(ints, "")};
					} else {
						player.sendMessage(Messages.getMessage(player, "GAME_ASSIGNATION_REQUIRED"));
					}
				}
				if (args.length == 1) {
					String server = ServerManager.getGameServer(args[0], null, false);
					if (server != null && ServerManager.isAbleToJoin(server)) {
						me.naptie.bukkit.core.Main.getInstance().connect(player, server, false);
					} else {
						if (ServerManager.isInLobby(player, true)) {
							String serverName = CoreStorage.getServerName(player.getServer().getPort());
							String ints = serverName.replaceAll("\\D+", "");
							args = new String[]{serverName.replaceAll(ints, ""), args[0]};
						} else {
							player.sendMessage(Messages.getMessage(player, "SERVER_NOT_FOUND"));
							StringBuilder games = new StringBuilder();
							for (String game : ServerManager.getGames()) {
								if (games.toString().equals("")) {
									games = new StringBuilder(game);
								} else {
									games.append(", ").append(game);
								}
							}
							player.sendMessage(CU.ts(Messages.getMessage(player, "AVAILABLE_GAMES") + games));
							for (String game : ServerManager.getGames())
								player.sendMessage(CU.ts(Messages.getMessage(player, "AVAILABLE_TYPES_FOR").replace("%game%", game) + ServerManager.getGameTypes(game)));
						}
					}
				}
				if (args.length == 2) {
					ServerManager.GameType gameType;
					try {
						gameType = ServerManager.GameType.valueOf(args[1].toUpperCase());
					} catch (IllegalArgumentException e) {
						player.sendMessage(Messages.getMessage(player, "TYPE_NOT_FOUND").replace("%type%", args[1]));
						return true;
					}
					String server = ServerManager.getGameServer(args[0], gameType, false);
					if (server != null && ServerManager.isAbleToJoin(server)) {
						me.naptie.bukkit.core.Main.getInstance().connect(player, server, false);
					} else {
						player.sendMessage(Messages.getMessage(player, "SERVER_NOT_FOUND"));
						StringBuilder games = new StringBuilder();
						for (String game : ServerManager.getGames()) {
							if (games.toString().equals("")) {
								games = new StringBuilder(game);
							} else {
								games.append(", ").append(game);
							}
						}
						player.sendMessage(CU.ts(Messages.getMessage(player, "AVAILABLE_GAMES") + games));
						for (String game : ServerManager.getGames())
							player.sendMessage(CU.ts(Messages.getMessage(player, "AVAILABLE_TYPES_FOR").replace("%game%", game) + ServerManager.getGameTypes(game)));
					}
				}
			} else {
				player.sendMessage(Messages.getMessage(player, "NOT_IN_LOBBY"));
			}
		} else {
			sender.sendMessage(Messages.getMessage("zh-CN", "NOT_A_PLAYER"));
		}
		return true;
	}
}
