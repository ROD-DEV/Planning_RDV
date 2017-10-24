package agenda.vue;

import agenda.modele.AgendaWrapper;
import agenda.modele.RendezVous;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class VueControleur {
	@FXML
	private DatePicker choixDate;
	@FXML
	private ComboBox<String> heure;
	@FXML
	private ComboBox<String> minutes;
	@FXML
	private TextField contact;
	@FXML
	private TextField telephone;
	@FXML
	private CheckBox confirmer;
	@FXML
	private CheckBox transport;
	@FXML
	private TextField objet;
	@FXML
	private ListView<RendezVous> liste;
	private ObservableList<RendezVous> lesrendezvous = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		ObservableList<String> lesheures = FXCollections.observableArrayList("8", "9", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20");
		heure.setItems(lesheures);
		heure.setValue("8");
		ObservableList<String> lesminutes = FXCollections.observableArrayList("00", "10", "20", "30", "40", "50");
		minutes.setItems(lesminutes);
		minutes.setValue("00");
		choixDate.setValue(LocalDate.now());
		// La ListView liste visualisera la ObservableList lesrendezvous.
		liste.setItems(lesrendezvous);
		File fichier = new File ("agenda.xml");
		if (fichier.exists()) {
		JAXBContext context;
		try {
		context = JAXBContext.newInstance(AgendaWrapper.class);
		Unmarshaller um = context.createUnmarshaller();
		AgendaWrapper agenda = (AgendaWrapper) um.unmarshal(fichier);
		List<RendezVous> lesrv = agenda.getAgenda();
		lesrendezvous.addAll(lesrv);
		} catch (JAXBException e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Erreur grave");
		alert.setHeaderText("Le fichier agenda ne peut pas être lu");
		alert.showAndWait();
		}
		}
		}
		
	

	@FXML
	private void handleAjouterRV() {
		String Heure = heure.getValue() + ":" + minutes.getValue();
		RendezVous rv = new RendezVous(choixDate.getValue(), Heure, contact.getText(), objet.getText(),
				telephone.getText(), confirmer.isSelected(), transport.isSelected());
		lesrendezvous.add(rv);
	}
	@FXML
	private void handleEffacerlesChamps(){
		heure.setValue("0");
		minutes.setValue("00");
		choixDate.setValue(LocalDate.now());
		contact.clear();
		telephone.clear();
		objet.clear();
		confirmer.setSelected(true);
		transport.setSelected(false);}
		
	@FXML
	
		private void handleSupprimerunRV(){
			RendezVous sel = liste.getSelectionModel().getSelectedItem();
			lesrendezvous.remove(sel);
		}
		
		@FXML
		private void handleTrierlesRV(){
			
			ObservableList<RendezVous> lesRVtries;
			lesRVtries = lesrendezvous.sorted(new Comparator<RendezVous>(){
				
				public int compare (RendezVous a , RendezVous b){
					int r = a.getDate().compareTo(b.getDate());
					if ( r!= 0)
						return r;
					String ha = a.getHeure();
					String hb = b.getHeure();
					String [] haT = ha.split(";");
					String [] hbT = hb.split(" ; ");
					
					int heureA = Integer.parseInt(haT[0]);
					int heureB = Integer.parseInt(hbT[0]);
					if (heureA != heureB)
						return (heureA - heureB);
					
					int minA = Integer.parseInt(haT[1]);
					int minB = Integer.parseInt(hbT[1]);
						return minA - minB;
														
				}});
			lesrendezvous.setAll(lesRVtries);
		}
		
		private boolean pasErreur() {
			String Contact = contact.getText() ;
			String tel = telephone.getText() ;
			if (Contact.isEmpty() || tel.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Informations incomplètes");
			alert.setHeaderText("Compléter les champs indiqués");
			if (Contact.isEmpty())
			contact.setPromptText("à compléter");
			if (tel.isEmpty())
			telephone.setPromptText("à compléter");
			alert.showAndWait();
			
			return false;}
			else return true;}
			
			private void handleAjouterARV() {
				if (pasErreur()) {
				String Heure = heure.getValue() + ":" + minutes.getValue();
				RendezVous rv = new RendezVous(choixDate.getValue(), Heure, contact.getText(),
				objet.getText(),telephone.getText(),confirmer.isSelected(),
				transport.isSelected());
				lesrendezvous.add(rv);
				}
			}


@FXML
private void sauverAgenda() {
JAXBContext context;
try {
context = JAXBContext.newInstance(AgendaWrapper.class);
Marshaller m = context.createMarshaller();
m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
AgendaWrapper wrapper = new AgendaWrapper() ;
wrapper.setAgenda(lesrendezvous);
//m.marshal(wrapper, System.out);
m.marshal(wrapper, new File("agenda.xml"));
} catch (JAXBException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}
}}



				
				
			
			
		
		
		

