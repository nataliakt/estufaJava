package br.nataliakt.gpa2.util;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Classe para gerar uma legenda nos itens do gráfico.
 * 
 * @author Natalia Kelim Thiel
 *
 */
public class HoveredThresholdNode extends StackPane {

	/**
	 * Construtor com valor a ser legendado.
	 * 
	 * @param value
	 *            Valor.
	 */
	public HoveredThresholdNode(int value, Color color, String defaultColor) {
		setPrefSize(15, 15);

		final Label label = createDataThresholdLabel(value, color, defaultColor);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
			}
		});
	}

	/**
	 * Método que adiciona a legenda.
	 * 
	 * @param value
	 *            Valor.
	 * @return Label gerado.
	 */
	private Label createDataThresholdLabel(int value, Color color, String defaultColor) {
		final Label label = new Label(value + "");
		label.getStyleClass().addAll(defaultColor, "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");

		label.setTextFill(color);

		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}