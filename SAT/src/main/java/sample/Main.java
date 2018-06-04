package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sat4j.reader.DimacsReader;
import org.sat4j.tools.DimacsOutputSolver;
import java.io.File;


public class Main extends Application {

    TextArea outputField;
    Label fileChecker;
    Text t;
    Button executeButton;
    String fileInput;
    String saveOutput;
    Label labelChoose;
    boolean satelite;
    Button saveFile;


    @Override
    public void start(Stage primaryStage) throws Exception{

        DimacsReader dimacsReader = new DimacsReader(new DimacsOutputSolver());
        primaryStage.setTitle("SAT Solver Interfejs");
        Pane pane = new Pane();
        saveOutput ="";
        satelite = false;


        Label chooseFile = new Label();
        chooseFile.setPrefSize(500,20);
        chooseFile.setText("Wybierz plik");
        chooseFile.setLayoutX(200);
        chooseFile.setLayoutY(20);
        pane.getChildren().addAll(chooseFile);


        t = new Text();

        Button chooseFileButton = new Button("Wyszukaj");
        chooseFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("/home/szergowiec/Pulpit/tkk/SAT/src/main/java/solvers"));
                File file = fileChooser.showOpenDialog(primaryStage);

                if(file != null) {
                    fileInput=file.getAbsolutePath();
                    t.setText(file.getName());

                    try{
                        dimacsReader.parseInstance(file.getAbsolutePath());
                        t.setFill(Color.GREEN);
                        executeButton.setDisable(false);
                    }catch(Exception e) {
                        t.setFill(Color.RED);
                        executeButton.setDisable(true);
                    }

                    t.setLayoutX(210);
                    t.setLayoutY(113);

                }
            }}
        );
        pane.getChildren().addAll(t);
        chooseFileButton.setLayoutX(200);
        chooseFileButton.setLayoutY(50);

        fileChecker = new Label();
        fileChecker.setText("Problem SAT : ");
        fileChecker.setLayoutX(110);
        fileChecker.setLayoutY(100);
        pane.getChildren().addAll(fileChecker);


        Label chooseSolver = new Label();
        chooseSolver.setText("Wybierz solver");
        chooseSolver.setLayoutX(100);
        chooseSolver.setLayoutY(150);
        pane.getChildren().addAll(chooseSolver);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                "AbcdSAT",
                "Cadical",
                "Glucose",
                "Lingeling",
                "Minisat",
                "Riss",
                "Syrup",
                "Zchaff"
        );
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(comboBox.getValue().toString().equals("Syrup") || comboBox.getValue().toString().equals("AbcdSAT") ||comboBox.getValue().toString().equals("Zchaff") ||comboBox.getValue().toString().equals("Cadical") ){
                    saveFile.setDisable(true);
                }else{
                    saveFile.setDisable(false);
                }
            }
        });
        comboBox.setValue("AbcdSAT");
        comboBox.setLayoutX(100);
        comboBox.setLayoutY(170);
        pane.getChildren().addAll(comboBox);


        CheckBox sateliteCheckBox = new CheckBox("Satelite");
        sateliteCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(sateliteCheckBox.isSelected()){
                    satelite=true;
                }else{
                    satelite=false;
                }
            }
        });
        sateliteCheckBox.setLayoutX(250);
        sateliteCheckBox.setLayoutY(175);

        pane.getChildren().addAll(sateliteCheckBox);


        executeButton = new Button("Wykonaj");
        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String s = comboBox.getValue().toString();
                String output = new String();
                switch (s){
                    case "AbcdSAT" : output = Controller.runSATSolver("abcdsat_p",fileInput,saveOutput,satelite);
                        break;
                    case "Cadical" : output = Controller.runSATSolver("cadical",fileInput,saveOutput,satelite);
                        break;
                    case "Glucose" : output = Controller.runSATSolver("glucose_static",fileInput,saveOutput,satelite);
                        break;
                    case "Lingeling" : output = Controller.runSATSolver("lingeling",fileInput,saveOutput,satelite);
                        break;
                    case "Minisat" : output = Controller.runSATSolver("minisat",fileInput,saveOutput,satelite);
                        break;
                    case "Riss" : output = Controller.runSATSolver("riss",fileInput,saveOutput,satelite);
                        break;
                    case "Syrup" : output = Controller.runSATSolver("glucose-syrup",fileInput,saveOutput,satelite);
                        break;
                    case "Zchaff" : output = Controller.runSATSolver("zchaff",fileInput,saveOutput,satelite);
                        break;
                }

                outputField.setText(output);
            }
        });
        executeButton.setDisable(true);
        executeButton.setLayoutX(300);
        executeButton.setLayoutY(300);
        pane.getChildren().addAll(executeButton);


        Label labelSave = new Label("Wybierz miejsce zapisu ");
        labelSave.setLayoutX(50);
        labelSave.setLayoutY(270);
        pane.getChildren().addAll(labelSave);

         saveFile = new Button("Zapisz");
        saveFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);
                if(file == null){
                    labelChoose.setText("Wybrano plik : ");
                    saveOutput = "src/solvers/tmp";
                }
                else{
                    labelChoose.setText("Wybrano plik : " + file.getName());
                    saveOutput = file.getAbsolutePath();
                }

            }
        });
        saveFile.setLayoutX(100);
        saveFile.setLayoutY(300);
        saveFile.setDisable(true);
        pane.getChildren().addAll(saveFile);


        labelChoose = new Label("Wybrano plik : ");
        labelChoose.setLayoutX(20);
        labelChoose.setLayoutY(340);
        pane.getChildren().addAll(labelChoose);



        outputField = new TextArea();
        outputField.setPrefSize(730, 850);
        outputField.setLayoutX(450);
        outputField.setLayoutY(0);
        pane.getChildren().addAll(outputField);

        pane.getChildren().addAll(chooseFileButton);


        primaryStage.setScene(new Scene(pane, 1200, 850));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
