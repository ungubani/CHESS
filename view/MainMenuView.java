package view;

import model.*;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MainMenuView extends JFrame {
    public MainMenuView() {
        setTitle("Шахматы");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        JButton playWithFriend = new JButton("Игра с другом");
        JButton playWithComputer = new JButton("Игра с компьютером");
        JButton exit = new JButton("Выход");

        playWithFriend.addActionListener(e -> startGame(false));
        playWithComputer.addActionListener(e -> startGame(true));
        exit.addActionListener(e -> System.exit(0));

        add(playWithFriend);
        add(playWithComputer);
        add(exit);

        setVisible(true);
    }

    private void startGame(boolean againstComputer) {
        new GameView(againstComputer);
        dispose(); // Закрывает главное меню
    }
}
