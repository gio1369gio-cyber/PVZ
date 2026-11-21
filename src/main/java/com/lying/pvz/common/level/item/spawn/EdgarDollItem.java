package com.lying.pvz.common.level.item.spawn;


import net.minecraft.world.item.Item;

public class EdgarDollItem extends Item {

	public EdgarDollItem(Properties props) {
		super(props);
	}
	
//	@Override
//	public ActionResultType useOn(ItemUseContext context) {
//		final PlayerEntity player = context.getPlayer();
//		final World world = context.getLevel();
//		final BlockPos pos = context.getClickedPos();
//		if(! world.isClientSide && ! player.getCooldowns().isOnCooldown(this) && context.getClickedFace() == Direction.UP) {
//			if(this.canSpawnHere(world, pos)) {
//				Edgar090505Entity zomboss = EntityRegister.EDGAR_090505.get().create(world);
//				EntityUtil.onEntitySpawn(world, zomboss, pos.above());
//				context.getItemInHand().shrink(1);
//			}
//		}
//		return super.useOn(context);
//	}

//@Override
//public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
//	super.appendHoverText(stack, context, tooltip, flagIn);
//	tooltip.add(Component.translatable("tooltip.pvz.wait_for_update").withStyle(ChatFormatting.RED));

//	private boolean canSpawnHere(World world, BlockPos pos) {
//		for(int i = - 5; i <= 5; ++ i) {
//			for(int j = - 5; j <= 5; ++ j) {
//				for(int k = 1; k <= 12; ++ k) {
//					BlockPos tmp = pos.offset(i, k, j);
//					if(! world.getBlockState(tmp).isAir(world, pos)) {
//						return false;
//					}
//				}
//			}
//		}
//		return world.getBlockState(pos).canOcclude();
//	}

}
