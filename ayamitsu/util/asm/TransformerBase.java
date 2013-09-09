package ayamitsu.util.asm;

import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public abstract class TransformerBase implements IClassTransformer, Opcodes {

	protected String targetClassName = this.createTargetClassName();

	protected abstract String createTargetClassName();

	@Override
	public byte[] transform(String name, String transformedName, byte[] arrayOfByte) {
		if (!this.targetClassName.equals(transformedName)) {
			return arrayOfByte;
		}

		try {
			return this.transformTarget(name, transformedName, arrayOfByte);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Could not transform %s class", transformedName), e);
		}
	}

	protected abstract byte[] transformTarget(String name, String transformedName, byte[] arrayOfByte);

	protected ClassNode encode(byte[] arrayOfByte) {
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(arrayOfByte);
		cReader.accept(cNode, 0);
		return cNode;
	}

	protected byte[] decode(ClassNode cNode) {
		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)
		{
			@Override
			public String getCommonSuperClass(String type1, String type2)
			{
				return FMLDeobfuscatingRemapper.INSTANCE.map(type1); // need this ... ?
			}
		};
		cNode.accept(cWriter);
		return cWriter.toByteArray();
	}

	protected String map(String typeName) {
		return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
	}

	protected String mapFieldName(String owner, String name, String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
	}

	protected String mapMethodName(String owner, String name, String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
	}

	protected String mapDesc(String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
	}

	protected String mapMethodDesc(String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
	}

	protected String mapInvokeDynamicMethodName(String name, String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapInvokeDynamicMethodName(name, desc);
	}

	protected String mapType(String type) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapType(type);
	}

	protected String unmap(String typeName) {
		return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
	}

	protected String mapSignature(String signature, boolean typeSignature) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapSignature(signature, typeSignature);
	}

	protected String unmapMethodName(String owner, String name) {
		owner = this.unmap(owner);// unmap
		Map<String, String> methodMap = null;

		try {
			Method method = FMLDeobfuscatingRemapper.class.getDeclaredMethod("getMethodMap", new Class[] { String.class });
			method.setAccessible(true);
			methodMap = (Map<String, String>)method.invoke(FMLDeobfuscatingRemapper.INSTANCE, owner);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for (Map.Entry<String, String> entry : methodMap.entrySet()) {
			if (name.equals(entry.getValue())) {
				// example:
				// g(III)Lahz -> g
				return entry.getKey().substring(0, entry.getKey().indexOf("("));
			}
		}

		return name; // noop
	}

}
