package com.example.blagodari.Models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.blagodari.MainScreen;

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

    public static final String LOGGED_USER_ID = "UserLogged"; //для shared preferences
    public static final int ADMIN_ID = 3;
    private User CurrentUser;
    Context context;
    public static final String PATH = "https://blagodari.herokuapp.com/";


    public DBhelper(Context context) {
        this.context = context;
    }


    public User getUserById(int id) {
        String firstname = null, surname = null, passwd = null, email = null, avatar = null;
        long time = 0;
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
        String firstname = null, surname = null, avatar = null;
        long time = 0;
        int id = -1;
        try {
            String url = PATH + "GetUserByEmailAndPassword?email=" + email + "&password=" + password;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            firstname = jsonObject.getString("firstname");
            surname = jsonObject.getString("surname");
            time = jsonObject.getLong("time");
            id = jsonObject.getInt("id");
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
            URL url = new URL(PATH + "AddUser");
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
            URL url = new URL(PATH + "UpdateUserProfile");
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
        try {
            String url = PATH + "CheckIfUserExists?email=" + email + "&password=" + password;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getBoolean("check");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addRequest(final Request request) {
        try {
            URL url = new URL(PATH + "AddRequest");
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
            if (request.getPhotoAsString() != null) {
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
        ArrayList<Request> requests = new ArrayList<>();
        try {
            String url = PATH + "GetAllRequests";
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("requests");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                String title = null, text = null, photo = null;
                long time = 0;
                int id = 0, userId = 0;
                try {
                    title = object.getString("title");
                    text = object.getString("text");
                    time = object.getLong("time");
                    id = object.getInt("id");
                    userId = object.getInt("userId");
                    photo = object.getString("photo");
                } catch (Exception e) {
                }
                User user = getUserById(userId);
                Request r;
                if (photo == null) {
                    r = new Request(id, user, title, text, time);
                } else {
                    r = new Request(id, user, title, text, photo, time);
                }
                requests.add(r);
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Request> getAllUserRequests(User user) {
        ArrayList<Request> requests = new ArrayList<>();
        try {
            String url = PATH + "GetAllUserRequests?userId=" + user.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("requests");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                String title = null, text = null, photo = null;
                long time = 0;
                int id = 0;
                try {
                    title = object.getString("title");
                    text = object.getString("text");
                    time = object.getLong("time");
                    id = object.getInt("id");
                    photo = object.getString("photo");
                } catch (Exception e) {
                }
                Request r;
                if (photo == null) {
                    r = new Request(id, user, title, text, time);
                } else {
                    r = new Request(id, user, title, text, photo, time);
                }
                requests.add(r);
            }
            return requests;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteRequest(Request r) {
        try {
            URL url = new URL(PATH + "DeleteRequest");
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
        String title = null, text = null, photo = null;
        long time = 0;
        int userId = 0;
        try {
            String url = PATH + "GetRequestById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            title = object.getString("title");
            text = object.getString("text");
            time = object.getLong("time");
            id = object.getInt("id");
            photo = object.getString("photo");
            userId = object.getInt("userId");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Request r;
        User user = getUserById(userId);
        if (photo == null) {
            r = new Request(id, user, title, text, time);
        } else {
            r = new Request(id, user, title, text, photo, time);
        }
        return r;
    }

    public void updateRequest(Request request) {
        try {
            URL url = new URL(PATH + "UpdateRequest");
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
            if (request.getPhotoAsString() != null) {
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
            URL url = new URL(PATH + "AddNewsAPI");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", news.getId());
            jsonObject.put("title", news.getTitle());
            jsonObject.put("text", news.getText());
            if (news.getPhotoAsString() != null) {
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
        String title = null, text = null, photo = null;
        long time = 0;
        try {
            String url = PATH + "GetNewsById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            title = object.getString("title");
            text = object.getString("text");
            time = object.getLong("time");
            photo = object.getString("photo");
            News n = new News(id, title, text, photo, time);
            return n;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<News> getAllNews() {
        ArrayList<News> news = new ArrayList<>();
        try {
            String url = PATH + "GetAllNews";
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("news");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                String title = null, text = null, photo = null;
                long time = 0;
                int id = 0;
                try {
                    title = object.getString("title");
                    text = object.getString("text");
                    time = object.getLong("time");
                    id = object.getInt("id");
                    photo = object.getString("photo");
                } catch (Exception e) {
                }
                news.add(new News(id, title, text, photo, time));
            }
            return news;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteNews(News news) {
        try {
            URL url = new URL(PATH + "DeleteNews");
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
            URL url = new URL(PATH + "AddToHistory");
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
        User user = getCurrentUser();
        try {
            String url = PATH + "GetAllUserHistory?userId=" + user.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("history");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                int eventType = object.getInt("eventType");
                int eventId = object.getInt("eventId");
                int id = object.getInt("id");
                if (eventType == 1) {
                    Request r = getRequestById(eventId);
                    histories.add(new History(id, user, r));
                } else if (eventType == 2) {
                    News n = getNewsById(eventId);
                    histories.add(new History(id, user, n));
                }
            }
            return histories;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFromHistory(History history) {
        int eventType = 0;
        int eventId = 0;
        int userId = history.getUser_opened().getId();
        if (history.getRequest() != null) {
            eventType = 1;
            eventId = history.getRequest().getId();
        } else if (history.getNews() != null) {
            eventType = 2;
            eventId = history.getNews().getId();
        }
        try {
            URL url = new URL(PATH + "DeleteFromHistory");
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
        try {
            URL url = new URL(PATH + "CreateNewChat");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user1", user1.getId());
            jsonObject.put("user2", user2.getId());
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

    public ArrayList<Chat> getAllCurrentUserChats() {
        ArrayList<Chat> chats = new ArrayList<>();
        User user1 = getCurrentUser();
        try {
            String url = PATH + "GetAllUserChats?userId=" + user1.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("chats");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                int user2Id = object.getInt("user2Id");  //у меня уже есть currentUser, значит можно передавать только одного
                int id = object.getInt("id");
                User user2 = getUserById(user2Id);
                chats.add(new Chat(id, user1, user2));
            }
            return chats;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Chat getChatById(int id) {
        try {
            String url = PATH + "GetChatById?id=" + id;
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            int user1Id = object.getInt("user1Id");
            int user2Id = object.getInt("user2Id");
            User user1 = getUserById(user1Id);
            User user2 = getUserById(user2Id);
            return new Chat(id, user1, user2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<Message> getAllChatMessages(Chat ch) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            String url = PATH + "GetAllChatMessages?chatId=" + ch.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arr = jsonObject.getJSONArray("messages");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                User userFrom, userTo;
                String text = object.getString("text");
                long time_created = object.getLong("time");
                int userFromId = object.getInt("userFromId");
                int userToId = object.getInt("userToId");
                userFrom = getUserById(userFromId);
                userTo = getUserById(userToId);
                messages.add(new Message(ch, userFrom, userTo, text, time_created));
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Chat getChatByTwoUsers(User u1, User u2) {
        try {
            String url = PATH + "GetChatByTwoUsers?user1=" + u1.getId() + "&user2=" + u2.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            int id = object.getInt("id");
            return new Chat(id, u1, u2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void createNewMessage(Message m) {
        try {
            URL url = new URL(PATH + "CreateNewMessage");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chatId", m.getChat().getId());
            jsonObject.put("userFrom", m.getUserFrom().getId());
            jsonObject.put("userTo", m.getUserTo().getId());
            jsonObject.put("text", m.getText());
            jsonObject.put("time", m.getTime_created());
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

    public boolean checkIfAlreadyLiked(User from, User to) {
        try {
            String url = PATH + "CheckIfAlreadyLiked?userFrom=" + from.getId() + "&userTo=" + to.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getBoolean("check");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int likeCount(User user) {
        try {
            String url = PATH + "LikeCount?userId=" + user.getId();
            MyJsonTask task = new MyJsonTask();
            task.execute(url);
            String result = task.get();
            JSONObject object = new JSONObject(result);
            return object.getInt("count");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void addLike(User from, User to) {
        try {
            URL url = new URL(PATH + "AddLike");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", from.getId());
            jsonObject.put("to", to.getId());
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

    public void deleteLike(User from, User to) {
        try {
            URL url = new URL(PATH + "DeleteLike");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from", from.getId());
            jsonObject.put("to", to.getId());
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