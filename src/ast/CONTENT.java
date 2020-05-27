package ast;
import java.util.ArrayList;
import java.util.List;
import libs.Node;
// CONTENT::= "Content:"( ("SETTINGS")?  (STRING) )| FORMULA)
public class CONTENT extends Node {
    SIZE size = null;
    COLOR color = null;
    BI bi = null;
    List<SENTENCE> sentences = new ArrayList<SENTENCE>();
    @Override
    public void parse() {
        //TODO
        // Parse Content

        // the following has been changed in the latest github version

//            case "Settings:":
//                // Parse Settings
//                SETTINGS settings = new SETTINGS();
//                settings.parse();
//                content=tokenizer.getNext();
//                break;
//            case "Formula:":
//                FORMULA formula = new FORMULA();
//                formula.parse();
//                break;
        if (tokenizer.checkToken("Size:")) {
            size = new SIZE();
            size.parse();
        }
        if (tokenizer.checkToken("Bi:")) {
            bi = new BI();
            bi.parse();
        }
        if (tokenizer.checkToken("Color:")) {
            color = new COLOR();
            color.parse();
        }

        // take sentences input
        while (tokenizer.moreTokens() && !tokenizer.checkToken("FORMULA:")){
            // TODO: need to add termination symbol for sentences
            SENTENCE s = new SENTENCE();
            s.parse();
            sentences.add(s);
        }
    }

    @Override
    public void evaluate() {
        // TODO: change this method to toString later
        // TODO: implement a real evaluate()
        System.out.println("process CONTENT: ");
        if (size != null){
            System.out.println("process SIZE = " + size.toString());
        } if (bi != null) {
            System.out.println("process BI = " + bi.toString());
        } if (color != null) {
            System.out.println("process COLOR = " + color.toString());
        }

        for (SENTENCE s: sentences){
            System.out.println("process SENTENCE: " + s.toString());
        }
    }
}
