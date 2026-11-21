package com.lying.pvz.common.level.entity.npc;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.ability.IHasGroup;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TransactionType;
import com.lying.pvz.common.datapack.TransactionTypeLoader;
import com.lying.pvz.common.init.PVZEntityDataSerializers;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.transaction.TransactionManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Dave 系列 NPC 的基类。
 *
 * <p>商品/交易的全部状态由 {@link TransactionManager} 维护，
 * 本类只负责：
 * <ul>
 *   <li>tick 驱动（委托 manager）</li>
 *   <li>交互（{@link #interact} / {@link #openContainer}）</li>
 *   <li>同步数据（GOODS / NEVER_REFRESHED / NEXT_REFRESH_AT）</li>
 *   <li>存档（ValueInput/ValueOutput）</li>
 *   <li>顾客状态、AI、声音、传送、死亡等杂项</li>
 * </ul>
 */
public abstract class AbstractDaveEntity extends PathfinderMob implements IHasGroup {

	private static final EntityDataAccessor<CompoundTag> GOODS =
			SynchedEntityData.defineId(AbstractDaveEntity.class, PVZEntityDataSerializers.COMPOUND_TAG.get());
	private static final EntityDataAccessor<Boolean> NEVER_REFRESHED =
			SynchedEntityData.defineId(AbstractDaveEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Long> NEXT_REFRESH_AT =
			SynchedEntityData.defineId(AbstractDaveEntity.class, EntityDataSerializers.LONG);

	/** 该 Dave 使用的交易类型资源 ID；null 表示"无交易配置"。 */
	@Nullable
	protected Identifier transactionResource = null;

	@Nullable
	private Player customer;

	private final TransactionManager transactionManager = new TransactionManager(this);

	public AbstractDaveEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
		this.refreshDimensions();
	}

	/* ============================================================ 同步 ============================================================ */

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GOODS, new CompoundTag());
		builder.define(NEVER_REFRESHED, true);
		builder.define(NEXT_REFRESH_AT, 0L);
	}

	public void setGoods(CompoundTag tag) {
		this.entityData.set(GOODS, tag);
	}

	public CompoundTag getGoods() {
		return this.entityData.get(GOODS);
	}

	public void setNeverRefreshed(boolean b) {
		this.entityData.set(NEVER_REFRESHED, b);
	}

	public boolean isNeverRefreshed() {
		return this.entityData.get(NEVER_REFRESHED);
	}

	public void setNextRefreshAt(long tick) {
		this.entityData.set(NEXT_REFRESH_AT, tick);
	}

	public long getNextRefreshAt() {
		return this.entityData.get(NEXT_REFRESH_AT);
	}

	/* ============================================================ 交易 ============================================================ */

	public TransactionManager transactions() {
		return this.transactionManager;
	}

	/**
	 * 运行时切换交易类型。会立即清空当前商品并触发下一 tick 重新生成。
	 *
	 * <p>构造器 / 读档恢复请走 {@link #restoreTransaction(Identifier)}，它不会重置刷新状态。
	 */
	public void setTransactionResource(@Nullable Identifier res) {
		this.transactionResource = res;
		this.transactionManager.changeType(
				res == null ? null : TransactionTypeLoader.getTransaction(res));
	}

	/**
	 * 从存档 / 构造器恢复交易类型。不重置 {@code neverRefreshed / nextRefreshAt}。
	 *
	 * <p>{@code res == null} 表示显式清空；{@code res} 非空但 type 尚未就绪时
	 * （客户端未同步 datapack / 早期加载），保留当前 type，不清成 null。
	 */
	protected void restoreTransaction(@Nullable Identifier res) {
		this.transactionResource = res;
		if (res == null) {
			this.transactionManager.restoreType(null);
			return;
		}
		TransactionType t = TransactionTypeLoader.getTransaction(res);
		if (t != null) {
			this.transactionManager.restoreType(t);
		} else {
			PvZMod.LOGGER.warn("Transaction type not available for {}, keeping current type", res);
		}
	}

	/** 强制立刻重建商店（清空 + 重新生成 + 重置刷新计时）。 */
	public void refreshTransactions() {
		this.transactionManager.refresh();
	}

	/** 清空当前商品（下一次刷新仍按 {@code nextRefreshAt} 决定）。 */
	public void clearTransaction() {
		this.transactionManager.clear();
	}

	/** 顾客已购买 pos 位置的一次商品。 */
	public void sellGoodForTransactions(int pos) {
		this.transactionManager.sell(pos);
	}

	/** 兼容旧签名：按内容查 pos 后售卖。 */
	public void sellGoodForTransactions(GoodType good) {
		for (Map.Entry<Integer, GoodType> e : this.transactionManager.currentGoods().entrySet()) {
			if (e.getValue().equals(good)) {
				this.transactionManager.sell(e.getKey());
				return;
			}
		}
		PvZMod.LOGGER.warn("Tried to sell good not in current list: {}", good);
	}

	/** 展示顺序的商品列表（值集合视图）。 */
	public List<GoodType> getGoodList() {
		return new ArrayList<>(this.transactionManager.currentGoods().values());
	}

	/** pos → GoodType。GUI 推荐使用。 */
	public Map<Integer, GoodType> getGoodMap() {
		return this.transactionManager.currentGoods();
	}

	/* ============================================================ tick / AI ============================================================ */

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(3, new LookAtCustomerGoal(this));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.25D) {
			@Override
			public boolean canUse() {
				if (this.mob instanceof AbstractDaveEntity dave) {
					return super.canUse() && dave.getCustomer() == null;
				}
				return super.canUse();
			}
		});
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.transactionManager.tick();
		}
	}

	/* ============================================================ 交互 ============================================================ */

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (this.canOpenShop(player, player.getItemInHand(hand))) {
			if (!this.level().isClientSide() && player instanceof ServerPlayer serverPlayer && !this.hasCustomer()) {
				this.openContainer(serverPlayer);
				this.setCustomer(player);
			}
			return InteractionResult.SUCCESS;
		}
		return super.interact(player, hand, location);
	}

	/** 由子类实现：如何向玩家打开交易菜单。 */
	protected abstract void openContainer(ServerPlayer player);

	protected boolean canOpenShop(Player player, ItemStack heldItem) {
		return heldItem.isEmpty();
	}

	/* ============================================================ 顾客 ============================================================ */

	public void setCustomer(@Nullable Player player) {
		this.customer = player;
	}

	@Nullable
	public Player getCustomer() {
		return this.customer;
	}

	public boolean hasCustomer() {
		return this.customer != null;
	}

	protected void resetCustomer() {
		this.setCustomer(null);
	}

	@Nullable
	@Override
	public Entity teleport(TeleportTransition transition) {
		this.resetCustomer();
		return super.teleport(transition);
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		this.resetCustomer();
	}

	/* ============================================================ 其它 ============================================================ */

	@Override
	public int getAmbientSoundInterval() {
		return 200;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.scalable(0.9f, 2.6f);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return PvZSoundRegister.DAVE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return PvZSoundRegister.DAVE_SCREAM.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return PvZSoundRegister.CRAZY_SAY.get();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}

	@Override
	public GroupType getEntityGroupType() {
		return GroupType.NEUTRALS;
	}

	/* ============================================================ 存档 ============================================================ */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		// 1. 先恢复交易类型引用（仅成功拿到 type 时才覆盖，失败保留构造器默认值）
		String res = input.getStringOr("transaction_res", "");
		if (!res.isEmpty()) {
			Identifier id = Identifier.tryParse(res);
			if (id != null) {
				this.restoreTransaction(id);
			} else {
				PvZMod.LOGGER.warn("Dave has malformed transaction_res: {}", res);
			}
		}
		// res 为空：保留构造器默认交易类型，不再 restoreTransaction(null) 清空

		// 2. 恢复商品状态（load 内部会 sync，把状态写回 entityData 以便同步客户端）
		CompoundTag goods = input.read("goods_nbt", CompoundTag.CODEC).orElseGet(CompoundTag::new);
		boolean never = input.getBooleanOr("never_refreshed", true);
		long nextAt = input.getLongOr("next_refresh_at", 0L);
		this.transactionManager.load(goods, never, nextAt);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.store("goods_nbt", CompoundTag.CODEC, this.getGoods());
		output.putBoolean("never_refreshed", this.isNeverRefreshed());
		output.putLong("next_refresh_at", this.getNextRefreshAt());
		if (this.transactionResource != null) {
			output.putString("transaction_res", this.transactionResource.toString());
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (this.level().isClientSide()
				&& (GOODS.equals(key) || NEVER_REFRESHED.equals(key) || NEXT_REFRESH_AT.equals(key))) {
			this.transactionManager.load(
					this.getGoods(),
					this.isNeverRefreshed(),
					this.getNextRefreshAt());
		}
	}

	/* ============================================================ 顾客朝向 ============================================================ */

	public static class LookAtCustomerGoal extends LookAtPlayerGoal {
		private final AbstractDaveEntity dave;

		public LookAtCustomerGoal(AbstractDaveEntity dave) {
			super(dave, Player.class, 8.0F);
			this.dave = dave;
		}

		@Override
		public boolean canUse() {
			if (this.dave.hasCustomer()) {
				this.lookAt = this.dave.getCustomer();
				return true;
			}
			return false;
		}
	}
}