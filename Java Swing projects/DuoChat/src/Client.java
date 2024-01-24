import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {
    static JFrame frame = new JFrame();
    static JPanel bottomPanel;
    JTextField tf;
    JButton pc;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    Client(){
        frame.setLayout(null);
        frame.setTitle("Client");

        // this panel is the status bar and other stuff on the top of the frame
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0,0,0));
        topPanel.setBounds(0,0,450,70);
        topPanel.setLayout(null);
        frame.add(topPanel);

        // setting up the back icon on the top panel
        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("pictures/back.png"));
        Image img2 = img1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
        ImageIcon img3 = new ImageIcon(img2);
        JLabel back = new JLabel(img3);
        back.setBounds(5,20,25,25);
        topPanel.add(back);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // Setting up the profile picture of the receiver
        ImageIcon img4 = new ImageIcon(ClassLoader.getSystemResource("pictures/kiluva.jpeg"));
        Image img5 = img4.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon img6 = new ImageIcon(img5);
        JLabel profile = new JLabel(img6);
        profile.setBounds(40,10,50,50);
        topPanel.add(profile);

        // setting up the Video icon on the top panel
        ImageIcon img7 = new ImageIcon(ClassLoader.getSystemResource("pictures/video.png"));
        Image img8 = img7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon img9 = new ImageIcon(img8);
        JLabel video = new JLabel(img9);
        video.setBounds(300,20,30,30);
        topPanel.add(video);

        //setting up the Dialer icon in the top panel
        ImageIcon img10 = new ImageIcon(ClassLoader.getSystemResource("pictures/Dialer.png"));
        Image img11 = img10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon img12 = new ImageIcon(img11);
        JLabel phone = new JLabel(img12);
        phone.setBounds(360,20,35,30);
        topPanel.add(phone);

        // setting up the three dot icon on the top panel
        ImageIcon img13 = new ImageIcon(ClassLoader.getSystemResource("pictures/threeDot.png"));// here it get the path of the threeDot image
        Image img14 = img13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);// here it take the threeDot image from the path and stores the image in the Image Object with the Scales of 10 pixels and 25 pixels
        ImageIcon img15 = new ImageIcon(img14);// here actually stores the image in the ImageIcon
        JLabel more = new JLabel(img15);
        more.setBounds(420,20,10,25);
        topPanel.add(more);

        // this is the name of receiving person
        JLabel name = new JLabel("Selby");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.white);
        name.setFont(new Font("georgia", Font.BOLD,18));
        topPanel.add(name);

        // this shows the person is offline or online
        JLabel status = new JLabel("Online");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.white);
        status.setFont(new Font("georgia",Font.PLAIN,14));
        topPanel.add(status);

        // this the Chatting pannel
        bottomPanel = new JPanel();
        bottomPanel.setBounds(5,75,440,570);
        bottomPanel.setBackground(new Color(105,105,105));
        frame.add(bottomPanel);

       // setting up the PaperClip option in the Bottom Panel. to choose file from the file manager
        ImageIcon paper = new ImageIcon(ClassLoader.getSystemResource("pictures/pc3.png"));
        Image paper2 = paper.getImage().getScaledInstance(35, 40,Image.SCALE_DEFAULT);
        ImageIcon paper3 = new ImageIcon(paper2);
        pc = new JButton();
        pc.setBackground(Color.black);
        pc.setBounds(280,655,35,40);
        pc.setIcon(paper3);
        pc.addActionListener(this);
        frame.add(pc);

        // this is the msg box
        tf = new JTextField();
        tf.setBounds(5,655,280,40);
        tf.setFont(new Font("Helvetica",Font.PLAIN,16));
        frame.add(tf);

        // this is the send Button
        JButton send = new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(0,0,0));
        send.setFont(new Font("georgia",Font.PLAIN,16));
        send.setForeground(Color.white);
        send.setFocusable(false);
        send.addActionListener(this);

        frame.add(send);
        frame.setLocation(800,50);
        frame.getContentPane().setBackground(new Color(105,105,105));
        frame.setUndecorated(true); // it is used for create a frame without any decprations like minimize,maximize etc.
        frame.setSize(450,700);
        frame.setVisible(true);
    }

    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(new Color(105,105,105));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(255,255,255));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));


        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Client();

        try{
            Socket s = new Socket("127.0.0.1",6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true){
                bottomPanel.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                panel.setBackground(new Color(105,105,105)); //<--

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                left.setBackground(new Color(105,105,105)); //<--
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                bottomPanel.add(vertical,BorderLayout.PAGE_START);
                frame.validate();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = tf.getText();
            JPanel panel2 = formatLabel(out);
            panel2.setBackground(new Color(105,105,105)); //<--
            bottomPanel.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(panel2,BorderLayout.LINE_END);
            right.setBackground(new Color(105,105,105)); //<---
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            bottomPanel.add(vertical,BorderLayout.PAGE_START);
            dout.writeUTF(out);
            tf.setText("");

            frame.repaint();
            frame.invalidate();
            frame.validate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(ae.getSource() == pc){
            JFileChooser fc = new JFileChooser();

            fc.setCurrentDirectory(new File("This PC"));

            int response = fc.showOpenDialog(null); // select file to open
            // int response = fc.showSaveDialog(null); // select file to save

            if(response == JFileChooser.APPROVE_OPTION){ // means response == 0(means file selected)

                File f = new File(fc.getSelectedFile().getAbsolutePath()); // So, the purpose of this line of code is to create a File object that represents the selected file in the JFileChooser. This can be useful for reading the contents of the file, moving it to another location, etc.
                System.out.println(f);
            }
        }
    }
}