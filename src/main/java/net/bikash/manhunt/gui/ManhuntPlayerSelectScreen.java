package net.bikash.manhunt.gui;

import net.bikash.manhunt.network.ManhuntRoleSelectionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManhuntPlayerSelectScreen extends Screen {


    private String selectedrunner = null;
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


    private String errorMessage = "";


    private java.util.Set<String> proviousPlayers = new java.util.HashSet<>();


    public ManhuntPlayerSelectScreen (){
        super(Component.literal("Select Players"));
    }



    @Override
    protected void init(){

int panelwidth = 800;
int panelheight = 300;

int left = (this.width - panelwidth)/2;
int top = (this.height - panelheight)/2;

        panelLeft = left;
        panelRight = left + panelwidth;

        int buttonwidth = 170;
        int buttonheight = 20;

        int runnerX = left + 20 ;
        int hunterX = left +215;
        int spectatorsX = left + 410;
        int onlineX = left + 605;



        int runnerstartY = top+82;
        int hunterstartY = top+182;

        int runnermaxY = top+155;
        int huntermaxY = top+panelheight-65;

        int scrollTop = top+75;
        int scrollBottom = top+panelheight-55;

        //calculating how far we can scroll

        int totalPlayers = 0;
        if(this.minecraft.getConnection()!=null){
            totalPlayers = this.minecraft.getConnection().getOnlinePlayers().size();
        }
int visiblePlayers = 3;

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

             if(selectedhunters.contains(name)){
                 continue;
             }

             if(playerY + buttonheight < scrollTop || playerY > scrollBottom){
                 playerY += 25;
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
             playerY += 25;

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


                if(name.equals(selectedrunner)){
                    continue;
                }

                if(hunterY + buttonheight < scrollTop || hunterY >scrollBottom){
                    hunterY+=25;
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
                hunterY+=25;

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

                if(name.equals(selectedrunner) || selectedhunters.contains(name)){
                    continue;
                }
                if(spectatorsY+buttonheight<scrollTop || spectatorsY>scrollBottom){
                    spectatorsY += 25;
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
                spectatorsY+=25;
            }
        }

        //Online players Button

        if(this.minecraft.getConnection()!=null) {
            int onlineY = runnerstartY - scroolOffset;

            for (var playerInfo : this.minecraft.getConnection().getOnlinePlayers()) {
                String name = playerInfo.getProfile().name();

                if(onlineY > huntermaxY){
                    break;
                }

                if(onlineY + buttonheight < scrollTop || onlineY > scrollBottom){
                    onlineY+=25;
                    continue;
                }

                Button onlineButton = Button.builder(
                        Component.literal(name),
                        button -> {
                            errorMessage = "Select a role using the role columns.";

                        }

                ).bounds(
                        onlineX,
                        onlineY,
                        buttonwidth,
                        buttonheight
                ).build();

                this.addRenderableWidget(onlineButton);
                onlineY+=25;

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

        int panelwidth = 400;
        int panelheight = 300;

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
        int panelwidth = 400;
        int panelheight = 300;

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
                left + 40,
                top + 60,
                0xFF55FF55,
                true
        );


        graphics.text(
                this.font,
                "HUNTERS",
                left + 40,
                top + 160,
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



        super.extractRenderState(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );





    }


    }
