package org.l2x9.translatebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    static JDABuilder builder;
    static JDA jda;
    private static GoogleTranslate googleTranslate;

    public static void main(String[] args) {
        builder = JDABuilder.createDefault("");
        builder.setActivity(Activity.watching("#Announcements"));
        builder.setStatus(OnlineStatus.IDLE);
        try {
            jda = builder.build();
            googleTranslate = new GoogleTranslate();
            jda.addEventListener(new Events());
        } catch (LoginException loginException) {
            loginException.printStackTrace();
        }
    }

    public static GoogleTranslate getGoogleTranslate() {
        return googleTranslate;
    }
    public static JDA getJda() {
        return jda;
    }
}
