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

	// Descrição da variável
	@FXML
	private MenuButton variable;

	// Data da variável
	@FXML
	private MenuButton date;
	
	// Data da variável
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
	 * Inicializa a classe controller. Este método é chamado automaticamente
	 * após o arquivo fxml ter sido carregado.
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
	 * Métod que mostra a lista
	 */
	public void showList() {
		showDescription();
		
		valueColumn.setText(getVariable());
		// Criando coleção
		variableData = FXCollections.observableArrayList(mainApp.getCurrentFileController().readFile());

		// Adiciona os dados da observable list na tabela
		variableTable.setItems(variableData);
	}

	/**
	 * Método para mostrar a descrição.
	 */
	private void showDescription() {
		// Printando o nome da variável
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
		

		// Printando a visão
		view.setText(getView());
		MenuItem graphic = new MenuItem("Gráfico");
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
	 * Busca a descrição da variável.
	 * 
	 * @return Descrição (Temperatura ou Luminosidade).
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
	 * Busca a descrição da visão.
	 * 
	 * @return Descrição (Gráfico ou Lista).
	 */
	private String getView() {
		switch (mainApp.getView()) {
		case 0:
			return "Gráfico";
		case 1:
			return "Lista";
		}
		return "";
	}

	/**
	 * Set padrão de mainApp.
	 * 
	 * @param mainApp
	 *            Aplicação inicial.
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
