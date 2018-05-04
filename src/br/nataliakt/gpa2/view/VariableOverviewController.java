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

	// Caixa de seleção
	@FXML
	private ChoiceBox<String> choiceFile;

	// Input de data
	@FXML
	private DatePicker datePicker;

	// Aplicação principal
	private MainApp mainApp;
	// Controlador do diretório atual
	private CurrentDirectoryController currentDirectoryController;

	/**
	 * Exibe as datas na tabela para seleção.
	 * 
	 * @param currentDirectoryController
	 *            Controlador do diretório.
	 */
	public void showDirectory(CurrentDirectoryController currentDirectoryController) {
		this.currentDirectoryController = currentDirectoryController;
		// Caso haja um diretório.
		if (currentDirectoryController != null) {
			// Caso haja arquivos de registro no diretório
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
				alert.setHeaderText("Registros não encontrados");
				alert.setContentText("Não há registros para esta pasta. Tente outra pasta.");
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
				alert.setHeaderText("Registros não encontrados");
				alert.setContentText("Não há registros para esta data. Tente outra data.");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Data não Informada");
			alert.setHeaderText("Data não Informada");
			alert.setContentText("Data não Informada. Selecione uma data no campo de data.");
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
	 * Set padrão de mainApp.
	 * 
	 * @param mainApp
	 *            Aplicação inicial.
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
