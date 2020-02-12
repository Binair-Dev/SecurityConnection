package su.binair.security;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
 
public class SqlConnection 
{
    private Security m;
    private Connection connection;
    private String urlbase,host,database,user,pass;
    private String hasAccountCheck = "SELECT id FROM users WHERE pseudo = ?";
    
    public SqlConnection(String urlbase, String host, String database, String user, String pass) 
    {
        this.urlbase = urlbase;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
    }
   
    public void connection()
    {
        if(!isConnected())
        {
            try 
            {
                connection = DriverManager.getConnection(urlbase + host + "/" + database + "?autoReconnect=true", user, pass);
                System.out.println("connected ok");
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
                connection();
            }
        }
    }
   
    public void disconnect()
    {
        if(isConnected())
        {
            try 
            {
                connection.close();
                System.out.println("connected off");
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
    }
   
    public boolean isConnected()
    {
    	try 
    	{
			if(connection.isValid(3000))
			{
				connection.close();
				connection = null;
				return true;
			}
			else
			{
				connection();
				return false;
			}
		} 
    	catch (SQLException e) 
    	{
			try 
			{
				connection.close();
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			connection = null;
			connection();
			System.out.println("Connection impossible a la base de donnée.");
		}
		return false;
    }
    
 
    public boolean hasAccount(AsyncPlayerPreLoginEvent player)
    {
       if(isConnected())
       {
           try 
           {
               PreparedStatement q = connection.prepareStatement(hasAccountCheck);
               q.setString(1, player.getName().toString());
               ResultSet resultat = q.executeQuery();
               boolean hasAccount = resultat.next();
               q.close();
               return hasAccount;
           } 
           catch (SQLException e) 
           {
               e.printStackTrace();
           }
       }
        return false;
    }
        
    public boolean hasAccountDebug(String test)
    {
       if(isConnected())
       {
           try 
           {
               PreparedStatement q = connection.prepareStatement(hasAccountCheck);
               q.setString(1, test);
               ResultSet resultat = q.executeQuery();
               boolean hasAccount = resultat.next();
               q.close();
               return hasAccount;
           } 
           catch (SQLException e) 
           {
               e.printStackTrace();
           }
       }
        return false;
    }
}