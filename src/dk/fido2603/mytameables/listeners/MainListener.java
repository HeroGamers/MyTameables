package dk.fido2603.mytameables.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import dk.fido2603.mytameables.MyTameables;

public class MainListener implements Listener
{
	private MyTameables	plugin			= null;
	private Location	safeLocation 	= null;
	private boolean		isNotSafe		= false;

	public MainListener(MyTameables p)
	{
		this.plugin = p;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		if (event.getChunk().getEntities() == null)
		{
			return;
		}

		this.safeLocation = null;
		this.isNotSafe = false;

		Entity[] entities = event.getChunk().getEntities();
		for (Entity e : entities)
		{
			if (e != null && e instanceof Sittable && e instanceof Tameable && !isNotSafe && ((Tameable) e).getOwner() != null && ((Tameable) e).getOwner() instanceof Player)
			{
				Sittable sittingEntity = (Sittable) e;
				Tameable tameableEntity = (Tameable) e;
				Player player = (Player) tameableEntity.getOwner();
				if (player != null && player.isOnline() && MyTameables.getPermissionsManager().hasPermission(player, "mytameables.teleport"))
				{
					if (!sittingEntity.isSitting() || !player.getWorld().equals(e.getWorld()))
					{
						Location loc = null;
						if (safeLocation == null)
						{
							loc = player.getLocation();
							if (!isSafeLocation(loc))
							{
								plugin.logDebug("Whoops, seems like our player isn't at a safe location, let's find a good spot for the tameable...");
								loc = searchSafeLocation(loc);
								if (loc == null)
								{
									plugin.logDebug("Did not find a safe place to teleport the tameable! Keeping tameable at unloaded chunks!");
									player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[" + plugin.getChatPrefix() + "] " + ChatColor.RESET + ChatColor.RED + "Hello! Looks like you just teleported away from your Tameable(s)! " +
											"They can sadly not find a safe place to stay, so they are staying behind for now :( They will be waiting for you where you left them...");
									this.isNotSafe = true;
									return;
								}
							}
							this.safeLocation = loc;
						}
						else
						{
							loc = safeLocation;
						}
						
						plugin.logDebug("It's a safe location, teleporting!");
						plugin.logDebug("Teleported a tameable to a player! Chunk-unload!");
						e.teleport(loc);
						if (sittingEntity.isSitting())
						{
							sittingEntity.setSitting(false);
						}
					}
				}
			}
		}
	}

	public Location searchSafeLocation(Location loc)
	{
		if (plugin.expandedSearch)
		{
			double y;
			double x;
			double z;
			plugin.logDebug("Starting safe location search!");
			for (z = 0; z <= 2; z++)
			{
				loc.setZ(loc.getZ()+z);
				plugin.logDebug("Setting 1 Current location = X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
				for (x = 0; x <= 2; x++)
				{
					loc.setX(loc.getX()+x);
					plugin.logDebug("Setting 2 Current location = X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
					for (y = 255; y > 1; y--)
					{
						loc.setY(y);
						plugin.logDebug("Setting 3 Current location = X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
						if (isSafeLocation(loc))
						{
							plugin.logDebug("is safe");
							return loc;
						}
						plugin.logDebug("not safe");
					}
				}
			}
		}
		else
		{
			double y;
			for (y = 255; y > 1; y--)
			{
				loc.setY(y);
				plugin.logDebug("Current location = X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
				if (isSafeLocation(loc))
				{
					plugin.logDebug("is safe");
					return loc;
				}
				plugin.logDebug("not safe");
			}
		}
		
		return null;
	}

	public boolean isSafeLocation(Location location) {
		Block feet = location.getBlock();
		Block ground = feet.getRelative(BlockFace.DOWN);
		plugin.logDebug("Feet: " + feet.getType().toString());
		plugin.logDebug("Ground: " + ground.getType().toString());

		return (isTransparent(feet.getType()) && (ground.getType().isSolid() || ground.getType() == Material.WATER));
    }

	public boolean isTransparent(Material materialType)
	{
		switch (materialType)
		{
		case AIR:
		case GRASS:
		case OAK_SAPLING:
		case SPRUCE_SAPLING:
		case JUNGLE_SAPLING:
		case BIRCH_SAPLING:
		case ACACIA_SAPLING:
		case DARK_OAK_SAPLING:
		case DEAD_BUSH:
		case VINE:
		case LILY_PAD:
		case LILAC:
		case ROSE_BUSH:
		case TALL_GRASS:
		case PEONY:
		case OAK_SIGN:
		case SPRUCE_SIGN:
		case BIRCH_SIGN:
		case JUNGLE_SIGN:
		case ACACIA_SIGN:
		case DARK_OAK_SIGN:
		case SUNFLOWER:
		case WHITE_CARPET:
		case ORANGE_CARPET:
		case MAGENTA_CARPET:
		case LIGHT_BLUE_CARPET:
		case YELLOW_CARPET:
		case LIME_CARPET:
		case PINK_CARPET:
		case GRAY_CARPET:
		case LIGHT_GRAY_CARPET:
		case CYAN_CARPET:
		case PURPLE_CARPET:
		case BLUE_CARPET:
		case BROWN_CARPET:
		case GREEN_CARPET:
		case RED_CARPET:
		case BLACK_CARPET:
		case DANDELION:
		case POPPY:
		case BLUE_ORCHID:
		case ALLIUM:
		case AZURE_BLUET:
		case RED_TULIP:
		case ORANGE_TULIP:
		case WHITE_TULIP:
		case PINK_TULIP:
		case OXEYE_DAISY:
		case CORNFLOWER:
		case LILY_OF_THE_VALLEY:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case TORCH:
		case REDSTONE_TORCH:
		case SNOW:
		case LARGE_FERN:
		case FERN:
		case BAMBOO:
		case SUGAR_CANE:
		case WHEAT:
		case TRIPWIRE:
		case PUMPKIN_STEM:
		case MELON_STEM:
		case NETHER_WART:
		case BEETROOTS:
			return true;
		default:
			return false;
		}
	}
}
