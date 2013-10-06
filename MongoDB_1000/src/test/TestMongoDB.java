package test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 * User: jim_qiao Date: 5/9/2012
 */
public class TestMongoDB {
	private static final String host = "172.20.230.58";
	private static final int port = 27017;
	private String dbname = "temp";
	private String tableName = "users";
	private Mongo mongo;
	private DB db;
	private DBCollection users;// table object
	private boolean showLog = true;

	@Before
	public void init() {
		try {
			mongo = new Mongo(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		//drop dbname database
		mongo.dropDatabase(dbname);
		// get db ； but default not create this db, mongodb auto create this db
		db = mongo.getDB(dbname);
		// get DBCollection ;but default not create this DBCollection, mongodb
		// auto create this DBCollection
		users = db.getCollection(tableName);

		add();
	}

	public void removeAll() {
		long len = users.count();
		for (long i = 0; i < len; i++) {
			DBObject one = users.findOne();
			int n = users.remove(one).getN();
			Assert.assertTrue(1 == n);
			print("remove", one, n);
		}
	}

	@After
	public void destory() {
		queryAll();
		if (null != mongo) {
			mongo.close();
		}
		mongo = null;
		db = null;
		users = null;
		System.gc();
	}

	@Test
	public void queryAll() {
		print("start queryAll method, count", users.count());
		DBCursor cur = users.find();
		while (cur.hasNext()) {
			print(cur.next());
		}
		print("end queryAll method");
		hr();
	}

	public void add() {
		print("start add method");

		DBObject user = new BasicDBObject();
		user.put("name", "tom");
		user.put("age", 24);
		user.put("sex", "男");
		print("users.save", user, assertWriteResultIsZero(users.save(user)).getN());

		// 添加多�?�数�?�，传递Array对象
		BasicDBObject[] array = { new BasicDBObject("name", "student"), new BasicDBObject("name", "student"),
				new BasicDBObject("name", "student"), new BasicDBObject("name", "student"),
				new BasicDBObject("name", "student"), new BasicDBObject("name", "student"),
				new BasicDBObject("name", "student"), new BasicDBObject("name", "student") };
		print("users.insert", assertWriteResultIsZero(users.insert(array)).getN());

		// 创建List集�?�
		List<DBObject> list = new ArrayList<DBObject>();
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			DBObject u = new BasicDBObject("name", "lucy" + i);
			u.put("age", 24);
			u.put("sex", "男");
			list.add(u);
		}
		for (int i = 0; i < 30; i++) {
			DBObject u = new BasicDBObject("name", "jared" + i);
			u.put("age", 24 + r.nextInt(10));
			u.put("sex", "男");
			list.add(u);
		}
		// 添加List集�?�
		print("user.insertList", assertWriteResultIsZero(users.insert(list)).getN());

		queryAll();
	}

	@Test
	public void remove() {
		DBObject user = users.findOne();
		print("remove", user, assertWriteResultIsOne(users.remove(user)).getN());

		BasicDBObject basicDBObject = new BasicDBObject("age", new BasicDBObject("$gte", 24));
		print("remove age > 24: " + assertWriteResult(users.remove(basicDBObject)).getN());
	}

	@Test
	public void modify1() {
		// 原先的用户
		DBObject user = users.findOne();
		print("modify.user._id", user.get("_id"));
		// 新用户
		DBObject newUser = new BasicDBObject();
		newUser.put("name", "update");

		print("users.update", user, assertWriteResult(users.update(user, newUser, true// �?添加数�?�
				, false// 多�?�修改
				)));
	}

	@Test
	public void query() {
		// search by id
		ObjectId oid = (ObjectId) users.findOne().get("_id");
		DBObject dbObject = new BasicDBObject("_id", oid);
		List<DBObject> list = users.find(dbObject).toArray();
		Assert.assertTrue(list.size() == 1);
		println("find id = " + oid + " name=" + list);

		// search age = 24
		List<DBObject> list2 = users.find(new BasicDBObject("age", 24)).toArray();
		Assert.assertTrue(list2.size() > 1);
		println("find age = 24 name = ", list2);

	}
	
	@Test
	public void testOthers(){
	    DBObject user = new BasicDBObject();
	    user.put("name", "hoojo");
	    user.put("age", 24);
	    //JSON 对象转�?�        
	    print("serialize: " + JSON.serialize(user));
	    //�??�?列化
	    print("parse: " + JSON.parse("{ \"name\" : \"hoojo\" , \"age\" : 24}"));
	    print("判断readOnly Collection是�?�存在: " + db.collectionExists("readOnly"));

	    //如果�?存在就创建
	    if (!db.collectionExists("readOnly")) {
	        DBObject options = new BasicDBObject();
	        options.put("size", 20);
	        options.put("capped", 20);
	        options.put("max", 20);
	        print(db.createCollection("account", options));
	    }
	    //设置db为�?�读
	    db.setReadOnly(true);
	    //�?�读�?能写入数�?�
	    db.getCollection("readOnly").save(user);
	}

	private WriteResult assertWriteResult(WriteResult wr) {
		Assert.assertTrue(wr.getError() == null);
		return wr;
	}

	private WriteResult assertWriteResultIsOne(WriteResult wr) {
		Assert.assertTrue(wr.getN() == 1);
		return assertWriteResult(wr);
	}

	private WriteResult assertWriteResultIsZero(WriteResult wr) {
		Assert.assertTrue(wr.getN() == 0);
		return assertWriteResult(wr);
	}

	public void print(Object o) {
		if (showLog) {
			System.out.println(o);
		}
	}

	public void print(String n, Object o) {
		print(n + ": " + o);
	}

	public void print(String name, Object one, Object o) {
		print(name + "(" + one + ")", o);
	}

	public void println(Object o) {
		print(o);
		hr();
	}

	public void println(String n, Object o) {
		print(n, o);
		hr();
	}

	private void hr() {
		print("----------------------------");
	}
}
