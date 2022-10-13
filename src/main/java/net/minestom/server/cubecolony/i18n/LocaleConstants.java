package net.minestom.server.cubecolony.i18n;

import com.google.gson.annotations.SerializedName;
import net.minestom.server.cubecolony.i18n.impl.command.CommandConstants;

/**
 * @author LBuke (Teddeh)
 */
public final class LocaleConstants {
    @SerializedName("hello_world")
    public String helloWorld;

    @SerializedName("test")
    public String test;

    @SerializedName("network_motd")
    public String networkMotd;

    @SerializedName("command")
    public CommandConstants command;

    @Override
    public String toString() {
        return "LocaleConstants{" +
                "helloWorld='" + helloWorld + '\'' +
                ", test='" + test + '\'' +
                ", networkMotd='" + networkMotd + '\'' +
                '}';
    }
}
