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
 * Desc.:   
 */
class ChessSaver {

    private static ChessSaver saver = new ChessSaver()

    static ChessSaver getInstance() {
        return saver
    }

    private String startFEN
    private saves = [:]
    private final AtomicInteger counter = new AtomicInteger();

    ChessSaver init(String FEN) {
        this.loadFENsFromXML("jessy.sav.xml")
        startFEN = FEN
        return this;
    }

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
        saves.each { index, values ->
            println "Index: ${index}"
            println "Time: ${values[0]}"
            println()
        }
        return this
    }

    ChessSaver saveGame(String FEN) {
        saves.put(counter.getAndIncrement(), [
                new SimpleDateFormat("YYYY-MM-dd HH:mm").format(new Date()),
                FEN
        ])
        return this
    }

    boolean writeToXML(String xml) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(xml)
        } catch (IOException io) {
            return false
        }
        new MarkupBuilder(fw).collection {
            saves.each { index, val ->
                save {
                    timestamp(val[0])
                    fen(val[1])
                }
            }
        }
        fw.flush()
        fw.close()
        return true;
    }

    void recoverStartUpPositions() {
        Main.CHESSGAMEBOARD.loadFromFEN(startFEN)
    }


    void loadGame() {
        String res = this.askUserForSave()

        saves.each { index, values ->
            if (values[0] == res) {
                println values[1]
                Main.CHESSGAMEBOARD.loadFromFEN((String) values[1])
            }
        }
    }


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
        okButton.minWidth = 295.0

        Scene scene = new Scene(new VBox(listView, okButton))


        outPut.setScene(scene)
        outPut.setTitle("Please choose a game")

        outPut.showAndWait()
        return result

    }

}

