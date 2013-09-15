package com.speed;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * See license.txt for licensing information.
 *
 * @author Shivam Mistry
 */
public class RSLoader extends JFrame {

    private final String paramURL;
    private final Properties properties, parameters, messages;
    private URLClassLoader loader;

    public RSLoader(final String paramURL, boolean noLimits) {
        this.paramURL = paramURL;
        properties = new Properties();
        parameters = new Properties();
        messages = new Properties();
        try {
            loadParams();
            RSAppletStub stub = new RSAppletStub(parameters, properties);
            Applet applet = loadClasses();
            setPreferredSize(new Dimension(Integer.parseInt(properties.getProperty("window_preferredwidth", "1024")),
                    Integer.parseInt(properties.getProperty("window_preferredheight", "768"))));
            if (!noLimits) {
                String appletMinW = properties.getProperty("applet_minwidth", "765");
                String appletMinH = properties.getProperty("applet_minheight", "540");
                String appletMaxW = properties.getProperty("applet_maxwidth", "3200");
                String appletMaxH = properties.getProperty("applet_maxheight", "1200");
                applet.setMinimumSize(new Dimension(Integer.parseInt(appletMinW), Integer.parseInt(appletMinH)));
                applet.setMaximumSize(new Dimension(Integer.parseInt(appletMaxW), Integer.parseInt(appletMaxH)));
            }
            applet.setStub(stub);
            setTitle("RuneScape");
            add(applet);
            applet.init();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            pack();
            applet.start();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Applet loadClasses() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url = new URL(properties.getProperty("codebase", "http://world16.runescape.com/k=3/") + properties.getProperty("initial_jar"));
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String arch = System.getProperty("os.arch");
            String browser;
            if (arch.endsWith("86")) {
                browser = properties.getProperty("codebase", "http://world16.runescape.com/k=3/") + properties.getProperty("browsercontrol_win_x86_jar");
            } else {
                browser = properties.getProperty("codebase", "http://world16.runescape.com/k=3/") + properties.getProperty("browsercontrol_win_amd64_jar");
            }
            loader = new URLClassLoader(new URL[]{url, new URL(browser)});
        } else {
            loader = new URLClassLoader(new URL[]{url});
        }
        return (Applet) loader.loadClass(properties.getProperty("initial_class", "Rs2Applet.class").replace(".class", "")).newInstance();
    }

    private void loadParams() throws IOException {
        URL url = new URL(paramURL);
        BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
        String input;
        while ((input = read.readLine()) != null) {
            if (input.startsWith("msg=")) {
                String s = input.substring("msg=".length());
                String[] parts = s.split("=", 2);
                if (parts.length == 2) {
                    messages.put(parts[0], parts[1]);
                }
            } else if (input.startsWith("param=")) {
                String s = input.substring("param=".length());
                String[] parts = s.split("=", 2);
                if (parts.length == 2) {
                    parameters.put(parts[0], parts[1]);
                }
            } else {
                String[] parts = input.split("=", 2);
                if (parts.length == 2) {
                    properties.put(parts[0], parts[1]);
                }
            }
        }
    }

    public static void main(String[] args) {
        String lang = "0";
        boolean noLimits = false;
        if (args.length >= 1) {
            String language = args[0];
            if (language.length() == 1) {
                lang = language;
            }
            if (args.length >= 2) {
                String toLimit = args[1].toLowerCase().trim();
                noLimits = toLimit.equals("1") || toLimit.contains("true") || toLimit.equals("yes");
            }
        }
        new RSLoader("http://www.runescape.com/k=3/l=" + lang + "/jav_config.ws", noLimits);
    }
}
