package com.lying.pvz.common.level.item.spawn;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.misc.GardenRakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * 园艺耙（Garden Rake）放置道具。
 *
 * <p><b>功能</b>：对空中方块顶面右键，在点击方块或点击面偏移处生成一个
 * {@link GardenRakeEntity}，绑定放置者并消耗一个物品（创造模式不消耗）。
 *
 * <p><b>放置条件</b>（任一不满足则 {@link InteractionResult#FAIL}，不消耗物品）：
 * <ul>
 *   <li>点击面必须是方块顶面（{@link Direction#UP}）</li>
 *   <li>点击方块上方一格必须为空（{@link Level#isEmptyBlock}）</li>
 *   <li>实体生成成功（{@code EntityType#spawn} 返回非 {@code null}）</li>
 * </ul>
 *
 * <p><b>生成位置</b>：若点击方块自身有碰撞箱，则生成在 {@code pos.relative(clickedFace)}；
 * 否则生成在 {@code pos} 本身。
 *
 * <p><b>服务端 / 客户端</b>：生成实体与消耗物品只在服务端执行；
 * 客户端返回 {@link InteractionResult#SUCCESS} 以触发挥手。
 */
public class GardenRakeItem extends Item {

	public GardenRakeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}

		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();

		if (context.getClickedFace() != Direction.UP || !level.isEmptyBlock(pos.above())) {
			return InteractionResult.FAIL;
		}

		BlockPos spawnPos = pos;
		if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
			spawnPos = pos.relative(context.getClickedFace());
		}

		if (!level.isClientSide()) {
			GardenRakeEntity entity = PvZEntityRegister.GARDEN_RAKE.get().spawn(
					(ServerLevel) level, stack, player, spawnPos,
					EntitySpawnReason.SPAWNER, true, true);
			if (entity == null) {
				PvZMod.LOGGER.error("GardenRakeItem: failed to spawn GardenRakeEntity at {}", spawnPos);
				return InteractionResult.FAIL;
			}
			entity.setPlacer(player);
			entity.summonByOwner(player);
			if (!player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
		}
		return InteractionResult.SUCCESS;
	}
}