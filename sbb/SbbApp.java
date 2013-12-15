
public class SbbApp {

	public static void main(String[] args) {
		final SbbModel SbbModel = new SbbModel();
		final SbbController SbbController = new SbbController(SbbModel);
		SbbController.initializeView();
	}
}  