package com.mg;

import com.mongodb.MongoClient;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String uid = s.nextLine();
        System.out.println("Enter your password: ");
        String pwd = s.nextLine();
        System.out.println("Enter your database name: ");
        String dbName = s.nextLine();

        MongoClient mg = util.connectToDb(uid, dbName, pwd);
        if (mg == null)
            return;
        boolean flag = true;
        try {
            while (flag) {
                String command;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                }
                System.out.println("Enter your command: ");
                command = s.nextLine();
                while (!command.matches("^[a-z A-Z0-9]*$")) { //keeps taking input, unless all characters are either alphanumerics and/or spaces
                    System.out.println("Only Alphanumerics and spaces are allowed. Please try again.");
                    command = s.nextLine();
                }
                String[] sArr = command.split(" "); //Splits the command into respective parts
                String op = sArr[0];
                try {
                    switch (op) {
                        case "create": //creates a collection
                            util.createCollection(mg, sArr[1], sArr[2]); //createCollection(MongoClient mg, String dbName, String collName)
                            break;
                        case "insert": //inserts a document in a collection
                            util.insertDoc(mg, sArr[1], sArr[2]); //insertDoc(MongoClient mg, String dbName, String collName)
                            break;
                        case "find": //retrieves all documents in a collection
                            util.retrieveAllDocs(mg, sArr[1], sArr[2]); //retrieveAllDocs(MongoClient mg, String dbName, String collName)
                            break;
                        case "update": //updates a field in a document
                            util.updateDoc(mg, sArr[1], sArr[2], sArr[3], sArr[4], sArr[5], sArr[6]); //updateDoc(MongoClient mg, String dbName, String collName, String docName, String docVal, String fieldName, String fieldVal)
                            break;
                        case "delete": //Deletes a document in a collection
                            util.delDoc(mg, sArr[1], sArr[2], sArr[3]); //delDoc(MongoClient mg, String dbName, String collName, String docName)
                            break;
                        case "drop": //Drops a collection
                            util.dropColl(mg, sArr[1], sArr[2]); //dropColl(MongoClient mg, String dbName, String collName)
                            break;
                        case "listAll": //Lists all collections
                            util.listAllColl(mg, sArr[1]); //listAllColl(MongoClient mg, String dbName)
                            break;
                        case "exit": //exits the program
                            System.out.println("Exiting program.");
                            System.exit(0);
                        default: //edge case for invalid input
                            System.out.println("Invalid command; please try again.");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error!");
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
        }
    }
}
