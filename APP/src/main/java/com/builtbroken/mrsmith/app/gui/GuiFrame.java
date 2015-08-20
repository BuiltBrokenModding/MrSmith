package com.builtbroken.mrsmith.app.gui;


import com.builtbroken.mrsmith.app.Smith;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Dark on 8/19/2015.
 */
public class GuiFrame extends JFrame implements ActionListener
{
    private JTextField workingDirectoryField;
    private JTextField binDirectoryField;
    private JTextField modsDirectoryField;

    private JComboBox mcVersionBox;
    private JComboBox forgeVersionBox;
    private JComboBox smithVersionBox;

    private Smith smith;

    public GuiFrame(Smith smith)
    {
        super("Mr Smith");
        this.smith = smith;

        int width = 330;
        int height = 170;

        //TODO load valid MC versions from json
        mcVersionBox = new JComboBox();
        for (String v : smith.mc_version)
        {
            mcVersionBox.addItem(v);
        }
        mcVersionBox.setEditable(false);
        mcVersionBox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent event)
            {
                if (event.getStateChange() == ItemEvent.SELECTED)
                    reloadForgeVersions();
            }
        });

        forgeVersionBox = new JComboBox();
        reloadForgeVersions();
        forgeVersionBox.setEditable(false);

        smithVersionBox = new JComboBox();
        smithVersionBox.addItem("1");
        smithVersionBox.addItem("2");
        smithVersionBox.setEditable(false);
        smithVersionBox.setEnabled(false);

        JPanel versionPanel = versionPanel(new JPanel(new GridLayout(1, 3)));
        JPanel dirPanel = dirPanel(new JPanel(new GridLayout(3, 1)));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(dirPanel, BorderLayout.NORTH);
        contentPanel.add(versionPanel, BorderLayout.SOUTH);
        contentPanel.setBorder(new TitledBorder("Data"));

        setContentPane(contentPanel);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });

        pack();
        if (width < getWidth())
            width = getWidth();
        if (height < getHeight())
            height = getHeight();
        centerOnScreen(width, height);
    }

    private void reloadForgeVersions()
    {
        forgeVersionBox.removeAllItems();
        for (String v : smith.forge_versions.get(mcVersionBox.getItemAt(mcVersionBox.getSelectedIndex())))
        {
            forgeVersionBox.addItem(v);
        }
    }

    private JPanel versionPanel(JPanel panel)
    {
        JPanel mcPanel = new JPanel(new BorderLayout());
        mcPanel.add(new JLabel("MC: "), BorderLayout.WEST);
        mcPanel.add(mcVersionBox, BorderLayout.CENTER);

        JPanel forgePanel = new JPanel(new BorderLayout());
        forgePanel.add(new JLabel("Forge: "), BorderLayout.WEST);
        forgePanel.add(forgeVersionBox, BorderLayout.CENTER);

        JPanel smithPanel = new JPanel(new BorderLayout());
        smithPanel.add(new JLabel("Smith: "), BorderLayout.WEST);
        smithPanel.add(smithVersionBox, BorderLayout.CENTER);

        panel.add(mcPanel);
        panel.add(forgePanel);
        panel.add(smithPanel);
        panel.setBorder(new TitledBorder("Versions"));
        return panel;
    }

    private JPanel dirPanel(JPanel panel)
    {
        workingDirectoryField = new JTextField(40);
        workingDirectoryField.setText(smith.home_folder.getPath());

        JPanel workDirPanel = new JPanel(new BorderLayout());
        workDirPanel.add(new JLabel("Bin: "), BorderLayout.WEST);
        workDirPanel.add(workingDirectoryField, BorderLayout.CENTER);

        binDirectoryField = new JTextField(40);
        binDirectoryField.setText(smith.bin_folder.getPath());

        JPanel configDirPanel = new JPanel(new BorderLayout());
        configDirPanel.add(new JLabel("Config: "), BorderLayout.WEST);
        configDirPanel.add(binDirectoryField, BorderLayout.CENTER);

        modsDirectoryField = new JTextField(40);
        modsDirectoryField.setText(smith.mods_folder.getPath());

        JPanel modsDirPanel = new JPanel(new BorderLayout());
        modsDirPanel.add(new JLabel("Mods: "), BorderLayout.WEST);
        modsDirPanel.add(modsDirectoryField, BorderLayout.CENTER);

        panel.add(workDirPanel);
        panel.add(configDirPanel);
        panel.add(modsDirPanel);
        panel.setBorder(new TitledBorder("Folders"));
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {

    }

    public void centerOnScreen(int width, int height)
    {
        int top, left, x, y;

        // Get the screen dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the location for the top left corner of the frame
        x = (screenSize.width - width) / 2;
        y = (screenSize.height - height) / 2;
        left = (x < 0) ? 0 : x;
        top = (y < 0) ? 0 : y;

        // set the frame to the specified location & size
        this.setBounds(left, top, width, height);
    }

}
