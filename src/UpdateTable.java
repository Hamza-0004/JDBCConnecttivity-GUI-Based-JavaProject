import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class UpdateTable extends JFrame{
    public JFrame updateScreen;
    DBHandlerJDBC dbHandlerJDBC;
    Home objHome;


    public UpdateTable(Home objHome) {

        this.objHome = objHome;

        dbHandlerJDBC = new DBHandlerJDBC();

        updateScreen =  new JFrame("Update Database");
        updateScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        updateScreen.setSize(1000,500);
        updateScreen.setLayout(null);

        //JOptionPane.showMessageDialog((Component)null, "You will get your food in 10min Thankyou!");

        // ---------------- ComboBox Work -----------------------------------------------------------------
        List<String> databases = dbHandlerJDBC.getDatabases();

        JComboBox<String> comboBox = new JComboBox<>(databases.toArray(new String[0]));

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<?> source = (JComboBox<?>) e.getSource();
                String selectedOption = (String) source.getSelectedItem();
                dbHandlerJDBC.selectDatabase(selectedOption);
                System.out.println("Selected option (ActionListener): " + selectedOption);

                DBHandlerJDBC.QueryResult queryResult = dbHandlerJDBC.getQueryResult("SELECT * FROM film");
            }
        });

        comboBox.setBounds(50,50,150,50);

        updateScreen.add(comboBox);

        // ---------------- ComboBox Work ---------------

        updateScreen.setVisible(true);

    }


}
