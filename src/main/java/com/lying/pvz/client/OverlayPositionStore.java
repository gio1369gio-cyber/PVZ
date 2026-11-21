package com.lying.pvz.client;

import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 资源条覆盖层（{@code renderResources}：阳光 / 金钱 / 宝石 / 等级条）的显示位置存储。
 *
 * <p>位置用归一化坐标表示：{@code x/y ∈ [0,1]}，分别乘以屏幕宽 / 高得到资源条左上角的像素坐标。
 * 位置通过 {@code config/pvz-overlay-position.properties} 持久化，与客户端配置分离，
 * 由位置调整界面（{@code OverlayPositionScreen}）在拖拽后写入。
 *
 * <p>实现参考 Peculiar 的 {@code PositionStore}：所有读写都在 {@code synchronized} 方法内进行，
 * 首次访问时惰性从文件加载；只动位置，不动大小（资源条缩放仍由渲染代码固定为 0.7F）。
 */
public final class OverlayPositionStore {

    /** 默认锚点：屏幕左上角区域。资源条当前从 (0,0) 起绘制，这里给一点内边距避免贴边。 */
    private static final float DEFAULT_X = 0.0f;
    private static final float DEFAULT_Y = 0.0f;
    private static final String FILE_NAME = "pvz-overlay-position.properties";

    private static Float x = null;
    private static Float y = null;

    private OverlayPositionStore() {
    }

    private static Path path() {
        return FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
    }

    public static synchronized float x() {
        ensureLoaded();
        return x;
    }

    public static synchronized float y() {
        ensureLoaded();
        return y;
    }

    /** 设置并持久化归一化锚点，自动 clamp 到 [0,1]。 */
    public static synchronized void set(float nx, float ny) {
        x = clamp(nx);
        y = clamp(ny);
        save();
    }

    private static void ensureLoaded() {
        if (x != null && y != null) return;
        Path p = path();
        if (Files.isRegularFile(p)) {
            Properties props = new Properties();
            try (var in = Files.newInputStream(p)) {
                props.load(in);
                x = clamp(parseFloat(props.getProperty("x"), DEFAULT_X));
                y = clamp(parseFloat(props.getProperty("y"), DEFAULT_Y));
                return;
            } catch (IOException e) {
                // fall through
            }
        }
        x = DEFAULT_X;
        y = DEFAULT_Y;
    }

    private static float parseFloat(String s, float def) {
        if (s == null || s.isBlank()) return def;
        try {
            return Float.parseFloat(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private static void save() {
        Properties props = new Properties();
        props.setProperty("x", String.valueOf(x));
        props.setProperty("y", String.valueOf(y));
        try {
            Path p = path();
            Files.createDirectories(p.getParent());
            try (var out = Files.newOutputStream(p)) {
                props.store(out, "PVZ resource overlay position (auto-managed)");
            }
        } catch (IOException ignored) {
        }
    }
}
