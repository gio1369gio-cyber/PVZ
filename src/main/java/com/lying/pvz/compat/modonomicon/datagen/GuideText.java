package com.lying.pvz.compat.modonomicon.datagen;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Patchouli 文本宏 → Modonomicon markdown 的转换辅助。
 *
 * <p>旧书使用 Patchouli 的 {@code $(...)} 宏语法，Modonomicon 使用 markdown
 * （{@code **粗体**} / {@code *斜体*}）。Patchouli 的宏是「开启 + 关闭」配对结构：
 *
 * <ul>
 *   <li>{@code $(l)...$()} → {@code **...**}（加粗）</li>
 *   <li>{@code $(m)...$()} → {@code *...*}（斜体）</li>
 *   <li>{@code $(br)} / {@code <br>} → 换行</li>
 *   <li>{@code $(red)...$()} / {@code $(#ff0000)...$()} 等颜色宏 → 去掉标记，保留文字</li>
 * </ul>
 *
 * <p>关键点：{@code $()} 是通用「关闭」标记，必须关闭最近开启的宏。因此这里用一个
 * 栈跟踪开启的宏，保证 {@code **} / {@code *} 正确配对，避免产生孤立的 markdown 标记。
 */
public final class GuideText {

    private GuideText() {
    }

    /**
     * 将一段 Patchouli 文本转换为 Modonomicon markdown。
     */
    public static String convert(String patchouli) {
        if (patchouli == null) {
            return "";
        }

        // 先把换行宏规范化为真实换行，避免被后面的宏解析吞掉。
        String s = patchouli.replace("$(br)", "\n").replace("<br>", "\n");

        StringBuilder out = new StringBuilder(s.length());
        // 栈中保存每个「开启宏」对应的关闭标记；颜色/未知宏压入空串表示只做配对、无可视输出。
        Deque<String> closes = new ArrayDeque<>();

        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) != '$' || i + 1 >= s.length() || s.charAt(i + 1) != '(') {
                out.append(s.charAt(i++));
                continue;
            }

            int end = s.indexOf(')', i + 2);
            if (end < 0) {
                // 没有右括号，按普通字符处理，避免死循环。
                out.append(s.charAt(i++));
                continue;
            }

            String macro = s.substring(i + 2, end);
            if (macro.isEmpty()) {
                // $( ) 关闭标记：关闭最近开启的宏。
                if (!closes.isEmpty()) {
                    String close = closes.pop();
                    out.append(close); // 颜色宏的 close 为空串，不输出
                }
            } else if (macro.equals("l")) {
                out.append("**");
                closes.push("**");
            } else if (macro.equals("m")) {
                out.append('*');
                closes.push("*");
            } else {
                // 颜色宏（bred/bred/b#hex/...）或未知宏：无可视输出，但仍压栈以配对 $()。
                closes.push("");
            }

            i = end + 1;
        }

        // 旧书中有少数文本的宏未严格配对（如 $(l)...$(red)...$() 缺少关闭），
        // 这里把仍未关闭的 markdown 标记补上，避免产生残缺的 ** / *。
        while (!closes.isEmpty()) {
            out.append(closes.pop());
        }

        return out.toString();
    }
}
