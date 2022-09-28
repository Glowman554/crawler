package gq.glowman554.crawler.data;

import java.io.File;
import java.io.IOException;

import gq.glowman554.crawler.utils.FileUtils;
import gq.glowman554.reflex.Reflex;
import gq.glowman554.reflex.ReflexField;
import gq.glowman554.reflex.loaders.ReflexJsonLoader;
import gq.glowman554.reflex.types.ReflexStringArray;

public class Config
{
	@ReflexField
	public String db_url;
	@ReflexField
	public String db_user;
	@ReflexField
	public String db_password;
	@ReflexField
	public ReflexStringArray initial_sites;
	
	@ReflexField
	public int threads;
	
	private static String config_path = "crawler_config.json";
	
	public static Config load() throws IOException, IllegalArgumentException, IllegalAccessException
	{
		if (!new File(config_path).exists())
		{
			FileUtils.writeFile(config_path, FileUtils.readFile(Config.class.getResourceAsStream("/config.json")));
			
			System.out.println("Wrote " + config_path + ". please edit it.");
			System.exit(0);
		}
		
		return (Config) new Reflex(new ReflexJsonLoader(FileUtils.readFile(config_path))).load(new Config());
	}
}
