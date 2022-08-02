package com.lubuteam.sellsourcecode.supercleaner.utils;

import android.Manifest;

import com.lubuteam.sellsourcecode.supercleaner.R;

import java.io.Serializable;

public class Config {

    public static final String DATA_OPEN_RESULT = "data open result screen";
    public static final String DATA_OPEN_BOOST = "data open boost screen";
    public static final String DATA_OPEN_FUNCTION = "data open function";
    public static final String REUSLT_DEEP_CLEAN_DATA = "result deep clean data";
    public static final long TIME_ALLOW_BOOOST = 5 * 60 * 1000;
    public static final long TIME_BATTERY_FULL = 5 * 60 * 1000;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 111;
    public static final int PERMISSIONS_USAGE = 112;
    public static final int PERMISSIONS_DRAW_APPICATION = 113;
    public static final int PERMISSIONS_NOTIFICATION_LISTENER = 114;
    public static final int PERMISSIONS_WRITE_SETTINGS = 115;
    public static final int UNINSTALL_REQUEST_CODE = 116;
    public static final int UNINSTALL_REQUEST_CODE_ACTIVITY = 117;
    public static final int MY_PERMISSIONS_REQUEST_CLEAN_CACHE = 118;
    public static final String PKG_RECERVER_DATA = "package recerver data";
    public static final String ALARM_OPEN_FUNTION = "alarm open funtion";

    public enum TYPE_DISPLAY_ADAPTER {
        VERTICAL,
        HORIZOLTAL,
        SUGGEST
    }

    public enum FUNCTION implements Serializable {
        JUNK_FILES(1, R.drawable.ic_junk_file, R.string.junk_files, R.string.junk_files_des, R.drawable.bg_func_junkfile, R.color.color_00b6c5, R.string.clean, "trash_result.json", R.string.clean_result, R.drawable.ic_junk_file_result),
        CPU_COOLER(2, R.drawable.ic_cpu_cooler, R.string.cpu_cooler, R.string.cpu_cooler_des, R.drawable.bg_func_cpucooler, R.color.color_3f7af8, R.string.cooling, "cpu_result.json", R.string.result_cooler, R.drawable.ic_cpu_cooler_result),
        PHONE_BOOST(3, R.drawable.ic_phone_booster, R.string.phone_booster, R.string.phone_booster_des, R.drawable.bg_func_phoneboost, R.color.color_ff7674, R.string.boost, "boost_result.json", R.string.boost_result, R.drawable.ic_phone_booster_result),
        ANTIVIRUS(4, R.drawable.ic_antivirus, R.string.antivirus, R.string.antivirus_des, R.drawable.bg_func_antivirus, R.color.color_ff79a2, R.string.protect_now, "antivirus_result.json", R.string.antivirus_result, R.drawable.ic_antivirus_result),
        POWER_SAVING(5, R.drawable.ic_power_saving, R.string.power_saving, R.string.power_saving_des, R.drawable.bg_func_power_saver, R.color.color_b365ff, R.string.save_now, "power_saving_result.json", R.string.sm_edge_device_optimized, R.drawable.ic_power_saving_result),
        GAME_BOOSTER_MAIN(6, R.drawable.ic_game_booster1, R.string.game_booster, R.string.game_booster_des, R.drawable.bg_func_gameboost, R.color.color_ffc400, R.string.try_it, "restult_like.json", R.string.sm_edge_device_optimized, R.drawable.ic_junk_file_result),
        APP_LOCK(7, R.drawable.ic_app_lock, R.string.app_lock, R.string.app_lock_des, R.drawable.bg_func_applock, R.color.color_ff79a2, R.string.try_it, "restult_like.json", R.string.sm_edge_device_optimized, R.drawable.ic_junk_file_result),
        SMART_CHARGE(8, R.drawable.ic_smart_charge, R.string.smart_charge, R.string.smart_charge_des, R.drawable.bg_func_smart_charge, R.color.color_00e3ad, R.string.try_it, "charge_result.json", R.string.fast_charger_boosted_result, R.drawable.ic_junk_file_result),
        DEEP_CLEAN(9, R.drawable.ic_deep_clean, R.string.deep_clean, R.string.deep_clean_des, R.drawable.bg_func_deep_clean, R.color.color_ff7674, R.string.try_it, "heart.json", R.string.deep_clean_result, R.drawable.ic_junk_file_result),
        APP_UNINSTALL(10, R.drawable.ic_app_uninstall, R.string.app_uninstall, R.string.app_uninstall_des, R.drawable.bg_func_appmanager, R.color.color_3f7af8, R.string.try_it, "restult_like.json", R.string.sm_edge_device_optimized, R.drawable.ic_junk_file_result),
        GAME_BOOSTER(11, R.drawable.ic_game_booster, R.string.game_booster, R.string.game_booster_des, R.drawable.bg_func_gameboost, R.color.color_ffc400, R.string.try_it, "restult_like.json", R.string.sm_edge_device_optimized, R.drawable.ic_junk_file_result),
        NOTIFICATION_MANAGER(12, R.drawable.ic_notification_manager, R.string.notification_manager, R.string.notification_manager_des, R.drawable.bg_func_notification_manager, R.color.color_b365ff, R.string.try_it, "trash_result.json", R.string.notification_manager_result, R.drawable.ic_junk_file_result);

        public int id;
        public int icon;
        public int title;
        public int descrition;
        public int color;
        public int action;
        public int background;
        public String jsonResult;
        public int titleResult;
        public int imageResult; // добавил картинку вместо анимации


        //        FUNCTION(int id, int icon, int title, int descrition, int background, int color, int action, String jsonResult, int titleResult) {
        FUNCTION(int id, int icon, int title, int descrition, int background, int color, int action, String jsonResult, int titleResult, int imageResult) {
            this.id = id;
            this.icon = icon;
            this.title = title;
            this.descrition = descrition;
            this.color = color;
            this.action = action;
            this.background = background;
            this.jsonResult = jsonResult;
            this.titleResult = titleResult;
            this.imageResult = imageResult;
        }
    }

    public static final FUNCTION[] LST_HOME_HORIZONTAL = new FUNCTION[]{
            FUNCTION.JUNK_FILES, FUNCTION.CPU_COOLER, FUNCTION.PHONE_BOOST, FUNCTION.ANTIVIRUS, FUNCTION.POWER_SAVING, FUNCTION.GAME_BOOSTER_MAIN
    };

    public static final FUNCTION[] LST_HOME_VERTICAL = new FUNCTION[]{
            FUNCTION.APP_LOCK, FUNCTION.SMART_CHARGE
    };

    public static final FUNCTION[] LST_SUGGEST = new FUNCTION[]{
            FUNCTION.JUNK_FILES, FUNCTION.CPU_COOLER, FUNCTION.PHONE_BOOST, FUNCTION.POWER_SAVING, FUNCTION.ANTIVIRUS, FUNCTION.APP_LOCK, FUNCTION.SMART_CHARGE, FUNCTION.APP_UNINSTALL, FUNCTION.NOTIFICATION_MANAGER
    };

    public static final FUNCTION[] LST_TOOL_CLEAN_BOOST = new FUNCTION[]{
            FUNCTION.DEEP_CLEAN, FUNCTION.APP_UNINSTALL, FUNCTION.GAME_BOOSTER
    };

    public static final FUNCTION[] LST_TOOL_SECURITY = new FUNCTION[]{
            FUNCTION.NOTIFICATION_MANAGER, FUNCTION.APP_LOCK
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

    public enum PERMISSION_DANGEROUS implements Serializable {

        READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE, R.string.per_read_phone_title, R.string.per_read_phone_des),
        ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, R.string.per_access_location_title, R.string.per_access_location_des),
        READ_SMS(Manifest.permission.READ_SMS, R.string.per_read_sms_title, R.string.per_read_sms_des),
        SEND_SMS(Manifest.permission.SEND_SMS, R.string.per_send_sms_title, R.string.per_send_sms_des),
        CALL_PHONE(Manifest.permission.CALL_PHONE, R.string.per_call_phone_title, R.string.per_call_phone_des),
        PROCESS_OUTGOING_CALLS(Manifest.permission.PROCESS_OUTGOING_CALLS, R.string.per_outgoing_call_title, R.string.per_outgoing_call_des),
        RECORD_AUDIO(Manifest.permission.RECORD_AUDIO, R.string.per_record_audio_title, R.string.per_record_audio_des),
        CAMERA(Manifest.permission.CAMERA, R.string.per_camera_title, R.string.per_camera_des);

        public String name;
        public int title;
        public int description;

        PERMISSION_DANGEROUS(String name, int title, int description) {
            this.name = name;
            this.title = title;
            this.description = description;
        }
    }

    public static Config.FUNCTION getFunctionById(int id) {
        for (Config.FUNCTION mFunction : Config.FUNCTION.values()) {
            if (mFunction.id == id)
                return mFunction;
        }
        return null;
    }
}
