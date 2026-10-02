package net.bikash.manhunt.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManhuntPlayerSelectScreen extends Screen {


    private String selectedrunner = null;
    private final java.util.Set<String> selectedhunters =
            new java.util.HashSet<>();
    private int refreshtimer = 0;

    private java.util.Set<String> proviousPlayers = new java.util.HashSet<>();


    public ManhuntPlayerSelectScreen (){
        super(Component.literal("Select Players"));
    }



    @Override
    protected void init(){




        int buttonwidth = 120;
        int buttonheight = 20;


        int x = (this.width-buttonwidth)/2;


        //runner selection
     if(this.minecraft.getConnection()!=null){

         int playerY = (this.height - 300) / 2 + 78;

         for(var playerInfo : this.minecraft.getConnection().getOnlinePlayers()) {
             String name = playerInfo.getProfile().name();
             Button runnerButton = Button.builder(
                     Component.literal(
                             (name.equals(selectedrunner)?"[✓]" : "[ ]")+name),

                     button -> {
                         selectedrunner = name;
                         System.out.println(
                                 "RUNNER SELECTED" + selectedrunner);

                         //the screen refreshes so the chekbox updates
                         this.clearWidgets();
                         this.init();


                     }
             ).bounds(
                     x,
                     playerY,
                     120,
                     20
             ).build();
             this.addRenderableWidget(runnerButton);
             playerY += 25;

         }
     }

     //hunters selection

        if(this.minecraft.getConnection()!=null){
            int hunterY = (this.height - 300)/2 + 178;


            for( var playerInfo : this.minecraft.getConnection().getOnlinePlayers()){
                String name = playerInfo.getProfile().name();

                //not both runner and hunter means a runner cannot be a  hunter


                if(name.equals(selectedrunner)){
                    continue;
                }
                Button hunterButton= Button.builder(
                        Component.literal(
                                (selectedhunters.contains(name)? "[✓]":"[ ]"+name )),
                                button -> {
                                    if(selectedhunters.contains(name)) {
                                        selectedhunters.remove(name);
                                    }
                                    else{
                                        selectedhunters.add(name);
                                    }
                                    System.out.print("HUNTERS: "+selectedhunters);
                                    this.clearWidgets();
                                    this.init();
                                }
                        ).bounds(
                                x,
                                hunterY,
                                120,
                                20
                        ).build();
                this.addRenderableWidget(hunterButton);
                hunterY+=25;

            }
        }

        // done button

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("DONE"),
                        button -> {
                            this.onClose();
                        }
                ).bounds(
                        x,
                        this.height /2 +60,
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


    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ){
        //making the bg dark

        graphics.fill(
                0,0,this.width,this.height,  0x66000000
        );

//panel
        int panelwidth = 400;
        int panelheight = 300;

        int left = (this.width - panelwidth)/2;
        int top = (this.height - panelheight)/2;
        int right = left + panelwidth;
        int bottom = top+panelheight;

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

        super.extractRenderState(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    }
