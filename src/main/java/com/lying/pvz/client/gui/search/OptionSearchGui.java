package com.lying.pvz.client.gui.search;

import com.google.common.collect.Lists;
import com.lying.pvz.client.gui.screen.AbstractOptionScreen;
import com.lying.pvz.common.menu.AbstractOptionMenu;
import com.lying.pvz.utils.AlgorithmUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class OptionSearchGui implements Renderable, GuiEventListener, NarratableEntry {

	public static final Identifier TEXTURE = StringUtil.id("textures/gui/container/almanac_search.png");

	private Minecraft mc;
	protected AbstractOptionMenu container;
	private EditBox searchBar;
	protected final PvZRecipeManager recipeManager = new PvZRecipeManager();
	private final List<CategoryToggleWidget> toggleTabs = Lists.newArrayList();
	private CategoryToggleWidget currentTab;
	protected OptionPage page;
	private AbstractOptionScreen<?> screen;
	private String lastSearch = "";
	private int width;
	private int height;
	private int guiLeft;
	private int guiTop;
	private final int xSize = 150;
	private final int ySize = 200;
	private boolean canType;
	private boolean focused;

	public void init(Minecraft mc, AbstractOptionScreen<?> screen, AbstractOptionMenu container,
					 int widthIn, int heightIn) {
		this.mc = mc;
		this.screen = screen;
		this.container = container;
		this.width = widthIn;
		this.height = heightIn;
		this.page = new OptionPage(screen);
	}

	public void initSearchBar() {
		String s = this.searchBar != null ? this.searchBar.getValue() : "";
		this.searchBar = new EditBox(this.mc.font,
				this.guiLeft + 25, this.guiTop + 14, 100, 14,
				Component.translatable("itemGroup.search"));
		this.searchBar.setMaxLength(50);
		this.searchBar.setBordered(false);
		this.searchBar.visible = true;
		this.searchBar.setTextColor(16777215);
		this.searchBar.setValue(s);
		this.page.init(this.mc, this.guiLeft, this.guiTop);

		this.toggleTabs.clear();
		for (CategoryToggleWidget.SearchCategories c : this.screen.getSearchCategories()) {
			this.toggleTabs.add(new CategoryToggleWidget(c));
		}
		if (this.currentTab == null) {
			this.currentTab = this.toggleTabs.get(0);
		}
		this.currentTab.setStateTriggered(true);
		this.updateCollections(false);
		this.updateTabs();
	}

	public void slotClicked(@Nullable Slot slotIn) {
		if (this.container.isCraftSlot(slotIn)) {
			this.recipeManager.clear();
		}
	}

	private void updateCollections(boolean resetPage) {
		List<SearchOption> list = this.page.getCurrentList(this.currentTab.getCategory());
		String s = this.searchBar.getValue();
		if (!s.isEmpty()) {
			list.removeIf(a -> !AlgorithmUtil.KMP.kmp(a.getType().getText().getString(), s.toLowerCase()));
		}
		this.page.updateLists(list, resetPage);
	}

	public void tick() {}

	@Override
	public boolean keyPressed(KeyEvent event) {
		this.canType = false;
		if (this.mc.player == null) return false;
		if (!this.mc.player.isSpectator()) {
			if (this.searchBar.keyPressed(event)) {
				this.updateSearch();
				return true;
			} else if (this.searchBar.isFocused()) {
				return true;
			} else if (this.mc.options.keyChat.matches(event) && !this.searchBar.isFocused()) {
				this.canType = true;
				this.searchBar.setFocused(true);
				return true;
			}
		}
		return false;
	}

	private void updateSearch() {
		String s = this.searchBar.getValue().toLowerCase(Locale.ROOT);
		if (!s.equals(this.lastSearch)) {
			this.updateCollections(false);
			this.lastSearch = s;
		}
	}

	@Override
	public boolean keyReleased(KeyEvent event) {
		this.canType = false;
		return false;
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		if (this.canType) return false;
		if (this.mc.player == null) return false;
		if (this.mc.player.isSpectator()) return false;
		if (this.searchBar.charTyped(event)) {
			this.updateSearch();
			return true;
		}
		return false;
	}

	@Override
	public void setFocused(boolean focused) { this.focused = focused; }
	@Override
	public boolean isFocused() { return this.focused; }

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.mc.player == null) return false;
		if (this.mc.player.isSpectator()) return false;

		if (this.page.mouseClicked(event, doubleClick)) {
			return true;
		} else if (this.searchBar.mouseClicked(event, doubleClick)) {
			return true;
		} else {
			for (CategoryToggleWidget toggle : this.toggleTabs) {
				if (toggle.mouseClicked(event, doubleClick)) {
					if (this.currentTab != toggle) {
						this.currentTab.setStateTriggered(false);
						this.currentTab = toggle;
						this.currentTab.setStateTriggered(true);
						this.updateCollections(true);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop,
				0, 0, this.xSize, this.ySize, 256, 256);

		this.searchBar.extractRenderState(graphics, mouseX, mouseY, partialTicks);

		for (CategoryToggleWidget a : this.toggleTabs) {
			a.extractRenderState(graphics, mouseX, mouseY, partialTicks);
		}

		this.page.extract(graphics, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
	}

	public void extractTooltip(GuiGraphicsExtractor graphics, int guiLeft, int guiTop, int mouseX, int mouseY) {
		this.page.extractTooltip(graphics, mouseX, mouseY);
		this.recipeManager.extractGhostRecipeTooltip(this.mc, graphics, guiLeft, guiTop, mouseX, mouseY);
	}

	public void removed() {
		this.searchBar = null;
		this.currentTab = null;
	}

	public boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn) {
		boolean flag = mouseX < guiLeft || mouseY < guiTop
				|| mouseX >= guiLeft + xSize || mouseY >= guiTop + ySize;
		return flag && !this.currentTab.isHovered();
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.guiLeft && mouseY >= this.guiTop
				&& mouseX < this.guiLeft + this.xSize && mouseY < this.guiTop + this.ySize;
	}

	public Optional<SearchOption> getCurrentOption() { return Optional.ofNullable(this.page.getCurrentOption()); }
	public void resetCurrentOption() { this.page.resetCurrentOption(); }
	public AbstractOptionScreen<?> getOptionScreen() { return this.screen; }
	public PvZRecipeManager getRecipeManager() { return this.recipeManager; }

	private void updateTabs() {
		int x = this.guiLeft - 30;
		int y = this.guiTop + 10;
		int h = 27;
		for (int i = 0; i < this.toggleTabs.size(); i++) {
			CategoryToggleWidget toggle = this.toggleTabs.get(i);
			toggle.visible = true;
			toggle.setPosition(x, y + h * i);
		}
	}

	public int updateScreenPosition(int x, int y) {
		int totWidth = this.xSize + x;
		this.guiLeft = (this.width - totWidth) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		return this.guiLeft + this.xSize;
	}

	@Override public NarrationPriority narrationPriority() { return NarrationPriority.NONE; }
	@Override public boolean isActive() { return true; }
	@Override public void updateNarration(NarrationElementOutput output) {}
}