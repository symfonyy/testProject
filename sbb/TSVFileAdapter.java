import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;


public class TSVFileAdapter extends AbstractTableModel {
        private final SbbModel model;
	
	
	TSVFileAdapter(SbbModel model) {
                this.model = model;
	}

        @Override
        public boolean isCellEditable(int row, int column) {
           //all cells false
           return true;
        }
        
	@Override
	public int getRowCount() {
		return model.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return model.getTotalColumnCount();
	}

	@Override
	public String getColumnName(int column) {
		return model.getTitelInkVirtualAt(column);
	}

	@Override
	public Object getValueAt(int row, int column) {
		//return model.getValueAt(row, column);
                return model.getDepartureValueAt(row, column);
	}

    @Override
    public void fireTableCellUpdated(int row, int column) {
        super.fireTableCellUpdated(row, column); //To change body of generated methods, choose Tools | Templates.
    }

    
}