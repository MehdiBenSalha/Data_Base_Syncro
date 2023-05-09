package ho;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class Home_office {
    //Queue definition
    public final static String QUEUE_NAME="product_sale_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        DBInsertService dbInsertService = new DBInsertService();


        JFrame tableFrame = new JFrame();
        tableFrame.setVisible(true);
        tableFrame.setTitle("Head Office");

        Home_office_Table homeofficeTable = new Home_office_Table();

        tableFrame.add(homeofficeTable.getScrollPane());
        tableFrame.setSize(700, 450);
        tableFrame.setLocation(500, 250);


        //RabbitQ reception
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(connectionFactory.getHost(), connectionFactory.getPort()), 1000);
        if (socket.isConnected()) {
            System.out.println("Network connectivity to RabbitMQ server is established.");

            Connection c = connectionFactory.newConnection();
            // Vérifier si le serveur RabbitMQ est en cours d'exécution
            if (c != null && c.isOpen()) {
                System.out.println("RabbitMQ server is running.");

                Connection connection = connectionFactory.newConnection();
                Channel channel1 = connection.createChannel();
                Channel channel2 = connection.createChannel();
                Channel channel3 = connection.createChannel();
                channel1.queueDeclare(QUEUE_NAME + Integer.toString(1), false, false, false, null);
                channel2.queueDeclare(QUEUE_NAME + Integer.toString(2), false, false, false, null);
                channel3.queueDeclare(QUEUE_NAME + Integer.toString(3), false, false, false, null);
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    //Receiving and deserializing JSON into a list of messages.
                    String receivedMessage = new String(delivery.getBody(), "UTF-8");
                    System.out.println(receivedMessage);
                    List<Product> list = deserialize(receivedMessage);
                    System.out.println(list);
                    try {
                        //insert
                        dbInsertService.insert(list);
                        //update
                        homeofficeTable.fillTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                };
                channel1.basicConsume(QUEUE_NAME + Integer.toString(1), true, deliverCallback, consumerTag -> {
                    System.out.println("ERROR");
                });
                channel2.basicConsume(QUEUE_NAME + Integer.toString(2), true, deliverCallback, consumerTag -> {
                    System.out.println("ERROR");
                });
                channel3.basicConsume(QUEUE_NAME + Integer.toString(3), true, deliverCallback, consumerTag -> {
                    System.out.println("ERROR");
                });
            }
            else {
                System.out.println("RabbitMQ server is not running");
                Thread.sleep(5000);
            }


            }
        else {
            System.out.println("Failed to establish network connectivity to RabbitMQ");
            Thread.sleep(5000);
        }}
            public static List<Product> deserialize (String message) throws JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(message, new TypeReference<List<Product>>() {
                });

            }
        }
