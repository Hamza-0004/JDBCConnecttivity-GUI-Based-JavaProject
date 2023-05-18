import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DBHandlerJDBC{

    String url,username,password;
    String query;

    public DBHandlerJDBC(){
        url = "jdbc:mysql://localhost:3306/hr";
        username = "root";
        password = "Humz@1o1";
        query = "SELECT * FROM employee";
    }

    public class QueryResult {
        private List<String> columnNames;
        private int numColumns;
        private List<List<String>> records;
        private String exceptionMessage;

        public List<String> getColumnNames() {
            return columnNames;
        }

        public void setColumnNames(List<String> columnNames) {
            this.columnNames = columnNames;
        }

        public int getNumColumns() {
            return numColumns;
        }

        public void setNumColumns(int numColumns) {
            this.numColumns = numColumns;
        }

        public List<List<String>> getRecords() {
            return records;
        }

        public void setRecords(List<List<String>> records) {
            this.records = records;
        }

        public String getExceptionMessage() {
            return exceptionMessage;
        }

        public void setExceptionMessage(String exceptionMessage) {
            this.exceptionMessage = exceptionMessage;
        }
    }


    public List<String> getDatabases(){

        QueryResult queryResult;

        final QueryResult[] tempqueryResult = new QueryResult[1];

        CountDownLatch latch = new CountDownLatch(1);
        // Call the query runner function in a separate thread
        Thread queryThread = new Thread(() -> {
            // Run your query and obtain the QueryResult object
            tempqueryResult[0] = runQuery("SHOW DATABASES");

            // Process the result as needed

            // Release the latch to signal that the query is complete
            latch.countDown();
        });
        queryThread.start();

        try {
            // Wait for the latch countdown to reach 0
            latch.await();
            queryResult = tempqueryResult[0];
            List<String> databases = new ArrayList<>();

            for (List<String> record : queryResult.records) {
                for (String value : record) {
                    databases.add(value);
                }
            }
            System.out.println(databases);
            return databases;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void selectDatabase(String dbName){

        url = "jdbc:mysql://localhost:3306/"+dbName;
        System.out.println(dbName+" Database is Selected");
    }

    public int modifyDatabase(String query){


        return 0;
    }

    public QueryResult getQueryResult(String query){

        QueryResult queryResult = runQuery(query);

        return queryResult;
    }

    public QueryResult runQuery(String query){

        QueryResult queryResult = new QueryResult();

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url,username,password);

            Statement statement = connection.createStatement();

            ResultSet resultset = statement.executeQuery(query);

            ResultSetMetaData metaData = resultset.getMetaData();

            // --------------------- Finding no. of Columns --------------------------------------------------
            int columnCount = metaData.getColumnCount();
            queryResult.setNumColumns(columnCount);

            // --------------------- Populating column Names -------------------------------------------------
            ArrayList<String> columnNames = new ArrayList<>();  // setting ColumnNumber in queryResult Class

            for(int i=1; i<=columnCount; i++){
                columnNames.add(metaData.getColumnName(i));
            }
            queryResult.setColumnNames(columnNames);  // setting ColumnNames in queryResult Class

            // ---------------- Population records ------------------------------------------------------------
            List<List<String>> records = new ArrayList<>();

            while(resultset.next()){
                List<String> record = new ArrayList<>();
                for(int i=1; i<=columnCount; i++){
                    record.add(resultset.getString(i));
                }
                records.add(record);
            }
            queryResult.setRecords(records);

            connection.close();
            return queryResult;
        }

        catch(Exception e){
            queryResult.setExceptionMessage(e.toString());
        }
        return queryResult;
    }

}