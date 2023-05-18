import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {

    JFrame homeScreen;
    JButton btn_update;
    JButton btn_record;

    public void invokeUpdateTable(){
        UpdateTable updatetable = new UpdateTable(this);
    }

    public void invokeSeeRecords(){
        SeeRecords seeRecords = new SeeRecords(this);
    }

    public Home(){

        homeScreen = new JFrame("Database Application");

        btn_update = new JButton("Update Database");
        btn_update.setBounds(50,100,160,80);
        homeScreen.add(btn_update);

        btn_record = new JButton("See Records");
        btn_record.setBounds(260,100,160,80);
        homeScreen.add(btn_record);

        homeScreen.setSize(500,320);
        homeScreen.setLayout(null);
        homeScreen.setVisible(true);

        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invokeUpdateTable();
//                homeScreen.setVisible(false);
            }
        });

        btn_record.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invokeSeeRecords();
            }
        });

        homeScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    //JTable jTable = new JTable();
    //jTable.

}
