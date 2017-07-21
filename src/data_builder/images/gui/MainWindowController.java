package data_builder.images.gui;

import data_builder.images.logic.DataSetBuilder;
import data_builder.images.logic.ImageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

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
    private int happyNum;
    private int sadNum;
    private int angryNum;
    private int surprisedNum;
    private int noEmotionsNum;
    private int otherNum;

    public MainWindowController() {
        manager = new ImageManager("C:/Users/Sergiusz/Documents/imgs");
        manager.init();
        dataSetBuilder = new DataSetBuilder("C:/Users/Sergiusz/Documents/imgs/dataSet.txt", "C:/Users/Sergiusz/Documents/imgs/dataSetInfo.txt");
        happyNum = 0;
        sadNum = 0;
        angryNum = 0;
        surprisedNum = 0;
        noEmotionsNum = 0;
        otherNum = 0;

        for (double cls : dataSetBuilder.getDataSet().getClassifications()) {
            if (cls == 0) happyNum++;
            else if (cls == 1) sadNum++;
            else if (cls == 2) angryNum++;
            else if (cls == 3) surprisedNum++;
            else if (cls == 4) noEmotionsNum++;
            else otherNum ++;
        }
    }

    private void refreshView() {
        
    }
}
