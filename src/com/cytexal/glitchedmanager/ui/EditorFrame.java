package com.cytexal.glitchedmanager.ui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import com.cytexal.glitchedmanager.parse.SavefileList;
import com.cytexal.glitchedmanager.parse.SavefileReader;
import com.cytexal.glitchedmanager.parse.SavefileString;

public class EditorFrame extends JFrame {
	JTabbedPane container;

	GeneralPanel general;
	StringListPanel keepsakes;
	ItemQuantityPanel tools;
	ItemQuantityPanel knickKnacks;
	ItemQuantityPanel currencies;
	EnumListPanel states;
	StringListPanel books;
	StringListPanel achievements;
	StringListPanel interactedObjects;
	StringListPanel triggers;

	File savefile;
	SavefileList mainList;

	public EditorFrame(File file) {
		super("GLITCHED Savegame Editor");
		this.savefile = file;
		container = new JTabbedPane();

		try {
			SavefileReader r = new SavefileReader(file);
			mainList = (SavefileList) r.read();

			container.addTab("General", general = new GeneralPanel(mainList));
			container.addTab("Keepsakes", keepsakes = new StringListPanel("Keepsakes", (SavefileString) mainList.get(11)));
			container.addTab("Tools", tools = new ItemQuantityPanel("Tools", (SavefileString) mainList.get(12),
					(SavefileString) mainList.get(13)));
			container.addTab("knick-knacks", knickKnacks = new ItemQuantityPanel("knick-knacks",
					(SavefileString) mainList.get(14), (SavefileString) mainList.get(15)));
			container.addTab("Currencies", currencies = new ItemQuantityPanel("Currencies",
					(SavefileString) mainList.get(16), (SavefileString) mainList.get(17)));
			container.addTab("States", states = new EnumListPanel("States", (SavefileString) mainList.get(20)));
			container.addTab("Books", books = new StringListPanel("Books", (SavefileString) mainList.get(28)));
			container.addTab("Achievements", achievements = new StringListPanel("Achievements", (SavefileString) mainList.get(44)));
			container.addTab("Interacted Objects",
					interactedObjects = new StringListPanel("Interacted Objects", (SavefileString) mainList.get(46)));
			container.addTab("Triggers", triggers = new StringListPanel("Triggers", (SavefileString) mainList.get(65)));

			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			JMenuItem saveMenuItem = new JMenuItem("Save");

			menuBar.add(fileMenu);

			fileMenu.add(saveMenuItem);

			// JMenuItem resetMenuItem = new JMenuItem("Reset");
			saveMenuItem.setMnemonic(KeyEvent.VK_S);
			saveMenuItem.addActionListener((e) -> {
				save();
			});

			setJMenuBar(menuBar);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(container);

		setSize(850, 600);
		setLocationRelativeTo(null);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	void save() {
		general.save();
		tools.save();
		keepsakes.save();
		knickKnacks.save();
		currencies.save();
		states.save();
		books.save();
		achievements.save();
		interactedObjects.save();
		triggers.save();

		String filePath = savefile.getPath();

		if (!filePath.endsWith(".edited"))
			filePath += ".edited";

		File file = new File(filePath);

		try {
			PrintWriter writer = new PrintWriter(file);
			writer.print("[Lists]\r\n0=\"");
			writer.write(mainList.getEncoded());
			writer.print("\"\r\n");
			writer.close();

			MainFrame.getInstance().refresh();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
