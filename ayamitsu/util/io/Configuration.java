package ayamitsu.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configuration
{
	private final File file;

	/** key, property<key, value> **/
	protected Map<String, Property> map = new LinkedHashMap<String, Property>();

	public Configuration(File f)
	{
		this.file = f;
	}

	public Property getProperty(String key, String _default)
	{
		Property prop = this.map.get(key);

		if (prop == null)
		{
			this.map.put(key, (prop = new Property(key, _default)));
		}

		return prop;
	}

	public Property getProperty(String key, int _default) { return this.getProperty(key, Integer.toString(_default)); }
	public Property getProperty(String key, boolean _default) { return this.getProperty(key, Boolean.toString(_default)); }

	public void load() throws IOException
	{
		if (!this.file.exists() || !this.file.canRead())
		{
			return;
		}

		BufferedReader br = new BufferedReader(new FileReader(this.file));
		String line;

		while ((line = br.readLine()) != null)
		{
			if (line.contains(":"))
			{
				line = new String(line.getBytes("UTF-8"), "UTF-8");
				String[] arrayOfString = line.split(":", 2);

				if (arrayOfString.length < 2)
				{
					arrayOfString = new String[] { arrayOfString[0], "" };
				}

				try
				{
					this.map.put(arrayOfString[0], new Property(arrayOfString[0], arrayOfString[1]));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		br.close();
	}

	public void save() throws IOException
	{
		File dir = this.file.getParentFile();

		if (!dir.exists() && !dir.mkdirs())
		{
			throw new IOException("Failed to make configuration directory " + dir.getPath());
		}

		if (!this.file.exists() && !this.file.createNewFile())
		{
			throw new IOException("Failed to create configuration file " + this.file.getPath());
		}

		if (!this.file.canWrite())
		{
			throw new IOException("Failed to save configuration file " + this.file.getPath());
		}

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(this.file)));
		pw.println("#" + (Calendar.getInstance().getTime()));// created time

		for (Property prop : this.map.values())
		{
			pw.println(prop.getKey() + ":" + prop.getValue());
		}

		pw.close();
	}
}
