package com.lying.pvz.event.client.handler;

import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.event.client.gamebus.PVZClientEvents;
import com.lying.pvz.utils.EntityUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import java.util.List;

public class PvZEntityRenderHandler {

	/* ============================================================
	 * 冰冻：rs.isFullyFrozen 判断 + 4 块 FROSTED_ICE 立方体（脚部）
	 * ============================================================ */
	public static void checkAndRenderFrozenIce(LivingEntityRenderState rs, PoseStack stack, SubmitNodeCollector collector) {
		if (!rs.isFullyFrozen) return;

		final int light = rs.lightCoords;
		final int overlay = OverlayTexture.NO_OVERLAY;
		final BlockState iceState = Blocks.FROSTED_ICE.defaultBlockState();

		stack.pushPose();
		final float scale = 0.5F;
		stack.scale(scale, scale, scale);

		for (int i = 0; i < 4; i++) {
			stack.pushPose();
			stack.rotate(Axis.YP.rotationDegrees(i * 90F));
			submitSingleBlock(stack, collector, iceState, true, light, overlay);
			stack.popPose();
		}

		stack.popPose();
	}

	/* ============================================================
	 * 黄油：外框 BUTTER_BLOCK。。
	 * ============================================================ */
	public static void checkAndRenderButter(LivingEntityRenderState rs, PoseStack stack, SubmitNodeCollector collector) {

		final LivingEntity entity = rs.getRenderData(PVZClientEvents.ENTITY);
		if (entity == null) return;
		if (!EntityUtil.isEntityButter(entity)) return;   // ← 确认新签名

		final int light = rs.lightCoords;
		final int overlay = OverlayTexture.NO_OVERLAY;
		final BlockState butterState = PvZBlockRegister.BUTTER_BLOCK.get().defaultBlockState();

		stack.pushPose();
		final float scale = 0.7F;
		stack.scale(scale, scale, scale);
		stack.translate(-0.5F, rs.boundingBoxHeight / scale - 0.5F, -0.5F);
		submitSingleBlock(stack, collector, butterState, true, light, overlay);
		stack.popPose();
	}


	/* ============================================================
	 * 内部工具：从 BlockState 画一整块方块
	 * ============================================================ */
	private static void submitSingleBlock(PoseStack stack, SubmitNodeCollector collector, BlockState state,
										  boolean translucent, int light, int overlay) {

		final BlockStateModel model = ClientUtil.getMinecraft().getModelManager()
				.getBlockStateModelSet().get(state);

		BlockModelRenderState bmrs = new BlockModelRenderState();
		List<BlockStateModelPart> parts = bmrs.setupModel(new Matrix4f(), translucent);
		model.collectParts(BlockAndTintGetter.EMPTY, BlockPos.ZERO, state,
				RandomSource.create(0L), parts);

		IntList tints = bmrs.tintLayers();
		for (int i = 0; i < parts.size(); i++) {
			tints.add(-1);
		}

		bmrs.submitMultiLayer(stack, collector, light, overlay, 0);
	}
}