package com.example.blagodari.Models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class DBhelper extends SQLiteOpenHelper {
    //дб
    public static final String DATABASE_NAME = "Blagodari_database";
    public static final int DATABASE_VERSION = 15;
    //юзеры
    public static final String TABLE_USERS = "Users";
    public static final String KEY_ID_USERS = "_id";
    public static final String USER_NAME = "FirstName";
    public static final String USER_SURNAME = "Surname";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_EMAIL = "Email";
    public static final String USER_ACCOUNT_DATE_CREATED = "CreatedTime";
    public static final String USER_AVATAR = "Avatar";
    //запросы
    public static final String TABLE_REQUESTS = "Requests";
    public static final String KEY_ID_REQUESTS = "_id";
    public static final String REQUEST_USER_CREATED_ID = "UserCreated";
    public static final String REQUEST_TITLE = "Title";
    public static final String REQUEST_TEXT = "RequestText";
    public static final String REQUEST_TIME_CREATED = "TimeCreated";
    public static final String REQUEST_PHOTO = "Photo";
    //новости
    public static final String TABLE_NEWS = "News";
    public static final String KEY_ID_NEWS = "_id";
    public static final String NEWS_TITLE = "Title";
    public static final String NEWS_TEXT = "NewsText";
    public static final String NEWS_TIME_CREATED = "TimeCreated";
    public static final String NEWS_PHOTO = "Photo";
    //история
    public static final String TABLE_HISTORY = "History";
    public static final String KEY_ID_HISTORY = "_id";
    public static final String HISTORY_EVENT_TYPE_ID = "EventType";//1 request 2 news
    public static final String HISTORY_EVENT_ID = "EventID";
    public static final String HISTORY_USER_CREATED_ID = "UserCreated";
    //чаты
    public static final String TABLE_CHATS = "Chats";
    public static final String KEY_ID_CHATS = "_id";
    public static final String CHATS_USER_1 = "UserFirst";
    public static final String CHATS_USER_2 = "UserSecond";
    //сообщения
    public static final String TABLE_MESSAGES = "Messages";
    public static final String MESSAGES_CHAT_ID = "ChatId";
    public static final String MESSAGE_FROM = "MessageFrom";
    public static final String MESSAGE_TO = "MessageTo";
    public static final String MESSAGE_TEXT = "MessageText";
    public static final String MESSAGE_TIME_CREATED = "TimeCreated";
    //лайки
    public static final String TABLE_LIKES = "Likes";
    public static final String LIKE_FROM = "LikeFrom";
    public static final String LIKE_TO = "LikeTo";

    public static final String LOGGED_USER_ID = "UserLogged"; //для shared preferences
    public static final int ADMIN_ID = 3;
    private User CurrentUser;
    ContentValues contentValues = new ContentValues();
    Context context;

    public DBhelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_sql = "Create table " + TABLE_USERS + " ( " + KEY_ID_USERS + " integer primary key, " + USER_NAME +
                " nvarchar(128), " + USER_SURNAME + " nvarchar(128), " + USER_PASSWORD + " varchar(50), " +
                USER_EMAIL + " varchar(128), " + USER_ACCOUNT_DATE_CREATED + " integer, " + USER_AVATAR + " longtext) ";
        db.execSQL(user_sql);
        String request_sql = "Create table " + TABLE_REQUESTS + " (" + KEY_ID_REQUESTS + " integer primary key, " +
                REQUEST_USER_CREATED_ID + " integer, " +
                REQUEST_TITLE + " nvarchar(77), " + REQUEST_TEXT + " text, " + REQUEST_TIME_CREATED + " integer ,"
                + REQUEST_PHOTO + " text, " + " foreign key (" +
                REQUEST_USER_CREATED_ID + ") references " + TABLE_USERS + " (" + KEY_ID_USERS + "))";
        db.execSQL(request_sql);
        String sql = "create table " + TABLE_NEWS + " ( " + KEY_ID_NEWS + " integer primary key, " + NEWS_TITLE + " nvarchar(77), " +
                NEWS_TEXT + " text, " + NEWS_TIME_CREATED + " integer, " + NEWS_PHOTO + " text )";
        db.execSQL(sql);
        sql = "create table " + TABLE_HISTORY + " ( " + KEY_ID_HISTORY + " integer primary key, " + HISTORY_EVENT_TYPE_ID + " integer, " +
                HISTORY_USER_CREATED_ID + " integer, " + HISTORY_EVENT_ID + " integer, " + " foreign key (" + HISTORY_USER_CREATED_ID + ") references " + TABLE_USERS +
                " (" + KEY_ID_USERS + "))";
        db.execSQL(sql);
        sql = "create table " + TABLE_CHATS + " ( " + KEY_ID_CHATS + " integer primary key, " + CHATS_USER_1 + " integer, " +
                CHATS_USER_2 + " integer) ";
        db.execSQL(sql);
        sql = "create table " + TABLE_MESSAGES + " ( " + MESSAGES_CHAT_ID + " integer, " + MESSAGE_FROM + " integer, " + MESSAGE_TO + " integer, " +
                MESSAGE_TEXT + " text, " + MESSAGE_TIME_CREATED + " integer, foreign key ( " + MESSAGES_CHAT_ID + " ) references " +
                TABLE_CHATS + " ( " + KEY_ID_CHATS + " ))";
        db.execSQL(sql);
        sql = "create table " + TABLE_LIKES + " ( " + LIKE_FROM + " integer, " + LIKE_TO + " integer, foreign key ( " +
                LIKE_FROM + " ) references " + TABLE_USERS + " ( " + KEY_ID_USERS + " ),  foreign key ( " + LIKE_TO +
                " )  references " + TABLE_USERS + " ( " + KEY_ID_USERS + " ) )";
        db.execSQL(sql);
        //прописать тут создание каждой таблицы
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 14) {
            String sql;
            sql = "create table if not exists " + TABLE_CHATS + " ( " + KEY_ID_CHATS + " integer primary key, " + CHATS_USER_1 + " integer, " +
                    CHATS_USER_2 + " integer) ";
            db.execSQL(sql);
            sql = "create table if not exists " + TABLE_MESSAGES + " ( " + MESSAGES_CHAT_ID + " integer, " + MESSAGE_FROM + " integer, " + MESSAGE_TO + " integer, " +
                    MESSAGE_TEXT + " text, " + MESSAGE_TIME_CREATED + " integer, foreign key ( " + MESSAGES_CHAT_ID + " ) references " +
                    TABLE_CHATS + " ( " + KEY_ID_CHATS + " ))";
            db.execSQL(sql);
        } else if (newVersion == 15) {
            String sql = "alter table " + TABLE_USERS + " add column " + USER_AVATAR + " longtext";
            db.execSQL(sql);
            sql = "create table " + TABLE_LIKES + " ( " + LIKE_FROM + " integer, " + LIKE_TO + " integer, foreign key ( " +
                    LIKE_FROM + " ) references " + TABLE_USERS + " ( " + KEY_ID_USERS + " ),  foreign key ( " + LIKE_TO +
                    " )  references " + TABLE_USERS + " ( " + KEY_ID_USERS + " ) )";
            db.execSQL(sql);
        }
    }

    public User getUserById(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_USERS + " where " + KEY_ID_USERS + "=" + id;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int userid = cursor.getInt(0);
            String name = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(USER_SURNAME));
            String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
            long createdtime = cursor.getLong(cursor.getColumnIndex(USER_ACCOUNT_DATE_CREATED));
            String avatar = cursor.getString(cursor.getColumnIndex(USER_AVATAR));
            User user;
            if (avatar != null) {
                user = new User(userid, name, surname, password, email, createdtime, avatar);
            } else {
                user = new User(userid, name, surname, password, email, createdtime);
            }
            return user;
        }
        return null;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_USERS + " where " + USER_EMAIL + " = '" + email +
                "' and " + USER_PASSWORD + " = '" + password + "'";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int userid = cursor.getInt(0);
            String name = cursor.getString(cursor.getColumnIndex(USER_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(USER_SURNAME));
            long createdtime = cursor.getLong(cursor.getColumnIndex(USER_ACCOUNT_DATE_CREATED));
            String avatar = cursor.getString(cursor.getColumnIndex(USER_AVATAR));
            User user;
            if (avatar != null) {
                user = new User(userid, name, surname, password, email, createdtime, avatar);
            } else {
                user = new User(userid, name, surname, password, email, createdtime);
            }
            return user;
        }
        return null;
    }

    public void addUser(User user) {
        SQLiteDatabase database = getWritableDatabase();
        String name = user.getFirstName();
        String surname = user.getSurname();
        String passwd = user.getPassword();
        String email = user.getEmail().toLowerCase();
        long date = user.getDate_created();
        String avatar = user.getAvatarAsString();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_SURNAME, surname);
        contentValues.put(USER_PASSWORD, passwd);
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_ACCOUNT_DATE_CREATED, date);
        if (avatar != null) {
            contentValues.put(USER_AVATAR, avatar);
        }
        database.insert(TABLE_USERS, null, contentValues);
        contentValues.clear();
    }
    public void updateUserProfile(User user){
        SQLiteDatabase database = getWritableDatabase();
        String name = user.getFirstName();
        String surname = user.getSurname();
        String passwd = user.getPassword();
        String email = user.getEmail().toLowerCase();
        String avatar = user.getAvatarAsString();
        String sql;
        if (avatar!=null) {
            sql = "update " + TABLE_USERS + " set " + USER_NAME + "= '" + name + "', " + USER_SURNAME + "= '" + surname +
                    "', " + USER_PASSWORD + "= '" + passwd + "', " + USER_EMAIL + "= '" + email + "', "+USER_AVATAR+
            "= '"+avatar+"'";
        } else {
            sql = "update " + TABLE_USERS + " set " + USER_NAME + "= '" + name + "', " + USER_SURNAME + "= '" + surname +
                    "', " + USER_PASSWORD + "= '" + passwd + "', " + USER_EMAIL + "= '" + email + "'";
        }
        database.execSQL(sql);
    }

    public void setCurrentUser(User user) {
        SharedPreferences pref = this.context.getSharedPreferences(LOGGED_USER_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        User user1 = getUserByEmailAndPassword(user.getEmail(), user.getPassword());//дополнительная проверка, что юзер действительно существует
        if (user1 != null) {
            editor.putInt(LOGGED_USER_ID, user1.getId());
        } else {
            editor.putInt(LOGGED_USER_ID, -1);
        }
        editor.commit();
        this.CurrentUser = user1;
    }

    public User getCurrentUser() {
        SharedPreferences pref = this.context.getSharedPreferences(LOGGED_USER_ID, 0);
        int userid = pref.getInt(LOGGED_USER_ID, -1);
        if (userid != -1) {
            if (this.CurrentUser == null || this.CurrentUser.getId() != userid) {
                User user = getUserById(userid);
                this.CurrentUser = user;
            }
        }
        return CurrentUser;
    }

    public boolean checkIfUserExists(String email, String password) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_USERS + " where " + USER_EMAIL + "='" + email.toLowerCase() + "' and " +
                USER_PASSWORD + "='" + password + "'";
        Cursor cursor = database.rawQuery(sql, null);
        return cursor.moveToFirst();
    }

    public void addRequest(Request request) {
        SQLiteDatabase database = getWritableDatabase();
        int userid = request.getUser().getId();
        String title = request.getTitle();
        String text = request.getText();
        String photo;
        photo = request.getPhotoAsString();
        long time = request.getTime_created();
        contentValues.put(REQUEST_USER_CREATED_ID, userid);
        contentValues.put(REQUEST_TITLE, title);
        contentValues.put(REQUEST_TEXT, text);
        if (photo != null) {
            contentValues.put(REQUEST_PHOTO, photo);
        }
        contentValues.put(REQUEST_TIME_CREATED, time);
        database.insert(TABLE_REQUESTS, null, contentValues);
        contentValues.clear();
    }

    public ArrayList<Request> getAllRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "Select * from " + TABLE_REQUESTS + " ORDER BY "
                + KEY_ID_REQUESTS + " DESC";
        Cursor c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String title = c.getString(c.getColumnIndex(REQUEST_TITLE));
            String text = c.getString(c.getColumnIndex(REQUEST_TEXT));
            String photo = c.getString(c.getColumnIndex(REQUEST_PHOTO));
            int userid = c.getInt(c.getColumnIndex(REQUEST_USER_CREATED_ID));
            long time = c.getLong(c.getColumnIndex(REQUEST_TIME_CREATED));
            User user = getUserById(userid);
            Request request;
            if (photo != null) {
                request = new Request(id, user, title, text, photo, time);
            } else {
                request = new Request(id, user, title, text, time);
            }
            requests.add(request);
        }
        c.close();
        return requests;
    }

    public ArrayList<Request> getAllUserRequests(User user) {
        ArrayList<Request> requests = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "Select * from " + TABLE_REQUESTS + " where " + REQUEST_USER_CREATED_ID + "=" +
                user.getId() + " ORDER BY "
                + KEY_ID_REQUESTS + " DESC";
        Cursor c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            int index = c.getColumnIndex(KEY_ID_REQUESTS);
            int id = c.getInt(index);
            String photo = c.getString(c.getColumnIndex(REQUEST_PHOTO));
            String title = c.getString(c.getColumnIndex(REQUEST_TITLE));
            String text = c.getString(c.getColumnIndex(REQUEST_TEXT));
            long time = c.getLong(c.getColumnIndex(REQUEST_TIME_CREATED));
            Request request;
            if (photo != null) {
                request = new Request(id, user, title, text, photo, time);
            } else {
                request = new Request(id, user, title, text, time);
            }
            requests.add(request);
        }
        c.close();
        return requests;
    }

    public void deleteRequest(Request r) {
        SQLiteDatabase database = getWritableDatabase();
        int id = r.getId();
        String sql = "delete from " + TABLE_REQUESTS + " where " + KEY_ID_REQUESTS + "=" + id;
        database.execSQL(sql);
    }

    public Request getRequestById(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_REQUESTS + " where " + KEY_ID_REQUESTS + "=" + id;
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String title = c.getString(c.getColumnIndex(REQUEST_TITLE));
            String photo = c.getString(c.getColumnIndex(REQUEST_PHOTO));
            String text = c.getString(c.getColumnIndex(REQUEST_TEXT));
            int userid = c.getInt(c.getColumnIndex(REQUEST_USER_CREATED_ID));
            long time = c.getLong(c.getColumnIndex(REQUEST_TIME_CREATED));
            User user = getUserById(userid);
            Request request;
            if (photo != null) {
                request = new Request(id, user, title, text, photo, time);
            } else {
                request = new Request(id, user, title, text, time);
            }
            return request;
        }
        return null;
    }

    public void updateRequest(Request request) {
        SQLiteDatabase database = getWritableDatabase();
        String title = request.getTitle();
        String text = request.getText();
        String photo = request.getPhotoAsString();
        String sql;
        if (photo != null) {
            sql = "update " + TABLE_REQUESTS + " set " + REQUEST_TITLE + "='" + title + "', " + REQUEST_TEXT
                    + "='" + text + "', " + REQUEST_PHOTO + "='" + photo + "'" + " where " + KEY_ID_REQUESTS + "=" + request.getId();
        } else {
            sql = "update " + TABLE_REQUESTS + " set " + REQUEST_TITLE + "='" + title + "', " + REQUEST_TEXT
                    + "='" + text + "' where " + KEY_ID_REQUESTS + "=" + request.getId();
        }
        database.execSQL(sql);
    }

    public void addNews(News news) {
        SQLiteDatabase database = getWritableDatabase();
        String title = news.getTitle();
        String text = news.getText();
        String photo = news.getPhotoAsString();
        long time = news.getTime_created();
        if (photo != null) {
            contentValues.put(NEWS_PHOTO, photo);
        }
        contentValues.put(NEWS_TITLE, title);
        contentValues.put(NEWS_TEXT, text);
        contentValues.put(NEWS_TIME_CREATED, time);
        database.insert(TABLE_NEWS, null, contentValues);
        contentValues.clear();
    }

    public News getNewsById(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_NEWS + " where " + KEY_ID_NEWS + "=" + id;
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String title = c.getString(1);
            String text = c.getString(2);
            String photo = c.getString(3);
            long time = c.getLong(4);
            News news;
            if (photo != null) {
                news = new News(id, title, text, photo, time);
            } else {
                news = new News(id, title, text, time);
            }

            return news;
        }
        return null;
    }

    public ArrayList<News> getAllNews() {
        ArrayList<News> news = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_NEWS + " order by " + KEY_ID_NEWS + " desc";
        Cursor c;
        c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(KEY_ID_NEWS));
            String title = c.getString(c.getColumnIndex(NEWS_TITLE));
            String text = c.getString(2);//а тут только в 1 месте
            String photo = c.getString(c.getColumnIndex(NEWS_PHOTO));
            long time = c.getLong(c.getColumnIndex(NEWS_TIME_CREATED));
            News n;
            if (photo != null) {
                n = new News(id, title, text, photo, time);
            } else {
                n = new News(id, title, text, time);
            }
            news.add(n);
        }
        c.close();
        return news;
    }

    public void deleteNews(News news) {
        SQLiteDatabase database = getWritableDatabase();
        int id = news.getId();
        String sql = "delete from " + TABLE_NEWS + " where " + KEY_ID_NEWS + "=" + id;
        database.execSQL(sql);
    }

    public void addToHistory(History history) {
        int userId = history.getUser_opened().getId();
        int eventType;
        int eventId;
        if (history.getRequest() != null) {
            eventType = 1;
            eventId = history.getRequest().getId();
        } else if (history.getNews() != null) {
            eventType = 2;
            eventId = history.getNews().getId();
        } else return;
        SQLiteDatabase database = getWritableDatabase();
        contentValues.put(HISTORY_EVENT_ID, eventId);
        contentValues.put(HISTORY_EVENT_TYPE_ID, eventType);
        contentValues.put(HISTORY_USER_CREATED_ID, userId);
        database.insert(TABLE_HISTORY, null, contentValues);
        contentValues.clear();
    }

    public ArrayList<History> getAllUserHistory() {
        ArrayList<History> histories = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        int userId = getCurrentUser().getId();
        String sql = "select * from " + TABLE_HISTORY + " where " + HISTORY_USER_CREATED_ID + " = " + userId + " order by " +
                KEY_ID_HISTORY + " desc";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID_HISTORY));
            int eventType = cursor.getInt(cursor.getColumnIndex(HISTORY_EVENT_TYPE_ID));
            int eventId = cursor.getInt(cursor.getColumnIndex(HISTORY_EVENT_ID));
            if (eventType == 1) {
                Request request = getRequestById(eventId);
                History history = new History(id, getCurrentUser(), request);
                histories.add(history);
            } else if (eventType == 2) {
                News news = getNewsById(eventId);
                History history = new History(id, getCurrentUser(), news);
                histories.add(history);
            }
        }
        return histories;
    }

    public void deleteFromHistory(History history) {
        SQLiteDatabase database = getWritableDatabase();
        int id = history.getUser_opened().getId();
        String sql = "delete from " + TABLE_HISTORY + " where " + HISTORY_USER_CREATED_ID + "=" + id;
        database.execSQL(sql);
    }

    public void createNewChat(User user1, User user2) {
        SQLiteDatabase database = getWritableDatabase();
        int id1 = user1.getId();
        int id2 = user2.getId();
        contentValues.put(CHATS_USER_1, id1);
        contentValues.put(CHATS_USER_2, id2);
        database.insert(TABLE_CHATS, null, contentValues);
        contentValues.clear();
    }

    public ArrayList<Chat> getAllCurrentUserChats() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Chat> chats = new ArrayList<>();
        int userid = getCurrentUser().getId();
        String sql = "select * from " + TABLE_CHATS + " where " + CHATS_USER_1 + "=" + userid + " or " + CHATS_USER_2 + "=" + userid;
        Cursor c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(KEY_ID_CHATS));
            int user1id = c.getInt(c.getColumnIndex(CHATS_USER_1));
            int user2id = c.getInt(c.getColumnIndex(CHATS_USER_2));
            User u1 = getUserById(user1id);
            User u2 = getUserById(user2id);
            Chat chat = new Chat(id, u1, u2);
            chats.add(chat);
        }
        return chats;
    }

    public Chat getChatById(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "select * from " + TABLE_CHATS + " where " + KEY_ID_CHATS + "=" + id;
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            int chatId = c.getInt(c.getColumnIndex(KEY_ID_CHATS));
            User u1 = getUserById(c.getInt(c.getColumnIndex(CHATS_USER_1)));
            User u2 = getUserById(c.getInt(c.getColumnIndex(CHATS_USER_2)));
            return new Chat(chatId, u1, u2);
        }
        return null;
    }

    public ArrayList<Message> getAllChatMessages(Chat ch) {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Message> messages = new ArrayList<>();
        int chatId = ch.getId();
        String sql = "select * from " + TABLE_MESSAGES + " where " + MESSAGES_CHAT_ID + "=" + chatId +
                " order by " + MESSAGE_TIME_CREATED + " asc";
        Cursor c = database.rawQuery(sql, null);
        while (c.moveToNext()) {
            int chId = c.getInt(c.getColumnIndex(MESSAGES_CHAT_ID));
            int uFrom = c.getInt(c.getColumnIndex(MESSAGE_FROM));
            int uTo = c.getInt(c.getColumnIndex(MESSAGE_TO));
            String text = c.getString(c.getColumnIndex(MESSAGE_TEXT));
            long time = c.getLong(c.getColumnIndex(MESSAGE_TIME_CREATED));
            Chat chat = getChatById(chId);
            User userFrom = getUserById(uFrom);
            User userTo = getUserById(uTo);
            Message message = new Message(chat, userFrom, userTo, text, time);
            messages.add(message);
        }
        return messages;
    }

   /* public boolean checkIfChatAlreadyExists(User u1, User u2){
        SQLiteDatabase database=getReadableDatabase();
        int u1Id=u1.getId();
        int u2Id=u2.getId();
        String sql="select * from "+TABLE_CHATS+" where "+CHATS_USER_1+"="+u1Id+" and "+CHATS_USER_2+"="+u2Id+
                " or "+CHATS_USER_1+"="+u2Id+" and "+CHATS_USER_2+"="+u1Id;
        Cursor c=database.rawQuery(sql, null);
        return c.moveToFirst();
    }*/

    public Chat getChatByTwoUsers(User u1, User u2) {
        SQLiteDatabase database = getReadableDatabase();
        int u1Id = u1.getId();
        int u2Id = u2.getId();
        String sql = "select * from " + TABLE_CHATS + " where " + CHATS_USER_1 + "=" + u1Id + " and " + CHATS_USER_2 + "=" + u2Id +
                " or " + CHATS_USER_1 + "=" + u2Id + " and " + CHATS_USER_2 + "=" + u1Id;
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndex(KEY_ID_CHATS));
            User user1 = getUserById(u1Id);
            User user2 = getUserById(u2Id);
            Chat chat = new Chat(id, user1, user2);
            return chat;
        }
        return null;
    }

    public void createNewMessage(Message m) {
        SQLiteDatabase database = getWritableDatabase();
        int chatId = m.getChat().getId();
        int uFromId = m.getUserFrom().getId();
        int uToId = m.getUserTo().getId();
        String text = m.getText();
        long time = m.getTime_created();
        String sql = "insert into " + TABLE_MESSAGES + " ( " + MESSAGES_CHAT_ID + ", " + MESSAGE_FROM + ", " + MESSAGE_TO +
                ", " + MESSAGE_TEXT + ", " + MESSAGE_TIME_CREATED + ") values ( " + chatId + ", " + uFromId + ", " +
                uToId + ", '" + text + "', " + time + " )";
        database.execSQL(sql);
    }

    public boolean checkIfAlreadyLiked(User from, User to) {
        SQLiteDatabase database = getReadableDatabase();
        int fromId = from.getId();
        int toId = to.getId();
        String sql = "select * from " + TABLE_LIKES + " where " + LIKE_FROM + "=" + fromId + " and " + LIKE_TO + "=" + toId;
        Cursor c = database.rawQuery(sql, null);
        return c.moveToFirst();
    }

    public int likeCount(User user) {
        SQLiteDatabase database = getReadableDatabase();
        int userId = user.getId();
        String sql = "select count(*) from " + TABLE_LIKES + " where " + LIKE_TO + "=" + userId;
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return -1;
    }

    public void addLike(User from, User to) {
        SQLiteDatabase database = getWritableDatabase();
        int fromId = from.getId();
        int toId = to.getId();
        String sql = "insert into " + TABLE_LIKES + " ( " + LIKE_FROM + ", " + LIKE_TO + " ) values ( " + fromId + ", " +
                toId + " )";
        database.execSQL(sql);
    }

    public void deleteLike(User from, User to) {
        SQLiteDatabase database = getWritableDatabase();
        int fromId = from.getId();
        int toId = to.getId();
        String sql = "delete from " + TABLE_LIKES + " where " + LIKE_FROM + "=" + fromId + " and " + LIKE_TO + "=" + toId;
        database.execSQL(sql);
    }
}