package com.redsmods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Config {
    private static Config INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = Platform.getConfigFolder().resolve("SoundPhysicsPerfected.json");

    // RaysCast setting
    public int raysCast = 128;
    public int raysBounced = 3;
    public boolean reverbEnabled = true;
    public boolean permeationEnabled = true;
    public int maxRayLength = 4; // chunks
    public float SoundMult = 1; // make it just work like default Minecraft for lag helping :)
    public int tickRate = 5; // once every 5 ticks
    public RedsAttenuationType attenuationType = RedsAttenuationType.INVERSE_SQUARE;

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadConfig();
        }
        return INSTANCE;
    }

    private static Config loadConfig() {
        Config config;

        if (Files.exists(CONFIG_FILE)) {
            try {
                String json = Files.readString(CONFIG_FILE);
                config = GSON.fromJson(json, Config.class);
            } catch (Exception e) {
                System.err.println("Failed to load config, using defaults: " + e.getMessage());
                config = new Config();
            }
        } else {
            config = new Config();
            config.save(); // Create default config file
        }

        return config;
    }

    public void save() {
        try {
            // Ensure config directory exists
            Files.createDirectories(CONFIG_FILE.getParent());

            String json = GSON.toJson(this);
            Files.writeString(CONFIG_FILE, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    public void reload() {
        INSTANCE = loadConfig();
    }
}