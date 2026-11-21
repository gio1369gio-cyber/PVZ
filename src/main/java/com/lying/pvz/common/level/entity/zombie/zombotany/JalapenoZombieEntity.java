package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.fire.JalapenoEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 火爆辣椒僵尸。
 *
 * <p>本体 44 血，行走 {@link ZombieUtil#WALK_FAST}。死亡时若非寒冷/冻结，
 * 在头顶生成一个 {@link JalapenoEntity} 并同步阵营、技能、枯萎免疫：
 * <ul>
 *   <li>{@code setImmuneToWeak(true)}：新生成的辣椒不会因地形枯萎；</li>
 *   <li>{@code setCharmed(...)}：与本体阵营保持一致（被魅惑时辣椒也翻转）；</li>
 *   <li>{@code applySkills(this.getSkills())}：完整传导技能到辣椒实体。</li>
 * </ul>
 *
 * <p>辣椒负责后续火焰范围伤害；本类本身不发射弹药。
 */
public class JalapenoZombieEntity extends AbstractZombotanyEntity {

	public JalapenoZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attribute != null) {
			attribute.setBaseValue(ZombieUtil.WALK_FAST);
		}
	}

	@Override
	public void die(DamageSource source) {
		if (!EntityUtil.isEntityCold(this) && !EntityUtil.isEntityFrozen(this)) {
			this.startBomb();
		}
		super.die(source);
	}

	public void startBomb() {
		if (!this.level().isClientSide()) {
			JalapenoEntity jalapeno = PvZEntityRegister.JALAPENO.get()
					.create(this.level(), EntitySpawnReason.TRIGGERED);
			if (jalapeno == null) {
				return;
			}
			jalapeno.setImmuneToWeak(true);
			jalapeno.setCharmed(!this.isCharmed());
			jalapeno.applySkillLevels(this.getSkillLevels());
			EntityUtil.onEntitySpawn(this.level(), jalapeno, this.blockPosition().above());
		}
	}

	public int getFireRange() {
		return 20;
	}

	@Override
	public float getLife() {
		return 44;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.JALAPENO_ZOMBIE.get();
	}
}