package data_builder.images.gui;

import data_builder.DataSet;
import data_builder.images.logic.DataSetBuilder;
import data_builder.images.logic.ImageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.io.File;

/**
 * Created by Sergiusz on 21.07.2017.
 */
public class MainWindowController {
    @FXML
    private ScrollPane canvas;
    @FXML
    private Button but_prev;
    @FXML
    private Button but_happy;
    @FXML
    private Button but_sad;
    @FXML
    private Button but_angry;
    @FXML
    private Button but_surprised;
    @FXML
    private Button but_no_emotions;
    @FXML
    private Button but_other;
    @FXML
    private Button but_next;
    @FXML
    private Label lab_pic_nr;
    @FXML
    private Label lab_total_pic;
    @FXML
    private Label lab_unclassified;
    @FXML
    private Label lab_num_happy;
    @FXML
    private Label lab_num_sad;
    @FXML
    private Label lab_num_angry;
    @FXML
    private Label lab_num_surprised;
    @FXML
    private Label lab_num_no_emotions;
    @FXML
    private Label lab_num_other;

    private ImageManager manager;
    private DataSetBuilder dataSetBuilder;

    public MainWindowController() {
        manager = new ImageManager("C:/Users/Sergiusz/Documents/imgs");
        manager.init();
        dataSetBuilder = new DataSetBuilder("C:/Users/Sergiusz/Documents/imgs/dataSet.txt");
    }
}
