package net.minestom.server.cubecolony.i18n;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * @author LBuke (Teddeh)
 */
public final class Constants {
    @SerializedName("network_name")
    public static String NETWORK_NAME;

    @SerializedName("website")
    public static String WEBSITE;

    @SerializedName("website_store")
    public static String WEBSITE_STORE;

    @SerializedName("network_motd")
    public static String MOTD;

    @SerializedName("primary_colour_dark")
    public static String PRIMARY_COLOR_DARK;

    @SerializedName("primary_colour_medium")
    public static String PRIMARY_COLOR_MEDIUM;

    @SerializedName("primary_colour_light")
    public static String PRIMARY_COLOR_LIGHT;

    @SerializedName("primary_colour_lightest")
    public static String PRIMARY_COLOR_LIGHTEST;

    @SerializedName("release")
    public static Date RELEASE;

    @SerializedName("changes")
    public static String CHANGELOG;
}
