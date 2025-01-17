package xaeroplus.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;

import java.util.Objects;

import static xaeroplus.util.Shared.customDimensionId;

@Mixin(value = WaypointsIngameRenderer.class, remap = false)
public class MixinWaypointsIngameRenderer {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lxaero/common/minimap/waypoints/WaypointsManager;getDimensionDivision(Ljava/lang/String;)D"))
    public double redirectDimensionDivision(final WaypointsManager waypointsManager, final String worldContainerID) {
        if (worldContainerID != null && Minecraft.getMinecraft().world != null) {
            try {
                int dim = Minecraft.getMinecraft().world.provider.getDimension();
                if (!Objects.equals(dim, customDimensionId)) {
                    double currentDimDiv = Objects.equals(dim, -1) ? 8.0 : 1.0;
                    String dimPart = worldContainerID.substring(worldContainerID.lastIndexOf(47) + 1);
                    Integer dimKey = waypointsManager.getDimensionForDirectoryName(dimPart);
                    double selectedDimDiv = dimKey == -1 ? 8.0 : 1.0;
                    return currentDimDiv / selectedDimDiv;
                }
            } catch (final Exception e) {
                // fall through
            }
        }
        return waypointsManager.getDimensionDivision(worldContainerID);
    }
}
