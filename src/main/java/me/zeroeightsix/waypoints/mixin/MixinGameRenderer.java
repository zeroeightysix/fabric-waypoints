package me.zeroeightsix.waypoints.mixin;

import me.zeroeightsix.waypoints.api.Waypoint;
import me.zeroeightsix.waypoints.api.Waypoints;
import me.zeroeightsix.waypoints.impl.VectorMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.WeakHashMap;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    private final WeakHashMap<Waypoint, Vec2f> screenPositions = new WeakHashMap<>();

    @Inject(method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/util/math/Matrix4f;)V"
            ), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci, boolean bl, Camera camera, MatrixStack matrixStack, Matrix4f matrix4f) {
        float scale = MinecraftClient.getInstance().getWindow().calculateScaleFactor(
                MinecraftClient.getInstance().options.guiScale,
                MinecraftClient.getInstance().forcesUnicodeFont()
        );

        Waypoints.getAccessor().getWaypoints().forEach(waypoint -> {
            Vec2f p = VectorMath.divideVec2f(VectorMath.project3Dto2D(camera.getPos().negate().add(waypoint.getPosition()), matrix.peek().getModel(), matrix4f), scale);
            screenPositions.put(waypoint, p);
        });
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V", shift = At.Shift.AFTER))
    public void onRender(CallbackInfo ci) {
        Waypoints.getAccessor().getWaypoints().forEach(waypoint -> waypoint.getRenderer().render(waypoint, screenPositions.remove(waypoint)));
    }

}