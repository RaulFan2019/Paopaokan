package com.app.pao.utils;

import android.content.Context;
import android.content.Intent;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.data.PreferencesData;
import com.app.pao.service.VoiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/6/21.
 */
public class TTsUtils {

    static int[] rawNum = {R.raw.n_0, R.raw.n_1, R.raw.n_2, R.raw.n_3, R.raw.n_4, R.raw.n_5, R.raw.n_6, R.raw.n_7, R.raw.n_8, R.raw.n_9};
    static int[] rawUnit = {R.raw.n_10, R.raw.n_100, R.raw.n_1000, R.raw.n_10000};
    static int[] rawComeon = {R.raw.comeon1, R.raw.comeon2, R.raw.comeon3};

    /**
     * 开始跑步
     */
    public static void playStartRun(final Context context, final int count) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            if (count == 1) {
                resList.add(R.raw.start3);
            } else {
                resList.add(R.raw.start1);
                resList.addAll(NumToVoiceList(count));
                resList.add(R.raw.start2);
            }
            play(context, resList, true);
        }
    }

    /**
     * 继续跑步
     */
    public static void playContinueRun(final Context context) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            resList.add(R.raw.resume);
            play(context, resList, true);
        }
    }

    /**
     * 暂停跑步
     */
    public static void playPauseRun(final Context context) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            resList.add(R.raw.pause);
            play(context, resList, true);
        }
    }

    /**
     * 跑步结束
     *
     * @param context
     */
    public static void playFinishRun(final Context context) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            resList.add(R.raw.end);
            play(context, resList, true);
        }
    }

    /**
     * 没公里报
     */
    public static void perKm(final Context context, final int km, final int time, final int pace) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            if (km == 21) {
                resList.add(R.raw.congrat1);
            } else if (km == 42) {
                resList.add(R.raw.congrat2);
            } else if (km % 5 == 0) {
                resList.add(R.raw.break_through);
                resList.addAll(NumToVoiceList(km));
                resList.add(R.raw.km);
            } else {
                resList.add(R.raw.report);
                resList.addAll(NumToVoiceList(km));
                resList.add(R.raw.km);
            }
            resList.add(R.raw.time);
            resList.addAll(NumToTimeVoice(time));
            if (km != 1) {
                resList.add(R.raw.pace);
                resList.addAll(NumToTimeVoice(pace));
            }
            if (km == 21) {
                resList.add(R.raw.excellent);
            } else if (km == 42) {
                resList.add(R.raw.excellent);
            } else if (km % 5 == 0) {
                resList.add(R.raw.great);
            } else {
                resList.add(rawComeon[new Random().nextInt(3)]);
            }

            play(context, resList, false);
        }
    }

    /**
     * 播报当前心率
     *
     * @param context
     * @param heartbeat
     */
    public static void playHeartbeat(final Context context, final int heartbeat,final int mCurrCadence) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            resList.add(R.raw.heart_rate);
            resList.addAll(NumToVoiceList(heartbeat));
            if (mCurrCadence != 0){
                resList.add(R.raw.stridefrequency);
                resList.addAll(NumToVoiceList(mCurrCadence));
            }
            play(context, resList, false);
        }
    }

    /**
     * 你有新记录
     */
    public static void playNewRecord(final Context context) {
        if (PreferencesData.getVoiceEnable(context)) {
            ArrayList<Integer> resList = new ArrayList<>();
            resList.add(R.raw.new_record);
            play(context, resList, false);
        }
    }

    /**
     * 播放
     *
     * @param context
     * @param resList
     */
    private static void play(final Context context, final ArrayList<Integer> resList, final boolean isChongtu) {
        Intent intent = new Intent(context, VoiceService.class);
        intent.putExtra("resList", resList);
        intent.putExtra("isChongtu", isChongtu);
        context.startService(intent);
    }


    /**
     * 将数字转换为声音数字播报文件
     *
     * @param num
     * @return
     */
    private static ArrayList<Integer> NumToVoiceList(int num) {
        ArrayList<Integer> rawRes = new ArrayList<>();
        List<NumVoice> voiceList = new ArrayList<>();

        for (int count = 0; num != 0; count++) {
            int yu = num % 10;
            if (yu != 0) {
                NumVoice numVoice = new NumVoice(count, yu);
                voiceList.add(numVoice);
            }
            num = num / 10;
        }

        int key = voiceList.size();
        for (int i = key - 1; i > -1; i--) {
            NumVoice numVoice = voiceList.get(i);
            if ((key - numVoice.key) > 1) {
                rawRes.add(rawNum[0]);
            }
            rawRes.add(rawNum[numVoice.value]);
            if (numVoice.key > 0) {
                rawRes.add(rawUnit[numVoice.key - 1]);
            }
            key = numVoice.key;
        }

        return rawRes;
    }

    public static ArrayList<Integer> NumToTimeVoice(int secNum) {
        ArrayList<Integer> rawRes = new ArrayList<>();
        int hour = secNum / 3600;
        int min = (secNum % 3600) / 60;
        int sec = secNum % 60;

        if (hour > 0) {
            rawRes.addAll(NumToVoiceList(hour));
            rawRes.add(R.raw.hour);
        }
        if (min > 0) {
            rawRes.addAll(NumToVoiceList(min));
            rawRes.add(R.raw.minute);
        }
        if (sec != 0) {
            rawRes.addAll(NumToVoiceList(sec));
            rawRes.add(R.raw.second);
        }

        return rawRes;
    }

    static class NumVoice {
        int key;
        int value;

        public NumVoice(int key, int value) {
            super();
            this.key = key;
            this.value = value;
        }
    }
}
