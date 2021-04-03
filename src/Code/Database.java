/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Code;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dipto
 */
public class Database {
    private String databasePath="jdbc:mysql://localhost:3306/bugreport";
    private String username = "root";
    private String password = "";
    public DefaultTableModel model = new DefaultTableModel();
    
      private Connection connect() {
        // SQLite connection string
        Connection conn = null;
       // Class.forName(com.mysql.jdbc.Driver);
        try {
          
            conn = DriverManager.getConnection(databasePath,username,password);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void insert(int pid,int bid, String bname, String description, String attachment, String status, String dateTime,String t_name) {
        String sql = "INSERT INTO dashboard(pid,bid,bname,description,photo,status,date,pushBy,path) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
                
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("2");
            pstmt.setInt(1, pid);
            pstmt.setInt(2, bid);
            pstmt.setString(3, bname);
            pstmt.setString(4, description);
            if(attachment.isEmpty()){
                pstmt.setNull(5, java.sql.Types.BLOB);
            }else{
                 InputStream in = new FileInputStream(attachment);
                 pstmt.setBlob(5, in);
            }

            pstmt.setString(6, status);
            pstmt.setString(7, dateTime);
            pstmt.setString(8, t_name);
            pstmt.setString(9, attachment);
            
            pstmt.executeUpdate();
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            
        }
    }
    
    public void popData(DefaultTableModel model1){
        String sql ="select * from dashboard";
         try{
             Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             
             while(rs.next()){
                 int pid = rs.getInt("pid");
                 int bid = rs.getInt("bid");
                 String bname = rs.getString("bname");
                 String des = rs.getString("description");
                 //photo here
                 //byte[] img  = rs.getBytes("photo");
                 String status = rs.getString("status");
                 String date = rs.getString("date");
                 String pushBy = rs.getString("pushBy");
                 String path = rs.getString("path");
               
                 model1.addRow(new Object[]{pid,bid,bname,des,"view",status,date,pushBy,path});
                
             }
    
         }catch(Exception e){
             System.out.println(e);

         }
       
    }

    public void updateData(int pid,int bid, String bname, String description, String attachment, String status, String dateTime,String t_name){
         String sql ="UPDATE dashboard SET pid= ? ,bname=?,description=?,photo=?,status=?,date=?,pushBy=?  WHERE pid=? and bid=?;";
         try{
             Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pid);
           // pstmt.setInt(2, bid);
            pstmt.setString(2, bname);
            pstmt.setString(3, description);
            if(attachment.isEmpty()){
                pstmt.setNull(4, java.sql.Types.BLOB);
            }else{
                 InputStream in = new FileInputStream(attachment);
                 pstmt.setBlob(4, in);
            }

            pstmt.setString(5, status);
            pstmt.setString(6, dateTime);
            pstmt.setString(7, t_name);
            
            pstmt.setInt(8, pid);
            pstmt.setInt(9, bid);
            
            
            pstmt.executeUpdate();
         
         }catch(Exception e){
             System.out.println(e);

         }

    }
    public void deleteData(int pid,int bid){
        String sql = "DELETE FROM dashboard WHERE pid =? and  bid=? ;";
        try{
             Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql);

          pstmt.setInt(1, pid);
          pstmt.setInt(2, bid);
          pstmt.executeUpdate();       
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public int getBid(int pid){
        String sql = "SELECT MAX(bid) as m from dashboard Where pid = "+pid+" ;";
        try{
            Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             
             if(rs.next()){
                 return rs.getInt("m");
             }
             
        }catch(Exception e){
            System.out.println(e);
            return -1;
        }
        return -1;
    }
      public int getCount(){
        String sql = "SELECT * from count ;";
        try{
            Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             
             if(rs.next()){
                 return rs.getInt("new");
             }
             
        }catch(Exception e){
            System.out.println(e);
            return -1;
        }
        return -1;
    }
    public void pushCount(int _new){

        String sql = "Update count set new ="+ _new+";";
        try{
            Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             stmt.executeUpdate(sql);

        }catch(Exception e){
            System.out.println(e);
            
        }
        
    }
        
}
    
    
 
