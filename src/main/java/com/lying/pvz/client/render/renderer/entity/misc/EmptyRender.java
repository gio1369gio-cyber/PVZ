package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.common.level.entity.MultiPartEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 空渲染器：不提交任何几何。
 *
 * <p>用于 {@link MultiPartEntity} 的父实体——实际渲染由各部件实体负责，
 * 父实体本身不需要画任何东西。
 */
public class EmptyRender<T extends MultiPartEntity> extends EntityRenderer<T, EntityRenderState> {

	public EmptyRender(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}