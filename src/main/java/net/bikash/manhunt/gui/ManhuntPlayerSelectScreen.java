package net.bikash.manhunt.gui;

import net.bikash.manhunt.network.ManhuntAdminNetwork;
import net.bikash.manhunt.network.ManhuntRoleSelectionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManhuntPlayerSelectScreen extends Screen {


    private String selectedrunner = null;
    private String selectedonlineplayer = null;
    private final java.util.Set<String> selectedhunters =
            new java.util.HashSet<>();
    private final java.util.Set<String> selectedspectators =
            new java.util.HashSet<>();
    private int refreshtimer = 0;
    private int scroolOffset = 0;
    private int maxScrool = 0;

    private int panelLeft;
    private int panelRight;
    private String roleMenuPlayer = null;
    private int roleMenuX ;
    private int roleMenuY;


    private String errorMessage = "";


    private java.util.Set<String> proviousPlayers = new java.util.HashSet<>();


    public ManhuntPlayerSelectScreen (){
        super(Component.literal("Select Players"));
    }

    public int getPanelWidth(){
        return Math.min(800,Math.max(1,this.width-20));
    }
    private int getPanelHeight(){
        return Math.min(400,Math.max(1,this.height-20));
    }


    @Override
    protected void init(){

        if(!ManhuntAdminNetwork.isAdmin){
            this.minecraft.setScreen(new ManhuntStartScreen());
            return;
        }

int panelwidth = getPanelWidth();
int panelheight = getPanelHeight();

int left = (this.width - panelwidth)/2;
int top = (this.height - panelheight)/2;

        panelLeft = left;
        panelRight = left + panelwidth;

        int padding = 16;
        int gap = 12;

        int availableWidth = panelwidth - padding * 2 - gap * 3;
        int columnWidth = availableWidth / 4;

        int buttonwidth = Math.max(1,columnWidth - 12);
        int buttonheight = 18;


        int runnerColumnX = left + padding;
        int hunterColumnX = runnerColumnX + columnWidth + gap;
        int spectatorsColumnX = hunterColumnX + columnWidth + gap;
        int onlineColumnX = spectatorsColumnX + columnWidth + gap;

        int runnerX = runnerColumnX + (columnWidth - buttonwidth)/2;
        int hunterX = hunterColumnX + (columnWidth - buttonwidth)/2;
        int spectatorsX = spectatorsColumnX + (columnWidth-buttonwidth)/2;
        int onlineX = onlineColumnX + (columnWidth-buttonwidth)/2;

        int scrollTop = top+85;
        int scrollBottom =Math.max(scrollTop,top+panelheight-45);

        int runnerstartY = scrollTop;
        int hunterstartY = scrollTop;

        int runnermaxY = scrollBottom;
        int huntermaxY = scrollBottom;



        //calculating how far we can scroll

        int totalPlayers = 0;
        if(this.minecraft.getConnection()!=null){
            totalPlayers = this.minecraft.getConnection().getOnlinePlayers().size();
        }
int visiblePlayers = Math.max(1,(scrollBottom - scrollTop) /22);

        maxScrool = Math.max( 0,
                (totalPlayers-visiblePlayers)*22
        );

        if(scroolOffset > maxScrool){
            scroolOffset = maxScrool;
        }

        //runner selection
     if(this.minecraft.getConnection()!=null){

         int playerY = runnerstartY-scroolOffset;


         for(var playerInfo : this.minecraft.getConnection().getOnlinePlayers()) {

             if(playerY > runnermaxY){
                 break;
             }




             String name = playerInfo.getProfile().name();

             if(!name.equals(selectedrunner)){
                 continue;
             }

             if(playerY + buttonheight < scrollTop || playerY > scrollBottom){
                 playerY += 22;
                 continue;
             }

             Button runnerButton = Button.builder(
                     Component.literal(
                             (name.equals(selectedrunner)?"[✓]" : "[ ]")+name),

                     button -> {
                         errorMessage = "";
                        if(name.equals(selectedrunner)){
                            selectedrunner=null;
                        }else{
                            selectedrunner = name;
                            selectedhunters.remove(name);
                            selectedspectators.remove(name);
                        }
                        System.out.println("RUNNER SELECTED: "+selectedrunner);
                         //the screen refreshes so the chekbox updates
                         this.clearWidgets();
                         this.init();


                     }
             ).bounds(
                     runnerX,
                     playerY,
                     buttonwidth,
                     buttonheight
             ).build();
             this.addRenderableWidget(runnerButton);
             playerY += 22;

         }
     }

     //hunters selection

        if(this.minecraft.getConnection()!=null){
            int hunterY = hunterstartY-scroolOffset;


            for( var playerInfo : this.minecraft.getConnection().getOnlinePlayers()){
                String name = playerInfo.getProfile().name();

                if (hunterY > huntermaxY) {
                    break;
                }

                //not both runner and hunter means a runner cannot be a  hunter


                if(!selectedhunters.contains(name)){
                    continue;
                }

                if(hunterY + buttonheight < scrollTop || hunterY >scrollBottom){
                    hunterY+=22;
                    continue;
                }

                Button hunterButton= Button.builder(
                        Component.literal(
                                (selectedhunters.contains(name)? "[✓]":"[ ]")+ name ),
                                button -> {

                                    errorMessage = "";

                                    if(selectedhunters.contains(name)) {
                                        selectedhunters.remove(name);
                                        selectedspectators.remove(name);
                                    }
                                    else{
                                        selectedhunters.add(name);

                                        if(name.equals(selectedrunner)){
                                            selectedrunner = null;
                                        }
                                    }
                                    System.out.print("HUNTERS: "+selectedhunters);
                                    this.clearWidgets();
                                    this.init();
                                }
                         ).bounds(
                                hunterX,
                                hunterY,
                                buttonwidth,
                                buttonheight
                        ).build();
                this.addRenderableWidget(hunterButton);
                hunterY+=22;

            }
        }




        // Spectators selection

        if(this.minecraft.getConnection() != null){
            int spectatorsY = runnerstartY - scroolOffset;

            for(var playerInfo: this.minecraft.getConnection().getOnlinePlayers()){
                String name = playerInfo.getProfile().name();

                if(spectatorsY > huntermaxY){
                    break;
                }

                if(!selectedspectators.contains(name)){
                    continue;
                }
                if(spectatorsY+buttonheight<scrollTop || spectatorsY>scrollBottom){
                    spectatorsY += 22;
                    continue;
                }

                Button spectatorButton = Button.builder(
                        Component.literal(
                                (selectedspectators.contains(name) ? "[✓]":"[ ]")+name
                        ),
                        button -> {
                            errorMessage = "";

                            if(selectedspectators.contains(name)){
                                selectedspectators.remove(name);
                            }
                            else{
                                selectedspectators.add(name);
                            }
                            this.clearWidgets();
                            this.init();
                        }
                ).bounds(
                        spectatorsX,
                        spectatorsY,
                        buttonwidth,
                        buttonheight
                ).build();
                this.addRenderableWidget(spectatorButton);
                spectatorsY+=22;
            }
        }

        //Online players Button

        if(this.minecraft.getConnection()!=null) {
            int onlineY = runnerstartY - scroolOffset;

            for (var playerInfo : this.minecraft.getConnection().getOnlinePlayers()) {
                String name = playerInfo.getProfile().name();

                if(name.equals(selectedrunner) || selectedhunters.contains(name)|| selectedspectators.contains(name)){
                    onlineY+=22;
                    continue;
                }

                if(onlineY > huntermaxY){
                    break;
                }

                if(onlineY + buttonheight < scrollTop || onlineY > scrollBottom){
                    onlineY+=22;
                    continue;
                }int buttonY = onlineY;

                Button onlineButton = Button.builder(
                        Component.literal(
                                (name.equals(selectedonlineplayer)?">" : "")+name
                        ),
                        button -> {
                           openRoleMenu(name,onlineX,buttonY);
                        }

                ).bounds(
                        onlineX,
                        onlineY,
                        buttonwidth,
                        buttonheight
                ).build();

                this.addRenderableWidget(onlineButton);
                onlineY+=22;

            }

        }

        // done button

        int doneWidth = 100;
        int doneHeight = 20;

        int doneX = left + (panelwidth - doneWidth) / 2;
        int doneY = top + panelheight - doneHeight - 10;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("DONE"),
                        button -> {



                            if(this.minecraft.getConnection()==null){
                                return;
                            }

                            java.util.UUID runnerUUID = null;

                            java.util.List<java.util.UUID> hunterUUIDs = new java.util.ArrayList<>();

                            for(var playerInfo: this.minecraft.getConnection().getOnlinePlayers()) {
                                String name = playerInfo.getProfile().name();

                                java.util.UUID uuid = playerInfo.getProfile().id();

                                //converting runner name into uuid

                                if (name.equals(selectedrunner)) {
                                    runnerUUID = uuid;
                                }
                                //convrting hunters name into uuids

                                if (selectedhunters.contains(name)) {
                                    hunterUUIDs.add(uuid);
                                }
                            }

                            System.out.println("RUNNER UUID: " + runnerUUID);
                            System.out.println("HUNTER UUIDS: " + hunterUUIDs);

                            java.util.List<java.util.UUID> spectatorsUUIDs = new java.util.ArrayList<>();

                            for(String name : selectedspectators){
                                var player = this.minecraft.player.connection.getOnlinePlayers().stream()
                                        .filter(p -> p.getProfile().name().equals(name))
                                        .findFirst()
                                        .orElse(null);

                                if(player!=null){
                                    spectatorsUUIDs.add(player.getProfile().id());
                                }
                            }

                            if (selectedrunner == null && selectedhunters.isEmpty() && selectedspectators.isEmpty()) {
                                this.onClose();
                                return;
                            }

                            ClientPlayNetworking.send(
                                    new ManhuntRoleSelectionPayload(
                                            runnerUUID,
                                            hunterUUIDs,
                                            spectatorsUUIDs
                                    )
                            );

                            this.onClose();

                        }
                ).bounds(
                        doneX,
                      doneY,
                        doneWidth,
                        doneHeight
                ).build()
        );
        createRoleMenu();
    }



    private void openRoleMenu(String name , int x , int y){
        roleMenuPlayer = name;
        roleMenuX = x;
        roleMenuY = y;

        this.clearWidgets();
        this.init();
    }

    private void createRoleMenu(){
        if(roleMenuPlayer == null){
            return;
        }


int menuWidth = 120;
        int menuHeight = 64;


        int menuX = Math.max(
                panelLeft + 5,
                Math.min(roleMenuX,panelRight - menuWidth - 5)
        );

        int menuY = Math.max(0,Math.min(roleMenuY + 18 , this.height- menuHeight - 5));

        this.addRenderableWidget(
                Button.builder(Component.literal("♟ RUNNER").withStyle(ChatFormatting.GREEN), button -> {
                    assignRole(roleMenuPlayer,"runner");
                }).bounds(
                        menuX,
                        menuY,
                        menuWidth,
                        16
                ).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("⚔ Hunter").withStyle(ChatFormatting.RED), button -> {
                    assignRole(roleMenuPlayer,"hunter");
                }).bounds(
                        menuX,
                        menuY +16,
                        menuWidth,
                        16
                ).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("◉ Spectator").withStyle(ChatFormatting.BLUE), button -> {
                    assignRole(roleMenuPlayer,"spectator");
                }).bounds(
                        menuX,
                        menuY + 32,
                        menuWidth,
                        16
                ).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("× Unassigned").withStyle(ChatFormatting.GRAY), button -> {
                    assignRole(roleMenuPlayer,"unassigned");
                }).bounds(
                        menuX,
                        menuY +48,
                        menuWidth,
                        16
                ).build()
        );


    }

    @Override
    public void tick(){
        refreshtimer++;
        if(refreshtimer >=10){
            refreshtimer=0;

           if(this.minecraft.getConnection()==null){
               return;
           }
           java.util.Set<String> currentPlayers = new java.util.HashSet<>();

           for(var playerInfo:this.minecraft.getConnection().getOnlinePlayers()){
               currentPlayers.add(
                       playerInfo.getProfile().name()
               );
           }
           if(!currentPlayers.equals(proviousPlayers)){
               proviousPlayers = currentPlayers;

               this.clearWidgets();
               this.init();
           }
        }
    }


    //mouse wheel scrolling funcction:


@Override
public boolean mouseScrolled(
        double mouseX,
        double mouseY,
        double scrollX,
        double scrollY
){

        int panelwidth = getPanelWidth();
        int panelheight = getPanelHeight();

        int left = (this.width-panelwidth)/2;
        int top = (this.height - panelheight)/2;

        int scrollTop = (this.height-getPanelHeight())/2+90;
        int scrollBotoom = (this.height+getPanelHeight())/2-45;

        if(mouseX<left|| mouseX>left+panelwidth || mouseY < scrollTop || mouseY > scrollBotoom){

            return false;
        }

        scroolOffset -= (int) (scrollY *22);

    scroolOffset = Math.max(0, scroolOffset);
    scroolOffset = Math.min(maxScrool, scroolOffset);

    this.clearWidgets();
        this.init();
        return true;
}

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        //making the bg dark

        graphics.fill(
                0, 0, this.width, this.height, 0x66000000
        );

//panel
        int panelwidth = getPanelWidth();
        int panelheight = getPanelHeight();

        int left = (this.width - panelwidth) / 2;
        int top = (this.height - panelheight) / 2;
        int right = left + panelwidth;
        int bottom = top + panelheight;

        graphics.fill(
                left,
                top,
                right,
                bottom,
                0xEE151515
        );

        // top of border
        graphics.fill(
                left,
                top,
                right,
                top + 2,
                0xFF55FF55
        );

        int lineTop = top +50;
        int lineBottom = top + panelheight - 50;

        int padding = 16;
        int gap = 12;
        int columnWidth = Math.max(1,(panelwidth - padding * 2 - gap * 3) / 4);
        int separator1 = left + padding + columnWidth + gap / 2;
        int separator2 = separator1 + columnWidth + gap;
        int separator3 = separator2 + columnWidth + gap;

        graphics.fill(
                separator1,
                lineTop,
                separator1+1,
                lineBottom,
                0xFF55FF55

        );

        graphics.fill(
                separator2,
                lineTop,
                separator2+1,
                lineBottom,
                0xFF55FF55
        );

        graphics.fill(
                separator3,
                lineTop,
                separator3+1,
                lineBottom,
                0xFF55FF55
        );


        int runnerCenter = left + padding + columnWidth / 2;
        int hunterCenter = runnerCenter + columnWidth + gap;
        int spectatorsCenter = hunterCenter + columnWidth + gap;
        int onlineCenter = spectatorsCenter + columnWidth + gap;


        String title = "SELECT PLAYERS";

        graphics.text(
                this.font,
                title,
                this.width / 2 - this.font.width(title) / 2,
                top + 35,
                0xFFFFFFFF,
                true
        );

//runner player
        graphics.text(
                this.font,
                "RUNNER",
                runnerCenter - font.width("RUNNER")/2,
                top + 60,
                0xFF55FF55,
                true
        );


        graphics.text(
                this.font,
                "HUNTERS",
                hunterCenter - font.width("HUNTERS") / 2,
                top + 60,
                0xFFFF5555,
                true
        );

        graphics.text(
                this.font,
                "SPECTATORS",
                spectatorsCenter - font.width("SPECTATORS") / 2,
                top + 60,
                0xFF55AAFF, true
        );

        graphics.text(
                this.font,
                "ONLINE PLAYERS",
                onlineCenter - font.width("ONLINE PLAYERS") / 2,
                top + 60,
                0xFFFFFFFF, true
        );


        if (!errorMessage.isEmpty()) {
            graphics.text(
                    this.font,
                    errorMessage,
                    left + 40,
                    top + panelheight - 55,
                    0xFFFF5555,
                    true
            );
        }


        super.extractRenderState(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );





    }


    private void assignRole(String name, String role){
        selectedhunters.remove(name);
        selectedspectators.remove(name);

        if(name.equals(selectedrunner)){
            selectedrunner = null;
        }


        switch(role) {
            case "runner" -> selectedrunner = name;
            case "hunter" -> selectedhunters.add(name);
            case "spectator" -> selectedspectators.add(name);
        }

        roleMenuPlayer = null;
        errorMessage = "";

        this.clearWidgets();
        this.init();
    }

   
    }
