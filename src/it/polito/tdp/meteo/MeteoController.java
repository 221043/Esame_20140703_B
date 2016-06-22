package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Model;
import it.polito.tdp.meteo.bean.UmiditaCitta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Month> boxMese;

    @FXML
    private Button btnUmidita;

    @FXML
    private Button btnCalcola;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doCalcola(ActionEvent event) {
    	txtResult.clear();
    	Month m = boxMese.getValue();
    	if(m==null){
    		txtResult.setText("Devi selezionare un mese");
    		return;
    	}
    	Map<Integer, String> map = model.ricercaViaggio(m);
    	for(Integer g:map.keySet())
    		txtResult.appendText(String.format("Giorno %d -> Città %s\n", g, map.get(g)));
    }

    @FXML
    void doUmidita(ActionEvent event) {
    	txtResult.clear();
    	Month m = boxMese.getValue();
    	if(m==null){
    		txtResult.setText("Devi selezionare un mese");
    		return;
    	}
    	List<UmiditaCitta> lista = model.getUmiditaMedia(m);
    	txtResult.setText("Mese: "+m.toString()+"\n");
    	for(UmiditaCitta u:lista)
    		txtResult.appendText(String.format("%s: %.2f\n", u.getLocalita(), u.getUmidita()));
    }

    @FXML
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";

        boxMese.getItems().addAll(Month.values());
    }

	public void setModel(Model model) {
		this.model = model;	
	}
	
}
