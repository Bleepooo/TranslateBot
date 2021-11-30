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
        if (event.getChannel().getName().equalsIgnoreCase("announcements") || event.getChannel().getName().startsWith("ticket") && !event.getAuthor().isBot() ) {
            GoogleTranslate translate = Main.getGoogleTranslate();
            String user = "[" + event.getAuthor().getAsTag().concat("] ");
            String messageOG = event.getMessage().getContentRaw();
            try {
                if (translate.detectLanguage(messageOG).toLowerCase().equals("en")) {
                    String messageSP = translate.translate("es", messageOG).replace("@everyone", "").replace("@here", "");
                    sendEmbed(messageSP, event.getTextChannel());
                }

                if (translate.detectLanguage(messageOG).toLowerCase().equals("es")) {
                    String messageSP = translate.translate("en", messageOG).replace("@everyone", "").replace("@here", "");
                    sendEmbed1(messageSP, event.getTextChannel());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase(">ip")) {
            sendEmbed2("l2x9.dev", event.getTextChannel());
        }
    }

    private void sendEmbed(String message, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setAuthor("Mensaje en Español:", null, "https://images-ext-2.discordapp.net/external/-YlMK6cOc7nIR_OWWjY_ftndizbrXWNtVGsqHdHnSFM/%3Fsize%3D4096/https/cdn.discordapp.com/icons/847646170373292054/b148bff906be49793c581cc0eb541508.png");
        embedBuilder.setColor(Color.CYAN);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    private void sendEmbed1(String message, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setAuthor("Message in English:", null, "https://images-ext-2.discordapp.net/external/-YlMK6cOc7nIR_OWWjY_ftndizbrXWNtVGsqHdHnSFM/%3Fsize%3D4096/https/cdn.discordapp.com/icons/847646170373292054/b148bff906be49793c581cc0eb541508.png");
        embedBuilder.setColor(Color.CYAN);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    private void sendEmbed2(String message, TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setAuthor("L2X9 Bot", null, "https://images-ext-2.discordapp.net/external/-YlMK6cOc7nIR_OWWjY_ftndizbrXWNtVGsqHdHnSFM/%3Fsize%3D4096/https/cdn.discordapp.com/icons/847646170373292054/b148bff906be49793c581cc0eb541508.png");
        embedBuilder.setColor(Color.CYAN);
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
