package com.lying.pvz.common.level.entity.plant;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.paz.PlantTrait;
import com.lying.pvz.api.entity.plant.IPlantEntity;
import com.lying.pvz.api.entity.plant.IPlantInfo;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 植物内层 / 外层信息的数据载体。
 *
 * <p>不是实体，不参与 tick。内层 {@code innerPlant} 与外层 {@code outerPlant}
 * 都是本类实例（外层可能由具体子类如 {@code PumpkinInfo} 覆盖 read/write）。
 *
 * <p><b>外层植物的读取契约</b>：{@link IPlantType#getOuterPlantInfo()} 返回
 * {@link Supplier}，每次 {@code get()} 必须返回新实例（见 {@code PlantTypeBuilder}）。
 * 因此 {@link #read} 在外层分支必须重新调用 {@code get()} 而非复用旧实例，
 * 并处理 Supplier 返回 null 或抛异常的情况。
 */
public class PlantInfo implements IPlantInfo {

    protected IPlantType type;
    protected int sunCost;
    protected boolean superState;

    public PlantInfo() {
    }

    public PlantInfo(IPlantType type) {
        this.type = type;
    }

    @Override
    public void placeOn(IPlantEntity plantEntity, int sunCost) {
        this.setSunCost(sunCost);
        if (plantEntity instanceof Entity entity) {
            EntityUtil.playSound(entity, PvZSoundRegister.PLACE_PLANT_GROUND.get());
        }
    }

    @Override
    public void onSuper(IPlantEntity plantEntity) {
        this.markSuper();
    }

    @Override
    public boolean isSuper() {
        return this.superState;
    }

    @Override
    public void markSuper() {
        this.superState = true;
    }

    /**
     * 从实体 NBT 中读取植物信息，返回解析后的实例。
     *
     * <p>外层植物走 {@link IPlantType#getOuterPlantInfo()} 的 Supplier 取新实例，
     * 并做三层防御：
     * <ol>
     *   <li>类型声明为外层但 Supplier 缺失（{@code IllegalStateException}）→ 返回 {@code current}</li>
     *   <li>Supplier 返回 {@code null} → 返回 {@code current}</li>
     *   <li>Supplier 正常返回 → {@code setType} 后 {@code read}</li>
     * </ol>
     */
    public static IPlantInfo read(IPlantInfo current, CompoundTag compound, String flag) {
        if (!compound.contains(flag)) {
            return current;
        }
        final CompoundTag nbt = compound.getCompoundOrEmpty(flag);
        if (!nbt.contains("plant_type")) {
            return current;
        }
        final String string = nbt.getStringOr("plant_type", "");
        final Identifier id = Identifier.tryParse(string);
        if (id == null) {
            return current;
        }
        final Optional<IPlantType> op = PVZAPI.lookup().plant(id);
        if (op.isEmpty()) {
            return current;
        }

        final IPlantType plantType = op.get();
        final IPlantInfo result;
        if (plantType.hasTrait(PlantTrait.OUTER)) {
            final Supplier<IPlantInfo> supplier;
            try {
                supplier = plantType.getOuterPlantInfo();
            } catch (IllegalStateException ex) {
                return current;
            }
            if (supplier == null) {
                return current;
            }
            final IPlantInfo outer = supplier.get();
            if (outer == null) {
                return current;
            }
            outer.setType(plantType);
            result = outer;
        } else {
            result = new PlantInfo(plantType);
        }
        result.read(nbt);
        return result;
    }

    public static void write(IPlantInfo info, CompoundTag compound, String flag) {
        if (info == null) {
            return;
        }
        final CompoundTag nbt = new CompoundTag();
        info.write(nbt);
        compound.put(flag, nbt);
    }

    @Override
    public void read(CompoundTag nbt) {
        if (nbt.contains("sun_cost")) {
            this.setSunCost(nbt.getIntOr("sun_cost", 0));
        }
        if (nbt.contains("super_state")) {
            this.superState = nbt.getBooleanOr("super_state", false);
        }
    }

    @Override
    public void write(CompoundTag nbt) {
        // type 未初始化时不写，避免 NPE；读回时无 plant_type 会走 current 分支
        if (this.type != null) {
            nbt.putString("plant_type", this.type.getID().toString());
        }
        nbt.putInt("sun_cost", this.getSunCost());
        nbt.putBoolean("super_state", this.superState);
    }

    @Override
    public void setType(IPlantType type) {
        this.type = type;
    }

    @Override
    public IPlantType getType() {
        return this.type;
    }

    @Override
    public void setSunCost(int cost) {
        this.sunCost = cost;
    }

    @Override
    public int getSunCost() {
        return this.sunCost;
    }

    @Override
    public void onHeal(IPlantEntity plantEntity, float percent) {
    }
}