package com.lying.pvz.common.level.item.bullet;

import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 鞭炮（Fire Cracker）——投掷型子弹道具。
 *
 * <p>主手右键，沿玩家视线方向发射一发 {@link BulletKinds#FIRE_CRACKER}，
 * 造成 4 点伤害。播放雪球投掷音效。
 *
 * <p>消耗（仅非创造模式）：消耗 1 个物品 + 10 tick 冷却。
 */
public class FireCrackerItem extends Item {

	/** 使用后的冷却（tick）。 */
	private static final int CD = 10;

	public FireCrackerItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level instanceof ServerLevel serverLevel && hand == InteractionHand.MAIN_HAND) {
			final Vec3 vec = player.getLookAngle();
			final Vec3 pos = new Vec3(
					player.getX() + vec.x,
					player.getY() + player.getEyeHeight() + vec.y,
					player.getZ() + vec.z);

			final ShotSpec spec = new ShotSpec().baseDamage(4.0F);
			BulletLauncher.launch(serverLevel, PvZEntityRegister.FIRE_CRACKER.get(), player,
					EntityUtil.getEntityGroup(player), BulletKinds.FIRE_CRACKER, spec,
					pos, null, vec);

			EntityUtil.playSound(player, SoundEvents.SNOWBALL_THROW);
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
			player.getCooldowns().addCooldown(this.getDefaultInstance(), CD);
		}
		return InteractionResult.SUCCESS;
	}
}
