package com.builtbroken.mrsmith.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dark on 8/19/2015.
 */
public class Smith
{
    public File root_folder;
    public File home_folder;
    public File bin_folder;
    public File mods_folder;

    public final Config config;
    public boolean noGui = false;

    public List<String> mc_version = new ArrayList();
    public HashMap<String, List<String>> forge_versions = new HashMap();

    public Smith(File home_folder, boolean noGui)
    {
        this.noGui = noGui;
        //Create default files
        this.root_folder = home_folder;
        this.home_folder = home_folder;
        this.bin_folder = new File(home_folder.getParentFile(), "mc_bin");
        this.mods_folder = new File(home_folder.getParentFile(), "mods");

        //Load config
        this.config = new Config(this);
        config.load();

        //Init config files
        this.home_folder = new File(config.home_dir);
        this.bin_folder = new File(config.bin_dir);
        this.mods_folder = new File(config.mods_dir);

        if (!home_folder.exists())
            home_folder.mkdirs();
        if (!bin_folder.exists())
            bin_folder.mkdirs();
        if (!mods_folder.exists())
            mods_folder.mkdirs();

        try
        {
            File mcFile = new File(bin_folder, "data/mc_versions.json");
            File forgeFile = new File(bin_folder, "data/forge_versions.json");
            if (!mcFile.exists())
            {
                URL inputUrl = getClass().getResource("/data/mc_versions.json");
                if (inputUrl != null)
                {
                    FileUtils.copyURLToFile(inputUrl, mcFile);
                }
                else
                {
                    if (!noGui)
                        JOptionPane.showMessageDialog(null, "Failed to find mc_versions.json", "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Failed to locate mv_versions.json in the jar");
                    System.exit(-1);
                }
            }
            if (!forgeFile.exists())
            {
                URL inputUrl = getClass().getResource("/data/forge_versions.json");
                if (inputUrl != null)
                {
                    FileUtils.copyURLToFile(inputUrl, forgeFile);
                }
                else
                {
                    if (!noGui)
                        JOptionPane.showMessageDialog(null, "Failed to find forge_versions.json", "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Failed to locate forge_versions.json in the jar");
                    System.exit(-1);
                }
            }
        } catch (IOException e)
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, "Failed to move files to bin folder", "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
            System.exit(-1);
        }

        loadMCVersions();
        loadForgeVersions();
        //TODO download maven info for MrSmith mod for each MC version
        //TODO download updated mc and forge version files
    }

    public void save()
    {

    }

    public void run(String mc_version, String forge_version)
    {
        if (this.mc_version.contains(mc_version) && this.forge_versions.containsKey(mc_version))
        {
            if (this.forge_versions.get(mc_version) != null && this.forge_versions.get(mc_version).contains(forge_version))
            {
                if (!isMCDownloaded(mc_version))
                {
                    boolean download = true;
                    if (!noGui && JOptionPane.showConfirmDialog(null, "MC version " + mc_version + " is not downloaded!\n\nWould you like to download this file?", "Missing File!", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                    {
                        download = false;
                    }

                    if (download)
                    {
                        downloadMC(mc_version);
                    }
                    else
                    {
                        return;
                    }
                }
                if (!isForgeDownloaded(forge_version))
                {
                    boolean download = true;
                    if (!noGui && JOptionPane.showConfirmDialog(null, "Forge version " + forge_version + " is not downloaded!\n\nWould you like to download this file?", "Missing File!", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                    {
                        download = false;
                    }

                    if (download)
                    {
                        downloadForge(mc_version);
                    }
                    else
                    {
                        return;
                    }
                }
            }
            else
            {
                if (!noGui)
                    JOptionPane.showMessageDialog(null, "Invalid forge version " + mc_version, "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);
                System.out.println("Invalid mc version " + mc_version);
            }
        }
        else
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, "Invalid mc version " + mc_version, "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);
            System.out.println("Invalid mc version " + mc_version);
        }
    }

    public boolean isMCDownloaded(String version)
    {
        return false;
    }

    public boolean isForgeDownloaded(String version)
    {
        return false;
    }

    public void downloadForge(String version)
    {

    }

    public void downloadMC(String version)
    {

    }

    public void exit()
    {
        System.exit(0);
    }

    private void loadMCVersions()
    {
        System.out.println("Loading mc versions.json");
        File file = new File(bin_folder, "data/mc_versions.json");

        String data = readFile(file.getPath());
        if (data != null)
        {
            JsonObject obj = new JsonParser().parse(data).getAsJsonObject();
            JsonArray arr = obj.getAsJsonArray("versions");
            for (int i = 0; i < arr.size(); i++)
            {
                String version = arr.get(i).toString().replace("{\"version\":\"", "").replace("\"}", "");
                if (!mc_version.contains(version))
                {
                    mc_version.add(version);
                }
            }
        }
        else
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, "Failed to load MC version json", "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);

            System.out.println("Failed to load MC version json");
            System.exit(-1);
        }
    }

    private void loadForgeVersions()
    {
        System.out.println("Loading forge versions.json");
        File file = new File(bin_folder, "data/forge_versions.json");

        String data = readFile(file.getPath());
        if (data != null)
        {
            for (String mc : mc_version)
            {
                List<String> list = new ArrayList();
                JsonObject obj = new JsonParser().parse(data).getAsJsonObject();
                JsonArray arr = obj.getAsJsonArray(mc);
                for (int i = 0; i < arr.size(); i++)
                {
                    String version = arr.get(i).toString().replace("{\"version\":\"", "").replace("\"}", "");
                    if (!list.contains(version))
                    {
                        list.add(version);
                    }
                }
                forge_versions.put(mc, list);
            }
        }
        else
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, "Failed to load MC version json", "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);

            System.out.println("Failed to load MC version json");
            System.exit(-1);
        }
    }

    protected String readFile(String filename)
    {
        String result = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (FileNotFoundException e)
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, ("Failed to read file " + filename + ".\n\nError Message: \n    " + e.getClass()).replace("class", "") + (e.getMessage() != null ? ": " + e.getMessage() : ""), "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e)
        {
            if (!noGui)
                JOptionPane.showMessageDialog(null, ("Failed to load file " + filename + ".\n\nError Message: \n    " + e.getClass()).replace("class", "") + (e.getMessage() != null ? ": " + e.getMessage() : ""), "Fatal Exception in Runtime", JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }
}
