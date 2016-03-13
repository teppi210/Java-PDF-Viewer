package ge.vakho.pdf_viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PageDrawerParameters;

public class PdfViewer {

	private JFrame frame;
	private MyPDFRenderer renderer;
	private JPanel panelSelectedPage;

	private int numberOfPages;
	private int currentPageIndex = 0;

	private int width;
	private int height;

	private JTextField txtPageNumber;
	private JButton btnLastPage;
	private JButton btnNextPage;
	private JButton btnPreviousPage;
	private JButton btnFirstPage;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new PdfViewer(new File("C:/Users/vakho/Desktop/cwguide-070313.pdf"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void enableDisableButtons(int actionIndex) {
		switch (actionIndex) {
		case 0:
			btnFirstPage.setEnabled(false);
			btnPreviousPage.setEnabled(false);
			btnNextPage.setEnabled(true);
			btnLastPage.setEnabled(true);
			break;
		case 1:
			btnFirstPage.setEnabled(true);
			btnPreviousPage.setEnabled(true);
			btnNextPage.setEnabled(false);
			btnLastPage.setEnabled(false);
			break;
		default:
			btnFirstPage.setEnabled(true);
			btnPreviousPage.setEnabled(true);
			btnNextPage.setEnabled(true);
			btnLastPage.setEnabled(true);
		}
	}

	public PdfViewer(File document) throws Exception {
		initialize(document);
	}

	private void selectPage(int pageIndex) {
		BufferedImage renderImage = null;

		try {
			renderImage = renderer.renderImage(pageIndex, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		panelSelectedPage.removeAll(); // Remove children		
		
		ImagePanel imagePanel = new ImagePanel(renderImage, width, height);
		imagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		imagePanel.setLayout(new CardLayout(0, 0));
		imagePanel.setPreferredSize(new Dimension(width, height));
		panelSelectedPage.add(imagePanel, BorderLayout.CENTER);
		currentPageIndex = pageIndex;

		String pageText = String.format("Page: %d / %d", pageIndex + 1, numberOfPages);
		txtPageNumber.setText(pageText);

		if (pageIndex == 0) {
			enableDisableButtons(0);
		} else if (pageIndex == (numberOfPages - 1)) {
			enableDisableButtons(1);
		} else {
			enableDisableButtons(-1);
		}

		panelSelectedPage.revalidate();
		panelSelectedPage.repaint();
		
		System.out.println(imagePanel.getPreferredSize().width + " : " + imagePanel.getPreferredSize().height);
	}

	private void initialize(File file) throws Exception {

		PDDocument doc = PDDocument.load(file);
		
		// Getting/calculating screen dimensions...
		Float realWidth = new Float(doc.getPage(0).getMediaBox().getWidth());
		Float realHeight = new Float(doc.getPage(0).getMediaBox().getHeight());

		System.out.println(realWidth + ", " + realHeight);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double ratio = 0.8;

		height = (int) (screenSize.getHeight() * ratio);
		width = (int) ((height * realWidth) / realHeight);

		numberOfPages = doc.getNumberOfPages();
		
		renderer = new MyPDFRenderer(doc);

		System.out.println("Number of pages = " + numberOfPages);

		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(file.getName());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelControls = new JPanel();
		frame.getContentPane().add(panelControls, BorderLayout.SOUTH);
		panelControls.setLayout(new BorderLayout(0, 0));

		Component verticalStrutTop = Box.createVerticalStrut(10);
		panelControls.add(verticalStrutTop, BorderLayout.NORTH);

		Box horizontalBoxControls = Box.createHorizontalBox();
		panelControls.add(horizontalBoxControls);

		Component horizontalStrutLeft = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutLeft);

		btnFirstPage = new JButton("First Page");
		btnFirstPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectPage(0);
			}
		});
		horizontalBoxControls.add(btnFirstPage);

		Component horizontalStrutLeft_1 = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutLeft_1);

		btnPreviousPage = new JButton("Previous Page");
		btnPreviousPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentPageIndex > 0) {
					selectPage(currentPageIndex - 1);
				}
			}
		});
		horizontalBoxControls.add(btnPreviousPage);

		Component horizontalStrutLeft_2 = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutLeft_2);

		txtPageNumber = new JTextField();
		horizontalBoxControls.add(txtPageNumber);
		txtPageNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPageNumber.setEditable(false);
		txtPageNumber.setPreferredSize(new Dimension(50, txtPageNumber.getPreferredSize().width));
		txtPageNumber.setColumns(10);

		Component horizontalStrutRight_2 = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutRight_2);

		btnNextPage = new JButton("Next Page");
		btnNextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentPageIndex < (numberOfPages - 1)) {
					selectPage(currentPageIndex + 1);
				}
			}
		});
		horizontalBoxControls.add(btnNextPage);

		Component horizontalStrutRight_1 = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutRight_1);

		btnLastPage = new JButton("Last Page");
		btnLastPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectPage(numberOfPages - 1);
			}
		});
		horizontalBoxControls.add(btnLastPage);

		Component horizontalStrutRight = Box.createHorizontalStrut(10);
		horizontalBoxControls.add(horizontalStrutRight);

		Component verticalStrutBottom = Box.createVerticalStrut(10);
		panelControls.add(verticalStrutBottom, BorderLayout.SOUTH);

		Box verticalBoxView = Box.createVerticalBox();
		frame.getContentPane().add(verticalBoxView, BorderLayout.WEST);

		Component verticalStrutView = Box.createVerticalStrut(10);
		verticalBoxView.add(verticalStrutView);

		Box horizontalBoxView = Box.createHorizontalBox();
		verticalBoxView.add(horizontalBoxView);

		Component horizontalStrutViewLeft = Box.createHorizontalStrut(10);
		horizontalBoxView.add(horizontalStrutViewLeft);

		panelSelectedPage = new JPanel();
		panelSelectedPage.setBackground(Color.LIGHT_GRAY);
		horizontalBoxView.add(panelSelectedPage);
		panelSelectedPage.setPreferredSize(new Dimension(width, height));
		panelSelectedPage.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelSelectedPage.setLayout(new BorderLayout(0, 0));

		Component horizontalStrutViewRight = Box.createHorizontalStrut(10);
		horizontalBoxView.add(horizontalStrutViewRight);

		selectPage(0);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.repaint();
	}
}
