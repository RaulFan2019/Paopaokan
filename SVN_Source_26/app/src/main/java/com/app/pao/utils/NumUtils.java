package com.app.pao.utils;

import android.graphics.Color;

import com.app.pao.R;
import com.app.pao.config.AppEnum;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/12/9.
 */
public class NumUtils {

    /**
     * 保留除去1000 后数值的小数
     *
     * @param num
     * @return
     */
    public static final String retainTheDecimal(float num) {
        //将值先取整
        int totalLength = (int) (num * 100);
        String totalResult = (int) (num / 1000) + "";
        //判断去整后
        if (totalLength != 0) {
            if (totalResult.length() < 2) {
                BigDecimal b = new BigDecimal(num / 1000);
                totalResult = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "";
            } else if (totalResult.length() < 3) {
                BigDecimal b = new BigDecimal(num / 1000);
                totalResult = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() + "";
            } else if(totalResult.length() >4) {
                totalResult = (int) (num / 1000 / 10000) + "";
                if (totalResult.length() == 1) {
                    BigDecimal b = new BigDecimal(num / 1000 / 10000);
                    totalResult = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "万";
                } else if (totalResult.length() == 2) {
                    BigDecimal b = new BigDecimal(num / 1000 / 10000);
                    totalResult = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() + "万";
                } else {
                    totalResult += "万";
                }
            }
        } else if (totalLength == 0) {
            totalResult = "0";
        }
        return totalResult;
    }

    /**
     * 保留距离2位小数
     *
     * @return
     */
    public static final double formatLength(double length) {
        final BigDecimal bg = new BigDecimal(length);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 根据配速计算颜色
     *
     * @param pace
     * @return
     */
    public static final int parseColorFromPace(int pace) {
        int level = pace / 60;//颜色等级

        if (level < 3) {
            return Color.parseColor("#5faf4c");
        }

        if (level == 3) {
            return Color.parseColor("#3db150");
        }

        if (level == 4) {
            return Color.parseColor("#77ad4a");
        }

        if (level == 5) {
            return Color.parseColor("#b8a643");
        }

        if (level == 6) {
            return Color.parseColor("#e9953d");
        }

        if (level == 7) {
            return Color.parseColor("#ec653a");
        }

        if (level == 8) {
            return Color.parseColor("#ec313a");
        }

        if (level == 9) {
            return Color.parseColor("#e6153a");
        }

        if (level > 10) {
            return Color.parseColor("#ec2e3a");
        }
        return Color.parseColor("#ec2e3a");
    }

    /**
     * 根据总长度返回不同背景
     *
     * @param totalLength
     * @return
     */
    public static int parseBgFromLength(float totalLength) {
        int bg;

        if (totalLength >= AppEnum.UserCardBgLv.LV_8) {
            bg = R.drawable.bg_card_big_level_8;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_7) {
            bg = R.drawable.bg_card_big_level_7;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_6) {
            bg = R.drawable.bg_card_big_level_6;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_5) {
            bg = R.drawable.bg_card_big_level_5;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_4) {
            bg = R.drawable.bg_card_big_level_4;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_3) {
            bg = R.drawable.bg_card_big_level_3;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_2) {
            bg = R.drawable.bg_card_big_level_2;
        } else {
            bg = R.drawable.bg_card_big_level_1;
        }

        return bg;
    }

    /**
     * 根据总长度返回不同卡片类型字符串
     *
     * @param totalLength
     * @return
     */
    public static String getCardTypeString(float totalLength) {
        String cardType;
        if (totalLength >= AppEnum.UserCardBgLv.LV_8) {
            cardType = "海王星";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_7) {
            cardType = "天王星";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_6) {
            cardType = "木星";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_5) {
            cardType = "土星";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_4) {
            cardType = "火星";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_3) {
            cardType = "月球";
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_2) {
            cardType = "金星";
        } else {
            cardType = "水星";
        }

        return cardType;
    }

    /**
     * 根据总长度返回总距离等级
     *
     * @param totalLength
     * @return
     */
    public static int getTotalLengthLevel(float totalLength) {
        if (totalLength >= AppEnum.UserCardBgLv.LV_8) {
            return 8;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_7) {
            return 7;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_6) {
            return 6;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_5) {
            return 5;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_4) {
            return 4;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_3) {
            return 3;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_2) {
            return 2;
        } else {
            return 1;
        }
    }


    /**
     * 根据卡片等级获取卡片名称
     *
     * @param level
     * @return
     */
    public static String getTotalLengthLvCardName(int level) {
        switch (level) {
            case 1:
                return "纸卡";
            case 2:
                return "木卡";
            case 3:
                return "水晶卡";
            case 4:
                return "琥珀卡";
            case 5:
                return "金卡";
            case 6:
                return "铂金卡";
            case 7:
                return "翡翠卡";
            case 8:
                return "钻石卡";
        }
        return "纸卡";
    }

    /**
     * 根据总长度返回不同背景(新：这里的level指的是当前list中的pos位置，所以与以前的level是反的)
     *@tip
     * @param totalLength
     * @return
     */
    public static int parseLevelFromLength(float totalLength) {
        int level;
        if (totalLength >= AppEnum.UserCardBgLv.LV_8) {
            level = 0;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_7) {
            level = 1;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_6) {
            level = 2;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_5) {
            level = 3;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_4) {
            level = 4;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_3) {
            level = 5;
        } else if (totalLength >= AppEnum.UserCardBgLv.LV_2) {
            level = 6;
        } else {
            level = 7;
        }

        return level;
    }

    /**
     * 获取总距离等级的距离
     *
     * @param level
     * @return
     */
    public static int getNextLevelLength(int level) {
        switch (level) {
            case 1:
                return AppEnum.UserCardBgLv.LV_2;
            case 2:
                return AppEnum.UserCardBgLv.LV_3;
            case 3:
                return AppEnum.UserCardBgLv.LV_4;
            case 4:
                return AppEnum.UserCardBgLv.LV_5;
            case 5:
                return AppEnum.UserCardBgLv.LV_6;
            case 6:
                return AppEnum.UserCardBgLv.LV_7;
            case 7:
                return AppEnum.UserCardBgLv.LV_8;
            case 8:
                return AppEnum.UserCardBgLv.LV_8;
        }
        return AppEnum.UserCardBgLv.LV_1;
    }

    /**
     * 获取总距离等级的距离
     *
     * @param level
     * @return
     */
    public static int getLevelLength(int level) {
        switch (level) {
            case 1:
                return AppEnum.UserCardBgLv.LV_1;
            case 2:
                return AppEnum.UserCardBgLv.LV_2;
            case 3:
                return AppEnum.UserCardBgLv.LV_3;
            case 4:
                return AppEnum.UserCardBgLv.LV_4;
            case 5:
                return AppEnum.UserCardBgLv.LV_5;
            case 6:
                return AppEnum.UserCardBgLv.LV_6;
            case 7:
                return AppEnum.UserCardBgLv.LV_7;
            case 8:
                return AppEnum.UserCardBgLv.LV_8;
        }
        return AppEnum.UserCardBgLv.LV_1;
    }

    /**
     * 计算出万位以上的数字
     *
     * @param num
     * @return
     */
    public static String retainTheMyriabit(long num) {
        String numStr = num + "";
        //百万位 （） 1 000 000
//        if (numStr.length() > 7) {
//
//            //十万位
//        } else if (numStr.length() > 6) {
//
//            //万 10 000
//        } else
        if (num / 10000 > 0) {
            BigDecimal bigDecimal = new BigDecimal(((double) num / 10000));
            numStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "万";
        }
        return numStr;
    }
}
