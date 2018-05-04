package br.nataliakt.gpa2.view;

import br.nataliakt.gpa2.MainApp;
import br.nataliakt.gpa2.controller.CurrentDirectoryController;
import br.nataliakt.gpa2.controller.CurrentFileController;
import br.nataliakt.gpa2.model.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VariableListController {

	// Descri��o da vari�vel
	@FXML
	private MenuButton variable;

	// Data da vari�vel
	@FXML
	private MenuButton date;
	
	// Data da vari�vel
	@FXML
	private MenuButton view;

	// Tabela
	@FXML
	private TableView<Variable> variableTable;
	// Coluna da hora
	@FXML
	private TableColumn<Variable, String> hourColumn;
	// Coluna do valor
	@FXML
	private TableColumn<Variable, String> valueColumn;

	// Aplicativo principal
	private MainApp mainApp;

	// Lista para a tabela
	private ObservableList<Variable> variableData = FXCollections.observableArrayList();

	/**
	 * Inicializa a classe controller. Este m�todo � chamado automaticamente
	 * ap�s o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
		// Inicializa a tabela com duas colunas.
		hourColumn.setCellValueFactory(cellData -> cellData.getValue().hourProperty());
		valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
	}

	/**
	 * Chamada ao clicar em voltar.
	 */
	@FXML
	private void handleReturn() {
		mainApp.showVariableOverview(
				new CurrentDirectoryController(mainApp.getCurrentFileController().getFile().getParentFile()));
	}

	/**
	 * M�tod que mostra a lista
	 */
	public void showList() {
		showDescription();
		
		valueColumn.setText(getVariable());
		// Criando cole��o
		variableData = FXCollections.observableArrayList(mainApp.getCurrentFileController().readFile());

		// Adiciona os dados da observable list na tabela
		variableTable.setItems(variableData);
	}

	/**
	 * M�todo para mostrar a descri��o.
	 */
	private void showDescription() {
		// Printando o nome da vari�vel
		variable.setText(getVariable());
		MenuItem temperature = new MenuItem("Temperatura");
		MenuItem luminosity = new MenuItem("Luminosidade");
		temperature.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainApp.setVariable(0);
			}
		});
		luminosity.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainApp.setVariable(1);
			}
		});
		variable.getItems().add(temperature);
		variable.getItems().add(luminosity);
		

		// Printando a vis�o
		view.setText(getView());
		MenuItem graphic = new MenuItem("Gr�fico");
		MenuItem list = new MenuItem("Lista");
		graphic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainApp.setView(0);
			}
		});
		list.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainApp.setView(1);
			}
		});
		view.getItems().add(graphic);
		view.getItems().add(list);

		// Printando a data
		date.setText(mainApp.getCurrentFileController().getDate());
		CurrentDirectoryController currentDirectoryController = new CurrentDirectoryController(
				mainApp.getCurrentFileController().getFile().getParentFile());
		for (String dateString : currentDirectoryController.getDataFiles()) {
			MenuItem dateItem = new MenuItem(dateString);
			dateItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					mainApp.initFile(new CurrentFileController(
							currentDirectoryController.fileOf(dateString, mainApp.getExtencion())));
				}
			});
			date.getItems().add(dateItem);
		}
	}

	/**
	 * Busca a descri��o da vari�vel.
	 * 
	 * @return Descri��o (Temperatura ou Luminosidade).
	 */
	private String getVariable() {
		switch (mainApp.getVariable()) {
		case 0:
			return "Temperatura";
		case 1:
			return "Luminosidade";
		}
		return "";
	}

	/**
	 * Busca a descri��o da vis�o.
	 * 
	 * @return Descri��o (Gr�fico ou Lista).
	 */
	private String getView() {
		switch (mainApp.getView()) {
		case 0:
			return "Gr�fico";
		case 1:
			return "Lista";
		}
		return "";
	}

	/**
	 * Set padr�o de mainApp.
	 * 
	 * @param mainApp
	 *            Aplica��o inicial.
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	/**
	 * Retorna os dados como uma observable list de Persons.
	 * 
	 * @return
	 */
	public ObservableList<Variable> getPersonData() {
		return variableData;
	}

}
