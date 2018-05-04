package br.nataliakt.gpa2;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import br.nataliakt.gpa2.controller.CurrentFileController;
import br.nataliakt.gpa2.controller.CurrentDirectoryController;
import br.nataliakt.gpa2.view.RootLayoutController;
import br.nataliakt.gpa2.view.VariableGraphicController;
import br.nataliakt.gpa2.view.VariableListController;
import br.nataliakt.gpa2.view.VariableOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Classe da aplicação principal.
 * 
 * @author Natalia Kelim Thiel
 */
public class MainApp extends Application {

	// Janela principal
	private Stage primaryStage;
	// Layout de fundo
	private BorderPane rootLayout;
	// Controlador de arquivo padrão
	private CurrentFileController currentFileController;
	// Variável (0 = temperatura, 1 = luminosidade)
	private int variable;
	// Visão (0 = gráficos, 1 = lista)
	private int view;

	/**
	 * Método principal.
	 * 
	 * @param args
	 *            Argumentos padrão.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		variable = 0;
		view = 0;
		currentFileController = null;
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Variáveis");
		this.primaryStage.setMinWidth(600);
		this.primaryStage.setMinHeight(600);
		this.primaryStage.setMaximized(true);
		// this.primaryStage.getIcons().add(new
		// Image("file:resources/images/Address_Book.png"));

		initRootLayout();
		showVariableOverview(null);

		// Tenta carregar o último arquivo de pessoa aberto.
		File directory = getDirectryPath();
		if (directory != null && directory.exists()) {
			showVariableOverview(new CurrentDirectoryController(directory));
		}
	}

	/**
	 * Retorna o arquivo de preferências da pessoa, o último arquivo que foi
	 * aberto. As preferências são lidas do registro específico do SO (Sistema
	 * Operacional). Se tais prefêrencias não puderem ser encontradas, ele
	 * retorna null.
	 * 
	 * @return
	 */
	public File getDirectryPath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("directoryPath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Define o caminho do arquivo do arquivo carregado atual. O caminho é
	 * persistido no registro específico do SO (Sistema Operacional).
	 * 
	 * @param file
	 *            O arquivo ou null para remover o caminho
	 */
	public void setDirectryPath(File directory) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (directory != null) {
			prefs.put("directoryPath", directory.getPath());

			// Update the stage title.
			primaryStage.setTitle("Variáveis - " + directory.getAbsolutePath());
		} else {
			prefs.remove("directoryPath");

			// Update the stage title.
			primaryStage.setTitle("Variáveis");
		}
	}

	/**
	 * Inicia o RootLayout.fxml.
	 */
	public void initRootLayout() {
		try {
			// Carregando o arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Mostra a cena
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			// primaryStage.setResizable(false);

			// Permite acesso ao controlador
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra o VariableOverview.fxml.
	 */
	public void showVariableOverview(CurrentDirectoryController currentDirectoryController) {
		try {
			if (currentDirectoryController != null) {
				setDirectryPath(currentDirectoryController.getDirectory());
				primaryStage.setTitle("Variáveis - " + currentDirectoryController.getDirectory().getAbsolutePath());
			}
			this.currentFileController = null;
			// Carrega o arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/VariableOverview.fxml"));
			AnchorPane variableOverview = (AnchorPane) loader.load();

			// Adiciona no centro do rootLayout
			rootLayout.setCenter(variableOverview);

			// Permite acesso ao controlador
			VariableOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.showDirectory(currentDirectoryController);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicia um novo arquivo.
	 * 
	 * @param currentFileController
	 *            Controlador do arquivo atual.
	 */
	public void initFile(CurrentFileController currentFileController) {
		setDirectryPath(currentFileController.getFile().getParentFile());
		primaryStage.setTitle("Variáveis - " + currentFileController.getFile().getAbsolutePath());
		this.currentFileController = currentFileController;
		switch (currentFileController.getFile().getName().substring(9, 12)) {
		case "TMP":
			variable = 0;
			break;
		case "LDR":
			variable = 1;
		}
		switch (view) {
		case 0: // Gráficos
			showVariableGraphic();
			break;
		case 1: // Lista
			showVariableList();
			break;
		}
	}

	/**
	 * Mostra VariableGraphic.fxml.
	 */
	public void showVariableGraphic() {
		try {
			// Carrega o arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/VariableGraphic.fxml"));
			AnchorPane variableGraphic = (AnchorPane) loader.load();

			// Adiciona no centro do rootLayout
			rootLayout.setCenter(variableGraphic);

			// Permite acesso ao controlador
			VariableGraphicController controller = loader.getController();
			controller.setMainApp(this);
			controller.showGraphic();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra VariableList.fxml.
	 */
	public void showVariableList() {
		try {
			// Carrega o arquivo
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/VariableList.fxml"));
			AnchorPane variableList = (AnchorPane) loader.load();

			// Adiciona no centro do rootLayout
			rootLayout.setCenter(variableList);

			// Permite acesso ao controlador
			VariableListController controller = loader.getController();
			controller.setMainApp(this);
			controller.showList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Busca extenção da variavel.
	 * 
	 * @return Extenção (TMP ou LDR).
	 */
	public String getExtencion() {
		switch (variable) {
		case 0:
			return "TMP";
		case 1:
			return "LDR";
		default:
			return "";
		}
	}

	/**
	 * Set para o valor numérico.
	 * 
	 * @param variable
	 *            Valor.
	 */
	public void setVariableNumber(int variable) {
		this.variable = variable;
	}

	/**
	 * Get padrão do primaryStage.
	 * 
	 * @return Palco principal.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Get padrão da varivel.
	 * 
	 * @return Variavel.
	 */
	public int getVariable() {
		return variable;
	}

	/**
	 * Set padrão da varivel.
	 * 
	 * @param variable
	 *            Variavel.
	 */
	public void setVariable(int variable) {
		if (variable != this.variable) {
			this.variable = variable;
			if (currentFileController != null) {
				currentFileController = new CurrentFileController(currentFileController, getExtencion());
				initFile(currentFileController);
			}
		}
	}

	/**
	 * Get padrão da visão.
	 * 
	 * @return Visão.
	 */
	public int getView() {
		return view;
	}

	/**
	 * Set padrão da visão.
	 * 
	 * @param view
	 *            Visão.
	 */
	public void setView(int view) {
		if (view != this.view) {
			this.view = view;
			if (currentFileController != null) {
				initFile(currentFileController);
			}
		}
	}

	/**
	 * Get padrão da data padrão.
	 * 
	 * @return Controlador da data padrão.
	 */
	public CurrentFileController getCurrentFileController() {
		return currentFileController;
	}
}
