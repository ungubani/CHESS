package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenuView extends JFrame {
    public MainMenuView() {
        setTitle("Шахматы");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Используем BorderLayout

        // Панель для кнопок с вертикальным расположением
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Выравнивание по центру

        // Создаем кнопки
        JButton playWithFriend = new JButton("Игра с другом");
        JButton playWithComputer = new JButton("Игра с компьютером");
        JButton exit = new JButton("Выход");

        // Добавляем отступы между кнопками с помощью пустых границ
        playWithFriend.setAlignmentX(Component.CENTER_ALIGNMENT);
        playWithComputer.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Добавляем обработку событий
        playWithFriend.addActionListener(e -> startGame(false));
        playWithComputer.addActionListener(e -> startGame(true));
        exit.addActionListener(e -> System.exit(0));

        // Добавляем кнопки в панель
        buttonPanel.add(playWithFriend);
        buttonPanel.add(playWithComputer);
        buttonPanel.add(exit);

        
        // Размещаем панель в центре окна
        add(buttonPanel, BorderLayout.CENTER);

        // Центрируем окно относительно экрана
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void startGame(boolean againstComputer) {
        new GameView(againstComputer);
        dispose(); // Закрывает главное меню
    }
}
