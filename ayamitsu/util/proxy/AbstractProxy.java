package ayamitsu.util.proxy;

import cpw.mods.fml.relauncher.Side;

public abstract class AbstractProxy {

    public abstract Side getSide();

    public abstract void preInit();

    public abstract void init();

    public abstract void postInit();

    public abstract int getUniqueRenderId();

}
