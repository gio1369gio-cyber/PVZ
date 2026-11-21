package com.lying.pvz.utils;

import com.lying.pvz.PvZMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Matrix3x2fStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StringUtil {
    private static final List<String> ROMAN_NUMBERS = Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X");
    public static final Identifier WIDGETS = id("textures/gui/widgets.png");
    public static final String INIT_VERSION = "0.0.0";

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(PvZMod.MOD_ID, name);
    }

    public static Identifier idNeoforge(String name) {
        return Identifier.fromNamespaceAndPath("c", name);
    }

    public static String identify(String modId, String name) {
        return modId + ":" + name;
    }

    public static String getRomanString(int num) {
        if (num > 0 && num <= 10) {
            return ROMAN_NUMBERS.get(num - 1);
        }
        return "";
    }
    /**
     * 缩放绘制单行字符串（左上角为锚点）。
     * <p>26.3 使用 {@link Matrix3x2fStack}，只有 2D 缩放。</p>
     */
    public static void drawScaledString(GuiGraphicsExtractor graphics, Font font,
                                        String string, int x, int y, int color, float scale) {
        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale, scale);
        graphics.text(font, string, 0, 0, color, false);
        pose.popMatrix();
    }

    /** 居中绘制（无缩放）。 */
    public static void drawCenteredString(GuiGraphicsExtractor graphics, Font font,
                                          String string, int x, int y, int color) {
        graphics.centeredText(font, string, x, y, color);
    }

    /** 居中 + 缩放绘制（锚点为 x）。 */
    public static void drawCenteredScaledString(GuiGraphicsExtractor graphics, Font font,
                                                String string, int x, int y, int color, float scale) {
        int width = font.width(string);
        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale, scale);
        graphics.text(font, string, -width / 2, 0,  color, false);
        pose.popMatrix();;
    }
    public static String getRandomLangText(Minecraft mc, Random rand, String name) {
        if (mc == null) {
            mc = Minecraft.getInstance();
        }
        List<String> texts = StringUtil.getLangTextList(mc, name);
        if (texts.isEmpty()) {
            return "";
        }
        return texts.get(rand.nextInt(texts.size()));
    }

    public static List<String> getLangTextList(Minecraft mc, String name) {
        Resource iresource = StringUtil.getTxtResource(mc, name);
        if (iresource == null) {
            return Collections.emptyList();
        }
        try (BufferedReader bufferedreader = new BufferedReader(
                new InputStreamReader(iresource.open(), StandardCharsets.UTF_8))) {
            return bufferedreader.lines().map(String::trim)
                    .filter(p -> p.hashCode() != 125780783).collect(Collectors.toList());
        } catch (IOException var36) {
            return Collections.emptyList();
        }
    }

    public static Resource getTxtResource(Minecraft mc, String name) {
        Identifier fileLoc = id("lang/others/" + mc.options.languageCode + "/" + name + ".txt");
        Identifier backupLoc = id("lang/others/en_us/" + name + ".txt");
        return mc.getResourceManager().getResource(fileLoc)
                .orElseGet(() -> mc.getResourceManager().getResource(backupLoc).orElse(null));
    }
}
