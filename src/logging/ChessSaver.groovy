package logging

import groovy.xml.MarkupBuilder
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.stage.Stage
import main.Main

import java.text.SimpleDateFormat
import java.util.concurrent.atomic.AtomicInteger
/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    19.05.17
 * Poject:  jessy
 * Package: logging
 * Desc.:   The ChessSaver takes care of saving and loading ChessGames.
 *          This Class is built around the Singleton design-pattern
 */
class ChessSaver {

    /**
     * The Reference to the private Object. This will be used to enable object
     * oriented programming with a static variable
     */
    private static ChessSaver saver = new ChessSaver()

    /**
     * This Method returns the static reference to the ChessSaver object
     * @return The reference to saver
     */
    static ChessSaver getInstance() {
        return saver
    }

    /**
     * The startFEN of a fresh start
     */
    private String startFEN
    /**
     * A map which holds all the save-slots. An AtomicInteger takes care of
     * enumerating the elements, where each number holds an Array consisting of
     * the Date of the Save and the FEN
     */
    private saves = [:]
    /**
     * A counter which is responsible for enumerating the unique elements of the
     * saves-Map
     */
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Initializes everything, from Loading all the save-games and setting the
     * backup FEN.
     * @param FEN The FEN which will be used at a fresh start
     * @return This method Returns itself to enable a Builder-Style programming
     */
    ChessSaver init(String FEN) {
        this.loadFENsFromXML("jessy.sav.xml")
        startFEN = FEN
        return this;
    }

    /**
     * Loads all the Saves from the specified File and puts them in the map
     * @param s The Filename which should be used
     * @return Itself to enable a Builder-Style programming
     */
    private ChessSaver loadFENsFromXML(String s) {
        File f = new File(s)
        if (f.exists()) {
            new XmlParser().parse(f).save.each { sav ->
                saves.put(counter.getAndIncrement(), [
                        sav.timestamp[0].text(),
                        sav.fen[0].text()
                ])
            }
        }
        return this
    }

    /**
     * Adds a new element to the saves-Map.
     * @param FEN The FEN which should be saved
     * @return A reference to the ChessSaver-object
     */
    ChessSaver saveGame(String FEN) {
        saves.put(counter.getAndIncrement(), [
                new SimpleDateFormat("YYYY-MM-dd HH:mm").format(new Date()),
                FEN
        ])
        return this
    }

    /**
     * Writes the saves-Map to the specified XML-File.
     * The XML File is structured as follows:
     * <ol>
     *     <li>At the root is the collection Element</li>
     *     <li>Inside the Collection is the save Element</li>
     *     <li>The children of save are timestamp,
     *         which is the date and the FEN-String</li>
     * </ol>
     * @param xmlFile The file, where the saves should be written to
     * @return True , if no errors occurred while opening the File, false, if one
     *         of those happened
     */
    boolean writeToXML(String xmlFile) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(xmlFile)
        } catch (IOException io) {
            return false
        }
        /**
         String playerSide = "WHITE"
         if (Main.CHESSGAMEBOARD.getSideToMove() == Side.BLACK) {playerSide = "BLACK";}*/
        new MarkupBuilder(fw).collection {
            saves.each { index, val ->
                save {
                    timestamp(val[0])
                    fen(val[1])
                    //side(playerSide)
                }
            }
        }
        fw.flush()
        fw.close()
        return true
    }

    /**
     * RecoverStartUpPositions will load the initial FEN
     */
    void recoverStartUpPositions() {
        this.loadGameFromFEN(startFEN)
    }

    /**
     * loadGame asks the user in a Dialog for a game, which he saved.
     * This will then be returned and compared with the saves. If the selected
     * game was found it will be loaded.
     */
    void loadGame() {
        String res = this.askUserForSave()

        saves.each { index, values ->
            if (values[0] == res) {
                this.loadGameFromFEN((String) values[1])
                return;
            }
        }
    }

    /**
     * AskUserForSave creates a Dialog Window, where a list representing all the
     * saves will ask the user, which one to choose
     * @return The Date, which will then be used to load the game
     */
    String askUserForSave() {

        String result = startFEN
        ObservableList<String> saveDates = FXCollections.observableArrayList()

        Stage outPut = new Stage()
        outPut.setMaxHeight(480.0)
        outPut.setMaxWidth(200.0)

        saves.each { index, values ->
            saveDates.add(values[0])
        }
        ListView<String> listView = new ListView<>(saveDates)

        Button okButton = new Button("Select this Game")
        okButton.onAction = { event ->
            result = listView.selectionModel.getSelectedItem()
            outPut.hide()
        }
        okButton.prefWidth = 295.0

        Scene scene = new Scene(new VBox(listView, okButton))


        outPut.setScene(scene)
        outPut.setTitle("Please choose a game")

        outPut.showAndWait()
        return result

    }

    /**
     * Loads the specified String and switches the Player if necessary
     * @param FEN The FEN String to load
     * @param isClient A boolean variable, indicating whether the player is the
     *                 Client or the server
     */
    void loadGameFromFEN(String FEN) {
        Main.CHESSGAMEBOARD.loadFromFEN(FEN)
    }

}

