package com.speed;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.Properties;

/**
 * See license.txt for licensing information.
 *
 * @author Shivam Mistry
 */
public class RSLoader extends JFrame {

    private final String paramURL;
    private final Properties properties, parameters, messages;
    private LoadingDialog dialog;
    private URLClassLoader loader;

    public RSLoader(final String paramURL, boolean noLimits, boolean decorated) {
        this.paramURL = paramURL;
        properties = new Properties();
        parameters = new Properties();
        messages = new Properties();
        try {
            dialog = new LoadingDialog("Loading parameters");
            dialog.setVisible(true);
            loadParams();
            dialog.setMessage("Creating applet stub");
            RSAppletStub stub = new RSAppletStub(parameters, properties);
            dialog.addProgress(5);
            dialog.setMessage("Loading applet");
            Applet applet = loadClasses();
            dialog.addProgress(20);
            dialog.setMessage("Creating GUI");
            setPreferredSize(new Dimension(Integer.parseInt(properties.getProperty("window_preferredwidth", "1024")),
                    Integer.parseInt(properties.getProperty("window_preferredheight", "768"))));
            if (!noLimits) {
                String appletMinW = properties.getProperty("applet_minwidth", "765");
                String appletMinH = properties.getProperty("applet_minheight", "540");
                String appletMaxW = properties.getProperty("applet_maxwidth", "3200");
                String appletMaxH = properties.getProperty("applet_maxheight", "1200");
                applet.setMinimumSize(new Dimension(Integer.parseInt(appletMinW), Integer.parseInt(appletMinH)));
                applet.setMaximumSize(new Dimension(Integer.parseInt(appletMaxW), Integer.parseInt(appletMaxH)));
            } else {
                applet.setMinimumSize(null);
                applet.setMaximumSize(null);
            }
            setUndecorated(!decorated);
            applet.setStub(stub);
            setTitle("RuneScape");
            add(applet);
            dialog.addProgress(5);
            dialog.setMessage("Initialising applet");
            dialog.setMessage("Opening GUI");
            dialog.addProgress(5);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dialog.end();
            setVisible(true);
            pack();
            applet.init();
            applet.start();
        } catch (Exception e) {
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
        dialog.addProgress(5);
        return (Applet) loader.loadClass(properties.getProperty("initial_class", "Rs2Applet.class").replace(".class", "")).newInstance();
    }

    private void loadParams() throws IOException {
        URL url = new URL(paramURL);
        BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
        dialog.addProgress(10);
        String input;
        LinkedList<String> paramLines = new LinkedList<String>();
        while ((input = read.readLine()) != null) {
            paramLines.add(input);
        }
        double progressPerLine = (50d / paramLines.size());
        System.out.println(progressPerLine);
        for (String line : paramLines) {
            if (line.startsWith("msg=")) {
                String s = line.substring("msg=".length());
                String[] parts = s.split("=", 2);
                if (parts.length == 2) {
                    messages.put(parts[0], parts[1]);
                }
            } else if (line.startsWith("param=")) {
                String s = line.substring("param=".length());
                String[] parts = s.split("=", 2);
                if (parts.length == 2) {
                    parameters.put(parts[0], parts[1]);
                }
            } else {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    properties.put(parts[0], parts[1]);
                }
            }
            dialog.addProgress(progressPerLine);
        }
    }

    public static void main(String[] args) {
        String lang = "0";
        boolean noLimits = false;
        boolean decorated = true;
        boolean oldschool = false;
        String configUrl = null;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (args.length >= 1) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-l") || args[i].equals("--lang")) {
                    lang = args[++i];
                    if (lang.equalsIgnoreCase("fr") || lang.equalsIgnoreCase("french")) {
                        lang = "2";
                    } else if (lang.equalsIgnoreCase("de") || lang.equalsIgnoreCase("german")) {
                        lang = "1";
                    } else if (lang.equalsIgnoreCase("br") || lang.equalsIgnoreCase("brazillian") || lang.equalsIgnoreCase("portuguese")) {
                        lang = "3";
                    } else if (lang.equalsIgnoreCase("es") || lang.equalsIgnoreCase("spanish")) {
                        lang = "6";
                    }
                } else if (args[i].equals("-nl") || args[i].equals("--nolimits") || args[i].equals("-n")) {
                    if (args[i].equals("-nl")) {
                        System.out.println("Option -nl is deprecated and will be removed in a future release");
                    }
                    noLimits = true;
                } else if (args[i].equals("-u") || args[i].equals("--undecorated")) {
                    decorated = false;
                } else if (args[i].equals("-o") || args[i].equals("--oldschool") || args[i].equals("-os")) {
                    oldschool = true;
                } else if (args[i].equals("-c") || args[i].equals("--config")) {
                    configUrl = args[++i];
                }
                else {
                    System.out.println("Option " + args[i] + " not recognised and will be ignored.");
                }
            }
        }
        if (configUrl != null) {
            new RSLoader(configUrl, noLimits, decorated);
        } else if (oldschool) {
            new RSLoader("http://oldschool.runescape.com/jav_config.ws", noLimits, decorated);
        } else {
            new RSLoader("http://www.runescape.com/k=3/l=" + lang + "/jav_config.ws", noLimits, decorated);
        }
    }
}
