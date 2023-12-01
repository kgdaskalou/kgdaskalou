import javax.swing.*;
import java.awt.event.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
public class MainScreen extends JFrame{
    public MainScreen() {
        JFrame frame= new JFrame();
        frame.setTitle("JFrame Based Contact Form");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel headingPanel = new JPanel();
        JLabel headingLabel = new JLabel("Contact Us Panel");
        headingPanel.add(headingLabel);
        JPanel panel = new JPanel(new GridBagLayout());
// Constraints for the layout
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);
        constr.anchor = GridBagConstraints.WEST;
// Setting initial grid values to 0,0
        constr.gridx=0;
        constr.gridy=0;
        JLabel nameLabel      = new JLabel("Enter your name :");
        JLabel emailLabel      = new JLabel("Enter your email :");
        JLabel phoneLabel     = new JLabel("Enter your phone :");
        JLabel msgLabel        = new JLabel("Message :");
        JTextField nameInput           = new JTextField(20);
        JTextField emailInput           = new JTextField(20);
        JTextField phoneInput          = new JTextField(20);
        JTextArea textArea = new JTextArea(5, 20);
        panel.add(nameLabel, constr);
        constr.gridx=1;
        panel.add(nameInput, constr);
        constr.gridx=0; constr.gridy=1;
        panel.add(emailLabel, constr);
        constr.gridx=1;
        panel.add(emailInput, constr);
        constr.gridx=0; constr.gridy=2;
        panel.add(phoneLabel, constr);
        constr.gridx=1;
        panel.add(phoneInput, constr);
        constr.gridx=0; constr.gridy=3;
        panel.add(msgLabel, constr);
        constr.gridx=1;
        panel.add(textArea, constr);
        constr.gridx=0; constr.gridy=4;
        constr.gridwidth = 2;
        constr.anchor = GridBagConstraints.CENTER;
// Button with text "Register"
        JButton button = new JButton("Submit");
// add a listener to button
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                headingLabel.setText("Thanks for Contacting us. We'll get back to you shortly.");
                nameInput.setText("");
                emailInput.setText("");
                phoneInput.setText("");
                textArea.setText("");
            }
        });
        panel.add(button, constr);
        mainPanel.add(headingPanel);
        mainPanel.add(panel);
        frame.add(mainPanel);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
