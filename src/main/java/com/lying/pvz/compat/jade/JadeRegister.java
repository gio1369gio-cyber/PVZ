package com.lying.pvz.compat.jade;


import com.lying.pvz.PvZMod;
import com.lying.pvz.compat.jade.provider.PvZEntityProvider;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.ConfigIcon;

@WailaPlugin(PvZMod.MOD_ID)
public class JadeRegister implements IWailaPlugin {

	public static final Identifier CONFIG_SHOW_DEFENCE_HEALTH = StringUtil.id("show_defence_health");
	public static final Identifier CONFIG_SHOW_OWNER = StringUtil.id("show_owner");
	public static final Identifier CONFIG_SHOW_SKILLS = StringUtil.id("show_skills");

	@Override
	public void register(IWailaCommonRegistration registration) {
		// 如果你的防御生命值等数据需要从服务端同步，可以在这里注册数据提供者。
		// 如果所有数据客户端都能直接读取，可以留空。
		// registration.registerEntityDataProvider(PvZEntityProvider.INSTANCE, Entity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		// 注册客户端实体组件显示
		registration.registerEntityComponent(PvZEntityProvider.INSTANCE, Entity.class);

		// 添加配置选项
		registration.addConfig(CONFIG_SHOW_DEFENCE_HEALTH, ConfigIcon.none(), true);
		registration.addConfig(CONFIG_SHOW_OWNER, ConfigIcon.none(),true);
		registration.addConfig(CONFIG_SHOW_SKILLS, ConfigIcon.none(),true);

	}
}