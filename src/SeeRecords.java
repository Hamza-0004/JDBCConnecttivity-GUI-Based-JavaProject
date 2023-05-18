import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SeeRecords {

    public JFrame showRecordsScreen;
    DBHandlerJDBC dbHandlerJDBC;
    Home objHome;

    DefaultTableModel tableModel;

    JTable table;
    public SeeRecords(Home objHome){
        this.objHome = objHome;
        dbHandlerJDBC = new DBHandlerJDBC();

        showRecordsScreen = new JFrame("See Records by writing Query");

        showRecordsScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        showRecordsScreen.setSize(1100,500);
        showRecordsScreen.setLayout(null);

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

        comboBox.setBounds(170,50,180,30);

        showRecordsScreen.add(comboBox);

        // ---------------- ComboBox Work ---------------

        JLabel lblSelectDatabase = new JLabel("Select Database");
        lblSelectDatabase.setBounds(50,50,100 , 30);
        showRecordsScreen.add(lblSelectDatabase);

        JLabel lblWriteQuery = new JLabel("Write SQL Query");
        lblWriteQuery.setBounds(50,100,100 , 30);
        showRecordsScreen.add(lblWriteQuery);


        JTextField txtQuery = new JTextField();
        txtQuery.setBounds(170,100, 600, 30);
        showRecordsScreen.add(txtQuery);

        JButton btn_run = new JButton("Run Query");
        btn_run.setBounds(780,100,100,30);
        showRecordsScreen.add(btn_run);

        btn_run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runQuery(txtQuery.getText());
            }
        });

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        showRecordsScreen.setVisible(true);

    }

    public void runQuery(String query){



        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear all rows in the model
        model.setColumnCount(0); // Clear all columns in the model

        DBHandlerJDBC.QueryResult queryResult = dbHandlerJDBC.getQueryResult(query);



        if (queryResult.getExceptionMessage() == null) {
            System.out.println(queryResult.getRecords());
            System.out.println("-------- In Contstructor of SeeRecords -------------------");

            tableModel.setColumnIdentifiers(queryResult.getColumnNames().toArray(new String[0]));

            // Populate the table model with the records
            for (List<String> record : queryResult.getRecords()) {
                tableModel.addRow(record.toArray());
            }

            // Create the table with the table model
            table = new JTable(tableModel);

            JScrollPane scrollPane = new JScrollPane(table);

            scrollPane.setBounds(40,150,1000,300);

            showRecordsScreen.add(scrollPane);

            showRecordsScreen.setVisible(true);
        }
        else {
            System.out.println(queryResult.getExceptionMessage()+"");
            JOptionPane.showMessageDialog((Component)null, queryResult.getExceptionMessage());
        }

    }

}
