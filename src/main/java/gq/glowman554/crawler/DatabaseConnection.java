package gq.glowman554.crawler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gq.glowman554.crawler.events.LinkDbCheck;
import gq.glowman554.crawler.events.PageInsertEvent;
import gq.glowman554.crawler.utils.FileUtils;
import gq.glowman554.starlight.annotations.StarlightEventTarget;

public class DatabaseConnection
{
	private Connection connect = null;

	public DatabaseConnection(String url, String username, String password) throws ClassNotFoundException, SQLException, IOException
	{
		// Setup the connection with the DB
		connect = DriverManager.getConnection(String.format("jdbc:mysql://%s/?user=%s&password=%s", url, username, password));

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

	@StarlightEventTarget
	public void onPageInsert(PageInsertEvent e)
	{
		try
		{
			PreparedStatement s = connect.prepareStatement("INSERT IGNORE INTO `sites` (link, title, text) VALUES (?, ?, ?)");
			s.execute("USE `search`");

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
	public void onLinkBeforeCrawl(LinkDbCheck e)
	{
		try
		{
			PreparedStatement s = connect.prepareStatement("SELECT `link` FROM `sites` WHERE `link` = ?");
			s.execute("USE `search`");

			s.setString(1, e.getLink());

			ResultSet rs = s.executeQuery();

			if (rs.next())
			{
				System.out.println(e.getLink() + " already in db!");
				e.setCanceled(true);
			}

			rs.close();
			s.close();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
}
