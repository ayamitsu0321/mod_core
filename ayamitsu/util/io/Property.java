package ayamitsu.util.io;

public class Property
{
	protected final String key;
	protected String value;

	public Property(String str, String str1)
	{
		this.key = str;
		this.value = str1;
	}

	public String getKey()
	{
		return this.key;
	}

	public String getValue()
	{
		return this.value;
	}

	public int getInt(int defaultValue)
	{
		try
		{
			return Integer.parseInt(this.value);
		}
		catch (NumberFormatException e)
		{
			return defaultValue;
		}
	}

	public int getInt()
	{
		return this.getInt(-1);
	}

	public boolean getBoolean()
	{
		return this.getBoolean(false);
	}

	public boolean getBoolean(boolean defaultValue)
	{
		if ("true".equals(this.value.toLowerCase()) || "on".equals(this.value.toLowerCase()))
		{
			return true;
		}
		else if ("false".equals(this.value.toLowerCase()) || "off".equals(this.value.toLowerCase()))
		{
			return false;
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Property && ((Property)obj).key.equals(this.key) && ((Property)obj).value.equals(this.value);
	}

}