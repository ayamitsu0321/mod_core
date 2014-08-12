package ayamitsu.util.reflect;

import cpw.mods.fml.relauncher.CoreModManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Reflector {

	private static final boolean renameTable = Boolean.parseBoolean(System.getProperty("renamed"));// -Drenamed
	private static final boolean deobfuscatedEnvironment;

	private Reflector() {}

	public static boolean isRenameTable() {
		return renameTable;
	}

	public static boolean isDeobfuscatedEnvironment() {
		return deobfuscatedEnvironment;
	}

	public static Constructor getConstructor(Class clazz, Class ... arrayOfClass) throws RuntimeException {
		try {
			Constructor constructor = clazz.getConstructor(arrayOfClass);
			constructor.setAccessible(true);
			return constructor;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Field getField(Class clazz, Object instance, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Field getField(Class clazz, Object instance, int i) {
		try {
			Field field = clazz.getDeclaredFields()[i];
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getPrivateValue(Class clazz, Object instance, int i) {
		try {
			Field field = getField(clazz, instance, i);
			return field.get(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getPrivateValue(Class clazz, Object instance, String name) {
		try {
			Field field = getField(clazz, instance, name);
			return field.get(instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPrivateValue(Class clazz, Object instance, int i, Object obj) {
		try {
			Field field = getField(clazz, instance, i);
			field.set(instance, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPrivateValue(Class clazz, Object instance, String name, Object obj) {
		try {
			Field field = getField(clazz, instance, name);
			field.set(instance, obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Method getMethod(Class clazz, String str, Class ... arrayOfClass) throws RuntimeException {
		try {
			Method method = clazz.getDeclaredMethod(str, arrayOfClass);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static {
		boolean flag = false;

		try {
			Field field = CoreModManager.class.getField("deobfuscatedEnvironment");
			field.setAccessible(true);
			flag = field.getBoolean(null);
			field.setAccessible(false);
		} catch (Exception e) {
			flag = false;
		}

		deobfuscatedEnvironment = flag;
	}
}
