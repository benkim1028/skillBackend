package skillbackend.Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import skillbackend.Exceptions.InvalidUserException;
import skillbackend.Exceptions.UsernameExistException;
import skillbackend.Exceptions.WrongPasswordException;
import skillbackend.Model.Credentials;
import skillbackend.Model.Hash;
import skillbackend.Model.User;

import javax.validation.constraints.Null;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.set;

public class userCRUD{
    MongoDB mongoDB = MongoDB.getInstance();
    MongoDatabase db = mongoDB.getDB();
    private Hash HASH = new Hash();
    private MongoCollection<Document> collection = db.getCollection("user");

    public void create(Object obj) throws Exception {
        User aUser = (User) obj;
        Document query = collection.find(eq("username", aUser.getUsername())).first();
        if (query != null) throw new UsernameExistException();

        Document doc = User.createDBObject(aUser);
        collection.insertOne(doc);
    }


    public Object read(Object obj) throws Exception {
        Credentials credentials = (Credentials) obj;
        Document query = collection.find(eq("username", credentials.getUsername())).first();
        if (query == null) {
            throw new InvalidUserException();
        } else if (!HASH.checkPassword(credentials.getPassword().trim(), query.getString("password"))) {
            throw new WrongPasswordException();
        }
        return query;
    }

    public void update(User aUser) {
        collection.updateOne(
                eq("username", aUser.getUsername()),
                combine(set("firstname", aUser.getFirstname()),
                        set("lastname", aUser.getLastname()),
                        set("phonenumber", aUser.getPhonenumber()),
                        set("gender", aUser.getGender()),
                        set("birthdate", aUser.getBirthdate()),
                        currentDate("lastModified")));
    }

    public void delete() {
    }

    public String getUserInfo(String username) throws NullPointerException {
        System.out.println("getUserINfo");
        Document query = collection.find(eq("username", username)).first();
        System.out.println("getUserINfo" + query);
        query.remove("password");
        System.out.println("getUserINfo" + query);
        return query.toJson();
    }
    
    public static void main(String[] args){
        userCRUD userCRUD =  new userCRUD();
        System.out.println(userCRUD.getUserInfo("benkim1028@gmail.co"));
    }


}
