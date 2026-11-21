package com.lying.pvz.common.level.block.others;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.block.AbstractFacingBlock;
import com.lying.pvz.common.list.PlantItemList;
import com.lying.pvz.common.list.ZombieList;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class QuestionMarkPotBlock extends AbstractFacingBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4.0, 0.0, 4.0, 12.0, 1.0, 12.0),
            Block.box(3.0, 1.0, 3.0, 13.0, 10.0, 13.0),
            Block.box(4.0, 10.0, 4.0, 12.0, 12.0, 12.0),
            Block.box(3.0, 12.0, 3.0, 13.0, 14.0, 13.0));

    public QuestionMarkPotBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {

        RandomSource random = RandomSource.create();
        IZombieType zombieType = ZombieList.ZOMBIE.getRandomItem(random).orElse(null);
        ItemStack plant = PlantItemList.PLANT_ITEM.getRandomItem(random).get().getDefaultInstance();
        ItemStack pot = PvZItemRegister.POT_GRASS_CARD.get().getDefaultInstance();

        if (!level.isClientSide() && player.getMainHandItem().getItem()==PvZItemRegister.HAMMER.get()) {
            level.removeBlock(pos, false);
            //僵尸
            Mob zombie = null;
            if (zombieType != null) {
                zombie = zombieType.getEntityType().get().create(level, EntitySpawnReason.SPAWNER);
            }
            if (random.nextInt(2) == 0) {
                if (zombie != null) {
                    EntityUtil.onEntitySpawn(level, zombie, pos);
                }
            } else if(random.nextInt(10)==0){
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(pot.getItem())));
            }else {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(plant.getItem())));
            }
        }
        //扣除耐久
        player.getMainHandItem().hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return PathType.FENCE;
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
