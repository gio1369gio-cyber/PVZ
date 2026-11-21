package com.lying.pvz.client.gui.screen;

import com.lying.pvz.client.OverlayPositionStore;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/**
 * 资源条覆盖层位置调整界面。
 *
 * <p>参考 Peculiar 的 {@code LyricsPositionScreen}：在游戏中显示一个与资源条等大的矩形，
 * 鼠标拖拽矩形即可改变资源条左上角在屏幕上的归一化位置；ESC / 关闭时写入
 * {@link OverlayPositionStore} 并回到父界面。游戏在此界面打开时不会暂停，便于实时观察效果。
 *
 * <p>只调整位置，不调整大小：矩形尺寸固定对应渲染代码缩放后的资源条尺寸。
 */
public class OverlayPositionScreen extends Screen {

    /** 资源条渲染时 scale=0.7F，宽度 157px → 约 110px；高度取等级条 34px → 约 24px。 */
    private static final int BAR_W = 110;
    private static final int BAR_H = 24;

    private final Screen parent;
    private float anchorX;
    private float anchorY;
    private boolean dragging = false;
    private double dragOffsetX;
    private double dragOffsetY;

    public OverlayPositionScreen(Screen parent) {
        super(Component.translatable("config.pvz.overlay.position"));
        this.parent = parent;
        this.anchorX = OverlayPositionStore.x();
        this.anchorY = OverlayPositionStore.y();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // 保留背后的游戏画面，便于实时观察资源条位置
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float a) {
        super.extractRenderState(g, mouseX, mouseY, a);

        Component hint = Component.translatable("config.pvz.overlay.position.hint");
        g.centeredText(this.font, hint, this.width / 2, 20, -1);

        final int hx = Math.round(this.anchorX * (float) this.width);
        final int hy = Math.round(this.anchorY * (float) this.height);
        final int left = hx;
        final int right = hx + BAR_W;
        final int top = hy;
        final int bottom = hy + BAR_H;

        // 资源条预览区域（半透明填充 + 高亮边框），尺寸固定，仅位置随锚点变化
        g.fill(left, top, right, bottom, 0x60000000);
        g.fill(left, top, right, top + 2, -1593835521);
        g.fill(left, bottom - 2, right, bottom, -1593835521);
        g.fill(left, top, left + 2, bottom, -1593835521);
        g.fill(right - 2, top, right, bottom, -1593835521);

        // 锚点手柄
        g.fill(hx - 3, hy - 3, hx + 3, hy + 3, -53200);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick)) {
            return true;
        }
        int button = event.buttonInfo().button();
        if (button == 0 || button == 1) {
            double mx = event.x();
            double my = event.y();
            int hx = Math.round(this.anchorX * (float) this.width);
            int hy = Math.round(this.anchorY * (float) this.height);
            if (mx >= hx && mx <= hx + BAR_W && my >= hy && my <= hy + BAR_H) {
                this.dragging = true;
                this.dragOffsetX = mx - hx;
                this.dragOffsetY = my - hy;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (this.dragging) {
            double mx = event.x();
            double my = event.y();
            this.anchorX = (float) Math.max(0.0, Math.min(1.0, (mx - this.dragOffsetX) / (double) this.width));
            this.anchorY = (float) Math.max(0.0, Math.min(1.0, (my - this.dragOffsetY) / (double) this.height));
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        int button = event.buttonInfo().button();
        if ((button == 0 || button == 1) && this.dragging) {
            this.dragging = false;
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public void onClose() {
        OverlayPositionStore.set(this.anchorX, this.anchorY);
        this.minecraft.gui.setScreen(this.parent);
    }
}
