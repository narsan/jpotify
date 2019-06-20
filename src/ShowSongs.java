import com.mpatric.mp3agic.*;
import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ShowSongs extends JButton {

    public ShowSongs() {


        this.setFont(new Font("Arial", Font.PLAIN, 20));
        this.setText("Songs");
        this.setBackground(Color.BLACK);
        this.setForeground(Color.white);
        this.setBorder(null);

    }

    public JPanel CreatButtonFromSongs() throws InvalidDataException, IOException, UnsupportedTagException {
        JPanel songsPanel = new JPanel();
        songsPanel.setVisible(true);
        songsPanel.setLayout(new GridLayout(Library.getSongs().size(), Library.getSongs().size()));
        ArrayList<PausablePlayer> playedSongs=new ArrayList<PausablePlayer>();

        // songsPanel.setLayout(new FlowLayout());
        songsPanel.setBackground(Color.DARK_GRAY);
        JLabel Title = null;
        JButton songImage = null;




//            Iterator iterator=Library.getSongs().iterator();
        for (int i = 0; i < Library.getSongs().size(); i++) {
            File temp = Library.getSongs().get(i);
            Mp3File mp3File = new Mp3File(temp);


//TODO


            System.out.println(Library.getSongs().size());
            if (mp3File.hasId3v1Tag()) {
                ID3v1 id3v1 = mp3File.getId3v1Tag();
                Title = new JLabel();
                Title.setVisible(true);

                //Title.setText(id3v1.getTitle());
                System.out.println(id3v1.getTitle());


                Title.setFont(new Font("Arial", Font.PLAIN, 13));
                Title.setForeground(Color.white);
            }

            if (mp3File.hasId3v2Tag()) {


                ID3v2 id3v2 = mp3File.getId3v2Tag();
                songImage = new JButton();
                songImage.setBorder(null);
                songImage.setVisible(true);


                songImage.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JPanel showPlayingSong = new JPanel();
                        showPlayingSong.setLayout(new BorderLayout());
                        showPlayingSong.setBackground(Color.black);
                        byte[] imageData = id3v2.getAlbumImage();
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(new ByteArrayInputStream(imageData));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(60, 60, Image.SCALE_DEFAULT));

                        showPlayingSong.add(new JLabel(imageIcon), BorderLayout.WEST);
                        JLabel Artist = new JLabel(id3v2.getArtist());
                        Artist.setForeground(Color.WHITE);
                        showPlayingSong.add(Artist, BorderLayout.CENTER);
                        JLabel Title = new JLabel(id3v2.getTitle());
                        Title.setForeground(Color.WHITE);
                        showPlayingSong.add(Title, BorderLayout.PAGE_END);
                        JLabel album = new JLabel(id3v2.getAlbum());
                        album.setForeground(Color.WHITE);
                        showPlayingSong.add(album, BorderLayout.NORTH);
                        showPlayingSong.setBackground(Color.DARK_GRAY);
                        FileInputStream in= null;
                        try {
                            in = new FileInputStream(temp);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }

                        PausablePlayer player= null;
                        try {
                            player = new PausablePlayer(in);
                        } catch (JavaLayerException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            if (playedSongs.size()!=0){
                                playedSongs.get(playedSongs.size()-1).close();
                            }
                            player.play();
                            playedSongs.add(player);
                        } catch (JavaLayerException e1) {
                            e1.printStackTrace();
                        }
                        PlayMusic playMusic = new PlayMusic(temp,player);
                        DownPanel.addPlayingSongInfo(showPlayingSong);
                        // playMusic.playThread.start();


                    }
                });

                byte[] imageData = id3v2.getAlbumImage();
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
                ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                songImage.setIcon(imageIcon);
                String mimeType = id3v2.getAlbumImageMimeType();
                System.out.println(mimeType);
                Title.setText(id3v2.getTitle());

            }


            JPanel songData = new JPanel();
            songData.setBackground(Color.black);
            songData.setLayout(new BoxLayout(songData, BoxLayout.Y_AXIS));
            songData.add(songImage);
            songData.add(Title);
            songData.setMinimumSize(new Dimension(200, 200));
            songsPanel.add(songData);


        }

        return songsPanel;
        // return test;

    }


}

