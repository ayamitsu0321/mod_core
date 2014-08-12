package ayamitsu.util.proxy;

import cpw.mods.fml.relauncher.Side;

public abstract class AbstractServerProxy extends AbstractProxy {

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    public int getUniqueRenderId() {
        return -1;
    }

}
