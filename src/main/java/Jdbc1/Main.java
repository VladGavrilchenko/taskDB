package Jdbc1;


import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "fakasky1234";


    static Connection conn;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            try {
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add apartmen");
                    System.out.println("2: add random apartments");
                    System.out.println("3: view apartment");
                    System.out.println("4: delete apartment");
                    System.out.println("5: change apartment");
                    System.out.println("6: change apartment by price:");
                    System.out.println("7: change apartment by number of rooms:");
                    System.out.println("8: change apartment by all parameters:");
                    System.out.println("9: exit");



                    System.out.print("->");
                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartment(sc);
                            break;
                        case "2":
                            insertRandomApartament(sc);
                            break;
                        case "3":
                            viewApartament();
                            break;
                        case "4":
                            deleteApartament(sc);
                            break;
                        case "5":
                            changeApartament(sc);
                            break;
                        case "6":
                            changeApartamentByPrice(sc);
                            break;

                        case "7":
                            choiceApartmentByNumberOfRooms(sc);
                            break;
                        case "8":
                            choiceApartmentByAllParameters(sc);
                            break;
                        case "9":
                            return;
                        default:
                            System.out.println("Wrong choice");
                            break;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private  static  void initDB() throws SQLException
    {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE  IF EXISTS  Apartament");
            st.execute("CREATE TABLE Apartament (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20) " + "NOT NULL, address  INT NOT NULL"
                    +" ,  square INT NOT NULL , numberOfRooms INT NOT NULL"
                    + ", price INT NOT NULL)");
        }finally {
            st.close();
        }
    }

    private  static  void  addApartment( Scanner sc ) throws  SQLException {
        System.out.println("Enter district:");
        String district = sc.nextLine();
        System.out.println("Enter address:");
        String sAddress = sc.nextLine();
        System.out.println("Enter square:");
        String sSquare = sc.nextLine();
        System.out.println("Enter number of rooms:");
        String sNumberOfRooms = sc.nextLine();
        System.out.println("Enter price");
        String sSprice = sc.nextLine();

        int address =Integer.parseInt(sAddress);
        int square = Integer.parseInt(sSquare);
        int numberOfRooms = Integer.parseInt(sNumberOfRooms);
        int price = Integer.parseInt(sSprice);

        PreparedStatement ps = conn.prepareStatement(
                "INSERT  INTO Apartament(district ,address , square ,numberOfRooms ,  price ) " +
                        "VALUE (? , ? , ?, ?, ?)");

        try {
            ps.setString(1 ,district );
            ps.setInt(2 , address);
            ps.setInt(3 , square);
            ps.setInt( 4 , numberOfRooms);
            ps.setInt(5 , price);
            ps.executeUpdate();

        }finally {
            ps.close();
        }


    }


    private static  void deleteApartament(Scanner sc) throws SQLException {
        System.out.println("Enter district:");
        String district = sc.nextLine();
        System.out.println("Enter address:");
        String sAddress = sc.nextLine();
        System.out.println("Enter square:");
        String sSquare = sc.nextLine();
        System.out.println("Enter number of rooms:");
        String sNumberOfRooms = sc.nextLine();

        int address =Integer.parseInt(sAddress);
        int square = Integer.parseInt(sSquare);
        int numberOfRooms = Integer.parseInt(sNumberOfRooms);

        PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM  Apartament WHERE district = ? AND   address= ? " +
                        "AND   square =? AND  numberOfRooms =?"  );
        try {
            ps.setString(1, district);
            ps.setInt(2 , address);
            ps.setInt(3 , square);
            ps.setInt( 4 , numberOfRooms);
            ps.executeUpdate();
        }
        finally {
            ps.close();
        }
    }

    private static  void changeApartament(Scanner sc) throws  SQLException{
        System.out.println("Enter district:");
        String district = sc.nextLine();
        System.out.println("Enter address:");
        String sAddress = sc.nextLine();
        System.out.println("Enter number of rooms:");
        String sNumberOfRooms = sc.nextLine();


        int address =Integer.parseInt(sAddress);
        int numberOfRooms = Integer.parseInt(sNumberOfRooms);


        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM Apartament   WHERE  district = ? AND address= ?  AND  numberOfRooms =? ");

        try {


            ps.setString(1 ,district );
            ps.setInt(2 , address);
            ps.setInt( 3 , numberOfRooms);


            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i =1 ; i <= md.getColumnCount() ; i++)
                    System.out.print(md.getColumnName(i) + "\t");
                System.out.println();

                while (rs.next()){
                    for (int i=1 ; i <= md.getColumnCount(); i++)
                        System.out.print(rs.getString(i) + "\t");
                    System.out.println();
                }

            }finally {
                rs.close();
            }
        }finally {
            ps.close();
        }

    }

    private static  void changeApartamentByPrice(Scanner sc) throws  SQLException{
        System.out.println("Enter minimum price:");
        String  sMinPrice= sc.nextLine();
        System.out.println("Enter max price");
        String sMaxPrice = sc.nextLine();

        int minPrice = Integer.parseInt(sMinPrice);
        int maxPrice = Integer.parseInt(sMaxPrice);

        if (minPrice > maxPrice)
            return;


        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM Apartament WHERE price >=   ?   AND  price <= ?  ");

        try {

          ps.setInt(1 , minPrice);
          ps.setInt(2 , maxPrice);

            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i =1 ; i <= md.getColumnCount() ; i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()){
                    for (int i=1 ; i <= md.getColumnCount(); i++)
                        System.out.print(rs.getString(i) + "\t\t");
                    System.out.println();
                }

            }finally {
                rs.close();
            }
        }finally {
            ps.close();
        }

    }


    private  static void viewApartament() throws  SQLException{
        PreparedStatement ps = conn.prepareStatement( "SELECT * FROM  Apartament");

        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i =1 ; i <= md.getColumnCount() ; i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()){
                    for (int i=1 ; i <= md.getColumnCount(); i++)
                                            System.out.print(rs.getString(i) + "\t\t");
                    System.out.println();
                }

            }finally {
                rs.close();
            }
        }finally {
            ps.close();
        }
    }


    private  static void choiceApartmentByNumberOfRooms(Scanner sc) throws  SQLException{
        System.out.println("Enter number of rooms:");
        String  sNumberOfRooms = sc.nextLine();


        int numberOfRooms = Integer.parseInt(sNumberOfRooms);

        PreparedStatement ps = conn.prepareStatement( "SELECT * FROM  Apartament WHERE numberOfRooms =?");

        try {
            ps.setInt(1 , numberOfRooms );
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i =1 ; i <= md.getColumnCount() ; i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()){
                    for (int i=1 ; i <= md.getColumnCount(); i++)
                        System.out.print(rs.getString(i) + "\t\t");
                    System.out.println();
                }

            }finally {
                rs.close();
            }
        }finally {
            ps.close();
        }
    }


    private static void choiceApartmentByAllParameters(Scanner sc) throws  SQLException{

        System.out.println("Enter district:");
        String district = sc.nextLine();
        System.out.println("Enter address:");
        String sAddress = sc.nextLine();
        System.out.println("Enter square:");
        String sSquare = sc.nextLine();
        System.out.println("Enter number of rooms:");
        String sNumberOfRooms = sc.nextLine();
        System.out.println("Enter price:");
        String sPrice = sc.nextLine();

        int address = Integer.parseInt(sAddress);
        int numberOfRooms = Integer.parseInt(sNumberOfRooms);
        int square = Integer.parseInt(sSquare);
        int price = Integer.parseInt(sPrice);



        PreparedStatement ps = conn.prepareStatement( "SELECT * FROM Apartament WHERE  district = ? AND address = ? " +
                " AND numberOfRooms= ? AND square = ? AND price =? ");



        try {

            ps.setString(1 ,district );
            ps.setInt(2 ,address);
            ps.setInt(3 , numberOfRooms);
            ps.setInt(4 , square);
            ps.setInt(5,price );
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i =1 ; i <= md.getColumnCount() ; i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()){
                    for (int i=1 ; i <= md.getColumnCount(); i++)
                        System.out.print(rs.getString(i) + "\t\t");
                    System.out.println();
                }

            }finally {
                rs.close();
            }
        }finally {
            ps.close();
        }
    }

    private static void insertRandomApartament(Scanner sc) throws  SQLException{
        System.out.print("Enter apartament count: ");
        String sCount = sc.nextLine();
        int cout = Integer.parseInt(sCount);
        Random rnd = new Random();

        conn.setAutoCommit(false);
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO  Apartament(district ,address , square , numberOfRooms , price ) VALUE (? , ? , ? , ? ,?)");
                try {
                    for (int i =0 ; i < cout; i++) {
                        ps.setString(1 , "district" + i);
                        ps.setInt(2 , rnd.nextInt(30));
                        ps.setInt(3 , 60 + rnd.nextInt(200));
                        ps.setInt(4 , 1 + rnd.nextInt(2));
                        ps.setInt(5 , 400000 + rnd.nextInt(500000));
                        ps.executeUpdate();
                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            }catch (Exception ex) {
                conn.rollback();
            }

        } finally {
            conn.setAutoCommit(true);
        }
    }
}




