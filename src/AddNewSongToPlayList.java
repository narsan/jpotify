import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

public class AddNewSongToPlayList {
    public JButton getAddNewSong() {
        return addNewSong;
    }

    private JButton addNewSong = new JButton();
    private PlayList playList;

    public PlayList getPlayList() {
        return playList;
    }

    public AddNewSongToPlayList(PlayList playList) {

        this.playList = playList;

        ArrayList<String> songsName=new ArrayList<>();


        addNewSong.setText("Add new Song..");
        addNewSong.setBorder(null);
        addNewSong.setBackground(Color.black);
        addNewSong.setFont(new Font("Arial", Font.PLAIN, 13));
        addNewSong.setForeground(Color.WHITE);



        addNewSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i <Library.getSongs().size() ; i++) {

                    Mp3File mp3File;

                    try {
                        mp3File = new Mp3File(Library.getSongs().get(i));
                        if (mp3File.hasId3v1Tag()&&mp3File.getId3v1Tag().getTitle()!=null){
                        songsName.add(mp3File.getId3v1Tag().getTitle());}
                        else if (mp3File.hasId3v2Tag()&&mp3File.getId3v2Tag().getTitle()!=null){

                            songsName.add(mp3File.getId3v2Tag().getTitle());
                        }
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    } catch (UnsupportedTagException e5) {
                        e5.printStackTrace();
                    } catch (InvalidDataException e5) {
                        e5.printStackTrace();
                    }

                }

                JFrame frame=new JFrame();


                String input= (String) JOptionPane.showInputDialog(frame,"choose new song","add new Song",JOptionPane.QUESTION_MESSAGE, null,songsName.toArray(),songsName.get(0));

                for (int i = 0; i <Library.getSongs().size() ; i++) {

                    Mp3File mp3File;

                    try {
                        mp3File = new Mp3File(Library.getSongs().get(i));
                        if (mp3File.hasId3v1Tag()&&mp3File.getId3v1Tag().getTitle()!=null){

                            if (mp3File.getId3v1Tag().getTitle().equals(input)){
                                //System.out.println("here");

                                playList.addSongToPlayList(Library.getSongs().get(i));
                            }


                        }

                        else if (mp3File.hasId3v2Tag()&&mp3File.getId3v2Tag().getTitle()!=null){

                            if (mp3File.getId3v2Tag().getTitle().equals(input)){
                                //System.out.println("here");
                                playList.addSongToPlayList(Library.getSongs().get(i));
                            }


                        }



                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (UnsupportedTagException e2) {
                        e2.printStackTrace();
                    } catch (InvalidDataException e3) {
                        e3.printStackTrace();
                    }

                }

            }
        });






    }
}




