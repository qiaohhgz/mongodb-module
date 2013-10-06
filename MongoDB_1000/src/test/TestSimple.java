package test;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * User: jim_qiao
 * Date: 5/8/2012
 */
public class TestSimple {

	private static final String dbname = "test";
	private static final String collectionName = "foo";

	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo();
		// 查询所有的Database
		for (String name : mongo.getDatabaseNames()) {
			System.out.println("dbName: " + name);
		}
		hr();
		/*
		 * create a mongodb data object default connect localhost address post
		 * 27017
		 * mongoDB�?�以在没有创建这个数�?�库的情况下，完�?数�?�的添加�?作。当添加的时候，没有这个库，mongoDB会自动创建当�?数�?�库。
		 */
		DB db = mongo.getDB(dbname);
		// 查询所有的�?�集集�?�
		for (String name : db.getCollectionNames()) {
			System.out.println("collectionName:" + name);
		}
		hr();
		// DBCollection 相当于数�?�库中的Table
		DBCollection users = db.getCollection(collectionName);
		// select all data
		DBCursor cur = users.find();
		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
		hr();
		System.out.println(cur.count());
		System.out.println(cur.getCursorId());
		System.out.println(JSON.serialize(cur));
	}

	public static void hr() {
		System.out.println("-----------------------------");
	}
}
