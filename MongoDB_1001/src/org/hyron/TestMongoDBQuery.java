package org.hyron;

import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.QueryOperators;

public class TestMongoDBQuery {
	private String dbname = "temp";
	private String tableName = "users";
	private Mongo mongo;
	private DB db;
	private DBCollection dbColl;

	@After
	public void destory() {
		if (null != mongo) {
			mongo.close();
		}
		mongo = null;
		db = null;
		dbColl = null;
		System.gc();
	}

	@Before
	public void init() {
		try {
			mongo = new Mongo(Constant.HOST, Constant.PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		// if there dbname is drop
		mongo.dropDatabase(dbname);
		// get db ； but default not create this db, mongodb auto create this db
		db = mongo.getDB(dbname);
		// get DBCollection ;but default not create this DBCollection, mongodb
		// auto create this DBCollection
		dbColl = db.getCollection(tableName);

		initTestData();
	}

	public void initTestData() {
		DBObject[] users = new BasicDBObject[10];
		for (int i = 0; i < users.length; i++) {
			users[i] = new BasicDBObject();
		}
		users[0].put("name", "admin");
		users[0].put("pwd", "123");
		users[0].put("age", 33);
		users[0].put("sex", "男");

		users[1].put("name", "jim");
		users[1].put("pwd", "123");
		users[1].put("age", 25);
		users[1].put("sex", "男");

		users[2].put("name", "tom");
		users[2].put("pwd", "123");
		users[2].put("age", 30);
		users[2].put("sex", "女");

		users[3].put("name", "a");
		users[3].put("pwd", "123");
		users[3].put("age", 28);
		users[3].put("sex", "男");

		users[4].put("name", "d");
		users[4].put("pwd", "123");
		users[4].put("age", 30);
		users[4].put("sex", "女");

		users[5].put("name", "c");
		users[5].put("pwd", "123");
		users[5].put("age", 30);
		users[5].put("sex", "女");

		users[6].put("name", "to");
		users[6].put("pwd", "123");
		users[6].put("age", 20);
		users[6].put("sex", "男");
		
		users[7].put("name", "t1");
		users[8].put("name", "t2");
		users[9].put("name", "t3");
		dbColl.insert(users);
	}

	private void print(Object o) {
		System.out.println(o);
	}

	private void println() {
		for (int i = 0; i < 20; i++) {
			System.out.print("-----");
		}
		System.out.println();
	}

	@Test
	// select * from users
	public void testFind() {
		print("select * from users");
		DBCursor cursor = dbColl.find();
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			print(dbObject);
		}
		println();
	}

	@Test
	// select * from users where sex = '男'
	public void testFindRef() {
		print("select * from users where sex = '男'");
		// object for which to serach
		DBObject ref = new BasicDBObject();
		ref.put("sex", "男");
		DBCursor cursor = dbColl.find(ref);
		List<DBObject> array = cursor.toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select name, pwd from users
	public void testFindRefKeys() {
		print("select name, pwd from users");
		// object for which to search
		DBObject query = new BasicDBObject();
		// fields to return
		DBObject keys = new BasicDBObject();
		keys.put("name", 1);
		keys.put("pwd", true);
		List<DBObject> list = dbColl.find(query, keys).toArray();
		for (DBObject o : list) {
			print(o);
		}
		println();
	}

	@Test
	// select name,pwd from users where sex = '男'
	public void testFindRefKeys2() {
		print("select name,pwd from users where sex = '男'");
		// object for which to search
		DBObject ref = new BasicDBObject();
		ref.put("sex", "男");
		// fields to return
		DBObject keys = new BasicDBObject();
		keys.put("name", 1);
		keys.put("pwd", true);
		DBCursor cursor = dbColl.find(ref, keys);
		List<DBObject> array = cursor.toArray();
		for (DBObject object : array) {
			print(object);
		}
		println();
	}

	@Test
	// select * from users where sex ='男' order by name
	public void testFindRefSort() {
		print("select * from users where sex ='男' order by name");
		// object for which to search
		DBObject ref = new BasicDBObject();
		ref.put("sex", "男");

		// the fields by which to srot
		DBObject orderBy = new BasicDBObject();
		orderBy.put("name", 1);

		DBCursor sort = dbColl.find(ref).sort(orderBy);
		List<DBObject> array = sort.toArray();

		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where age > 25
	public void testFindRefGt() {
		print("select * from users where age > 25");
		DBObject ref = new BasicDBObject();
		DBObject gtRef = new BasicDBObject("$gt", 25);
		ref.put("age", gtRef);
		DBCursor cursor = dbColl.find(ref);
		List<DBObject> array = cursor.toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where age != 30
	public void testFindNe() {
		print("select * from users where age != 30");
		DBObject ref = new BasicDBObject();
		DBObject gtRef = new BasicDBObject("$ne", 30);
		ref.put("age", gtRef);
		DBCursor cursor = dbColl.find(ref);
		List<DBObject> array = cursor.toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where name like '%m%'
	public void testFindLike() {
		print("select * from users where name like '%m%'");
		DBObject ref = new BasicDBObject();
		// �?�以使用正则表达�?
		ref.put("name", Pattern.compile(".*m.*"));
		DBCursor cursor = dbColl.find(ref);
		List<DBObject> array = cursor.toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where name like 'a%'
	public void testFindLike2() {
		print("select * from users where name like 'a%'");
		DBObject ref = new BasicDBObject();
		// �?�以使用正则表达�?
		ref.put("name", Pattern.compile("m.*"));
		DBCursor cursor = dbColl.find(ref);
		List<DBObject> array = cursor.toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where age > 30 and age <= 40
	public void testFindGeLte() {
		print("select * from users where age > 30 and age <= 40");
		DBObject obj = new BasicDBObject(QueryOperators.GT, 30).append(QueryOperators.LTE, 40);
		List<DBObject> array = dbColl.find(new BasicDBObject("age", obj)).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users order by name asc
	public void testFindOrderByAsc() {
		print("select * from users order by name asc");
		// 1 asc, -1 desc
		List<DBObject> array = dbColl.find().sort(new BasicDBObject("name", 1)).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users order by name desc
	public void testFindOrderByDesc() {
		print("select * from users order by name desc");
		// 1 asc, -1 desc
		List<DBObject> array = dbColl.find().sort(new BasicDBObject("name", -1)).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users age = 30 and name = 'd'
	public void testFindDBObject() {
		print("select * from users age = 30 and name = 'd'");
		List<DBObject> array = dbColl.find(new BasicDBObject("age", 30).append("name", "d")).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users limit 3 skip 4
	public void testFindLimitSkip() {
		print("select * from users limit 3 skip 4");
		// 显示3行， 跳过4�?�数�?�
		List<DBObject> array = dbColl.find().limit(3).skip(4).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users where age = 25 or name = 'tom'
	public void testFindOr() {
		print("select * from users where age = 25 or name = 'tom'");
		List<DBObject> array = dbColl.find(
				new BasicDBObject("$or", new BasicDBObject[] { new BasicDBObject("age", 25),
						new BasicDBObject("name", "tom") })).toArray();
		for (DBObject o : array) {
			print(o);
		}
		println();
	}

	@Test
	// select * from users limit 1
	public void testLimit() {
		print("select * from users limit 1");
		DBObject findOne = dbColl.findOne();
		print(findOne);
		println();
	}

	@Test
	// select count(*) from users
	public void testFindCount() {
		print("select count(*) from users");
		int count = dbColl.find().count();
		print(count);
		println();
	}

	@Test
	// select count(age) from users
	public void testFindCountAge() {
		print("select count(age) from users");
		int count = dbColl.find(new BasicDBObject("age", new BasicDBObject(QueryOperators.EXISTS, true))).count();
		print(count);
		println();
	}
}
