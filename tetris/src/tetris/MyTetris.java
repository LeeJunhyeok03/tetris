package tetris;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.*;
import java.io.*;

public class MyTetris extends JFrame {
    private JPanel contentPane;
    public static TetrisCanvas tetrisCanvas = new TetrisCanvas();
    private Clip bgmClip;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MyTetris frame = new MyTetris();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyTetris() {
        setTitle("Tetris");
        TetrisView tetrisView = new TetrisView();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 560, 629);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("\uAC8C\uC784");
        menuBar.add(menu);
        JMenuItem mntmNewMenuItem = new JMenuItem("\uC2DC\uC791");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tetrisCanvas.start();
                playBGM();
            }
        });
        menu.add(mntmNewMenuItem);
        JMenuItem mntmNewMenuItem_1 = new JMenuItem("\uC885\uB8CC");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                stopBGM();
                System.exit(0);
            }
        });
        menu.add(mntmNewMenuItem_1);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        contentPane.add(tetrisView, BorderLayout.CENTER);
    }

    private void playBGM() {
        try {
            File bgmFile = new File(".\\src\\tetris\\tetris_background_music.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bgmFile);
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            bgmClip = (Clip) AudioSystem.getLine(info);
            bgmClip.open(audioInputStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}
