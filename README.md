# Java-PDF-Viewer

This is a Java project that uses swinglabs's [pdf-renderer](https://java.net/projects/pdf-renderer) to render PDF file's content on a swing component.  

### Important Note
When rendering PDF document on a JFrame (or some other GUI element), it is important to write these commands in the following order:
```java
	PagePanel panel = new PagePanel();
	
	// Some code before this ...
	frame.setVisible(true);
	panel.showPage(page);
	
	// Some code after this
```
