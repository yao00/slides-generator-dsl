package ast;

import libs.Node;

public class PARAGRAPH extends PAGESTUFF {
    CONTENT paragraph;
    @Override
    public void parse() {
        tokenizer.getAndCheckNext("Paragraph:");
        paragraph = new CONTENT();
        paragraph.parse();
    }

    @Override
    public void evaluate() {
        paragraph.evaluate();
        String end  = "\\newline";
        writer.println(end);
    }
}

/*
        sample paragraph:
        line 1 (string) \newline
        */