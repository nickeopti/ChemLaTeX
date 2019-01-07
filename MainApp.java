package com.mycompany.chemlatexmaven;

import com.proudapes.jlatexmathfx.Control.LateXMathControl;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Nicklas Boserup
 */
public class MainApp extends Application {
    
    String formula = "";
    
    @Override
    public void start(Stage primaryStage) {
        LateXMathControl latex = new LateXMathControl("2+ab");
        latex.setOnMouseClicked((me) -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(formula), null);
        });
        latex.setTextColor(Color.WHITE);
        latex.setSize(new Label().getFont().getSize()+5);
        
        TextField input = new TextField();
        input.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String regex = newValue;
            System.out.println(regex);
            regex = regex.replaceAll("[^a-zA-Z()\\d+-][ ]*([+-])[ ]*((?![gls]|aq)[A-Za-z])", " & $2"); // Substitutes +'s (except charges) with ampersands
            System.out.println(regex);
            regex = regex.replaceAll("([a-zA-Z()]+)(\\d+(?![+-]))", "$1_{$2}"); // Subscript numbers
            System.out.println(regex);
            regex = regex.replaceAll("([()])?[ ]*([\\d]*[+-](?!>|[ ]*\\d))", "$1^{$2}"); // Raise charges
            System.out.println(regex);
            regex = regex.replaceAll("(?<![a-zA-Z])(\\d+)[ ]*([a-zA-Z])", "$1\\\\;$2"); // Space multipliers
            System.out.println(regex);
            regex = regex.replaceAll("\\(([gls]|aq)\\)", "$1"); // Remove parentheses
            System.out.println(regex);
            regex = regex.replaceAll("([^ ;]+)[ ]*((?<![a-zA-Z])[gls]|aq)", "\\\\f{$1}{$2}"); // Use f
            System.out.println(regex);
            regex = regex.replaceAll("&", "+"); // Replace ampersand with plus for multiple molecules
            System.out.println(regex);
            regex = regex.replaceAll("=|->|⇌", "\\\\;\\\\rightleftharpoons\\\\;"); // Use harpoons
            System.out.println(regex);
            
            regex = regex.replaceAll("\\*", "\\\\cdot"); // Use LaTeX multiplication dot
            regex = regex.replaceAll("\\[", "\\\\left["); // Use proper sized LaTeX brackets
            regex = regex.replaceAll("\\]", "\\\\right]");
            regex = regex.replaceAll("\\(", "\\\\left(");
            regex = regex.replaceAll("\\)", "\\\\right)");
            System.out.println(regex);
            
            latex.setFormula("\\newcommand{\\f}[2]{{#1}_{\\,(#2)}} \\quad " + regex.trim() + " \\quad");
            
            regex = "\\[ " + regex.trim() + " \\]\n";
            System.out.println(regex);
            formula = regex;
        });
        input.setOnAction((ae) -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(formula), null);
        });
        
        VBox root = new VBox(input, latex);
        root.setPadding(new Insets(5));
        root.setStyle("-fx-base: rgb(50,50,50); -fx-focus-color: transparent;");
        
        Scene scene = new Scene(root, 500, 70);
        
        primaryStage.setTitle("Kemi ⇌ LaTeX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(250);
        primaryStage.setMinHeight(50);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
