package connectfour;

import javax.swing.*;
import java.awt.*;

public class GUI {
    public GUI(Grid grid) {
    	makeFrame(grid);
    }
    
    private void makeFrame(Grid grid) {
    	JFrame frame = new JFrame("Connect 4");
    	Container content = frame.getContentPane();
    	content.setLayout(new BorderLayout());
    	
    	content.add(grid);
    	
    	frame.setPreferredSize(new Dimension(900, 800));
    	frame.pack();
    	frame.setVisible(true);
    }
}
