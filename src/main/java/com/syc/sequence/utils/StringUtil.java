package com.syc.sequence.utils;

/**
 * 字符串工具类。
 *
 * @author tiny.liu
 */
public class StringUtil {

    /**
     * 字符串处理方式 - 左侧
     */
    private static final int LEFT = -1;

    /**
     * 字符串处理方式 - 右侧
     */
    private static final int RIGHT = 1;

    /**
     * 字符串处理方式 - 两侧
     */
    private static final int BOTH = 2;

    /**
     * 是否是空字符串
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    public static String subOrLefPad(String no, int len) {
        if (no.length() > len) {
            return StringUtil.subString(no, -len);
        }

        return StringUtil.leftPad(no, "0", len);
    }

    /**
     * @param padStr 填充字符
     * */
    public static String leftPad(String msg, String padStr, int totalLen) {
        return pad(msg, padStr, totalLen, LEFT);
    }

    private static String pad(String msg, String padStr, int totalLen, int padMode) {
        if (isEmpty(padStr) || totalLen <= 0 || (msg != null && msg.length() >= totalLen)) {
            return msg;
        }

        StringBuilder str = new StringBuilder(msg);
        out:
        while (str.length() + padStr.length() * Math.abs(padMode) <= totalLen) {
            switch (padMode) {
                case LEFT: // 左填充
                    str.insert(0, padStr);
                    break;
                case RIGHT: // 右填充
                    str.append(padStr);
                    break;
                case BOTH: // 双向填充
                    str.insert(0, padStr).append(padStr);
                    break;
                default: // 未知填充方式
                    break out;
            }
        }
        return str.toString();
    }

    /**
     * 获取子字符串, 起始位置可以为负，不会出现下标越界
     *
     * @param msg
     * @param start 起始位置(包含), 取负代表从最后一个字符开始倒数，负无穷大时位置为0，正无穷大位置为length
     */
    public static String subString(String msg, int start) {
        return subString(msg, start, 0);
    }

    /**
     * 获取子字符串, 起始和结束位置可以为负，不会出现下标越界
     *
     * @param msg
     * @param start 起始位置(包含), 取负代表从最后一个字符开始倒数，负无穷大和零时位置为0，正无穷大位置为length
     * @param end   结束位置(不包含), 取负代表从最后一个字符开始倒数，负无穷大时位置为0，零和正无穷大位置为length
     */
    public static String subString(String msg, int start, int end) {
        if (isEmpty(msg)) {
            return msg;
        }
        if (start < 0) {
            start = Math.max(0, msg.length() + start); // 负无穷大取0
        }
        if (end <= 0) {
            end = msg.length() + end;
        }
        end = Math.min(end, msg.length()); // 正无穷大取length

        if (start >= msg.length() || end > msg.length() || start >= end) {
            return "";
        }
        return msg.substring(start, end);
    }
}
