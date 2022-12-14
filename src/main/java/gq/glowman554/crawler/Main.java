package gq.glowman554.crawler;

import java.io.IOException;
import java.sql.SQLException;

import gq.glowman554.crawler.data.Config;
import gq.glowman554.crawler.events.PageInsertEvent;
import gq.glowman554.crawler.utils.ThreadHelper;
import gq.glowman554.starlight.StarlightEventManager;
import gq.glowman554.starlight.annotations.StarlightEventTarget;

public class Main
{
	private static Main instance;

	private LinkQueue linkQueue;
	private Crawler crawler = new Crawler();
	private DatabaseConnection databaseConnection;
	private Config config;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, IllegalArgumentException, IllegalAccessException
	{
		(instance = new Main()).run();
	}

	public static Main getInstance()
	{
		return instance;
	}

	public Main() throws IllegalArgumentException, IllegalAccessException, IOException
	{
		config = Config.load();
		
		String[] initial_sites = new String[config.initial_sites.len()];
		for (int i = 0; i < config.initial_sites.len(); i++)
		{
			initial_sites[i] = config.initial_sites.get(i);
		}
		
		linkQueue = new LinkQueue(initial_sites);
	}

	public void run() throws ClassNotFoundException, SQLException, IOException
	{
		databaseConnection = new DatabaseConnection(config.db_url, config.db_user, config.db_password);
		StarlightEventManager.register(this);
		StarlightEventManager.register(linkQueue);
		StarlightEventManager.register(databaseConnection);

		new ThreadHelper(config.threads, () -> {
			while (true)
			{
				try
				{
					crawler.crawl(linkQueue.fetch());
				}
				catch (IOException | IllegalStateException e)
				{
					e.printStackTrace();
					try
					{
						Thread.sleep(1000 * 10);
					}
					catch (InterruptedException e1)
					{
					}
				}
			}
		}).start().join();
	}

	@StarlightEventTarget
	public void onPageInsert(PageInsertEvent e)
	{
		System.out.println(String.format("queue size: %d (last page inserted: %s (%s))", linkQueue.len(), e.getTitle(), e.getUrl()));
	}

	public Crawler getCrawler()
	{
		return crawler;
	}

	public LinkQueue getLinkQueue()
	{
		return linkQueue;
	}
}
