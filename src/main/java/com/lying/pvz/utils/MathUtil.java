package com.lying.pvz.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MathUtil {
    public static int getIncreaseAverage(int now, int len, int from, int to) {
        return from + (to - from) * now / len;
    }
    public static int getRandomMinMax(RandomSource rand, int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }


    public static int getProgressByDif(int segLen, int dif, int now, int max, int from, int to) {
        return Mth.floor(getProgressByDif(segLen, dif, now, max, from, to * 1F));
    }
    public static float getProgressByDif(int segLen, float dif, int now, int max, float from, float to) {
        if (now + segLen > max) return to;
        return from + (now - 1) / segLen * dif;
    }

    public static int getProgressAverage(int now, int max, int from, int to) {
        return Mth.floor(getProgressAverage(now, max, from, to * 1F));
    }
    public static float getProgressAverage(int now, int max, float from, float to) {
        return getProgressByDif(1, (to - from) / max, now, max, from, to);
    }


    public static float getIncreaseAverage(int now, int len, float from, float to) {
        return from + (to - from) * now / len;
    }


    public static int getRandomInRange(RandomSource rand, int range) {
        return rand.nextInt(range << 1 | 1) - range;
    }
    public static float getRandomFloat(RandomSource rand) {
        return (rand.nextFloat() - 0.5F) * 2;
    }

    public static AABB getAABBWithPos(BlockPos pos, double len) {
        return new AABB(pos.getX() + 0.5D - len, pos.getY() + 0.5D - len, pos.getZ() - len,
                pos.getX() + 0.5D + len, pos.getY() + 0.5D + len, pos.getZ() + 0.5D + len);
    }

    public static double getPosDisToVec(BlockPos pos, Vec3 vec) {
        final Vec3 now = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return vec.distanceTo(now);
    }

    public static Vec3 getHorizontalNormalizedVec(Vec3 a, Vec3 b) {
        return getHorizontalVec(a, b).normalize();
    }
    public static Vec3 getHorizontalVec(Vec3 a, Vec3 b) {
        return new Vec3(b.x - a.x, 0, b.z - a.z);
    }
    public static Vec3 toVector(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }
    public static Vec3 getVector3dBy2(double x, double z) {
        return new Vec3(x, 0, z);
    }

    /** use for render bar. */
    public static int getBarLen(int num, int maxNum, int maxLen) {
        if (maxNum <= 0 || maxLen <= 0) return 0;
        if (num <= 0) return 0;
        if (num >= maxNum) return maxLen;

        final long percent = (long) num * maxLen / maxNum;
        if (percent == 0) return 1;
        if (percent >= maxLen) return maxLen - 1;
        return (int) percent;
    }

    public static BlockPos getRandomRangePos(RandomSource rand, int range) {
        return getRandomRangePos(rand, 0, range);
    }
    public static BlockPos getRandomRangePos(RandomSource rand, int minR, int maxR) {
        final int x = (rand.nextInt(2) == 0 ? -1 : 1) * getRandomMinMax(rand, minR, maxR);
        final int z = (rand.nextInt(2) == 0 ? -1 : 1) * getRandomMinMax(rand, minR, maxR);
        return new BlockPos(x, 0, z);
    }

    public static boolean randDouble(RandomSource rand, double value) {
        return rand.nextDouble() < value;
    }

    public static boolean isInArea(int x, int y, int posX, int posY, int xLen, int yLen) {
        return x >= posX && x <= posX + xLen && y >= posY && y <= posY + yLen;
    }
}
