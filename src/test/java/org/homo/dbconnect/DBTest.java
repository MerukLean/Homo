package org.homo.dbconnect;

import org.homo.Application;
import org.homo.authority.model.User;
import org.homo.dbconnect.query.Query;
import org.homo.dbconnect.inventory.InventoryManager;
import org.homo.dbconnect.inventory.InventoryFactory;
import org.homo.dbconnect.transaction.Transaction;
import org.homo.orderdemo.model.Commodity;
import org.homo.orderdemo.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujianchuan 2018/12/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DBTest {

    @Autowired
    DatabaseManager manager;
    @Autowired
    InventoryFactory sessionFactory;

    @Before
    public void setup() {
    }

    @Test
    public void test1() {
        Connection connection = manager.getConn();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT T.AVATAR AS avatar, T.NAME AS name FROM TBL_USER T");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String avatar = resultSet.getString("avatar");
                String name = resultSet.getString("name");
                User user = User.newInstance(avatar, name);
                System.out.println(user.getName() + " - " + user.getAvatar());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        Transaction transaction = manager.getTransaction();
        transaction.connect();
        transaction.transactionOn();
        manager.save(User.newInstance("Crease", "克里斯"));
        manager.save(User.newInstance("Homo", "霍姆"));
        transaction.commit();
        transaction.closeConnection();
    }

    @Test
    public void test3() throws SQLException {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        Query query = manager.createSQLQuery("select avatar, name from TBL_user");
        Object[] result = (Object[]) query.unique();
        System.out.println(result[0] + "-" + result[1]);
        manager.getTransaction().closeConnection();
    }

    @Test
    public void test4() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        System.out.println(((User) manager.findOne(User.class, 5L)).getName());
        manager.getTransaction().closeConnection();
    }

    @Test
    public void test5() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        User user = (User) manager.findOne(User.class, 5L);
        user.setAvatar("ANT");
        user.setName("蚂蚁2号");
        manager.update(user);
        manager.getTransaction().closeConnection();
    }

    @Test
    public void test6() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        User user = (User) manager.findOne(User.class, manager.getMaxUuid(User.class));
        manager.delete(user);
        manager.getTransaction().closeConnection();
    }

    @Test
    public void test7() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        manager.getTransaction().transactionOn();
        Order order = Order.newInstance("A-002", new BigDecimal("12.6"));
        List<Commodity> detailList = new ArrayList<>();
        Commodity apple = new Commodity();
        apple.setName("苹果");
        apple.setPrice(new BigDecimal(7.6));
        Commodity cookies = new Commodity();
        cookies.setName("饼干");
        cookies.setPrice(new BigDecimal(5));
        detailList.add(apple);
        detailList.add(cookies);
        order.setCommodities(detailList);
        manager.save(order);
        manager.getTransaction().commit();
        manager.getTransaction().closeConnection();
    }

    @Test
    public void test8() throws Exception {
        InventoryManager manager = sessionFactory.getManager();
        manager.getTransaction().connect();
        System.out.println(((Order) manager.findOne(Order.class, 11L)).getCommodities());
        manager.getTransaction().closeConnection();
    }
}
