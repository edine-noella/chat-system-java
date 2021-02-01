package repositories;

import models.Messages;
import config.Config;
import utils.DirectMessage;
import utils.GroupMessage;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class MessagesRepository {


    //View Messages

    public List<DirectMessage> getDirectMessages(Messages messages) throws SQLException {
        List<DirectMessage> allMessagesDM = new ArrayList<>();

        Connection conn = Config.getConnection();
        Statement statement = conn.createStatement();

        String readQuery = String.format(
                "SELECT * from messages where sender = %d && user_receiver = %d;",
                messages.getSender(), messages.getUser_receiver());

        ResultSet result = statement.executeQuery(readQuery);

        while (result.next()){

            Integer id = result.getInt(1);
            String content = result.getString(2);
            Integer sender = result.getInt(3);
            Integer user_receiver = result.getInt(4);
            Integer original_message = result.getInt(6);
            Date sent_at = result.getDate(7);

            DirectMessage message = (DirectMessage) result;
            allMessagesDM.add(message);
        }
        statement.close();
        conn.close();
        return allMessagesDM;
    }

    public List<GroupMessage> getGroupMessages(Messages messages) throws SQLException {
        List<GroupMessage> allMessagesGrp = new ArrayList<>();

        Connection conn = Config.getConnection();
        Statement statement = conn.createStatement();

        String readQuery = String.format(
                "SELECT * from messages where sender = %d && group_receiver = %d;",
                messages.getSender(), messages.getUser_receiver());

        ResultSet result = statement.executeQuery(readQuery);

        while (result.next()){

            Integer id = result.getInt(1);
            String content = result.getString(2);
            Integer sender = result.getInt(3);
            Integer group_receiver = result.getInt(4);
            Integer original_message = result.getInt(6);
            Date sent_at = result.getDate(7);

            GroupMessage message = (GroupMessage) result;
            allMessagesGrp.add(message);
        }
        statement.close();
        conn.close();
        return allMessagesGrp;
    }
    public Set<ResultSet> getNotifications(int user_id)throws Exception{
		Set<ResultSet>  notis = new HashSet<ResultSet>();
		Connection conn = Config.getConnection();
        Statement statement = conn.createStatement();
		ResultSet gr;
		gr = statement.executeQuery("select * from user_group where user_id="+user_id);
		ResultSet grm = null;
		while(gr.next()) {				
				grm = statement.executeQuery("select * from messages where group_receiver = "+gr.getInt(1)+" and isRead=false and sender!="+user_id);
				notis.add(grm);
		}
		ResultSet rs;
		rs = statement.executeQuery("Select * from messages where user_receiver="+user_id+" and isRead=false and sender!="+user_id);
		notis.add(rs);
		
		statement.close();
        conn.close();
		return notis;
	}
}
