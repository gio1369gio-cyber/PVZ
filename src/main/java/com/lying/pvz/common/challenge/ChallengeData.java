package com.lying.pvz.common.challenge;

import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.common.attachment.challenge.RaiderData;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.utils.StringUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChallengeData extends SavedData {

    private static final Identifier DATA_ID = StringUtil.id("challenge_data");

    public static final SavedDataType<ChallengeData> TYPE = new SavedDataType<>(
            DATA_ID,
            ChallengeData::new,
            ChallengeData::codec,
            null
    );

    private final Map<Integer, ChallengeInstance> challenges = new HashMap<>();
    private int currentId = 1;

    /**
     * 落盘计时，与 {@link ChallengeInstance#getTick()} 无关。
     * 每 {@value #SAVE_INTERVAL} tick 强制 {@link #setDirty()} 一次，
     * 让运行中的 tick / stopTick / waitTick / currentWave / currentSpawn 进度
     * 在服务器崩溃或 stop 前有落盘机会。
     */
    private int saveTick = 0;

    private static final int SAVE_INTERVAL = 200;

    /** 新世界首次创建。 */
    public ChallengeData() {
    }

    /** 由 codec 加载旧世界时构造。 */
    private ChallengeData(ServerLevel level) {
    }

    public static ChallengeData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private static Codec<ChallengeData> codec(ServerLevel level) {
        return RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("current_id").forGetter(d -> d.currentId),
                ChallengeSnapshot.CODEC.listOf().fieldOf("challenges").forGetter(ChallengeData::getSnapshots)
        ).apply(i, (currentId, snapshots) -> {
            final ChallengeData data = new ChallengeData(level);
            data.currentId = currentId;
            for (ChallengeSnapshot snapshot : snapshots) {
                final IChallengeDefinition definition =
                        ChallengeManager.getDefinitionStatic(snapshot.definitionId());
                if (definition == null) {
                    // datapack 里已无此定义，丢弃该实例
                    continue;
                }
                final ChallengeInstance instance = new ChallengeInstance(level, snapshot, definition);
                data.challenges.put(instance.getInstanceId(), instance);
            }
            return data;
        }));
    }

    private List<ChallengeSnapshot> getSnapshots() {
        final List<ChallengeSnapshot> list = new ArrayList<>();
        for (ChallengeInstance c : this.challenges.values()) list.add(c.toSnapshot());
        return list;
    }

    /**
     * 每 tick 驱动全部挑战。
     *
     * <p>先做一遍 {@code RaiderData} 回填扫描，把区块重载后重新加载的实体按
     * {@code challenge_id} 重新挂回对应挑战的 {@code raiders} 集合，
     * 之后逐挑战调 {@link ChallengeTicker#tick(ChallengeInstance)}。
     *
     * <p>每 {@value #SAVE_INTERVAL} tick 调一次 {@link #setDirty()}，让运行进度
     * 能落盘。
     *
     * @param level 当前服务端世界。由调用方传入，{@code SavedData} 不持有 level。
     */
    public void tick(ServerLevel level) {
        this.rebuildRaiderBindings(level);

        final Iterator<Map.Entry<Integer, ChallengeInstance>> it = this.challenges.entrySet().iterator();
        while (it.hasNext()) {
            final ChallengeInstance c = it.next().getValue();
            if (c.isRemoving()) {
                it.remove();
                setDirty();
            } else {
                ChallengeTicker.tick(c);
            }
        }

        if (++this.saveTick % SAVE_INTERVAL == 0) {
            this.setDirty();
        }
    }

    /**
     * 扫描 {@code level} 内全部实体，按 {@code RaiderData.getChallengeID()} 把实体挂回挑战。
     *
     * <ul>
     *   <li>{@code id == 0}：初次未绑定，尝试在现有挑战的 {@code raiders} 集合里找匹配；
     *       找不到则标记为 {@code -1}，之后 tick 不再重复扫描。</li>
     *   <li>{@code id > 0}：已绑定但可能因区块重载丢失引用，重新挂回对应挑战。</li>
     *   <li>{@code id == -1}：已确认不属于任何挑战，跳过。</li>
     * </ul>
     */
    private void rebuildRaiderBindings(ServerLevel level) {
        for (Entity entity : level.getAllEntities()) {
            final RaiderData data = entity.getData(PvZAttachments.CHALLENGE_DATA);
            if (data.getChallengeID() == 0) {
                for (ChallengeInstance tmp : this.challenges.values()) {
                    if (tmp.getRaiders().contains(entity)) {
                        data.setChallengeID(tmp.getInstanceId());
                        break;
                    }
                }
                if (data.getChallengeID() == 0) {
                    data.setChallengeID(-1);
                }
            } else if (data.getChallengeID() != -1) {
                final ChallengeInstance match = this.matchID(data.getChallengeID());
                if (match == null) {
                    data.setChallengeID(-1);
                } else {
                    match.getRaiders().add(entity);
                }
            }
        }
    }

    /** 按 instanceId 查挑战。找不到返回 {@code null}。 */
    public ChallengeInstance matchID(int id) {
        return this.challenges.get(id);
    }

    public void addChallenge(ChallengeInstance c) {
        this.challenges.put(c.getInstanceId(), c);
        setDirty();
    }

    public int nextId() {
        setDirty();
        return this.currentId++;
    }

    public List<ChallengeInstance> getChallenges() {
        return new ArrayList<>(this.challenges.values());
    }
}