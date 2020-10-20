package org.l2x9.translatebot;


import org.l2x9.jsonshit.JSONArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class GoogleTranslate {
    public char codes;

    GoogleTranslate() {
    }

    private String generateURL(String sourceLanguage, String targetLanguage, String text) throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(text, "UTF-8");
        String GOOGLE_TRANSLATE_URL = "http://translate.google.com/translate_a/single";
        return GOOGLE_TRANSLATE_URL +
                "?client=webapp" +
                "&hl=en" +
                "&sl=" +
                sourceLanguage +
                "&tl=" +
                targetLanguage +
                "&q=" +
                encoded +
                "&multires=1" +
                "&otf=0" +
                "&pc=0" +
                "&trs=1" +
                "&ssel=0" +
                "&tsel=0" +
                "&kc=1" +
                "&dt=t" +
                "&ie=UTF-8" +
                "&oe=UTF-8" +
                "&tk=" +
                generateToken(text);
    }

    public String detectLanguage(String text) throws IOException {
        String urlText = generateURL("auto", "en", text);
        URL url = new URL(urlText);
        String rawData = urlToText(url);
        return findLanguage(rawData);
    }

    public String translate(String targetLanguage, String text) throws IOException {
        return translate("auto", targetLanguage, text);
    }

    public String translate(String sourceLanguage, String targetLanguage, String text) throws IOException {
        String urlText = generateURL(sourceLanguage, targetLanguage, text);
        URL url = new URL(urlText);
        String rawData = urlToText(url);
        String[] raw = rawData.split("\"");
        if (raw.length < 2) {
            return null;
        }
        JSONArray arr = new JSONArray(rawData);
        JSONArray arr1 = arr.getJSONArray(0);
        StringBuilder trans = new StringBuilder();
        for (int i = 0; i < arr1.length(); i++) {
            JSONArray arr2 = arr1.getJSONArray(i);
            trans.append(arr2.get(0).toString());
        }

        return trans.toString();
    }

    private String urlToText(URL url) throws IOException {
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0");
        Reader r = new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8);
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        return buf.toString();
    }

    private String findLanguage(String rawData) {
        for (int i = 0; i + 5 < rawData.length(); i++) {
            boolean dashDetected = rawData.charAt(i + 4) == '-';
            if (rawData.charAt(i) == ',' && rawData.charAt(i + 1) == '"' && ((rawData.charAt(i + 4) == '"' && rawData.charAt(i + 5) == ',') || dashDetected)) {
                if (dashDetected) {
                    int lastQuote = rawData.substring(i + 2).indexOf('"');
                    if (lastQuote > 0)
                        return rawData.substring(i + 2, i + 2 + lastQuote);
                } else {
                    String possible = rawData.substring(i + 2, i + 4);
                    if (containsLettersOnly(possible)) {
                        return possible;
                    }
                }
            }
        }
        return null;
    }

    private boolean containsLettersOnly(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isLetter(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private int[] TKK() {
        return new int[]{0x6337E, 0x217A58DC + 0x5AF91132};
    }

    private int shr32(int x, int bits) {
        if (x < 0) {
            long x_l = 0xffffffffL + x + 1;
            return (int) (x_l >> bits);
        }
        return x >> bits;
    }

    private int RL(int a, String b) {
        for (int c = 0; c < b.length() - 2; c += 3) {
            int d = b.charAt(c + 2);
            d = d >= 65 ? d - 87 : d - 48;
            d = b.charAt(c + 1) == '+' ? shr32(a, d) : (a << d);
            a = b.charAt(c) == '+' ? (a + (d)) : a ^ d;
        }
        return a;
    }

    private String generateToken(String text) {
        int[] tkk = TKK();
        int b = tkk[0];
        int e = 0;
        int f = 0;
        List<Integer> d = new ArrayList<>();
        for (; f < text.length(); f++) {
            int g = text.charAt(f);
            if (0x80 > g) {
                d.add(e++, g);
            } else {
                if (0x800 > g) {
                    d.add(e++, g >> 6 | 0xC0);
                } else {
                    if (0xD800 == (g & 0xFC00) && f + 1 < text.length() && 0xDC00 == (text.charAt(f + 1) & 0xFC00)) {
                        g = 0x10000 + ((g & 0x3FF) << 10) + (text.charAt(++f) & 0x3FF);
                        d.add(e++, g >> 18 | 0xF0);
                        d.add(e++, g >> 12 & 0x3F | 0x80);
                    } else {
                        d.add(e++, g >> 12 | 0xE0);
                        d.add(e++, g >> 6 & 0x3F | 0x80);
                    }
                }
                d.add(e++, g & 63 | 128);
            }
        }

        int a_i = b;
        for (e = 0; e < d.size(); e++) {
            a_i += d.get(e);
            a_i = RL(a_i, "+-a^+6");
        }
        a_i = RL(a_i, "+-3^+b+-f");
        a_i ^= tkk[1];
        long a_l;
        if (0 > a_i) {
            a_l = 0x80000000L + (a_i & 0x7FFFFFFF);
        } else {
            a_l = a_i;
        }
        a_l %= Math.pow(10, 6);
        return String.format(Locale.US, "%d.%d", a_l, a_l ^ b);
    }
}