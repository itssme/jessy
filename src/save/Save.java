package save;

import board.Move;
import board.MoveList;
import board.Position;
import logging.LoggingSingleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-05-13
 * Project: jessy
 * Desc.:
 */
public class Save {

    File fXmlFile;
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;

    public Save() {
        try {
            fXmlFile = new File("/home/joel/IdeaProjects/jessy/src/save/moves.xml");
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            LoggingSingleton.getInstance().error("Could not start saver" , e);
        }
    }

    public void write(Move move) {
        //TODO
    }

    public MoveList read(Document doc) {
        NodeList nList = doc.getElementsByTagName("move");
        MoveList<Move> moves = new MoveList<>();

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                int FromX = Integer.parseInt(eElement.getElementsByTagName("fromX").item(0).getTextContent());
                int FromY = Integer.parseInt(eElement.getElementsByTagName("fromY").item(0).getTextContent());

                int ToX = Integer.parseInt(eElement.getElementsByTagName("toX").item(0).getTextContent());
                int ToY = Integer.parseInt(eElement.getElementsByTagName("toY").item(0).getTextContent());

                Position from = new Position(FromX, FromY);
                Position to   = new Position(ToX  , ToY  );

                moves.add(new Move(from, to));
            }
        }

        return moves;
    }
}
