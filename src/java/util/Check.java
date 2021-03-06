package util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Check {

    public boolean movieExists(String nameMovie) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT name FROM tvguide.movie WHERE name='" + nameMovie + "'";
        ResultSet rs = instr.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString(1);
            if (nameMovie.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean userExists(String user) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT username FROM tvguide.user WHERE username='" + user + "'";
        ResultSet rs = instr.executeQuery(sql);
        while (rs.next()) {
            String username = rs.getString(1);
            if (user.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean userExists2(String user, String pass) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        pass = getHash(pass);
        String sql = "SELECT username,password FROM tvguide.user WHERE username='" + user + "' AND password='" + pass + "'";
        ResultSet rs = instr.executeQuery(sql);
        return rs.next();
    }

    public boolean isAdmin(String user) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT username FROM tvguide.user WHERE username='" + user + "' AND administrator='" + 1 + "'";
        ResultSet rs = instr.executeQuery(sql);
        while (rs.next()) {
            String username = rs.getString(1);
            if (user.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static String getHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        digest.reset();
        try {
            digest.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return new BigInteger(1, digest.digest()).toString(16);
    }

    public void setGenres(String genres, String user) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE tvguide.user SET genre = '" + genres + "' WHERE username='" + user + "'";
        PreparedStatement preparedStmt = con.prepareStatement(sql);
        preparedStmt.executeUpdate();
    }
    
    public void setNotifications(String movies, String user) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "UPDATE tvguide.user SET movies = '" + movies + "' WHERE username='" + user + "'";
        PreparedStatement preparedStmt = con.prepareStatement(sql);
        preparedStmt.executeUpdate();
    }
    public String getDate(String movie) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT dates FROM tvguide.movie WHERE name='" + movie + "'";
        ResultSet rs = instr.executeQuery(sql);
        return  rs.next() ?  rs.getString(1).toString() : "" ;
        
    }
    public String getHour(String movie) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT hours FROM tvguide.movie WHERE name='" + movie + "'";
        ResultSet rs = instr.executeQuery(sql);
        return  rs.next() ?  rs.getString(1).toString() : "" ;
        
    }
    public String getChannel(String movie) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT channel FROM tvguide.movie WHERE name='" + movie + "'";
        ResultSet rs = instr.executeQuery(sql);
        return  rs.next() ?  rs.getString(1).toString() : "" ;
        
    }
    public String getUserMovies(String username) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getConnection();
        java.sql.Statement instr = con.createStatement();
        String sql = "SELECT movies FROM tvguide.user WHERE username='" + username + "'";
        ResultSet rs = instr.executeQuery(sql);
        return  rs.next() || rs !=null ?  rs.getString(1).toString() : "" ;
  
    }
    
    
    public void addReview(String review, String movie) throws SQLException{
        Connection con = DBConnection.getConnection();
        String sql="SELECT review FROM tvguide.movie WHERE name='"+movie+"'";
        String update;
        ResultSet rs = con.createStatement().executeQuery(sql);
        rs.next();
        if(rs.getString("review")!=null){
                String[] reviews=rs.getString(1).split("\"");
                update = "UPDATE tvguide.movie SET review='\""+review+"\", \""+reviews[1]+"\"' WHERE name='"+movie+"'";  
            } 
        else update = "UPDATE tvguide.movie SET review='\""+review+"\"' WHERE name='"+movie+"'";   
        PreparedStatement preparedStmt = con.prepareStatement(update);
        preparedStmt.executeUpdate();
    }
}
