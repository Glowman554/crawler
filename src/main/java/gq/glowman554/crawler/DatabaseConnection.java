package gq.glowman554.crawler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gq.glowman554.crawler.events.PageInsertEvent;
import gq.glowman554.crawler.events.PageUpdateEvent;
import gq.glowman554.crawler.utils.FileUtils;
import gq.glowman554.starlight.annotations.StarlightEventTarget;

public class DatabaseConnection
{
	private Connection connect = null;

	public DatabaseConnection(String url, String username, String password) throws ClassNotFoundException, SQLException, IOException
	{
		// Setup the connection with the DB
		connect = DriverManager.getConnection(String.format("jdbc:mysql://%s/search?user=%s&password=%s", url, username, password));

		execute_script("database_setup");
	}

	public void execute_script(String script_name) throws SQLException, IOException
	{
		Statement s = connect.createStatement();
		String[] sql_commands = FileUtils.readFile(this.getClass().getResourceAsStream("/sql/" + script_name + ".sql")).split(";");

		for (String sql : sql_commands)
		{
			sql = sql.trim()/* .replace("\n", " ").replace("\r", "") */;
			
			if (sql.equals(""))
			{
				continue;
			}

			System.out.println("Executing: " + sql);
			s.execute(sql);
		}

		s.close();
	}

	private int linkToId(String link) throws SQLException
	{
		PreparedStatement s = connect.prepareStatement("SELECT `site_id` FROM `sites` WHERE link = ?");
		s.setString(1, link);
		
		ResultSet rs = s.executeQuery();
		
		rs.next();
		int result = rs.getInt("site_id");
		
		rs.close();
		s.close();
		
		return result;
	}
	
	@StarlightEventTarget
	public void onPageInsert(PageInsertEvent e)
	{
		try
		{
			PreparedStatement s = connect.prepareStatement("INSERT IGNORE INTO `sites` (link, title, text) VALUES (?, ?, ?)");

			s.setString(1, e.getUrl());
			s.setString(2, e.getTitle().replace("\\n", ""));
			s.setString(3, e.getPagetext().replace("\\n", ""));

			s.executeUpdate();
			s.close();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}

	}
	
	@StarlightEventTarget
	public void onPageUpdate(PageUpdateEvent e)
	{
		try
		{
			PreparedStatement s = connect.prepareStatement("UPDATE `sites` SET link = ?, title = ?, text = ? WHERE site_id = ?");

			s.setString(1, e.getUrl());
			s.setString(2, e.getTitle().replace("\\n", ""));
			s.setString(3, e.getPagetext().replace("\\n", ""));
			s.setInt(4, linkToId(e.getUrl()));

			s.executeUpdate();
			s.close();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}

	}
	
	public String fetchRandomSite() throws SQLException
	{
		PreparedStatement s = connect.prepareStatement("SELECT `link` FROM `sites` ORDER BY RAND() LIMIT 1");
		ResultSet rs = s.executeQuery();
		
		rs.next();
		String result = rs.getString("link");
		
		rs.close();
		s.close();
		
		return result;
	}
	
	public boolean isCrawled(String link)
	{
		boolean return_val = false;
		try
		{
			PreparedStatement s = connect.prepareStatement("SELECT `link` FROM `sites` WHERE `link` = ?");

			s.setString(1, link);

			ResultSet rs = s.executeQuery();

			if (rs.next())
			{
				System.out.println(link + " already in db!");
				return_val = true;
			}

			rs.close();
			s.close();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		return return_val;
	}
}
