package br.nataliakt.gpa2.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import br.nataliakt.gpa2.model.Variable;

/**
 * Classe de controle para a data padrão.
 * 
 * @author Natalia Kelim Thiel
 *
 */
public class CurrentFileController {

	// Arquivo atual
	private File file;

	/**
	 * Contrutor padrão.
	 * 
	 * @param file
	 *            Arquivo.
	 */
	public CurrentFileController(File file) {
		super();
		this.file = file;
	}

	/**
	 * Contrutor para mudar a extenção.
	 * 
	 * @param currentFileController
	 *            Controller anterior.
	 * @param extencion
	 *            Extenção.
	 */
	public CurrentFileController(CurrentFileController currentFileController, String extencion) {
		super();
		for (File file : currentFileController.getFile().getParentFile().listFiles()) {
			String fileName = currentFileController.getFile().getName().substring(0, 8) + "." + extencion;
			if (fileName.equals(file.getName())) {
				this.file = file;
			}
		}
	}

	/**
	 * Lê o arquivo.
	 * 
	 * @return Vetor com as variaveis.
	 */
	public ArrayList<Variable> readFile() {
		// Cria o arraylist
		ArrayList<Variable> list = new ArrayList<Variable>();
		// Cria o buffer
		BufferedReader buffRead = null;
		try {
			buffRead = new BufferedReader(new FileReader(file.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String value = "";
		String hour = "";
		String minute = "";
		String second = "";
		String read = "";
		try {
			while (buffRead.ready()) {
				// Limpando as variáveis
				value = "";
				hour = "";
				minute = "";
				second = "";
				read = "";
				// Percorre os caracteres da variavel
				while (!read.equals("/")) {
					value += read;
					try {
						read = Character.toString((char) buffRead.read());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Limpa a variável leitura
				read = "";
				// Percorre os caracteres da hora
				while (!read.equals(":")) {
					hour += read;
					try {
						read = Character.toString((char) buffRead.read());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Limpa a variável leitura
				read = "";
				// Percorre os caracteres do minuto
				while (!read.equals(":")) {
					minute += read;
					try {
						read = Character.toString((char) buffRead.read());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Limpa a variável leitura
				read = "";
				// Percorre os caracteres do segundo
				while (!read.equals("/")) {
					second += read;
					try {
						read = Character.toString((char) buffRead.read());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Limpa a variável leitura
				read = "";
				// Percorre a data (desnecessária)
				while (!read.equals(";")) {
					try {
						read = Character.toString((char) buffRead.read());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!value.equals(" NAN")) {
					try {
						Calendar c = Calendar.getInstance();
						c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
						c.set(Calendar.MINUTE, Integer.parseInt(minute));
						c.set(Calendar.SECOND, Integer.parseInt(second));
						Variable variable = new Variable(Float.parseFloat(value), c);
						list.add(variable);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			buffRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Data do arquivo.
	 * 
	 * @return Data no formato dd/mm/aaaa.
	 */
	public String getDate() {
		return file.getName().substring(0, 2) + "/" + file.getName().substring(2, 4) + "/"
				+ file.getName().substring(4, 8);
	}

	/**
	 * Get padrão do arquivo.
	 * 
	 * @return Arquivo.
	 */
	public File getFile() {
		return file;
	}

}
