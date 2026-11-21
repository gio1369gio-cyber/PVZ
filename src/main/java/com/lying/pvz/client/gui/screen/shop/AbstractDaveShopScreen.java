package com.lying.pvz.client.gui.screen.shop;

import com.lying.pvz.api.transaction.GoodKind;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.client.gui.GuiHandler;
import com.lying.pvz.client.gui.screen.PvZContainerScreen;
import com.lying.pvz.common.menu.shop.AbstractDaveShopMenu;
import com.lying.pvz.common.payload.ClickButtonPayload;
import com.lying.pvz.common.transaction.goodkind.EnergyGood;
import com.lying.pvz.common.transaction.goodkind.MoneyGood;
import com.lying.pvz.common.transaction.goodkind.SlotGood;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.Colors;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Dave 系列商店屏幕。
 *
 * <p><b>核心不变量</b>：
 * <ul>
 *   <li>选中项以 {@link AbstractDaveShopMenu.GoodEntry#key()} 标识，<b>不是</b>列表位置；
 *       商品被购买/重新排序后选中项自动转移到当前列表中的有效项。</li>
 *   <li>购买按钮发出的 {@code ClickButtonPayload} 第三参数为 <b>商品 key</b>，
 *       与 {@link AbstractDaveShopMenu#clickMenuButton} 的 {@code key} 一一对应。</li>
 *   <li>每帧数据（{@code frameGoods / downHeight / selectedKey}）在
 *       {@link #extractBackgroundTexture} 开头统一刷新；滚动条与条目内容使用同一帧快照。</li>
 * </ul>
 */
public abstract class AbstractDaveShopScreen extends PvZContainerScreen<AbstractDaveShopMenu> {

    private static final Identifier TEXTURE = StringUtil.id("textures/gui/container/shop.png");
    public static final int TRADE_NUM_PER_PAGE = 8;

    private static final int TEX_WIDTH = 512;
    private static final int TEX_HEIGHT = 256;

    protected final TradeButton[] trades = new TradeButton[TRADE_NUM_PER_PAGE];
    protected Button buyButton;

    /** 当前滚动的行偏移（绝对行索引）。 */
    private int downHeight = 0;

    /** 选中商品的稳定 key；null = 无选中。 */
    private @Nullable Integer selectedKey = null;

    /** 本帧的商品快照。每帧在 {@link #refreshFrame()} 刷新一次。 */
    private List<AbstractDaveShopMenu.GoodEntry> frameGoods = List.of();

    public AbstractDaveShopScreen(AbstractDaveShopMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 285, 195);
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < TRADE_NUM_PER_PAGE; ++i) {
            this.trades[i] = this.addRenderableWidget(new TradeButton(
                    this.leftPos + 5, this.topPos + 27 + 20 * i, i, (button) -> {
                if (button instanceof TradeButton trade) {
                    final int idx = this.downHeight + trade.getId();
                    if (idx >= 0 && idx < this.frameGoods.size()) {
                        this.selectedKey = this.frameGoods.get(idx).key();
                    }
                }
            }));
        }

        this.buyButton = this.addRenderableWidget(
                Button.builder(
                        Component.translatable("gui.pvz.dave_shop.buy"),
                        (button) -> {
                            if (this.buyButton.visible && this.selectedKey != null) {
                                ClientPacketDistributor.sendToServer(
                                        new ClickButtonPayload(this.getShopID(), 0, this.selectedKey));
                            }
                        }
                ).bounds(this.leftPos + 206, this.topPos + 85, 18, 18).build()
        );
        this.buyButton.visible = false;
    }

    // ============================================================ 每帧快照 ============================================================

    /**
     * 刷新本帧的商品快照、滚动偏移、选中项。必须在 {@link #extractBackgroundTexture} 开头调用。
     */
    private void refreshFrame() {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            this.frameGoods = List.of();
            this.selectedKey = null;
            this.downHeight = 0;
            return;
        }

        this.frameGoods = this.menu.getDisplayGoods(player);

        final int size = this.frameGoods.size();
        this.downHeight = Mth.clamp(this.downHeight, 0, Math.max(0, size - TRADE_NUM_PER_PAGE));

        // selectedKey 失效 → 自动重选为当前可见范围内的第一项
        if (this.selectedKey == null
                || this.frameGoods.stream().noneMatch(e -> e.key() == this.selectedKey)) {
            if (size == 0) {
                this.selectedKey = null;
            } else {
                final int idx = Mth.clamp(this.downHeight, 0, size - 1);
                this.selectedKey = this.frameGoods.get(idx).key();
            }
        }
    }

    private @Nullable AbstractDaveShopMenu.GoodEntry selectedEntry() {
        if (this.selectedKey == null) return null;
        for (AbstractDaveShopMenu.GoodEntry e : this.frameGoods) {
            if (e.key() == this.selectedKey) return e;
        }
        return null;
    }

    // ============================================================ 渲染 ============================================================

    @Override
    protected void extractBackgroundTexture(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // 先刷新本帧数据，保证滚动条 / 条目 / 按钮可见性使用同一帧
        this.refreshFrame();

        // 主背景
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, TEX_WIDTH, TEX_HEIGHT);

        // 钱条
        final int width = 112;
        final int height = 22;
        final Pair<Integer, Integer> pair = this.getMoneyBarPos();
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                this.leftPos + 3, this.topPos + 3,
                pair.getFirst().floatValue(), pair.getSecond().floatValue(),
                width, height, TEX_WIDTH, TEX_HEIGHT);

        // 钱数 / 标题
        StringUtil.drawCenteredScaledString(graphics, this.font, String.valueOf(this.getCurrentMoney()),
                this.leftPos + 25 + 44, this.topPos + 9, Colors.WHITE, 1.4f);
        StringUtil.drawCenteredScaledString(graphics, this.font, this.getShopTitle().getString(),
                this.leftPos + 115 + 82, this.topPos + 6, Colors.BLACK, 1.4f);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // 按钮可见性（使用本帧快照）
        for (TradeButton trade : this.trades) {
            trade.visible = this.downHeight + trade.getId() < this.frameGoods.size();
        }
        final AbstractDaveShopMenu.GoodEntry entry = this.selectedEntry();
        this.buyButton.visible = entry != null
                && this.menu.canBuy()
                && this.getCurrentMoney() >= entry.good().price();

        super.extractContents(graphics, mouseX, mouseY, a);

        this.extractScroll(graphics);

        // 商品行（使用 frameGoods 绝对行索引，避免列表位置/内容错位）
        int h = 0;
        int posX = this.leftPos + 6;
        int posY = this.topPos + 28;
        for (AbstractDaveShopMenu.GoodEntry e : this.frameGoods) {
            if (h >= this.downHeight && h < this.downHeight + TRADE_NUM_PER_PAGE) {
                this.extractTrade(graphics, e.good(), posX, posY);
                posY += 20;
            }
            ++h;
        }

        if (entry != null) {
            this.extractDetails(graphics, entry.good());
        }

        this.menu.getLeftRefreshTime().ifPresent(time ->
                StringUtil.drawCenteredScaledString(graphics, this.font,
                        Component.translatable("gui.pvz.shop.left_time", time).getString(),
                        this.leftPos + 117 + 120, this.topPos + 28,
                        time > 12000 ? Colors.GREEN : time > 1200 ? Colors.YELLOW : Colors.RED, 0.8f));
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        for (TradeButton trade : this.trades) {
            if (trade.isHovered()) {
                trade.extractTooltip(graphics, this.frameGoods, mouseX, mouseY);
                return;
            }
        }
        super.extractTooltip(graphics, mouseX, mouseY);
    }

    protected void extractDetails(GuiGraphicsExtractor graphics, GoodType good) {
        if (good == null) return;
        StringUtil.drawCenteredScaledString(graphics, this.font,
                this.getGoodDescription(good).getString(),
                this.leftPos + 117 + 80, this.topPos + 28 + 20, Colors.BLACK, 1.5f);
    }

    protected void extractTrade(GuiGraphicsExtractor graphics, GoodType good, int posX, int posY) {
        StringUtil.drawCenteredScaledString(graphics, this.font, String.valueOf(good.price()),
                posX + 31, posY + 4, Colors.WHITE, 1.2f);

        final int offsetX = posX + 81;
        final int offsetY = posY + 1;
        final GoodKind kind = good.kind();

        if (kind instanceof EnergyGood) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    offsetX, offsetY, 112.0F, 195.0F, 16, 16, TEX_WIDTH, TEX_HEIGHT);
        } else if (kind instanceof SlotGood) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    offsetX, offsetY, 128.0F, 195.0F, 16, 16, TEX_WIDTH, TEX_HEIGHT);
        } else if (kind instanceof MoneyGood) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    offsetX, offsetY, 144.0F, 195.0F, 16, 16, TEX_WIDTH, TEX_HEIGHT);
        } else {
            final ItemStack stack = this.createIcon(kind);
            if (!stack.isEmpty()) {
                graphics.item(stack, offsetX, offsetY);
            }
        }
    }

    /**
     * 商品描述：用 {@code kind.createStack} 生成代表栈，再交给 {@code kind.describe}。
     */
    protected Component getGoodDescription(GoodType good) {
        return this.describeKind(good.kind());
    }

    /** 统一的"kind → 展示描述"入口。 */
    private Component describeKind(GoodKind kind) {
        final var level = Minecraft.getInstance().level;
        if (level == null) return Component.empty();
        return kind.describe(kind.createStack(level.registryAccess()));
    }

    /** kind → 图标栈（无物品的 kind 返回 EMPTY）。 */
    private ItemStack createIcon(GoodKind kind) {
        final var level = Minecraft.getInstance().level;
        if (level == null) return ItemStack.EMPTY;
        return kind.createStack(level.registryAccess());
    }

    // ============================================================ 滚动条 ============================================================

    private void extractScroll(GuiGraphicsExtractor graphics) {
        final int x = this.leftPos;
        final int y = this.topPos;
        final int i = this.frameGoods.size() - TRADE_NUM_PER_PAGE + 1;

        if (i > 1) {
            final int j = 159 - (27 + (i - 1) * 159 / i);
            final int k = 1 + j / i + 159 / i;
            final int len = 133;
            int i1 = Math.min(len, this.downHeight * k);
            if (this.downHeight == i - 1) {
                i1 = 133;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    x + 106, y + 27 + i1, 0.0F, 195.0F, 6, 27, TEX_WIDTH, TEX_HEIGHT);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                    x + 106, y + 27, 6.0F, 195.0F, 6, 27, TEX_WIDTH, TEX_HEIGHT);
        }
    }

    // ============================================================ 输入 ============================================================

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX < this.leftPos || mouseX >= this.leftPos + this.imageWidth
                || mouseY < this.topPos || mouseY >= this.topPos + this.imageHeight) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        final int size = this.frameGoods.size();
        if (size <= TRADE_NUM_PER_PAGE) return true;
        final int max = size - TRADE_NUM_PER_PAGE;
        this.downHeight = Mth.clamp(this.downHeight - (int) Math.signum(scrollY), 0, max);
        return true;
    }

    // ============================================================ 子类扩展点 ============================================================

    protected int getShopID() {
        return GuiHandler.SHOP;
    }

    protected abstract int getCurrentMoney();

    protected abstract Pair<Integer, Integer> getMoneyBarPos();

    protected abstract Component getShopTitle();

    // ============================================================ TradeButton ============================================================

    // ============================================================ TradeButton ============================================================

    public class TradeButton extends Button.Plain {

        final int id;

        public TradeButton(int x, int y, int id, OnPress press) {
            super(x, y, 100, 20, Component.empty(), press, Button.DEFAULT_NARRATION);
            this.id = id;
            this.visible = false;
        }

        public int getId() {
            return this.id;
        }

        public void extractTooltip(GuiGraphicsExtractor graphics,
                                   List<AbstractDaveShopMenu.GoodEntry> goods,
                                   int mouseX, int mouseY) {
            final int idx = downHeight + this.id;
            if (idx < 0 || idx >= goods.size()) return;

            final GoodType good = goods.get(idx).good();
            if (good == null) return;

            final GoodKind kind = good.kind();
            if (kind instanceof EnergyGood || kind instanceof SlotGood || kind instanceof MoneyGood) {
                // 无物品语义的 kind：直接给描述
                graphics.setComponentTooltipForNextFrame(font,
                        List.of(describeKind(kind)), mouseX, mouseY);
                return;
            }

            // 有物品的 kind：优先物品 tooltip，失败回退到 describe
            final ItemStack stack = createIcon(kind);
            if (!stack.isEmpty()) {
                graphics.setTooltipForNextFrame(font, stack, mouseX, mouseY);
            } else {
                graphics.setComponentTooltipForNextFrame(font,
                        List.of(describeKind(kind)), mouseX, mouseY);
            }
        }
    }
}