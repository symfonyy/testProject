import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.Timer;

/**
 * @author 
 */
public class SbbModel implements Observable {
	//hint: not sure if both needed
        public static final String SELECTED_DEPARTURE = "selectedDeparture";
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        //hint: not sure if need to move to departure
	private int counter = 0;
	private boolean isInputValid = true;
	private boolean isUndoAvailable = false;
	private boolean isRedoAvailable = false;
        //hint not sure what for
        private boolean modified = false;
        
	//own
	private List<Departure> departureList = null;
	private Departure selectedDeparture = null;
      
        //file infos
        private final Pattern TAB_PATTERN = Pattern.compile(";");
	final URL resource = TSVFileAdapter.class.getResource("data/olten.txt");
	private final File file = new File(URLDecoder.decode(resource.getFile()));
        private final int rowCount;
	private final int colCount;
        private int currentRow = -1; //hint: later not needed
        private String currentLine = null; //hint: later not needed
        private List<String> titelList;
        private List<String> virtualTitelList = new ArrayList<>();
        
        //initializing the list and fill it with data form the list
	public SbbModel() {
		rowCount = countRows();
                colCount = countColumns();
                titelList = getTitlesFromFile();
		departureList = getDeparturesFromFile();
                if(departureList != null){
                    setSelectedDeparture(departureList.get(0));
                }
                //add the status
                virtualTitelList.add("Status");
	}
	
        public List<String> getTitlesFromFile(){
            List<String> tmpTitles= new ArrayList<>();
            for(int i=0;i<colCount;i++){
                tmpTitles.add(getColValue(getLine(0), i));
             }
            return tmpTitles;
        }	
        
        public String getTitelInkVirtualAt(int nr){           
            List<String> tmpList = new ArrayList<>();
            tmpList.addAll(titelList);
            tmpList.addAll(virtualTitelList);
            return tmpList.get(nr);
        }
        
        public String getDepartureValueAt(int r, int c){
            Departure lineDeparture = departureList.get(r);
            //hint hier verbesserungsprotential siehe angefangen unten
            /*Field[] AllProperties;
            AllProperties = getClass().getDeclaredFields();
            for(Field x : AllProperties){
                x.getName();
            }*/
            //System.out.print(c + "  /  ");
            switch(c){
                    case 0:
                            return lineDeparture.getDepartureTime();
                    case 1:
                            return lineDeparture.getTrain();
                    case 2:
                            return lineDeparture.getDestination();
                    case 3:
                            return lineDeparture.getUeber();
                    case 4:
                            return lineDeparture.getGleis();
                    case 5://hint status nachträglich eingeführt auch noch gefärhrlich
                            return lineDeparture.getStatus();    
            }
            String failure ="not yetdeclared statement";
            return failure;
        }
        
	private List<Departure> getDeparturesFromFile(){	
		int anzRows = this.getRowCount();
		int anzColumn = this.getColumnCount();
                List<Departure> tmpList = new ArrayList<>();
		for(int r=0;r<anzRows;r++){
			Departure lineDeparture = new Departure();
			for(int c=0;c<anzColumn;c++){
				//System.out.print(fileAdapter.getValueAt(r, c));
				//hier unsicher da kopplung an adapter resp. file mit diesen werten.
				//System.out.println(c + "--");
				switch(c){
					case 0:
						lineDeparture.setDepartureTime(this.getValueAt(r, c).toString());
					//	System.out.println("Time: "+lineDeparture.getDepartureTime());
						break;
					case 1:
						lineDeparture.setTrain(this.getValueAt(r, c).toString());
					//	System.out.println("Train: "+lineDeparture.getTrain());
						break;
					case 2:
						lineDeparture.setDestination(this.getValueAt(r, c).toString());
					//	System.out.println("Destination: "+lineDeparture.getDestination());
						break;
					case 3:
						lineDeparture.setUeber(this.getValueAt(r, c).toString());
					//	System.out.println("Ueber: "+lineDeparture.getUeber());
						break;
					case 4:
						lineDeparture.setGleis(this.getValueAt(r, c).toString());
					//	System.out.println("Gleis: "+lineDeparture.getGleis());
						break;
				}
				
			}
			tmpList.add(lineDeparture);
			//System.out.println();
		}
		
		
		return tmpList;
	}
	
	public List<Departure> getAllDepartures(){
		return departureList;
	}
	
	public Departure getSelectedDeparture(){
		return selectedDeparture;
	}
	
	public boolean isModified(){
		return modified;
	}
	
	public boolean isUndoAvailable(){
		return false;
	}
	
	public Departure getDeparture(int index){
		return departureList.get(index);
	}
	
	public void setSelectedDeparture(Departure selectedDeparture) {
		System.out.println("---> schuss" + selectedDeparture.toString(" "));
		pcs.firePropertyChange(SELECTED_DEPARTURE, this.selectedDeparture, this.selectedDeparture = selectedDeparture);
	} 
        
	//nicht sicher ob das so richtig oder ob eigene eigenschaft des models
	public void setDepartureTime(String newTime){
		selectedDeparture.setDepartureTime(newTime);
                this.setSelectedDeparture(selectedDeparture);
	}
	
	public boolean isRedoAvailable() {
		return isRedoAvailable;
	}

	public void setRedoAvailable(boolean redoAvailable) {
		if (isRedoAvailable == redoAvailable) {
			return;
		}
		isRedoAvailable = redoAvailable;
		//hinter ändern auf ps.fire
                //notifyObservers();
	}
        
	public void setUndoAvailable(boolean undoAvailable) {
		if (isUndoAvailable == undoAvailable) {
			return;
		}
		isUndoAvailable = undoAvailable;
		//hinter ändern auf ps.fire
                //notifyObservers();
	}        

        
	private Object getProperty(String propertyName) {
		try {
			final Field field = getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			return field.get(this);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("can't access property " + propertyName + " in class " + getClass().getName());
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("unknown field " + propertyName + " in class " + getClass().getName());
		}
	}

	private Object getValue(Field field) {
		field.setAccessible(true);
		try {
			return field.get(this);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("can't access value of field " + field + " in class " + getClass().getName());
		}
	}
        
 	private void setValue(Field field, Object newValue) {
		field.setAccessible(true);
		try {
			field.set(this, newValue);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("can't access value of field " + field + " in class " + getClass().getName());
		}
	}       
        
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
		final Object value = getProperty(propertyName);
		listener.propertyChange(new PropertyChangeEvent(this, propertyName,
				value, value));
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return pcs.getPropertyChangeListeners();
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		return pcs.getPropertyChangeListeners(propertyName);
	}
        
        
        
        //File methods
	private int countRows() {
		FileReader in = null;
		try {
			in = new FileReader(file);
			final LineNumberReader reader = new LineNumberReader(in);
			while (reader.readLine() != null) {
			}
			return reader.getLineNumber() - 1;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	private int countColumns() {
		final Scanner scanner = new Scanner(getLine(0));
		scanner.useDelimiter(TAB_PATTERN);
		int counter = 0;
		while (scanner.hasNext()) {
			scanner.next();
			counter++;
		}
		return counter;
	}        
        
	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return colCount;
	}        
        
	public Object getValueAt(int row, int column) {
		return getColValue(getLine(row + 1), column);
	}
        
	private String getColValue(String line, int column) {
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(TAB_PATTERN);

		String value = null;
		for (int i = -1; i < column; i++) {
			value = scanner.next();
		}

		return value;
	}
        
        private String getLine(int row) {
		if (currentRow == row) {
			return currentLine;
		}

		final long start = System.nanoTime();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			for (int i = 0; i < row; i++) {
				reader.readLine();
				//System.out.println(reader.readLine());
			}
			currentLine = reader.readLine();
			currentRow = row;
			return currentLine;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignored) {
			}
			//System.out.println("read line for row " + row + " took :" + (System.nanoTime() - start));
		}
	}

    void saveToFile() {
        BufferedWriter writer = null;
        String titelLine = "";
        try {
                file.delete();
                file.createNewFile();
                for(String titel : titelList){
                    titelLine += titel + TAB_PATTERN.toString();
                }
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                writer.write(titelLine);
                writer.newLine();
                for(Departure currentDeparture : departureList){
                    writer.write(currentDeparture.toString(TAB_PATTERN.toString()));
                    writer.newLine();
                }
        } catch (IOException e) {
                throw new IllegalStateException(e);
        } finally {
                try {
                        if (writer != null) {
                                writer.close();
                        }
                } catch (IOException ignored) {
                }
                //System.out.println("read line for row " + row + " took :" + (System.nanoTime() - start));
        }
    }

    int getTotalColumnCount() {
        //hint: hier evtl. prüfen ob nicht mehr als eigenschaften vorhanden
        return (virtualTitelList.size()+colCount);
    }
}


