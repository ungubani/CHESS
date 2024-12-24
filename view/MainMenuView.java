package view;

import javax.swing.*;
import java.awt.*;


public class MainMenuView extends JFrame {
    public MainMenuView() {
        setTitle("Шахматы");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playWithFriend = new JButton("Игра с другом");
        JButton playWithComputer = new JButton("Игра с компьютером");
        JButton exit = new JButton("Выход");

        playWithFriend.setAlignmentX(Component.CENTER_ALIGNMENT);
        playWithComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        playWithFriend.addActionListener(e -> startGame(false));
        playWithComputer.addActionListener(e -> startGame(true));
        exit.addActionListener(e -> System.exit(0));

        buttonPanel.add(playWithFriend);
        buttonPanel.add(playWithComputer);
        buttonPanel.add(exit);

        add(buttonPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void startGame(boolean againstComputer) {
        new GameView(againstComputer);
        dispose();
    }
}
