package com.builtbroken.mrsmith.app;

import java.io.*;
import java.util.Properties;

/**
 * Created by Dark on 8/19/2015.
 */
public class Config
{
    public Properties props = new Properties();

    private Smith smith;
    private File file;

    public String home_dir;
    public String bin_dir;
    public String mods_dir;

    public Config(Smith smith)
    {
        this.smith = smith;
        this.file = new File(smith.root_folder, "settings.cfg");
    }

    public void create()
    {
        props = new Properties();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            props.setProperty("home_dir", smith.home_folder.getPath());
            props.setProperty("bin_dir", smith.bin_folder.getPath());
            props.setProperty("mods_dir", smith.mods_folder.getPath());
            props.store(out, "");
            out.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean load()
    {
        if (!file.exists())
            create();
        if (file.exists())
        {
            try
            {
                InputStream inputStream = new FileInputStream(file);
                if (inputStream != null)
                {
                    try
                    {
                        props.load(inputStream);
                        home_dir = props.getProperty("home_dir");
                        bin_dir = props.getProperty("bin_dir");
                        mods_dir = props.getProperty("mods_dir");
                        inputStream.close();
                    } catch (IOException e)
                    {
                        System.out.println("Failed to load config file");
                        e.printStackTrace();
                    }
                    return true;
                }
            } catch (FileNotFoundException e)
            {
            }
        }
        return false;
    }
}
