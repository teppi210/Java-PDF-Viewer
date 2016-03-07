package ge.vakho.pdf_viewer;

import java.awt.Component;
import java.awt.Image;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class PdfListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	private List<Image> pageImages;

	public PdfListCellRenderer(List<Image> pageImages) {
		this.pageImages = pageImages;		
	}

	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon(new ImageIcon(pageImages.get(index)));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        
        return label;
	}

}
