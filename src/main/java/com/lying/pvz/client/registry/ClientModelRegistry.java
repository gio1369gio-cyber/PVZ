package com.lying.pvz.client.registry;

import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.api.client.model.IPlantModel;
import com.lying.pvz.api.client.model.IZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 客户端模型 / 贴图 / 缩放注册表。
 *
 * <p><b>仅客户端</b>：本类 import {@code net.minecraft.client.model.geom.*}，
 * 服务端加载会触发类加载错误。仅在 {@code FMLClientSetupEvent} 之后访问。
 *
 * <p><b>为什么独立于 API 层</b>：{@link IClientPAZInfo} 只暴露纯数据（{@code renderScale} /
 * {@code texture}），模型工厂涉及 {@link ModelPart} / {@link EntityModelSet} 等客户端专用类型，
 * 由本注册表单独维护。
 */
public final class ClientModelRegistry {

    private static final Map<Identifier, IClientPAZInfo> INFOS = new ConcurrentHashMap<>();
    private static final Map<Identifier, Function<ModelPart, ? extends IPlantModel>> PLANT_MODELS =
            new ConcurrentHashMap<>();
    private static final Map<Identifier, Function<EntityModelSet, ? extends IZombieModel>> ZOMBIE_MODELS =
            new ConcurrentHashMap<>();

    private ClientModelRegistry() {
    }

    /* ------------------------------------------------------------------ */
    /* 注册                                                                */
    /* ------------------------------------------------------------------ */

    /**
     * 注册植物。
     *
     * @param id       植物类型 ID（对应 {@code IPlantType.getID()}）
     * @param scale    渲染缩放
     * @param texture  贴图路径（相对 {@code assets/<namespace>/textures/}）
     * @param factory  模型工厂（接收 {@link ModelPart}，返回 {@link IPlantModel}）
     */
    public static void registerPlant(Identifier id, float scale, Identifier texture,
                                     Function<ModelPart, ? extends IPlantModel> factory) {
        INFOS.put(id, new ClientPAZInfo(scale, texture));
        PLANT_MODELS.put(id, factory);
    }

    /**
     * 注册僵尸。
     *
     * @param id       僵尸类型 ID
     * @param scale    渲染缩放
     * @param texture  贴图路径
     * @param factory  模型工厂（接收 {@link EntityModelSet}，返回 {@link IZombieModel}）
     */
    public static void registerZombie(Identifier id, float scale, Identifier texture,
                                      Function<EntityModelSet, ? extends IZombieModel> factory) {
        INFOS.put(id, new ClientPAZInfo(scale, texture));
        ZOMBIE_MODELS.put(id, factory);
    }

    /* ------------------------------------------------------------------ */
    /* 查询                                                                */
    /* ------------------------------------------------------------------ */

    public static Optional<IClientPAZInfo> getInfo(Identifier id) {
        return Optional.ofNullable(INFOS.get(id));
    }

    public static Optional<Function<ModelPart, ? extends IPlantModel>> getPlantModel(Identifier id) {
        return Optional.ofNullable(PLANT_MODELS.get(id));
    }

    public static Optional<Function<EntityModelSet, ? extends IZombieModel>> getZombieModel(Identifier id) {
        return Optional.ofNullable(ZOMBIE_MODELS.get(id));
    }

    /* ------------------------------------------------------------------ */
    /* 内部实现                                                            */
    /* ------------------------------------------------------------------ */

    private record ClientPAZInfo(float renderScale, Identifier texture) implements IClientPAZInfo {
        @Override
        public float getRenderScale() {
            return this.renderScale;
        }

        @Override
        public Identifier getTexture() {
            return this.texture;
        }
    }
}