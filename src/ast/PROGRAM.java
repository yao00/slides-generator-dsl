package ast;

import libs.Node;

import java.util.ArrayList;
import java.util.List;


public class PROGRAM extends Node {
    //PROGRAM::= TITLE AUTHOR? (SECTION)*
    TITLE title= null;;
    AUTHOR author= null;;
    List<SECTION> sections = new ArrayList<>();

    // Fields for evaluation
    String start = "\\documentclass[11pt]{beamer}\n" +
            "\\usetheme[progressbar=frametitle]{metropolis}\n" +
            "\\usepackage{appendixnumberbeamer}\n" +
            "\\usepackage{hyperref}\n" +
            "\\usepackage{booktabs}\n" +
            "\\usepackage[scale=2]{ccicons}\n" +
            "\\usepackage{pgfplots}\n" +
            "\\usepgfplotslibrary{dateplot}\n" +
            "\\usepackage{xspace}\n" +
            "\\usepackage{color,xcolor}\n" +
            "\\newcommand{\\themename}{\\textbf{\\textsc{metropolis}}\\xspace}\n" +
            "\n" +
            "\\setbeamertemplate{headline}\n" +
            "{\n" +
            "   \\leavevmode%\n" +
            "   \\hbox{%\n" +
            "   \\begin{beamercolorbox}[wd=.5\\paperwidth,ht=2.25ex,dp=1ex,right]{section in head/foot}%\n" +
            "     \\usebeamerfont{section in head/foot}\\thesection.\\ \\insertsectionhead\\hspace*{2ex}\n" +
            "   \\end{beamercolorbox}%\n" +
            "   \\begin{beamercolorbox}[wd=.5\\paperwidth,ht=2.25ex,dp=1ex,left]{subsection in head/foot}%\n" +
            "     \\usebeamerfont{subsection in head/foot}\\hspace*{2ex}\\insertsubsectionhead\n" +
            "   \\end{beamercolorbox}}%\n" +
            "   \\vskip0pt%\n" +
            "}\n" +
            "\n" +
            "\\begin{document}";
    String end = "\\end{document}";

    public void parse(){
        // Parse TITLE
        if (tokenizer.checkToken("Title:")) {
            title = new TITLE();
            title.parse();
        }
        System.out.println("title parsed");

        // Parse AUTHOR
        if (tokenizer.checkToken("Author:")) {
            author = new AUTHOR();
            author.parse();
        }
        System.out.println("author parsed");

        // Parse SECTION
        while(tokenizer.moreTokens() && tokenizer.checkToken("Section:")){
            SECTION s = new SECTION();
            s.parse();
            sections.add(s);
        }
    }

    @Override
    public void evaluate() {
        writer.println(start);

        // Evaluate Title
        if (title != null)
            title.evaluate();

        // Evaluate author
        if (author != null)
            author.evaluate();

        // print \maketitle
        String make = "\\maketitle\n";
        writer.println(make);

        // table of contents
        writer.print("\\begin{frame}{Table of contents}\n" +
                "  \\setbeamertemplate{section in toc}[sections numbered]\n" +
                "  \\tableofcontents%[hideallsubsections]\n" +
                "\\end{frame}\n");

        // Evaluate sections
       for (SECTION s : sections){
            s.evaluate();
        }

        writer.println(end);
    }
}
