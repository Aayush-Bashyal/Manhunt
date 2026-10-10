package net.bikash.manhunt.gui;

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
    private int scrollBottom;
    private int panelLeft;
    private int panelRight;
    private String roleMenuPlayer = null;
    private int roleMenuX ;
    private int roleMenuY;
    private int roleMenuWidth = 140;
    private int roleMenuHeight = 100;

    private String errorMessage = "";


    private java.util.Set<String> proviousPlayers = new java.util.HashSet<>();


    public ManhuntPlayerSelectScreen (){
        super(Component.literal("Select Players"));
    }



    @Override
    protected void init(){

int panelwidth = 800;
int panelheight = 400;

int left = (this.width - panelwidth)/2;
int top = (this.height - panelheight)/2;

        panelLeft = left;
        panelRight = left + panelwidth;

        int buttonwidth = 160;
        int buttonheight = 18;

        int runnerX = left + 20 ;
        int hunterX = left +215;
        int spectatorsX = left + 410;
        int onlineX = left + 605;



        int runnerstartY = top+110;
        int hunterstartY = top+110;

        int runnermaxY = top+155;
        int huntermaxY = top+panelheight-65;

        int scrollTop = top+110;
        int scrollBottom = top+panelheight-55;

        //calculating how far we can scroll

        int totalPlayers = 0;
        if(this.minecraft.getConnection()!=null){
            totalPlayers = this.minecraft.getConnection().getOnlinePlayers().size();
        }
int visiblePlayers = Math.max(1,(scrollBottom - scrollTop) /22);

        maxScrool = Math.max( 0,
                (totalPlayers-visiblePlayers)*25
        );

        if(scroolOffset > maxScrool){
            scroolOffset = maxScrool;
        }

        //runner selection
     if(this.minecraft.getConnection()!=null){

         int playerY = runnerstartY-scroolOffset;
         int hunterY = hunterstartY - scroolOffset;

         for(var playerInfo : this.minecraft.getConnection().getOnlinePlayers()) {

             if(playerY > runnermaxY){
                 break;
             }

             if(hunterY > huntermaxY){
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

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("DONE"),
                        button -> {
                            if(selectedrunner==null){
                                errorMessage = "Please select a Runner!";
                                return;
                            }



                            if(selectedhunters.isEmpty()){
                                errorMessage = "Please select at least one Hunter!";
                                return;
                            }


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
                                if(runnerUUID==null){
                                    System.out.println("RUNNER UUID NOT FOUND");
                                    return;
                                }
                                System.out.println("RUNNER UUID: "+runnerUUID);
                            System.out.print("HUNTER UUIDS: "+hunterUUIDs);

                            ClientPlayNetworking.send(
                                    new ManhuntRoleSelectionPayload(
                                            runnerUUID,
                                            hunterUUIDs
                                    )
                            );

                            this.onClose();

                        }
                ).bounds(
                        (this.width - buttonwidth)/2,
                      top+panelheight-40,
                        buttonwidth,
                        buttonheight
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

        int panelwidth = 800;
        int panelheight = 400;

        int left = (this.width-panelwidth)/2;
        int top = (this.height - panelheight)/2;

        int scrollTop = top +75;
        int scrollBotoom = top + panelheight-55;

        if(mouseX<left|| mouseX>left+panelwidth || mouseY < scrollTop || mouseY > scrollBotoom){

            return false;
        }

        scroolOffset -= (int) (scrollY *15);

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
        int panelwidth = 800;
        int panelheight = 400;

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
                left + 25,
                top + 60,
                0xFF55FF55,
                true
        );


        graphics.text(
                this.font,
                "HUNTERS",
                left + 215,
                top + 60,
                0xFFFF5555,
                true
        );

        graphics.text(
                this.font,
                "SPECTATORS",
                left + 410,
                top + 60,
                0xFF55AAFF, true
        );

        graphics.text(
                this.font,
                "ONLINE PLAYERS",
                left + 605,
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

if(roleMenuPlayer!=null) {
    int menuX = Math.max(
            panelLeft + 5,
            Math.min(roleMenuX, panelRight - roleMenuWidth - 5)
    );

    int menuY = Math.max(0, Math.min(roleMenuY + 18,
            this.height - roleMenuHeight - 5));

//runner bg
    graphics.fill(
            menuX,
            menuY + 22,
                menuX+roleMenuWidth,menuY + 40,
            0xFF245C24
            );
//hunter
    graphics.fill(
            menuX,
            menuY + 42,
            menuX+roleMenuWidth,menuY + 60,
            0xFF702020
    );

    graphics.fill(
            menuX,
            menuY + 62,
            menuX+roleMenuWidth,menuY + 80,
            0xFF204A70
    );

    graphics.fill(
            menuX,
            menuY + 82,
            menuX+roleMenuWidth,menuY + 100,
            0xFF404040
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

    private void assignSelectedPlayer(String role) {
        if (selectedonlineplayer == null) {
            errorMessage = "Select a player from ONLINE PLAYERS first!";
            return;
        }

        String name = selectedonlineplayer;

        selectedhunters.remove(name);
        selectedspectators.remove(name);

        if ("runner".equals(role)) {
            selectedrunner = name;
        } else if ("hunter".equals(role)) {
            if (name.equals(selectedrunner)) {
                selectedrunner = null;
            }
            selectedhunters.add(name);
        } else if ("spectator".equals(role)) {
            if (name.equals(selectedrunner)) {
                selectedrunner = null;
            }
            selectedspectators.add(name);
        }

        errorMessage = name + " assigned as " + role;
        this.clearWidgets();
        this.init();
    }
    }
