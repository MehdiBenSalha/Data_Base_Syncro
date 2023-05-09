package bo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Branch_Office {
//define queue
    public final static String QUEUE_NAME="product_sale_queue";
// define Data Access Object
    public static DBRetrieveService dbRetrieveService;
    public static DBUpdateService dbUpdateService;

    public static void main(String[] args) throws IOException, SQLException, TimeoutException, InterruptedException {
        int i = Integer.parseInt(args[0]);


        JFrame insertionFrame = new JFrame();
        insertionFrame.setVisible(true);

        insertionFrame.setTitle("Branch Office " + args[0]);

        Branch_Office_Table branchOfficeTable = new Branch_Office_Table(i);

        InsertPanel insertPanel = new InsertPanel(i, branchOfficeTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                insertPanel, branchOfficeTable.getScrollPane());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);

        insertionFrame.add(splitPane);
        insertionFrame.setSize(700, 450);
        insertionFrame.setLocation(500, 250);

        //Initialize Data Access Object
        dbRetrieveService = new DBRetrieveService(i, false);
        dbUpdateService = new DBUpdateService(i);

        //RabbitQ sender
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
                //Job Definition
                TimerTask task = new TimerTask() {
                    public void run() {
                        try {
                            //
                            List<Product> List = dbRetrieveService.retrieve();
                            System.out.println(List);

                            String message = serialize(List);

                            try (Connection connection = connectionFactory.newConnection()) {
                                Channel channel = connection.createChannel();
                                channel.queueDeclare(QUEUE_NAME + Integer.toString(i), false, false, false, null);

                                channel.basicPublish("", QUEUE_NAME + Integer.toString(i), null, message.getBytes());
                                System.out.println(" [x] sent '" + message + " '" + LocalDateTime.now().toString());
                                //Setting the 'sent' attribute to TRUE in the database table.
                                dbUpdateService.update(List);
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                        }
                    }
                };
                Timer timer = new Timer("Timer");

                //This process repeats every 30 seconds
                long time = 30 * 1000L;
                timer.schedule(task, 0, time);

            } else {
                System.out.println("RabbitMQ server is not running");
                Thread.sleep(5000);
            }
        } else {
            System.out.println("Failed to establish network connectivity to RabbitMQ");
            Thread.sleep(5000);
        }
    }

    public static String serialize(List<Product> List) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(List);
    }}

