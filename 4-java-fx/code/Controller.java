package sample;

import static sample.Main.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class Controller {

    @FXML private Button run, select;
    @FXML private Label filebutton;
    @FXML private TableView<WordCount> table;
    @FXML private TableColumn<WordCount, Integer> Id;
    @FXML private TableColumn<WordCount, String> Word;
    @FXML private TableColumn<WordCount, Integer> Count;

    private WordDict w;

    public void onHover(MouseEvent event) {

        if(run.isHover()) {
            run.setStyle("-fx-background-color: #342985; -fx-text-fill: white;" +
                    "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 5");
        }
        if(select.isHover()) {

            select.setStyle("-fx-background-color: #342985; -fx-text-fill: white;" +
                    "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 5");
        }
    }

    public void onPress (MouseEvent event) {
        if(event.getSource() == run) {

            Id.setCellValueFactory(new PropertyValueFactory<WordCount, Integer>("id"));
            Word.setCellValueFactory(new PropertyValueFactory<WordCount, String>("word"));
            Count.setCellValueFactory(new PropertyValueFactory<WordCount, Integer>("count"));

            table.setItems(w.getList());
            System.out.println("Ran!");
        }
        else if(event.getSource() == select) {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile == null) return;

            filebutton.setText(selectedFile.getName());
            filebutton.setStyle("-fx-background-color: #59b259");


            w = new WordDict(selectedFile.toString());
            w.display();
        }
    }

    public void endHover(MouseEvent event) {
        if(!run.isHover()) {
            run.setStyle("-fx-background-color: white; -fx-text-fill: #342985;" +
                    "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 5");
        }
        if(!select.isHover()) {
            select.setStyle("-fx-background-color: white; -fx-text-fill: #342985;" +
                    "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 5");
        }
    }

}

class WordDict {

    private ArrayList<String> words;
    private ArrayList<Integer> count;
    private File file;


    public WordDict (String str) {

        words = new ArrayList<String>();
        count = new ArrayList<Integer>();

        file = new File(str);

        Scanner sc = null;
        try {
            sc = new Scanner(file).useDelimiter("[^A-za-z]+\\p{P}*");
        } catch (FileNotFoundException e) {
            System.out.println("Problem reading file!");
        }

        while (sc.hasNext()) {
            set(sc.next().toLowerCase());
        }

        sc.close();

    }

    public void set (String str) {
        int n = words.indexOf(str);
        if (n != -1) {
            System.out.println("Already present!");
            int c = count.get(n);
            count.set(n, c+1);
        }
        else {
            System.out.println("Not present!");
            words.add(str);
            count.add(1);
        }
    }

    public int get (String str) {
        int n = words.indexOf(str);
        if (n != -1) {
            return count.get(n);
        }
        else {
            return 0;
        }
    }

    public int size () {
        return words.size();
    }

    public void display () {
        for (int i=0; i<size(); i++) {
            System.out.println(words.get(i) + " - " + count.get(i));
        }
    }

    public ObservableList<WordCount> getList() {

        ObservableList<WordCount> wordList = FXCollections.observableArrayList();
        for (int i=0; i<size(); i++) {
            wordList.add(new WordCount(words.get(i), count.get(i)));
        }
        return wordList;
    }

}
