package com.lying.pvz.client.render.renderer.entity;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class PvZEntityRender<T extends Entity, S extends PvZEntityRenderState,
		M extends EntityModel<? super S>> extends EntityRenderer<T, S> {

	protected final M model;

	protected PvZEntityRender(EntityRendererProvider.Context context, M model) {
		super(context);
		this.model = model;
	}

	/* ------------------------------------------------------------------ */
	/* extract                                                             */
	/* ------------------------------------------------------------------ */

	@Override
	public void extractRenderState(T entity, S state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.scale = this.getScaleByEntity(entity);
		state.translate = this.getTranslateVec(entity);

		Identifier direct = this.getTexture(entity);
		if (direct != null) {
			state.texture = direct;
			return;
		}

		Identifier pazId = this.getPAZId(entity);
		if (pazId != null) {
			@Nullable IClientPAZInfo info = PVZAPI.client().paz(pazId).orElse(null);
			state.texture = info != null ? info.getTexture() : null;
		} else {
			state.texture = null;
		}
	}

	/* ------------------------------------------------------------------ */
	/* submit                                                              */
	/* ------------------------------------------------------------------ */

	@Override
	public void submit(S state, PoseStack poseStack,
					   SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);

		if (state.texture == null) {
			return;
		}

		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);

		float f = state.scale;
		poseStack.scale(f, f, f);

		poseStack.translate(0.0, -1.501, 0.0);

		Vec3 vec = state.translate;
		poseStack.translate(vec.x, vec.y, vec.z);

		RenderType renderType = this.model.renderType(state.texture);

		collector.submitModel(this.model, state, poseStack, renderType,
				state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0);

		poseStack.popPose();
	}

	/* ------------------------------------------------------------------ */
	/* 扩展点                                                              */
	/* ------------------------------------------------------------------ */

	/**
	 * 该实体对应的 PAZ ID，用于从 {@code ClientModelRegistry} 查询纹理。
	 * 默认返回 {@code null}（不渲染）；子类应覆盖。
	 */
	protected @Nullable Identifier getPAZId(T entity) {
		return null;
	}
	/**
	 * 直接指定纹理，绕过 PAZ 查询。返回非 null 时优先于 {@link #getPAZId}。
	 */
	protected @Nullable Identifier getTexture(T entity) {
		return null;
	}
	/**
	 * 缩放。接收实体，在 {@link #extractRenderState} 阶段调用。
	 */
	protected abstract float getScaleByEntity(T entity);

	/**
	 * 额外平移。接收实体，在 {@link #extractRenderState} 阶段调用。
	 */
	public Vec3 getTranslateVec(T entity) {
		return Vec3.ZERO;
	}


}