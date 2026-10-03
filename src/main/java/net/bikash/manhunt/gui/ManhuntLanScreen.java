package net.bikash.manhunt.gui;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.level.GameType;
import org.jspecify.annotations.Nullable;

public class ManhuntLanScreen extends Screen {

    private final Screen lastScreen;

    private GameType gameMode = GameType.SURVIVAL;
    private boolean commands;
    private int port;

    private @Nullable EditBox portEdit;

    public ManhuntLanScreen(Screen lastScreen){
        super(Component.translatable("lanServer.title"));

        this.lastScreen = lastScreen;
        this.port = HttpUtil.getAvailablePort();

    }

@Override
    protected void init(){
        IntegratedServer singleplayerServer = this.minecraft.getSingleplayerServer();

        this.gameMode = singleplayerServer.getDefaultGameType();

        this.commands = singleplayerServer.getWorldData().isAllowCommands();

    //adding game mode selection
    this.addRenderableWidget(
            CycleButton.builder(GameType::getShortDisplayName,this.gameMode)
                    .withValues(
                            GameType.SURVIVAL,
                            GameType.ADVENTURE,
                            GameType.CREATIVE,
                            GameType.SPECTATOR
                    ).create(
                            this.width/2-155,
                    100,
                    150,
                    10,
                    Component.translatable("selectWorld.gameMode"),
                            (button,value)->this.gameMode = value
                    )
    );

    //Allow commands
    this.addRenderableWidget(
            CycleButton.onOffBuilder(this.commands)
                    .create(
                            this.width/2+5,
                            100,
                            150,
                            20,
                            Component.translatable("selectWorld.allowCommands"),
                            (button,value)->this.commands = value
                    )
    );
    this.portEdit = new EditBox(
            this.font,
            this.width/2-75,
            160,
            150,
            20,
            Component.translatable("lanServer.port")
            );
    this.portEdit.setHint(
            Component.literal(""+this.port)
    );
    this.addRenderableWidget(this.portEdit);

}
}
