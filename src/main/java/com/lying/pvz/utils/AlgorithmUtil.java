package com.lying.pvz.utils;

import java.util.Comparator;

import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;

public class AlgorithmUtil {
    /** 钢南瓜位。PAZState 枚举占 ordinal 0~11，这里用 12 避开 LOST_HEAD（bit 5）。 */
    public final static int STEEL_PUMPKIN_FLAG = 12;


    public static class BitOperator {
        public static int setBit(int value, int pos, boolean flag) {
            if (flag) return setBitOne(value, pos);
            return setBitZero(value, pos);
        }
        public static boolean hasBitOne(int value, int pos) {
            return ((value >> pos) & 1) == 1;
        }
        private static int setBitOne(int value, int pos) { return value | (1 << pos); }
        private static int setBitZero(int value, int pos) {
            if (hasBitOne(value, pos)) return value - (1 << pos);
            return value;
        }
    }

    public static class EntitySorter implements Comparator<Entity> {
        private final Entity entity;
        public EntitySorter(Entity entityIn) { this.entity = entityIn; }
        public int compare(Entity a, Entity b) {
            double d0 = this.entity.distanceToSqr(a);
            double d1 = this.entity.distanceToSqr(b);
            if (d0 < d1) return -1;
            return d0 > d1 ? 1 : 0;
        }
    }

    public static class PairSorter<T> implements Comparator<Pair<T, Integer>> {
        @Override
        public int compare(Pair<T, Integer> p1, Pair<T, Integer> p2) {
            return p1.getSecond() > p2.getSecond() ? -1 : p1.getSecond().equals(p2.getSecond()) ? 0 : 1;
        }
    }

    public static class KMP {
        private static int calcMatchValue(String subStr) {
            int length = subStr.length();
            String preFixStr = subStr.substring(0, length - 1);
            String suffFixStr = subStr.substring(1);
            while (preFixStr.length() > 0 && suffFixStr.length() > 0) {
                if (preFixStr.equals(suffFixStr)) return preFixStr.length();
                if (preFixStr.length() == 1 && suffFixStr.length() == 1) break;
                preFixStr = preFixStr.substring(0, preFixStr.length() - 1);
                suffFixStr = suffFixStr.substring(1);
            }
            return 0;
        }
        private static int[] createPartialMatchTable(String pattern) {
            int patternLen = pattern.length();
            int[] matchTable = new int[patternLen];
            int i = 0, matchValue = 0;
            while (i < patternLen) {
                if (i == 0) matchValue = 0;
                else matchValue = calcMatchValue(pattern.substring(0, i + 1));
                matchTable[i] = matchValue;
                i++;
            }
            return matchTable;
        }
        public static boolean kmp(String target, String pattern) {
            int[] partialMatchTable = createPartialMatchTable(pattern);
            char[] targetCharArr = target.toCharArray();
            char[] patterncharArr = pattern.toCharArray();
            int matchCharCounts = 0, i = 0, j = 0, moveCounts = 0;
            while (i < targetCharArr.length) {
                if (targetCharArr[i] == patterncharArr[j]) {
                    matchCharCounts++; i++; j++;
                } else if (j == 0) {
                    i++;
                } else {
                    moveCounts = matchCharCounts - partialMatchTable[j - 1];
                    j = j - moveCounts;
                    matchCharCounts = matchCharCounts - moveCounts;
                }
                if (j == patterncharArr.length) return true;
            }
            return false;
        }
    }

    public static boolean getSteelPumpkin(PlantEntity plant) {
        return BitOperator.hasBitOne(plant.getPAZState(), STEEL_PUMPKIN_FLAG);
    }

    public static void setSteelPumpkin(PlantEntity plant,boolean flag) {
        plant.setPAZState(BitOperator.setBit(plant.getPAZState(), STEEL_PUMPKIN_FLAG, flag));
    }
}