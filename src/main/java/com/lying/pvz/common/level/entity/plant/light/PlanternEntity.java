package com.lying.pvz.common.level.entity.plant.light;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.ILightEffect;
import com.lying.pvz.api.raid.IRaidAPI;
import com.lying.pvz.common.challenge.ChallengeManager;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.effect.OriginEffectEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.Colors;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * 路灯花。
 *
 * <p>每 {@link #EFFECT_CD} tick 给附近友方施加光源效果与夜视；super 状态下主手空手交互
 * 会在自身位置生成 {@link OriginEffectEntity} 并高亮挑战中的所有怪物。
 *
 */
public class PlanternEntity extends PlantEntity implements ILightEffect {

	private static final int EFFECT_CD = 100;

	public PlanternEntity(EntityType<? extends PlanternEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide() && this.getExistTick() % EFFECT_CD == 10) {
			this.giveLightToPlayers();
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec3d) {
		if (hand == InteractionHand.MAIN_HAND
				&& player.getItemInHand(hand).isEmpty()
				&& this.isPlantInSuperMode()) {
			if (!this.level().isClientSide()) {
				OriginEffectEntity.create(this.level(), this.blockPosition().above(), Colors.YELLOW);
				this.displayAllRaider();
				this.getSpawnSound().ifPresent(sound -> EntityUtil.playSound(this, sound));
			}
			return InteractionResult.SUCCESS;
		}
		return super.interact(player, hand, vec3d);
	}



	private void giveLightToPlayers() {
		final float range = this.getEffectRange();
		EntityUtil.getFriendlyLivings(this, EntityUtil.getEntityAABB(this, range, range))
				.forEach(entity -> {
					entity.addEffect(this.getLightEyeEffect());
					final int nightVisionTime = this.getNightVisionTime();
					if (nightVisionTime > 0) {
						entity.addEffect(EffectUtil.viewEffect(MobEffects.NIGHT_VISION, nightVisionTime, 0));
					}
				});
	}

	private void displayAllRaider() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final IRaidAPI raid = PVZAPI.raid();
		if (raid.getNearByRaid(serverLevel, this.blockPosition()).isEmpty()) {
			return;
		}
		final float range = 64F;
		serverLevel.getEntitiesOfClass(LivingEntity.class,
						EntityUtil.getEntityAABB(this, range, range),
						e -> raid.isRaider(serverLevel, e))
				.forEach(living -> living.addEffect(
						EffectUtil.viewEffect(MobEffects.GLOWING, 200, 0)));
	}

	public float getEffectRange() {
		return this.getSkillValue(SkillTypes.MORE_LIGHT_RANGE.get());
	}

	public int getNightVisionTime() {
		return (int) this.getSkillValue(SkillTypes.NIGHT_VISION.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.75F, 1.7F);
	}

	@Override
	public MobEffectInstance getLightEyeEffect() {
		return EffectUtil.viewEffect(PvZEffectRegister.LIGHT_EYE_EFFECT, this.getLightEyeTime(), 0);
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.EFFECT_TIME, this.getLightEyeTime()));
	}

	public int getLightEyeTime() {
		return 1800;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(PvZSoundRegister.PLANTERN.get());
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.PLANTERN.get();
	}
}