package org.l2x9.translatebot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class Events extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getName().equalsIgnoreCase("announcements") && !event.getAuthor().isBot()) {
            GoogleTranslate translate = Main.getGoogleTranslate();
            String user = "[" + event.getAuthor().getAsTag().concat("] ");
            String messageOG = event.getMessage().getContentRaw();
            try {
                if (translate.detectLanguage(messageOG).toLowerCase().equals("en")) {
                    String messageSP = translate.translate("es", messageOG).replace("@everyone", "").replace("@here", "");
                    String tacoMessage = user + messageSP;
                    sendEmbed(tacoMessage, event.getTextChannel());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEmbed(String message, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(":taco: " + message);
        embedBuilder.setAuthor(Main.getJda().getSelfUser().getAsTag().concat(" By 254n_m"), null, Main.getJda().getSelfUser().getAvatarUrl());
        embedBuilder.setColor(Color.GRAY);
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
