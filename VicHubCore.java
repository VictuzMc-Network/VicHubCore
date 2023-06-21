package com.eliseo190.main;

import com.eliseo190.main.Commands.Admin.ReloadManagerCMD;
import com.eliseo190.main.Commands.Others.Gamemode;
import com.eliseo190.main.Commands.Outhers.Fly;
import com.eliseo190.main.Commands.Outhers.Icons.SetTagCommand;
import com.eliseo190.main.Commands.Outhers.MenussCMD.VMenuCMD;
import com.eliseo190.main.Commands.vCoreCMD;
import com.eliseo190.main.Manager.Chat.ChatListener;;
import com.eliseo190.main.Manager.Modules.TagsManager;
import com.eliseo190.main.Manager.PlacehondersExtencion.PlacehonderExtencion;
import com.eliseo190.main.Manager.ScoreManager.ScoreManager;
import com.eliseo190.main.Manager.menus.AparienciaGuiManager;
import com.eliseo190.main.Manager.menus.PerfilGuiManager;
import com.eliseo190.main.Manager.menus.TagsGuiManager;
import com.eliseo190.main.Utils.Colors;

import net.luckperms.api.LuckPerms;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class VicHubCore extends JavaPlugin {
    private static VicHubCore instance;
    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private FileConfiguration config;
    private Logger logger;
    private ScoreManager scoreManager;
    private TagsManager tagsManager;

    // Menus
    private PerfilGuiManager perfilGuiManager;
    private TagsGuiManager tagsGuiManager;
    private AparienciaGuiManager aparienciaGuiManager;
    // FIN MENUS

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        scoreManager = new ScoreManager(this);
        tagsManager = new TagsManager(this);
        perfilGuiManager = new PerfilGuiManager(this, scoreManager);
        tagsGuiManager = new TagsGuiManager(this);
        aparienciaGuiManager = new AparienciaGuiManager(this);
        ScoreManager scoreManager = new ScoreManager(this);

        scoreManager.ScoreboardManagerA(Integer.valueOf(getConfig().getString("Scoreboard.ticks")));

        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&c&lVicHubCore &fv1.0 &8&l| &fCreated for"));
            console.sendMessage(Colors.translate("&r   &7xAEliseo & Forgez LLC"));
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&r  &4&lYOU MUST INSTALL LUCKPERMS"));
            console.sendMessage(Colors.translate("&r     &4&lTO FUNCTION PROPERLY"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&r &c&lVicHubCore &fv1.0 &8&l| &fCreated for"));
            console.sendMessage(Colors.translate("&r   &7xAEliseo & Forgez LLC"));
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&4&lYOU MUST INSTALL PLACEHONDERSAPI"));
            console.sendMessage(Colors.translate("&r     &4&lTO FUNCTION PROPERLY"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&c&lVicHubCore &fv1.0 &8&l| &fCreated for"));
            console.sendMessage(Colors.translate("&r   &7xAEliseo & Forgez LLC"));
            console.sendMessage(Colors.translate(""));
            console.sendMessage(Colors.translate("&c[VHC] &fCargando dependencias..."));
            console.sendMessage(Colors.translate("&r &aLuckperms - PlahceholdersAPI"));
            console.sendMessage(Colors.translate(""));
        }
        registerCommands();
        registerEvents();
        saveDefaultConfig();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlacehonderExtencion expansion = new PlacehonderExtencion(tagsManager, scoreManager);
            expansion.register();
        } else {
            getLogger().warning("PlaceholderAPI no está instalado. Las variables personalizadas no funcionarán correctamente.");
        }
    }

    @Override
    public void onDisable() {
    }

    private boolean setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        return provider != null;
    }

    private LuckPerms getLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            return provider.getProvider();
        }
        return null;
    }

    private void registerCommands() {
        getCommand("vcore").setExecutor(new vCoreCMD(this));
        getCommand("vreload").setExecutor(new ReloadManagerCMD(this, scoreManager, perfilGuiManager, tagsGuiManager, aparienciaGuiManager));
        getCommand("vmenu").setExecutor(new VMenuCMD(perfilGuiManager, tagsGuiManager));

        getCommand("vtags").setExecutor(new SetTagCommand());
        getCommand("fly").setExecutor(new Fly());
        getCommand("gm").setExecutor(new Gamemode());

        AparienciaGuiManager guiManager = new AparienciaGuiManager(this);

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(perfilGuiManager, this);
        //getServer().getPluginManager().registerEvents(new weather(), this);
    }

    public static VicHubCore getInstance() {
        return instance;
    }
}