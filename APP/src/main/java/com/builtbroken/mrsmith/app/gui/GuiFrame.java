package com.builtbroken.mrsmith.app.gui;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Dark on 8/19/2015.
 */
public class GuiFrame extends JFrame implements ActionListener
{
    File home_folder;
    File mc_bin;

    JTextField workingDirectoryField;
    private JComboBox mcVersionBox;

    public GuiFrame(File home_folder, File mc_bin)
    {
        super("Mr Smith");
        this.home_folder = home_folder;
        this.mc_bin = mc_bin;

        int width = 330;
        int height = 170;

        JLabel label1 = new JLabel("Working Dir: ");
        workingDirectoryField = new JTextField(40);
        workingDirectoryField.setText(mc_bin.getPath());

        mcVersionBox = new JComboBox();
        mcVersionBox.addItem("1.7.10");
        mcVersionBox.addItem("1.6.4");
        mcVersionBox.setEditable(false);

        JPanel mcPanel = new JPanel(new BorderLayout());
        mcPanel.add(new JLabel("MC Versions: "), BorderLayout.WEST);
        mcPanel.add(mcVersionBox, BorderLayout.CENTER);

        JPanel workDirPanel = new JPanel(new BorderLayout());
        workDirPanel.add(label1, BorderLayout.WEST);
        workDirPanel.add(workingDirectoryField, BorderLayout.CENTER);


        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(workDirPanel, BorderLayout.NORTH);
        contentPanel.add(mcPanel, BorderLayout.SOUTH);
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

    @Override
    public void actionPerformed(ActionEvent e)
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
