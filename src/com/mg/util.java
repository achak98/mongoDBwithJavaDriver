package com.mg;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class util {

    public static MongoClient connectToDb(String user, String dbName, String pwd) {
        MongoClient mg = new MongoClient("localhost", 27017);
        MongoCredential creds;
        try {
            creds = MongoCredential.createCredential(user, dbName, pwd.toCharArray());
            System.out.println("Connection Established.");
            System.out.println("Credentials: " + creds);
            return mg;
        } catch (Exception e) {
            System.out.println("Couldn't establish connection.");
            return null;
        }
    }

    public static void createCollection(MongoClient mg, String dbName, String collName) {
        MongoDatabase db = mg.getDatabase(dbName);
        db.createCollection(collName);
        System.out.println("Collection Created");
    }

    public static void insertDoc(MongoClient mg, String dbName, String collName) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection(collName);
        System.out.println("Collection " + collName + " selected successfully");
        Scanner sc = new Scanner(System.in);
        System.out.println("How many documents do you plan on inserting?");
        int n = Integer.parseInt(sc.nextLine());
        String docName, docVal;
        if (n == 1) {
            char flag = 'y';
            System.out.println("Please enter document name.");
            docName = sc.nextLine();
            System.out.println("Please enter document id value.");
            docVal = sc.nextLine();
            while (!docVal.matches("^[a-zA-Z0-9]*$"))
                docVal = sc.nextLine();
            Document doc = new Document(docName, docVal);
            while (flag == 'y') { //Format: (fieldName, Value)
                System.out.println("Enter field name:");
                String fn = sc.nextLine();
                while (!fn.matches("^[a-zA-Z0-9]*$"))
                    fn = sc.nextLine();
                System.out.println("Enter field value:");
                String fv = sc.nextLine();
                while (fv.equals(null))
                    fv = sc.nextLine();
                doc.append(fn, fv);
                System.out.println("If you wish to input more lines, please enter 'y'. Else enter anything else.");
                flag = sc.nextLine().charAt(0);
            }
            coll.insertOne(doc);
        } else {
            List<Document> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                char flag = 'y';
                System.out.println("Please enter document name.");
                docName = sc.nextLine();
                System.out.println("Please enter document id value.");
                docVal = sc.nextLine();
                while (!docVal.matches("^[a-zA-Z0-9]*$"))
                    docVal = sc.nextLine();
                Document doc = new Document(docName, docVal);
                while (flag == 'y') {
                    System.out.println("Enter field name:");
                    String fn = sc.nextLine();
                    while (!fn.matches("^[a-zA-Z0-9]*$"))
                        fn = sc.nextLine();
                    System.out.println("Enter field value:");
                    String fv = sc.nextLine();
                    while (fv.equals(""))
                        fv = sc.nextLine();
                    doc.append(fn, fv);
                    System.out.println("If you wish to input more lines, please enter 'y'. Else enter anything else.");
                    flag = sc.nextLine().charAt(0);
                }
                list.add(doc);
            }
            coll.insertMany(list);
        }
        System.out.println("Insertion Complete.");
        sc.close();
    }

    public static void retrieveAllDocs(MongoClient mg, String dbName, String collName) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection(collName);
        FindIterable<Document> itrD = coll.find();
        for (Document document : itrD) System.out.println(document);
    }

    public static void updateDoc(MongoClient mg, String dbName, String collName, String docName, String docVal, String fieldName, String fieldVal) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection(collName);
        Bson filter = Filters.eq(docName, docVal);
        Bson updateOperation = Updates.set(fieldName, fieldVal);
        coll.updateOne(filter, updateOperation);
        System.out.println("Document " + docName + " updated.");
    }

    public static void delDoc(MongoClient mg, String dbName, String collName, String docName) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection(collName);
        coll.deleteOne(Filters.eq(docName));
        System.out.println("Document " + docName + " deleted.");
    }

    public static void dropColl(MongoClient mg, String dbName, String collName) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoCollection<Document> coll = db.getCollection(collName);
        coll.drop();
        System.out.println("Collection " + collName + " dropped.");
    }

    public static void listAllColl(MongoClient mg, String dbName) {
        MongoDatabase db = mg.getDatabase(dbName);
        MongoIterable<String> list = db.listCollectionNames();
        for (String name : list) {
            System.out.println(name);
        }
    }
}