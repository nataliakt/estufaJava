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
 * Classe da aplica��o principal.
 * 
 * @author Natalia Kelim Thiel
 */
public class MainApp extends Application {

	// Janela principal
	private Stage primaryStage;
	// Layout de fundo
	private BorderPane rootLayout;
	// Controlador de arquivo padr�o
	private CurrentFileController currentFileController;
	// Vari�vel (0 = temperatura, 1 = luminosidade)
	private int variable;
	// Vis�o (0 = gr�ficos, 1 = lista)
	private int view;

	/**
	 * M�todo principal.
	 * 
	 * @param args
	 *            Argumentos padr�o.
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
		this.primaryStage.setTitle("Vari�veis");
		this.primaryStage.setMinWidth(600);
		this.primaryStage.setMinHeight(600);
		this.primaryStage.setMaximized(true);
		// this.primaryStage.getIcons().add(new
		// Image("file:resources/images/Address_Book.png"));

		initRootLayout();
		showVariableOverview(null);

		// Tenta carregar o �ltimo arquivo de pessoa aberto.
		File directory = getDirectryPath();
		if (directory != null && directory.exists()) {
			showVariableOverview(new CurrentDirectoryController(directory));
		}
	}

	/**
	 * Retorna o arquivo de prefer�ncias da pessoa, o �ltimo arquivo que foi
	 * aberto. As prefer�ncias s�o lidas do registro espec�fico do SO (Sistema
	 * Operacional). Se tais pref�rencias n�o puderem ser encontradas, ele
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
	 * Define o caminho do arquivo do arquivo carregado atual. O caminho �
	 * persistido no registro espec�fico do SO (Sistema Operacional).
	 * 
	 * @param file
	 *            O arquivo ou null para remover o caminho
	 */
	public void setDirectryPath(File directory) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (directory != null) {
			prefs.put("directoryPath", directory.getPath());

			// Update the stage title.
			primaryStage.setTitle("Vari�veis - " + directory.getAbsolutePath());
		} else {
			prefs.remove("directoryPath");

			// Update the stage title.
			primaryStage.setTitle("Vari�veis");
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
				primaryStage.setTitle("Vari�veis - " + currentDirectoryController.getDirectory().getAbsolutePath());
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
		primaryStage.setTitle("Vari�veis - " + currentFileController.getFile().getAbsolutePath());
		this.currentFileController = currentFileController;
		switch (currentFileController.getFile().getName().substring(9, 12)) {
		case "TMP":
			variable = 0;
			break;
		case "LDR":
			variable = 1;
		}
		switch (view) {
		case 0: // Gr�ficos
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
	 * Busca exten��o da variavel.
	 * 
	 * @return Exten��o (TMP ou LDR).
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
	 * Set para o valor num�rico.
	 * 
	 * @param variable
	 *            Valor.
	 */
	public void setVariableNumber(int variable) {
		this.variable = variable;
	}

	/**
	 * Get padr�o do primaryStage.
	 * 
	 * @return Palco principal.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Get padr�o da varivel.
	 * 
	 * @return Variavel.
	 */
	public int getVariable() {
		return variable;
	}

	/**
	 * Set padr�o da varivel.
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
	 * Get padr�o da vis�o.
	 * 
	 * @return Vis�o.
	 */
	public int getView() {
		return view;
	}

	/**
	 * Set padr�o da vis�o.
	 * 
	 * @param view
	 *            Vis�o.
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
	 * Get padr�o da data padr�o.
	 * 
	 * @return Controlador da data padr�o.
	 */
	public CurrentFileController getCurrentFileController() {
		return currentFileController;
	}
}
