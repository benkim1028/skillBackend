package skillbackend.Database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import skillbackend.Model.Credentials;
import skillbackend.Model.Hash;

public class userCRUD implements CRUD {
    private static final Hash HASH = new Hash();
    private MongoCollection<Document> collection = db.getCollection("user");

    @Override
    public void create(Object obj) throws Exception {
        if (!(obj instanceof Credentials))
            throw new Exception();
        Credentials credentials = (Credentials) obj;
        Document doc = new Document("username", credentials.getUsername())
                .append("password", HASH.hashPassword(credentials.getPassword()));
        collection.insertOne(doc);
    }
    @Override
    public void read(String collectionName){}

    @Override
    public void update(String collectionName){}

    @Override
    public void delete(){}

}
