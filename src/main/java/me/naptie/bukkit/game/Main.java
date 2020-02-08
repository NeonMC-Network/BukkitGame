package me.naptie.bukkit.game;

import me.naptie.bukkit.game.commands.Leave;
import me.naptie.bukkit.game.commands.Play;
import me.naptie.bukkit.game.utils.MySQLManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

	public static MySQLManager mysql;
	private static Logger logger;
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		logger = this.getLogger();
		mysql = new MySQLManager();
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveDefaultConfig();
		for (String language : getConfig().getStringList("languages")) {
			File localeFile = new File(getDataFolder(), language + ".yml");
			if (localeFile.exists()) {
				if (getConfig().getBoolean("update-language-files")) {
					saveResource(language + ".yml", true);
				}
			} else {
				saveResource(language + ".yml", false);
			}
		}
		getCommand("play").setExecutor(new Play());
		getCommand("leave").setExecutor(new Leave());
		logger.info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		instance = null;
		logger.info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
		logger = null;
	}
}
