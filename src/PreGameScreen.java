import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

public class PreGameScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultListModel<ImageIcon> imageListModel;
    private JList<ImageIcon> imageList;
    private JPanel gridPanel;
    private JButton[][] buttons;
    private JButton startGameButton;
    private JButton toggleOrientationButton;
    private String[] imagePaths = { "images/carrier.png", "images/battleship.png", "images/submarine.png", "images/cruiser.png", "images/destroyer.png" };
    private int[][] placementArray;
    private static int[][] indexArray;
    private String language;
    private String username;
    private int difficulty;
    private boolean isHorizontal;
    private LanguagePack languagePack;

    public PreGameScreen(String username, String language, int difficulty) {
        this.language = language;
        this.username = username;
        this.difficulty = difficulty;

        languagePack = new LanguagePack();
        languagePack.loadLanguage(language);

        startGameButton = new JButton(languagePack.startGameButtonText);
        toggleOrientationButton = new JButton(languagePack.toggleOrientationButtonText);
        placementArray = new int[10][10];
        indexArray = new int[10][10];
        isHorizontal = true;

        fillTheIndexArray(indexArray);
        setTitle(languagePack.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        gridPanel = createGridPanel();
        JPanel imageListPanel = createImageListPanel();

        //ActionListener for startGame button.
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean allNull = true;
                for (int i = 0; i < imageListModel.size(); i++) {
                    if (imageListModel.get(i) != null) {
                        allNull = false;
                        break;
                    }
                }

                if (allNull) {
                    SwingUtilities.invokeLater(() -> new GameScreen(buttons, username, language, difficulty));
                    dispose();
                }
            }
        });

        //ActionListener for toggleOrientation button.
        toggleOrientationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isHorizontal = !isHorizontal; //Toggle orientation.
                toggleOrientationButton.setText(isHorizontal ? languagePack.horizontalText : languagePack.verticalText);
            }
        });

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(toggleOrientationButton, BorderLayout.WEST);
        southPanel.add(startGameButton, BorderLayout.CENTER);

        add(imageListPanel, BorderLayout.WEST);
        add(gridPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Creates and configures the grid panel for ship placement.
    public JPanel createGridPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 10, 0, 0));
        panel.setBackground(Color.WHITE);

        buttons = new JButton[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setFocusPainted(false);
                button.setTransferHandler(new ImageTransferHandler(i, j));
                buttons[i][j] = button;
                panel.add(button);
            }
        }

        return panel;
    }

    //Creates and configures the panel for the image list.
    public JPanel createImageListPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        buttonPanel.add(toggleOrientationButton);

        imageListModel = new DefaultListModel<>();
        imageList = new JList<>(imageListModel);
        imageList.setCellRenderer(new ImageCellRenderer());
        imageList.setDragEnabled(true);
        imageList.setTransferHandler(new ListItemTransferHandler());

        loadSampleImages();

        JScrollPane scrollPane = new JScrollPane(imageList);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    //Loads sample images into the image list.
    public void loadSampleImages() {
        ArrayList<Integer> imageSpace = new ArrayList<>();
        int counter = 5;

        for (String path : imagePaths) {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                Image scaledImage = image.getScaledInstance(100, 300, Image.SCALE_SMOOTH);
                imageListModel.addElement(new ImageIcon(scaledImage));
                imageSpace.add(counter);
                counter--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Custom cell renderer for displaying images in the image list.
    public class ImageCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon((ImageIcon) value);
            label.setText("");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }

    //Transfer handler for dragging ship images onto the grid.
    public class ListItemTransferHandler extends TransferHandler {
        @Override
        protected Transferable createTransferable(JComponent c) {
            JList<?> list = (JList<?>) c;
            Object value = list.getSelectedValue();
            if (value instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) value;
                return new ImageSelection(imageIcon.getImage(), list.getSelectedIndex());
            }
            return null;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    //Represents a selected ship image during drag-and-drop operations.
    public class ImageSelection implements Transferable {
        private Image image;
        private int index;

        public ImageSelection(Image image, int index) {
            this.image = image;
            this.index = index;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor, new DataFlavor(Integer.class, "Index") };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor) || flavor.getRepresentationClass() == Integer.class;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (DataFlavor.imageFlavor.equals(flavor)) {
                return image;
            } else if (flavor.getRepresentationClass() == Integer.class) {
                return index;
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }

    //Transfer handler for dropping ship images onto the grid cells.
    public class ImageTransferHandler extends TransferHandler {
        private static final long serialVersionUID = 1L;
        private int row;
        private int col;

        public ImageTransferHandler(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.imageFlavor)
                    || !support.isDataFlavorSupported(new DataFlavor(Integer.class, "Index"))) {
                return false;
            }

            try {
                Transferable transferable = support.getTransferable();
                int index = (Integer) transferable.getTransferData(new DataFlavor(Integer.class, "Index"));
                int rowsToSpan = isHorizontal ? 1 : getShipSize(index);
                int colsToSpan = isHorizontal ? getShipSize(index) : 1;

                if (row + rowsToSpan > buttons.length || col + colsToSpan > buttons[0].length) {
                    return false;
                }

                for (int i = row; i < row + rowsToSpan; i++) {
                    for (int j = col; j < col + colsToSpan; j++) {
                        if (buttons[i][j].getIcon() != null) {
                            return false;
                        }
                    }
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            try {
                Transferable transferable = support.getTransferable();
                Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                int index = (Integer) transferable.getTransferData(new DataFlavor(Integer.class, "Index"));

                int rowsToSpan = isHorizontal ? 1 : getShipSize(index);
                int colsToSpan = isHorizontal ? getShipSize(index) : 1;

                setButtonIcon(row, col, image, rowsToSpan, colsToSpan, index);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        //Determines the size of the ship based on its index.
        public int getShipSize(int index) {
            switch (index) {
                case 0:
                    return 5;
                case 1:
                    return 4;
                case 2:
                case 3:
                    return 3;
                case 4:
                default:
                    return 2;
            }
        }

        //Sets the icon for the grid cells where the ship is placed.
        public void setButtonIcon(int row, int col, Image image, int rowsToSpan, int colsToSpan, int index) {
            BufferedImage image2 = null;
            String path = null;

            for (int i = row; i < row + rowsToSpan; i++) {
                for (int j = col; j < col + colsToSpan; j++) {
                    if (i >= buttons.length || j >= buttons[i].length || buttons[i][j].getIcon() != null) {
                        return; //Stop if out of bounds or position is already occupied.
                    }
                }
            }

            for (int i = row; i < row + rowsToSpan; i++) {
                for (int j = col; j < col + colsToSpan; j++) {
                    switch (index) {
                        case 0:
                            if (isHorizontal) {
                                path = "images/Carrier_horizontal_part" + (j - col + 1) + ".png";
                            } else {
                                path = "images/Carrier_vertical_part" + (i - row + 1) + ".png";
                            }
                            break;
                        case 1:
                            if (isHorizontal) {
                                path = "images/Battleship_horizontal_part" + (j - col + 1) + ".png";
                            } else {
                                path = "images/Battleship_vertical_part" + (i - row + 1) + ".png";
                            }
                            break;
                        case 2:
                            if (isHorizontal) {
                                path = "images/Submarine_horizontal_part" + (j - col + 1) + ".png";
                            } else {
                                path = "images/Submarine_vertical_part" + (i - row + 1) + ".png";
                            }
                            break;
                        case 3:
                            if (isHorizontal) {
                                path = "images/Cruiser_horizontal_part" + (j - col + 1) + ".png";
                            } else {
                                path = "images/Cruiser_vertical_part" + (i - row + 1) + ".png";
                            }
                            break;
                        case 4:
                            if (isHorizontal) {
                                path = "images/Destroyer_horizontal_part" + (j - col + 1) + ".png";
                            } else {
                                path = "images/Destroyer_vertical_part" + (i - row + 1) + ".png";
                            }
                            break;
                    }
                    try {
                        image2 = ImageIO.read(new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buttons[i][j].setIcon(new ImageIcon(image2));
                    buttons[i][j].setOpaque(true);
                    buttons[i][j].setContentAreaFilled(true);
                    buttons[i][j].repaint();
                    placementArray[i][j] = 1;
                    indexArray[i][j] = index;
                }
            }

            imageListModel.remove(index);
            imageListModel.insertElementAt(null, index);
        }
    }

    //Fills the index array with initial values.
    public void fillTheIndexArray(int[][] index_array) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                index_array[i][j] = -1;
            }
        }
    }

    //Retrieves the index array.
    public static int[][] getIndexArray() {
        return indexArray;
    }

    //Language pack class to manage language-specific strings/
    public class LanguagePack {
        private String startGameButtonText;
        private String toggleOrientationButtonText;
        private String windowTitle;
        private String horizontalText;
        private String verticalText;

        //Loads language-specific strings.
        public void loadLanguage(String language) {
            if (language.equals("en")) {
                startGameButtonText = "START GAME";
                toggleOrientationButtonText = "Toggle Ship Orientation";
                windowTitle = "Place your ships!";
                verticalText = "Vertical";
                horizontalText = "Horizontal";
            } else {
                startGameButtonText = "ΠΑΙΞΕ";
                toggleOrientationButtonText = "Εναλλαγή Προσανατολισμού Πλοίου";
                windowTitle = "Τοποθέτησε τα πλοία σου!";
                verticalText = "Κάθετο";
                horizontalText = "Οριζόντιο";
            }
        }
    }
}
