package br.nataliakt.gpa2.view;

import java.util.Arrays;
import java.util.Calendar;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import br.nataliakt.gpa2.MainApp;
import br.nataliakt.gpa2.controller.CurrentDirectoryController;
import br.nataliakt.gpa2.controller.CurrentFileController;
import br.nataliakt.gpa2.model.Variable;
import br.nataliakt.gpa2.util.HoveredThresholdNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;

/**
 * Classe que controla VariableGraphics.fxml.
 * 
 * @author Natalia Kelim Thiel
 *
 */
public class VariableGraphicController {

	// Descrição da variável
	@FXML
	private MenuButton variable;

	// Data da variável
	@FXML
	private MenuButton date;

	// Data da variável
	@FXML
	private MenuButton view;

	// Eixo x
	@FXML
	private CategoryAxis xAxis;

	// Gráfico
	@FXML
	private LineChart<String, Number> variableChart;

	private ObservableList<String> hours = FXCollections.observableArrayList();

	// Aplicativo principal
	private MainApp mainApp;

	/**
	 * Inicializa a classe controller.
	 */
	@FXML
	private void initialize() {
		// Obtém an array com nomes dos meses em Inglês.
		String[] months = { "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00",
				"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
				"21:00", "22:00", "23:00" };
		// Converte o array em uma lista e adiciona na ObservableList
		hours.addAll(Arrays.asList(months));

		// Associa os nomes de mês como categorias para o eixo horizontal.
		xAxis.setCategories(hours);
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
	 * Chamada ao selecionar gráfico.
	 */
	@FXML
	private void handleGraphic() {
		mainApp.setView(0);
	}

	/**
	 * Chamada ao selecionar lista.
	 */
	@FXML
	private void handleList() {
		mainApp.setView(1);
	}

	/**
	 * Método que mostra os gráficos
	 */
	public void showGraphic() {
		showDescription();
		variableChart.setStyle("CHART_COLOR_1: #40C4FF;CHART_COLOR_2: #FF5252;CHART_COLOR_3: #e040fb;");
		// Faz a média da variavel
		int counter[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		float sum[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		boolean first[] = { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true, true };
		float max[] = new float[24];
		float min[] = new float[24];
		// Percorre as variáveis
		for (Variable p : mainApp.getCurrentFileController().readFile()) {
			int hour = p.getHour().get(Calendar.HOUR_OF_DAY);
			if (first[hour]) {
				max[hour] = p.getValue();
				min[hour] = p.getValue();
				first[hour] = false;
			}
			// Incrementa na hora
			counter[hour]++;
			// Soma no total
			sum[hour] += p.getValue();
			// Mínimos e máximos
			if (p.getValue() > max[hour]) {
				max[hour] = p.getValue();
			}
			if (p.getValue() < min[p.getHour().get(Calendar.HOUR_OF_DAY)]) {
				min[hour] = p.getValue();
			}
		}

		XYChart.Series<String, Number> seriesAvg = new XYChart.Series<>();
		XYChart.Series<String, Number> seriesMin = new XYChart.Series<>();
		XYChart.Series<String, Number> seriesMax = new XYChart.Series<>();

		// Cria um objeto XYChart.Data para cada hora. Adiciona ele às séries.
		for (int i = 0; i < 24; i++) {
			if (counter[i] > 0) {
				int media = (int) sum[i] / counter[i];
				XYChart.Data<String, Number> dataAvg = new XYChart.Data<>(hours.get(i), media);
				dataAvg.setNode(
						new HoveredThresholdNode(media, new Color(0.8784, 0.2509, 0.9843, 1), "default-color2"));
				seriesAvg.getData().add(dataAvg);

				XYChart.Data<String, Number> dataMin = new XYChart.Data<>(hours.get(i), (int) min[i]);
				dataMin.setNode(
						new HoveredThresholdNode((int) min[i], new Color(0.2509, 0.7686, 1, 1), "default-color0"));
				seriesMin.getData().add(dataMin);

				XYChart.Data<String, Number> dataMax = new XYChart.Data<>(hours.get(i), (int) max[i]);
				dataMax.setNode(
						new HoveredThresholdNode((int) max[i], new Color(1, 0.3215, 0.3215, 1), "default-color1"));
				seriesMax.getData().add(dataMax);
			}
		}
		seriesAvg.setName(getVariable() + " Média");
		seriesMin.setName(getVariable() + " Mínima");
		seriesMax.setName(getVariable() + " Máxima");
		variableChart.getData().add(seriesMin);
		variableChart.getData().add(seriesMax);
		variableChart.getData().add(seriesAvg);
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

}
