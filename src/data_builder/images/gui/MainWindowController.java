package data_builder.images.gui;

import data_builder.DataSet;
import data_builder.images.logic.DataSetBuilder;
import data_builder.images.logic.ImageManager;
import descriptor.FeatureDescriptor;
import descriptor.hog.HOGDescriptor;
import inferer.neural_network.NeuralNetwork;
import inferer.neural_network.NeuralNetworkTrainer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import loader.error.ErrorHandler;

/**
 * Created by Sergiusz on 21.07.2017.
 */
public class MainWindowController {
    @FXML
    private ScrollPane canvas;
    @FXML
    private ImageView image_container;
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
    private FeatureDescriptor descriptor;
    private int happyNum;
    private int sadNum;
    private int angryNum;
    private int surprisedNum;
    private int noEmotionsNum;
    private int otherNum;
    private NeuralNetwork neuralNetwork;

    public MainWindowController() {
        manager = new ImageManager("C:/Users/Sergiusz/Documents/img_database", 128, 128);
        manager.init();
        dataSetBuilder = new DataSetBuilder("C:/Users/Sergiusz/Documents/img_database/dataSet.txt", "C:/Users/Sergiusz/Documents/img_database/dataSetInfo.txt");
        happyNum = 0;
        sadNum = 0;
        angryNum = 0;
        surprisedNum = 0;
        noEmotionsNum = 0;
        otherNum = 0;
        descriptor = new HOGDescriptor(manager.getImage(), null);

        for (double cls : dataSetBuilder.getDataSet().getClassifications()) {
            if (cls == 0) happyNum++;
            else if (cls == 1) sadNum++;
            else if (cls == 2) angryNum++;
            else if (cls == 3) surprisedNum++;
            else if (cls == 4) noEmotionsNum++;
            else otherNum ++;
        }
        neuralNetwork = new NeuralNetwork(dataSetBuilder.getDataSet().getFeatureVectorSize(), 2, new int[]{6, 6});
        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(neuralNetwork, dataSetBuilder.getDataSet(), new DataSet(dataSetBuilder.getDataSet().getClassesNum(), dataSetBuilder.getDataSet().getFeatureVectorSize(), ErrorHandler.getInstance()), null);
        trainer.train();
    }

    @FXML
    public void initialize() {
        refreshView();
    }

    private void refreshView() {
        image_container.setImage(SwingFXUtils.toFXImage(manager.getImage(), null));
        if (dataSetBuilder.getAssignedFilesList().contains(manager.getCurrentImageName())) {
            lab_pic_nr.setText(manager.getCurrentIndex()+" (classified)");
            lab_pic_nr.setTextFill(Color.GREEN);
            but_happy.setDisable(true);
            but_sad.setDisable(true);
            but_angry.setDisable(true);
            but_surprised.setDisable(true);
            but_no_emotions.setDisable(true);
            but_other.setDisable(true);
        } else {
            lab_pic_nr.setText(manager.getCurrentIndex()+" (unclassified)");
            lab_pic_nr.setTextFill(Color.RED);
            but_happy.setDisable(false);
            but_sad.setDisable(false);
            but_angry.setDisable(false);
            but_surprised.setDisable(false);
            but_no_emotions.setDisable(false);
            but_other.setDisable(false);
        }
        lab_total_pic.setText(manager.getImagesNum()+"");
        lab_unclassified.setText((manager.getImagesNum() - dataSetBuilder.getAssignedFilesList().size())+"");
        lab_num_happy.setText(happyNum+"");
        lab_num_sad.setText(sadNum+"");
        lab_num_angry.setText(angryNum+"");
        lab_num_surprised.setText(surprisedNum+"");
        lab_num_no_emotions.setText(noEmotionsNum+"");
        lab_num_other.setText(otherNum+"");

        descriptor.setSource(manager.getImage());
        double[] featureVector = descriptor.describe().getAsVector();

        double[] response = neuralNetwork.respond(featureVector);
        System.out.println(manager.getCurrentImageName()+":");
        for (int i = 0; i < response.length; i++) {
            System.out.println(i+": "+response[i]);
        }
    }

    public void next() {
        manager.next();
        descriptor.setSource(manager.getImage());
        refreshView();
    }

    public void previous() {
        manager.previous();
        descriptor.setSource(manager.getImage());
        refreshView();
    }

    public void classifyHappy() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 0, manager.getCurrentImageName());
        happyNum++;
        refreshView();
    }

    public void classifySad() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 1, manager.getCurrentImageName());
        sadNum++;
        refreshView();
    }

    public void classifyAngry() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 2, manager.getCurrentImageName());
        angryNum++;
        refreshView();
    }

    public void classifySurprise() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 3, manager.getCurrentImageName());
        surprisedNum++;
        refreshView();
    }

    public void classifyNoEmotions() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 4, manager.getCurrentImageName());
        noEmotionsNum++;
        refreshView();
    }

    public void classifyOther() {
        dataSetBuilder.addEntry(descriptor.describe().getAsVector(), 5, manager.getCurrentImageName());
        otherNum++;
        refreshView();
    }
}
