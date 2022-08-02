package com.security.applock.utils;

import android.os.Environment;

import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.model.IconApp;
import com.security.applock.model.SelectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 111;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 112;
    public static final int PERMISSIONS_USAGE = 113;
    public static final int PERMISSIONS_DRAW_APPICATION = 114;

    public static final String URL_ABOUT = "https://www.google.com/";
    public static final String URL_POLICY = "https://www.google.com/";
    public static final String EMAIL = "dienMailVaoDay@gmail.com";

    public static final int DEFAULT_SOUND = 50;
    public static final int DEFAULT_TIMER = 10;
    public static final int DEFAULT_TIME_LOCK = 1;
    public static final int DEFAULT_TONE = 1;
    public static final int DEFAULT_INTRUDER_SELFIE = 3;

    public static final int DETECTION_NONE = 0;
    public static final int DETECTION_PROXIMITY = 1;
    public static final int DETECTION_MOTION = 2;
    public static final int DETECTION_CHARGER = 3;
    public static final int DETECTION_KIDZONE = 4;

    public static final String TYPE_DETECTION = "type detection";

    public static final String[] LST_QUESTION = {
            "Where's my brithplace?", "What was your first car?", "When is your anniversary?"
            , "What is your oldest sibling's middle name?", "In what city or town did your mother and father meet?"
    };

    public static SelectModel[] lstIncorrectPasswordEntries = new SelectModel[]{
            new SelectModel(1, App.getInstace().getContext().getString(R.string.once)),
            new SelectModel(3, App.getInstace().getContext().getString(R.string.times_3)),
            new SelectModel(5, App.getInstace().getContext().getString(R.string.times_5)),
    };

    public static SelectModel[] lstTimer = new SelectModel[]{
            new SelectModel(5, App.getInstace().getContext().getString(R.string.seconds_05)),
            new SelectModel(10, App.getInstace().getContext().getString(R.string.seconds_10)),
            new SelectModel(15, App.getInstace().getContext().getString(R.string.seconds_15)),
            new SelectModel(20, App.getInstace().getContext().getString(R.string.seconds_20)),
            new SelectModel(25, App.getInstace().getContext().getString(R.string.seconds_25)),
            new SelectModel(30, App.getInstace().getContext().getString(R.string.seconds_30)),
    };

    public static SelectModel[] lstTimeLock = new SelectModel[]{
            new SelectModel(1, App.getInstace().getContext().getString(R.string.alway_lock)),
            new SelectModel(2, App.getInstace().getContext().getString(R.string.five_minutes)),
            new SelectModel(3, App.getInstace().getContext().getString(R.string.phone_screen_off)),
    };

    public static final int[] LIST_TONE = {
            R.raw.tone_1, R.raw.tone_2, R.raw.tone_3, R.raw.tone_4, R.raw.tone_5, R.raw.tone_6, R.raw.tone_7, R.raw.tone_8, R.raw.tone_9
    };

    public static final List<Integer> LIST_THEME() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg_theme_defatult);
        list.add(R.drawable.bg_theme_blur);
        list.add(R.drawable.bg_theme_1);
        list.add(R.drawable.bg_theme_2);
        list.add(R.drawable.bg_theme_3);
        list.add(R.drawable.bg_theme_4);
        list.add(R.drawable.bg_theme_5);
        list.add(R.drawable.bg_theme_6);
        return list;
    }

    public static IconApp[] lstIconApp = new IconApp[]{
            new IconApp(0, ".ui.splash.SplashActivity", R.drawable.ic_logo, App.getInstace().getContext().getString(R.string.app_name)),
            new IconApp(1, ".ui.splash.SplashActivity_1", R.drawable.logo_map, App.getInstace().getContext().getString(R.string.map)),
            new IconApp(2, ".ui.splash.SplashActivity_2", R.drawable.logo_evaluate, App.getInstace().getContext().getString(R.string.evaluate)),
            new IconApp(3, ".ui.splash.SplashActivity_3", R.drawable.logog_achieve, App.getInstace().getContext().getString(R.string.achieve)),
            new IconApp(4, ".ui.splash.SplashActivity_4", R.drawable.logo_health, App.getInstace().getContext().getString(R.string.health))
    };

    public static final String[] LIST_APP_NETWORK = {
            "com.zhiliaoapp.musically", "app.buzz.share", "in.mohalla.sharechat", "com.instagram.android", "com.twitter.android", "com.redefine.welike", "com.facebook.katana",
            "com.ss.android.ugc.boom", "com.facebook.lite", "com.vivashow.share.video.chat", "com.roposo.android", "com.ss.android.ugc.boomlite", "com.uc.vmlite", "com.snapchat.android",
            "com.asiainno.uplive", "sg.bigo.live", "com.nebula.mamu", "com.starmakerinteractive.starm", "com.thankyo.hwgame", "com.kryptolabs.android.speakerswire", "com.yy.hiyo",
            "com.whatsapp", "com.facebook.orca", "com.truecaller", "com.facebook.mlite", "org.telegram.messenger", "com.google.android.apps.tachyon", "com.imo.android.imoim", "com.whatsapp.w4b",
            "com.eyecon.global", "com.dstukalov.walocalstoragestickers", "com.bingo.livetalk", "com.jio.join", "com.bsb.hike", "com.azarlive.android", "com.jiochat.jiochatapp",
            "com.wastickers.wastickerapps", "im.thebot.messenger", "com.caller.id.mobile.phone.number.location.locator.live.track.tracker.callblocker", "com.michatapp.im", "jp.naver.line.android",
            "com.techirsh.islamicsticker", "com.tencent.mm", "com.bbm", "com.discord", "com.bluesoft.clonappmessenger", "com.google.android.talk", "com.linecorp.linelite", "com.google.android.gm",
            "com.breakdev.zapclone", "com.stickotext.main", "com.p1.mobile.putong", "app.zenly.locator", "com.pinterest", "com.michatapp.im.lite", "sg.bigo.hellotalk", "com.google.android.apps.plus",
            "com.beetalk", "com.thinkmobile.accountmaster", "com.badoo.mobile", "com.boo.boomoji", "com.linkedin.android", "com.doyo.game.live", "com.myyearbook.m", "com.easycodes.stickercreator",
            "stickermaker.android.stickermaker", "com.easycodes.memesbr", "org.telegram.messenger.erick.zapzap", "com.whatdir.stickers", "com.link.messages.sms", "com.skype.raider",
            "com.yahoo.mobile.client.android.mail", "com.enflick.android.tn2ndLine", "br.gov.caixa.bolsafamilia", "com.lazygeniouz.saveit", "messenger.messenger.messenger.messenger",
            "com.cmcm.live", "com.jaumo", "net.lovoo.android", "com.narvii.amino.master", "com.parallel.space.lite", "com.viber.voip", "com.goldmessenger.freefastchat", "com.imo.android.imoimbeta",
            "org.hakika.wwww", "com.juphoon.justalk", "com.video.chat.spark", "com.muper.radella", "com.meet.android", "com.dev.questionaskto33", "messenger.pro.messenger", "kaf.bl3arabi.com",
            "com.crush.gogo", "com.videochat.livu", "ps.AndroTeam.HeshamPoems", "com.tinder", "com.tencent.mobileqq", "com.tencent.mm", "com.sina.weibolite"};

     public static final String INTRUDER_SELFIE = "intruder_selfie";
    public static final String ANTI_THEFT = "antitheft";
    public static final String PATH_INTRUDER_SELFIE = File.separator + ANTI_THEFT + File.separator + INTRUDER_SELFIE;
    public static final String PATH_DIRECTORY_INTRUDER_SELFIE = Environment.DIRECTORY_PICTURES + Config.PATH_INTRUDER_SELFIE;


    public class TextPassword {
        public static final String ZERO = "0";
        public static final String ONE = "1";
        public static final String TWO = "2";
        public static final String THREE = "3";
        public static final String FOUR = "4";
        public static final String FIVE = "5";
        public static final String SIX = "6";
        public static final String SEVEN = "7";
        public static final String EIGHT = "8";
        public static final String NINE = "9";
        public static final String EMPTY = "";
    }

    public class KeyBundle {
        public static final String KEY_PATTERN_CODE = "pattern code";
        public static final String KEY_CHANGE_QUESTION = "change question";
        public static final String KEY_PACKAGE_NAME = "package name";
        public static final String KEY_FIRST_SETUP_PASS = "first setup pass";
    }

    public class DataBundle {
        public static final int DATA_SWITCH = 0;
        public static final int DATA_CHANGE_QUESTION = 1;
        public static final int DATA_FIRST_SETUP_QUESTION = 2;
    }

    public class ActionIntent {
        public static final String ACTION_SET_UP_PATTERN_CODE = "action set up pattern code";
        public static final String ACTION_CHANGE_PATTERN_CODE = "action change pattern code";
        public static final String ACTION_SET_UP_PATTERN_CODE_WHEN_CHANGE = "action set up pattern code when change";
        public static final String ACTION_SET_UP_PIN_CODE_WHEN_CHANGE = "action set up pin code when change";
        public static final String ACTION_CHANGE_PIN_CODE = "action change pin code";
        public static final String ACTION_CHECK_PATTERN_CODE = "action check pattern code";
        public static final String ACTION_CHECK_PIN_CODE = "action check pin code";
        public static final String ACTION_SWITCH_TO_PIN_CODE = "action check pattern code switch";
        public static final String ACTION_SWITCH_TO_PATTERN_CODE = "action check pin code switch";
        public static final String ACTION_SET_UP_QUESTION_SUCCESS = "action set up question success";
        public static final String ACTION_CHECK_PASSWORD_FROM_SERVICE = "action check password from service";
        public static final String ACTION_CHECK_PASSWORD_ANTI_THEFT = "action check password anti theft";
        public static final String ACTION_START_DETECTION_PROXIMITY = "start detection proximity";
        public static final String ACTION_STOP_DETECTION_PROXIMITY = "stop detection proximity";
        public static final String ACTION_START_DETECTION_MOTION = "start detection motion";
        public static final String ACTION_STOP_DETECTION_MOTION = "stop detection motion";
        public static final String ACTION_START_DETECTION_CHARGER = "start detection charger";
        public static final String ACTION_START_ALARM_CHARGER = "start alarm charger";
        public static final String ACTION_START_ALARM_POWER_CONNECTED = "start alarm power connected";
        public static final String ACTION_START_ALARM_POWER_FULL = "start alarm power full";
        public static final String ACTION_STOP_DETECTION_CHARGER = "stop detection charger";
        public static final String ACTION_STOP_ANTI_THEFT = "stop anti theft";
        public static final String ACTION_STOP_SERVICE = "force stop service";
        public static final String ACTION_SET_TIME_LOCK = "set time lock";
        public static final String ACTION_NEW_APP_RECEIVER = "receiver new app";
        public static final String ACTION_UPDATE_APP_MASK = "update app mask";
    }

}
