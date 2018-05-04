package br.nataliakt.gpa2.view;

import java.io.File;

import br.nataliakt.gpa2.MainApp;
import br.nataliakt.gpa2.controller.CurrentFileController;
import br.nataliakt.gpa2.controller.CurrentDirectoryController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * Classe que controla o RootLayout.fxml.
 * 
 * @author Natalia Kelim Thiel
 *
 */
public class RootLayoutController {
	
	// Aplicativo principal
	private MainApp mainApp;

	/**
	 * Abre uma pasta.
	 */
	@FXML
	private void handleOpenDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Diretório Padrão");

		// Abre a janela para escolha
		File directory = directoryChooser.showDialog(mainApp.getPrimaryStage());
		if (directory != null) {
			CurrentDirectoryController currentDirectoryController = new CurrentDirectoryController(directory);
			mainApp.showVariableOverview(currentDirectoryController);
		}
	}

	/**
	 * Abre um arquivo.
	 */
	@FXML
	private void handleOpenFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Arquivo Padrão");

		// Define um filtro de extensão
		FileChooser.ExtensionFilter TmpExtFilter = new FileChooser.ExtensionFilter("Arquivos de Temperatura (*.TMP)",
				"*.TMP");
		FileChooser.ExtensionFilter LdrExtFilter = new FileChooser.ExtensionFilter("Arquivos de Luminosidade (*.LDR)",
				"*.LDR");
		fileChooser.getExtensionFilters().add(TmpExtFilter);
		fileChooser.getExtensionFilters().add(LdrExtFilter);

		// Abre a janela para escolha
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.initFile(new CurrentFileController(file));
		}
	}

	/**
	 * Abre uma janela de Ajuda.
	 */
	@FXML
	private void handleHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Ajuda");
		alert.setHeaderText("Como funciona?");
		alert.setContentText("Para iniciar, vá em Arquivo > Abrir... e selecione uma pasta ou um arquivo.\n"
				+ "Caso selecione uma pasta, escolha uma data ou insira uma data manualmente.\n"
				+ "Para alterar a variável, vá no cartão Variável e selecione a desejada.\n"
				+ "Para alterar a visão, vá em Visão e selecione a desejada.\n"
				+ "Para retornar a seleção de pasta, clique em Voltar.");
		alert.showAndWait();
	}

	/**
	 * Abre uma janela Sobre.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sobre");
		alert.setHeaderText("Sobre");
		alert.setContentText("Instituto Federal Catarinense - Campus Rio do Sul\n"
				+ "Grupo de Pesquisa em Automação Industrial e Agropecuária - GPA2\n" + "Autora: Natalia Kelim Thiel\n"
				+ "Contato: natalia.kthiel@gmail.com");
		alert.showAndWait();
	}

	/**
	 * Fecha a aplicação.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
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
