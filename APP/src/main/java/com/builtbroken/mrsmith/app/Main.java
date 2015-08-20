package com.builtbroken.mrsmith.app;

import com.builtbroken.mrsmith.app.gui.GuiFrame;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Dark on 8/19/2015.
 */
public class Main
{
    public static void main(String... args) throws URISyntaxException
    {
        File home_folder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        File mc_bin = new File(home_folder, "mc_bin");

        System.out.println("*****************************************************");
        System.out.println("\tMr.Smith ");
        System.out.println("\tVersion 0.0.1 ");
        System.out.println("*****************************************************\n");
        System.out.println("\tHome: " + home_folder);


        boolean noGui = false;

        if (args != null && args.length > 0)
        {
            for (int i = 0; i < args.length; i++)
            {
                String s = args[i];
                System.out.println("Program Arg[" + i + "] " + s);
                boolean valid = (i + 1 < args.length) && !args[i + 1].contains("-");
                String next_s = valid ? args[i + 1] : null;
                if (s.startsWith("-"))
                {
                    String var = s.replace("-", "");
                    if (var.equalsIgnoreCase("help"))
                    {
                        System.out.println("In order for the program to run correctly " +
                                "you need to add some arguments. This way we can build " +
                                "the page information correctly.\n\n");
                        System.out.println("Valid program arguments");
                        System.out.println("-NoGui  -> optional, Forces the program to run in cmd mode");
                        System.exit(0);
                    }
                    else if (next_s != null)
                    {
                        if (valid)
                        {
                            i++;
                        }
                        if (var.equalsIgnoreCase("NoGui"))
                        {
                            noGui = true;
                        }
                    }
                }
            }
        }

        try
        {
            Smith smith = new Smith(home_folder, noGui);

            if (!noGui)
            {
                final JFrame f = new GuiFrame(smith);
                f.setVisible(true);
            }
        } catch (Exception e)
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, ("It looks like something went wrong while running Mr Smith. Check the logs\n for the full error report and feel free to report it to the authors of the APP.\n\nError Message: \n    " + e.getClass()).replace("class", "") + (e.getMessage() != null ? ": " + e.getMessage() : ""), "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        finally
        {
            System.out.println("*****************************************************");
        }

    }
}
