package su.binair.security;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_7_R4.EnumChatFormat;

public class CmdTest  implements CommandExecutor , Listener
{
    public SqlConnection sql;
    public Security m;
    
	public CmdTest(SqlConnection sql, Security m)
	{
		this.sql = sql;
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) 
	{
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				Player p = (Player)sender;
				p.sendMessage(EnumChatFormat.RED + "Tu ne peux pas éxécuter cette commande.");
			}
			else
			{
				if(m.isTesting)
				{
					m.isTesting = false;
					System.out.println("Test de la base de donnée désactivé.");
				}
				else
				{
					m.isTesting = true;
					System.out.println("Test de la base de donnée lancée.");
				}
			}
		}
		return false;
	}
}
