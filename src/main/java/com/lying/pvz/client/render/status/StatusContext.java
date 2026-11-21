package com.lying.pvz.client.render.status;

import com.lying.pvz.api.projectile.Mark;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * 状态视觉判定上下文。由两个通用 render state 提供。
 */
public record StatusContext(
        boolean cold,
        boolean frozen,
        boolean buttered,
        List<Mark.View> marks,
        float ageInTicks
) {

    public boolean hasMark(Identifier mark) {
        for (Mark.View view : this.marks) {
            if (view.mark().equals(mark)) {
                return true;
            }
        }
        return false;
    }
}
