package br.nataliakt.gpa2.view;

import br.nataliakt.gpa2.MainApp;
import br.nataliakt.gpa2.controller.CurrentDirectoryController;
import br.nataliakt.gpa2.controller.CurrentFileController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Classe que controla o VariableOverview.fxml.
 * 
 * @author Natalia Kelim Thiel
 *
 */
public class VariableOverviewController {

	// Mensagem inicial
	@FXML
	private Label labelTop;

	// Painel de data
	@FXML
	private AnchorPane datePane;

	// Painel de arquivo
	@FXML
	private AnchorPane filePane;

	// Caixa de sele��o
	@FXML
	private ChoiceBox<String> choiceFile;

	// Input de data
	@FXML
	private DatePicker datePicker;

	// Aplica��o principal
	private MainApp mainApp;
	// Controlador do diret�rio atual
	private CurrentDirectoryController currentDirectoryController;

	/**
	 * Exibe as datas na tabela para sele��o.
	 * 
	 * @param currentDirectoryController
	 *            Controlador do diret�rio.
	 */
	public void showDirectory(CurrentDirectoryController currentDirectoryController) {
		this.currentDirectoryController = currentDirectoryController;
		// Caso haja um diret�rio.
		if (currentDirectoryController != null) {
			// Caso haja arquivos de registro no diret�rio
			if (currentDirectoryController.getDataFiles().size() != 0) {
				filePane.setVisible(true);
				datePane.setVisible(true);
				for (String date : currentDirectoryController.getDataFiles()) {
					choiceFile.getItems().add(date);
				}
				choiceFile.getSelectionModel().selectFirst();
				labelTop.setText(
						"Pasta \"" + currentDirectoryController.getDirectory().getAbsolutePath() + "\" selecionada.");
				labelTop.setStyle("-fx-background-color: #4caf50");
				labelTop.setTextFill(Color.web("#FFFFFF"));
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Pasta sem Registros");
				alert.setHeaderText("Registros n�o encontrados");
				alert.setContentText("N�o h� registros para esta pasta. Tente outra pasta.");
				alert.showAndWait();
			}
		}

	}

	/**
	 * Ao clicar em selecionar no card "Escolha uma Data".
	 */
	@FXML
	private void handleSelectDate() {
		mainApp.initFile(new CurrentFileController(currentDirectoryController
				.fileOf(choiceFile.getSelectionModel().getSelectedItem(), mainApp.getExtencion())));
	}

	/**
	 * Ao clicar em selecionar no card "Informe uma Data".
	 */
	@FXML
	private void handlePickDate() {
		if (datePicker.getValue() != null) {
			String date = format(datePicker.getValue().getDayOfMonth()) + "/"
					+ format(datePicker.getValue().getMonthValue()) + "/" + format(datePicker.getValue().getYear());
			if (currentDirectoryController.isFile(date)) {
				mainApp.initFile(
						new CurrentFileController(currentDirectoryController.fileOf(date, mainApp.getExtencion())));
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Data sem Registros");
				alert.setHeaderText("Registros n�o encontrados");
				alert.setContentText("N�o h� registros para esta data. Tente outra data.");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Data n�o Informada");
			alert.setHeaderText("Data n�o Informada");
			alert.setContentText("Data n�o Informada. Selecione uma data no campo de data.");
			alert.showAndWait();
		}
	}

	/**
	 * Formata um valor para que fique com dois caracteres (adiciona o 0).
	 * 
	 * @param value
	 *            Valor.
	 * @return Valor formatado.
	 */
	private String format(int value) {
		String valueFormat = Integer.toString(value);
		if (valueFormat.length() < 2) {
			valueFormat = "0" + valueFormat;
		}
		return valueFormat;
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
}
