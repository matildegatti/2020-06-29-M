/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicini;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxRegista.getItems().clear();
    	
    	Integer anno=this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.setText("Selezionare un anno");
    		return;
    	}
    	
    	this.model.creaGrafo(anno);
    	
    	this.boxRegista.getItems().addAll(this.model.getVertici());
    	
    	this.txtResult.setText("Grafo creato: 	\n"+"# vertici: "+this.model.nVertici()+"\n"+"# archi: "+this.model.nArchi());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Director direttore=this.boxRegista.getValue();

    	if(direttore==null) {
    		this.txtResult.setText("Selezionare prima un regista");
    		return;
    	}
    	
    	List<Vicini> list=this.model.registiAdiacenti(direttore);
    	this.txtResult.appendText("Registi adiacenti a "+direttore.toString()+":\n");
    	for(Vicini v:list) {
    		this.txtResult.appendText(v.toString()+"\n");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	
    	int x;
    	try {
    		x=Integer.parseInt(this.txtAttoriCondivisi.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero");
    		return;
    	}
    	Director direttore=this.boxRegista.getValue();
    	
    	if(direttore==null) {
    		this.txtResult.setText("Selezionare prima un regista");
    		return;
    	}
    	
    	this.txtResult.appendText("Attori condivisi: "+this.model.attoriCondivisi());
    	for(Director d:this.model.ricorsione(x, direttore)) {
    		this.txtResult.appendText(d.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    
    	List<Integer> anni=new LinkedList<Integer>();
    	anni.add(2004);
    	anni.add(2005);
    	anni.add(2006);
    	
    	this.boxAnno.getItems().addAll(anni);
    	
    }
    
}
