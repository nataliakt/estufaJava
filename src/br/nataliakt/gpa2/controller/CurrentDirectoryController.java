package br.nataliakt.gpa2.controller;

import java.io.File;
import java.util.ArrayList;

/**
 * Classe para controlar o diretório padrão.
 * 
 * @author Natalia Kelim Thiel
 */
public class CurrentDirectoryController {

	// Diretório atual
	private File directory;

	/**
	 * Construtor padrão.
	 * 
	 * @param directory Diretório.
	 */
	public CurrentDirectoryController(File directory) {
		super();
		this.directory = directory;
	}

	/**
	 * Busca os arquivos da pasta.
	 * 
	 * @return Lista de arquivos.
	 */
	public ArrayList<String> getDataFiles() {
		ArrayList<String> list = new ArrayList<String>();
		for (File file : directory.listFiles()) {
			if (file.getName().length() == 12) {
				// Extenção do arquivo
				String ext = file.getName().substring(file.getName().length() - 4, file.getName().length());
				String date = file.getName().substring(0, 2) + "/" + file.getName().substring(2, 4) + "/"
						+ file.getName().substring(4, 8);
				if ((ext.equals(".TMP") || ext.equals(".LDR")) && !list.contains(date)) {
					list.add(date);
				}
			}
		}
		return list;
	}

	/**
	 * Verifica se há um arquivo de uma data.
	 * 
	 * @param date Data no formato dd/mm/aaaa.
	 * @return Se existe.
	 */
	public boolean isFile(String date) {
		if (getDataFiles().contains(date)) {
			return true;
		}
		return false;
	}

	/**
	 * Busca um arquivo de uma data
	 * @param date Data no formato dd/mm/aaaa.
	 * @param extencion Extenção.
	 * @return Arquivo
	 */
	public File fileOf(String date, String extencion) {
		File fileOfDate = null;
		for (File file : directory.listFiles()) {
			if (file.getName().length() == 12) {
				String fileName = date.substring(0, 2) + date.substring(3, 5) + date.substring(6, 10) + "." + extencion;
				if(file.getName().equals(fileName)){
					fileOfDate = file;
				}
			}
		}
		return fileOfDate;
	}

	/**
	 * Get padrão do diretório.
	 * 
	 * @return Diretório.
	 */
	public File getDirectory() {
		return directory;
	}

}
