package com.app.pao.utils;

import android.content.Context;
import android.text.TextUtils;

import com.app.pao.R;
import com.app.pao.config.AppEnum;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hwh.sdk.ble.BLEConfig;

/**
 * Created by Raul on 2015/11/11.
 * 手机相关工具
 */
public class StringUtils {


    /**
     * 验证邀请码
     *
     * @param context
     * @param invite
     * @return
     */
    public static String checkInvite(final Context context, final String invite) {
        //手机号码不能为空
        if (TextUtils.isEmpty(invite)) {
            return context.getResources().getString(R.string.Error_Check_Invite_Empty);
        }
        if (invite.length() != 4) {
            return context.getResources().getString(R.string.Error_Check_Invite_Length);
        }
        String regex = "[a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9][a-zA-Z0-9]";
        if (!invite.matches(regex)) {
            return context.getResources().getString(R.string.Error_Check_Invite_Error);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查手机号码是否正确
     *
     * @param context  上下文
     * @param phoneNum 手机号码
     * @return
     */
    public static String checkPhoneNumInputError(final Context context, final String phoneNum, String mCountryIso) {
        //手机号码不能为空
        if (TextUtils.isEmpty(phoneNum)) {
            return context.getResources().getString(R.string.Error_Check_Phonenum_Empty);
        }
        //若是中国的电话号码
        if(mCountryIso.toUpperCase().equals("CN") && !isMobileNO(phoneNum)){
            return context.getResources().getString(R.string.Error_Check_Phonenum_Illegal);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查密码是否符合规范
     *
     * @param context  上下文
     * @param password 密码
     * @return
     */
    public static String checkPasswordInputError(final Context context, final String password) {
        //密码不能为空
        if (TextUtils.isEmpty(password)) {
            return context.getResources().getString(R.string.Error_Check_Password_Empty);
        }
        if (password.length() > 16 || password.length() < 6) {
            return context.getResources().getString(R.string.Error_Check_Password_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }


    /**
     * 检查验证码字符串是否正确
     *
     * @param code
     * @return
     */
    public static String checkVCCodeError(final Context context, final String code) {
        //验证码不能为空
        if (TextUtils.isEmpty(code)) {
            return context.getResources().getString(R.string.Error_Check_VcCode_Empty);
        }
        if (code.length() != 4) {
            return context.getResources().getString(R.string.Error_Check_VcCode_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查昵称
     *
     * @param context
     * @param nickname
     * @return
     */
    public static String checkNickNameError(final Context context, final String nickname) {
        //昵称不能为空
        if (TextUtils.isEmpty(nickname)) {
            return context.getResources().getString(R.string.Error_Check_Nickname_Empty);
        }
        if (nickname.length() > 10) {
            return context.getResources().getString(R.string.Error_Check_Nickname_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查跑团描述
     *
     * @param context
     * @param Description
     * @return
     */
    public static String checkGroupDescription(final Context context, final String Description) {
        //昵称不能为空
        if (TextUtils.isEmpty(Description)) {
            return context.getResources().getString(R.string.Error_Check_Description_Empty);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查跑团名称
     *
     * @param context
     * @param nickname
     * @return
     */
    public static String checkGroupNameError(final Context context, final String nickname) {
        //昵称不能为空
        if (TextUtils.isEmpty(nickname)) {
            return context.getResources().getString(R.string.Error_Check_Groupname_Empty);
        }
        if (nickname.length() > 10) {
            return context.getResources().getString(R.string.Error_Check_Groupname_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }


    /**
     * 检查邮箱
     *
     * @param context
     * @param email
     * @return
     */
    public static String checkEmailError(final Context context, final String email) {
        //邮箱不能为空
        if (TextUtils.isEmpty(email)) {
            return context.getResources().getString(R.string.Error_Check_Email_Empty);
        }
        if(!isEmail(email)){
            return context.getResources().getString(R.string.Error_Check_Email_Error);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查昵称
     *
     * @param context
     * @param nickname
     * @return
     */
    public static String checkTrueNameError(final Context context, final String nickname) {
        //昵称不能为空
        if (TextUtils.isEmpty(nickname)) {
            return context.getResources().getString(R.string.Error_Check_Truename_Empty);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查活动名称错误
     *
     * @param context
     * @param partyName
     * @return
     */
    public static String checkPartyNameError(final Context context, final String partyName) {
        if (TextUtils.isEmpty(partyName)) {
            return context.getResources().getString(R.string.Error_Check_PartyName_Empty);
        }
        if (partyName.length() > 10) {
            return context.getResources().getString(R.string.Error_Check_PartyName_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 检查活动名称错误
     *
     * @param context
     * @param partyLocation
     * @return
     */
    public static String checkPartyLocationError(final Context context, final String partyLocation) {
        if (TextUtils.isEmpty(partyLocation)) {
            return context.getResources().getString(R.string.Error_Check_PartyLocation_Empty);
        }
        if (partyLocation.length() > 30) {
            return context.getResources().getString(R.string.Error_Check_PartyLocation_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }


    /**
     * 检查活动描述
     *
     * @param context
     * @param Description
     * @return
     */
    public static String checkPartyDescriptionError(final Context context, final String Description) {
        if (TextUtils.isEmpty(Description)) {
            return context.getResources().getString(R.string.Error_Check_PartyDescription_Empty);
        }
        if (Description.length() > 200) {
            return context.getResources().getString(R.string.Error_Check_PartyDescription_Length);
        }
        return AppEnum.DEFAULT_CHECK_ERROR;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(final String mobiles) {
        /*SS
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * Email检查
     * @param email
     * @return
     */
    private static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))" +
                "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 取传进来的字符串的首个字符，英文不变，中文取首个汉字拼音首字母，其他默认为#
     *
     * @param inputString
     * @return
     */
    public static char getPinYinFirst(String inputString){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//全部转化为大写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//没有音调
        format.setVCharType(HanyuPinyinVCharType.WITH_V);//所有的ü用v表示
        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for(char curchar : input){
                if(java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")){//用于判断是否是中文
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                }else{
                    output += java.lang.Character.toString(curchar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        if(output.equals("")){
            return '#';
        }else{
            char firstChar = output.toUpperCase().charAt(0);
            if(!(firstChar >= 65 && firstChar <= 90)){
                firstChar = '#';
            }
            return firstChar;
        }

    }

    /**
     * @param b
     * @return
     */
    public static int byteToInt2(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    /**
     * 获取原始数据
     *
     * @param src
     * @return
     */
    public static String parseOriginalData(final byte[] src) {
        String original = "";
        //计算滤光
        long light = ((((long) src[2] & 0xff) << 16)
                | (((long) src[1] & 0xff) << 8)
                | (((long) src[0] & 0xff) << 0));
        original += light + ",";
        //计算X轴
        short x = (short) (((short) src[3] & 0xFF)
                | (((short) src[4] & 0xFF) << 8));
        original += x + ",";
        //计算Y轴
        short y = (short) (((short) src[5] & 0xFF)
                | (((short) src[6] & 0xFF) << 8));
        original += y + ",";
        //计算Z轴
        short z = (short) (((short) src[7] & 0xFF)
                | (((short) src[8] & 0xFF) << 8));
        original += z + ";";

        //计算滤光
        light = ((((long) src[11] & 0xff) << 16)
                | (((long) src[10] & 0xff) << 8)
                | (((long) src[9] & 0xff) << 0));
        original += light + ",";
        //计算X轴
        x = (short) (((short) src[12] & 0xFF)
                | (((short) src[13] & 0xFF) << 8));
        original += x + ",";
        //计算Y轴
        y = (short) (((short) src[14] & 0xFF)
                | (((short) src[15] & 0xFF) << 8));
        original += y + ",";
        //计算Z轴
        z = (short) (((short) src[16] & 0xFF)
                | (((short) src[17] & 0xFF) << 8));
        original += z + ";";
        //校验位
//        int jiaoyan = ((src[19] & 0xFF));
//        original += "[" + jiaoyan + "]";
//        Log.e(TAG, "hexString:" + bytesToHexString(src));
//        Log.e(TAG, "original:" + original + "[" + jiaoyan + "]");
        return original;
    }

    public static int crc16(byte[] buffer, final int type) {
        int crc = 0;
        int length = buffer.length;
        if (type == BLEConfig.TYPE_SOFTWARE) {
            length = length - 1;
        }

        for (int i = 4, size = length; i < size; i++) {
            crc = crc(crc, buffer[i]);
//            Log.v(TAG, "crc:" + crc);
        }
//        Log.v(TAG,"crc:" +crc);
        crc = crc(crc, (byte) 0);
//        Log.v(TAG,"crc:" +crc);
        crc = crc(crc, (byte) 0);
//        Log.v(TAG, "crc:" + crc);
        return crc;
    }

    public static int crc(int crc, byte val) {
        int poly = 0x1021;
        for (byte cnt = 0; cnt < 8; cnt++, val <<= 1) {
            byte msb = (byte) (((crc & 0x8000) != 0) ? 1 : 0);
            crc <<= 1;
            if ((val & 0x80) != 0) {
                crc |= 0x0001;
            }
            if (msb != 0) {
                crc ^= poly;
            }
        }
        return crc & 0xFFFF;
    }
}
