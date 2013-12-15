
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * @author 
 */
public class SbbView extends JFrame {
	//topPanel
	JToggleButton btnZugKnopf;
	JTextField fldSuche;
	JButton btnBack; 
	JButton btnForeward;
	//rightPanel
	JLabel lblUhrzeit;
	JTextField fldUhrzeit;
	JLabel lblRichtung;
	JTextField fldRichtung;
	JLabel lblFahrt;
	JTextField fldFahrt;
	JLabel lblGleis;
	JTextField fldGleis;
	JLabel lblUeber;
	JTextArea fldUeber;
	JButton btnEinfahrt; 
	JButton btnAusfahrt;
	JButton btnErsterEintrag; 
	//tbl
	JTable abfahrtsTable;
	//icon
	URL iconURL = getClass().getResource("/data/abb.gif");
	ImageIcon icon = new ImageIcon(iconURL);

	
	private final SbbModel model;
	private final SbbController controller;

	final AbstractTableModel tblmodel;
	
	public SbbView(SbbModel model, SbbController controller) {
		super("SBB app");
		this.model = model;
		this.controller = controller;
                this.tblmodel = new TSVFileAdapter(model);
	}

	public void createAndShow() {
		initializeComponents();
		JPanel contents = layoutComponents();
		addActionEvents();
		addPropertyChangeListeners();

		/*
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        System.out.println(info.getClassName());
		    	if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		*/
	try 
	    { 
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
	    } 
	    catch(Exception e){ 
	    }
		setTitle("Abfahrts Bahnhof Olten");
		setIconImage(icon.getImage());
		add(contents);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void initializeComponents() {
		final String fontName = "Arial";
		final Font defaultFont = new Font(fontName, Font.BOLD, 20);
		final Font LargeFont = new Font(fontName, Font.BOLD, 24);
		Font veryLargeFont = new Font(fontName, Font.BOLD, 38);
		//hint size not needed
		//int size = model.size();

		//JToggleButton btnZugKnopf = new JToggleButton(new ImageIcon(Window.class.getResource("zug.gif")));
		fldSuche = new JTextField("", 10);
		btnBack = new JButton("Zur�ck");
		btnBack.setFont(defaultFont);
		btnForeward = new JButton("Vorw�rts"); 
		btnForeward.setFont(defaultFont);
		
		lblUhrzeit = new JLabel("Uhrzeit");
		fldUhrzeit = new JTextField("-");
		lblRichtung = new JLabel("Richtung");
		fldRichtung = new JTextField("-");
		lblFahrt = new JLabel("Fahrt");
		fldFahrt = new JTextField("-");
		lblGleis = new JLabel("Gleis");
		fldGleis = new JTextField("-");
		lblUeber = new JLabel("�ber");
		fldUeber = new JTextArea("-");
		btnEinfahrt = new JButton("Fährt ein");
		btnAusfahrt = new JButton("Fährt aus");
		btnErsterEintrag = new JButton("Erster Eintrag auf Abfahrtstabel");
		
                btnZugKnopf = new JToggleButton("zug logo hier");
                
		abfahrtsTable = new JTable(tblmodel);
		abfahrtsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		
		
		ListSelectionModel listSelectionModel = abfahrtsTable.getSelectionModel();
                listSelectionModel.addListSelectionListener(
	                            new SharedListSelectionHandler(controller, model));
		//http://docs.oracle.com/javase/tutorial/uiswing/events/listselectionlistener.html
		//http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
		//http://docs.oracle.com/javase/tutorial/uiswing/events/api.html
	}

	private JPanel layoutComponents() {
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(btnZugKnopf); 
		topPanel.add(btnBack); 
		topPanel.add(btnForeward); 
		topPanel.add(fldSuche); 
		
		JScrollPane srollTable =  new JScrollPane(abfahrtsTable);
		srollTable.setMinimumSize(new Dimension(300,300));

		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.LIGHT_GRAY);
		rightPanel.setBorder(new EmptyBorder(5, 60, 5, 60));
		rightPanel.setLayout(new GridLayout(0,2));
		rightPanel.add(lblUhrzeit);
		rightPanel.add(fldUhrzeit);
		rightPanel.add(lblRichtung);
		rightPanel.add(fldRichtung);
		rightPanel.add(lblFahrt);
		rightPanel.add(fldFahrt);
		rightPanel.add(lblGleis);
		rightPanel.add(fldGleis);
		rightPanel.add(lblUeber);
		rightPanel.add(fldUeber);
		rightPanel.add(btnEinfahrt);
		rightPanel.add(btnAusfahrt);
		rightPanel.add(btnErsterEintrag);
		rightPanel.setMinimumSize(new Dimension(300,300));
		
		JPanel content = new JPanel(new BorderLayout());
		content.setSize(new Dimension(900,400));
		content.add(topPanel, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,srollTable, rightPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation((content.getWidth()/2));
		content.add(splitPane, BorderLayout.CENTER);

		return content;
	}

	
	
	
	

	private void addActionEvents() {
		fldUhrzeit.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
		    public void change() {
			    	 System.out.println(fldUhrzeit.getText() + "!!!!!!!!!!!");
                                 controller.setNewUhrzeit(fldUhrzeit.getText());
			 }
		});
		
		fldFahrt.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
		    public void change() {
			    	 System.out.println(fldFahrt.getText() + "!!!!!!!!!!!");
                                 controller.setNewFahrt(fldFahrt.getText());
			 }
		});  
                
                fldGleis.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
		    public void change() {
			    	 System.out.println(fldGleis.getText() + "!!!!!!!!!!!");
                                 controller.setNewGleis(fldGleis.getText());
			 }
		});  
                
                fldRichtung.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
		    public void change() {
			    	 System.out.println(fldRichtung.getText() + "!!!!!!!!!!!");
                                 controller.setNewRichtung(fldRichtung.getText());
			 }
		});  
                
                fldUeber.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
		    public void change() {
			    	 System.out.println(fldUeber.getText() + "!!!!!!!!!!!");
                                 controller.setNewUeber(fldUeber.getText());
			 }
		});  
                
                
		btnAusfahrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Ausfahrt dedr�ckt");
				//controller.startNewGame();
			}
		});
                
                btnEinfahrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("einfahrt dedr�ckt");
				controller.setStatusBlink();
			}
		});
                
                btnErsterEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("erster eintrag dedr�ckt");
				//controller.startNewGame();
			}
		});
                btnZugKnopf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("departure board dedr�ckt");
				//controller.startNewGame();
			}
		});
                btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Zurück dedr�ckt");
				//controller.startNewGame();
			}
		});
                
                btnForeward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Forwärts dedr�ckt");
				//controller.startNewGame();
			}
		});
	}


	private void addPropertyChangeListeners() {
		model.addPropertyChangeListener(SbbModel.SELECTED_DEPARTURE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//hint: eigentlich sollte diese pr�fung nicht n�tig sein sie ist es jedoch
				if(evt.getOldValue() != evt.getNewValue()){
					System.out.println(evt.toString());
					System.out.println("XXX Schuss ist eingetroffen");
					System.out.println(model.getSelectedDeparture().getGleis().toString());
					fldUhrzeit.setText(model.getSelectedDeparture().getDepartureTime());
                                        //abfahrtsTable.getSelectionModel()
           
                                        //((AbstractTableModel)abfahrtsTable.getModel()).fireTableCellUpdated(WIDTH, WIDTH);
                                         //tblmodel.fireTableCellUpdated(WIDTH, WIDTH);
                                        
                                        //tblmodel.setValueAt("XXXXX", 1, 1/*abfahrtsTable.getSelectedColumn()*/);
					//tblmodel.setValueAt(model.getSelectedDeparture().getDepartureTime(), abfahrtsTable.getSelectedRow(), 1/*abfahrtsTable.getSelectedColumn()*/);
                                        fldRichtung.setText(model.getSelectedDeparture().getDestination());
					fldGleis.setText(model.getSelectedDeparture().getGleis().toString());
					fldUeber.setText(model.getSelectedDeparture().getUeber().toString());
                                        fldFahrt.setText(model.getSelectedDeparture().getTrain());
				}else{
					System.out.println("Schuss ist unver�ndert");
				}
				/*for (int row = 0; row < buttons.length; row++) {
					for (int col = 0; col < buttons[row].length; col++) {
						final boolean isEmptyField = model.isEmpty(row, col);
						String newText = isEmptyField ? "" : Integer.toString(model.getValue(row, col));
						buttons[row][col].setText(newText);
						buttons[row][col].setEnabled(!model.isFinished() && !isEmptyField);
					}
				}*/
			}
		});

                
                //giving it to all the departuress
                PropertyChangeListener myListener = new MyPropertyChangeListener();
                List<Departure> xx = model.getAllDepartures();
                for(Departure x : xx){
                    x.addPropertyChangeListener(Departure.SELECTED_DEPARTURE_GLEIS, myListener);
                    x.addPropertyChangeListener(Departure.SELECTED_DEPARTURE_STATUS, myListener);
                }
                
                //hint: voll krass nur für index 0 alle anderen werden dynamisch hinugefüt sobald der index wächselt
               /* model.getSelectedDeparture().addPropertyChangeListener(Departure.SELECTED_DEPARTURE_GLEIS, new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                                System.out.println(evt);
                                System.out.println("jetzt");
                                //tblmodel.fireTableDataChanged();
                        }

                    @Override
                    public String toString() {
                        String me = "ich bin es ";
                        return me;
                    }

                });*/

	}

	
}

class MyPropertyChangeListener implements PropertyChangeListener{
        @Override
         public void propertyChange(PropertyChangeEvent evt) {
                 System.out.println(evt);
                 System.out.println("----------------------------");
                 //tblmodel.fireTableDataChanged();
         }

         @Override
         public String toString() {
             String me = "ich bin es ";
             return me;
         }
}

class SharedListSelectionHandler implements ListSelectionListener {
	SbbController controller;
        SbbModel model;
	public SharedListSelectionHandler(SbbController controller, SbbModel model) {
		this.controller = controller;
                this.model = model;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();

        int firstIndex = e.getFirstIndex();
        int lastIndex = e.getLastIndex();
        boolean isAdjusting = e.getValueIsAdjusting();
        /*
        System.out.println("Event for indexes "
                      + firstIndex + " - " + lastIndex
                      + "; isAdjusting is " + isAdjusting
                      + "; selected indexes:");
        */
        
        
        if (lsm.isSelectionEmpty()) {
            System.out.println(" <none>");
        } else {
            // Find out which indexes are selected. only one because table is configured to only allow one selection
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                	System.out.println("Index = " + i);
                }
            }
        }
        
        System.out.println();System.out.println();System.out.println();System.out.println();System.out.println("klick");
        controller.setSelectedDeparture(lsm.getMinSelectionIndex());
        //hint: voll krass
        //model.getSelectedDeparture().addPropertyChangeListener(Departure.SELECTED_DEPARTURE_GLEIS, new MyPropertyChangeListener());
    }
}
