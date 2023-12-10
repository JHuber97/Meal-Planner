package mealplanner;

import java.sql.*;

public class MealPlannerDB {
    private Connection con;
    private String DB_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private String USER = "postgres";
    private String PASS = "1111";

    public MealPlannerDB(){
        try{
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            con.setAutoCommit(true);
            createTables();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void createTables(){
        try{
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE if not exists meals("
                    + "meal_id int,"
                    + "category VARCHAR(30),"
                    + "meal VARCHAR(1024)"
                    + ")");
            statement.executeUpdate("CREATE TABLE if not exists ingredients("
                    + "ingredient_id int,"
                    + "ingredient VARCHAR(1024),"
                    + "meal_id INTEGER"
                    + ")");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addMeal(String category, String nameOfMeal, String ingredients){
        String queryInsertMeals = "INSERT INTO meals (category, meal) VALUES (?, ?)";
        String queryInsertIngredients = "INSERT INTO ingredients (ingredient) VALUES (?)";
        try(Connection con = DriverManager.getConnection(DB_URL, USER, PASS)){
            try(PreparedStatement pStatement1 = con.prepareStatement(queryInsertMeals)){
                pStatement1.setString(1, category);
                pStatement1.setString(2, nameOfMeal);
            }
            try(PreparedStatement pStatement2 = con.prepareStatement(queryInsertIngredients)){
                pStatement2.setString(1, ingredients);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void showMeals() {
        try{
            String queryJoin = "SELECT m.category, m.meal, i.ingredients FROM meals AS m " +
                    "JOIN ingredients AS i" +
                    "WHERE meals.meal_id = ingredients.meal_id";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(queryJoin);
            if(rs.next() == false){
                System.out.println("No meals saved. Add a meal first.");
                Main.menue();
            }
            while(rs.next()){
                String category = rs.getString("category");
                String name = rs.getString("meal");
                String ingredients = rs.getString("ingredient");
                String[] ingr = ingredients.split(",");
                System.out.printf("Category: %s/n", category);
                System.out.printf("Name: %s/n", name);
                System.out.println("Ingredients: ");
                for(String ingredient : ingr){
                    System.out.println(ingredient.trim());
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public boolean isEmpty(){
        try{
            Statement statement = con.createStatement();
            int numberOfRows;
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM meals");
            rs.next();
            numberOfRows = rs.getInt(1);
            return numberOfRows == 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
