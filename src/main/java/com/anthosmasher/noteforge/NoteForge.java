package com.anthosmasher.noteforge;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

public class NoteForge extends JFrame implements ActionListener {

    private final JTextArea area;
    private final JScrollPane scpane;
    private File currentFile = null;
    String text = "";

    public NoteForge() {
        super("NoteForge");
        setBackground(Color.darkGray);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1950, 1050);

        URL iconURL = getClass().getResource("/noteforge.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.darkGray);

        JMenu file = new JMenu("File");
        file.setFont(new Font("Aerial", Font.PLAIN, 14));
        file.setForeground(Color.white);

        JMenuItem newdoc = new JMenuItem("New");
        newdoc.setFont(new Font("Aerial", Font.PLAIN, 14));
        newdoc.setBackground(Color.darkGray);
        newdoc.setForeground(Color.white);
        newdoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newdoc.addActionListener(this);

        JMenuItem open = new JMenuItem("Open");
        open.setFont(new Font("Aerial", Font.PLAIN, 14));
        open.setBackground(Color.darkGray);
        open.setForeground(Color.white);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.addActionListener(this);

        JMenuItem save = new JMenuItem("Save");
        save.setFont(new Font("Aerial", Font.PLAIN, 14));
        save.setBackground(Color.darkGray);
        save.setForeground(Color.white);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.addActionListener(this);

        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.setFont(new Font("Aerial", Font.PLAIN, 14));
        saveAs.setBackground(Color.darkGray);
        saveAs.setForeground(Color.white);
        saveAs.addActionListener(this);

        JMenuItem print = new JMenuItem("Print");
        print.setFont(new Font("Aerial", Font.PLAIN, 14));
        print.setBackground(Color.darkGray);
        print.setForeground(Color.white);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        print.addActionListener(this);

        JMenuItem exit = new JMenuItem("Exit");
        exit.setFont(new Font("Aerial", Font.PLAIN, 14));
        exit.setBackground(Color.darkGray);
        exit.setForeground(Color.white);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        exit.addActionListener(this);

        JMenu edit = new JMenu("Edit");
        edit.setFont(new Font("Aerial", Font.PLAIN, 14));
        edit.setForeground(Color.white);

        JMenuItem copy = new JMenuItem("Copy");
        copy.setFont(new Font("Aerial", Font.PLAIN, 14));
        copy.setBackground(Color.darkGray);
        copy.setForeground(Color.white);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copy.addActionListener(this);

        JMenuItem paste = new JMenuItem("Paste");
        paste.setFont(new Font("Aerial", Font.PLAIN, 14));
        paste.setBackground(Color.darkGray);
        paste.setForeground(Color.white);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        paste.addActionListener(this);

        JMenuItem cut = new JMenuItem("Cut");
        cut.setFont(new Font("Aerial", Font.PLAIN, 14));
        cut.setBackground(Color.darkGray);
        cut.setForeground(Color.white);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cut.addActionListener(this);

        JMenuItem selectall = new JMenuItem("Select All");
        selectall.setFont(new Font("Aerial", Font.PLAIN, 14));
        selectall.setBackground(Color.darkGray);
        selectall.setForeground(Color.white);
        selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        selectall.addActionListener(this);

        JMenu about = new JMenu("Help");
        about.setFont(new Font("Aerial", Font.PLAIN, 14));
        about.setForeground(Color.white);

        JMenuItem notepad = new JMenuItem("About NoteForge");
        notepad.setFont(new Font("Aerial", Font.PLAIN, 14));
        notepad.setBackground(Color.darkGray);
        notepad.setForeground(Color.white);
        notepad.addActionListener(this);

        setJMenuBar(menuBar);
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(about);

        file.add(newdoc);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(print);
        file.add(exit);

        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectall);

        about.add(notepad);

        area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretColor(Color.white);
        area.setBackground(Color.darkGray);
        area.setForeground(Color.white);

        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                int caretPos = area.getCaretPosition();

                // Auto-Closing Pairs
                if (c == '(' || c == '{' || c == '[' || c == '\'' || c == '\"' || c == '<') {
                    String closingChar = switch (c) {
                        case '(' -> ")";
                        case '{' -> "}";
                        case '[' -> "]";
                        case '\'' -> "'";
                        case '\"' -> "\"";
                        case '<' -> ">"; // Added for HTML tags
                        default -> "";
                    };

                    // Insert both the opening and closing character
                    area.insert(c + closingChar, caretPos);
                    area.setCaretPosition(caretPos + 1); // Move caret inside the brackets/quotes
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    try {
                        int caretPos = area.getCaretPosition();
                        if (caretPos > 0) {
                            char prevChar = area.getText(caretPos - 1, 1).charAt(0);

                            // Check if backspace is pressed after a '{' and the next character is '}'
                            if (prevChar == '{' && caretPos < area.getText().length()) {
                                char nextChar = area.getText(caretPos, 1).charAt(0);

                                if (nextChar == '}') {
                                    area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '{' and '}'
                                    area.setCaretPosition(caretPos - 1); // Move caret to the left of the '{'
                                    e.consume(); // Consume the event to prevent default backspace behavior
                                }
                            }
                            // Repeat for other pairs like '(', '[', "'", and '"'
                            else if (prevChar == '(' && caretPos < area.getText().length() && area.getText(caretPos, 1).charAt(0) == ')') {
                                area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '(' and ')'
                                area.setCaretPosition(caretPos - 1);
                                e.consume();
                            } else if (prevChar == '[' && caretPos < area.getText().length() && area.getText(caretPos, 1).charAt(0) == ']') {
                                area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '[' and ']'
                                area.setCaretPosition(caretPos - 1);
                                e.consume();
                            } else if (prevChar == '\'' && caretPos < area.getText().length() && area.getText(caretPos, 1).charAt(0) == '\'') {
                                area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '\'' and '\''
                                area.setCaretPosition(caretPos - 1);
                                e.consume();
                            } else if (prevChar == '\"' && caretPos < area.getText().length() && area.getText(caretPos, 1).charAt(0) == '\"') {
                                area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '\"' and '\"'
                                area.setCaretPosition(caretPos - 1);
                                e.consume();
                            } else if (prevChar == '<' && caretPos < area.getText().length() && area.getText(caretPos, 1).charAt(0) == '>') {
                                area.replaceRange("", caretPos - 1, caretPos + 1); // Delete both '<' and '>'
                                area.setCaretPosition(caretPos - 1);
                                e.consume();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        int caretPos = area.getCaretPosition();
                        int lineNum = area.getLineOfOffset(caretPos);
                        int lineStart = area.getLineStartOffset(lineNum);

                        String prevLine = area.getText(lineStart, caretPos - lineStart);
                        String indentation = prevLine.replaceAll("\\S.*", ""); // Extract leading spaces

                        // Check if cursor is immediately after an opening bracket or quote
                        if (caretPos > 0 && caretPos < area.getText().length()) {
                            char currentChar = area.getText(caretPos - 1, 1).charAt(0);
                            char nextChar = area.getText(caretPos, 1).charAt(0);

                            boolean isBracket = (currentChar == '{' && nextChar == '}') ||
                                    (currentChar == '(' && nextChar == ')') ||
                                    (currentChar == '[' && nextChar == ']');

                            if (isBracket) {
                                String newIndent = "\n" + indentation + "    "; // Indented block
                                String closingLine = "\n" + indentation; // Closing bracket stays in place

                                area.insert(newIndent + closingLine, caretPos);
                                area.setCaretPosition(caretPos + newIndent.length()); // Move caret inside
                                e.consume();
                                return;
                            }
                        }

                        // Normal indentation behavior (when not between brackets)
                        String newIndent = "\n" + indentation;
                        area.insert(newIndent, caretPos);
                        area.setCaretPosition(caretPos + newIndent.length());
                        e.consume();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        scpane = new JScrollPane(area);
        scpane.setBorder(BorderFactory.createEmptyBorder());
        scpane.getVerticalScrollBar().setBackground(Color.darkGray);
        scpane.getHorizontalScrollBar().setBackground(Color.darkGray);
        scpane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.gray;
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        add(scpane, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New")) {
            area.setText("");
        } else if (e.getActionCommand().equals("Open")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(true);

            FileNameExtensionFilter nfgFilter = new FileNameExtensionFilter("NoteForge Files (.nfg)", "nfg");
            chooser.setFileFilter(nfgFilter);

            int result = chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();

            // Check if the file is a valid text file
            if (!isTextFile(file)) {
                JOptionPane.showMessageDialog(this,
                        "Not a valid text-based file",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop execution if it's not text
            }

            // Read and load text into the JTextArea
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                area.read(br, null);
                area.requestFocus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("Save")) {
            if (currentFile == null) {
                saveAsFile();
            } else {
                saveFile(currentFile);
            }
        } else if (e.getActionCommand().equals("Save As")) {
            saveAsFile();
        } else if (e.getActionCommand().equals("Print")) {
            try {
                area.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Copy")) {
            text = area.getSelectedText();
        } else if (e.getActionCommand().equals("Paste")) {
            area.insert(text, area.getCaretPosition());
        } else if (e.getActionCommand().equals("Cut")) {
            text = area.getSelectedText();
            area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
        } else if (e.getActionCommand().equals("Select All")) {
            area.selectAll();
        } else if (e.getActionCommand().equals("About NoteForge")) {
            new About().setVisible(true);
        }
    }

    private boolean isTextFile(File file) {
        try (InputStream in = new FileInputStream(file)){
            int size = 1024;
            byte[] buffer = new byte[size];
            int read = in.read(buffer);
            if (read == -1) return false;

            for (int i = 0; i < read; i++) {
                if (buffer[i] == 0) return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(area.getText());
            writer.flush();
            currentFile = file;
        } catch (IOException ex) {
            JOptionPane optionPane = new JOptionPane("Error saving file!", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void saveAsFile() {
        JFileChooser saveas = new JFileChooser();
        saveas.setDialogTitle("Save As");
        saveas.setApproveButtonText("Save");
        saveas.setAcceptAllFileFilterUsed(true);

        FileNameExtensionFilter nfgFilter = new FileNameExtensionFilter("NoteForge Files (*.nfg)", "nfg");
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        saveas.addChoosableFileFilter(txtFilter);
        saveas.setFileFilter(nfgFilter); // Default selection

        int action = saveas.showSaveDialog(this);
        if (action != JFileChooser.APPROVE_OPTION) {
            return; // Exit if user cancels
        }

        File file = saveas.getSelectedFile();
        String filePath = file.getAbsolutePath();

        // Ensure the correct file extension if missing
        if (!filePath.contains(".")) {
            FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) saveas.getFileFilter();
            String selectedExtension = "nfg"; // Default extension
            if (selectedFilter == txtFilter) {
                selectedExtension = "txt";
            }
            file = new File(filePath + "." + selectedExtension);
        }

        saveFile(file);
    }
    public static void main(String[] args) {
        new NoteForge();
    }
}