package comself_ignition.httpsgithub.meetneat.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class SaveSharedPreference {
    static final String PREF_EMAIL_ADDRESS= "email";
    static final String PREF_USER_NAME= "username";
    static final String PREF_LOGGED_IN= "LoggedIn";
    static final String PREF_DATE_LOGGED_IN = "date";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences("meetneat", Context.MODE_PRIVATE);
    }

    public static void setUserName(Context ctx, String userName)
    {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setEmailAddress(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL_ADDRESS, userName);
        editor.commit();
    }

    public static String getEmailAddress(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL_ADDRESS, "");
    }

    public static void setLoggedIn(Context ctx, Boolean isLoggedIn)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public static Boolean getLoggedIn(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_LOGGED_IN, false);
    }
}
