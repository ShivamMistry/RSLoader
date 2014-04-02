package com.speed;

import javax.swing.*;
import java.awt.*;

/**
 * See license.txt for licensing information.
 *
 * @author Shivam Mistry
 */
public class LoadingDialog extends JDialog {


    private final GridBagLayout gbLayout;
    private String currentMessage;
    private JLabel messageLabel;
    private JProgressBar progressBar;

    public LoadingDialog(String msg) {
        //initialise fields and interface
        this.currentMessage = msg;
        this.messageLabel = new JLabel(currentMessage);
        this.progressBar = new JProgressBar();
        setLayout(gbLayout = new GridBagLayout());
        gbLayout.columnWidths = new int[]{1, 1, 1, 1, 1};
        gbLayout.columnWeights = new double[]{0.2, 0.2, 0.2, 0.2, 0.2};
        gbLayout.rowWeights = new double[]{0.25, 0.25, 0.25};
        progressBar.setMaximum(1000);
        progressBar.setMinimum(0);
        //add components and position them
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 1;
        gbcLabel.gridy = 0;
        gbcLabel.gridheight = 1;
        gbcLabel.gridwidth = 1;
        add(messageLabel, gbcLabel);
        GridBagConstraints gbcProgress = new GridBagConstraints();
        gbcProgress.gridx = 1;
        gbcProgress.gridy = 1;
        gbcProgress.gridheight = 2;
        gbcProgress.gridwidth = 2;
        add(progressBar, gbcProgress);
        //set window specific parameters
        setSize(200, 100);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("RuneScape Loading");
        setVisible(true);
    }


    public void addProgress(double percent) {
        progressBar.setValue((int) (progressBar.getValue() + percent * 10));
    }

    public void end() {
        setVisible(false);
        dispose();
    }

    public void setMessage(String msg) {
        messageLabel.setText(msg);
        this.currentMessage = msg;
    }

}
