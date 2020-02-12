package su.binair.security;

import java.sql.Time;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public class Security extends JavaPlugin implements Listener
{	
    public SqlConnection sql;
    
    //BDD
    public static String ip;
    public static String base;
    public static String pass;
    public static String user;
    
    public boolean isActive = true;
    public boolean isTesting = false;
    
    public static int id = 0;
    
	@SuppressWarnings("deprecation")
	public void onEnable()
	{
		saveDefaultConfig();
		
		ip = this.getConfig().getString("bdd.ip");
		base = this.getConfig().getString("bdd.base");
		user = this.getConfig().getString("bdd.user");
		pass = this.getConfig().getString("bdd.pass");
		
        sql = new SqlConnection("jdbc:mysql://",ip,base,user,pass);
        sql.connection();
        
		this.getServer().getPluginManager().registerEvents((Listener) this, this);
		
		this.getCommand("test").setExecutor((CommandExecutor)new CmdTest(sql, this));

        Bukkit.getScheduler().runTaskTimer((Plugin)this, (BukkitRunnable)new BukkitRunnable() 
        {
            public void run() 
            {
            	
            }
        }, 0L, 1L);
    }
	
	public void onDisable()
	{
		sql.disconnect();
	}
	
	@EventHandler
	public void onJoinCheck(AsyncPlayerPreLoginEvent e)
	{
		if(sql.isConnected())
		{
			if(sql.hasAccount(e))
			{
				System.out.println("Connection autorisée - Joueur bien inscrit sur le site.");
			}
			else
			{
				System.out.println("Connection refusée - Joueur non-inscrit sur le site.");
		        e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "Vous n'étes pas inscrit sur le site!" == null ? "Vous avez donc été bannis" : "Vous n'étes pas inscrit sur le site!");
			}
		}
		else
		{
			Player p = Bukkit.getPlayer(e.getName());
			if(p.isOp())
			{
				p.sendMessage("La base de donnée n'est pas liée, la sécurité est donc désactivée");
			}
		}
	}
}
