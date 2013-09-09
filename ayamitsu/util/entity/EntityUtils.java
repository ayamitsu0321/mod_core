package ayamitsu.util.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public final class EntityUtils
{
	// on server exist Map
	//classToStringMapping
	//stringToClassMapping
	//IDtoClassMapping

	public static boolean isLivingClass(Class clazz)
	{
		return clazz != null && (EntityLiving.class).isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers());
	}

	public static boolean isLivingName(String name)
	{
		Class clazz = getClassFromName(name);
		return isLivingClass(clazz);
	}

	public static boolean containsClass(Class clazz)
	{
		return getClassToStringMapping().containsKey(clazz);
	}

	public static boolean containsName(String name)
	{
		return getStringToClassMapping().containsKey(name);
	}

	public static Class getClassFromName(String name)
	{
		return (Class)getStringToClassMapping().get(name);
	}

	public static String getNameFromClass(Class clazz)
	{
		return (String)getClassToStringMapping().get(clazz);
	}

	public static Map getStringToClassMapping()
	{
		try
		{
			Field field = EntityList.class.getDeclaredFields()[0];
			field.setAccessible(true);
			return (Map)field.get(null);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static Map getClassToStringMapping()
	{
		try
		{
			Field field = EntityList.class.getDeclaredFields()[1];
			field.setAccessible(true);
			return (Map)field.get(null);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static int getAllEntityValue()
	{
		return getStringToClassMapping().size();
	}
}
