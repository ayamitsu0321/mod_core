package ayamitsu.util.asm;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
//import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions("ayamitsu.util.asm")
public final class ASMDebugUtils implements Opcodes {

	private static final ASMDebugUtils SINGLETON = new ASMDebugUtils();
	private static final Logger logger = LogManager.getLogger("ASMDebug");//Logger.getLogger("ASMDebug");

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void log(String msg) {
		logger.log(Level.ALL, msg);
	}

    public static void fine(String msg) {
        logger.log(Level.DEBUG, msg);
    }

	public static void logAll(ClassNode cNode) {
		fine("start log");
		log(cNode);
		fine("field node log start");

		if (cNode.fields != null) {
			for (FieldNode fNode : (List<FieldNode>)cNode.fields) {
				log(fNode);
			}
		} else {
			logger.info("fields are null!");
		}

		fine("field node log end");
		fine("method node log start");

		if (cNode.methods != null) {
			for (MethodNode mNode : (List<MethodNode>)cNode.methods) {
				logAll(mNode);
			}
		} else {
			fine("methods are null!");
		}

		fine("method node log end");
		fine("end log");
	}

	public static void logAll(MethodNode mNode) {
		log(mNode);
		fine("method instruction node log start");

		if (mNode.instructions != null) {
			for (AbstractInsnNode aiNode : mNode.instructions.toArray()) {
				log(aiNode);
			}
		} else {
			logger.info("method instructions are null!");
		}

		fine("method instruction node log end");
		fine("method local variable node log start");

		if (mNode.localVariables != null) {
			for (LocalVariableNode lvNode : (List<LocalVariableNode>)mNode.localVariables) {
				log(lvNode);
			}
		} else {
			fine("method localVariables are null!");
		}

		fine("method local variable node log end");
	}

	public static void log(ClassNode cNode) {
		fine((new DebugStringBuilder().appendClass(cNode).appendName(cNode.name).appendSignature(cNode.signature).appendVersion(cNode.version).trim()).toString());
	}

	public static void log(FieldNode fNode) {
		fine((new DebugStringBuilder().appendClass(fNode).appendName(fNode.name).appendDesc(fNode.desc).appendValue(fNode.value).appendSignature(fNode.signature).trim()).toString());
	}

	public static void log(MethodNode mNode) {
		fine((new DebugStringBuilder().appendClass(mNode).appendName(mNode.name).appendDesc(mNode.desc).appendSignature(mNode.signature).trim()).toString());
	}

	public static void log(LocalVariableNode lvNode) {
		fine((new DebugStringBuilder().appendClass(lvNode).appendName(lvNode.name).appendDesc(lvNode.desc).appendSignature(lvNode.signature).appendIndex(lvNode.index).trim()).toString());
	}

	public static void log(InsnList insnList) {
		for (AbstractInsnNode aiNode : insnList.toArray()) {
			log(aiNode);
		}
	}

	public static void log(AbstractInsnNode aiNode) {
		if (aiNode instanceof FieldInsnNode) {
			FieldInsnNode fiNode = (FieldInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(fiNode).appendOpcode(fiNode.getOpcode()).appendName(fiNode.name).appendOwner(fiNode.owner).appendDesc(fiNode.desc).trim()).toString());
		} else if (aiNode instanceof FrameNode) {
			FrameNode fNode = (FrameNode)aiNode;
			fine((new DebugStringBuilder().appendClass(fNode).appendOpcode(fNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof IincInsnNode) {
			IincInsnNode iiNode = (IincInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(iiNode).appendOpcode(iiNode.getOpcode()).appendVar(iiNode.var).trim()).toString());
		} else if (aiNode instanceof InsnNode) {
			InsnNode iNode = (InsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(iNode).appendOpcode(iNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof IntInsnNode) {
			IntInsnNode iiNode = (IntInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(iiNode).appendOpcode(iiNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof InvokeDynamicInsnNode) {
			InvokeDynamicInsnNode idiNode = (InvokeDynamicInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(idiNode).appendOpcode(idiNode.getOpcode()).appendName(idiNode.name).appendDesc(idiNode.desc).trim()).toString());
		} else if (aiNode instanceof JumpInsnNode) {
			JumpInsnNode jiNode = (JumpInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(jiNode).appendOpcode(jiNode.getOpcode()).appendLabel(jiNode.label).trim()).toString());
		} else if (aiNode instanceof LabelNode) {
			LabelNode lNode = (LabelNode)aiNode;
			fine((new DebugStringBuilder().appendClass(lNode).appendOpcode(lNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof LdcInsnNode) {
			LdcInsnNode liNode = (LdcInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(liNode).appendOpcode(liNode.getOpcode()).appendCst(liNode.cst).trim()).toString());
		} else if (aiNode instanceof LineNumberNode) {
			LineNumberNode lnNode = (LineNumberNode)aiNode;
			fine((new DebugStringBuilder().appendClass(lnNode).appendOpcode(lnNode.getOpcode()).appendLine(lnNode.line).appendStart(lnNode.start).trim()).toString());
		} else if (aiNode instanceof LookupSwitchInsnNode) {
			LookupSwitchInsnNode lsiNode = (LookupSwitchInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(lsiNode).appendOpcode(lsiNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof MethodInsnNode) {
			MethodInsnNode miNode = (MethodInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(miNode).appendOpcode(miNode.getOpcode()).appendName(miNode.name).appendOwner(miNode.owner).appendDesc(miNode.desc).trim()).toString());
		} else if (aiNode instanceof MultiANewArrayInsnNode) {
			MultiANewArrayInsnNode manaiNode = (MultiANewArrayInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(manaiNode).appendOpcode(manaiNode.getOpcode()).appendDesc(manaiNode.desc).trim()).toString());
		} else if (aiNode instanceof TableSwitchInsnNode) {
			TableSwitchInsnNode tsiNode = (TableSwitchInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(tsiNode).appendOpcode(tsiNode.getOpcode()).trim()).toString());
		} else if (aiNode instanceof TypeInsnNode) {
			TypeInsnNode tiNode = (TypeInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(tiNode).appendOpcode(tiNode.getOpcode()).appendDesc(tiNode.desc).trim()).toString());
		} else if (aiNode instanceof VarInsnNode) {
			VarInsnNode viNode = (VarInsnNode)aiNode;
			fine((new DebugStringBuilder().appendClass(viNode).appendOpcode(viNode.getOpcode()).appendVar(viNode.var).trim()).toString());
		}
	}

	public static String translateOpcode(int opcode) {
		try {
			Field[] fields = Opcodes.class.getDeclaredFields();// from 57

			for (int i = 57; i < fields.length; i++) {
				if (fields[i].getInt(SINGLETON) == opcode) {
					return fields[i].getName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "unknown";
	}

	public static void exportToFile(byte[] bytes, String path) throws IOException {
		File currentDir = (File)FMLInjectionData.data()[6];
		File saveFile = new File(currentDir, path);

		if (!saveFile.getParentFile().exists() && !saveFile.getParentFile().mkdirs()) {
			throw new IOException("cannot create dir:" + saveFile.getParent());
		}

		if (!saveFile.exists() && !saveFile.createNewFile() || !saveFile.canWrite()) {
			throw new IOException("cannot create or write file:" + saveFile.getPath());
		}

		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
		dos.write(bytes);
		dos.close();
	}

	static {
		//logger.setParent(FMLLog.getLogger());
	}

	private static class DebugStringBuilder {

		private StringBuilder instance = new StringBuilder();

		public DebugStringBuilder() {}

		public DebugStringBuilder append(Object obj) {
			this.instance.append(String.valueOf(obj));
			return this;
		}

		public DebugStringBuilder appendVersion(int version) {
			this.instance.append("version:" + Integer.toString(version) + ", ");
			return this;
		}

		public DebugStringBuilder appendClass(Object instance) {
			this.instance.append((instance == null ? "null" : instance.getClass().getSimpleName()) + ", ");
			return this;
		}

		public DebugStringBuilder appendName(String name) {
			this.instance.append("name:" + name + ", ");
			return this;
		}

		public DebugStringBuilder appendOwner(String owner) {
			this.instance.append("owner:" + owner + ", ");
			return this;
		}

		public DebugStringBuilder appendDesc(String desc) {
			this.instance.append("desc:" + desc + ", ");
			return this;
		}

		public DebugStringBuilder appendOpcode(int opcode) {
			this.instance.append("opcode:" + Integer.toHexString(opcode) + "(" + ASMDebugUtils.translateOpcode(opcode) + ")" + ", ");
			return this;
		}

		public DebugStringBuilder appendValue(Object obj) {
			this.instance.append("value:" + String.valueOf(obj) + ", ");
			return this;
		}

		public DebugStringBuilder appendVar(int var) {
			this.instance.append("var:" + Integer.toString(var) + ", ");
			return this;
		}

		public DebugStringBuilder appendSignature(String signature) {
			this.instance.append("signature:" + signature + ", ");
			return this;
		}

		public DebugStringBuilder appendIndex(int index) {
			this.instance.append("index:" + Integer.toString(index) + ", ");
			return this;
		}

		public DebugStringBuilder appendCst(Object cst) {
			this.instance.append("cst:" + (cst == null ? "null" : cst.getClass().getSimpleName()) + "(" + String.valueOf(cst) + "), ");
			return this;
		}

		public DebugStringBuilder appendLine(int line) {
			this.instance.append("line:" + Integer.toString(line) + ", ");
			return this;
		}

		public DebugStringBuilder appendStart(LabelNode start) {
			this.instance.append("start(LabelNode):opcode:" + start.getOpcode() + ", label(LabelNode):info:" + start.getLabel().info + ", ");
			return this;
		}

		public DebugStringBuilder appendLabel(LabelNode label) {
			this.instance.append("label:opcode" + label.getOpcode());
			return this;
		}

		public DebugStringBuilder trim() {
			String str = this.instance.toString().trim();
			this.instance = new StringBuilder(str);

			if (this.instance.charAt(this.instance.length() - 1) == ',') {
				this.instance.deleteCharAt(this.instance.length() - 1);
			}

			return this;
		}

		public String toString() {
			return this.instance.toString();
		}

	}
}
