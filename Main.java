import javax.swing.*;
public class Main {
    public static void main (String []args)
    {
        //create the canvas
        JFrame obj = new JFrame();
        obj.setBounds (10,10,700,600);
        obj.setTitle ("Brick Breaker");
        obj.setResizable (false);
        obj.setVisible (true);
        obj.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
        //set gameplay inside jframe canvas
        Gameplay gp = new Gameplay();
        obj.add (gp);
    }
}
