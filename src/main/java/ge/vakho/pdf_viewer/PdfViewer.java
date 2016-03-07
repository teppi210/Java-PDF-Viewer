package ge.vakho.pdf_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PdfViewer {

	private JFrame frame;
	private List<Image> pageImages = new ArrayList<Image>();
	private PDFFile pdfFile;
	private PagePanel pagePanel;

	/**
	 * asdas Create the application.
	 * 
	 * @throws Exception
	 */
	public PdfViewer(File document) throws Exception {
		initialize(document);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param document
	 * @throws IOException
	 */
	private void initialize(File document) throws IOException {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double ratio = 0.8;
		Double height = screenSize.getHeight() * ratio;
		Double width = height * 0.7;

		//
		RandomAccessFile raf = new RandomAccessFile(document, "r");
		FileChannel fc = raf.getChannel();
		ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		try {
			pdfFile = new PDFFile(buf);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Can't preview the document!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		int numpages = pdfFile.getNumPages();
		Integer[] pageNums = new Integer[numpages];
		for (int i = 0; i < numpages; i++) {
			pageNums[i] = i + 1;
			PDFPage pg = pdfFile.getPage(i + 1);
			Rectangle2D r2d = pg.getBBox();
			Image image = pg.getImage((int) (width / 7), (int) (height / 7), r2d, null, true, true);
			pageImages.add(image);
		}
		System.out.println("Number of pages = " + numpages);
		PDFPage page = pdfFile.getPage(1);
		pagePanel = new PagePanel();
		pagePanel.setBorder(null);
		pagePanel.setBackground(Color.BLACK);
		//

		frame = new JFrame();
		frame.setType(Type.UTILITY);
		// frame.setBounds(100, 100, width.intValue(), height.intValue());
		frame.setTitle(document.getName());
		BorderLayout borderLayout = (BorderLayout) frame.getContentPane().getLayout();

		JPanel panelPreview = new JPanel();
		frame.getContentPane().add(panelPreview, BorderLayout.WEST);
		panelPreview.setLayout(new GridLayout(0, 1, 0, 0));
		panelPreview.setBorder(new EmptyBorder(10, 10, 10, 10));
		// panelPreview.setPreferredSize(new Dimension(100, height.intValue()));

		JList listPages = new JList(pageNums);
		listPages.setVisibleRowCount(1);
		// listPages.setFixedCellWidth((int) (width / 10));
		// listPages.setFixedCellHeight((int) (height / 10));
		// listPages.setBorder(new EmptyBorder(10, 10, 10, 10));
		listPages.setFont(new Font("Arial", Font.PLAIN, 11));
		listPages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPages.setCellRenderer(new PdfListCellRenderer(pageImages));
		listPages.setSelectedIndex(0); // First page is selected by default
		listPages.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {

				// Detect change once...
				if (e.getValueIsAdjusting()) {
					JList list = (JList) e.getSource();
					int selectedIndex = list.getSelectedIndex();
					PDFPage page = pdfFile.getPage(selectedIndex + 1);
					pagePanel.showPage(page);
					System.out.println(list.getSelectedIndex());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(listPages);
		panelPreview.add(scrollPane);
		frame.getContentPane().add(pagePanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pagePanel.setPreferredSize(new Dimension(width.intValue(), height.intValue()));
		pagePanel.setLayout(new GridLayout(1, 1, 0, 0));
		pagePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.pack();
		frame.setLocationRelativeTo(null); // Do after .pack()
		frame.setVisible(true);
		pagePanel.showPage(page);
	}
}
