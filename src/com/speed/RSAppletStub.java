package com.speed;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * See license.txt for licensing information.
 *
 * @author Shivam Mistry
 */
public class RSAppletStub implements AppletStub, AppletContext {

    private final Properties parameters, properties;

    public RSAppletStub(Properties parameters, Properties properties) {
        this.parameters = parameters;
        this.properties = properties;
    }

    public boolean isActive() {
        return false;
    }

    public URL getDocumentBase() {
        return getCodeBase();
    }

    public URL getCodeBase() {
        try {
            return new URL(properties.getProperty("codebase", "http://world16.runescape.com/k=3/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getParameter(String name) {
        return parameters.getProperty(name);
    }

    public AppletContext getAppletContext() {
        return this;
    }

    public void appletResize(int width, int height) {
    }

    public AudioClip getAudioClip(URL url) {
        return null;
    }

    public Image getImage(URL url) {
        return null;
    }

    public Applet getApplet(String name) {
        return null;
    }

    public Enumeration<Applet> getApplets() {
        return null;
    }

    public void showDocument(final URL url) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(url.toURI());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(url);
            }
        });

    }

    public void showDocument(final URL url, final String target) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(url.toURI());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(url);
                System.out.println(target);
            }
        });

    }

    public void showStatus(String status) {
        System.out.println(status);
    }

    public void setStream(String key, InputStream stream) throws IOException {
    }

    public InputStream getStream(String key) {
        return null;
    }

    public Iterator<String> getStreamKeys() {
        return null;
    }
}
