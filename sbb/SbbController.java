
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.SwingUtilities;

/**
 * @author 
 */
public class SbbController {
	private final SbbModel model;
        
	private final Deque<ICommand> undoStack = new ArrayDeque<>();
	private final Deque<ICommand> redoStack = new ArrayDeque<>();
        
	public SbbController(SbbModel model) {
		this.model = model;

	}

	public void undo() {
		if (undoStack.isEmpty()) {
			return;
		}
		ICommand cmd = undoStack.pop();
		redoStack.push(cmd);
		setUndoRedoStatus();

		cmd.undo();
	}

	public void redo() {
		if (redoStack.isEmpty()) {
			return;
		}
		ICommand cmd = redoStack.pop();
		undoStack.push(cmd);
		setUndoRedoStatus();

		cmd.execute();
	}        

	private void execute(ICommand cmd) {
		undoStack.push(cmd);
		redoStack.clear();
		setUndoRedoStatus();
		cmd.execute();
	}

	private void setUndoRedoStatus() {
		model.setRedoAvailable(!redoStack.isEmpty());
		model.setUndoAvailable(!undoStack.isEmpty());
	}    
        
        
        
        /* muss noch angepasst werden eine mehtode für jedes änderbare eigenschaft
	private void setCounter(int newValue) {
		if(model.getCounter() != newValue){
			execute(new SetCounterCommand(model, newValue));
		}
	}        
        */
	public void initializeView() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SbbView view = new SbbView(model, SbbController.this);
				view.createAndShow();
			}
		});
                //hint: not sure if this is the right place
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        model.saveToFile();
                    }
                }));
	}

	
	public void setSelectedDeparture(int selectedRow){
		System.out.println("model wird von controller ge�ndert");
		model.setSelectedDeparture(model.getDeparture(selectedRow));
	}

        public void setNewUhrzeit(String uhrzeit){
		System.out.println("model(Uhrzeit) wird von controller ge�ndert");
		model.getSelectedDeparture().setDepartureTime(uhrzeit);
	}

        public void setNewFahrt(String fahrt){
		System.out.println("model(fahrt) wird von controller ge�ndert?????????????");
		model.getSelectedDeparture().setTrain(fahrt);
	}        
        
        public void setNewGleis(String gleis){
		System.out.println("model(gleis) wird von controller ge�ndert");
		model.getSelectedDeparture().setGleis(gleis);
	} 
        
        public void setNewRichtung(String direction){
		System.out.println("model(direction) wird von controller ge�ndert");
		model.getSelectedDeparture().setDestination(direction);
	} 
        
        public void setNewUeber(String ueber){
		System.out.println("model(ueber) wird von controller ge�ndert");
		model.getSelectedDeparture().setUeber(ueber);
	}          
        
        public void setStatusBlink(){
		System.out.println("model(status) wird zu Blinkend(im Bahnhof) ge�ndert");
		model.getSelectedDeparture().setStatus("Im Bahnhof");
	}         
}
