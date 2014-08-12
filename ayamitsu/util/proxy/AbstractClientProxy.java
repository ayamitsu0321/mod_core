package ayamitsu.util.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;

public abstract class AbstractClientProxy extends AbstractProxy {

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    public int getUniqueRenderId() {
        return RenderingRegistry.getNextAvailableRenderId();
    }

}
