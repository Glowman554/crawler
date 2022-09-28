package gq.glowman554.crawler.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class FileUtils
{
	public static String readFile(InputStream inputStream) throws IOException
	{
		StringWriter out = new StringWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		int read;
		char[] buf = new char[4096];

		while ((read = br.read(buf)) != -1)
		{
			out.write(buf, 0, read);
		}

		br.close();
		return out.toString();
	}

	public static String readFile(String file_name) throws IOException
	{
		FileReader fr = new FileReader(file_name);
		StringWriter out = new StringWriter();

		int read;
		char[] buf = new char[4096];

		while ((read = fr.read(buf)) != -1)
		{
			out.write(buf, 0, read);
		}

		fr.close();
		return out.toString();
	}

	public static void writeFile(String file_name, String file_contents) throws IOException
	{
		FileWriter fw = new FileWriter(file_name);
		fw.write(file_contents);
		fw.close();
	}

	public static String getFileExtension(String file_name)
	{
		int dot = file_name.lastIndexOf('.');
		if (dot == -1)
		{
			return "";
		}

		String extension = file_name.substring(dot + 1);
		return extension;
	}
}
