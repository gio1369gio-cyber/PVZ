package com.lying.pvz.utils;

import com.lying.pvz.common.init.PvZParticleRegister;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {

	/**
	 * spawn particles like zombie rising from dirt.
	 */
	public static void spawnSplash(Level level, Vec3 vec, int cnt) {
		for (int i = 0; i < cnt; ++i) {
			RandomSource rand = level.getRandom();
			level.addParticle(PvZParticleRegister.DIRT_BURST_OUT.get(), vec.x + 0.5d, vec.y, vec.z + 0.5d, (rand.nextFloat() - 0.5) / 10, 0.05d, (rand.nextFloat() - 0.5) / 10);
			level.addParticle(PvZParticleRegister.DIRT_BURST_OUT.get(), vec.x + 0.5d, vec.y, vec.z - 0.5d, (rand.nextFloat() - 0.5) / 10, 0.05d, (rand.nextFloat() - 0.5) / 10);
			level.addParticle(PvZParticleRegister.DIRT_BURST_OUT.get(), vec.x - 0.5d, vec.y, vec.z + 0.5d, (rand.nextFloat() - 0.5) / 10, 0.05d, (rand.nextFloat() - 0.5) / 10);
			level.addParticle(PvZParticleRegister.DIRT_BURST_OUT.get(), vec.x - 0.5d, vec.y, vec.z - 0.5d, (rand.nextFloat() - 0.5) / 10, 0.05d, (rand.nextFloat() - 0.5) / 10);
		}
	}
}