import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.event.*;

import java.awt.*;
import javax.swing.event.*;

//import java.io.*;
//import java.util.Scanner;
import java.util.ArrayList;

public final class GripACube extends javax.swing.JFrame
{
	private static final long serialVersionUID = 1L;
	// Variable declarations
	private JButton[][] facelet = new JButton[6][9];
	private final JButton[] colorSel = new JButton[6];
	private final int FSIZE = 45;
	private final int[] XOFF = { 3, 6, 3, 3, 0, 9 }; // Offsets for facelet display
	private final int[] YOFF = { 0, 3, 3, 6, 3, 3 };
	private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.black, Color.blue};

	private JCheckBox checkBoxShowAll;
	private JCheckBox checkBoxShowOpt;

	private JButton Solve;

	private JLabel jLabel1; 		// metric chooser
	private JSpinner spinnerMetric;

	private JLabel jLabel2;			// move restriction
	private JTextField restField;

	private JLabel jLabel3;			// moves you don't care about starting/ending with
	private JTextField idcField;

	private JLabel jLabelNo;
	private JTextArea noSortArea;	// display unsorted stuff

	private JLabel jLabelYa;
	private JTextArea yaSortArea;	// display   sorted stuff

	private JLabel jLabelStatus;	//that thing below the colors.

	private Color curCol = COLORS[0];
	private String metric = "f";

	boolean showAll = false;
	boolean showOpt = true;

	int octalMoves = 777;

	public ArrayList<String> solutionList = new ArrayList<String>();

	private static int[] edges = {7, 19, 5, 10, 1, 46, 3, 37, 28, 25, 32, 16, 34, 52, 30, 43, 23, 12, 21, 41, 48, 14,  50, 39};
	private static int[] corners = {8, 20, 9, 2, 11, 45, 0, 47, 36, 6, 38, 18, 29, 15, 26, 27, 24, 44, 33, 42, 53, 35, 51, 17};

	// ++++ Main class ++++
	public static void main(String[] args)
		{SwingUtilities.invokeLater(new Runnable()
			{public void run()
				{GripACube inst = new GripACube();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);}});}

	// ++++ Run the program ++++
	public GripACube()
		{super();
		initGUI();}

	// ++++ Start the GUI ++++
	private void initGUI() {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Grip-a-Cube!");

	// ++++ Set up Solve Cube Button ++++
		Solve = new JButton("Solve Cube");
		getContentPane().add(Solve);
		Solve.setBounds(422, 64, 114, 48);
		Solve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt)
			{solveCube(evt);}});				//see bottom event

	// ++++ Set up Metric Spinner ++++
		{	jLabel1 = new JLabel();				//Label
			getContentPane().add(jLabel1);
			jLabel1.setText("Metric");
			jLabel1.setBounds(282, 65, 72, 16);
		}{										//Quarter, face, and slice-turn metrics
			SpinnerListModel model = new SpinnerListModel(new String[]{"q", "f", "s"});
			spinnerMetric = new JSpinner(model);
			getContentPane().add(spinnerMetric);
			spinnerMetric.setBounds(354, 62, 56, 21);
			spinnerMetric.getEditor().setPreferredSize(new java.awt.Dimension(37, 19));
			spinnerMetric.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					metric = (String) spinnerMetric.getValue();}});}

	// ++++ Set up Move Restriction ++++
		{	jLabel2 = new JLabel();
			getContentPane().add(jLabel2);
			jLabel2.setText("Restrictions");
			jLabel2.setBounds(10, 15, 100, 16);}
		{	restField = new JTextField(21);
			restField.setText("RUDFBLMSE");
			getContentPane().add(restField);
			restField.setBounds(10, 30, 100, 16);}

	// ++++ Set up moves you don't care about ++++
		{	jLabel3 = new JLabel();
			getContentPane().add(jLabel3);
			jLabel3.setText("Pre/post ignore");
			jLabel3.setBounds(10, 55, 100, 16);}
		{	idcField = new JTextField(21);
			idcField.setText("");
			getContentPane().add(idcField);
			idcField.setBounds(10, 70, 100, 16);}

	// ++++ Set up Use ShowAll CheckBox ++++
		{	checkBoxShowAll = new JCheckBox("Show all", false);
			getContentPane().add(checkBoxShowAll);
			checkBoxShowAll.setBounds(12, 320, 121, 20);
			checkBoxShowAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					showAll = checkBoxShowAll.isSelected();}});}
	// ++++ Set up Use ShowOpt CheckBox ++++
		{	checkBoxShowOpt = new JCheckBox("Show optimal", true);
			getContentPane().add(checkBoxShowOpt);
			checkBoxShowOpt.setBounds(12, 343, 121, 20);
			checkBoxShowOpt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					showOpt = checkBoxShowOpt.isSelected();}});}

	// ++++ Set up display TextAreas ++++
		{	jLabelNo = new JLabel();
			getContentPane().add(jLabelNo);
			jLabelNo.setText("Unsorted algs:");
			jLabelNo.setBounds(10, 435, 100, 16);}
		{	noSortArea = new JTextArea("no algs yet!", 220, 100);
			noSortArea.setLineWrap(true);
			getContentPane().add(noSortArea);
			noSortArea.setBounds(10, 450, 200, 100);}

		{	jLabelYa = new JLabel();
			getContentPane().add(jLabelYa);
			jLabelYa.setText("Sorted algs:");
			jLabelYa.setBounds(260, 435, 100, 16);}
		{	yaSortArea = new JTextArea("no algs yet!", 220, 100);
			yaSortArea.setLineWrap(true);
			getContentPane().add(yaSortArea);
			yaSortArea.setBounds(260, 450, 200, 100);}

	// ++++ Set up status label ++++
		{	jLabelStatus = new JLabel();
			getContentPane().add(jLabelStatus);
			jLabelStatus.setText("Status:");
			jLabelStatus.setBounds(315, 315, 100, 48);}

	// ++++ Set up editable facelets ++++
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++) {
				facelet[i][j] = new JButton();
				getContentPane().add(facelet[i][j]);
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
				facelet[i][j].setOpaque(true);
				facelet[i][j].setBounds(FSIZE * XOFF[i] + FSIZE * (j % 3), FSIZE * YOFF[i] + FSIZE * (j / 3), FSIZE, FSIZE);
				facelet[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						((JButton) evt.getSource()).setBackground(curCol);}});}

		String[] txt = { "U", "R", "F", "D", "L", "B" };
		for (int i = 0; i < 6; i++)
			facelet[i][4].setText(txt[i]);
		for (int i = 0; i < 6; i++) {
			colorSel[i] = new JButton();
			getContentPane().add(colorSel[i]);
			colorSel[i].setBackground(COLORS[i]);
			colorSel[i].setOpaque(true);
			colorSel[i].setBounds(FSIZE * (XOFF[1] + 1) + FSIZE / 4 * 3 * i, FSIZE * (YOFF[3] + 1)-40, FSIZE / 4 * 3,
					FSIZE / 4 * 3 );
			colorSel[i].setName("" + i);
			colorSel[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					curCol = COLORS[Integer.parseInt(((JButton) evt.getSource()).getName())];}});}
		pack();
		this.setSize(556, 600);}

	// ++++++++++++++++++++++++ End initGUI ++++++++++++++++++++++++

	//////////////// Generate cube from GUI-Input and solve it ////////////////
		private void solveCube(ActionEvent evt) {

			StringBuffer com = new StringBuffer("");	//will eventually be useful! :p
			////// Set up options //////
			com.append(metric);					//should be q, f, or s
			if(showAll){com.append("a");}		//for searching all solutions with the current shortest length
			if(showOpt){com.append("o");}		//for searching only for optimal sequences in the given metrics

			String command = com.toString();
			String[] c2 = command.split(" ");
			Options options = options(c2);

			////// Set up input //////
			String cubeString = getInput();

			////// Set up leading/trailing moves //////
			String asdf = "";
			if(idcField.getText().length() > 0){
				asdf = "~" + idcField.getText() + " ";}

			String octalString = "777 ";
			////// Set up move restrictions //////
			if(restField.getText().length()>0)
				{octalString = getOctal(restField.getText());}


			StringBuilder changeTo = new StringBuilder(asdf + octalString);
			/////// Credits to Frank Dumont for this: ///////

	        for (int i = 0; i < 12; i++) {
	            changeTo.append(cubeString.charAt(edges[2 * i]));
	            changeTo.append(cubeString.charAt(edges[2 * i + 1]));
	            changeTo.append(' ');}

	        for (int i = 0; i < 8; i++) {
	            for (int j = 0; j < 3; j++) {
	                changeTo.append(cubeString.charAt(corners[3 * i + j]));}
	            changeTo.append(' ');}

	        String aCubeString = changeTo.toString();

	        ////// Set up CubeReader //////
	        CubeReader cubeReader = new CubeReader();

	        ///// Go solve that cube! /////
		    String r = cubeReader.init(options, aCubeString);

		    if (r == "OK") {
		    	jLabelStatus.setText("Solving!");
		        cubeReader.solve(solutionList);
		        int n = solutionList.size();
		        String aS = "";
		        for(int i = 0; i < n ; i++){
		       		aS += solutionList.get(i) + "\n";}
		        noSortArea.setText(aS);
		        JOptionPane.showMessageDialog(null,aS);
		        
		        JTextArea noSortArea;
		        noSortArea = new JTextArea(aS, 180, 100);
		        noSortArea.setBounds(100, 100, 400, 120);
		        
		        JPanel outputPanel = new JPanel();
		        outputPanel.add( noSortArea );
		        outputPanel.setBounds(100, 100, 400, 120);
		        
		        JDialog outputDialog = new JDialog();
		        outputDialog.add( outputPanel );
		        outputDialog.setVisible( true );
		        	noSortArea.setText(aS);
		        
		        outputDialog.pack();
		        outputDialog.setSize(400, 120);
		    }
		    else {
		      	JOptionPane.showMessageDialog(null, aCubeString + "\nError: " + r + ".", "alert", JOptionPane.ERROR_MESSAGE);}}

		////// Set the metric options //////
		private Options options(String[] args) {
	    Options options = new Options();
	    options.metric = Turn.FACE_METRIC;
	    options.findAll = false;
	    options.findOptimal = false;
	    String whatsUp = "";
	    for (int i = 0; i < args.length; i++) {
	      for (int j = 0; j < args[i].length(); j++) {
	        switch (args[i].charAt(j)) {
	         case 'a':
	          options.findAll = true;
	          whatsUp += "All search ON.\n";
	          break;
	         case 'o':
	          options.findOptimal = true;
	          whatsUp += "Optimal search ON.\n";
	          break;
	         case 'q':
	          options.metric = Turn.QUARTER_METRIC;
	          whatsUp += "Quarter-turn metrics ON.\n";
	          break;
	         case 'f':
	          options.metric = Turn.FACE_METRIC;
	          whatsUp += "Face-turn metrics ON.\n";
	          break;
	         case 's':
	          options.metric = Turn.SLICE_METRIC;
	          whatsUp += "Slice-turn metrics ON.\n";
	          break;
	         default:
	          whatsUp += "Unknown option: ";
	          whatsUp += args[i].charAt(j);
	          whatsUp += "\nBAD!\n";
	        }}}
	    jLabelStatus.setText(whatsUp);

	    return options;}

		////// Get the octal move restrictions //////
	    private String getOctal(String x) {
  			int o1 = 0, o2 = 0;
  			int length = x.length();

  			for(int i = 0; i < length; i++){
    			int oct = moveOct(x.charAt(i));
    			if((i+1 < length) && x.charAt(i+1) == '2'){
      				o2 += oct;
      				i++;
      				continue;}
    		o1 += oct;}

  			String output = "";
  			if(o1 == 0 && o2 == 0){
  				return String.format("777 ", Integer.parseInt(output));
  				}
  			else {
   				output = Integer.toString(o1, 8);
   				if(o2 != 0){
     				output += Integer.toString(o2, 8);
     				return String.format("%06d ", Integer.parseInt(output));}
     			else{
     				return String.format("%03d ", Integer.parseInt(output));}}}

		// value of each of these guys
	  	private static int moveOct(char in){
			switch(in){
				case 'M': return 1;
				case 'S': return 2;
			  	case 'E': return 4;
			  	case 'R': return 8;
			  	case 'L': return 16;
			  	case 'B': return 32;
			  	case 'F': return 64;
			  	case 'D': return 128;
			  	case 'U': return 256;}
			return 0;}

		////// Get the input string //////
		private String getInput() {
			StringBuffer s1 = new StringBuffer(54);

			for (int i = 0; i < 54; i++)
					s1.insert(i, 'B');// default initialization

			for (int i = 0; i < 6; i++)
					// read the 54 facelets
				for (int j = 0; j < 9; j++) {
					if (facelet[i][j].getBackground() == facelet[0][4].getBackground())
							s1.setCharAt(9 * i + j, 'U');
					if (facelet[i][j].getBackground() == facelet[1][4].getBackground())
							s1.setCharAt(9 * i + j, 'R');
					if (facelet[i][j].getBackground() == facelet[2][4].getBackground())
							s1.setCharAt(9 * i + j, 'F');
					if (facelet[i][j].getBackground() == facelet[3][4].getBackground())
							s1.setCharAt(9 * i + j, 'D');
					if (facelet[i][j].getBackground() == facelet[4][4].getBackground())
							s1.setCharAt(9 * i + j, 'L');
					if (facelet[i][j].getBackground() == facelet[5][4].getBackground())
							s1.setCharAt(9 * i + j, 'B');}

			return s1.toString();}
}
