package ui;

import ast.PROGRAM;
import libs.Node;
import libs.Tokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static String outputFolder = "out/";

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        List<String> literals = Arrays.asList("Title:","Author:","Section:","NewPage:","BulletPoint:","Paragraph:","Image:","Url:","Path:"
                ,"@(", ")@", "Formula:","$", "Color:", "Size:", "Bi:");
        Tokenizer.makeTokenizer("wrongInput.thtml",literals);
        Node.setWriter("output.html");
        PROGRAM p = new PROGRAM();
        System.out.println("Done tokenizing");
        p.parse();
        System.out.println("Done parsing");
        p.evaluate();
        System.out.println("Done evaluation");
        Node.closeWriter();

        //take the beamer input, and convert it to slides output
        //any failure encounter here will throw errors
        generatePdf();
    }

    private static void generatePdf(){
        String latexRootPath= "out/";
        ArrayList<String> validPaths = getAllLatex(latexRootPath);
        String pdfLatexPath = findPdfLatexPath();
        if (validPaths.isEmpty()){
            // should not appear on user's side, as the Latex file is generated by code.
            System.out.println("Failed to find any Latex file.");
        } else if (pdfLatexPath != null) {
            latexToSlides(validPaths, pdfLatexPath);
        } else {
            System.out.println("Failed to load pdfLatex program. Please make sure you have basicTex installed" +
                    "using Homebrew command \n ==> brew cask install basictex");
        }
    }

    private static ArrayList<String> getAllLatex(String latexRootPath){
        //get all the valid paths
        File[] files = new File(latexRootPath).listFiles();
        ArrayList<String> validPaths = new ArrayList<>();
        for (File dir: files){
            if (dir.isDirectory()){
                for (File f: dir.listFiles()){
                    if (f.isFile() && f.toString().endsWith(".tex")){
                        System.out.println("Found file with path: " + f.toString());
                        validPaths.add(f.toString());
                    }
                }
            }
        }
        return validPaths;
    }

    // find PATH of pdflatex
    // TODO: need to test on OS other than Mac, and it would be best to automate this part.
    private static String findPdfLatexPath() {
        return "/Library/TeX/texbin/pdflatex";
    }

    private static void latexToSlides(ArrayList<String> validPaths, String pdfLatexPath){
        for (String p: validPaths){
            try {
                Process process = Runtime.getRuntime().exec(pdfLatexPath +
                        " -output-directory=" + p.substring(0, p.lastIndexOf("/")) + " "+ p);

                StringBuilder fullOutput = new StringBuilder();
                StringBuilder shortOutput = new StringBuilder();

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(process.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(process.getErrorStream()));

                String lineInput;
                while ((lineInput = stdInput.readLine()) != null) {
                    fullOutput.append(lineInput + "\n");
                    if (lineInput.startsWith("Output") || lineInput.startsWith("Transcript")){
                        shortOutput.append(lineInput + "\n");
                    }
                }

                String lineError;
                while ((lineError = stdError.readLine()) != null) {
                    fullOutput.append(lineError + "\n");
                }

                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    System.out.println("Success!");
                    System.out.println(shortOutput);
                    // TODO: remove all the log files if the pdf is created successfully.
                }

            } catch (IOException e) {
                System.out.println("Failed to load pdfLatex program. Please make sure you have basicTex installed" +
                        "using Homebrew command \n ==> brew cask install basictex");
                // e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("The program is forced to quit.");
                // e.printStackTrace();
            }
        }
    }
}
