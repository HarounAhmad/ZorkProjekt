package ch.bbw.zork;

import java.util.*;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.net.HttpHeader;
import com.teamdev.jxbrowser.net.HttpStatus;
import com.teamdev.jxbrowser.net.Network;
import com.teamdev.jxbrowser.net.UrlRequestJob;
import com.teamdev.jxbrowser.net.callback.InterceptRequestCallback;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * The type Game.
 *
 * @author Kemal Catan
 * @author Haroun Ahmad
 * @author Vethusan keteeswaran
 * @author Jeremy Nigg <br> <b>Main Game of Zork</b>
 * @version 1.0
 * @since 02.12.2020
 */

public class Game {

    /**
     * The Item list.
     */
    ArrayList<Items> itemList = new ArrayList<Items>();

    /**
     * The Name.
     */
    String name; // Variable für den Namen
    /**
     * The Name 1.
     */
    String name1 = "weise"; // Variable für den Namen
    /**
     * The Name 4.
     */
    String name4 = "einen anderen namen"; // Variable für den Namen

    /**
     * The Ausdauer.
     */
    int ausdauer = 10; // Charakter Attribut
    /**
     * The Gluck.
     */
    int gluck = 10; // Charakter Attribut
    /**
     * The Max weight.
     */
    float maxWeight = 10;
    /**
     * The Current weight.
     */
    float currentWeight = 0;
    /**
     * The Mapsehen.
     */
    int mapsehen;

    /**
     * The Rand.
     */
    Random rand = new Random(); // Zufallszahl verbindung

    /**
     * The Zufall.
     */
    int zufall;

    private final Parser parser;
    private Room currentRoom;
    private final Room flugzeug;
    private final Room wald;
    private final Room hoechster_punkt;
    private final Room fluss;
    private final Room hoehle;
    private final Room encounter;
    private final Room funkturm;
    private final Room stadt;

    /**
     * The Has item 1.
     */
    boolean hasItem1 = false, /**
     * The Has item 2.
     */
    hasItem2 = false;

    private static final String CONTENT_TYPE = "Content-Type";
    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();

    /**
     * Instantiates a new Game.
     */
    public Game() {

        parser = new Parser(System.in);

        flugzeug = new Room("Absturzort beim Flugzeug", "flugzeug");
        wald = new Room("Wald, vor dem Absturzort", "wald");
        hoechster_punkt = new Room("Kleiner Berg, höchster Punkt, Map ist hier einsehbar", "berg");
        fluss = new Room("Ruhiger Fluss mit Fischen, Ausdauer wird um 3 Punkte aufgefüllt", "fluss");
        hoehle = new Room("Höhle, Ausdauer wird um 3 Punkte aufgefüllt", "hoehle");
        encounter = new Room("Achtung, zufallsbegegnung mit Tier", "encounter");
        funkturm = new Room("Funkturm, hilfe anfordern wenn Funkgerät und Mikrofon dabei", "funkturm");
        stadt = new Room("Stadt, du hast es geschafft, du bist entkommen und hast überlebt!", "stadt");

        flugzeug.setExits(null, wald, null, null);
        wald.setExits(null, hoechster_punkt, hoehle, null);
        hoechster_punkt.setExits(null, fluss, encounter, wald);
        fluss.setExits(null, null, null, hoechster_punkt);
        hoehle.setExits(wald, encounter, null, null);
        encounter.setExits(hoechster_punkt, funkturm, null, hoehle);
        funkturm.setExits(fluss, null, null, encounter);
        stadt.setExits(null, null, null, null);

        currentRoom = flugzeug; // start des Spiels
    }

    /**
     * Randomization int.
     *
     * @return the int
     */
    public int randomization() {
        return rand.nextInt(5);
    }

    /**
     * Zufallsrechner int.
     *
     * @return the int
     */
    public int zufallsrechner() {
        zufall = gluck - randomization();
        System.out.println(zufall);

        if (zufall <= 0) {
            System.out.println("\nEs ist ein Bär, du bist am Arsch");
            ausdauer -= 3;
            System.out.println("Deine Ausdauer beträgt " + ausdauer);
            return ausdauer;

        } else if (zufall == 1) {
            System.out.println("\nEs ist ein Wolf, Pech gehabt, du verlierst einen Ausdauer Punkt");
            ausdauer -= 1;
            System.out.println("Deine Ausdauer beträgt " + ausdauer);
            return ausdauer;
        } else {
            System.out.println(
                    "\nEs ist ein Hase, glück gehabt, du fängst und isst es, deine Ausdauer lädt sich um 2 Punkte auf");
            ausdauer += 2;
            System.out.println("Deine Ausdauer beträgt " + ausdauer);
            return ausdauer;
        }


    }

    /**
     * Play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Danke " + name + " das du mein Spiel gespielt hast, vielleicht schaffst du es beim nächsten mal!");
    }

    /**
     * Compare string boolean.
     *
     * @param var1 the var 1
     * @param var2 the var 2
     * @return the boolean
     */
    public static boolean CompareString(String var1, String var2) {
        return var1.equals(var2);
    }

    /**
     * Charakter.
     */
    public void charakter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zu erst brauchen wir einen Ausdauer und danach Glückswert, ACHTUNG, beide Werte zusammen müssen 10 ergeben \n( Tipp, Ausdauer sollte mindestens 4 sein )");
        System.out.println("\n\nBitte Ausdauerwert eingeben");
        try {
            ausdauer = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Eingabe Falsch");
        }

        System.out.println("Bitte Glückswert eingeben");
        try {
            gluck = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Eingabe Falsch");
        }
        if (gluck + ausdauer > 10) {
            System.out.println("Zu hohe Werte");
            charakter();
        } else if (gluck + ausdauer < 10) {
            System.out.println("Zu niedrige Werte");
            charakter();
        } else {
            printWelcome2();
        }
    }

    /**
     * Print welcome.
     */
    public void printWelcome() {

        System.out.println("Wie heisst du, wähle weise, du kannst es später nicht mehr ändern");
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        if (CompareString(name, name1)) {
            System.out.println("Haha, lustig. Tippe einen anderen Namen");
            name = scanner.nextLine();
        }
        if (CompareString(name, name4)) {
            System.out.println("So, das reicht, du heisst nun 'Klugscheisser'");
            String name5 = "Klugscheisser";
            name = name5;
        }
        System.out.println();
        System.out.println("Willkommen " + name + " zum Zork-Spiel\n");
        System.out.println("Zork ist ein einfaches Text-Adventure Spiel\n");
        charakter();
    }

    private void printWelcome2() {
        System.out.println("\nTippe 'hilfe' für deine möglichen Befehle\n");
        System.out.println();
        System.out.println(currentRoom.longDescription());
    }

    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("Ich weiss nicht was du meinst");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {
            case "hilfe":
                printHelp();
                break;
            case "items":
                itemsanzeigen();
                break;
            case "gehe":
                goRoom(command);

                break;
            case "beenden":
                if (command.hasSecondWord()) {
                    System.out.println("was beenden?");
                } else {
                    return true; // signal that we want to quit
                }
                break;
            case "drop":
                System.out.println("Welches item wollen sie fallen lassen?");
                Scanner sc = new Scanner(System.in);
                itemsanzeigen();
                int wahl = sc.nextInt();
                itemList.remove(wahl);

                if (wahl == 0) {
                    hasItem1 = false;
                }
                if (wahl == 1) {
                    hasItem2 = false;
                }

                break;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("Du bist abgestürzt und alleine, das Flugzeug hat beim Bruchlanden einen Funkturm getroffen.\nSieht so aus als ob ein Teil beim Fluss und ein Teil bei der Höhle zerschellt ist.\nDa solltest du nach den Items suchen, ohne die wirst du nicht nach hilfe rufen können und auch nicht überleben");
        System.out.println("Du bist am Absturzort");
        System.out.println();
        System.out.println("Deine möglichen Befehle:");
        System.out.println(parser.showCommands());
    }

    /**
     * Add item.
     *
     * @param item the item
     */
    public void AddItem(Items item) {
        itemList.add(item);
    }

    /**
     * Itemsanzeigen.
     */
    public void itemsanzeigen() {
        for (Items items : itemList) {
            String itemname = items.Itemname;
            float Gewicht = items.WeightKG;
            int id = items.ID;
            System.out.println("--------------------------");
            System.out.println("ID:      " + id);
            System.out.println("Name:    " + itemname);
            System.out.println("Gewicht: " + Gewicht);
            System.out.println("--------------------------");
        }
    }


    /**
     *
     * @param command
     * <h1>goRoom</h1>
     *
     */


    private void goRoom(Command command) {
        Scanner sc = new Scanner(System.in);
        String ItemPickup;

        if (!command.hasSecondWord()) {
            System.out.println("Wohin gehen?");
        } else {

            String direction = command.getSecondWord();

            // Try to leave current room.
            Room nextRoom = currentRoom.nextRoom(direction);


            if (nextRoom == null)
                System.out.println("Da gibts kein weg!");
            else {
                ausdauer = ausdauer - 1;
                if (ausdauer > 0) {
                    currentRoom = nextRoom;
                    if (nextRoom == fluss) {
                        ausdauer = ausdauer + 3;
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        if (!hasItem1 && currentWeight <= maxWeight) {
                            System.out.println(currentRoom.longDescription());
                            System.out.println("Du hast einen Mikrofon gefunden willst du es aufnehmen? y/n");
                            ItemPickup = sc.nextLine().toLowerCase();
                            if (ItemPickup.equals("y")) {
                                ausdauer -= 1;
                                System.out.println("Du hast einen Mikrofon gefunden");
                                String Mikrofonname = "Mikrofon";
                                Items Mikrofon = new Items();
                                Mikrofon.setitemname(Mikrofonname);
                                Mikrofon.setWeightKG(5f);
                                Mikrofon.ID = itemList.size();
                                AddItem(Mikrofon);
                                hasItem1 = true;
                                currentWeight += 5;
                            } else {
                                System.out.println("Du hast den item nicht mitgenommen");
                            }
                        }
                    } else if (nextRoom == hoechster_punkt) {
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        System.out.println(currentRoom.longDescription());
                        System.out.println("Map ansehen? \n<1>Ja\nNein, drück was dein Herz begehrt");
                        try {
                            mapsehen = sc.nextInt();
                            if (mapsehen == 1) {
                                DrawMap();

                            }
                        } catch (Exception e) {
                            System.out.println("Exits: sueden, norden, osten, westen");

                        }

                    } else if (nextRoom == hoehle) {
                        ausdauer = ausdauer + 3;
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        if (!hasItem2 && currentWeight <= maxWeight) {
                            System.out.println("Du hast einen Mikrofon gefunden willst du es aufnehmen? y/n");
                            ItemPickup = sc.nextLine().toLowerCase();
                            if (ItemPickup.equals("y")) {
                                ausdauer -= 1;
                                System.out.println("Du hast einen Funkgerät gefunden");
                                String Mikrofonname = "Funkgerät";
                                Items Mikrofon = new Items();
                                Mikrofon.setWeightKG(5);
                                Mikrofon.setitemname(Mikrofonname);

                                Mikrofon.ID = itemList.size();
                                AddItem(Mikrofon);
                                hasItem2 = true;
                                currentWeight += 5;
                            }
                        }
                        System.out.println(currentRoom.longDescription());
                    } else if (nextRoom == wald) {
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        System.out.println(currentRoom.longDescription());
                    } else if (nextRoom == funkturm) {
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        System.out.println(currentRoom.longDescription());
                        System.out.println("du hast: " + itemList.size() + " items");
                        switch (itemList.size()) {
                            case 0:
                                System.out.println("Du hast keine von den beiden items");
                                break;
                            case 1:
                                System.out.println("Du hast nur einen. hol den anderen noch!");
                                break;
                            case 2:
                                System.out.println("Du hast die nötigen Teile dabei, \ndu rufst nach hilfe, wirst abgeholt und in Sicherheit gebracht");
                                System.out.println("Du hast es geschafft, du hast überlebt");
                                System.out.println("Gratulation " + name);
                                System.exit(0);
                                break;
                        }
                    } else if (nextRoom == encounter) {
                        System.out.println("Deine Ausdauer beträgt " + ausdauer + "\n");
                        System.out.println("Achtung, zufallsbegegnung mit Tier");
                        System.out.println("Dein Glück: " + gluck + " 		Zufallswert: " + randomization()
                                + "\n\n2\tHase: du fängst es, Grillst es, isst es und bekommst +2 Ausdauer\n1\tWolf: du wirst gejagt, leicht verletzt aber entkommst, -1 Ausdauer\n0\tBär: Machen wirs kurz, du bist am Arsch, -3 Ausdauer");
                        if (zufallsrechner() <= 0) {
                            System.out.println(name + ", du bist gestorben");
                            System.exit(0);
                        }

                    }
                } else {
                    System.out.println("Deine Ausdauer beträgt " + ausdauer);
                    System.out.println(name + ", du bist gestorben");
                    System.exit(0);
                }
            }
        }


    }


    /**
     * Draw map.
     */
    void DrawMap() {
        Engine engine = Engine.newInstance( // Checks for support, Finds chromium binaries, runs main process of Chromium, set's up IPC connection
                EngineOptions.newBuilder(HARDWARE_ACCELERATED) //adds Engine option for Hardware Acceleration
                        .licenseKey("1BNDHFSC1FXLNPUTMBJ7OZ28NTZLHM2WONZCAA47E3Y08JV1PPDH762JZCQVAA00XDIKSZ")
                        .build());
        Browser browser = engine.newBrowser(); // creates a new Browser with default context

        SwingUtilities.invokeLater(() -> {
            BrowserView view = BrowserView.newInstance(browser);

            JFrame frame = new JFrame("Map");
            frame.add(view, BorderLayout.CENTER);
            frame.setSize(2395, 1222);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        Network network = engine.network();
        network.set(InterceptRequestCallback.class, params -> {
            try {
                URL url = new URL(params.urlRequest().url());
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                byte[] data = new byte[dataInputStream.available()];
                dataInputStream.readFully(data);
                dataInputStream.close();

                String mimeType = getMimeType(params.urlRequest().url());
                UrlRequestJob urlRequestJob = engine.network().newUrlRequestJob(
                        UrlRequestJob.Options.newBuilder(params.urlRequest().id(), HttpStatus.OK)
                                .addHttpHeader(HttpHeader.of(CONTENT_TYPE, mimeType))
                                .build());
                urlRequestJob.write(data);
                urlRequestJob.complete();
                return InterceptRequestCallback.Response.intercept(urlRequestJob);
            } catch (IOException e) {
                return InterceptRequestCallback.Response.proceed();
            }
        });

        URL resource = Game.class.getResource("index.html");
        if (resource != null) {
            browser.navigation().loadUrl(resource.toString());
        }
    }

    static { // sets compatability for languages to run on chromium
        MIME_TYPE_MAP.put(".html", "text/html");
        MIME_TYPE_MAP.put(".js", "text/javascript");
        MIME_TYPE_MAP.put(".css", "text/css");
    }

    private static String getMimeType(String path) {
        String extension = path.substring(path.lastIndexOf("."));
        if (MIME_TYPE_MAP.containsKey(extension)) {
            return MIME_TYPE_MAP.get(extension);
        }
        return "";
    }

}

