/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minitest2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.json.*;

/**
 *
 * @author user
 */
public class MiniTest2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // set var
        String url = "https://www.metaweather.com/api/location/";
        String p0 = "";
        // check param
        try {
            p0 = args[0].toLowerCase();
            // check first param as recognized command
            switch(p0) {
                case "search":
                    // check second param
                    try {
                        String p1 = args[1].toLowerCase();
                        // check second param as recognized command
                        if(!p1.equals("city") && !p1.equals("coordinates")){
                            System.out.println("Unknown second parameter for '"+p0+"'!");
                            System.exit(0);
                        }
                        // check third param
                        try {
                            String p2 = args[2].toLowerCase();
                            switch(p1) {
                                case "city":
                                    url = url.concat("search/?query="+p2);
                                    break;
                                case "coordinates":
                                    url = url.concat("search/?lattlong="+p2);
                                    break;
                                default:
                                    System.exit(0);
                            }
                        } catch(Exception e){
                            System.out.println("Third parameter must be filled!");
                            System.exit(0);
                        }
                    } catch(Exception e){
                        System.out.println("Second parameter must be filled!");
                        System.exit(0);
                    }
                    
                    break;
                case "save":
                    // check second param
                    try {
                        String p1 = args[1].toLowerCase();
                        url = url.concat("search/?query="+p1);
                    } catch(Exception e){
                        System.out.println("Second parameter must be filled!");
                        System.exit(0);
                    }
                    break;
                case "show":
                    // check second param
                    try {
                        String p1 = args[1].toLowerCase();
                        // check second param as recognized command
                        if(!p1.equals("all")){
                            System.out.println("Unknown second parameter for '"+p0+"'!");
                            System.exit(0);
                        }
                    } catch(Exception e){
                        System.out.println("Second parameter must be filled!");
                        System.exit(0);
                    }
                    break;
                case "remove":
                    break;
                default:
                    System.out.println("Unknown first parameter!");
                    System.exit(0);
            }
        } catch(Exception e){
            System.out.println("First parameter must be filled!");
            System.exit(0);
        }
        // get data
        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        if(p0.equals("show") || p0.equals("remove")) {
            // set connection sqlite db
            Connection conn = null;
            try {
                // set connection
                String dburl = "jdbc:sqlite:database.sqlite";
                conn = DriverManager.getConnection(dburl);
                // get query
                Statement stmt = conn.createStatement();
                String qcity = "select * from city";
                ResultSet rset = stmt.executeQuery(qcity);
                switch(p0) {
                    case "show":
                        while(rset.next()) {
                            String city = rset.getString("city");
                            String location = rset.getString("location");
                            System.out.println("Weather for city " + city);
                            // get api data
                            try {
                                String jsonData = "";
                                URL wurl = new URL(url.concat(location));
                                URLConnection uc = wurl.openConnection();
                                InputStream in = uc.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                                for (String line; (line = reader.readLine()) != null;) {
                                    jsonData += line + "\n";
                                }
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(jsonData);
                                    JSONArray dataList = (JSONArray) jsonObject.get("consolidated_weather");
                                    String format = "%-10s%s%n";
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Date", ":"+dataList.getJSONObject(0).get("applicable_date"), dataList.getJSONObject(1).get("applicable_date"), dataList.getJSONObject(2).get("applicable_date"), dataList.getJSONObject(3).get("applicable_date"), dataList.getJSONObject(4).get("applicable_date"), dataList.getJSONObject(5).get("applicable_date"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Weather", ":"+dataList.getJSONObject(0).get("weather_state_name"), dataList.getJSONObject(1).get("weather_state_name"), dataList.getJSONObject(2).get("weather_state_name"), dataList.getJSONObject(3).get("weather_state_name"), dataList.getJSONObject(4).get("weather_state_name"), dataList.getJSONObject(5).get("weather_state_name"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Min. Temp", ":"+dataList.getJSONObject(0).get("min_temp"), dataList.getJSONObject(1).get("min_temp"), dataList.getJSONObject(2).get("min_temp"), dataList.getJSONObject(3).get("min_temp"), dataList.getJSONObject(4).get("min_temp"), dataList.getJSONObject(5).get("min_temp"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Max. Temp", ":"+dataList.getJSONObject(0).get("max_temp"), dataList.getJSONObject(1).get("max_temp"), dataList.getJSONObject(2).get("max_temp"), dataList.getJSONObject(3).get("max_temp"), dataList.getJSONObject(4).get("max_temp"), dataList.getJSONObject(5).get("max_temp"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Temp", ":"+dataList.getJSONObject(0).get("the_temp"), dataList.getJSONObject(1).get("the_temp"), dataList.getJSONObject(2).get("the_temp"), dataList.getJSONObject(3).get("the_temp"), dataList.getJSONObject(4).get("the_temp"), dataList.getJSONObject(5).get("the_temp"));
                                    System.out.println();
                                } catch (JSONException ex) {
                                    Logger.getLogger(MiniTest2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                                System.exit(0);
                            }
                        }
                        break;
                    case "remove":
                        List<String> cityList = new ArrayList<String>();
                        while(rset.next()) {
                            cityList.add(rset.getString("city"));
                            System.out.println(rset.getRow()+". "+rset.getString("city"));
                        }
                        System.out.print("Which one data that you want to remove (in number, ex: 1)? "); 
                        Integer inputter = Integer.parseInt(ob.readLine());
                        String city = "";
                        for (int i = 0; i < cityList.size(); i++) {
                            if(inputter == i+1){
                                city = cityList.get(i);
                            }
                        }
                        if(!city.isEmpty()){
                            try {
                                stmt.executeUpdate("delete from city where city ='"+ city +"'");
                                System.out.println("City with name "+ city +" has been removed");
                            }catch(SQLException e){
                                System.out.println(e.getMessage());
                            }
                        }else{
                            System.out.println("No data!");
                        }
                        System.exit(0);
                        break;
                }
                rset.close();
                stmt.close();
                conn.close();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }else{
            String jsonData = "";
            URL wurl = new URL(url);
            URLConnection uc = wurl.openConnection();
            InputStream in = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            if(!reader.ready()){
                System.out.println("No data!");
                System.exit(0);
            }
            for (String line; (line = reader.readLine()) != null;) {
                jsonData += line + "\n";
            }
            JSONArray arr;
            switch(p0) {
                case "search":
                    try {
                        arr = new JSONArray(jsonData);
                        if(arr.length() < 1){
                            System.out.println("No data!");
                            System.exit(0);
                        }
                        List<String> woeidList = new ArrayList<String>();
                        for(int i=0; i<arr.length(); i++){ 
                            JSONObject obj = arr.getJSONObject(i);
                            woeidList.add(obj.get("woeid").toString());
                            System.out.println(i+1+". "+ obj.getString("title"));
                        }
                        System.out.print("Which one that you want to see (in number, ex: 1)? "); 
                        Integer inputter = Integer.parseInt(ob.readLine());
                        String woeid = "";
                        for (int i = 0; i < woeidList.size(); i++) {
                            if(inputter == i+1){
                                woeid = woeidList.get(i);
                            }
                        }
                        if(!woeid.isEmpty()){
                            // get api data
                            try {
                                jsonData = "";
                                URL searchUrl = new URL("https://www.metaweather.com/api/location/"+woeid);
                                URLConnection searchUc = searchUrl.openConnection();
                                InputStream searchIs = searchUc.getInputStream();
                                BufferedReader searchReader = new BufferedReader(new InputStreamReader(searchIs, "UTF-8"));
                                if(!searchReader.ready()){
                                    System.out.println("No data!");
                                    System.exit(0);
                                }
                                for (String line; (line = searchReader.readLine()) != null;) {
                                    jsonData += line + "\n";
                                }
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(jsonData);
                                    JSONArray dataList = (JSONArray) jsonObject.get("consolidated_weather");
                                    System.out.println("Weather for city " + jsonObject.get("title"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Date", ":"+dataList.getJSONObject(0).get("applicable_date"), dataList.getJSONObject(1).get("applicable_date"), dataList.getJSONObject(2).get("applicable_date"), dataList.getJSONObject(3).get("applicable_date"), dataList.getJSONObject(4).get("applicable_date"), dataList.getJSONObject(5).get("applicable_date"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Weather", ":"+dataList.getJSONObject(0).get("weather_state_name"), dataList.getJSONObject(1).get("weather_state_name"), dataList.getJSONObject(2).get("weather_state_name"), dataList.getJSONObject(3).get("weather_state_name"), dataList.getJSONObject(4).get("weather_state_name"), dataList.getJSONObject(5).get("weather_state_name"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Min. Temp", ":"+dataList.getJSONObject(0).get("min_temp"), dataList.getJSONObject(1).get("min_temp"), dataList.getJSONObject(2).get("min_temp"), dataList.getJSONObject(3).get("min_temp"), dataList.getJSONObject(4).get("min_temp"), dataList.getJSONObject(5).get("min_temp"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Max. Temp", ":"+dataList.getJSONObject(0).get("max_temp"), dataList.getJSONObject(1).get("max_temp"), dataList.getJSONObject(2).get("max_temp"), dataList.getJSONObject(3).get("max_temp"), dataList.getJSONObject(4).get("max_temp"), dataList.getJSONObject(5).get("max_temp"));
                                    System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %s%n","Temp", ":"+dataList.getJSONObject(0).get("the_temp"), dataList.getJSONObject(1).get("the_temp"), dataList.getJSONObject(2).get("the_temp"), dataList.getJSONObject(3).get("the_temp"), dataList.getJSONObject(4).get("the_temp"), dataList.getJSONObject(5).get("the_temp"));
                                    System.out.println();
                                } catch (JSONException ex) {
                                    Logger.getLogger(MiniTest2.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                                System.exit(0);
                            }
                        }else{
                            System.out.println("No data!");
                        }    
                    } catch (JSONException ex) {
                        Logger.getLogger(MiniTest2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                    break;
                case "save":
                    try {
                        arr = new JSONArray(jsonData);
                        if(arr.length() < 1){
                            System.out.println("No data!");
                            System.exit(0);
                        }
                        String[][] datas = new String[arr.length()][2];
                        for(int i=0; i<arr.length(); i++){ 
                            JSONObject obj = arr.getJSONObject(i);
                            datas[i][0] = obj.getString("title");
                            datas[i][1] = obj.get("woeid").toString();
                            System.out.println(i+1+". "+ obj.getString("title"));
                        }
                        System.out.print("Which one data that you want to save (in number, ex: 1)? "); 
                        Integer inputter = Integer.parseInt(ob.readLine());
                        String title = "";
                        String woeid = "";
                        for(int i=0; i<datas.length; i++){
                            if(inputter == i+1){
                                title = datas[i][0];
                                woeid = datas[i][1];
                            }
                        }
                        if(!title.isEmpty()){
                            // set connection
                            String dburl = "jdbc:sqlite:database.sqlite";
                            Connection conn = null;
                            try {
                                conn = DriverManager.getConnection(dburl);
                                Statement stmt = conn.createStatement();
                                ResultSet rset = stmt.executeQuery("select * from city where location ="+ woeid);
                                Integer rowCount = 0;
                                while(rset.next()){
                                    rowCount++;
                                }
                                if(rowCount != 0){
                                    System.out.println("Data not save, city "+ title +" has been exist!");
                                    System.exit(0);
                                }
                                rset.close();
                                stmt.executeUpdate("insert into city values ('"+ title +"',Null,Null,"+ woeid +")");
                                System.out.println("City with name "+ title +" has been saved");
                            }catch(SQLException e){
                                System.out.println(e.getMessage());
                                System.exit(0);
                            }finally {
                                try {
                                    if (conn != null) {
                                        conn.close();
                                    }
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }else{
                            System.out.println("No data!");
                        }   
                    } catch (JSONException ex) {
                        Logger.getLogger(MiniTest2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                    break;
            } 
        }
    }
    
}
