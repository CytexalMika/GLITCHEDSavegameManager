package com.cytexal.glitchedmanager.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;

public class EditorHelper {

	public static JPanel createTextfield(String name, String value, ChangeListener listener) {
		JLabel myLabel = new JLabel(name);
		JTextField myTextField = new JTextField(value);
		if (listener != null)
			addChangeListener(myTextField, listener);

		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(myLabel);
		panel.add(myTextField);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

		return panel;
	}

	public static JPanel createNumberfield(String name, Number value, Class<? extends Number> numClass,
			ChangeListener listener) {

		NumberFormat format = NumberFormat.getInstance(new Locale("en", "US"));
		format.setGroupingUsed(false);
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(numClass);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);

		JLabel myLabel = new JLabel(name);
		JFormattedTextField myTextField = new JFormattedTextField(formatter);
		myTextField.setValue(value);
		if (listener != null)
			addChangeListener(myTextField, listener);

		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(myLabel);
		panel.add(myTextField);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

		return panel;
	}

	/**
	 * Installs a listener to receive notification when the text of any
	 * {@code JTextComponent} is changed. Internally, it installs a
	 * {@link DocumentListener} on the text component's {@link Document}, and a
	 * {@link PropertyChangeListener} on the text component to detect if the
	 * {@code Document} itself is replaced.
	 * 
	 * @param text           any text component, such as a {@link JTextField} or
	 *                       {@link JTextArea}
	 * @param changeListener a listener to receieve {@link ChangeEvent}s when the
	 *                       text is changed; the source object for the events will
	 *                       be the text component
	 * @throws NullPointerException if either parameter is null
	 */
	public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
		Objects.requireNonNull(text);
		Objects.requireNonNull(changeListener);
		DocumentListener dl = new DocumentListener() {
			private int lastChange = 0, lastNotifiedChange = 0;

			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lastChange++;
				SwingUtilities.invokeLater(() -> {
					if (lastNotifiedChange != lastChange) {
						lastNotifiedChange = lastChange;
						changeListener.stateChanged(new ChangeEvent(text));
					}
				});
			}
		};
		text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
			Document d1 = (Document) e.getOldValue();
			Document d2 = (Document) e.getNewValue();
			if (d1 != null)
				d1.removeDocumentListener(dl);
			if (d2 != null)
				d2.addDocumentListener(dl);
			dl.changedUpdate(null);
		});
		Document d = text.getDocument();
		if (d != null)
			d.addDocumentListener(dl);
	}
}
