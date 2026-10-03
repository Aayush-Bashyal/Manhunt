package net.bikash.manhunt.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.PublishCommand;
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
                    20,
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

    Button startButton  = Button.builder(
            Component.translatable("lanServer.start"),
            button -> {
                if(singleplayerServer.publishServer(
                        this.gameMode,
                        this.commands,
                        this.port
                )){
                    Component message = PublishCommand.getSuccessMessage(this.port);

                    this.minecraft.getNarrator().saySystemQueued(
                            message
                    );

                    this.minecraft.updateTitle();

                    this.minecraft.setScreen(
                            this.lastScreen
                    );
                }
            }
    ).bounds(
            this.width/2-155,
            this.height-28,
            150,
            20
    ).build();
    this.addRenderableWidget(startButton);

    this.addRenderableWidget(
            Button.builder(
                    CommonComponents.GUI_CANCEL,
                    button->this.onClose()
            ).bounds(
                    this.width/2+5,
                    this.height-28,
                    150,
                    20
            ).build()
    );

}
@Override
    public void onClose(){
        this.minecraft.setScreen(this.lastScreen);
}
@Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
){
        super.extractRenderState(

                graphics,
                mouseX,
                mouseY,
                partialTick
        );
        graphics.centeredText(
                this.font,
                this.title,
                this.width/2,
                50,
                0xFFFFFFFF

        );
        graphics.centeredText(
                this.font,
                Component.translatable("lanServer.otherPlayers"),
                this.width/2,
                82,
                0xFFFFFFFF
        );
        graphics.centeredText(
                this.font,
                Component.translatable("lanServer.port"),
                        this.width/2,
                        142,
                        0xFFFFFFFF

        );
}
}
