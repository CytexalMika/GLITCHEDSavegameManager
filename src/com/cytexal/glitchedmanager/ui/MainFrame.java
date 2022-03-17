package com.cytexal.glitchedmanager.ui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	private static MainFrame instance;

	static File savegameFolder;
	static File newSavegameFolder;

	JComboBox<File> originalFiles;
	JList<File> newFiles;

	public static void main(String[] args) throws IOException {
		savegameFolder = new File(System.getenv("LOCALAPPDATA") + "/GLITCHED");
		newSavegameFolder = new File("saves");
		if (!newSavegameFolder.exists())
			newSavegameFolder.mkdir();
		System.out.println(savegameFolder.getAbsolutePath());

		instance = new MainFrame();

		// SavefileConverter conv = new SavefileConverter(new File("saves/test"));
		// System.out.println(conv.toString());
		// JSONArray a = SavefileConverter.toJSONArray(new File("saves/test"));
		// File out = new File("saves/testOut");
		// FileWriter w = new FileWriter(out);
		// System.out.println(SavefileConverter.saveFromJSON(a));
	}

	public MainFrame() {
		super("GLITCHED Savegame Manager");
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		newFiles = new JList<File>();
		JScrollPane listScroller = new JScrollPane(newFiles);
		listScroller.setBounds(10, 50, 770, 400);
		add(listScroller);

		JButton copy = new JButton("Make a copy of Slot 1");
		copy.addActionListener((e) -> {
			try {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				File orig = ((File) originalFiles.getSelectedItem());
				Files.copy(orig.toPath(),
						new File(newSavegameFolder, orig.getName() + "_" + DATE_FORMAT.format(timestamp)).toPath());
				refresh();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		copy.setBounds(10, 460, 380, 30);
		add(copy);

		JButton set = new JButton("Set Slot 1 to copy");
		set.addActionListener((e) -> {
			try {
				File orig = ((File) originalFiles.getSelectedItem());
				Files.copy(newFiles.getSelectedValue().toPath(), orig.toPath(), StandardCopyOption.REPLACE_EXISTING);
				refresh();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		set.setBounds(10, 500, 380, 30);
		add(set);

		JButton copyToJ = new JButton("Open copy in Editor");
		copyToJ.addActionListener((e) -> {
			new EditorFrame(newFiles.getSelectedValue());
		});
		copyToJ.setBounds(400, 460, 380, 30);
		add(copyToJ);

		originalFiles = new JComboBox<File>();
		originalFiles.setBounds(10, 10, 770, 30);
		originalFiles.addActionListener((e) -> {
			set.setText("Set Slot " + ((File) originalFiles.getSelectedItem()).getName().substring(17) + " to copy");
			copy.setText("Make a copy of Slot " + ((File) originalFiles.getSelectedItem()).getName().substring(17));
		});
		add(originalFiles);
		
		File[] files = new File[3];
		for (int i = 0; i < files.length; i++)
			files[i] = new File(savegameFolder, "savefile_gus_v01_" + (i + 1));
		originalFiles.setModel(new DefaultComboBoxModel<File>(files));

		refresh();

		setVisible(true);
	}

	public void refresh() {
		DefaultListModel<File> listModel = new DefaultListModel<File>();
		listModel.addAll(Arrays.asList(newSavegameFolder.listFiles()));
		newFiles.setModel(listModel);
		originalFiles.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				value = "Save Slot " + ((File) value).getName().substring(17);

				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});

		listModel = new DefaultListModel<File>();
	}

	static MainFrame getInstance() {
		return instance;
	}
}
