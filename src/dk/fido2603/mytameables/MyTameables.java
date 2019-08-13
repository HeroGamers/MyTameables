package dk.fido2603.mytameables;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import dk.fido2603.mytameables.listeners.MainListener;

public class MyTameables extends JavaPlugin
{
	public static MyTameables 					instance;
	public static boolean						pluginEnabled							= false;

	public boolean								vaultEnabled							= false;

	public static Server						server									= null;
	public boolean								debug									= false;
	public boolean								expandedSearch							= false;
	private static MyTameables					plugin;
	private static FileConfiguration			config									= null;
	private static PermissionsManager			permissionsManager						= null;
	
	private static Economy						economy									= null;
	private String								chatPrefix								= "MyTameables";
	public String								serverName								= "Your Server";

	public static MyTameables instance()
	{
		return instance;
	}

	public static PermissionsManager getPermissionsManager()
	{
		return permissionsManager;
	}

	public static Economy getEconomy()
	{
		return economy;
	}

	public String getChatPrefix()
	{
		return chatPrefix;
	}

	public void sendInfo(Player player, String message)
	{
		if (player == null)
		{
			log(message);
		}
		else
		{
			player.sendMessage(message);
		}
	}

	public void onDisable()
	{
		saveSettings();
		reloadSettings();

		pluginEnabled = false;
	}

	@Override
	public void onEnable()
	{
		MainListener mainListener = null;
		
		plugin = this;
		instance = this;
		server = getServer();
		config = getConfig();

		pluginEnabled = true;

		mainListener = new MainListener(this);

		PluginManager pm = getServer().getPluginManager();

		// Check for Vault
		if (pm.getPlugin("Vault") != null && pm.getPlugin("Vault").isEnabled())
		{
			this.vaultEnabled = true;

			log("Vault detected.");

			RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null)
			{
				economy = economyProvider.getProvider();
			}
			else
			{
				plugin.log("Vault not found.");
			}
		}
		else
		{
			log("Vault not found.");
		}

		permissionsManager = new PermissionsManager(this);
		reloadSettings();
		saveSettings();

		permissionsManager.load();

		getServer().getPluginManager().registerEvents(mainListener, this);
	}

	public void log(String message)
	{
		plugin.getLogger().info(message);
	}

	public void logDebug(String message)
	{
		if (this.debug)
		{
			plugin.getLogger().info("[Debug] " + message);
		}
	}

	public void reloadSettings()
	{
		reloadConfig();
		loadSettings();
	}

	public void loadSettings()
	{
		config = getConfig();

		this.debug = config.getBoolean("Settings.Debug", false);
		this.chatPrefix = config.getString("Settings.ChatPrefix", "MyTameables");
		this.expandedSearch = config.getBoolean("Settings.ExpandedSearch", false);
	}

	public void saveSettings()
	{
		config.set("Settings.Debug", Boolean.valueOf(this.debug));
		config.set("Settings.ChatPrefix", this.chatPrefix);
		config.set("Settings.ExpandedSearch", this.expandedSearch);

		saveConfig();
	}
}