package com.lying.pvz.compat.modonomicon.datagen.entry.weapon;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.compat.modonomicon.datagen.entry.BaseGuideEntry;

/**
 * "Resource Collector" entry。
 */
public class ResourceCollectorEntry extends BaseGuideEntry {

    public ResourceCollectorEntry(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.spotlightPage("collector1", PvZItemRegister.RESOURCE_COLLECTOR,
                "Resource Collector", "资源收集器",
                "$(byellow)Resource Collector$() is a tool used to collect various resources. $(l)Normally aim at the entity and right click to collect it, or right click while shifting to collect a whole range of resource$().",
                "$(byellow)资源收集器$()是一种用于收集各种资源的工具。$(l)通常对准实体右键点击即可收集，或按住 Shift 右键点击收集整个范围内的资源$()。");

        this.craftingPage("collector2", "pvz:resource_collector", null,
                "Resource Collector Recipe", "资源收集器配方", null, null);

        this.addZhEntryName("资源收集器");
    }

    @Override
    protected String entryName() {
        return "Resource Collector";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(PvZItemRegister.RESOURCE_COLLECTOR);
    }

    @Override
    protected String entryId() {
        return "resource_collector";
    }
}
