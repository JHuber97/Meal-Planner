package mealplanner;

import java.util.*;
import java.sql.*;

import static java.lang.System.exit;

public class Main {
  static final MealPlannerDB db = new MealPlannerDB();
  static Scanner keyb = new Scanner(System.in);

  static ArrayList<String[]> savedMeals = new ArrayList<>();

  public static ArrayList<String[]> getSavedMeals() {
    return savedMeals;
  }

  public static void setSavedMeals(ArrayList<String[]> savedMeals) {
    Main.savedMeals = savedMeals;
  }

  public  static void menue() {
    String input;
    System.out.println("What would you like to do (add, show, exit)?");
    input = keyb.nextLine();
    switch (input) {
      case "add" -> addMenue();
      case "show" -> show();
      case "exit" -> {
        System.out.println("Bye!");
        System.exit(0);
      }
      default -> menue();
    }
  }

  public static void addMenue() {
    String category;
    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
    category = keyb.nextLine();
    switch (category) {
      case "breakfast", "lunch", "dinner" -> addMeal(category);
      default -> {
        do{
          System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
          category = keyb.nextLine();
        }while(!category.equals("breakfast") && !category.equals("lunch") && !category.equals("dinner"));
        addMeal(category);
      }
    }
  }

  public static void addMeal(String category) {
    String nameOfMeal;
    String ingredients;

    //name of meal
    System.out.println("Input the meal's name:");
    nameOfMeal = keyb.nextLine();
    if(!nameOfMeal.matches("[A-Za-z]+[A-Za-z \\\\']*")){
      System.out.println("Wrong format. Use letters only!");
      addMeal(category);
    }

    //ingredients of meal
    System.out.println("Input the ingredients:");
    ingredients = keyb.nextLine();
    while(!ingredients.matches("^(([a-zA-Z ](, [A-Za-z]+)?)*)+$")){
      System.out.println("Wrong format. Use letters only!");
      ingredients = keyb.nextLine();
    }

    db.addMeal(category, nameOfMeal, ingredients);
    System.out.println("The meal has been added!");
    menue();
  }

  public static void show() {
    if(db.isEmpty()){
      System.out.println("No meals saved. Add a meal first.");
      Main.menue();
    }
    db.showMeals();
  }
  public static void main(String[] args) {
    db.createTables();
    menue();
  }
}