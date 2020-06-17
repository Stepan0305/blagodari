package com.example.blagodari.Models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class DBhelper {
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
    public static final int ADMIN_ID = 1;
    private User CurrentUser;
    ContentValues contentValues = new ContentValues();
    Context context;
    public static final String PATH = "https://blagodari.herokuapp.com/";


    public User getUserById(int id) {
        String firstname=null, surname=null, passwd=null, email=null, avatar=null;
        long time=0;
        try {
            String url = PATH + "GetUserById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            firstname = jsonObject.getString("firstname");
            surname = jsonObject.getString("surname");
            passwd = jsonObject.getString("password");
            email = jsonObject.getString("email");
            time = jsonObject.getLong("time");
            avatar = jsonObject.getString("avatar");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        User u;
        if (avatar == null) {
         u = new User(id, firstname, surname, passwd, email, time);
    } else {
            u = new User(id, firstname, surname, passwd, email, time, avatar);
        }
        return u;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        String firstname=null, surname=null, avatar=null;
        long time=0;
        int id=-1;
        try {
            String url = PATH + "GetUserByEmailAndPassword?email=" + email+"&password="+password;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            firstname = jsonObject.getString("firstname");
            surname = jsonObject.getString("surname");
            time = jsonObject.getLong("time");
            id=jsonObject.getInt("id");
            avatar = jsonObject.getString("avatar");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        User u;
        if (avatar == null) {
            u = new User(id, firstname, surname, password, email, time);
        } else {
            u = new User(id, firstname, surname, password, email, time, avatar);
        }
        return u;
    }

    public void addUser(User u) {
        try {
            URL url=new URL(PATH+"AddUser");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", u.getId());
            jsonObject.put("firstname", u.getFirstName());
            jsonObject.put("surname", u.getSurname());
            jsonObject.put("password", u.getPassword());
            jsonObject.put("email", u.getEmail());
            jsonObject.put("time", u.getDate_created());
            jsonObject.put("avatar", u.getAvatarAsString());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserProfile(User u) {
        try {
            URL url=new URL(PATH+"UpdateUserProfile");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", u.getId());
            jsonObject.put("firstname", u.getFirstName());
            jsonObject.put("surname", u.getSurname());
            jsonObject.put("password", u.getPassword());
            jsonObject.put("email", u.getEmail());
            jsonObject.put("time", u.getDate_created());
            jsonObject.put("avatar", u.getAvatarAsString());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try{
            String url = PATH + "CheckIfUserExists?email=" + email+"&password="+password;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getBoolean("check");
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void addRequest(Request request) {
        try {
            URL url=new URL(PATH+"AddRequest");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", request.getId());
            jsonObject.put("userId", request.getUser().getId());
            jsonObject.put("title", request.getTitle());
            jsonObject.put("text", request.getText());
            if (request.getPhotoAsString()!=null){
                jsonObject.put("photo", request.getPhotoAsString());
            }
            jsonObject.put("time", request.getTime_created());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Request> getAllRequests() {
        ArrayList<Request>requests=new ArrayList<>();
        try{
            String url=PATH+"GetAllRequests";
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("requests");
            for (int i = 0; i < arr.length(); i++){
                JSONObject object=arr.getJSONObject(i);
                String title=null, text=null, photo=null;
                long time=0; int id=0, userId=0;
                try {
                    title=object.getString("title");
                    text=object.getString("text");
                    time=object.getLong("time");
                    id=object.getInt("id");
                    userId=object.getInt("userId");
                    photo=object.getString("photo");
                }catch (Exception e){}
                User user=getUserById(userId);
                Request r;
                if (photo==null){
                     r=new Request(id, user, title, text, time);
                }else {
                     r=new Request(id, user, title, text, photo, time);
                }
                requests.add(r);
            }
            return requests;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Request> getAllUserRequests(User user) {
        ArrayList<Request>requests=new ArrayList<>();
        try{
            String url=PATH+"GetAllUserRequests?userId="+user.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("requests");
            for (int i = 0; i < arr.length(); i++){
                JSONObject object=arr.getJSONObject(i);
                String title=null, text=null, photo=null;
                long time=0; int id=0;
                try {
                    title=object.getString("title");
                    text=object.getString("text");
                    time=object.getLong("time");
                    id=object.getInt("id");
                    photo=object.getString("photo");
                }catch (Exception e){}
                Request r;
                if (photo==null){
                    r=new Request(id, user, title, text, time);
                }else {
                    r=new Request(id, user, title, text, photo, time);
                }
                requests.add(r);
            }
            return requests;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteRequest(Request r) {
        try {
            URL url=new URL(PATH+"DeleteRequest");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", r.getId());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Request getRequestById(int id) {
        String title=null, text=null, photo=null;
        long time=0; int userId=0;
        try {
            String url = PATH + "GetRequestById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            title=object.getString("title");
            text=object.getString("text");
            time=object.getLong("time");
            id=object.getInt("id");
            photo=object.getString("photo");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Request r;
        User user=getUserById(userId);
        if (photo==null){
            r=new Request(id, user, title, text, time);
        }else {
            r=new Request(id, user, title, text, photo, time);
        }
        return r;
    }

    public void updateRequest(Request request) {
        try {
            URL url=new URL(PATH+"UpdateRequest");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", request.getId());
            jsonObject.put("userId", request.getUser().getId());
            jsonObject.put("title", request.getTitle());
            jsonObject.put("text", request.getText());
            if (request.getPhotoAsString()!=null){
                jsonObject.put("photo", request.getPhotoAsString());
            }
            jsonObject.put("time", request.getTime_created());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNews(News news) {
        try {
            URL url=new URL(PATH+"AddNewsAPI");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("text", news.getText());
            if (news.getPhotoAsString()!=null){
                jsonObject.put("photo", news.getPhotoAsString());
            }
            jsonObject.put("time", news.getTime_created());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public News getNewsById(int id) {
        String title=null, text=null, photo=null;
        long time=0;
        try {
            String url = PATH + "GetNewsById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            title=object.getString("title");
            text=object.getString("text");
            time=object.getLong("time");
            photo=object.getString("photo");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<News> getAllNews() {
        ArrayList<News> news = new ArrayList<>();
        try{
            String url=PATH+"GetAllNews";
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("news");
            for (int i = 0; i < arr.length(); i++){
                JSONObject object=arr.getJSONObject(i);
                String title=null, text=null, photo=null;
                long time=0; int id=0;
                try {
                    title=object.getString("title");
                    text=object.getString("text");
                    time=object.getLong("time");
                    id=object.getInt("id");
                    photo=object.getString("photo");
                }catch (Exception e){}
                news.add(new News(id, title, text, photo, time));
            }
            return news;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteNews(News news) {
        try {
            URL url=new URL(PATH+"DeleteNews");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            URL url=new URL(PATH+"AddToHistory");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("eventType", eventType);
            jsonObject.put("eventId", eventId);
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<History> getAllUserHistory() {
        ArrayList<History> histories = new ArrayList<>();
        User user=getCurrentUser();
        try{
            String url=PATH+"GetAllUserHistory?userId="+user.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("history");
            for (int i = 0; i < arr.length(); i++){
                JSONObject object=arr.getJSONObject(i);
                int eventType=object.getInt("eventType");
                int eventId=object.getInt("eventId");
                int id=object.getInt("id");
                if (eventType==1){
                    Request r=getRequestById(eventId);
                    histories.add(new History(id, user, r));
                } else if(eventType==2){
                    News n=getNewsById(eventId);
                    histories.add(new History(id, user, n));
                }
            }
            return histories;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFromHistory(History history) {
        int eventType=0;
        int eventId=0;
        int userId=history.getUser_opened().getId();
        if (history.getRequest()!=null){
            eventType=1;
            eventId=history.getRequest().getId();
        }else if (history.getNews()!=null){
            eventType=2;
            eventId=history.getNews().getId();
        }
        try {
            URL url=new URL(PATH+"DeleteFromHistory");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("eventType", eventType);
            jsonObject.put("eventId", eventId);
            jsonObject.put("userId", userId);
            OutputStream os = con.getOutputStream();
            byte[] input = jsonObject.toString().getBytes("utf-8");
            os.write(input);
            os.flush();
            os.close();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class MyJsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "";
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer += (line + "\n");
                }
                return buffer;

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}