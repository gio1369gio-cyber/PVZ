package com.lying.pvz.client.render.renderer.entity.misc.bowling;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.bowling.AbstractBowlingProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

/**
 * 保龄球类投掷物的抽象渲染器。
 *
 * <p>纹理 / 缩放统一从 {@code ClientModelRegistry} 查询（通过 {@link PVZAPI#client()}），
 * 不再硬编码 {@code getRenderResource()}。
 *
 * <p>三个几何扩展点仍接收实体，在 {@link #extractRenderState} 阶段算好存入 state，
 * {@link #submit} 阶段只读 state。
 */
public abstract class AbstractBowlingRender<T extends AbstractBowlingProjectile, S extends BowlingRenderState> extends EntityRenderer<T, S> {


	protected final EntityModel<? super S> model;

	protected AbstractBowlingRender(EntityRendererProvider.Context context,
									EntityModel<? super S> model) {
		super(context);
		this.model = model;
	}

	/* ------------------------------------------------------------------ */
	/* extract                                                             */
	/* ------------------------------------------------------------------ */

	@Override
	public void extractRenderState(T entity, S state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
		float baseSize = this.getRenderSize(entity);
		state.renderOffsetY = this.getRenderOffsetY(entity);
		state.renderPivotY = this.getRenderPivotY(entity);

		Identifier pazId = this.getPAZId(entity);
		if (pazId != null) {
			@Nullable IClientPAZInfo info = PVZAPI.client().paz(pazId).orElse(null);
			if (info != null) {
				state.texture = info.getTexture();
				state.renderSize = baseSize * info.getRenderScale();
			} else {
				state.texture = null;
				state.renderSize = baseSize;
			}
		} else {
			state.texture = null;
			state.renderSize = baseSize;
		}
	}

	/* ------------------------------------------------------------------ */
	/* submit                                                              */
	/* ------------------------------------------------------------------ */

	@Override
	public void submit(S state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);


		if (state.texture == null) {
			return;
		}

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);

		float f = state.renderSize;
		poseStack.scale(f, f, f);


		poseStack.translate(0.0F, -state.renderOffsetY, 0.0F);

		poseStack.rotateDegrees(Axis.YP, state.yRot);


		float pivotY = state.renderPivotY;
		poseStack.translate(0.0F, pivotY, 0.0F);
		poseStack.rotateDegrees(Axis.XN, state.ageInTicks * 15.0F);
		poseStack.translate(0.0F, -pivotY, 0.0F);

		RenderType renderType = this.model.renderType(state.texture);

		collector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0);

		poseStack.popPose();
	}

	/* ------------------------------------------------------------------ */
	/* 扩展点                                                              */
	/* ------------------------------------------------------------------ */

	/**
	 * 该投掷物对应的 PAZ ID，用于从 {@link com.lying.pvz.client.registry.ClientModelRegistry}
	 * 查询纹理与缩放。
	 *
	 * <p>默认返回 {@link PVZPlants#WALL_NUT} 的 ID（旧代码固定使用 WallNut 纹理）。
	 * 不同投掷物应覆盖本方法。
	 */
	protected @Nullable Identifier getPAZId(T entity) {
		return PVZPlants.WALL_NUT.getId();
	}

	protected float getRenderSize(T entity) {
		return 1.0F;
	}

	/**
	 * 模型脚底相对模型空间的竖直偏移（单位格），用于把脚底对齐到实体原点。
	 * 默认 1.5（对应 PartPose y=24 像素）；脚底不在 24 像素的模型应覆盖。
	 */
	protected float getRenderOffsetY(T entity) {
		return 1.5F;
	}

	/**
	 * 滚动旋转的 pivot 高度（相对实体脚底，单位格）。
	 * 模型几何中心与包围盒中心不一致时子类可覆盖。
	 */
	protected float getRenderPivotY(T entity) {
		return 1.0F;
	}

	/* ------------------------------------------------------------------ */
	/* render state                                                        */
	/* ------------------------------------------------------------------ */


}