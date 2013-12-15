
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import static java.lang.System.getProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author snd
 */
class Departure implements Observable{
	public static final String SELECTED_DEPARTURE_TIME = "departureTime";
        public static final String SELECTED_DEPARTURE_TRAIN = "train";
        public static final String SELECTED_DEPARTURE_DESTINATION = "destination";
        public static final String SELECTED_DEPARTURE_GLEIS = "gleis";
        public static final String SELECTED_DEPARTURE_UEBER = "ueber";	
        public static final String SELECTED_DEPARTURE_STATUS = "status";	
    
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        
        String departureTime;
	String train;
	String destination;
	String gleis;
	String ueber;
	String status = "hat Einfahrt";
        
        
	public Departure(String departureTime,String train,String destination,String gleis,String ueber) {
		this.departureTime=departureTime;
		this.train=train;
		this.destination=destination;
		this.gleis=gleis;
		this.ueber=ueber;
	}
	
	public Departure() {
		// TODO Auto-generated constructor stub
	}
        
        public Departure(Departure tmp) {
            setDepartureTime(tmp.getDepartureTime());
            setDestination(tmp.getDestination());
            setGleis(tmp.getGleis());
            setStatus(tmp.getStatus());
            setTrain(tmp.getTrain());
            setUeber(tmp.getUeber());
	}

	public String getTrain() {
		return train;
	}
	public void setTrain(String train) {
		pcs.firePropertyChange(SELECTED_DEPARTURE_TRAIN, train, this.train = train);
	}
	public String getUeber() {
		return ueber;
	}
	public void setUeber(String ueber) {
                pcs.firePropertyChange(SELECTED_DEPARTURE_UEBER, ueber, this.ueber = ueber);
	}
	public String getGleis() {
		return gleis;
	}
	public void setGleis(String gleis) {
                System.out.println("Gleis wird von Departure Model geändert ");
                /*PropertyChangeListener pcc[] = pcs.getPropertyChangeListeners();
                for(PropertyChangeListener pc : pcc){
                    System.out.println(pc.toString());
                }*/
		pcs.firePropertyChange(SELECTED_DEPARTURE_GLEIS, gleis, this.gleis = gleis);
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
                pcs.firePropertyChange(SELECTED_DEPARTURE_TIME, departureTime, this.departureTime = departureTime);
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		pcs.firePropertyChange(SELECTED_DEPARTURE_DESTINATION, destination, this.destination = destination);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
            pcs.firePropertyChange(SELECTED_DEPARTURE_STATUS, status, this.status = status);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		System.out.println("Listener wird zu " +listener.toString() + " hinzugefügt mit property " +  propertyName);
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

    public String toString(String delimiter) {
        //hint hier könnte ich von klasse die eigenschaften dynamisch holen und verbinden
        String eigenschaften = departureTime + delimiter + train + delimiter + destination + delimiter + ueber + delimiter + gleis + delimiter;
        return eigenschaften;
    } 
}
